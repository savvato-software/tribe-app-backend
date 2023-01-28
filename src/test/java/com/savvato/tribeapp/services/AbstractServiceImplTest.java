package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.UserRequest;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.entities.UserRole;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractServiceImplTest {
    public static long USER1_ID = 1;
    public static long USER2_ID = 732;
    public static String USER1_EMAIL = "user1@email.com";
    public static String USER2_EMAIL = "user2@email.com";
    public static String USER1_PHONE = "0035551212"; // starts with 0 to indicate to the code that this is a test
    public static String USER2_PHONE = "0035551213"; // starts with 0 to indicate to the code that this is a test
    public static String USER1_PASSWORD = "password1";
    public static String USER2_PASSWORD = "password2";
    public static int USER_IS_ENABLED = 1;
    public static String USER1_PREFERRED_CONTACT_METHOD = "email";

    public static String USER2_PREFERRED_CONTACT_METHOD = "phone";

    public static String USER1_NAME = "Fake A. Admin";
    public static String USER2_NAME = "Fake R. User"; // the R stand for Regular

    public Set<UserRole> getUserRoles_Admin() {
        Set<UserRole> rtn = new HashSet<>();

        rtn.add(UserRole.ROLE_ADMIN);
        rtn.add(UserRole.ROLE_ACCOUNTHOLDER);

        return rtn;
    }

    public User getUser1() {
        User rtn = new User();

        rtn.setId(USER1_ID);
        rtn.setName(USER1_NAME);
        rtn.setEmail(USER1_EMAIL);
        rtn.setPhone(USER1_PHONE);
        rtn.setPassword(USER1_PASSWORD);
        rtn.setEnabled(USER_IS_ENABLED);

        rtn.setRoles(getUserRoles_Admin());

        return rtn;
    }

    public User getUser2() {
        User rtn = new User();

        rtn.setId(USER2_ID);
        rtn.setName(USER2_NAME);
        rtn.setEmail(USER2_EMAIL);
        rtn.setPhone(USER2_PHONE);
        rtn.setPassword(USER2_PASSWORD);
        rtn.setEnabled(USER_IS_ENABLED);

        rtn.setRoles(getUserRoles_Admin());

        return rtn;
    }

    public UserRequest getUserRequestFor(User user) {
        UserRequest rtn = new UserRequest();

        rtn.id = user.getId();
        rtn.name = user.getName();
        rtn.email = user.getEmail();
        rtn.phone = user.getPhone();
        rtn.password = user.getPassword();

        return rtn;
    }
}
