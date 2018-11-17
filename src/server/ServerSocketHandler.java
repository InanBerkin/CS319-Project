package server;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is used to handle socket connections.
 * @author Zafer Tan Çankırı
 */
public class ServerSocketHandler extends Thread {

    private Socket socket;
    private SocketServer socketServer;
    private BufferedReader incoming;
    private PrintWriter outgoing;
    private AtomicBoolean isActive;

    /**
     * Constructor for ServerSocketHandler Class.
     * @param socket The socket object for the connection.
     * @param socketServer The instance of the SocketServer which holds
     *                     the collection of SocketHandlers.
     */
    ServerSocketHandler(Socket socket, SocketServer socketServer) {
        super("SocketHandlerThread");
        this.socket = socket;
        this.socketServer = socketServer;
        this.isActive = new AtomicBoolean(false);

        try {
            this.outgoing = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            this.outgoing = null;
            System.out.println(e.toString());
        }

        try {
            this.incoming = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            this.incoming = null;
            System.out.println(e.toString());
        }
    }

    /**
     * This method starts the thread of ServerSocketHandler.
     */
    @Override
    public synchronized void start() {
        super.start();
        isActive.set(true);
    }

    /**
     * This method handles the incoming messages over socket connection.
     */
    @Override
    public void run() {
        try {
            while (isActive.get()) {
                String text = incoming.readLine();

                if (text != null)
                    socketServer.onMessageReceived(this, text);
                else {
                    isActive.set(false);
                    socketServer.onExit(this);
                }
            }

            incoming.close();
            outgoing.close();
            socket.close();
        } catch (IOException e) {
            if (e.getMessage().equals("Connection reset")) {
                isActive.set(false);
                socketServer.onExit(this);
            }
        }

    }

    /**
     * This method sends message to the client over the socket connection.
     * @param message The string message.
     */
    void sendMessage(String message) {
        outgoing.println(message);
        System.out.println("To " + getConnectionIP() + " : " + message);
    }

    /**
     * This method sets the active status of the ServerSocketHandler.
     * @param status The active status.
     */
    void setActiveStatus(boolean status) {
        this.isActive.set(status);
    }

    /**
     * This method returns the IP of the client.
     * @return Returns the IP of the client.
     */
    String getConnectionIP() {
        return this.socket.getInetAddress().getHostAddress();
    }
}