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
import java.util.ArrayList;
import java.util.List;

public class OuterLayerController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    Button tb1;
    @FXML
    VBox mainLayout;

    @FXML
    private void initialize() throws IOException{
        System.out.println("INITIALIZE METHOD CALLED");

        HBox hboxItems = new HBox();
        hboxItems.setAlignment(Pos.CENTER);
        List<Button> buttonlist = new ArrayList<>();
//        buttonlist.add(new Button("These"));
//        buttonlist.add(new Button("are"));
//        buttonlist.add(new Button("generated"));
//        buttonlist.add(new Button("buttons"));

        if (buttonlist.size() == 0) {
            Button addTeamsButton = new Button("Add a team +");
            hboxItems.getChildren().add(addTeamsButton);
            addTeamsButton.setOnAction(event -> {
                try {
                    CreateTeamScene(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        else {
            hboxItems.getChildren().addAll(buttonlist);
        }

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

    public void testAction(ActionEvent event) {
        System.out.println("test action");
    }
}
