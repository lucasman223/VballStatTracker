package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CreateTeamController {
    @FXML
    Button addButton;
    @FXML
    Button backButton;
    @FXML
    TextField teamNameTF;

    public void goBack(ActionEvent event) throws IOException {
        System.out.println("go back!");
    }

    public void addTeam(ActionEvent event) {
        System.out.println("add team!");
    }
}
