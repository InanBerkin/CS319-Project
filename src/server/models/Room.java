package server.models;


public class Room {

    private int id;
    private String name;
    private int gamemode;
    private int ownerid;
    private int players;
    private int maxplayers;
    private int entranceLevel;
    private int roomtype;
    private String roomcode;

    public Room(String name, int gamemode, int ownerid, int players, int entranceLevel) {
        this.id = 0;
        this.name = name;
        this.gamemode = gamemode;
        this.ownerid = ownerid;
        this.players = players;
        this.maxplayers = 0;
        this.entranceLevel = entranceLevel;
        this.roomtype = 0;
        this.roomcode = "";
    }

    public Room(int id, String name, int gamemode, int ownerid, int players, int maxplayers, int entranceLevel, int roomtype, String roomcode) {
        this.id = id;
        this.name = name;
        this.gamemode = gamemode;
        this.ownerid = ownerid;
        this.players = players;
        this.maxplayers = maxplayers;
        this.entranceLevel = entranceLevel;
        this.roomtype = roomtype;
        this.roomcode = roomcode;
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

    public int getMaxplayers() {
        return maxplayers;
    }

    public void setMaxplayers(int maxplayers) {
        this.maxplayers = maxplayers;
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
}