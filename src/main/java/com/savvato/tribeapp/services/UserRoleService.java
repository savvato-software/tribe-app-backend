//user role service

package com.savvato.tribeapp.services;

import java.util.ArrayList;
import java.util.List;

import com.savvato.tribeapp.entities.UserRole;

public interface UserRoleService {

	Iterable<UserRole> getRoles();
	

}
