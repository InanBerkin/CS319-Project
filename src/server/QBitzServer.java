package server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import server.models.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.Random;

/**
 * This class is the main class which holds the QBitzServer properties of the game.
 * This class has instances of DatabaseConnector and SocketServer classes which
 * are the main systems of the server of the game.
 * @author Zafer Tan Çankırı
 */
class QBitzServer {

    private static final String ALPHANUMERIC = "abcdejghijklmnopqrstuxwyzABCDEFGHIJKLMNOPQRSTUXWYZ1234567890";

    private DatabaseConnector db;
    private String dbUsername;
    private String dbPassword;
    private String dbName;
    private SocketServer socketServer;
    private int socketPort;
    private VerificationManager verificationManager;
    private ResetPasswordManager resetPasswordManager;
    private ArrayList<Room> rooms;

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
        rooms = null;
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
        this.db = new DatabaseConnector(host, port);
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

    public void onConnect(ServerSocketHandler handler) {
        System.out.println("QBitzApplication connected: " + handler.getConnectionIP());
    }

    public void onExit(ServerSocketHandler handler) {
        for (Room room : rooms) {
            for (int i = 0; i < room.getUsers().size(); i++) {
                if (room.getUsers().get(i) == handler) {
                    room.getUsers().remove(i);
                    room.setPlayers(room.getPlayers() - 1);

                    if (room.getPlayers() == 0)
                        removeRoom(room);
                    else {
                        if (handler.getUser().getId() == room.getOwnerid())
                            changeOwner(room);

                        sendAnnouncementToOthers(room);
                    }
                    refreshRooms();
                    break;
                }
            }
        }

        System.out.println("QBitzApplication exit!");
    }

