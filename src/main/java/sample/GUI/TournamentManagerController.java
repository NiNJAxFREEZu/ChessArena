package sample.GUI;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import sample.Enums.Score;
import sample.Models.DTOs.GameDTO;
import sample.Service.PairingService;
import sample.Service.TournamentService;
import sample.Service.exceptions.HaveToEndTournamentException;

import java.net.URL;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Controller
public class TournamentManagerController implements Initializable {
    @FXML
    public Button startTournamentBtn;
    @FXML
    public Button nextRoundBtn;
    @FXML
    public Button saveTournamentBtn;
    @FXML
    public Button exitBtn;
    @FXML
    public TableView<GameDTO> roundTable;
    @FXML
    public TableColumn<GameDTO, String> whitePlayerCol;
    @FXML
    public TableColumn<GameDTO, String> blackPlayerCol;
    @FXML
    public TableColumn<GameDTO, Integer> chessboardNoCol;
    @FXML
    public TableColumn<GameDTO, StringProperty> scoreCol;
    @FXML
    public Label roundNoLabel;
    @FXML
    public VBox tournamentManagerVBox;
    @FXML
    public Alert uWant2SaveDialog;
    @FXML
    public Button showLeaderboardBtn;

    ObservableList<GameDTO> currentGamesList;
    List<GameDTO> modifiedGamesList;
    Pair<String, Boolean> tournamentAndIfFinishedWasClickedForIt;

