package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class addPlayerController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    Text teamText;
    @FXML
    TextField nameTF;
    @FXML
    TextField numTF;

    public void initialize() throws IOException, SQLException {
        System.out.println("INITIALIZE ADD PLAYER ");
        teamText.setText(JavaPostgreSQL.getCurTeamName());
    }
    public void goBack(ActionEvent event) throws IOException {
        System.out.println("go back!");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/EditTeamScene.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/Resources/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void addPlayer(ActionEvent event) throws IOException {

        //TODO update code to be conventional
        //TODO add an alert to show that fields were invalid

        String isNum = numTF.getText();

        if (isNum.matches("-?\\d+")) {
            System.out.println("a  nnum");
        }
        else {
            System.out.println("NOT NUM");
            return;
        }


        if (nameTF.getText() != "" && numTF.getText() != "") {
            System.out.println("adding player");
            String name = nameTF.getText();
            int num = Integer.parseInt(numTF.getText());
            if (num < 0 || num > 100) {
                System.out.println("TOO BIG OR TOO SMALl");
                return;
            }

            try {
                JavaPostgreSQL.writePlayerToDB(num, name);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            //TODO alert user that player was added

            nameTF.clear();
            numTF.clear();
        }


    }


}
