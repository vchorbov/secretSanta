
package server;

import commands.CommandExecutor;
import commands.StudentsWishes;
import commands.Vault;
import storage.StudentsWishesStorage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class WishListServer {

   // private static final int SERVER_PORT = 8888;
    private static final int BUFFER_SIZE = 1024;
    private static final String SERVER_HOST = "localhost";
    private static final Random RANDOM = new Random();

    private final int port;
    private final Map<String, Set<String>> studentsWishes;
    private final ByteBuffer messageBuffer;
    private final CommandExecutor commandExecutor;
    private final Vault vault;
    private StudentsWishesStorage storage;


    private boolean isStarted = true;

    public WishListServer(int port, Vault vault, StudentsWishesStorage storage) {
        this.port = port;
        this.storage = storage;
        this.studentsWishes = storage.getMap();
        this.messageBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        this.vault = vault;
        this.commandExecutor = new CommandExecutor(new StudentsWishes(vault,storage));
    }


    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, port));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (isStarted) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    // select() is blocking but may still return with 0, check javadoc
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();

                        messageBuffer.clear();
                        int r = socketChannel.read(messageBuffer);
                        if (r <= 0) {
                            System.out.println("Nothing to read, closing channel");
                            socketChannel.close();
                            continue;
                        }

                        handleKeyIsReadable(key, messageBuffer);
                    } else if (key.isAcceptable()) {
                        handleKeyIsAcceptable(selector, key);
                    }

                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            System.err.println("There is a problem with the server socket: " + e.getMessage());
            System.err.println(e);
        }

        System.out.println("Server stopped");
    }


    public void stop() {
        isStarted = false;
    }

    private void handleKeyIsReadable(SelectionKey key, ByteBuffer buffer) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        buffer.flip();
        String message = new String(buffer.array(), 0, buffer.limit()).trim();

        System.out.println("Message [" + message + "] received from client " + socketChannel.getRemoteAddress());

        String command = message.split(" ")[0];
        String arguments = message.substring(message.indexOf(" ") + 1);

        String response = null;

        if(command.equals("register")){
            response = vault.tryToRegister(arguments);

        }else if(command.equals("login")){
            response = vault.tryToLogin(arguments);

        }else if(command.equals("disconnect")){
            response = vault.tryToDisconnect();
            // serialize the data from StudentsWishesStorage storage
             storage.saveStudents();
        }else if(command.equals("logout")){
            response = vault.tryToLogOut(arguments);
        }
        else{
            response = commandExecutor.execute(command,arguments);
        }

        if (response != null) {
            System.out.println("Sending response to client: " + response);
            response += System.lineSeparator();
            buffer.clear();
            buffer.put(response.getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            if(response.equals("[ Disconnected from server ]")){
                disconnect(key);
            }
        }
    }

    private void handleKeyIsAcceptable(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();
        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);

        System.out.println("Connection accepted from client " + accept.getRemoteAddress());
    }


    private void disconnect(SelectionKey key) throws IOException {
        key.channel().close();
        key.cancel();
    }


}

