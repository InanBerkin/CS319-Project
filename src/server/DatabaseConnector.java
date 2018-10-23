package server;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DatabaseConnector {

    private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String USER_TABLE = "users";
    private static final int MIN_USERNAME_LENGTH = 4;
    private static final int MIN_PASSWORD_LENGTH = 8;

    private String dbHost;
    private int dbPort;
    private String dbUsername;
    private String dbPassword;
    private String dbName;

    private Connection connection;

    DatabaseConnector(String dbHost, int dbPort) {
        this.dbHost = dbHost;
        this.dbPort = dbPort;
        this.dbUsername = "";
        this.dbPassword = "";

        this.connection = null;

        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    boolean openConnection(String dbUsername, String dbPassword, String dbName) {
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.dbName = dbName;

        try {
            String connURL = String.format("jdbc:mysql://%s:%s@%s:%d/%s", dbUsername, dbPassword, dbHost, dbPort, dbName);
            connection = DriverManager.getConnection(connURL);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    boolean closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    boolean addUser(String username, String password, String email) {
        if (isValidEMail(email) && username.length() >= MIN_USERNAME_LENGTH && password.length() >= MIN_PASSWORD_LENGTH)
            return executeUpdate(USER_TABLE,"INSERT INTO ### (`username`, `password`, `email`) VALUES (?, MD5(?), ?)", username, password, email);
        else
            return false;
    }

    boolean resetUser(String username) {
        return executeUpdate(USER_TABLE,"UPDATE ### SET `exp` = 0, `level` = 1, `wins` = 0, `loses` = 0 WHERE `username` = ?", username);
    }

    int getUserIDFromUsername(String username) {
        ResultSet result = executeQuery(USER_TABLE,"SELECT `id` FROM ### WHERE `username` = ?", username);

        try {
            if (result.next())
                return result.getInt("id");
            else
                return -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    int getUserIDFromEMail(String email) {
        ResultSet result = executeQuery(USER_TABLE,"SELECT `id` FROM ### WHERE `email` = ?", email);

        try {
            if (result.next())
                return result.getInt("id");
            else
                return -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private boolean executeUpdate(String table, String query, Object... params) {
        try {
            PreparedStatement statement = connection.prepareStatement(query.replace("###", table));

            for (int i = 0; i < params.length; i++) {
                if (params[i].getClass().equals(String.class)) {
                    statement.setString(i + 1, (String) params[i]);
                }
                else if (params[i].getClass().equals(Integer.class)) {
                    statement.setInt(i + 1, (Integer) params[i]);
                }
                else if (params[i].getClass().equals(Double.class)) {
                    statement.setDouble(i + 1, (Double) params[i]);
                }
                else if (params[i].getClass().equals(Date.class)) {
                    statement.setDate(i + 1, (Date) params[i]);
                }
                else if (params[i].getClass().equals(Boolean.class)) {
                    statement.setBoolean(i + 1, (Boolean) params[i]);
                }
            }

            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private ResultSet executeQuery(String table, String query, Object... params) {
        ResultSet result = null;
        try {
            PreparedStatement statement = connection.prepareStatement(query.replace("###", table));

            for (int i = 0; i < params.length; i++) {
                if (params[i].getClass().equals(String.class)) {
                    statement.setString(i + 1, (String) params[i]);
                }
                else if (params[i].getClass().equals(Integer.class)) {
                    statement.setInt(i + 1, (Integer) params[i]);
                }
                else if (params[i].getClass().equals(Double.class)) {
                    statement.setDouble(i + 1, (Double) params[i]);
                }
                else if (params[i].getClass().equals(Date.class)) {
                    statement.setDate(i + 1, (Date) params[i]);
                }
                else if (params[i].getClass().equals(Boolean.class)) {
                    statement.setBoolean(i + 1, (Boolean) params[i]);
                }
            }

            result = statement.executeQuery();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    private boolean isValidEMail(String email) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }
}
