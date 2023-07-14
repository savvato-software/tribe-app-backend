package com.savvato.tribeapp.controllers;


import com.savvato.tribeapp.controllers.dto.ConnectRequest;
import com.savvato.tribeapp.services.ConnectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/connect")
public class ConnectAPIController {
    @Autowired
    ConnectService connectService;

    ConnectAPIController(){

    }

    @GetMapping("/{userId}")
    public ResponseEntity getQrCodeString(@PathVariable Long userId){

        Optional<String> opt =  connectService.storeQRCodeString(userId);

        if (opt.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping
    public boolean connect(@Valid ConnectRequest connectRequest){
        boolean rtn = connectService.connect(connectRequest.requestingUserId, connectRequest.toBeConnectedWithUserId, connectRequest.qrcodePhrase);
        return rtn;
    }
}
