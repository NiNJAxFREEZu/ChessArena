package sample.GUI;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class SplashScreenController {

    @FXML
    private javafx.scene.control.Button closeButton;

    @FXML
    public void closeButtonAction(){
        Stage stage = (Stage)closeButton.getScene().getWindow();
        stage.close();
    }

}