    @Autowired
    private SplashScreenController splashScreenController;
    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private PairingService pairingService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellValueFactories();
        nextRoundBtn.setDisable(true);
        startTournamentBtn.setDisable(false);
        tournamentAndIfFinishedWasClickedForIt = Pair.of("Not yet set", false);
    }

    public void startTournament(ActionEvent actionEvent) {
        if (startTournamentBtn.getText().equals("Finish tournament")) {
            if (modifiedGamesList.stream().anyMatch(e -> e.getScore() == Score.NotFinished)) {
                showYouCannotEndRoundNoScoreInserted(
                        modifiedGamesList.stream()
                                .filter(e -> e.getScore() == Score.NotFinished)
                                .map(GameDTO::getChessboardNo)
                                .collect(Collectors.toList()));
                return;
            }
            finishTournament();
            startTournamentBtn.setDisable(true);
            return;
        }

        tournamentService.startTournament();
        startTournamentBtn.setDisable(true);
        nextRoundBtn.setDisable(false);
        refreshTable();
        updateRoundLabel();
    }

    private void finishTournament() {
        if (!tournamentAndIfFinishedWasClickedForIt.getFirst().equals(TournamentService.currentTournament.getName())) {
            tournamentService.finishTournament(modifiedGamesList);
            tournamentAndIfFinishedWasClickedForIt = Pair.of(TournamentService.currentTournament.getName(), true);
        }
        splashScreenController.openScoreboard(TournamentService.currentTournament.getPlayerList());
    }

    public void nextRound(ActionEvent actionEvent) {
        if (modifiedGamesList.stream().anyMatch(e -> e.getScore() == Score.NotFinished)) {
            showYouCannotEndRoundNoScoreInserted(
                    modifiedGamesList.stream()
                            .filter(e -> e.getScore() == Score.NotFinished)
                            .map(GameDTO::getChessboardNo)
                            .collect(Collectors.toList()));
            return;
        }

        try {
            tournamentService.nextRound(modifiedGamesList);
        } catch (HaveToEndTournamentException ex) {
            showHaveToEndTournamentAlert();
            splashScreenController.openScoreboard(TournamentService.currentTournament.getPlayerList());
            return;
        }

        refreshTable();
        updateRoundLabel();

        if (Integer.parseInt(roundNoLabel.getText()) >= TournamentService.currentTournament.getNumberOfRounds()) {
            startTournamentBtn.setText("Finish tournament");
            startTournamentBtn.setDisable(false);
            nextRoundBtn.setDisable(true);
        }
    }

    public void saveTournament() {
        boolean saveNormal = showSaveSaveAsDialog();
        showSavingConfirmationDialog();

        if (saveNormal) {
            if (uWant2SaveDialog.getResult() == ButtonType.YES) {
                tournamentService.saveToFile(null, null, this::showFileExistsAlert);
            }
        } else {
            String fileName = showSavingFileNameDialog();

            if (uWant2SaveDialog.getResult() == ButtonType.YES) {
                tournamentService.saveToFile(null, fileName, this::showFileExistsAlert);
            }
        }
    }

    public void exit() {
        if (showAreYouSureToExitDialog()) {
            splashScreenController.closeTournamentManager();
        }
    }

    private void updateRoundLabel() {
        roundNoLabel.setText(
                String.valueOf(TournamentService.getCurrentRoundNo())
        );
    }

    public void open() {
        refreshTable();
        updateRoundLabel();

        if (Integer.parseInt(roundNoLabel.getText()) >= TournamentService.currentTournament.getNumberOfRounds()) {
            if (tournamentAndIfFinishedWasClickedForIt.getFirst().equals(TournamentService.currentTournament.getName())) {
                startTournamentBtn.setText("Finish tournament");
                startTournamentBtn.setDisable(true);
                nextRoundBtn.setText("Next Round");
                nextRoundBtn.setDisable(true);
            } else {
                startTournamentBtn.setText("Finish tournament");
                startTournamentBtn.setDisable(false);
                nextRoundBtn.setText("Next Round");
                nextRoundBtn.setDisable(true);
            }
        } else if (Integer.parseInt(roundNoLabel.getText()) == 0) {
            startTournamentBtn.setText("Start tournament");
            startTournamentBtn.setDisable(false);
            nextRoundBtn.setText("Next Round");
            nextRoundBtn.setDisable(true);
        } else if (Integer.parseInt(roundNoLabel.getText()) < TournamentService.currentTournament.getNumberOfRounds()) {
            startTournamentBtn.setText("Start tournament");
            startTournamentBtn.setDisable(true);
            nextRoundBtn.setText("Next Round");
            nextRoundBtn.setDisable(false);
        }
    }

    public void openFromFile() {
        startTournamentBtn.setText("Start tournament");
        startTournamentBtn.setDisable(false);
        nextRoundBtn.setText("Next Round");
        nextRoundBtn.setDisable(true);

        String fileNameOrPath = showOpeningFileNameDialog();
        tournamentService.openFromFile(fileNameOrPath);
    }

    private void refreshTable() {
        currentGamesList = TournamentService.getCurrentGamesList();
        modifiedGamesList = new LinkedList<>(currentGamesList);
        roundTable.setItems(currentGamesList.sorted(Comparator.comparing(GameDTO::getChessboardNo)));
        roundTable.sort();
    }

    private void setCellValueFactories() {
        whitePlayerCol.setCellValueFactory(new PropertyValueFactory<>("playerWhiteShortName"));
        blackPlayerCol.setCellValueFactory(new PropertyValueFactory<>("playerBlackShortName"));
        chessboardNoCol.setCellValueFactory(new PropertyValueFactory<>("chessboardNo"));

        scoreCol.setCellValueFactory(i -> {
            final StringProperty value = new SimpleStringProperty(i.getValue().getScore().getStringValue());
            return Bindings.createObjectBinding(() -> value);
        });

        scoreCol.setCellFactory(col -> {
            TableCell<GameDTO, StringProperty> c = new TableCell<>();
            final ComboBox<String> comboBox = new ComboBox<>(Score.getValues());
            c.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue != null) {
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if (newValue != null) {
                    comboBox.valueProperty().bindBidirectional(newValue);

                }
            });

            comboBox.setOnAction(new EventHandler<ActionEvent>() {
                                     @SneakyThrows
                                     @Override
                                     public void handle(ActionEvent event) {
                                         Thread.sleep(100);
                                         if (!comboBox.getValue().equals(Score.NotFinished.getStringValue())) {
                                             int index = c.getTableRow().getIndex();
                                             modifiedGamesList.get(index).setScore(Score.resolveFromStringValue(comboBox.getValue()));
                                             System.out.println(String.format("\n !!!!!!!! \nAssigning %s to %s \n !!!!!!!! \n", comboBox.getValue(), modifiedGamesList.get(index)));
                                         }
                                     }
                                 }
            );
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node) null).otherwise(comboBox));
            return c;
        });
    }

    private String showOpeningFileNameDialog() {
        TextInputDialog textInputDialog = new TextInputDialog("Tournament");
        textInputDialog.setTitle("File name");
        textInputDialog.setHeaderText("Please enter path to your file (if only name specified, it'll take file from your Desktop)");
        return textInputDialog.showAndWait().orElse(null);
    }

    private boolean showAreYouSureToExitDialog() {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to exit tournament?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }

    private boolean showYouCannotEndRoundNoScoreInserted(List<Integer> chessboardsWithNoScore) {
        Alert alert = new Alert(
                Alert.AlertType.ERROR,
                String.format("You cannot proceed!\n" +
                                "Chessboards%s have no score set!",
                        chessboardsWithNoScore.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(", nr ", " (nr ", ")"))),
                ButtonType.OK
        );
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }

    private boolean showSaveSaveAsDialog() {
        ButtonType save = new ButtonType("Save");
        ButtonType saveAs = new ButtonType("Save as");
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "",
                save, saveAs);
        alert.showAndWait();
        return alert.getResult() == save;
    }

    private void showSavingConfirmationDialog() {
        uWant2SaveDialog = new Alert(
                Alert.AlertType.CONFIRMATION,
                String.format("Do you want to save %s?", TournamentService.currentTournament.getName()),
                ButtonType.YES, ButtonType.NO);
        uWant2SaveDialog.showAndWait();
    }

    public boolean showFileExistsAlert() {
        Alert alert = new Alert(
                Alert.AlertType.WARNING,
                "Such file already exists in given path, override?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }

    private String showSavingFileNameDialog() {
        TextInputDialog textInputDialog = new TextInputDialog("Tournament");
        textInputDialog.setTitle("File name");
        textInputDialog.setHeaderText("Please enter desired file name");
        return textInputDialog.showAndWait().orElse(null);
    }

    private void showHaveToEndTournamentAlert() {
        Alert alert = new Alert(
                Alert.AlertType.INFORMATION,
                "Tournament has ended!",
                ButtonType.OK
        );

        alert.showAndWait();
    }

    @FXML
    public void showLeaderBoard(ActionEvent actionEvent) {
        splashScreenController.openScoreboard(TournamentService.currentTournament.getPlayerList());
    }
}
