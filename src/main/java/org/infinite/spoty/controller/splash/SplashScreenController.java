package org.infinite.spoty.controller.splash;

import fr.brouillard.oss.cssfx.CSSFX;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.controller.BaseController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class SplashScreenController implements Initializable {
    @FXML
    public Label applicationName;
    @FXML
    public AnchorPane splashScreenPane;
    @FXML
    public Label companyName;
    @FXML
    public MFXProgressBar hidden;

    public static void checkFunctions() {
        Platform.runLater(() -> {
            try {
                Thread.sleep(5000);
                CSSFX.start();
                FXMLLoader loader = fxmlLoader("fxml/Base.fxml");
                Stage stage = new Stage();
                loader.setControllerFactory(c -> new BaseController(stage));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
                io.github.palexdev.mfxcomponents.theming.enums.MFXThemeManager.PURPLE_LIGHT.addOn(scene);
                scene.setFill(Color.TRANSPARENT);
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setMaximized(true);
                stage.setTitle("Zenmat ERP");
                stage.show();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        companyName.setText("Nameless Systems Corp");
        applicationName.setText("Zenmat ERP");
    }
}
