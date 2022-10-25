import Controllers.OuterLayerController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Stage stage;
    private VBox mainLayout;
    private String css = this.getClass().getResource("Resources/style.css").toExternalForm();


    @Override
public void start(Stage stage) throws IOException {
        this.stage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Main.class.getResource("/Resources/OuterLayer.fxml"));
        mainLayout = fxmlLoader.load();
        Scene scene = new Scene(mainLayout);

        scene.getStylesheets().add(css);

        stage.setTitle("Volleyball Statistics Tracker");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();


    }
}