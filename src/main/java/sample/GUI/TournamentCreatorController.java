package sample.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class TournamentCreatorController {

    @FXML
    public MenuItem menuFileQuit;
    public Menu menuFile;
    public MenuBar menuBar;
    public VBox tournamentCreatorVBox;

    @FXML
    public void exit(ActionEvent actionEvent) {
        Stage stage = (Stage) tournamentCreatorVBox.getScene().getWindow();
        stage.close();
    }
}
