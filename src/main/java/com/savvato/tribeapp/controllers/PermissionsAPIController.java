package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.annotations.controllers.PermissionsAPIController.AddPermissions;
import com.savvato.tribeapp.controllers.annotations.controllers.PermissionsAPIController.DeletePermissions;
import com.savvato.tribeapp.controllers.annotations.controllers.PermissionsAPIController.GetAllUserRoles;
import com.savvato.tribeapp.controllers.annotations.controllers.PermissionsAPIController.GetAllUsers;
import com.savvato.tribeapp.controllers.dto.PermissionsRequest;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.services.UserRoleMapService;
import com.savvato.tribeapp.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/permissions")
@Tag(name="permissions", description="Delegation of user permissions")
public class PermissionsAPIController {
    @Autowired
    UserRoleMapService userRoleMapService;

    @Autowired
    UserService userService;

    @GetAllUsers
    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> getAllUsers() {

        // WHY is this defined here? Instead of UserAPIController? It's because only
        // permissions needs this functionality, at the moment. Also, all the methods
        // In UserAPIController assume public, no-password access. This endpoint would
        // require a login. When another domain eventually needs a list of users,
        // let's think about what they have in common, and create a controller/service
        // then, and removing this method, of course.

        Iterable<User> rtn = userService.getAllUsers();

        if (rtn != null) {
            return ResponseEntity.status(HttpStatus.OK).body(rtn);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
    }

    @GetAllUserRoles
    @GetMapping("/user-roles")
    public ResponseEntity<Iterable<String>> getAllUserRoles() {
        Iterable<String> rtn = userRoleMapService.getRoles();

        if (rtn != null) {
            return ResponseEntity.status(HttpStatus.OK).body(rtn);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
    }

    @AddPermissions
    @PostMapping
    public ResponseEntity<Boolean> addPermissions(@RequestBody @Valid PermissionsRequest request) {
        boolean rtn = userRoleMapService.addRolesToUser(request.id, request.permissions);

        if (rtn) {
            return ResponseEntity.status(HttpStatus.OK).body(rtn);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rtn);
        }
    }
    @DeletePermissions
    @DeleteMapping
    public ResponseEntity<Boolean> deletePermissions(@RequestBody @Valid PermissionsRequest request) {
        boolean rtn = userRoleMapService.removeRolesFromUser(request.id, request.permissions);

        if (rtn) {
            return ResponseEntity.status(HttpStatus.OK).body(rtn);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rtn);
        }
    }
}
