package com.savvato.tribeapp.services;

import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.dto.ConnectIncomingMessageDTO;
import com.savvato.tribeapp.dto.ConnectOutgoingMessageDTO;
import com.savvato.tribeapp.entities.Connection;
import com.savvato.tribeapp.entities.RejectedNonEnglishWord;
import com.savvato.tribeapp.repositories.ConnectionsRepository;
import com.savvato.tribeapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
public class ConnectServiceImplTest extends AbstractServiceImplTest {
    @TestConfiguration
    static class ConnectServiceImplTestContextConfiguration {
        @Bean
        public ConnectService connectService() {
            return new ConnectServiceImpl();
        }

        @Bean
        public CacheService cacheService() {return new CacheServiceImpl();}
    }

    @Autowired
    ConnectService connectService;

    @MockBean
    CacheService cacheService;

    @MockBean
    ConnectionsRepository connectionsRepository;

    @MockBean
    SimpMessagingTemplate simpMessagingTemplate;

    @MockBean
    UserRepository userRepository;

    @Test
    public void getQRCodeString() {
        Long userId = 1L;
        Optional<String> qrCodeString = Optional.of("QR code");
        Mockito.when(cacheService.get(Mockito.any(), Mockito.any())).thenReturn(qrCodeString.get());
        Optional<String> rtn = connectService.getQRCodeString(userId);
        assertEquals(qrCodeString, rtn);
    }
    @Test
    public void storeQRCodeString() {
        Long userId = 1L;
        Optional<String> generatedQRCodeString = Optional.of("QR code");

        Optional<String> rtn = connectService.storeQRCodeString(userId);

        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);
        verify(cacheService, times(1)).put(arg1.capture(), arg2.capture(), Mockito.any());

