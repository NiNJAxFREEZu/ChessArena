package sample.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import sample.Enums.TournamentType;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class TournamentCreatorController implements Initializable {

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
        Stage stage = (Stage) tournamentCreatorVBox.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadComboBox();
    }
}
