package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.controllers.annotations.controllers.ConnectAPIController.Connect;
import com.savvato.tribeapp.controllers.annotations.controllers.ConnectAPIController.GetConnections;
import com.savvato.tribeapp.controllers.annotations.controllers.ConnectAPIController.GetQRCodeString;
import com.savvato.tribeapp.controllers.annotations.controllers.ConnectAPIController.SaveCosign;
import com.savvato.tribeapp.controllers.dto.ConnectRequest;
import com.savvato.tribeapp.controllers.dto.CosignRequest;
import com.savvato.tribeapp.dto.ConnectIncomingMessageDTO;
import com.savvato.tribeapp.dto.ConnectOutgoingMessageDTO;
import com.savvato.tribeapp.services.ConnectService;
import com.savvato.tribeapp.services.CosignService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "connect", description = "Connections between users")
@RequestMapping("/api/connect")
public class ConnectAPIController {
  @Autowired ConnectService connectService;

  @Autowired
  CosignService cosignService;

  ConnectAPIController() {}

  @GetConnections
  @GetMapping("/all/{userId}")
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
    if (connectService.validateQRCode(
        connectRequest.qrcodePhrase, connectRequest.toBeConnectedWithUserId)) {
      boolean isConnectionSaved =
          connectService.saveConnectionDetails(
              connectRequest.requestingUserId, connectRequest.toBeConnectedWithUserId);
      if (isConnectionSaved) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  @MessageMapping("/connect/room")
  public void connect(@Payload ConnectIncomingMessageDTO incoming, @Header("simpSessionId") String sessionId) {
      connectService.connect(incoming);
  }

  //@SaveCosign
  @PostMapping("/cosign")
  public ResponseEntity saveCosign(@RequestBody @Valid CosignRequest cosignRequest) {

      cosignService.saveCosign(cosignRequest);
      return ResponseEntity.status(HttpStatus.OK).build();

  }
}
