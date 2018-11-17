package server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import server.models.User;

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
    private VerificationManager verificationManager;
    private ResetPasswordManager resetPasswordManager;

    /**
     * Constructor for Server Class.
     */
    private Server() {
        this.db = null;
        this.dbUsername = "";
        this.dbPassword = "";
        this.dbName = "";
        this.socketServer = null;
        this.verificationManager = new VerificationManager();
        this.resetPasswordManager = new ResetPasswordManager();
    }

    /**
     * This method initializes the database connection credentials.
     * @param host Hostname of the Database Server
     * @param port Port of the Database Server
     * @param username Username for the Database Server
     * @param password Password for the Database Server
     * @param dbname Database Name on the Database Server
     */
    private void setDBCredentials(String host, int port, String username, String password, String dbname) {
        this.db = new DatabaseConnector("localhost", 3306);
        this.dbUsername = username;
        this.dbPassword = password;
        this.dbName = dbname;
    }

    /**
     * This method initializes the port of socket connections.
     * @param socketPort The connection port for the Socket Server.
     */
    private void setSocketPort(int socketPort) {
        this.socketPort = socketPort;
    }

    /**
     * This method starts the main systems of the server. (Database and SocketServer)
     */
    private void start() {
        db.openConnection(dbUsername, dbPassword, dbName);
        socketServer = new SocketServer(socketPort, this);
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

    void onMessageReceived(ServerSocketHandler handler, String message) {
        System.out.println("From " + handler.getConnectionIP() + ": " + message);

        if (isJSONValid(message)) {
            JSONObject msgObj = new JSONObject(message);

            if (msgObj.getString("requestType").equals("register")) {
                String username = msgObj.getString("username");
                String email = msgObj.getString("email");
                String password = msgObj.getString("password");

                JSONObject respObj = new JSONObject();
                respObj.put("responseType", "register");

                if (!db.userExists(username, email)) {
                    User user = new User();
                    user.setUsername(username);
                    user.setEmail(email);
                    user.setPassword(password);
                    verificationManager.sendVerificationCode(user);
                    respObj.put("result", true);
                }
                else {
                    respObj.put("result", false);
                }

                handler.sendMessage(respObj.toString());
            }
            else if (msgObj.getString("requestType").equals("verify")) {
                String email = msgObj.getString("email");
                String code = msgObj.getString("code");

                User user = verificationManager.checkVerificationCode(email, code);

                JSONObject respObj = new JSONObject();
                respObj.put("responseType", "verify");

                if (user != null) {
                    if (db.addUser(user.getUsername(), user.getPassword(), user.getEmail())) {
                        respObj.put("result", true);
                    }
                    else {
                        respObj.put("result", false);
                    }
                }
                else {
                    respObj.put("result", false);
                }

                handler.sendMessage(respObj.toString());
            }
            else if (msgObj.getString("requestType").equals("login")) {
                String password = msgObj.getString("password");
                int id = -1;

                if (msgObj.has("email")) {
                    String email = msgObj.getString("email");
                    id = db.loginWithEmail(email, password);
                }
                else if (msgObj.has("username")) {
                    String username = msgObj.getString("username");
                    id = db.loginWithUsername(username, password);
                }

                JSONObject respObj = new JSONObject();
                respObj.put("responseType", "login");

                if (id != -1) {
                    respObj.put("result", true);
                    respObj.put("id", id);
                }
                else {
                    respObj.put("result", false);
                }

                handler.sendMessage(respObj.toString());
            }
            else if (msgObj.getString("requestType").equals("resetPasswordRequest")) {
                String email = msgObj.getString("email");

                resetPasswordManager.sendPasswordResetCode(email);

                JSONObject respObj = new JSONObject();
                respObj.put("responseType", "resetPasswordRequest");
                respObj.put("result", true);

                handler.sendMessage(respObj.toString());
            }
            else if (msgObj.getString("requestType").equals("resetPassword")) {
                String email = msgObj.getString("email");
                String password = msgObj.getString("password");
                String code = msgObj.getString("code");

                JSONObject respObj = new JSONObject();
                respObj.put("responseType", "resetPasswordRequest");

                if (resetPasswordManager.checkVerificationCode(email, password)) {
                    respObj.put("result", db.resetPassword(email, password));
                }
                else {
                    respObj.put("result", false);
                }

                handler.sendMessage(respObj.toString());
            }
        }
    }

    private boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.setDBCredentials("localhost", 3306, "root", "", "qbitz");
        server.setSocketPort(9999);
        server.start();
        System.out.println("Server Started!");
    }

}
