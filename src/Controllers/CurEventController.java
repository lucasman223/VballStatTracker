package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class CurEventController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    Alert a = new Alert(Alert.AlertType.NONE);

    @FXML
    Text teamNameAndEvent;

    public void initialize() {
        String curTeam = JavaPostgreSQL.getCurTeamName();
        String curEvent = JavaPostgreSQL.getCurEventName();
        teamNameAndEvent.setText(curTeam + "- " + curEvent);
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

    public void deleteEvent(ActionEvent event) throws IOException, SQLException {
        a.setAlertType(Alert.AlertType.CONFIRMATION);
        a.setTitle("Are you sure");
        a.setHeaderText("Are you sure you want to delete this event?");
        a.setContentText("Deleting a event deletes all its data");
        Optional<ButtonType> result = a.showAndWait();

        if (result.get() == ButtonType.OK) {
            JavaPostgreSQL.deleteEvent(-1);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/EventsScene.fxml"));
            root = loader.load();
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add("/Resources/style.css");
            stage.setScene(scene);
            stage.show();
        }
    }

    public void goActionScene(ActionEvent event) throws IOException, SQLException {
        //TODO need to know if player list and action button list are populated
        FXMLLoader loader;

        if (JavaPostgreSQL.isEventInitialized()) {
            System.out.println("EVENT INTITALIZED");

            loader = new FXMLLoader(getClass().getResource("/Resources/TrackStatsScene.fxml"));
        }
        else {
            System.out.println("EVENT NOT INITIALZIED");

            loader = new FXMLLoader(getClass().getResource("/Resources/InitActionPlayersScene.fxml"));
        }

        //if player_list and action_list not populated
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }
}
