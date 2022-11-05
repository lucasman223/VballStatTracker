package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class CreateEventController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    TextField eventNameTF;
    @FXML
    Text teamName;

   public void initialize() {
       String curTeam = JavaPostgreSQL.getCurTeamName();
       teamName.setText(curTeam);
   }

    public void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/EventsScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void addEvent(ActionEvent event) throws IOException {
        System.out.println("add event");
        if (eventNameTF.getText() != "") {
            System.out.println(eventNameTF.getText());

            try {
                JavaPostgreSQL.writeEventToDB(eventNameTF.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/EventsScene.fxml"));
            root = loader.load();
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add("/Resources/style.css");
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Invalid parameter: event name field empty");
        }
    }


}
