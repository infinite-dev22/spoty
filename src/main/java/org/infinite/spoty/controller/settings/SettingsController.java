package org.infinite.spoty.controller.settings;

import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.loadURL;
import static org.infinite.spoty.Utils.createToggle;

public class SettingsController implements Initializable {

    @FXML
    public VBox settingsNavbar;

    @FXML
    public StackPane contentPane;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MFXLoader loader = new MFXLoader();
        loader.addView(MFXLoaderBean.of("SYSTEM", loadURL("fxml/settings/system/System.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "System")).setDefaultRoot(true).get());
        loader.addView(MFXLoaderBean.of("POS", loadURL("fxml/settings/pos/POS.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "POS")).get());
        loader.addView(MFXLoaderBean.of("PAYMENTS", loadURL("fxml/settings/payments/Payments.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "Payments")).get());
        loader.addView(MFXLoaderBean.of("ROLES", loadURL("fxml/settings/roles/Roles.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "Roles")).get());
        loader.addView(MFXLoaderBean.of("BRANCHES", loadURL("fxml/settings/branches/Branches.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "Branches")).get());
        loader.addView(MFXLoaderBean.of("CURRENCY", loadURL("fxml/settings/currency/Currency.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "Currency")).get());
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
        loader.start();
    }
}
