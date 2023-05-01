package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.UserRequest;
import com.savvato.tribeapp.entities.*;

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

    public Set<UserRole> getUserRoles_AccountHolder() {
        Set<UserRole> rtn = new HashSet<>();
        rtn.add(UserRole.ROLE_ACCOUNTHOLDER);
        return rtn;
    }

    public Set<UserRole> getUserRoles_Admin() {
        Set<UserRole> rtn = new HashSet<>();

        rtn.add(UserRole.ROLE_ADMIN);
        rtn.add(UserRole.ROLE_ACCOUNTHOLDER);

        return rtn;
    }

    public Set<UserRole> getUserRoles_Admin_AccountHolder() {
        Set<UserRole> rtn = new HashSet<>();

        rtn.add(UserRole.ROLE_ADMIN);
        rtn.add(UserRole.ROLE_PHRASEREVIEWER);
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

    public Adverb getTestAdverb() {
        Adverb testAdverb = new Adverb();
        testAdverb.setId(100L);
        testAdverb.setWord("testAdverb");
        return testAdverb;
    }
    public Verb getTestVerb() {
        Verb testVerb = new Verb();
        testVerb.setId(100L);
        testVerb.setWord("testVerb");
        return testVerb;
    }
    public Preposition getTestPreposition() {
        Preposition testPreposition = new Preposition();
        testPreposition.setId(100L);
        testPreposition.setWord("testPreposition");
        return testPreposition;
    }
    public Noun getTestNoun() {
        Noun testNoun = new Noun();
        testNoun.setId(100L);
        testNoun.setWord("testNoun");
        return testNoun;
    }

    public Phrase getTestPhrase() {
        Phrase testPhrase = new Phrase();
        testPhrase.setId(1L);
        testPhrase.setAdverbId(1L);
        testPhrase.setVerbId(1L);
        testPhrase.setPrepositionId(1L);
        testPhrase.setNounId(1L);
        return testPhrase;
    }
}
