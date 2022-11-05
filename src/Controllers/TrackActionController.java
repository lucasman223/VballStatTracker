package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class TrackActionController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    Text playerNameAndNum;
    @FXML
    VBox vBox;

    Map<Integer, String> action_map;

    public void initialize() throws SQLException {
        playerNameAndNum.setText(JavaPostgreSQL.getCurPlayerString());

        action_map = JavaPostgreSQL.queryActionList();

        int totalAdded = 0;

        HBox hBoxItems1 = new HBox();
        hBoxItems1.setAlignment(Pos.CENTER);
        hBoxItems1.getStyleClass().add("hbox");
        HBox hBoxItems2 = new HBox();
        hBoxItems2.setAlignment(Pos.CENTER);
        hBoxItems2.getStyleClass().add("hbox");
        HBox hBoxItems3 = new HBox();
        hBoxItems3.setAlignment(Pos.CENTER);
        hBoxItems3.getStyleClass().add("hbox");

        //maps players 3 per row
        for (Map.Entry<Integer, String> me : action_map.entrySet()) {
            Button action_button = new Button(me.getValue());
            action_button.setId(me.getKey().toString());
            totalAdded++;

            if (totalAdded <= 4) {
                hBoxItems1.getChildren().add(action_button);
                action_button.setOnAction(event -> {
                    try {
                        goTrackPlayerScene(event);
                    } catch (IOException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                if (totalAdded == 4) {
                    vBox.getChildren().add(hBoxItems1);
                }
            } else if (totalAdded <= 8) {
                hBoxItems2.getChildren().add(action_button);
                action_button.setOnAction(event -> {
                    try {
                        goTrackPlayerScene(event);
                    } catch (IOException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                if (totalAdded == 8) {
                    vBox.getChildren().add(hBoxItems2);
                }
            } else {
                hBoxItems3.getChildren().add(action_button);
                action_button.setOnAction(event -> {
                    try {
                        goTrackPlayerScene(event);
                    } catch (IOException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        if (totalAdded < 4) {
            vBox.getChildren().add(hBoxItems1);
        }
        if (totalAdded > 4 && totalAdded < 8) {
            vBox.getChildren().add(hBoxItems2);
        }
        if (totalAdded > 8) {
            vBox.getChildren().add(hBoxItems3);
        }

    }

    public void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/TrackStatsScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void goTrackPlayerScene(ActionEvent event) throws SQLException, IOException {
        Node source = (Node) event.getSource();
        JavaPostgreSQL.writeStatToDB(Integer.parseInt(source.getId()));

        goBack(event);
    }
}
