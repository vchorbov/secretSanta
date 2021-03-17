package server.commands;

import commands.Vault;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import storage.StudentsCredentialsStorage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class VaultTest {


    private Map<String, ArrayList<String>> map = new HashMap<>() {{
        put("harry", new ArrayList<>(Arrays.asList("123", "true")));
        put("ron", new ArrayList<>(Arrays.asList("1234", "false")));
    }};
    private StudentsCredentialsStorage storage = new StudentsCredentialsStorage(map);

    private Vault vault = new Vault(storage);
    private final String argumentsInvalid = "ron!!! 123";
    private final String argumentsNameTaken = "ron 123";
    private final String argumentsCorrect = "luna 12345";


    @Test
    public void testTryToRegisterWithInvalidUsername() {
        String result = vault.tryToRegister(argumentsInvalid);
        assertEquals(" Username ron!!! is invalid, select a valid one ]", result);

    }

    @Test
    public void testTryToRegisterWithTakenUsername() {
        String result = vault.tryToRegister(argumentsNameTaken);
        assertEquals(" Username ron is already taken, select another one ]", result);
    }

    @Test
    public void testTryToRegisterSuccessfully() {
        String result = vault.tryToRegister(argumentsCorrect);
        assertEquals("[ Username luna successfully registered ]", result);
        assertEquals(3, map.size());
    }
    @Test
    public void testUserExistsWithBothPossibleCases(){
        assertEquals(true, vault.userExists("ron"));
        assertEquals(false, vault.userExists("hermione"));
    }

    @Test
    public void testTryToLoginInvalidNameOrPassword() {
        String result1 = vault.tryToLogin("roN 123");
        String result2 = vault.tryToLogin("ron 234");
        assertEquals("[ Invalid username/password combination ]", result1);
        assertEquals("[ Invalid username/password combination ]", result2);
    }

    @Test
    public void testTryToLoginAlreadyLoggedIn() {
        String result = vault.tryToLogin("harry 123");
        assertEquals("[ Username harry is already logged in ]", result);

    }
    @Test
    public void testTryToLoginSuccessfully() {
        String result = vault.tryToLogin("ron 1234");
        assertEquals("[ User ron successfully logged in ]", result);

    }
    @Test
    public void testTryToDisconnect(){
        StudentsCredentialsStorage storage1 = Mockito.mock(StudentsCredentialsStorage.class);
        Mockito.when(storage1.getMap()).thenReturn(map);
        Vault vault1 = new Vault(storage1);
        String result = vault1.tryToDisconnect();
        assertEquals("[ Disconnected from server ]",result);
    }
    @Test
    public void tryToLogOutWhenNotRegistered(){
        String result = vault.tryToLogOut("Draco");
        assertEquals(" [ You are not registered ] ", result);
    }
    @Test
    public void tryToLogOutWhenNotLogedIn(){
        String result = vault.tryToLogOut("ron");
        assertEquals(" [ You are not logged in ] ", result);
    }
    @Test
    public void tryToLogOutSuccessfully(){
        String result = vault.tryToLogOut("harry");
        assertEquals("[ Successfully logged out ]", result);
    }



}
