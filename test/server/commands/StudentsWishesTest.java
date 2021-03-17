package server.commands;

import commands.StudentsWishes;
import commands.Vault;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import storage.StudentsWishesStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

public class StudentsWishesTest {
    private final Map<String, ArrayList<String>> map = new HashMap<>(){{
        put("Harry", new ArrayList<>(Arrays.asList("wand","owl")));
        put("Ron", new ArrayList<>(Arrays.asList("chocolate")));

    }};

    private final Map<String, ArrayList<String>> mapEmpty = new HashMap<>();


    StudentsWishesStorage storage = new StudentsWishesStorage(map);

    Vault vault = Mockito.mock(Vault.class);
    StudentsWishes wishes = new StudentsWishes(vault, storage);

    @Test
    public void testPostWishWithUnregisteredUser(){
        when(vault.userExists("Luna")).thenReturn(false);
        String result = wishes.postWish("Hermione book Luna");
        Assert.assertEquals("[ Student with username Luna is not registered, so they have no permission to post wishes ] ", result);
    }
    @Test
    public void testPostWishWithUserWhoIsNotLoggedIn(){
        when(vault.userExists("Harry")).thenReturn(true);
        when(vault.userIsLoggedIn("Harry")).thenReturn(false);
        String result = wishes.postWish("Hermione book Harry");

        Assert.assertEquals("[ Student with username Harry is not logged in, so they have no permission to post wishes ] ", result);
    }
    @Test
    public void testPostWishForUserWhoIsNotLoggedIn(){
        when(vault.userExists("Hermione")).thenReturn(false);
        when(vault.userExists("Harry")).thenReturn(true);
        when(vault.userIsLoggedIn("Harry")).thenReturn(true);
        String result = wishes.postWish("Hermione book Harry");

        Assert.assertEquals("[ Student with username Hermione is not registered ] ", result);
    }
    @Test
    public void testPostWishForUserWhoAlreadyHasTheSameWishSubmitted(){
        when(vault.userExists("Ron")).thenReturn(true);
        when(vault.userExists("Harry")).thenReturn(true);
        when(vault.userIsLoggedIn("Harry")).thenReturn(true);

        String result = wishes.postWish("Ron chocolate Harry");

        Assert.assertEquals("[ The same gift for student Ron was already submitted ]", result);
    }
    @Test
    public void testPostWishWithNewWishBeingAdded(){
        when(vault.userExists("Ron")).thenReturn(true);
        when(vault.userExists("Harry")).thenReturn(true);
        when(vault.userIsLoggedIn("Harry")).thenReturn(true);

        String result = wishes.postWish("Ron broom Harry");

        Assert.assertEquals("[ Gift broom for student Ron submitted successfully ]", result);
    }

    @Test
    public void testGetWish(){
        String result = wishes.getWish("Harry");
        Assert.assertEquals("[ Ron: [chocolate] ]", result);
    }




}
