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
import java.util.Objects;

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

        for (Stat s: statList) {
            System.out.println("STAT ID IN STAT LIST: " + s.getStatID());
        }
        //method that can get list of players in events
        List<Player> playerList = JavaPostgreSQL.queryPlayersObjects();
        for (Player p: playerList) {
            System.out.println("PLAYER IN PLAYER LIST: " + p.getName());
        }
        //for loop that goes through each player and creates a report

        for (int i = 0; i < playerList.size(); i++) {
            Player p = playerList.get(i);
            Report r = new Report(statList, p);

            System.out.println(Objects.isNull(r));

            data.add(r);
        }

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

    public void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/CurEventScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }
}
