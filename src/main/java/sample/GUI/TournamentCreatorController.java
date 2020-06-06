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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
    public void loadComboBox() {
        TournamentType[] types = TournamentType.values();
        for (Enum<TournamentType> type : types) {
            tournamentTypeComboBox.getItems().addAll(type.toString());
        }
        tournamentTypeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent event) {
                Thread.sleep(100);
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
    }

    @FXML
    public void openCreatePlayerPopup(MouseEvent actionEvent) {
        splashScreenController.openPlayerCreator();
    }

    @FXML
    public void closeTournamentCreator(ActionEvent actionEvent) {
        splashScreenController.closeTournamentCreator();
    }

    @FXML
    public void nextTab(ActionEvent actionEvent) {
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

    public void saveSpecial(ActionEvent actionEvent) {
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

    public void finish(ActionEvent actionEvent) {

    }

    public void finish(KeyEvent keyEvent) {
        //for now it only assigns stuff to variables (same as "save" but without writing to file)
        //TODO -> go to tournament watching, scores etc
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
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        clubCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("club"));
    }

    private String showFileNameDialog() {
        TextInputDialog textInputDialog = new TextInputDialog("Tournament");
        textInputDialog.setTitle("File name");
        textInputDialog.setHeaderText("Please enter desired file name");
        return textInputDialog.showAndWait().orElse(null);
    }
}
