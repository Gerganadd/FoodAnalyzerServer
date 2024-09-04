package bg.sofia.uni.fmi.mjt;

import bg.sofia.uni.fmi.mjt.commands.Command;
import bg.sofia.uni.fmi.mjt.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.database.DatabaseManager;
import bg.sofia.uni.fmi.mjt.exceptions.ExceptionMessages;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchElementException;
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
    private static final String RESPOND_FORMATTER = "%s\n";
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
        } catch (IOException e) {
            Logger.getInstance().addException(Status.UNABLE_TO_START_SERVER, e.getMessage(), e);
            System.out.println(e.getMessage());
        }
    }

    public void start() throws IOException {
        Logger.getInstance().addLog(Status.OPEN_SERVER, "Server start successfully");

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
        } catch (IOException e) {
            Logger.getInstance().addException(Status.UNABLE_TO_START_SERVER, e.getMessage(), e);

            throw e;
        }
    }

    private void serviceAllReadySocketChannels() throws IOException {
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();

            if (key.isReadable()) {
                SocketChannel client = (SocketChannel) key.channel();

                String clientInput = getRequestFromClient(client);

                if (clientInput == null) { // client disconnect
                    keyIterator.remove();
                    saveData(); // save the information every time when client disconnect
                    break;
                }

                serviceClient(clientInput, client);
            } else if (key.isAcceptable()) {
                accept();
            }

            keyIterator.remove();
        }
    }

    private void serviceClient(String clientInput, SocketChannel clientChannel) throws IOException {
        try {
            Command clientCommand = CommandFactory.create(clientInput);
            String respond = clientCommand.execute();

            sendRespondToClient(respond, clientChannel);
        } catch (UnknownCommandException e) {
            Logger.getInstance().addException(Status.UNKNOWN_COMMAND,
                    e.getMessage(), e);

            sendRespondToClient(e.getMessage(), clientChannel);
        } catch (NoSuchElementException e) {
            Logger.getInstance().addException(Status.NOT_FOUND_ELEMENT,
                    e.getMessage(), e);

            sendRespondToClient(e.getMessage(), clientChannel);
        } catch (IOException e) {
            Logger.getInstance().addException(Status.UNABLE_TO_SERVICE_CHANNEL,
                    ExceptionMessages.UNABLE_TO_WRITE_TO_CLIENT, e);

            throw new IOException(ExceptionMessages.UNABLE_TO_SERVICE_CLIENT, e);
        }
    }

    private void accept() throws IOException {
        try {
            SocketChannel accept = serverSocketChannel.accept();

            accept.configureBlocking(false);
            accept.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } catch (IOException e) {
            Logger.getInstance().addException(Status.UNSUCCESSFUL_CONNECTION,
                    ExceptionMessages.UNABLE_TO_ACCEPT, e);

            throw new IOException(ExceptionMessages.UNABLE_TO_ACCEPT, e);
        }
    }

    private String getRequestFromClient(SocketChannel clientChannel) throws IOException {
        try {
            buffer.clear();

            int readBytesCount = clientChannel.read(buffer);
            if (readBytesCount < 0) {
                clientChannel.close();
                return null;
            }

            buffer.flip();

            byte[] clientInputBytes = new byte[buffer.remaining()];
            buffer.get(clientInputBytes);

            return new String(clientInputBytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Logger.getInstance().addException(
                    Status.UNABLE_TO_READ, ExceptionMessages.UNABLE_TO_READ_CLIENT_INPUT, e);

            throw new IOException(ExceptionMessages.UNABLE_TO_READ_CLIENT_INPUT, e);
        }
    }

    private void sendRespondToClient(String respond, SocketChannel clientChannel) throws IOException {
        try {
            String formattedRespond = String.format(RESPOND_FORMATTER, respond);

            buffer.clear();
            buffer.put(formattedRespond.getBytes());
            buffer.flip();

            clientChannel.write(buffer);
        } catch (IOException e) {
            Logger.getInstance().addException(
                    Status.UNABLE_TO_WRITE, ExceptionMessages.UNABLE_TO_WRITE_TO_CLIENT, e);

            throw new IOException(ExceptionMessages.UNABLE_TO_WRITE_TO_CLIENT, e);
        }
    }

    private void configureServerSocket() throws IOException {
        serverSocketChannel.bind(new InetSocketAddress(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void saveData() {
        DatabaseManager.getInstance().saveData();

        Logger.getInstance().saveLogs();
        Logger.getInstance().saveExceptions();
    }

    @Override
    public void close() {
        isWorking = false;

        if (selector.isOpen()) {
            selector.wakeup();
        }

        try {
            selector.close();
            serverSocketChannel.close();
        } catch (IOException e) {
            Logger.getInstance().addException(Status.CLOSE_SERVER,
                    ExceptionMessages.PROBLEM_WITH_CLOSING_THE_SERVER, e);
        }

        Logger.getInstance().addLog(Status.CLOSE_SERVER, "Server stop successfully");

        saveData();
    }
}
