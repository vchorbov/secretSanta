package storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Vault {

    private final Map<String, ArrayList<String>> vault;

    public Vault() {

        this.vault = new HashMap<>();
    }

    public String tryToRegister(String arguments) {
        String[] args = arguments.split(" +");
        String username = args[0];
        String password = args[1];

        if (!validateUserName(username)) {
            return String.format(" Username %S is invalid, select a valid one ]", username);
        }
        if (userExists(username)) {
            return String.format(" Username %S is already taken, select another one ]", username);
        }

        vault.put(username, new ArrayList<String>());
        vault.get(username).add(password);
        vault.get(username).add("true");
        return String.format("[ Username %s successfully registered ]", username);

    }

    public String tryToLogin(String arguments) {
        String[] args = arguments.split(" +");
        String username = args[0];
        String password = args[1];


        if (userExists(username) && passwordMatchesUser(username, password)) {
            if (!vault.containsKey(username)) {
                return "[ Invalid username/password combination ]";
            }
            List<String> userData = vault.get(username);
            if (userData.get(1).equals("true")) {
                return String.format("[ Username %s is already logged in ]", username);
            } else {
                vault.get(username).set(1, "true");

                return String.format("[ User %s successfully logged in ]", username);
            }
        }
        return "[ Invalid username/password combination ]";


    }

    public String tryToDisconnect(String arguments) {

        String username = arguments.trim();

        if (vault.containsKey(username)) {

            vault.get(username).set(1, "false");
        }

        return "[ Disconnected from server ]";

    }

    public String tryToLogOut(String arguments) {
        String username = arguments.trim();

        if (!vault.containsKey(username)) {
            return " [ You are not registered ] ";
        } else if (vault.get(username).get(1).equals("false")) {
            return " [ You are not logged in ] ";
        } else if (vault.get(username).get(1).equals("true")) {
            vault.get(username).set(1, "false");
            return "[ Successfully logged out ]";
        }

        return "[ Un successfully logged out ]";

    }

    public boolean userExists(String userName) {
        return vault.containsKey(userName);
    }

    public boolean userIsLoggedIn(String userName) {
        return vault.get(userName).get(1).equals("true");
    }

    private boolean validateUserName(String userName) {
        boolean length = userName.length() > 2;
        Pattern regex = Pattern.compile("[$&+,:;=?@#|'<>^*()%!]");
        Matcher matcher = regex.matcher(userName);

        return !(matcher.find() || !length);

    }

    private boolean passwordMatchesUser(String username, String password) {
        List<String> userData = vault.get(username);
        String passwordIs = userData.get(0);

        return passwordIs.equals(password);
    }


}