        assertEquals(arg1.getValue(), "ConnectQRCodeString");
        assertEquals(arg2.getValue(), String.valueOf(userId));
    }

    @Test
    public void connectWhenQrCodeIsInvalid() {
        UserPrincipal user = new UserPrincipal(getUser1());
        Long requestingUserId = 1L;
        Long toBeConnectedWithUserId = 2L;
        String qrcodePhrase = "invalid code";
        String connectionIntent = "";
        String expectedDestination = "/connect/user/queue/specific-user";
        ConnectIncomingMessageDTO incoming = ConnectIncomingMessageDTO.builder().requestingUserId(requestingUserId).toBeConnectedWithUserId(toBeConnectedWithUserId).qrcodePhrase(qrcodePhrase).connectionIntent(connectionIntent).build();
        ConnectOutgoingMessageDTO outgoing = ConnectOutgoingMessageDTO.builder().connectionError(true).message("Invalid QR code; failed to connect.").build();
        ConnectService connectServiceSpy = spy(connectService);
        doReturn(false).when(connectServiceSpy).validateQRCode(Mockito.any(), Mockito.any());

        connectServiceSpy.connect(incoming, user);

        verify(connectServiceSpy, never()).handleConnectionIntent(Mockito.any(), Mockito.any(), Mockito.any());
        ArgumentCaptor<String> recipientArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> destinationArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ConnectOutgoingMessageDTO> outgoingMsgArg = ArgumentCaptor.forClass(ConnectOutgoingMessageDTO.class);

        verify(simpMessagingTemplate, times(1)).convertAndSendToUser(recipientArg.capture(), destinationArg.capture(), outgoingMsgArg.capture());
        assertEquals(recipientArg.getValue(), String.valueOf(toBeConnectedWithUserId));
        assertEquals(destinationArg.getValue(), expectedDestination);
        assertThat(outgoingMsgArg.getValue()).isEqualToComparingFieldByField(outgoing);
    }

    @Test
    public void connectWhenQrCodeIsValid() {
        UserPrincipal user = new UserPrincipal(getUser1());
        Long requestingUserId = 1L;
        Long toBeConnectedWithUserId = 2L;
        String connectionIntent = "";
        ArrayList<Long> recipients = new ArrayList<>(Arrays.asList(toBeConnectedWithUserId));
        String expectedDestination = "/connect/user/queue/specific-user";
        ConnectIncomingMessageDTO incoming = ConnectIncomingMessageDTO.builder().requestingUserId(requestingUserId).toBeConnectedWithUserId(toBeConnectedWithUserId).connectionIntent(connectionIntent).build();
        ConnectOutgoingMessageDTO outgoing = ConnectOutgoingMessageDTO.builder().message("Please confirm that you wish to connect.").to(recipients).build();
        ConnectService connectServiceSpy = spy(connectService);
        doReturn(true).when(connectServiceSpy).validateQRCode(Mockito.any(), Mockito.any());
        doReturn(outgoing).when(connectServiceSpy).handleConnectionIntent(Mockito.any(), Mockito.any(), Mockito.any());

        connectServiceSpy.connect(incoming, user);

        ArgumentCaptor<String> connectionIntentArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> requestingUserIdArg = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> toBeConnectedWithUserIdArg = ArgumentCaptor.forClass(Long.class);
        verify(connectServiceSpy, times(1)).handleConnectionIntent(connectionIntentArg.capture(), requestingUserIdArg.capture(), toBeConnectedWithUserIdArg.capture());
        assertEquals(connectionIntentArg.getValue(), connectionIntent);
        assertEquals(requestingUserIdArg.getValue(), requestingUserId);
        assertEquals(toBeConnectedWithUserIdArg.getValue(), toBeConnectedWithUserId);

        ArgumentCaptor<String> recipientArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> destinationArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ConnectOutgoingMessageDTO> outgoingMsgArg = ArgumentCaptor.forClass(ConnectOutgoingMessageDTO.class);
        verify(simpMessagingTemplate, times(1)).convertAndSendToUser(recipientArg.capture(), destinationArg.capture(), outgoingMsgArg.capture());
        assertEquals(recipientArg.getValue(), String.valueOf(outgoing.to.get(0)));
        assertEquals(destinationArg.getValue(), expectedDestination);
        assertThat(outgoingMsgArg.getValue()).isEqualToComparingFieldByField(outgoing);
    }

    @Test
    public void handleConnectionIntentWhenNoConnectionIntent() {
        Long requestingUserId = 1L;
        Long toBeConnectedWithUserId = 2L;
        String connectionIntent = "";
        ArrayList<Long> recipients = new ArrayList<>(Arrays.asList(toBeConnectedWithUserId));
        ConnectOutgoingMessageDTO expectedOutgoingMsg = ConnectOutgoingMessageDTO.builder().message("Please confirm that you wish to connect.").to(recipients).build();
        ConnectService connectServiceSpy = spy(connectService);
        ConnectOutgoingMessageDTO outgoing = connectServiceSpy.handleConnectionIntent(connectionIntent, requestingUserId, toBeConnectedWithUserId);

        assertThat(expectedOutgoingMsg.equals(outgoing));
        verify(connectServiceSpy, never()).saveConnectionDetails(Mockito.any(), Mockito.any());
        verify(connectionsRepository, never()).save(Mockito.any());
    }

    @Test
    public void handleConnectionIntentWhenConnectionIntentConfirmedAndDatabaseSaveSuccessful() {
        Long requestingUserId = 1L;
        Long toBeConnectedWithUserId = 2L;
        String connectionIntent = "confirmed";
        ArrayList<Long> recipients = new ArrayList<>(Arrays.asList(requestingUserId, toBeConnectedWithUserId));
        ConnectOutgoingMessageDTO expectedOutgoingMsg = ConnectOutgoingMessageDTO.builder().connectionSuccess(true).to(recipients).message("Successfully saved connection!").build();
        Connection connection = new Connection(requestingUserId, toBeConnectedWithUserId);
        Mockito.when(connectionsRepository.save(Mockito.any())).thenReturn(connection);
        ConnectOutgoingMessageDTO outgoing = connectService.handleConnectionIntent(connectionIntent, requestingUserId, toBeConnectedWithUserId);

        ArgumentCaptor<Connection> connectionArg = ArgumentCaptor.forClass(Connection.class);
        verify(connectionsRepository, times(1)).save(connectionArg.capture());
        assertEquals(connectionArg.getValue().getToBeConnectedWithUserId(), toBeConnectedWithUserId);
        assertEquals(connectionArg.getValue().getRequestingUserId(), requestingUserId);
        assertThat(expectedOutgoingMsg.equals(outgoing));
    }

    @Test
    public void handleConnectionIntentWhenConnectionIntentConfirmedAndDatabaseSaveUnsuccessful() {
        Long requestingUserId = 1L;
        Long toBeConnectedWithUserId = 2L;
        String connectionIntent = "confirmed";
        ArrayList<Long> recipients = new ArrayList<>(Arrays.asList(requestingUserId, toBeConnectedWithUserId));
        ConnectOutgoingMessageDTO expectedOutgoingMsg = ConnectOutgoingMessageDTO.builder().connectionSuccess(true).to(recipients).message("Successfully saved connection!").build();;
        Mockito.when(connectionsRepository.save(Mockito.any())).thenReturn(null);
        ConnectOutgoingMessageDTO outgoing = connectService.handleConnectionIntent(connectionIntent, requestingUserId, toBeConnectedWithUserId);

        ArgumentCaptor<Connection> connectionArg = ArgumentCaptor.forClass(Connection.class);
        verify(connectionsRepository, times(1)).save(connectionArg.capture());
        assertEquals(connectionArg.getValue().getToBeConnectedWithUserId(), toBeConnectedWithUserId);
        assertEquals(connectionArg.getValue().getRequestingUserId(), requestingUserId);
        assertThat(expectedOutgoingMsg.equals(outgoing));
    }

    @Test
    public void handleConnectionIntentWhenConnectionIntentDenied() {
        Long requestingUserId = 1L;
        Long toBeConnectedWithUserId = 2L;
        String connectionIntent = "denied";
        ArrayList<Long> recipients = new ArrayList<>(Arrays.asList(toBeConnectedWithUserId));
        ConnectOutgoingMessageDTO expectedOutgoingMsg = ConnectOutgoingMessageDTO.builder().message("Please confirm that you wish to connect.").to(recipients).build();
        ConnectService connectServiceSpy = spy(connectService);
        ConnectOutgoingMessageDTO outgoing = connectServiceSpy.handleConnectionIntent(connectionIntent, requestingUserId, toBeConnectedWithUserId);

        assertThat(expectedOutgoingMsg.equals(outgoing));
        verify(connectServiceSpy, never()).saveConnectionDetails(Mockito.any(), Mockito.any());
        verify(connectionsRepository, never()).save(Mockito.any());
    }
}
