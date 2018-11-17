package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is used to handle socket connections.
 * @author Zafer Tan Çankırı
 */
public class ClientSocketHandler extends Thread {

    private String serverIP;
    private int serverPort;
    private Socket socket;
    private BufferedReader incoming;
    private PrintWriter outgoing;
    private AtomicBoolean isActive;

    /**
     * Constructor for ServerSocketHandler Class.
     * @param serverIP IP of the server.
     * @param serverPort Connection port for the socket connection.
     */
    public ClientSocketHandler(String serverIP, int serverPort) {
        super("SocketHandlerThread");
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.isActive = new AtomicBoolean(false);

        try {
            this.socket = new Socket(serverIP, serverPort);
        } catch (IOException e) {
            this.outgoing = null;
            System.out.println(e.toString());
        }

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

                if (text != null) {
                    System.out.println("ClientSH: " + text);
                    QbitzApplication.getSceneController().onMessageReceived(text);
                }
                else {
                    isActive.set(false);
                    QbitzApplication.getSceneController().onExit();
                }
            }

            incoming.close();
            outgoing.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sends message to the client over the socket connection.
     * @param message The string message.
     */
    public void sendMessage(String message) {
        outgoing.println(message);
    }

    /**
     * This method sets the active status of the ServerSocketHandler.
     * @param status The active status.
     */
    public void setActiveStatus(boolean status) {
        this.isActive.set(status);
    }

}