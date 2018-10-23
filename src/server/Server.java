package server;

class Server {

    private DatabaseConnector db;
    private String dbUsername;
    private String dbPassword;
    private String dbName;

    Server() {
        this.db = null;
        this.dbUsername = "";
        this.dbPassword = "";
        this.dbName = "";
    }

    void setDBCreditantials(String host, int port, String username, String password, String dbname) {
        this.db = new DatabaseConnector("localhost", 3306);
        this.dbUsername = username;
        this.dbPassword = password;
        this.dbName = dbname;
    }

    void start() {
        db.openConnection(dbUsername, dbPassword, dbName);
    }

    void stop() {
        db.closeConnection();
    }

}
