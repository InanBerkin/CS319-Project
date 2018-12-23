package server.models;

public class User {

    public static final int OUT = -1;
    public static final int IN_LOBBY = 0;
    public static final int IN_START_COUNT = 1;
    public static final int IN_GAME = 2;

    private String username;
    private String email;
    private String password;
    private String code;
    private int id;
    private int level;
    private int exp;
    private int status;
    private boolean isEliminated;
    private int gatheredPoints;

    public User() {}

    public User(String username, String email, String password, int id, int exp, int level) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.id = id;
        this.exp = exp;
        this.level = level;
        this.status = OUT;
        this.isEliminated = false;
        this.gatheredPoints = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isEliminated() {
        return isEliminated;
    }

    public void setEliminated(boolean eliminated) {
        isEliminated = eliminated;
    }

    public int getGatheredPoints() {
        return gatheredPoints;
    }

    public void setGatheredPoints(int gatheredPoints) {
        this.gatheredPoints = gatheredPoints;
    }
}
