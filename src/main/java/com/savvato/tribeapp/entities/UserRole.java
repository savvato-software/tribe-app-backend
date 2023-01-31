package com.savvato.tribeapp.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserRole {

	public static final UserRole ROLE_ADMIN = new UserRole(1L, "ROLE_admin");
	public static final UserRole ROLE_ACCOUNTHOLDER = new UserRole(2L, "ROLE_accountholder");
	public static final UserRole ROLE_PHRASEREVIEWER = new UserRole(2L, "ROLE_phrasereviewer");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	///
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	/////
	public UserRole(String name) {
		this.name = name;
	}
	
	private UserRole(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public UserRole() {
		
	}
}
