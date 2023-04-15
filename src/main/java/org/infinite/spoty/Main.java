package org.infinite.spoty;

import fr.brouillard.oss.cssfx.CSSFX;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.controller.BaseController;

import java.io.IOException;

public class Main extends Application {

    public static void runApp () {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        CSSFX.start();
        FXMLLoader loader = new FXMLLoader(SpotResourceLoader.loadURL("fxml/Base.fxml"));
        loader.setControllerFactory(c -> new BaseController(primaryStage));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MaterialFX Demo");
        primaryStage.show();
    }
}
