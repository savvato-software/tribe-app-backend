package com.savvato.tribeapp.services;

import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.controllers.dto.ConnectionRemovalRequest;
import com.savvato.tribeapp.dto.ConnectIncomingMessageDTO;
import com.savvato.tribeapp.dto.ConnectOutgoingMessageDTO;
import com.savvato.tribeapp.entities.Connection;
import com.savvato.tribeapp.repositories.ConnectionsRepository;
import com.savvato.tribeapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
        public CacheService cacheService() {
            return new CacheServiceImpl();
        }
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
    public void saveConnectionDetailsHappyPath() {
        Long requestingUserId = 1L;
        Long toBeConnectedWithUserId = 2L;

        Boolean connectionStatus = connectService.saveConnectionDetails(requestingUserId, toBeConnectedWithUserId);
        assertEquals(connectionStatus, true);
    }

    @Test
    public void saveConnectionDetailsUnhappyPath() {
        Long requestingUserId = 1L;
        Long toBeConnectedWithUserId = 2L;
        doThrow(new IndexOutOfBoundsException()).when(connectionsRepository).save(Mockito.any());
        Boolean connectionStatus = connectService.saveConnectionDetails(requestingUserId, toBeConnectedWithUserId);
        assertEquals(connectionStatus, false);
    }

    @Test
    public void saveConnectionDetailsWhenExistingConnectionWithReversedUserIdsExists() {
        Long requestingUserId = 1L;
        Long toBeConnectedWithUserId = 2L;
        Connection existingConnection = new Connection(requestingUserId, toBeConnectedWithUserId);
        when(connectionsRepository.findExistingConnectionWithReversedUserIds(anyLong(), anyLong())).thenReturn(Optional.of(existingConnection));
        Boolean connectionStatus = connectService.saveConnectionDetails(requestingUserId, toBeConnectedWithUserId);
        assertEquals(connectionStatus, false);
        verify(connectionsRepository, never()).save(any());
    }

    @Test
    public void saveConnectionDetailsWhenIdsAreTheSame() {
        Long requestingUserId = 2L;
        Long toBeConnectedWithUserId = 2L;
        Boolean connectionStatus = connectService.saveConnectionDetails(requestingUserId, toBeConnectedWithUserId);
        assertEquals(connectionStatus, false);

        verify(connectionsRepository, never()).findExistingConnectionWithReversedUserIds(anyLong(), anyLong());
        verify(connectionsRepository, never()).save(any());
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

        connectServiceSpy.connect(incoming);

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
        ArrayList<Long> recipients = new ArrayList<>(List.of(toBeConnectedWithUserId));
        String expectedDestination = "/connect/user/queue/specific-user";
        ConnectIncomingMessageDTO incoming = ConnectIncomingMessageDTO.builder().requestingUserId(requestingUserId).toBeConnectedWithUserId(toBeConnectedWithUserId).connectionIntent(connectionIntent).build();
        ConnectOutgoingMessageDTO outgoing = ConnectOutgoingMessageDTO.builder().message("Please confirm that you wish to connect.").to(recipients).build();
        ConnectService connectServiceSpy = spy(connectService);
        doReturn(true).when(connectServiceSpy).validateQRCode(Mockito.any(), Mockito.any());
        doReturn(outgoing).when(connectServiceSpy).handleConnectionIntent(Mockito.any(), Mockito.any(), Mockito.any());

        connectServiceSpy.connect(incoming);

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
        ArrayList<Long> recipients = new ArrayList<>(List.of(toBeConnectedWithUserId));
        ConnectOutgoingMessageDTO expectedOutgoingMsg = ConnectOutgoingMessageDTO.builder().message("Please confirm that you wish to connect.").to(recipients).build();
        ConnectService connectServiceSpy = spy(connectService);
        ConnectOutgoingMessageDTO outgoing = connectServiceSpy.handleConnectionIntent(connectionIntent, requestingUserId, toBeConnectedWithUserId);

        verify(connectServiceSpy, never()).saveConnectionDetails(Mockito.any(), Mockito.any());
        verify(connectionsRepository, never()).save(Mockito.any());
        assertThat(expectedOutgoingMsg).isEqualToComparingFieldByField(outgoing);
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
        assertThat(expectedOutgoingMsg).isEqualToComparingFieldByField(outgoing);
    }

    @Test
    public void handleConnectionIntentWhenConnectionIntentConfirmedAndDatabaseSaveUnsuccessful() throws Exception {
        Long requestingUserId = 1L;
        Long toBeConnectedWithUserId = 2L;
        String connectionIntent = "confirmed";
        ArrayList<Long> recipients = new ArrayList<>(Arrays.asList(requestingUserId, toBeConnectedWithUserId));
        ConnectOutgoingMessageDTO expectedOutgoingMsg = ConnectOutgoingMessageDTO.builder().connectionError(true).to(recipients).message("Failed to save connection to database.").build();

        Mockito.when(connectionsRepository.save(Mockito.any())).thenThrow(new NullPointerException("Something went wrong."));
        ConnectOutgoingMessageDTO outgoing = connectService.handleConnectionIntent(connectionIntent, requestingUserId, toBeConnectedWithUserId);

        ArgumentCaptor<Connection> connectionArg = ArgumentCaptor.forClass(Connection.class);
        verify(connectionsRepository, times(1)).save(connectionArg.capture());
        assertEquals(connectionArg.getValue().getToBeConnectedWithUserId(), toBeConnectedWithUserId);
        assertEquals(connectionArg.getValue().getRequestingUserId(), requestingUserId);
        assertThat(expectedOutgoingMsg).usingRecursiveComparison().isEqualTo(outgoing);
    }

    @Test
    public void handleConnectionIntentWhenConnectionIntentDenied() {
        Long requestingUserId = 1L;
        Long toBeConnectedWithUserId = 2L;
        String connectionIntent = "denied";
        ArrayList<Long> recipients = new ArrayList<>(Arrays.asList(requestingUserId, toBeConnectedWithUserId));
        ConnectOutgoingMessageDTO expectedOutgoingMsg = ConnectOutgoingMessageDTO.builder().connectionError(true).to(recipients).message("Connection request denied.").build();
        ConnectService connectServiceSpy = spy(connectService);
        ConnectOutgoingMessageDTO outgoing = connectServiceSpy.handleConnectionIntent(connectionIntent, requestingUserId, toBeConnectedWithUserId);

        verify(connectServiceSpy, never()).saveConnectionDetails(Mockito.any(), Mockito.any());
        verify(connectionsRepository, never()).save(Mockito.any());
        assertThat(expectedOutgoingMsg).isEqualToComparingFieldByField(outgoing);
    }

    @Test
    public void validateQRCode() {
        ConnectService connectServiceSpy = spy(connectService);
        Optional<String> qrCodeOpt = Optional.of("ABCDEFGHIJKL");
        String providedQRCode = "ABCDEFGHIJKL";
        Long toBeConnectedWithUserId = 1L;
        doReturn(qrCodeOpt).when(connectServiceSpy).getQRCodeString(anyLong());
        Boolean isValid = connectServiceSpy.validateQRCode(providedQRCode, toBeConnectedWithUserId);
        assertTrue(isValid);
    }

    @Test
    public void removeConnectionHappyPath() {
        ConnectionRemovalRequest connectionDeleteRequest = new ConnectionRemovalRequest();
        connectionDeleteRequest.requestingUserId = 1L;
        connectionDeleteRequest.connectedWithUserId = 2L;
        ArgumentCaptor<Long> requestingUserIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> connectedWithUserIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertTrue(connectService.removeConnection(connectionDeleteRequest));
        verify(connectionsRepository, times(1)).removeConnection(requestingUserIdCaptor.capture(), connectedWithUserIdCaptor.capture());
        assertEquals(requestingUserIdCaptor.getValue(), connectionDeleteRequest.requestingUserId);
        assertEquals(connectedWithUserIdCaptor.getValue(), connectionDeleteRequest.connectedWithUserId);
    }

    @Test
    public void removeConnectionWhenDatabaseDeleteFails() {
        ConnectionRemovalRequest connectionDeleteRequest = new ConnectionRemovalRequest();
        connectionDeleteRequest.requestingUserId = 1L;
        connectionDeleteRequest.connectedWithUserId = 2L;
        ArgumentCaptor<Long> requestingUserIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> connectedWithUserIdCaptor = ArgumentCaptor.forClass(Long.class);
        doThrow(new IllegalArgumentException("Database delete failed.")).when(connectionsRepository).removeConnection(anyLong(), anyLong());
        assertFalse(connectService.removeConnection(connectionDeleteRequest));
        verify(connectionsRepository, times(1)).removeConnection(requestingUserIdCaptor.capture(), connectedWithUserIdCaptor.capture());
        assertEquals(requestingUserIdCaptor.getValue(), connectionDeleteRequest.requestingUserId);
        assertEquals(connectedWithUserIdCaptor.getValue(), connectionDeleteRequest.connectedWithUserId);
    }

    @Test
    public void removeConnectionWhenBothIdsAreTheSame() {
        ConnectionRemovalRequest connectionRemovalRequest = new ConnectionRemovalRequest();
        connectionRemovalRequest.requestingUserId = 1L;
        connectionRemovalRequest.connectedWithUserId = 1L;
        assertFalse(connectService.removeConnection(connectionRemovalRequest));
        verify(connectionsRepository, never()).removeConnection(anyLong(), anyLong());
    }

    public void testGetAllConnectionsForAUserWhenConnectionsExist() {
        Long toBeConnectedUserId = 2L;

        Connection connection = new Connection();
        connection.setCreated();
        connection.setRequestingUserId(1L);
        connection.setToBeConnectedWithUserId(toBeConnectedUserId);

        when(connectionsRepository.findAllByToBeConnectedWithUserId(anyLong())).thenReturn(List.of(connection));

        List<ConnectOutgoingMessageDTO> expectedOutgoingMessageDTOS = new ArrayList<>();
        ConnectOutgoingMessageDTO outgoingMessage = ConnectOutgoingMessageDTO.builder()
                .connectionSuccess(true)
                .to(new ArrayList<>(Collections.singletonList(connection.getRequestingUserId())))
                .message("")
                .build();
        expectedOutgoingMessageDTOS.add(outgoingMessage);

        List<ConnectOutgoingMessageDTO> actualMessageDTOs = connectService.getAllConnectionsForAUser(toBeConnectedUserId);

        assertThat(actualMessageDTOs).usingRecursiveComparison().isEqualTo(expectedOutgoingMessageDTOS);

    }

    @Test
    public void testGetAllConnectionsForAUserWhenConnectionsDoNotExist() {
        Long toBeConnectedUserId = 2L;

        when(connectionsRepository.findAllByToBeConnectedWithUserId(anyLong())).thenReturn(Collections.emptyList());

        List<ConnectOutgoingMessageDTO> actualMessageDTOs = connectService.getAllConnectionsForAUser(toBeConnectedUserId);

        assertThat(actualMessageDTOs).usingRecursiveComparison().isEqualTo(Collections.emptyList());

    }

    @Test
    public void validateQRCodeWhenQRCodeIsEmpty() {
        String qrcodePhrase = "";
        Long userId = 1L;
        when(cacheService.get(any(), any())).thenReturn("");
        boolean isValidQRCode = connectService.validateQRCode(qrcodePhrase, userId);
        assertFalse(isValidQRCode);
    }

    @Test
    public void validateQRCodeWhenQRCodeIsValid() {
        String qrcodePhrase = "ABCDE";
        Long userId = 1L;
        when(cacheService.get(any(), any())).thenReturn(qrcodePhrase);
        boolean isValidQRCode = connectService.validateQRCode(qrcodePhrase, userId);
        assertTrue(isValidQRCode);
    }

    @Test
    public void validateQRCodeWhenNoQRCodeIsCached() {
        String qrcodePhrase = "ABCDE";
        Long userId = 1L;
        when(cacheService.get(any(), any())).thenReturn(null);
        boolean isValidQRCode = connectService.validateQRCode(qrcodePhrase, userId);
        assertFalse(isValidQRCode);
    }
}
