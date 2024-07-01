package inc.nomard.spoty.core.views.splash;

import inc.nomard.spoty.core.values.*;
import inc.nomard.spoty.core.values.strings.*;
import io.github.palexdev.materialfx.theming.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import lombok.extern.java.*;

@Log
public class LaunchPreloader extends Preloader {
    private Stage preloadStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        UserAgentBuilder.builder()
                .themes(JavaFXThemes.MODENA)
                .themes(MaterialFXStylesheets.forAssemble(true))
                .setDeploy(true)
                .setResolveAssets(true)
                .build()
                .setGlobal();

        this.preloadStage = primaryStage;
        Scene scene = new Scene(new SplashScreen());
        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(scene);
        scene.setFill(null);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setTitle(Labels.APP_NAME);
        // PreloadData.
        PreloadedData.preloadImages();
        primaryStage.getIcons().addAll(
                PreloadedData.icon16,
                PreloadedData.icon32,
                PreloadedData.icon64,
                PreloadedData.icon128,
                PreloadedData.icon256,
                PreloadedData.icon512
        );
        primaryStage.setAlwaysOnTop(false);
        primaryStage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
            preloadStage.hide();
            preloadStage.close();
        }
    }
}
