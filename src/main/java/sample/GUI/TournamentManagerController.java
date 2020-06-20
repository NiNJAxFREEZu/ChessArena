package sample.GUI;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sample.Enums.Score;
import sample.Models.DTOs.GameDTO;
import sample.Service.PairingService;
import sample.Service.TournamentService;

import java.net.URL;
import java.util.ResourceBundle;

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
    public TableColumn<GameDTO, Score> scoreCol;
    @FXML
    public Label roundNoLabel;
    @FXML
    public VBox tournamentManagerVBox;

    private ObservableList<GameDTO> gamesList;

    @Autowired
    private SplashScreenController splashScreenController;
    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private PairingService pairingService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellValueFactories();
    }

    public void open() {

    }

    public void openFromFile() {
        String fileNameOrPath = showOpeningFileNameDialog();
        tournamentService.openFromFile(fileNameOrPath);
    }

    public void start() {

    }

    public void nextRound() {

    }

    public void saveTournament() {

    }

    public void exit() {
        if (showAreYouSureToExitDialog()) {
            splashScreenController.closeTournamentManager();
        }
    }

    private String showOpeningFileNameDialog() {
        TextInputDialog textInputDialog = new TextInputDialog("Tournament");
        textInputDialog.setTitle("File name");
        textInputDialog.setHeaderText("Please enter path to your file (if only name specified, it'll take file from your Desktop)");
        return textInputDialog.showAndWait().orElse(null);
    }

    private void setCellValueFactories() {
        whitePlayerCol.setCellValueFactory(new PropertyValueFactory<>("playerWhiteShortName"));
        blackPlayerCol.setCellValueFactory(new PropertyValueFactory<>("playerBlackShortName"));
        chessboardNoCol.setCellValueFactory(new PropertyValueFactory<>("chessboardNo"));
        scoreCol.setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<Score>() {

            @Override
            public String toString(Score score) {
                return score.getStringValue();
            }

            @Override
            public Score fromString(String string) {
                return Score.resolveFromStringValue(string);
            }
        }, Score.values()));
    }

    private boolean showAreYouSureToExitDialog() {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to exit tournament?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
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

}