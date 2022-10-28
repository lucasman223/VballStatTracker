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
        teamText.setText(JavaPostgreSQL.getCurTeamName());
    }
    public void goBack(ActionEvent event) throws IOException {
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



        if (nameTF.getText() != "" && numTF.getText() != "") {
            if (!isNum.matches("-?\\d+")) {
                System.out.println("Invalid parameter: invalid number");
                return;
            }
            String name = nameTF.getText();
            int num = Integer.parseInt(numTF.getText());
            if (num < 0 || num > 100) {
                System.out.println("Invalid parameter: enter a number between 0-99");
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
        else {
            System.out.println("Invalid parameters: both fields should be filled");
        }


    }


}
