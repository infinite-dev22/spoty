package org.infinite.spoty.views.splash;

import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class LaunchPreloader extends Preloader {
    private Stage preloadStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.preloadStage = primaryStage;
        Scene scene = new Scene(fxmlLoader("fxml/splash/SplashScreen.fxml").load());
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
        io.github.palexdev.mfxcomponents.theming.enums.MFXThemeManager.PURPLE_LIGHT.addOn(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
            preloadStage.hide();
        }
    }
}
