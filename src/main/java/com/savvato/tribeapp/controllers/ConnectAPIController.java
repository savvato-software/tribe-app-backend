package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.controllers.annotations.controllers.ConnectAPIController.*;
import com.savvato.tribeapp.controllers.dto.ConnectRequest;
import com.savvato.tribeapp.controllers.dto.CosignRequest;
import com.savvato.tribeapp.dto.ConnectIncomingMessageDTO;
import com.savvato.tribeapp.dto.ConnectOutgoingMessageDTO;
import com.savvato.tribeapp.dto.CosignDTO;
import com.savvato.tribeapp.dto.UserNameDTO;
import com.savvato.tribeapp.services.ConnectService;
import com.savvato.tribeapp.services.CosignService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "connect", description = "Connections between users")
@RequestMapping("/api/connect")
public class ConnectAPIController {
  @Autowired ConnectService connectService;

  @Autowired
  CosignService cosignService;

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
    log.error("Exception occurred: " + ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: " + ex.getMessage());
  }

  ConnectAPIController() {}

  @GetConnections
  @GetMapping("/{userId}/all")
  public ResponseEntity<List<ConnectOutgoingMessageDTO>> getConnections(
      @Parameter(description = "The user ID of a user", example = "1") @PathVariable Long userId) {

    List<ConnectOutgoingMessageDTO> list = connectService.getAllConnectionsForAUser(userId);

    if (list != null) {
      return ResponseEntity.status(HttpStatus.OK).body(list);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }

  @GetQRCodeString
  @GetMapping("/{userId}")
  public ResponseEntity getQrCodeString(
      @Parameter(description = "The user ID of a user", example = "1") @PathVariable Long userId) {

    Optional<String> opt = connectService.storeQRCodeString(userId);

    if (opt.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(opt.get());
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }

  @Connect
  @PostMapping
  public boolean connect(@RequestBody @Valid ConnectRequest connectRequest) {
    if (connectService.validateQRCode(connectRequest.qrcodePhrase, connectRequest.toBeConnectedWithUserId)) {
      return connectService.saveConnectionDetails(connectRequest.requestingUserId, connectRequest.toBeConnectedWithUserId);
    } else {
      return false;
    }
  }

  @MessageMapping("/connect/room")
  public void connect(@Payload ConnectIncomingMessageDTO incoming, @Header("simpSessionId") String sessionId) {
      connectService.connect(incoming);
  }

  @SaveCosign
  @PostMapping("/cosign")
  public ResponseEntity<CosignDTO> saveCosign(@RequestBody @Valid CosignRequest cosignRequest) {

      CosignDTO cosignDTO = cosignService.saveCosign(cosignRequest.userIdIssuing, cosignRequest.userIdReceiving, cosignRequest.phraseId);
      
      return ResponseEntity.status(HttpStatus.OK).body(cosignDTO);

  }
  @DeleteCosign
  @DeleteMapping("/cosign")
  public ResponseEntity deleteCosign(@RequestBody @Valid CosignRequest cosignRequest) throws Exception {

    cosignService.deleteCosign(cosignRequest.userIdIssuing, cosignRequest.userIdReceiving, cosignRequest.phraseId);

    return ResponseEntity.status(HttpStatus.OK).build();

  }

  @GetMapping("cosign/{userIdReceiving}/{phraseId}")
  public ResponseEntity<List<UserNameDTO>> getCosignersForUserAttribute(@PathVariable Long userIdReceiving, @PathVariable Long phraseId) {

    List<UserNameDTO> list = cosignService.getCosignersForUserAttribute(userIdReceiving,phraseId);

    return ResponseEntity.status(HttpStatus.OK).body(list);
  }

}
