package sample.GUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sample.Enums.TournamentType;
import sample.Models.DTOs.CreatingTournamentForm;
import sample.Models.DTOs.PlayerDTO;
import sample.Service.TournamentService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class TournamentCreatorController implements Initializable {

    @FXML
    public Button cancelTournamentCreation;
    @FXML
    public Button next;
    @FXML
    public TabPane tabPane;
    @FXML
    public Button cancel;
    @FXML
    public Button addPlayer;
    @FXML
    public Button createTournament;
    public static TournamentType selectedTournamentType;
    public static Integer selectedAmountOfRounds;
    public static String tournamentName;
    @FXML
    public TableColumn<PlayerDTO, String> nameCol;
    @FXML
    public TableColumn<PlayerDTO, String> surnameCol;
    @FXML
    public TableColumn<PlayerDTO, String> licenseCol;
    @FXML
    public TableColumn<PlayerDTO, Integer> ratingCol;
    @FXML
    public TableColumn<PlayerDTO, String> genderCol;
    @FXML
    public TableColumn<PlayerDTO, String> clubCol;
    @FXML
    public TableColumn<PlayerDTO, String> titleCol;
    @FXML
    public TableView<PlayerDTO> entrantsTable;
    @FXML
    public MenuItem menuFileQuit;
    @FXML
    public Menu menuFile;
    @FXML
    public MenuBar menuBar;
    @FXML
    public VBox tournamentCreatorVBox;
    @FXML
    public ComboBox<String> tournamentTypeComboBox;
    @FXML
    public MenuItem newTournamentBtn;
    @FXML
    public MenuItem openBtn;
    @FXML
    public MenuItem saveBtn;
    @FXML
    public MenuItem saveAsBtn;
    @FXML
    public MenuItem preferencesBtn;
    @FXML
    public Button finishButton;
    private static boolean removingEntrantMode = false;
    private static boolean moreThanTwoPlayersPossible = true;

    @Autowired
    private SplashScreenController splashScreenController;
    @FXML
    public TextField amountOfRoundsText;
    @FXML
    public TextField tournamentNameText;
    @FXML
    public Alert uWant2SaveDialog;
    @Autowired
    private TournamentService tournamentService;
    @FXML
    public Button removeEntrant;

    @FXML
    public void loadComboBox() {
        tournamentTypeComboBox.setItems(TournamentType.getValues());
        tournamentTypeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent event) {
                Thread.sleep(100);
                moreThanTwoPlayersPossible = true;
                if (tournamentTypeComboBox.getValue().equals(TournamentType.RoundRobin.getStringValue())) {
                    amountOfRoundsText.setDisable(true);
                    amountOfRoundsText.setText("");
                } else if (tournamentTypeComboBox.getValue().equals(TournamentType.DoubleRoundRobin.getStringValue())) {
                    amountOfRoundsText.setDisable(true);
                    amountOfRoundsText.setText("");
                } else if (tournamentTypeComboBox.getValue().equals(TournamentType.HeadsUp.getStringValue())) {
                    moreThanTwoPlayersPossible = false;
                } else {
                    amountOfRoundsText.setDisable(false);
                }
                selectedTournamentType = TournamentType.resolveFromString(tournamentTypeComboBox.getValue());
            }
        });
    }

    @FXML
    public void exit(ActionEvent actionEvent) {
        splashScreenController.closeTournamentCreator();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadComboBox();
        setCellValueFactories();
        setNoOfRoundsTextBoxNumericOnly();
        setTournamentNameTextBoxProperty();
        initRowClick();
    }

    @FXML
    public void openCreatePlayerPopup() {
        if (!removingEntrantMode) {
            splashScreenController.openPlayerCreator();
        }
    }

    @FXML
    public void closeTournamentCreator(ActionEvent actionEvent) {
        splashScreenController.closeTournamentCreator();
    }

    @FXML
    public void nextTab(ActionEvent actionEvent) {
        openCreatePlayerPopup();
        ObservableList<Tab> tabs = tabPane.getTabs();
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.select(tabs.get(1));
    }

    public void addPlayer(PlayerDTO rowData) {
        if (!entrantsTable.getItems().contains(rowData)) {
            entrantsTable.getItems().add(rowData);
        }
    }

    public void saveDefault(ActionEvent actionEvent) {
        if (assertThatPlayersListIsOfGoodSize()) return;

        checkIfRoundRobinAndSetAdequateAmountOfRounds();

        CreatingTournamentForm creatingTournamentForm = CreatingTournamentForm.builder()
                .name(tournamentName)
                .date(System.currentTimeMillis())
                .tournamentType(selectedTournamentType)
                .amountOfRounds(selectedAmountOfRounds)
                .build();
        List<PlayerDTO> players = entrantsTable.getItems();

        tournamentService.createTournament(creatingTournamentForm, players);

        showSavingConfirmationDialog();

        if (uWant2SaveDialog.getResult() == ButtonType.YES) {
            tournamentService.saveToFile(null, null, this::showFileExistsAlert);
        }
    }

    private void checkIfRoundRobinAndSetAdequateAmountOfRounds() {
        if (selectedTournamentType.equals(TournamentType.RoundRobin)) {
            selectedAmountOfRounds = entrantsTable.getItems().size() - 1;
        }
        if (selectedTournamentType.equals(TournamentType.DoubleRoundRobin)) {
            selectedAmountOfRounds = (entrantsTable.getItems().size() * 2) - 1;
        }
    }

    public void saveSpecial(ActionEvent actionEvent) {
        if (assertThatPlayersListIsOfGoodSize()) return;

        checkIfRoundRobinAndSetAdequateAmountOfRounds();

        CreatingTournamentForm creatingTournamentForm = CreatingTournamentForm.builder()
                .name(tournamentName)
                .date(System.currentTimeMillis())
                .tournamentType(selectedTournamentType)
                .amountOfRounds(selectedAmountOfRounds)
                .build();
        List<PlayerDTO> players = entrantsTable.getItems();

        tournamentService.createTournament(creatingTournamentForm, players);

        showSavingConfirmationDialog();
        String fileName = showFileNameDialog();

        if (uWant2SaveDialog.getResult() == ButtonType.YES) {
            tournamentService.saveToFile(null, fileName, this::showFileExistsAlert);
        }
    }

    @FXML
    public void finish(ActionEvent actionEvent) {
        finish();
    }

    @FXML
    public void finishKey(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            finish();
        }
    }

    private void finish() {
        if (assertThatPlayersListIsOfGoodSize()) return;

        if (showAreYouSureToContinueDialog()) {
            checkIfRoundRobinAndSetAdequateAmountOfRounds();

            CreatingTournamentForm creatingTournamentForm = CreatingTournamentForm.builder()
                    .name(tournamentName)
                    .date(System.currentTimeMillis())
                    .tournamentType(selectedTournamentType)
                    .amountOfRounds(selectedAmountOfRounds)
                    .build();
            List<PlayerDTO> players = entrantsTable.getItems();

            tournamentService.createTournament(creatingTournamentForm, players);
            splashScreenController.openTournamentManager();
        }
    }

    private boolean assertThatPlayersListIsOfGoodSize() {
        if (entrantsTable.getItems().size() <= 1) {
            showYouCannotCreateTournamentWithoutPlayersDialog();
            return true;
        }
        if (!moreThanTwoPlayersPossible && entrantsTable.getItems().size() > 2) {
            showYouCannotCreateTournamentMoreThanTwoPlayersDialog();
            return true;
        }
        return false;
    }

    private void setNoOfRoundsTextBoxNumericOnly() {
        amountOfRoundsText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    amountOfRoundsText.setText(newValue.replaceAll("[^\\d]", ""));
                    selectedAmountOfRounds = Integer.parseInt(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    private void setTournamentNameTextBoxProperty() {
        tournamentNameText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                tournamentName = newValue;
            }
        });
    }

    private void setCellValueFactories() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        licenseCol.setCellValueFactory(new PropertyValueFactory<>("licenseID"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        clubCol.setCellValueFactory(new PropertyValueFactory<>("club"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
    }

    public void setRemovingMode(ActionEvent actionEvent) {
        removingEntrantMode = !removingEntrantMode;

        if (removingEntrantMode) {
            removeEntrant.setText("Click player to remove");
        } else {
            removeEntrant.setText("Remove entrant");
        }
    }

    private void initRowClick() {
        entrantsTable.setRowFactory(tv -> {
            TableRow<PlayerDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!removingEntrantMode) {
                    openCreatePlayerPopup();
                }
                if ((!row.isEmpty()) && removingEntrantMode) {
                    PlayerDTO rowData = row.getItem();
                    entrantsTable.getItems().remove(rowData);
                    removingEntrantMode = false;
                    removeEntrant.setText("Remove entrant");
                }
            });
            return row;
        });
    }

    private boolean showYouCannotCreateTournamentWithoutPlayersDialog() {
        Alert alert = new Alert(
                Alert.AlertType.ERROR,
                String.format("You cannot create tournament with %s!",
                        (entrantsTable.getItems().size() == 0)
                                ? "no players"
                                : "one player"
                ),
                ButtonType.OK
        );
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }

    private boolean showYouCannotCreateTournamentMoreThanTwoPlayersDialog() {
        Alert alert = new Alert(
                Alert.AlertType.ERROR,
                String.format("You cannot create 'Heads up' tournament with more than 2 players!\n" +
                        "Remove %s", ((entrantsTable.getItems().size() - 2) == 1)
                        ? "one player"
                        : (entrantsTable.getItems().size() - 2) + " players"
                ),
                ButtonType.OK
        );
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }

    private void showSavingConfirmationDialog() {
        uWant2SaveDialog = new Alert(
                Alert.AlertType.CONFIRMATION,
                String.format("Do you want to save %s?", tournamentName),
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

    private String showFileNameDialog() {
        TextInputDialog textInputDialog = new TextInputDialog("Tournament");
        textInputDialog.setTitle("File name");
        textInputDialog.setHeaderText("Please enter desired file name");
        return textInputDialog.showAndWait().orElse(null);
    }

    private boolean showAreYouSureToContinueDialog() {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to finish creating tournament?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }
}
