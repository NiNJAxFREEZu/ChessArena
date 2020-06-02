package sample.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class SplashScreenController {

    @FXML
    public Button newTournamentButton;
    public Button openButton;
    public javafx.scene.control.Button closeButton;

    @FXML
    public void exit(ActionEvent actionEvent){
        Stage stage = (Stage)closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void openFile(ActionEvent actionEvent) {
        //TODO open file
    }

    @FXML
    public void newTournament(ActionEvent actionEvent) {
        //TODO new tournament
    }
}
