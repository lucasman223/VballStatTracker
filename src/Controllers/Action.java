package Controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.CheckBox;

public class Action {
    private int id;
    private String name;

    private BooleanProperty remark = new SimpleBooleanProperty(this, "remark");

    public Action(int i, String n, boolean sel) {
        this.id = i;
        this.name = n;
        this.remark.set(sel);
    }

    public int getActionId() {
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

    public BooleanProperty remarkProperty() {
        return remark;
    }

    public boolean getRemark() {
        return remarkProperty().get();
    }

    public void setRemark(boolean r) {
        remarkProperty().set(r);
    }

}
