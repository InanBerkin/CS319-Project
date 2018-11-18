package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is used to hold socket operations.
 * It has a collection of SocketHandlers and it is responsible for
 * managing the socket connections with the clients.
 * It receives operation requests from clients, processes them, and sends responses.
 * This class is also an extension of Thread class. Therefore, it runs on a different thread
 * other than the main thread of the server application.
 * @author Zafer Tan Çankırı
 */
public class SocketServer extends Thread {

    private Server server;
    private ServerSocket serverSocket;
    private AtomicBoolean isActive;
    private ArrayList<ServerSocketHandler> clientList;
    private int port;

    /**
     * Constructor for SocketServer Class.
     * @param port The connection port of the socket connections.
     */
    SocketServer(int port, Server server) {
        this.port = port;
        this.isActive = new AtomicBoolean(false);
        this.clientList = new ArrayList<>();
        this.server = server;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method start the thread of SocketServer Class,
     * also it initializes the isActive property as true to start
     */
    @Override
    public synchronized void start() {
        super.start();
        isActive.set(true);
    }

    /**
     * This method stops the SocketHandlers in the collection.
     */
    void stopHandlers() {
        for (ServerSocketHandler serverSocketHandler : clientList)
            onExit(serverSocketHandler);
    }

    /**
     * This method runs the operations of the Thread of SocketServer class.
     */
    @Override
    public void run() {
        listen();
    }

    /**
     * This method is a callback method which is called when a client connected.
     * @param handler The handler for the socket connection with the client.
     */
    private void onConnect(ServerSocketHandler handler) {
        clientList.add(handler);
        System.out.println("QBitzApplication connected: " + handler.getConnectionIP());
    }

    /**
     * This method is a callback method which is called when a client sent a message through the socket connection.
     * @param handler The handler for the socket connection with the client.
     * @param message The message came through the socket connection.
     */
    void onMessageReceived(ServerSocketHandler handler, String message) {
        server.onMessageReceived(handler, message);
    }

    /**
     * This method is a callback method which is called when a client disconnected.
     * @param handler The handler for the socket connection with the client.
     */
    void onExit(ServerSocketHandler handler) {
        System.out.println("QBitzApplication exit!");
        handler.setActiveStatus(false);
        handler.interrupt();
    }

    /**
     * This method is responsible for listening the incoming socket connections.
     * It accepts the incoming socket connection requests, and adds them to the collection.
     * It also starts different threads for each socket connection.
     */
    private void listen() {
        while (isActive.get()) {
            try {
                Socket socket = serverSocket.accept();
                ServerSocketHandler handler = new ServerSocketHandler(socket,this);
                handler.start();
                onConnect(handler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}