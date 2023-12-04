/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package org.infinite.spoty.views.splash;

import static org.infinite.spoty.SpotyResourceLoader.fxmlLoader;

//import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
//import io.github.palexdev.materialfx.css.themes.Themes;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    Scene scene = new Scene(fxmlLoader("fxml/splash/SplashScreen.fxml").load());
//    MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
    io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(scene);
    scene.setFill(null);
    primaryStage.initStyle(StageStyle.TRANSPARENT);
    primaryStage.setScene(scene);
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
