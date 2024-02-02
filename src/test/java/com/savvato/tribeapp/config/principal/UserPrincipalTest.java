package com.savvato.tribeapp.config.principal;

import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.services.AbstractServiceImplTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class UserPrincipalTest extends AbstractServiceImplTest {

    @Test
    public void getAuthoritiesWhenUserNull() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        UserPrincipal userPrincipal = new UserPrincipal(null);
        assertThat(userPrincipal.getAuthorities()).usingRecursiveComparison().isEqualTo(authorities);
    }

    @Test
    public void getIdWhenUserNotNull() {
        User user = getUser1();
        Long id = user.getId();
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertEquals(id, userPrincipal.getId());
    }

    @Test
    public void getIdWhenUserNull() {
        UserPrincipal userPrincipal = new UserPrincipal(null);
        assertNull(userPrincipal.getId());
    }

    @Test
    public void getPasswordWhenUserNotNull() {
        User user = getUser1();
        String password = user.getPassword();
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertEquals(password, userPrincipal.getPassword());
    }

    @Test
    public void getPasswordWhenUserNull() {
        UserPrincipal userPrincipal = new UserPrincipal(null);
        assertNull(userPrincipal.getPassword());
    }

    @Test
    public void getUsernameWhenUserNotNull() {
        User user = getUser1();
        String username = user.getName();
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertEquals(username, userPrincipal.getUsername());
    }

    @Test
    public void getUsernameWhenUserNull() {
        UserPrincipal userPrincipal = new UserPrincipal(null);
        assertNull(userPrincipal.getUsername());
    }

    @Test
    public void isEnabledWhenUserNotNullAndUserIsEnabled() {
        User user = getUser1();
        user.setEnabled(1);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertTrue(userPrincipal.isEnabled());
    }

    @Test
    public void isEnabledWhenUserNotNullAndUserIsNotEnabled() {
        User user = getUser1();
        user.setEnabled(0);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertFalse(userPrincipal.isEnabled());
    }

    @Test
    public void isEnabledWhenUserNull() {
        UserPrincipal userPrincipal = new UserPrincipal(null);
        assertFalse(userPrincipal.isEnabled());
    }

    @Test
    public void isAccountNonExpired() {
        User user = getUser1();
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertEquals(userPrincipal.isEnabled(), userPrincipal.isAccountNonExpired());
    }

    @Test
    public void isAccountNonLocked() {
        User user = getUser1();
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertEquals(userPrincipal.isEnabled(), userPrincipal.isAccountNonLocked());
    }

    @Test
    public void isCredentialsNonExpired() {
        User user = getUser1();
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertEquals(userPrincipal.isEnabled(), userPrincipal.isCredentialsNonExpired());
    }
}
