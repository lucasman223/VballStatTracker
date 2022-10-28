package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class TeamSettingsController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    Text teamName;

    @FXML
    TableView playerList = new TableView<Player>();
    @FXML
    TextField changeNameTF;

    public void initialize() throws IOException, SQLException {
        System.out.println("INITIALIZE SETTINGS METHOD CALLED");
        String curTeam = JavaPostgreSQL.getCurTeamName();
        teamName.setText(curTeam);

        //TODO initialize tableview
        TableColumn numberCol = new TableColumn<Player, Integer>("Number");
        numberCol.setCellValueFactory(new PropertyValueFactory<Player, Integer>("number"));

        TableColumn nameCol = new TableColumn<Player, String>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));

        playerList.getColumns().add(numberCol);
        playerList.getColumns().add(nameCol);

        playerList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        JavaPostgreSQL.queryTeamPlayers(playerList);

    }

    public void goBack(ActionEvent event) throws IOException {
        System.out.println("go back!");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/EditTeamScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void saveSettings(ActionEvent event) throws IOException {
        System.out.println("save settings!");
        if (changeNameTF.getText() != "") {
            JavaPostgreSQL.alterTeamNameDB(changeNameTF.getText());
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/EditTeamScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void deleteCol(ActionEvent event) throws IOException, SQLException {
        //TODO check if any table row was selected if not dont delete
        //Temporary solution, should prob find a better one
        Player p;
        try {
            p = (Player) playerList.getItems().get(playerList.getSelectionModel().getSelectedIndex());
            System.out.println(p.getPlayer_id());
            System.out.println("Deleting col from table");
            JavaPostgreSQL.deletePlayer(p.getPlayer_id());

            playerList.getItems().removeAll(playerList.getSelectionModel().getSelectedItem());
        } catch (Exception e){
            System.out.println(e);
        }

    }
}
