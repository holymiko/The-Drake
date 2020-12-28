package thedrake.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button twoPlayers;

    @FXML
    public Button onePlayer;

    @FXML
    private Button online;

    @FXML
    private Button end;

    @FXML
    private VBox vBox;

    @FXML
    private Label textTitle;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        vBox.setAlignment(Pos.CENTER);
        vBox.setId("vbox");
        textTitle.setAlignment(Pos.TOP_CENTER);
        textTitle.setId("textTitle");
    }

    public void onEnd(){
        Stage stage = (Stage) end.getScene().getWindow();
        stage.close();
    }

    public void onTwoPlayers(){
        Stage primaryStage = (Stage) twoPlayers.getScene().getWindow();
        new StageConfig().game(primaryStage);
    }



}
