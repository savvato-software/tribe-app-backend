//user role service

package com.savvato.tribeapp.services;

import java.util.ArrayList;
import java.util.List;

import com.savvato.tribeapp.dto.UserRoleDTO;
import com.savvato.tribeapp.entities.UserRole;

public interface UserRoleService {

	Iterable<UserRoleDTO> getRoles();
	

}
