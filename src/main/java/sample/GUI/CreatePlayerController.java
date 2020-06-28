package sample.GUI;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sample.Enums.Genders;
import sample.Models.DTOs.CreatingPlayerForm;
import sample.Models.DTOs.PlayerDTO;
import sample.Service.PlayerService;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class CreatePlayerController implements Initializable {
    @FXML
    public Pane createPlayerPopup;
    @FXML
    public Button createPlayerBtn;
    @FXML
    public TextField nameText;
    @FXML
    public TextField surnameText;
    @FXML
    public TextField licenseIdText;
    @FXML
    public ComboBox<String> genderField;
    @FXML
    public TextField clubText;
    @FXML
    public TextField titleText;
    @FXML
    public Label failLabel;
    @FXML
    public Label successLabel;
    @FXML
    public Button cancel;
    @FXML
    public TableView<PlayerDTO> existingPlayersTable;
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

    @Autowired
    private PlayerService playerService;
    @Autowired
    private SplashScreenController splashScreenController;
    @Autowired
    private TournamentCreatorController tournamentCreatorController;

    @FXML
    public void createPlayer(ActionEvent actionEvent) {
        failLabel.setVisible(false);
        successLabel.setVisible(false);

        CreatingPlayerForm creatingPlayerForm = new CreatingPlayerForm(
                nameText.getText(),
                surnameText.getText(),
                licenseIdText.getText(),
                genderField.getValue(),
                clubText.getText(),
                titleText.getText()
        );

        try {
            playerService.createPlayer(creatingPlayerForm);
            successLabel.setVisible(true);
            refreshTable();
        } catch (Exception e) {
            failLabel.setVisible(true);
            e.printStackTrace();
        }
    }

    @FXML
    public void cancel(ActionEvent actionEvent) {
        failLabel.setVisible(false);
        successLabel.setVisible(false);
        splashScreenController.closePlayerCreator();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellValueFactories();
        initDoubleClick();
        refreshTable();
        initGenderComboBox();
    }

    private void initGenderComboBox() {
        genderField.setItems(Genders.getGenders());
    }

    private void refreshTable() {
        ObservableList<PlayerDTO> allEntrants = playerService.getAllEntrants();
        existingPlayersTable.setItems(allEntrants);
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

    private void initDoubleClick() {
        existingPlayersTable.setRowFactory(tv -> {
            TableRow<PlayerDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    PlayerDTO rowData = row.getItem();
                    tournamentCreatorController.addPlayer(rowData);
                    cancel(null);
                }
            });
            return row;
        });
    }


}