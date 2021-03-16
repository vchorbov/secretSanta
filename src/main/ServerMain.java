package main;

import commands.Vault;
import server.WishListServer;
import storage.StudentsCredentialsStorage;
import storage.StudentsWishesStorage;

public class ServerMain {
    private static final int SERVER_PORT = 8888;

    public static void main(String[] args) {

          StudentsWishesStorage studentsWishesStorage = new StudentsWishesStorage();
          StudentsCredentialsStorage studentsCredentialsStorage = new StudentsCredentialsStorage();
          Vault vault = new Vault(studentsCredentialsStorage);

          new WishListServer(SERVER_PORT, vault,studentsWishesStorage).start();
    }
}
