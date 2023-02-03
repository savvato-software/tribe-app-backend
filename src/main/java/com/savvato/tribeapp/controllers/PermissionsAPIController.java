package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.dto.PermissionsRequest;
import com.savvato.tribeapp.services.UserRoleMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PermissionsAPIController {
    @Autowired
    UserRoleMapService userRoleMapService;

    @RequestMapping(value = { "/api/permissions" }, method= RequestMethod.POST)
    public ResponseEntity<Boolean> addPermissions(@RequestBody @Valid PermissionsRequest request) {
        boolean rtn = userRoleMapService.addRolesToUser(request.id, request.permissions);

        if (rtn) {
            return ResponseEntity.status(HttpStatus.OK).body(rtn);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rtn);
        }
    }
}
