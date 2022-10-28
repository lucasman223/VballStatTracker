package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class EditTeamSceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    Text teamName;
    @FXML
    TableView playerList = new TableView<Player>();

    public void initialize() throws IOException, SQLException {
        String curTeam = JavaPostgreSQL.getCurTeamName();
        teamName.setText(curTeam);

        TableColumn numberCol = new TableColumn<Player, Integer>("Number");
        numberCol.setCellValueFactory(new PropertyValueFactory<Player, Integer>("number"));

        TableColumn nameCol = new TableColumn<Player, String>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));

        playerList.getColumns().add(numberCol);
        playerList.getColumns().add(nameCol);

        playerList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        JavaPostgreSQL.queryTeamPlayers(playerList);

        playerList.setSelectionModel(null);
    }

    public void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/TeamScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void teamSettingsScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/TeamSettings.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void addPlayerScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/addPlayerScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }
}
