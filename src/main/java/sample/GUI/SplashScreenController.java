package sample.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
    public void newTournament(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXML/tournamentCreator.fxml"));
        Stage stage = new Stage();
        Parent tCreator = fxmlLoader.load();
        stage.setScene(new Scene(tCreator, 1024, 576));
        stage.setMinWidth(1024);
        stage.setMinHeight(576);
        stage.show();
    }
}

