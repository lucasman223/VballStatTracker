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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OuterLayerController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Map<Integer, String> teams_map;

    @FXML
    Button tb1;
    @FXML
    VBox mainLayout;

    @FXML
    private void initialize() throws IOException{
        System.out.println("INITIALIZE METHOD CALLED");
//        Map<Integer, String> teams_map;
        try {
            teams_map = JavaPostgreSQL.queryTeams();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        HBox hboxItems = new HBox();
        hboxItems.setAlignment(Pos.CENTER);

        for (Map.Entry<Integer, String> me : teams_map.entrySet()) {
            Button team_button = new Button(me.getValue());
            team_button.setId(me.getKey().toString());
            hboxItems.getChildren().add(team_button);
            team_button.setOnAction(event -> {
                try {
                    TeamScene(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        Button addTeamsButton = new Button("Add a team +");
        hboxItems.getChildren().add(addTeamsButton);
        addTeamsButton.setOnAction(event -> {
            try {
                CreateTeamScene(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        mainLayout.getChildren().add(hboxItems);
    }



    private void CreateTeamScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/CreateTeamScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }

    private void TeamScene(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        JavaPostgreSQL.setCurTeamIDs(Integer.parseInt(source.getId()), teams_map.get(Integer.parseInt(source.getId())));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/TeamScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }
}
