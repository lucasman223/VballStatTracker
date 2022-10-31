package Controllers;

import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class InitActionPlayersController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    Text teamNameAndEvent;
    @FXML
    TableView playerList = new TableView<Player>();

    ObservableList<Player> data;


    public void initialize() throws SQLException {
        String curTeam = JavaPostgreSQL.getCurTeamName();
        String curEvent = JavaPostgreSQL.getCurEventName();
        teamNameAndEvent.setText(curTeam + "- " + curEvent);

        data = JavaPostgreSQL.queryEventPlayerList();

        TableColumn<Player, String> numberCol = new TableColumn<>("Number");
        numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<Player, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Player, Boolean> checkedCol = new TableColumn<>("Selected");
        checkedCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkedCol));
        checkedCol.setCellValueFactory(cellData -> cellData.getValue().remarkProperty());

        playerList.setItems(data);

        playerList.getColumns().add(numberCol);
        playerList.getColumns().add(nameCol);
        playerList.getColumns().add(checkedCol);

        playerList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        playerList.setEditable(true);
    }

    public void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/CurEventScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void initActionButtonsScene(ActionEvent event) throws IOException, SQLException {
        //TODO check if none were selected if so one must be selected
        JavaPostgreSQL.alterEventPlayerList(data);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/InitActionActionsScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }
}
