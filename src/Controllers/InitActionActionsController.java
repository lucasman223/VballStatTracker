package Controllers;

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

public class InitActionActionsController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    Text teamNameAndEvent;
    @FXML
    TableView actionList = new TableView<Action>();

    ObservableList<Action> data;
    public void initialize() throws SQLException {
        String curTeam = JavaPostgreSQL.getCurTeamName();
        String curEvent = JavaPostgreSQL.getCurEventName();
        teamNameAndEvent.setText(curTeam + "- " + curEvent);

        data = JavaPostgreSQL.queryActions();

        TableColumn<Action, String> actionCol = new TableColumn<>("Action");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Action, Boolean> checkedCol = new TableColumn<>("Selected");
        checkedCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkedCol));
        checkedCol.setCellValueFactory(cellData -> cellData.getValue().remarkProperty());


        actionList.setItems(data);
        actionList.getColumns().add(actionCol);
        actionList.getColumns().add(checkedCol);

        actionList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        actionList.setEditable(true);
    }

    public void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/InitActionPlayersScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void goTrackStats(ActionEvent event) throws SQLException, IOException {
        //TODO check if none were selected if so one must be selected
        JavaPostgreSQL.alterEventActionList(data);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/TrackStatsScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();

    }
}
