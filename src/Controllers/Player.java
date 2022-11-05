package Controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;

import java.io.IOException;

public class Player {
    private int number;
    private String name;
    private int player_id;
    private BooleanProperty remark = new SimpleBooleanProperty(this, "remark");

    public Player(int num, String s, int pID, boolean sel) {
        this.number = num;
        this.name = s;
        this.player_id = pID;
        this.remark.set(sel);
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

    public BooleanProperty remarkProperty() {
        return remark;
    }
    public boolean getRemark() {
        return remarkProperty().get();
    }

    public void setRemark(boolean r) {
        remarkProperty().set(r);
    }

    public void setNumber(int num) {
        this.number = num;
    }

    public void setName(String s) {
        this.name = s;
    }


}
