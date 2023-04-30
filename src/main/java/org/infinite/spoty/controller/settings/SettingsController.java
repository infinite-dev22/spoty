package org.infinite.spoty.controller.settings;

import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.infinite.spoty.controller.settings.branches.BranchesController;
import org.infinite.spoty.controller.settings.currency.CurrencyController;

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
    public SettingsController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MFXLoader loader = new MFXLoader();
        loader.addView(MFXLoaderBean.of("SYSTEM", loadURL("fxml/settings/system/System.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "System")).setDefaultRoot(true).get());
        loader.addView(MFXLoaderBean.of("POS", loadURL("fxml/settings/pos/POS.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "POS")).get());
        loader.addView(MFXLoaderBean.of("ROLES", loadURL("fxml/settings/roles/Roles.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "Roles")).get());
        loader.addView(MFXLoaderBean.of("BRANCHES", loadURL("fxml/settings/branches/Branches.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "Branches")).setControllerFactory(c -> new BranchesController(stage)).get());
        loader.addView(MFXLoaderBean.of("CURRENCY", loadURL("fxml/settings/currency/Currency.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "Currency")).setControllerFactory(c -> new CurrencyController(stage)).get());
        loader.addView(MFXLoaderBean.of("EXPORT", loadURL("fxml/settings/export/Export.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "Export")).get());
        loader.setOnLoadedAction(beans -> {
            List<ToggleButton> nodes = beans.stream()
                    .map(bean -> {
                        ToggleButton toggle = (ToggleButton) bean.getBeanToNodeMapper().get();
                        toggle.setOnAction(event -> contentPane.getChildren().setAll(bean.getRoot()));
                        if (bean.isDefaultView()) {
                            contentPane.getChildren().setAll(bean.getRoot());
                            toggle.setSelected(true);
                        }
                        return toggle;
                    })
                    .toList();
            settingsNavbar.getChildren().setAll(nodes);
        });
        Platform.runLater(loader::start);
    }
}
