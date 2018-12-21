package server.models;

public class User {

    private String username;
    private String email;
    private String password;
    private String code;
    private int id;
    private int level;
    private int exp;
    private boolean inLobby;

    public User() {}

    public User(String username, String email, String password, int id, int exp, int level) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.id = id;
        this.exp = exp;
        this.level = level;
        this.inLobby = false;
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

    public boolean isInLobby() {
        return inLobby;
    }

    public void setInLobby(boolean inLobby) {
        this.inLobby = inLobby;
    }
}
