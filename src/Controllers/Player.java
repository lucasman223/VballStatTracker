package Controllers;

import javafx.scene.control.CheckBox;

public class Player {
    private int number;
    private String name;
    private int player_id;
    private boolean select;
    private CheckBox checkBox;

    public Player() {
        this.number = -1;
        this.name = "";
        this.player_id = -1;
        this.select = false;
        this.checkBox = new CheckBox();
    }

    public Player(int num, String s, int pID, boolean sel) {
        this.number = num;
        this.name = s;
        this.player_id = pID;
        this.select = sel;

        this.checkBox = new CheckBox();
        this.checkBox.setSelected(sel);

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
    public boolean getSelect() {
        return select;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setNumber(int num) {
        this.number = num;
    }

    public void setName(String s) {
        this.name = s;
    }

    public void setSelect(boolean s) {
        this.select = s;
    }

    public void setCheckBox(CheckBox cb) {
        this.checkBox = cb;
    }
}
