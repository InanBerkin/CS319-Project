package server.models;

public class Progress {

    private final int THRESHOLD = 100;
    private int xp;
    private int level;
    private int xpLimit;


    public Progress() {
        xp = 0;
        level = 1;
        xpLimit = level*THRESHOLD;
    }


    // true: level increased
    // false: level not changed
    public boolean increaseXP(int xpToAdd) {
        this.xp = this.xp + xpToAdd;
        return updateLevel();
    }

    public boolean updateLevel() {
        if(xp >= xpLimit) {
            level++;
            int leftover = xp - xpLimit;
            xpLimit = level*THRESHOLD;
            xp = 0;
            return true;
        }
        return false;
    }

    public int getTHRESHOLD() {
        return THRESHOLD;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXpLimit() {
        return xpLimit;
    }

    public void setXpLimit(int xpLimit) {
        this.xpLimit = xpLimit;
    }
}
