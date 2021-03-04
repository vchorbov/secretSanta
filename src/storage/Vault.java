package storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Vault {

    private final Map<String, List<String>> vault;

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
        vault.put(username, List.of(password, "true"));
        return String.format("[ Username %s successfully registered ]", username);

    }

    public String tryToLogin(String arguments) {
        String[] args = arguments.split(" +");
        String username = args[0];
        String password = args[1];


        if (userExists(username) && passwordMatchesUser(username, password)) {
            List<String> userData = vault.get(username);
            if (userData.get(1).equals("true")) {
                return String.format("[ Username %s is already logged in ]", username);
            } else {
                vault.get(username).add(1, "true");

                return String.format("[ User %s successfully logged in ]", username);
            }
        }

        return "[ Invalid username/password combination ]";


    }

    public String tryToDisconnect(String arguments) {
        String[] tokens = arguments.split(" ");
        String username = tokens[1];

        if(vault.containsKey(username)){
            vault.get(username).add(1, "false");
        }

        return "[ Disconnected from server ]";

    }

    private boolean validateUserName(String userName) {
        boolean length = userName.length() > 2;
        Pattern regex = Pattern.compile("[$&+,:;=?@#|'<>^*()%!]");
        Matcher matcher = regex.matcher(userName);

        return !(matcher.find() || !length);

    }

    public boolean userExists(String userName) {
        return vault.containsKey(userName);
    }


    private boolean passwordMatchesUser(String username, String password) {
        List<String> userData = vault.get(username);
        String passwordIs = userData.get(0);

        return passwordIs.equals(password);
    }


}
