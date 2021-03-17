package main;

import commands.Vault;
import server.WishListServer;
import storage.StudentsCredentialsStorage;
import storage.StudentsWishesStorage;

public class ServerMain {
    private static final int SERVER_PORT = 8888;
    private static final String LOCATION_WISHES = "wishesDB";
    private static final String LOCATION_CREDENTIALS = "credentialsDB";

    public static void main(String[] args) {

        /*
         * You can instantiate the classes StudentsWishesStorage and StudentsCredentialStorage
         * without any parameters in the constructor. That way the app will start working with empty "DB"es.
         *
         * StudentsWishesStorage studentsWishesStorage = new StudentsWishesStorage();
         * StudentsCredentialsStorage studentsCredentialsStorage = new StudentsCredentialsStorage();
         *
         */

        /*
        *  By instantiating the classes with the locations of the files where the info from the previous sessions is
        *  being stored, we can pick up from where we left, creating a state.
        */
        StudentsWishesStorage studentsWishesStorage = new StudentsWishesStorage(LOCATION_WISHES);
        StudentsCredentialsStorage studentsCredentialsStorage = new StudentsCredentialsStorage(LOCATION_CREDENTIALS);
        Vault vault = new Vault(studentsCredentialsStorage);

        new WishListServer(SERVER_PORT, vault, studentsWishesStorage).start();
    }
}
