package Controllers;

public class Player {
    private int number;
    private String name;

    public Player() {
        this.number = -1;
        this.name = "";
    }

    public Player(int num, String s) {
        this.number = num;
        this.name = s;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public void setNumber(int num) {
        this.number = num;
    }

    public void setName(String s) {
        this.name = s;
    }
}
