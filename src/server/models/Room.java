package server.models;


import server.ServerSocketHandler;

import java.util.ArrayList;

public class Room {

    public static int PUBLIC = 0;
    public static int PRIVATE = 1;

    public static int RACE = 0;
    public static int ELIMINATION = 1;
    public static int IMAGE_RECREATION = 2;

    private int id;
    private String name;
    private int gameMode;
    private int ownerID;
    private String ownerName;
    private int players;
    private int maxPlayers;
    private int entranceLevel;
    private int roomType;
    private String roomCode;
    private ArrayList<ServerSocketHandler> users;
    private String encodedImage;
    private int boardSize;
    private ArrayList<FinishTime> finishTimes;
    private Counter counter;

    public Room(String name, int gameMode, int ownerID, int players, int entranceLevel) {
        this.id = 0;
        this.name = name;
        this.gameMode = gameMode;
        this.ownerID = ownerID;
        this.players = players;
        this.maxPlayers = 0;
        this.entranceLevel = entranceLevel;
        this.roomType = 0;
        this.roomCode = "";
        this.users = new ArrayList<>();
        this.encodedImage = "";
        this.boardSize = -1;
        this.finishTimes = new ArrayList<>();
    }

    public Room(int id, String name, int gameMode, int ownerID, int players, int maxPlayers, int entranceLevel, int roomType, String roomCode) {
        this.id = id;
        this.name = name;
        this.gameMode = gameMode;
        this.ownerID = ownerID;
        this.players = players;
        this.maxPlayers = maxPlayers;
        this.entranceLevel = entranceLevel;
        this.roomType = roomType;
        this.roomCode = roomCode;
        this.users = new ArrayList<>();
        this.encodedImage = "";
        this.boardSize = -1;
        this.finishTimes = new ArrayList<>();
    }

    public Room() {
        this.users = new ArrayList<>();
        this.encodedImage = "";
        this.boardSize = -1;
        this.finishTimes = new ArrayList<>();
    }

    public String getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getEntranceLevel() {
        return entranceLevel;
    }

    public void setEntranceLevel(int entranceLevel) {
        this.entranceLevel = entranceLevel;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public ArrayList<ServerSocketHandler> getUsers() {
        return users;
    }

    public void addUser(ServerSocketHandler userHandler) {
        users.add(userHandler);
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public void addFinishTime(String time, User user) {
        finishTimes.add(new FinishTime(time, user));
    }

    public ArrayList<FinishTime> getFinishTimes() {
        return finishTimes;
    }

    public FinishTime getUserFinishTime(User user) {
        for (FinishTime finishTime : finishTimes) {
            if (finishTime.user == user) {
                return finishTime;
            }
        }

        return null;
    }

    public boolean isStartable() {
        boolean allReady = true;

        for (ServerSocketHandler socketHandler : users) {
            if (socketHandler.getUser().getStatus() != User.IN_LOBBY) {
                allReady = false;
                break;
            }

        }

        return getPlayers() == getMaxPlayers() && allReady;
    }

    public Counter getCounter() {
        return counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    public int getCurrentPlayers() {
        int result = getPlayers();

        for (ServerSocketHandler socketHandler : users) {
            if (socketHandler.getUser().isEliminated())
                result--;
        }

        return result;
    }
}