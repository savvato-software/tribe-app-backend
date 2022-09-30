package com.savvato.tribeapp.entities;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Profile {

	private static final long serialVersionUID = 135332921L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Long userId;

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	///
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    ///
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	///
	private java.sql.Timestamp created;

    public java.sql.Timestamp getCreated() {
        return created;
    }

    public void setCreated() {
    	this.created = java.sql.Timestamp.from(Calendar.getInstance().toInstant());
    }

    public void setCreated(java.sql.Timestamp ts) {
    	this.created = ts;
    }

    ///
    private java.sql.Timestamp lastUpdated;

    public java.sql.Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated() {
    	this.lastUpdated = java.sql.Timestamp.from(Calendar.getInstance().toInstant());
    }

    public void setLastUpdated(java.sql.Timestamp ts) {
    	this.lastUpdated = ts;
    }

    /////
	public Profile(Long userId, String name, String description) {
		this.userId = userId;
		this.name = name;
		this.description = description;

		setCreated();
		setLastUpdated();
	}

	public Profile() {

	}
}
