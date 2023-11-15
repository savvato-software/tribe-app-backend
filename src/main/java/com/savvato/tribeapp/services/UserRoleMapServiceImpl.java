package com.savvato.tribeapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.savvato.tribeapp.entities.UserRoleMap;
import com.savvato.tribeapp.repositories.UserRoleMapRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRoleMapServiceImpl implements UserRoleMapService {
    @Autowired
    UserRoleMapRepository userRoleMapRepo;

    public List<String> getRoles() {
        // collect each ROLES value's name to list
        return Arrays.stream(UserRoleMapService.ROLES.values()).map(Enum::name).collect(Collectors.toList());
    }

    public void addRoleToUser(Long userId, ROLES role) {
        userRoleMapRepo.save(new UserRoleMap(userId, Long.valueOf(String.valueOf(role.ordinal() + 1))));
    }

    public void removeRoleFromUser(Long userId, ROLES role) {
        userRoleMapRepo.delete(new UserRoleMap(userId, Long.valueOf(String.valueOf(role.ordinal() + 1))));
    }

    public boolean addRolesToUser(Long userId, ArrayList<String> rolesToAdd) {
        boolean successfulAdd = true;
        for (String role : rolesToAdd) {
            try {
                addRoleToUser(userId, ROLES.valueOf(role));
            } catch (Exception e) {
                successfulAdd = false;
            }
        }
        return successfulAdd;
    }

    public boolean removeRolesFromUser(Long userId, ArrayList<String> rolesToDelete) {
        boolean successfulDelete = true;
        for (String role : rolesToDelete) {
            if (role != "ACCOUNTHOLDER") {
                try {
                    removeRoleFromUser(userId, ROLES.valueOf(role));
                } catch (Exception e) {
                    successfulDelete = false;
                }
            } else {
                successfulDelete = false;
            }
        }
        return successfulDelete;
    }
}
