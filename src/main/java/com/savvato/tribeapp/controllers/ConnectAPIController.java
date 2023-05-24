package com.savvato.tribeapp.controllers;


import com.savvato.tribeapp.controllers.dto.ConnectRequest;
import com.savvato.tribeapp.services.ConnectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class ConnectAPIController {
    @Autowired
    ConnectService connectService;

    ConnectAPIController(){

    }

    @RequestMapping(value = {"/api/connect/{userId}"}, method=RequestMethod.GET)
    public ResponseEntity getQrCodeString(@PathVariable Long userId){
        connectService.storeQRCodeString(userId);
        Optional<String> opt = connectService.getQRCodeString(userId);

        if (opt.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @RequestMapping(value = {"/api/connect/"}, method=RequestMethod.POST)
    public void connect(@Valid ConnectRequest connectRequest){

    }
}
