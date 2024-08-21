package bg.sofia.uni.fmi.mjt;

import bg.sofia.uni.fmi.mjt.commands.Command;
import bg.sofia.uni.fmi.mjt.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.database.DatabaseManager;
import bg.sofia.uni.fmi.mjt.exceptions.UnknownCommandException;
import bg.sofia.uni.fmi.mjt.logs.Logger;
import bg.sofia.uni.fmi.mjt.logs.Status;

import java.io.Closeable;
import java.io.IOException;

import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import java.util.Iterator;
import java.util.Set;

public class Server implements Closeable {
    private static final int DEFAULT_SERVER_PORT = 8888;
    private static final String DEFAULT_SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;

    private boolean isWorking;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

    public static void main(String[] args) {
        try (Server server = new Server()) {
            server.start();

            System.out.println("Stop server at " + System.currentTimeMillis());
        } catch (IOException e) {
            System.out.println("Problem with closing the server at " + System.currentTimeMillis());
        }
    }

    public void start() throws IOException {
        System.out.println("Start server at " + System.currentTimeMillis());
        Logger.getInstance().addLog(Status.OPEN_SERVER, "Start server at " + System.currentTimeMillis());

        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();

            configureServerSocket();
            isWorking = true;

            while (isWorking) {
                int readyChannels = selector.select();
                if (readyChannels == 0) { //select() is blocking but may still return 0
                    continue;
                }

                serviceAllReadySocketChannels();
            }
        } catch (IOException e) { //? Selector.close()
            String message = "Server has problem with connection";
            Logger.getInstance().addException(Status.UNABLE_TO_START_SERVER, message, e);

            throw new IOException("There is a problem with the server socket", e);
        } catch (UnknownCommandException e) {
            Logger.getInstance().addException(Status.UNABLE_TO_SERVICE_CHANNEL,
                    "Wrong command format", e); // Unable to execute command

            System.out.println("Wrong command");
        }
    }

    private void serviceAllReadySocketChannels() throws IOException, UnknownCommandException {
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();

            if (key.isReadable()) {
                System.out.println("Service client at " + System.currentTimeMillis());
                SocketChannel client = (SocketChannel) key.channel();

                String clientInput = getRequestFromClient(client);
                if (clientInput == null) { // client disconnect
                    keyIterator.remove();
                    break;
                }

                Command clientCommand = CommandFactory.create(clientInput);
                String respond = clientCommand.execute() + "\n";

                sendRespondToClient(respond, client);
            } else if (key.isAcceptable()) {
                accept();
            }

            keyIterator.remove();
        }
    }

    private void accept() throws IOException {
        System.out.println("Accept new client at " + System.currentTimeMillis());

        SocketChannel accept = serverSocketChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    private String getRequestFromClient(SocketChannel clientChannel) throws IOException {
        try {
            buffer.clear();

            int r = clientChannel.read(buffer);
            if (r < 0) {
                System.out.println("Closing chanel at: " + System.currentTimeMillis());
                clientChannel.close(); //?
                return null;
            }

            buffer.flip();

            byte[] clientInputBytes = new byte[buffer.remaining()];
            buffer.get(clientInputBytes);

            return new String(clientInputBytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            String message = "Couldn't read the client input";
            System.out.println(message); //
            Logger.getInstance().addException(Status.UNABLE_TO_READ, message, e);

            throw new IOException(message, e);
        }
    }

    private void sendRespondToClient(String respond, SocketChannel clientChannel) throws IOException {
        try {
            buffer.clear();
            buffer.put(respond.getBytes());
            buffer.flip();

            clientChannel.write(buffer);
        } catch (IOException e) {
            String message = "Couldn't send result to the client";
            Logger.getInstance().addException(Status.UNABLE_TO_WRITE, message, e);

            throw new IOException(message, e);
        }
    }

    private void configureServerSocket() throws IOException {
        serverSocketChannel.bind(new InetSocketAddress(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void close() throws IOException {
        isWorking = false;

        if (selector.isOpen()) { // ?
            selector.wakeup();
        }

        selector.close();
        serverSocketChannel.close();

        DatabaseManager.getInstance().saveData();

        Logger.getInstance().addLog(Status.CLOSE_SERVER, "Server stop at " + System.currentTimeMillis()); 

        Logger.getInstance().saveLogs();
        Logger.getInstance().saveExceptions();
    }
}
