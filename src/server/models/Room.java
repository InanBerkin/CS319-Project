package server.models;


import server.ServerSocketHandler;

import java.util.ArrayList;

public class Room {

    public static int PUBLIC = 0;
    public static int PRIVATE = 1;

    private int id;
    private String name;
    private int gamemode;
    private int ownerid;
    private int players;
    private int maxPlayers;
    private int entranceLevel;
    private int roomtype;
    private String roomcode;
    private ArrayList<ServerSocketHandler> users;
    private String encodedImage;

    public Room(String name, int gamemode, int ownerid, int players, int entranceLevel) {
        this.id = 0;
        this.name = name;
        this.gamemode = gamemode;
        this.ownerid = ownerid;
        this.players = players;
        this.maxPlayers = 0;
        this.entranceLevel = entranceLevel;
        this.roomtype = 0;
        this.roomcode = "";
        this.users = new ArrayList<>();
        this.encodedImage = "";
    }

    public Room(int id, String name, int gamemode, int ownerid, int players, int maxPlayers, int entranceLevel, int roomtype, String roomcode) {
        this.id = id;
        this.name = name;
        this.gamemode = gamemode;
        this.ownerid = ownerid;
        this.players = players;
        this.maxPlayers = maxPlayers;
        this.entranceLevel = entranceLevel;
        this.roomtype = roomtype;
        this.roomcode = roomcode;
        this.users = new ArrayList<>();
        this.encodedImage = "";
    }

    public Room() {
        this.users = new ArrayList<>();
        this.encodedImage = "";
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

    public int getGamemode() {
        return gamemode;
    }

    public void setGamemode(int gamemode) {
        this.gamemode = gamemode;
    }

    public int getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(int ownerid) {
        this.ownerid = ownerid;
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

    public int getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(int roomtype) {
        this.roomtype = roomtype;
    }

    public String getRoomcode() {
        return roomcode;
    }

    public void setRoomcode(String roomcode) {
        this.roomcode = roomcode;
    }

    public ArrayList<ServerSocketHandler> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<ServerSocketHandler> users) {
        this.users = users;
    }

    public void addUser(ServerSocketHandler userHandler) {
        users.add(userHandler);
    }


}