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

package org.infinite.spoty.views.settings;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.infinite.spoty.views.settings.branches.BranchesController;
import org.infinite.spoty.views.settings.currency.CurrencyController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.loadURL;
import static org.infinite.spoty.Utils.createToggle;

public class SettingsController implements Initializable {
    private final Stage stage;
    @FXML
    public VBox settingsNavbar;
    @FXML
    public StackPane contentPane;
    private static SettingsController instance;

    public static SettingsController getInstance(Stage stage) {
        if (instance == null)
            instance = new SettingsController(stage);
        return instance;
    }

    private SettingsController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MFXLoader loader = new MFXLoader();
        loader.addView(MFXLoaderBean.of("SYSTEM", loadURL("fxml/settings/system/System.fxml")).setBeanToNodeMapper(() -> createToggle("fas-computer", "System")).setDefaultRoot(true).get());
        loader.addView(MFXLoaderBean.of("POS", loadURL("fxml/settings/pos/POS.fxml")).setBeanToNodeMapper(() -> createToggle("fas-bag-shopping", "POS")).get());
        loader.addView(MFXLoaderBean.of("ROLES", loadURL("fxml/settings/roles/Roles.fxml")).setBeanToNodeMapper(() -> createToggle("fas-user-shield", "Roles")).get());
        loader.addView(MFXLoaderBean.of("BRANCHES", loadURL("fxml/settings/branches/Branches.fxml")).setBeanToNodeMapper(() -> createToggle("fas-store", "Branches")).setControllerFactory(c -> BranchesController.getInstance(stage)).get());
        loader.addView(MFXLoaderBean.of("CURRENCY", loadURL("fxml/settings/currency/Currency.fxml")).setBeanToNodeMapper(() -> createToggle("fas-dollar-sign", "Currency")).setControllerFactory(c -> CurrencyController.getInstance(stage)).get());
        loader.addView(MFXLoaderBean.of("EXPORT", loadURL("fxml/settings/export/Export.fxml")).setBeanToNodeMapper(() -> createToggle("fas-arrow-up-from-bracket", "Export")).get());
        loader.setOnLoadedAction(beans -> {
            List<MFXButton> nodes = beans.stream()
                    .map(bean -> {
                        MFXButton toggle = (MFXButton) bean.getBeanToNodeMapper().get();
                        toggle.setOnAction(event -> contentPane.getChildren().setAll(bean.getRoot()));
                        if (bean.isDefaultView()) {
                            contentPane.getChildren().setAll(bean.getRoot());
                        }
                        return toggle;
                    })
                    .toList();
            settingsNavbar.getChildren().setAll(nodes);
        });
        Platform.runLater(loader::start);
    }
}
