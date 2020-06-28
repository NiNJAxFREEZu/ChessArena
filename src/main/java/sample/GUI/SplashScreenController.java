package sample.GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sample.Models.DTOs.PlayerDTO;

import java.io.IOException;
import java.util.List;

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
    public VBox tournamentManagerVBox;
    @FXML
    public VBox scoreboard;
    @FXML
    private TournamentCreatorController tournamentCreatorController;
    @FXML
    private CreatePlayerController createPlayerController;
    @FXML

    @Autowired
    private TournamentManagerController tournamentManagerController;

    @Autowired
    private ScoreboardController scoreboardController;

    @FXML
    public Button newTournamentButton;
    public Button openButton;
    public javafx.scene.control.Button closeButton;

    @FXML
    public void exit(ActionEvent actionEvent) {
        if (showAreYouSureToExitDialog()) {
                Platform.exit();
                System.exit(0);
        }
    }

    @FXML
    public void openFile(ActionEvent actionEvent) {
        tournamentManagerController.openFromFile();
        openTournamentManager();
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
        scoreboard.setVisible(false);
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

    public void openTournamentManager() {
        tournamentManagerVBox.setVisible(true);
        tournamentCreatorVBox.setVisible(false);
        createPlayerPopup.setVisible(false);
        borderPane.setVisible(false);
        toolbar.setVisible(false);
        scoreboard.setVisible(false);
        tournamentManagerController.open();
    }


    public void openScoreboard(List<PlayerDTO> playerDTOS) {
        scoreboard.setVisible(true);
        tournamentManagerVBox.setVisible(false);
        tournamentCreatorVBox.setVisible(false);
        createPlayerPopup.setVisible(false);
        borderPane.setVisible(false);
        toolbar.setVisible(false);

        scoreboardController.displayResults(playerDTOS);
    }

    public void closeTournamentManager() {
        tournamentManagerVBox.setVisible(false);
        borderPane.setVisible(true);
        toolbar.setVisible(true);
    }

    private boolean showAreYouSureToExitDialog() {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to exit?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }

    public void closeScoreBoard() {
        openTournamentManager();
    }
}

