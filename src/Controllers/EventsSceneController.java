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

public class EventsSceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private Map<Integer, String> events_map;
    @FXML
    Text teamName;
    @FXML
    VBox mainLayout;

    public void initialize() {
        String curTeam = JavaPostgreSQL.getCurTeamName();
        teamName.setText(curTeam);

        try {
            events_map = JavaPostgreSQL.queryEvents();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        HBox hboxItems = new HBox();
        hboxItems.setAlignment(Pos.CENTER);

        for (Map.Entry<Integer, String> me : events_map.entrySet()) {
            Button team_button = new Button(me.getValue());
            team_button.setId(me.getKey().toString());
            hboxItems.getChildren().add(team_button);
            team_button.setOnAction(event -> {
                try {
                    CurEventScene(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        Button addTeamsButton = new Button("Add an event +");
        hboxItems.getChildren().add(addTeamsButton);
        addTeamsButton.setOnAction(event -> {
            try {
                CreateEventScene(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        mainLayout.getChildren().add(hboxItems);
    }



    public void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/OuterLayer.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }

    private void CreateEventScene(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/CreateEventScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }

    private void CurEventScene(ActionEvent event) throws IOException{
        Node source = (Node) event.getSource();
        JavaPostgreSQL.setCurEventIDs(Integer.parseInt(source.getId()), events_map.get(Integer.parseInt(source.getId())));


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/CurEventScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }
}
