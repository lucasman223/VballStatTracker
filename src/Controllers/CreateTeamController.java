package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class CreateTeamController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    Button addButton;
    @FXML
    TextField teamNameTF;

    public void goBack(ActionEvent event) throws IOException {
        System.out.println("go back!");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/OuterLayer.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void addTeam(ActionEvent event) throws IOException {
        //TODO check if teamNameTF is null if not dont add anything
        if (teamNameTF.getText() != "") {
            System.out.println("add team!");
            System.out.println(teamNameTF.getText());

            try {
                JavaPostgreSQL.writeTeamToDB(teamNameTF.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/OuterLayer.fxml"));
            root = loader.load();
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add("/Resources/style.css");
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("team name field empty!");
        }

    }
}
