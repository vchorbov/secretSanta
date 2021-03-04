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

    // static factory method or constructor


    public void start() {

        try (SocketChannel socketChannel = SocketChannel.open();
             BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8),true);
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST,SERVER_PORT));
            socketChannel.configureBlocking(false);

            while(true){
                System.out.println("=> ");
                String message = scanner.nextLine();
                writer.println(message);
                String line;
                if ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                if("disconnect".equals(message)){
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
