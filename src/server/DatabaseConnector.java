package server;

import server.models.Room;
import server.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to handle database operations.
 * It has specific methods for predefined database operations.
 * It also has dynamic query methods which can be used querying DB server easily.
 * @author Zafer Tan Çankırı
 */
class DatabaseConnector {

    private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String USER_TABLE = "users";
    private static final String ROOM_TABLE = "rooms";
    private static final int MIN_USERNAME_LENGTH = 4;
    private static final int MIN_PASSWORD_LENGTH = 8;

    private String dbHost;
    private int dbPort;
    private String dbUsername;
    private String dbPassword;
    private String dbName;

    private Connection connection;
    private PreparedStatement statement;

    /**
     * Constructor for DatabaseConnector Class.
     * @param dbHost The hostname for the Database Connection.
     * @param dbPort The port for the Database Connection.
     */
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

    /**
     * This method opens the database connection with the required credentials.
     * @param dbUsername The username for the database connection.
     * @param dbPassword The password for the database connection.
     * @param dbName The database name for the database connection.
     * @return Returns True if the connection is successful, otherwise is returns False.
     */
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

    /**
     * This method closes the database connection.
     * @return Returns True if the connection is closed successfully, otherwise is returns False.
     */
    boolean closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * This method adds a new user to the database.
     * @param username The username for the new record.
     * @param password The password for the new record.
     * @param email The e-mail for the new record.
     * @return Returns True if the operation is completed successfully, otherwise is returns False.
     */
    boolean addUser(String username, String password, String email) {
        if (isValidEMail(email) && username.length() >= MIN_USERNAME_LENGTH && password.length() >= MIN_PASSWORD_LENGTH)
            return executeUpdate(USER_TABLE,"INSERT INTO ### (`username`, `password`, `email`) VALUES (?, MD5(?), ?)", username, password, email);
        else
            return false;
    }

    boolean removeRoom(int roomID) {
        return executeUpdate(ROOM_TABLE,"DELETE FROM ### WHERE `id` = ?", roomID);
    }

    int addRoom(Room room) {
        executeUpdate(ROOM_TABLE,"INSERT INTO ### (`name`, `gamemode`, `ownerid`, `players`, `maxplayers`, `entrance_level`, `roomtype`, `roomcode`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                room.getName(), room.getGamemode(), room.getOwnerid(), room.getPlayers(), room.getMaxPlayers(), room.getEntranceLevel(), room.getRoomtype(), room.getRoomcode());

        ResultSet result = executeQuery(ROOM_TABLE,"SELECT `id` FROM ### WHERE `name` = ? AND `gamemode` = ? AND `ownerid` = ? AND `players` = ? AND `maxplayers` = ? AND `entrance_level` = ? AND `roomtype` = ? AND `roomcode` = ?",
                room.getName(), room.getGamemode(), room.getOwnerid(), room.getPlayers(), room.getMaxPlayers(), room.getEntranceLevel(), room.getRoomtype(), room.getRoomcode());

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

    /**
     * This method resets the progress of the user.
     * @param username The username of the user.
     * @return Returns True if the operation is completed successfully, otherwise is returns False.
     */
    boolean resetUser(String username) {
        return executeUpdate(USER_TABLE,"UPDATE ### SET `exp` = 0, `level` = 1, `wins` = 0, `loses` = 0 WHERE `username` = ?", username);
    }

    /**
     * This method finds the SQL ID of the user from its username.
     * @param username The username of the user.
     * @return Returns the SQL ID of the user if the operation is completed successfully, otherwise is returns -1.
     */
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

    /**
     * This method finds the SQL ID of the user from its e-mail.
     * @param email The username of the user.
     * @return Returns the SQL ID of the user if the operation is completed successfully, otherwise is returns -1.
     */
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

    User loginWithUsername(String username, String password) {
        ResultSet result = executeQuery(USER_TABLE,"SELECT * FROM ### WHERE `username` = ? AND `password` = MD5(?)", username, password);

        User user = null;

        try {
            if (result.next()) {
                user = new User(
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("email"),
                        result.getInt("id"),
                        result.getInt("exp"),
                        result.getInt("level"),
                        result.getInt("wins"),
                        result.getInt("loses")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    User loginWithEmail(String email, String password) {
        ResultSet result = executeQuery(USER_TABLE,"SELECT * FROM ### WHERE `email` = ? AND `password` = MD5(?)", email, password);

        User user = null;

        try {
            if (result.next()) {
                user = new User(
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("email"),
                        result.getInt("id"),
                        result.getInt("exp"),
                        result.getInt("level"),
                        result.getInt("wins"),
                        result.getInt("loses")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    boolean resetPassword(String email, String password) {
        if (password.length() >= MIN_PASSWORD_LENGTH)
            return executeUpdate(USER_TABLE,"UPDATE ### SET `password` = MD5(?) WHERE `email` = ?", password, email);
        else
            return false;
    }

    boolean userExists(String username, String email) {
        ResultSet result = executeQuery(USER_TABLE,"SELECT * FROM ### WHERE `username` = ? OR `email` = ?", username, email);

        try {
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * This method returns active game room list.
     * @return Returns active game room list.
     */
    ArrayList<Room> getRoomList() {
        ArrayList<Room> result = new ArrayList<>();

        ResultSet resultSet = executeQuery("rooms", "SELECT * FROM `rooms`, `users` WHERE users.id = rooms.ownerid");

        try {
            while (resultSet.next()) {
                Room room = new Room();
                room.setId(resultSet.getInt("id"));
                room.setName(resultSet.getString("name"));
                room.setGamemode(resultSet.getInt("gamemode"));
                room.setOwnerid(resultSet.getInt("ownerid"));
                room.setPlayers(resultSet.getInt("players"));
                room.setMaxPlayers(resultSet.getInt("maxplayers"));
                room.setEntranceLevel(resultSet.getInt("entrance_level"));
                room.setRoomtype(resultSet.getInt("roomtype"));
                room.setRoomcode(resultSet.getString("roomcode"));
                room.setOwnername(resultSet.getString("username"));

                result.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeStatement();
        return result;
    }

    /**
     * This method queries manipulating queries on the desired table.
     * @param table The table name which the operation runs on.
     * @param query The SQL query.
     * @param params The constraint parameters for the SQL query.
     * @return Returns True if the operation is completed successfully, otherwise is returns False.
     */
    private boolean executeUpdate(String table, String query, Object... params) {
        try {
            statement = connection.prepareStatement(query.replace("###", table));

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

    /**
     * This method queries selecting queries on the desired table.
     * @param table The table name which the operation runs on.
     * @param query The SQL query.
     * @param params The constraint parameters for the SQL query.
     * @return Returns a ResultSet object with the desired data in it
     *         if the operation completed successfully, otherwise it returns null.
     */
    private ResultSet executeQuery(String table, String query, Object... params) {
        ResultSet result = null;
        try {
            statement = connection.prepareStatement(query.replace("###", table));

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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * This method checks if an e-mail address is valid.
     * @param email The e-mail address.
     * @return Returns True if the e-mail address is valid, otherwise it returns False.
     */
    private boolean isValidEMail(String email) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    private void closeStatement() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}