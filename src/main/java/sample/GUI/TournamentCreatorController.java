package sample.GUI;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sample.Enums.TournamentType;

import java.net.URL;
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

    @Autowired
    private SplashScreenController splashScreenController;

    @FXML
    public MenuItem menuFileQuit;
    public Menu menuFile;
    public MenuBar menuBar;
    public VBox tournamentCreatorVBox;
    public ComboBox tournamentTypeComboBox;


    @FXML
    public void loadComboBox(){

        TournamentType[] types = TournamentType.values();
        for (Enum<TournamentType> type: types) {
            tournamentTypeComboBox.getItems().addAll(type.toString());
        }
    }

    @FXML
    public void exit(ActionEvent actionEvent) {
        splashScreenController.closeTournamentCreator();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadComboBox();
    }

    @FXML
    @SneakyThrows
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
}
