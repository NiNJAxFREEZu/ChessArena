package sample.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sample.Models.DTOs.CreatingPlayerForm;
import sample.Service.PlayerService;

@Controller
public class CreatePlayerController {
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
    public TextField genderField;
    @FXML
    public TextField clubText;
    @FXML
    public TextField titleText;
    @FXML
    public Label failLabel;
    @FXML
    public Label successLabel;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private SplashScreenController splashScreenController;

    @FXML
    public void createPlayer(ActionEvent actionEvent) {
        failLabel.setVisible(false);
        successLabel.setVisible(false);

        CreatingPlayerForm creatingPlayerForm = new CreatingPlayerForm(
                nameText.getText(),
                surnameText.getText(),
                licenseIdText.getText(),
                genderField.getText(),
                clubText.getText(),
                titleText.getText()
        );

        try {
            playerService.createPlayer(creatingPlayerForm);
            successLabel.setVisible(true);
        } catch (Exception e) {
            failLabel.setVisible(true);
            e.printStackTrace();
        }
    }

    @FXML
    public void cancel(ActionEvent actionEvent) {
        splashScreenController.closePlayerCreator();
    }
}