    public void onMessageReceived(ServerSocketHandler handler, String message) {
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
                String email;
                String username;
                User user = null;

                if (msgObj.has("email")) {
                    email = msgObj.getString("email");
                    user = db.loginWithEmail(email, password);
                }
                else if (msgObj.has("username")) {
                    username = msgObj.getString("username");
                    user = db.loginWithUsername(username, password);
                }

                JSONObject respObj = new JSONObject();
                respObj.put("responseType", "login");

                if (user != null) {
                    respObj.put("result", true);
                    respObj.put("userID", user.getId());
                    handler.setUser(user);
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
            else if(msgObj.getString("requestType").equals("createRoom")) {
                System.out.println("CreateRoomRecieved! -> " + msgObj.toString());
                String name = msgObj.getString("name");
                int gameMode = msgObj.getInt("gameMode");

                int players = 0;
                int maxPlayers = msgObj.getInt("maxPlayers");
                int entranceLevel = msgObj.getInt("entranceLevel");
                int roomType = msgObj.getInt("roomType");
                int boardSize = msgObj.getInt("boardSize");
                String roomCode = "";

                // id of the user
                int ownerId = handler.getUser().getId();

                Room newRoom = new Room(-1, name, gameMode, ownerId, players, maxPlayers, entranceLevel, roomType, roomCode);
                newRoom.setOwnername(handler.getUser().getUsername());
                newRoom.setBoardSize(boardSize);

                // send response to user.
                JSONObject respObj = new JSONObject();

                if (gameMode == 2) {
                    String encodedImage = getRandomImage(600);
                    newRoom.setEncodedImage(encodedImage);
                    respObj.put("encodedImage", encodedImage);
                }

                if (roomType == Room.PRIVATE) {
                    roomCode = generateCode(5);
                    newRoom.setRoomcode(roomCode);
                }

                int id = db.addRoom(newRoom);
                newRoom.setId(id);

                rooms.add(newRoom);

                respObj.put("responseType", "createRoom");
                respObj.put("roomID", id);

                if (roomType == Room.PRIVATE)
                    respObj.put("roomCode", roomCode);

                handler.sendMessage(respObj.toString());
                refreshRooms();
            }
            else if(msgObj.getString("requestType").equals("displayRooms")) {
                JSONArray roomList = new JSONArray();

                for (Room iterator : rooms) {
                    if (iterator.getRoomtype() == Room.PUBLIC) {
                        JSONObject room = new JSONObject();
                        room.put("roomID", iterator.getId());
                        room.put("name", iterator.getName());
                        room.put("gameMode", iterator.getGamemode());
                        room.put("players", iterator.getPlayers());
                        room.put("maxPlayers", iterator.getMaxPlayers());
                        room.put("entranceLevel", iterator.getEntranceLevel());
                        room.put("ownerName", iterator.getOwnername());

                        roomList.put(room);
                    }
                }

                JSONObject respObj = new JSONObject();
                respObj.put("responseType", "displayRooms");
                respObj.put("roomList", roomList);
                respObj.put("result", !roomList.isEmpty());

                handler.sendMessage(respObj.toString());
            }
            else if(msgObj.getString("requestType").equals("joinRoom")) {
                int id = msgObj.getInt("roomID");
                Room room = findRoomFromID(id);

                JSONObject respObj = new JSONObject();
                respObj.put("responseType", "joinRoom");

                if (room != null) {
                    if (room.getPlayers() != room.getMaxPlayers()) {
                        if (room.getEntranceLevel() <= handler.getUser().getLevel()) {
                            respObj.put("result", 0);

                            respObj.put("roomID", room.getId());
                            respObj.put("name", room.getName());
                            respObj.put("gameMode", room.getGamemode());
                            respObj.put("players", room.getPlayers());
                            respObj.put("maxPlayers", room.getMaxPlayers());
                            respObj.put("entranceLevel", room.getEntranceLevel());
                            respObj.put("name", room.getName());
                            respObj.put("ownerID", room.getOwnerid());

                            room.addUser(handler);
                            room.setPlayers(room.getPlayers() + 1);

                            handler.getUser().setInLobby(true);

                            JSONArray userList = new JSONArray();

                            for (ServerSocketHandler userHandler : room.getUsers()) {
                                User user = userHandler.getUser();

                                JSONObject userObj = new JSONObject();
                                userObj.put("name", user.getUsername());
                                userObj.put("id", user.getId());
                                userObj.put("level", user.getLevel());

                                userList.put(userObj);
                            }
                            respObj.put("userList", userList);

                            handler.sendMessage(respObj.toString());
                            sendAnnouncementToOthers(room);
                            refreshRooms();
                        }
                        else {
                            respObj.put("result", 2);
                            handler.sendMessage(respObj.toString());
                        }
                    }
                    else {
                        respObj.put("result", 1);
                        handler.sendMessage(respObj.toString());
                    }
                }
                else {
                    respObj.put("result", 3);
                    handler.sendMessage(respObj.toString());
                }
            }
            else if (msgObj.getString("requestType").equals("exitRoom")) {
                int roomID = msgObj.getInt("roomID");
                Room room = findRoomFromID(roomID);

                room.getUsers().remove(handler);
                room.setPlayers(room.getPlayers() - 1);

                handler.getUser().setInLobby(false);

                if (room.getPlayers() == 0)
                    removeRoom(room);
                else {
                    if (handler.getUser().getId() == room.getOwnerid())
                        changeOwner(room);

                    sendAnnouncementToOthers(room);
                }
                refreshRooms();
            }
            else if (msgObj.getString("requestType").equals("startCounter")) {
                int roomID = msgObj.getInt("roomID");
                Room room = findRoomFromID(roomID);

                startRoomCounter(room);
            }
            else if(msgObj.getString("requestType").equals("joinPrivateRoom")) {
                String code = msgObj.getString("roomCode");
                Room room = findRoomFromCode(code);

                JSONObject respObj = new JSONObject();
                respObj.put("responseType", "joinRoom");

                if (room != null) {
                    if (room.getPlayers() != room.getMaxPlayers()) {
                        if (room.getEntranceLevel() <= handler.getUser().getLevel()) {
                            respObj.put("result", 0);

                            respObj.put("roomID", room.getId());
                            respObj.put("name", room.getName());
                            respObj.put("gameMode", room.getGamemode());
                            respObj.put("players", room.getPlayers());
                            respObj.put("maxPlayers", room.getMaxPlayers());
                            respObj.put("entranceLevel", room.getEntranceLevel());
                            respObj.put("name", room.getName());
                            respObj.put("ownerID", room.getOwnerid());

                            room.addUser(handler);
                            room.setPlayers(room.getPlayers() + 1);

                            handler.getUser().setInLobby(true);

                            JSONArray userList = new JSONArray();

                            for (ServerSocketHandler userHandler : room.getUsers()) {
                                User user = userHandler.getUser();

                                JSONObject userObj = new JSONObject();
                                userObj.put("name", user.getUsername());
                                userObj.put("id", user.getId());
                                userObj.put("level", user.getLevel());

                                userList.put(userObj);
                            }
                            respObj.put("userList", userList);

                            handler.sendMessage(respObj.toString());
                            sendAnnouncementToOthers(room);
                            refreshRooms();
                        }
                        else {
                            respObj.put("result", 2);
                            handler.sendMessage(respObj.toString());
                        }
                    }
                    else {
                        respObj.put("result", 1);
                        handler.sendMessage(respObj.toString());
                    }
                }
                else {
                    respObj.put("result", 3);
                    handler.sendMessage(respObj.toString());
                }
            }
            else if(msgObj.getString("requestType").equals("submit")) {
                int roomID = msgObj.getInt("roomID");
                String finishTime = msgObj.getString("finishTime");

                Room room = findRoomFromID(roomID);
                room.addFinishTime(finishTime, handler.getUser());

                JSONObject respObj = new JSONObject();

                JSONArray userList = new JSONArray();

                ArrayList<JSONObject> tempForSort = new ArrayList<>();

                for (ServerSocketHandler userHandler : room.getUsers()) {
                    User user = userHandler.getUser();

                    FinishTime finish = room.getFromUser(user);
                    JSONObject userObj = new JSONObject();
                    userObj.put("name", user.getUsername());
                    userObj.put("id", user.getId());

                    if (finish != null) {
                        userObj.put("finishTime", finish.time);
                        userObj.put("rank", room.getFinishTimes().indexOf(finish) + 1);
                    }
                    else {
                        userObj.put("finishTime", "Solving...");
                        userObj.put("rank", 0);
                    }
                    tempForSort.add(userObj);
                }

                tempForSort.sort(new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject o1, JSONObject o2) {
                        return o1.getInt("rank") - o2.getInt("rank");
                    }
                });

                for (JSONObject obj : tempForSort)
                    userList.put(obj);

                respObj.put("finishList", userList);
                respObj.put("responseType", "submit");
                respObj.put("roomID", roomID);

                respObj.put("isGameFinished", room.countFinished() == room.getPlayers());

                for (ServerSocketHandler userHandler : room.getUsers()) {
                    userHandler.sendMessage(respObj.toString());
                }
            }
            else if(msgObj.getString("requestType").equals("backToLobby")) {
                int roomID = msgObj.getInt("roomID");
                Room room = findRoomFromID(roomID);

                handler.getUser().setInLobby(true);

                JSONObject respObj = new JSONObject();
                respObj.put("responseType", "backToLobby");

                respObj.put("roomID", room.getId());
                respObj.put("name", room.getName());
                respObj.put("gameMode", room.getGamemode());
                respObj.put("players", room.getPlayers());
                respObj.put("maxPlayers", room.getMaxPlayers());
                respObj.put("entranceLevel", room.getEntranceLevel());
                respObj.put("name", room.getName());
                respObj.put("ownerID", room.getOwnerid());

                JSONArray userList = new JSONArray();

                for (ServerSocketHandler userHandler : room.getUsers()) {
                    User user = userHandler.getUser();

                    JSONObject userObj = new JSONObject();
                    userObj.put("name", user.getUsername());
                    userObj.put("id", user.getId());
                    userObj.put("level", user.getLevel());

                    userList.put(userObj);
                }
                respObj.put("userList", userList);

                handler.sendMessage(respObj.toString());
                sendAnnouncementToOthers(room);
                refreshRooms();
            }
        }
    }

    private boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    private Room findRoomFromID(int id) {
        for (Room room : rooms) {
            if (room.getId() == id)
                return room;
        }

        return null;
    }

    private Room findRoomFromCode(String roomCode) {
        for (Room room : rooms) {
            if (room.getRoomcode().equals(roomCode))
                return room;
        }

        return null;
    }

    private void sendAnnouncementToOthers(Room room) {
        JSONObject json = new JSONObject();
        json.put("responseType", "userAnnouncement");

        JSONArray userList = new JSONArray();

        for (ServerSocketHandler userHandler : room.getUsers()) {
            User user = userHandler.getUser();

            JSONObject userObj = new JSONObject();
            userObj.put("name", user.getUsername());
            userObj.put("id", user.getId());
            userObj.put("level", user.getLevel());

            userList.put(userObj);
        }

        json.put("userList", userList);
        json.put("isStartable", room.getPlayers() == room.getMaxPlayers());

        for (ServerSocketHandler userHandler : room.getUsers()) {
            userHandler.sendMessage(json.toString());
        }
    }

    private void populateRoomsFromDB() {
        rooms = db.getRoomList();
        System.out.println("Game rooms are populated from DB.");
    }

    private String getRandomImage(int size) {
        String encodedImage = "";
        try {
            URL url = new URL("https://picsum.photos/" + size + "/" + size + "/?random");
            BufferedImage image = ImageIO.read(url);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);

            byte[] imgArray = baos.toByteArray();

            encodedImage = Base64.getEncoder().encodeToString(imgArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encodedImage;
    }

    private void startRoomCounter(Room room) {
        Counter counter = new Counter(Counter.BACKWARD, 5, 1, new CounterSignable() {
            @Override
            public void counterStopped() {
                PatternGenerator patternGenerator = new PatternGenerator(room.getBoardSize());
                JSONArray patternMatrix = new JSONArray(patternGenerator.generatePattern(false));

                JSONObject json = new JSONObject();

                json.put("responseType", "startGame");
                json.put("boardSize", room.getBoardSize());
                json.put("patternMatrix", patternMatrix);

                JSONArray userList = new JSONArray();

                for (ServerSocketHandler userHandler : room.getUsers()) {
                    User user = userHandler.getUser();

                    JSONObject userObj = new JSONObject();
                    userObj.put("name", user.getUsername());
                    userObj.put("id", user.getId());
                    userObj.put("level", user.getLevel());

                    userList.put(userObj);
                }

                json.put("userList", userList);

                for (ServerSocketHandler userHandler : room.getUsers()) {
                    userHandler.sendMessage(json.toString());
                    userHandler.getUser().setInLobby(false);
                }
            }

            @Override
            public void counterSignal(int count) {
                JSONObject json = new JSONObject();

                json.put("responseType", "startCounter");
                json.put("count", count);

                for (ServerSocketHandler userHandler : room.getUsers()) {
                    userHandler.sendMessage(json.toString());
                }
            }
        });

        counter.start();
    }

    private void refreshRooms() {
        for (ServerSocketHandler socketHandler : socketServer.getClientList()) {
            if (socketHandler.getUser() != null) {
                JSONArray roomList = new JSONArray();

                for (Room iterator : rooms) {
                    if (iterator.getRoomtype() == Room.PUBLIC) {
                        JSONObject room = new JSONObject();
                        room.put("roomID", iterator.getId());
                        room.put("name", iterator.getName());
                        room.put("gameMode", iterator.getGamemode());
                        room.put("players", iterator.getPlayers());
                        room.put("maxPlayers", iterator.getMaxPlayers());
                        room.put("entranceLevel", iterator.getEntranceLevel());
                        room.put("ownerName", iterator.getOwnername());

                        roomList.put(room);
                    }
                }

                JSONObject respObj = new JSONObject();
                respObj.put("responseType", "displayRooms");
                respObj.put("roomList", roomList);
                respObj.put("result", !roomList.isEmpty());

                socketHandler.sendMessage(respObj.toString());
            }
        }
    }

    private void removeRoom(Room room) {
        db.removeRoom(room.getId());

        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i) == room) {
                rooms.remove(i);
                break;
            }
        }
    }

    private void changeOwner(Room room) {
        int newOwnerID = room.getUsers().get(0).getUser().getId();
        String newOwnerName = room.getUsers().get(0).getUser().getUsername();
        db.changeRoomOwner(room.getId(), newOwnerID);

        room.setOwnerid(newOwnerID);
        room.setOwnername(newOwnerName);

        for (ServerSocketHandler socketHandler : room.getUsers()) {
            JSONObject json = new JSONObject();
            json.put("responseType", "changeOwner");
            json.put("ownerID", room.getOwnerid());

            socketHandler.sendMessage(json.toString());
        }
    }

    private String generateCode(int length) {
        StringBuilder result = new StringBuilder();
        Random generator = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = generator.nextInt(ALPHANUMERIC.length());
            result.append(ALPHANUMERIC.charAt(randomIndex));
        }

        return result.toString();
    }

    public static void main(String[] args) {
        QBitzServer QBitzServer = new QBitzServer();
        QBitzServer.setDBCredentials("138.197.189.245", 3306, "user", "qbitzpass", "qbitz");
        QBitzServer.setSocketPort(9999);
        QBitzServer.start();
        System.out.println("QBitzServer Started!");

        QBitzServer.populateRoomsFromDB();
    }
}