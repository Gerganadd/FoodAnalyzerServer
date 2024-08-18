package bg.sofia.uni.fmi.mjt;

import bg.sofia.uni.fmi.mjt.commands.Command;
import bg.sofia.uni.fmi.mjt.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.database.DatabaseManager;
import bg.sofia.uni.fmi.mjt.exceptions.UnknownCommandException;
import bg.sofia.uni.fmi.mjt.logs.Logger;
import bg.sofia.uni.fmi.mjt.logs.Status;

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

public class Server {
    private static final int DEFAULT_SERVER_PORT = 8888;
    private static final String DEFAULT_SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;

    private boolean isWorking;
    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        Logger.getInstance().addLog(Status.OPEN_SERVER, "Start server"); // + at time: ...

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            configureServerSocket(serverSocketChannel);

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            isWorking = true;

            while (isWorking) {
                int readyChannels = selector.select();
                if (readyChannels == 0) { //select() is blocking but may still return 0
                    continue;
                }

                serviceAllReadySocketChannels();
            }
        } catch (IOException e) {
            String message = "Server has problem with connection";
            Logger.getInstance().addException(Status.UNABLE_TO_START_SERVER, message, e);

            throw new RuntimeException("There is a problem with the server socket", e);
        }

        stop(); // ?
    }

    public void stop() {
        isWorking = false;

        if (selector.isOpen()) { // ?
            selector.wakeup();
        }

        DatabaseManager.getInstance().saveData();

        Logger.getInstance().addLog(Status.CLOSE_SERVER, "Server stop"); // + at time: ...

        Logger.getInstance().saveLogs();
        Logger.getInstance().saveExceptions();
    }

    private void serviceAllReadySocketChannels() {
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();

            if (key.isReadable()) {
                serviceCurrentSocketChannel(key);
            } else if (key.isAcceptable()) {
                accept(key);
            }

            keyIterator.remove();
        }
    }

    private void serviceCurrentSocketChannel(SelectionKey key) {
        try {
            SocketChannel client = (SocketChannel) key.channel();

            String clientInput = getRequestFromClient(client);
            if (clientInput == null) { // client disconnect
                return;
            }

            Command clientCommand = CommandFactory.create(clientInput);
            String respond = clientCommand.execute() + "\n";

            sendRespondToClient(respond, client);
        } catch (IOException e) {
            Logger.getInstance().addException(Status.UNABLE_TO_SERVICE_CHANNEL,
                    "Have problem with IO communication", e);
        } catch (UnknownCommandException | IllegalArgumentException e) {
            Logger.getInstance().addException(Status.UNABLE_TO_SERVICE_CHANNEL,
                    "Client input wrong command", e); // Unable to execute command
        }
    }

    private void accept(SelectionKey key) {
        try (ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel()) {
            SocketChannel accept = sockChannel.accept();

            accept.configureBlocking(false);
            accept.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            String message = "Client couldn't connect to server";
            Logger.getInstance().addException(Status.UNSUCCESSFUL_CONNECTION, message, e);
        }
    }

    private String getRequestFromClient(SocketChannel clientChannel) throws IOException {
        try {
            buffer.clear();

            int r = clientChannel.read(buffer);
            if (r < 0) {
                clientChannel.close();
                return null;
            }

            buffer.flip();

            byte[] clientInputBytes = new byte[buffer.remaining()];
            buffer.get(clientInputBytes);

            return new String(clientInputBytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            String message = "Couldn't read the client input";
            Logger.getInstance().addException(Status.UNABLE_TO_READ, message, e);

            throw e;
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

            throw e;
        }
    }

    private void configureServerSocket(ServerSocketChannel serverChannel) throws IOException {
        serverChannel.bind(new InetSocketAddress(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT));
        serverChannel.configureBlocking(false);
    }

}
