package Controllers;

public class Player {
    private int number;
    private String name;
    private int player_id;

    public Player() {
        this.number = -1;
        this.name = "";
        this.player_id = -1;
    }

    public Player(int num, String s, int pID) {
        this.number = num;
        this.name = s;
        this.player_id = pID;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public void setNumber(int num) {
        this.number = num;
    }

    public void setName(String s) {
        this.name = s;
    }
}
