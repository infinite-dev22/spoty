package org.infinite.spotmarkpos;

import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import fr.brouillard.oss.cssfx.CSSFX;
import org.infinite.spotmarkpos.controller.BaseController;

import java.io.IOException;

public class POSMain extends Application {
    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        CSSFX.start();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../fxml/base.fxml"));
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

    public static void main(String[] args) {
        launch();
    }
}
