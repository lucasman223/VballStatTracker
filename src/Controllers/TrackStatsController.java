package Controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class TrackStatsController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    Text teamNameAndEvent;
    @FXML
    TableView reportTable = new TableView<Stat>();


    ObservableList<Stat> data;
    Map<Integer, String> players_map;

    @FXML
    VBox vBox;
    public void initialize() throws SQLException {
        String curTeam = JavaPostgreSQL.getCurTeamName();
        String curEvent = JavaPostgreSQL.getCurEventName();
        teamNameAndEvent.setText(curTeam + "- " + curEvent);

       initReportTable();

        players_map = JavaPostgreSQL.queryPlayersList();
        int totalAdded = 0;

        HBox hBoxItems1 = new HBox();
        hBoxItems1.setAlignment(Pos.CENTER);
        HBox hBoxItems2 = new HBox();
        hBoxItems2.setAlignment(Pos.CENTER);
        HBox hBoxItems3 = new HBox();
        hBoxItems3.setAlignment(Pos.CENTER);

        //maps players 3 per row
        for (Map.Entry<Integer, String> me : players_map.entrySet()) {
            Button player_button = new Button(me.getValue());
            player_button.setId(me.getKey().toString());
            totalAdded++;

            if (totalAdded <= 3) {
                hBoxItems1.getChildren().add(player_button);
                player_button.setOnAction(event -> {
                    try {
                        goTrackActionScene(event);
                    } catch (IOException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                if (totalAdded == 3) {
                    vBox.getChildren().add(hBoxItems1);
                }
            } else if (totalAdded <= 6) {
                hBoxItems2.getChildren().add(player_button);
                player_button.setOnAction(event -> {
                    try {
                        goTrackActionScene(event);
                    } catch (IOException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                if (totalAdded == 6) {
                    vBox.getChildren().add(hBoxItems2);
                }
            } else {
                hBoxItems3.getChildren().add(player_button);
                player_button.setOnAction(event -> {
                    try {
                        goTrackActionScene(event);
                    } catch (IOException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        if (totalAdded < 3) {
            vBox.getChildren().add(hBoxItems1);
        }
        if (totalAdded > 3 && totalAdded < 6) {
            vBox.getChildren().add(hBoxItems2);
        }
        if (totalAdded > 6) {
            vBox.getChildren().add(hBoxItems3);
        }
    }

    public void initReportTable() throws SQLException {
        reportTable.getColumns().clear();

        data = JavaPostgreSQL.queryStatsTimeline();

        TableColumn<Stat, String> playerCol = new TableColumn<>("Player");
        playerCol.setCellValueFactory(new PropertyValueFactory<>("playerString"));

        TableColumn<Stat, String> actionCol = new TableColumn<>("Action");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("actionString"));

        reportTable.setItems(data);

        reportTable.getColumns().add(playerCol);
        reportTable.getColumns().add(actionCol);
        reportTable .setEditable(false);
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

    public void goInitPlayerList(ActionEvent event) throws  IOException{
        //TODO change to dedicated settings page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/InitActionPlayersScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void goTrackActionScene(ActionEvent event) throws IOException, SQLException {
        Node source = (Node) event.getSource();
        JavaPostgreSQL.setCurPlayerIDs(Integer.parseInt(source.getId()));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/TrackActionScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void undoStat() throws SQLException {
        JavaPostgreSQL.undoStat();
        initReportTable();
    }
}
