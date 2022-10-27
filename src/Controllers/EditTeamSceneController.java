package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class EditTeamSceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    Text teamName;

    public void initialize() throws IOException {
        System.out.println("INITIALIZE TEAM METHOD CALLED");
        String curTeam = JavaPostgreSQL.getCurTeamName();
        teamName.setText(curTeam);
    }

    public void goBack(ActionEvent event) throws IOException {
        System.out.println("go back!");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/TeamScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }
}
