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

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class TeamSceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    Alert a = new Alert(Alert.AlertType.NONE);

    @FXML
    Text teamName;


    public void initialize() {
        String curTeam = JavaPostgreSQL.getCurTeamName();
        teamName.setText(curTeam);
    }

    public void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/OuterLayer.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void editTeam(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/EditTeamScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void deleteTeam(ActionEvent event) throws IOException, SQLException {
        a.setAlertType(Alert.AlertType.CONFIRMATION);
        a.setTitle("Are you sure");
        a.setHeaderText("Are you sure you want to delete this team?");
        a.setContentText("Deleting a team deletes all its data");
        Optional<ButtonType> result = a.showAndWait();
        if (result.get() == ButtonType.OK) {
             JavaPostgreSQL.deleteTeam();

             FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/OuterLayer.fxml"));
             root = loader.load();
             stage = (Stage)((Node)event.getSource()).getScene().getWindow();
             scene = new Scene(root);
             scene.getStylesheets().add("/Resources/style.css");
             stage.setScene(scene);
             stage.show();
        }
    }
    public void eventScene (ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/EventsScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }



}
