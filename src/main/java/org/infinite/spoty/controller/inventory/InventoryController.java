package org.infinite.spoty.controller.inventory;

import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.infinite.spoty.controller.inventory.adjustment.AdjustmentController;
import org.infinite.spoty.controller.inventory.brand.BrandController;
import org.infinite.spoty.controller.inventory.category.CategoryController;
import org.infinite.spoty.controller.inventory.quotation.QuotationController;
import org.infinite.spoty.controller.inventory.unit_of_measure.UnitOfMeasureController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.loadURL;
import static org.infinite.spoty.Utils.createToggle;

public class InventoryController implements Initializable {
    private final Stage stage;

    @FXML
    public VBox inventoryNavbar;

    @FXML
    public StackPane contentPane;

    public InventoryController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MFXLoader loader = new MFXLoader();
        loader.addView(MFXLoaderBean.of("CATEGORY", loadURL("fxml/inventory/category/Category.fxml")).setBeanToNodeMapper(() -> createToggle("fas-toggle-on", "Category")).setControllerFactory(c1 -> new CategoryController(stage)).get());
        loader.addView(MFXLoaderBean.of("BRAND", loadURL("fxml/inventory/brand/Brand.fxml")).setBeanToNodeMapper(() -> createToggle("fas-square-caret-down", "Brand")).setControllerFactory(c2 -> new BrandController(stage)).get());
        loader.addView(MFXLoaderBean.of("UNIT", loadURL("fxml/inventory/unit_of_measure/UnitOfMeasure.fxml")).setBeanToNodeMapper(() -> createToggle("fas-square-caret-down", "Unit")).setControllerFactory(c3 -> new UnitOfMeasureController(stage)).get());
        loader.addView(MFXLoaderBean.of("PRODUCTS", loadURL("fxml/inventory/products/Products.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "Products")).setDefaultRoot(true).get());
        loader.addView(MFXLoaderBean.of("ADJUSTMENT", loadURL("fxml/inventory/adjustment/Adjustment.fxml")).setBeanToNodeMapper(() -> createToggle("fas-square-caret-down", "Adjustment")).setControllerFactory(c -> new AdjustmentController(stage)).get());
        loader.addView(MFXLoaderBean.of("QUOTATION", loadURL("fxml/inventory/quotation/Quotation.fxml")).setBeanToNodeMapper(() -> createToggle("fas-square-caret-down", "Quotation")).setControllerFactory(c -> new QuotationController(stage)).get());
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
            inventoryNavbar.getChildren().setAll(nodes);
        });
        Platform.runLater(loader::start);
    }
}
