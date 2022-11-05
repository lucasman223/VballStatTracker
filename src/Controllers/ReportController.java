package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.List;

public class ReportController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    TableView reportTable = new TableView<Report>();
    @FXML
    Text teamNameAndEvent;

    ObservableList<Report> data = FXCollections.observableArrayList();;


    public void initialize() throws SQLException {
        String curTeam = JavaPostgreSQL.getCurTeamName();
        String curEvent = JavaPostgreSQL.getCurEventName();
        teamNameAndEvent.setText(curTeam + "- " + curEvent);

        //initalize a table of reports
        //every player in the event has one report

        //method that can get list of stats
        List<Stat> statList = JavaPostgreSQL.queryStatsList();
        //method that can get list of players in events
        List<Player> playerList = JavaPostgreSQL.queryPlayersObjects();
        //for loop that goes through each player and creates a report

        for (int i = 0; i < playerList.size(); i++) {
            Player p = playerList.get(i);
            Report r = new Report(statList, p);

            data.add(r);
        }

        initServeTable();
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

    public void initServeTable() {

        reportTable.getColumns().clear();

        TableColumn<Report, String> playerCol = new TableColumn<>("Player");
        playerCol.setCellValueFactory(new PropertyValueFactory<>("playerString"));

        TableColumn<Report, String> attemptsCol = new TableColumn<>("Attempts");
        attemptsCol.setCellValueFactory(new PropertyValueFactory<>("serveAttempts"));

        TableColumn<Report, String> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("serveRating"));

        TableColumn<Report, String> errorCol = new TableColumn<>("Error %");
        errorCol.setCellValueFactory(new PropertyValueFactory<>("serveError"));

        TableColumn<Report, String> aceCol = new TableColumn<>("Ace %");
        aceCol.setCellValueFactory(new PropertyValueFactory<>("serveAce"));

        reportTable.setItems(data);

        reportTable.getColumns().add(playerCol);
        reportTable.getColumns().add(attemptsCol);
        reportTable.getColumns().add(ratingCol);
        reportTable.getColumns().add(errorCol);
        reportTable.getColumns().add(aceCol);

        reportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        reportTable.setEditable(false);
    }

    public void initRecTable() {

        reportTable.getColumns().clear();

        //Attempts, Rating, Error%
        TableColumn<Report, String> playerCol = new TableColumn<>("Player");
        playerCol.setCellValueFactory(new PropertyValueFactory<>("playerString"));

        TableColumn<Report, String> attemptsCol = new TableColumn<>("Attempts");
        attemptsCol.setCellValueFactory(new PropertyValueFactory<>("recAttempts"));

        TableColumn<Report, String> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("recRating"));

        TableColumn<Report, String> errorCol = new TableColumn<>("Error %");
        errorCol.setCellValueFactory(new PropertyValueFactory<>("recError"));


        reportTable.setItems(data);

        reportTable.getColumns().add(playerCol);
        reportTable.getColumns().add(attemptsCol);
        reportTable.getColumns().add(ratingCol);
        reportTable.getColumns().add(errorCol);

        reportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        reportTable.setEditable(false);
    }

    public void initAttackTable() {

        reportTable.getColumns().clear();

        //Attempts, Kills, Errors, Efficency%
        TableColumn<Report, String> playerCol = new TableColumn<>("Player");
        playerCol.setCellValueFactory(new PropertyValueFactory<>("playerString"));

        TableColumn<Report, String> attemptsCol = new TableColumn<>("Attempts");
        attemptsCol.setCellValueFactory(new PropertyValueFactory<>("attackAttempts"));

        TableColumn<Report, String> ratingCol = new TableColumn<>("Total Kills");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("attackKill"));

        TableColumn<Report, String> errorCol = new TableColumn<>("Total Errors");
        errorCol.setCellValueFactory(new PropertyValueFactory<>("attackError"));

        TableColumn<Report, String> effCol = new TableColumn<>("Efficiency %");
        effCol.setCellValueFactory(new PropertyValueFactory<>("attackEff"));

        reportTable.setItems(data);

        reportTable.getColumns().add(playerCol);
        reportTable.getColumns().add(attemptsCol);
        reportTable.getColumns().add(ratingCol);
        reportTable.getColumns().add(errorCol);
        reportTable.getColumns().add(effCol);

        reportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        reportTable.setEditable(false);
    }

    public void initSetTable() {

        reportTable.getColumns().clear();

        // Attempts, Assist, Assist Error
        TableColumn<Report, String> playerCol = new TableColumn<>("Player");
        playerCol.setCellValueFactory(new PropertyValueFactory<>("playerString"));

        TableColumn<Report, String> attemptsCol = new TableColumn<>("Attempts");
        attemptsCol.setCellValueFactory(new PropertyValueFactory<>("setAttempts"));

        TableColumn<Report, String> ratingCol = new TableColumn<>("Assist %");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("setAssists"));

        TableColumn<Report, String> errorCol = new TableColumn<>("Error %");
        errorCol.setCellValueFactory(new PropertyValueFactory<>("setError"));


        reportTable.setItems(data);

        reportTable.getColumns().add(playerCol);
        reportTable.getColumns().add(attemptsCol);
        reportTable.getColumns().add(ratingCol);
        reportTable.getColumns().add(errorCol);

        reportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        reportTable.setEditable(false);
    }
}
