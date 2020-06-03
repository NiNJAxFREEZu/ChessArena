package sample.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import sample.Enums.TournamentType;

@Component
public class TournamentCreatorController {

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
}
