package org.infinite.spoty.controller.returns;

import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import javafx.application.Platform;
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

public class ReturnsController implements Initializable {

    @FXML
    public VBox returnsNavbar;

    @FXML
    public StackPane contentPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MFXLoader loader = new MFXLoader();
        loader.addView(MFXLoaderBean.of("SALES", loadURL("fxml/returns/sales/Sales.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "Sales")).setDefaultRoot(true).get());
        loader.addView(MFXLoaderBean.of("PURCHASES", loadURL("fxml/returns/purchases/Purchases.fxml")).setBeanToNodeMapper(() -> createToggle("fas-square-caret-down", "Purchases")).get());
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
            returnsNavbar.getChildren().setAll(nodes);
        });
        Platform.runLater(loader::start);
    }
}
