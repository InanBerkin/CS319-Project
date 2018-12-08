package server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import server.models.User;

import java.util.HashMap;

/**
 * This class is the main class which holds the QBitzServer properties of the game.
 * This class has instances of DatabaseConnector and SocketServer classes which
 * are the main systems of the server of the game.
 * @author Zafer Tan Çankırı
 */
class QBitzServer {

    private DatabaseConnector db;
    private String dbUsername;
    private String dbPassword;
    private String dbName;
    private SocketServer socketServer;
    private int socketPort;
    private VerificationManager verificationManager;
    private ResetPasswordManager resetPasswordManager;
    private HashMap<Integer, User> userMap;

    /**
     * Constructor for QBitzServer Class.
     */
    private QBitzServer() {
        this.db = null;
        this.dbUsername = "";
        this.dbPassword = "";
        this.dbName = "";
        this.socketServer = null;
        this.verificationManager = new VerificationManager();
        this.resetPasswordManager = new ResetPasswordManager();
        this.userMap = new HashMap<>();
    }

    /**
     * This method initializes the database connection credentials.
     * @param host Hostname of the Database QBitzServer
     * @param port Port of the Database QBitzServer
     * @param username Username for the Database QBitzServer
     * @param password Password for the Database QBitzServer
     * @param dbname Database Name on the Database QBitzServer
     */
    private void setDBCredentials(String host, int port, String username, String password, String dbname) {
        this.db = new DatabaseConnector("localhost", 3306);
        this.dbUsername = username;
        this.dbPassword = password;
        this.dbName = dbname;
    }

    /**
     * This method initializes the port of socket connections.
     * @param socketPort The connection port for the Socket QBitzServer.
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
                String email = "";
                String username = "";
                int id = -1;

                if (msgObj.has("email")) {
                    email = msgObj.getString("email");
                    id = db.loginWithEmail(email, password);
                }
                else if (msgObj.has("username")) {
                    username = msgObj.getString("username");
                    id = db.loginWithUsername(username, password);
                }

                JSONObject respObj = new JSONObject();
                respObj.put("responseType", "login");

                if (id != -1) {
                    respObj.put("result", true);
                    respObj.put("id", id);
                    User tempUser = new User(username, email, password, id);
                    userMap.put(tempUser.getId(), tempUser);
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
            else if(msgObj.getString("requestType").equals("joinRoom")) {

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
        QBitzServer QBitzServer = new QBitzServer();
        QBitzServer.setDBCredentials("localhost", 3306, "root", "", "qbitz");
        QBitzServer.setSocketPort(9999);
        QBitzServer.start();
        System.out.println("QBitzServer Started!");
    }

}
