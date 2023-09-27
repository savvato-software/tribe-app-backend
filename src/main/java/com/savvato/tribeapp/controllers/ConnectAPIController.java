package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.annotations.controllers.ConnectAPIController.Connect;
import com.savvato.tribeapp.controllers.annotations.controllers.ConnectAPIController.GetQRCodeString;
import com.savvato.tribeapp.controllers.dto.ConnectRequest;
import com.savvato.tribeapp.services.ConnectService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@Tag(name = "connect", description = "Connections between users")
@RequestMapping("/api/connect")
public class ConnectAPIController {
  @Autowired ConnectService connectService;

  ConnectAPIController() {}

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
    boolean rtn =
        connectService.connect(
            connectRequest.requestingUserId,
            connectRequest.toBeConnectedWithUserId,
            connectRequest.qrcodePhrase);
    return rtn;
  }
}
