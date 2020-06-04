package sample.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class SplashScreenController {
    @FXML
    public VBox tournamentCreatorVBox;
    @FXML
    public ToolBar toolbar;
    @FXML
    public BorderPane borderPane;
    @FXML
    public VBox createPlayerPopup;
    @FXML
    public VBox mainBackground;
    @FXML
    private TournamentCreatorController tournamentCreatorController;
    @FXML
    private CreatePlayerController createPlayerController;

    @FXML
    public Button newTournamentButton;
    public Button openButton;
    public javafx.scene.control.Button closeButton;

    @FXML
    public void exit(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void openFile(ActionEvent actionEvent) {
        //TODO open file
    }

    @FXML
    public void newTournament(ActionEvent actionEvent) throws IOException {
        borderPane.setVisible(false);
        toolbar.setVisible(false);
        tournamentCreatorVBox.setVisible(true);

    }

    public void openPlayerCreator() {
        mainBackground.setPrefWidth(createPlayerPopup.getPrefWidth());
        mainBackground.setPrefHeight(createPlayerPopup.getPrefHeight());
        tournamentCreatorVBox.setVisible(false);
        createPlayerPopup.setVisible(true);
    }

    public void closeTournamentCreator() {
        tournamentCreatorVBox.setVisible(false);
        createPlayerPopup.setVisible(false);

        borderPane.setVisible(true);
        toolbar.setVisible(true);
    }

    public void closePlayerCreator() {
        mainBackground.setPrefHeight(tournamentCreatorVBox.getPrefHeight());
        mainBackground.setPrefWidth(tournamentCreatorVBox.getPrefWidth());
        tournamentCreatorVBox.setVisible(true);
        createPlayerPopup.setVisible(false);
    }
}

