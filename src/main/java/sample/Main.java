package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ResourceUtils;
import sample.GUI.SplashScreenController;

import java.net.URL;

@SpringBootApplication(scanBasePackages = "sample")
public class Main extends Application {
    private ConfigurableApplicationContext springContext;
    private FXMLLoader fxmlLoader;
    private SplashScreenController component;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(Main.class);
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(springContext::getBean);
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL mainFxmlFile = ResourceUtils.getURL("src/main/resources/FXML/splash.fxml");

        fxmlLoader.setLocation(mainFxmlFile);
        Parent root = fxmlLoader.load();
        component = fxmlLoader.getController();
        stage.setTitle("Chess Arena");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root, 1280, 720));
        stage.setMinWidth(1280);
        stage.setMinHeight(720);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        springContext.close();

    }
}
