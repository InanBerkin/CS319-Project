package client.GameModels;


import server.ServerSocketHandler;

import java.util.ArrayList;

public class Room {

    private int id;
    private boolean isPublic;
    private String name;
    private int gamemode;
    private String ownerName;
    private int players;
    private int maxPlayers;
    private int entranceLevel;
    private int roomtype;
    private String roomcode;
    private ArrayList<ServerSocketHandler> users;

    public Room(String name, int id, String ownerName,int gamemode, int players, int maxPlayers, int entranceLevel) {
        this.id = id;
        this.name = name;
        this.gamemode = gamemode;
        this.players = players;
        this.maxPlayers = maxPlayers;
        this.entranceLevel = entranceLevel;
        this.roomtype = 0;
        this.roomcode = "";
        this.ownerName = ownerName;
    }

    public Room(int id, String name, int gamemode, int players, int maxPlayers, int entranceLevel, int roomtype) {
        this.id = id;
        this.name = name;
        this.gamemode = gamemode;
        this.ownerName = "";
        this.players = players;
        this.maxPlayers = maxPlayers;
        this.entranceLevel = entranceLevel;
        this.roomtype = roomtype;
        this.roomcode = roomcode;
        this.users = new ArrayList<>();
        this.isPublic = true;
    }

    public Room() {
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}