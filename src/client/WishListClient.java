package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class WishListClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8888;
   // String loggedIn = "f";
  //  String username = "unknown";


    public WishListClient() {
        this.start();

    }


    private void start() {

        try (SocketChannel socketChannel = SocketChannel.open();
             BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true);
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            socketChannel.configureBlocking(false);

            while (true) {
                System.out.println("=> ");
                // 2 additional chars which save info about the user -> add the username also cookies :)
                String message = scanner.nextLine();
             //   message = message+" "+loggedIn+" "+ username;
                writer.println(message);
                String line;
                if ((line = reader.readLine()) != null) {
                    if(line.contains("successfully registered")){
                 //       loggedIn = "t";
                        String[] tokens = line.split(" +");
                  //      username = tokens[2];
                    }else if(line.contains("successfully logged in")){
                   //     loggedIn = "t";
                    }else if(line.contains("Successfully logged out")){
                  //      loggedIn = "f";
                    }else if(line.contains("Disconnected from server")){
                  //      loggedIn = "f";
                    }
                    System.out.println(line);
                }
                if ("disconnect".equals(message)) {
                    break;
                }
            }


            System.out.println("[ Disconnected ]");
        } catch (IOException e) {
            System.err.println("An error occurred in the client I/O: " + e.getMessage());
            System.err.println(e);
        }
    }

}
