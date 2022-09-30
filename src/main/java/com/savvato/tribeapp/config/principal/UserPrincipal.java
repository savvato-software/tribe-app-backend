package com.savvato.tribeapp.config.principal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.savvato.tribeapp.entities.UserRole;
import org.apache.commons.collections4.CollectionUtils;
import com.savvato.tribeapp.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

	private static final long serialVersionUID = 12303434L;
	
	private User user;
	
	public UserPrincipal(User user) {
		this.user = user;
	}
	
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final Set<UserRole> roles = user != null ? user.getRoles() : null;
        final List<GrantedAuthority> authorities = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(roles)) {
            roles.forEach(role -> {
                if (role != null) {
//                	System.out.println("User has role [" + role.getName() + "]");
                	authorities.add(new SimpleGrantedAuthority(role.getName()));
                }
            });
        }
        return authorities;
    }
    
    public User getUser() {
    	return this.user;
    }

    public Long getId() {
    	return user != null ? user.getId() : null;
    }
    
    public String getPassword() {
    	return user != null ? user.getPassword() : null;
    }

    public String getUsername() {
        return user != null ? user.getName() : null;
    }

	public boolean isAccountNonExpired() {
		return isEnabled();
	}

	public boolean isAccountNonLocked() {
		return isEnabled();
	}

	public boolean isCredentialsNonExpired() {
		return isEnabled();
	}

	public boolean isEnabled() {
		return user != null ? user.getEnabled() == 1 : false;
	}
}
