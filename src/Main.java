import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
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