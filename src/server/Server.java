package server;

/**
 * This class is the main class which holds the Server properties of the game.
 * This class has instances of DatabaseConnector and SocketServer classes which
 * are the main systems of the server of the game.
 * @author Zafer Tan Çankırı
 */
class Server {

    private DatabaseConnector db;
    private String dbUsername;
    private String dbPassword;
    private String dbName;
    private SocketServer socketServer;
    private int socketPort;

    /**
     * Constructor for Server Class.
     */
    Server() {
        this.db = null;
        this.dbUsername = "";
        this.dbPassword = "";
        this.dbName = "";
        this.socketServer = null;
    }

    /**
     * This method initializes the database connection credentials.
     * @param host Hostname of the Database Server
     * @param port Port of the Database Server
     * @param username Username for the Database Server
     * @param password Password for the Database Server
     * @param dbname Database Name on the Database Server
     */
    void setDBCredentials(String host, int port, String username, String password, String dbname) {
       //this.db = new DatabaseConnector("localhost", 3306);
        this.dbUsername = username;
        this.dbPassword = password;
        this.dbName = dbname;
    }

    /**
     * This method initializes the port of socket connections.
     * @param socketPort The connection port for the Socket Server.
     */
    void setSocketPort(int socketPort) {
        this.socketPort = socketPort;
    }

    /**
     * This method starts the main systems of the server. (Database and SocketServer)
     */
    void start() {
        //db.openConnection(dbUsername, dbPassword, dbName);
        socketServer = new SocketServer(socketPort);
        socketServer.start();
    }

    /**
     * This method stops the main systems of the server. (Database and SocketServer)
     */
    void stop() {
        db.closeConnection();
        socketServer.stopHandlers();
        socketServer.interrupt();
    }

}
