package sample.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    public VBox tournamentManagerVBox;
    @FXML
    private TournamentCreatorController tournamentCreatorController;
    @FXML
    private CreatePlayerController createPlayerController;
    @FXML
    private TournamentManagerController tournamentManagerController;

    @FXML
    public Button newTournamentButton;
    public Button openButton;
    public javafx.scene.control.Button closeButton;

    @FXML
    public void exit(ActionEvent actionEvent) {
        if (showAreYouSureToExitDialog()) {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
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
        tournamentManagerController.open();
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
}

