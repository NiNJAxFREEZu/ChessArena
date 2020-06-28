package sample.GUI;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sample.Models.DTOs.PlayerDTO;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Controller
public class ScoreboardController implements Initializable {

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
    public TableColumn<PlayerDTO, Float> scoreCol;
    @FXML
    public TableView<PlayerDTO> playersTable;
    @FXML
    public VBox scoreboard;
    @FXML
    public Button closeScoreBtn;

    @Autowired
    private SplashScreenController splashScreenController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellValueFactories();
    }

    public void displayResults(List<PlayerDTO> players) {
        playersTable.setItems(FXCollections.observableList(
                players.stream()
                        .sorted(Comparator.comparing(PlayerDTO::getScore).reversed())
                        .collect(Collectors.toList())
        ));
        playersTable.sort();
    }

    private void setCellValueFactories() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        licenseCol.setCellValueFactory(new PropertyValueFactory<>("licenseID"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        clubCol.setCellValueFactory(new PropertyValueFactory<>("club"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
    }

    public void closeScoreboard(ActionEvent actionEvent) {
        splashScreenController.closeScoreBoard();
    }
}
