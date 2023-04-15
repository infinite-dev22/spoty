package org.infinite.spoty.controller.inventory;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.loadURL;
import static org.infinite.spoty.Utils.createToggle;

public class InventoryController implements Initializable {

    @FXML
    public VBox inventoryNavbar;

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
        loader.addView(MFXLoaderBean.of("CATEGORY", loadURL("fxml/inventory/category/Category.fxml")).setBeanToNodeMapper(() -> createToggle("fas-toggle-on", "Category")).get());
        loader.addView(MFXLoaderBean.of("BRAND", loadURL("fxml/inventory/brand/Brand.fxml")).setBeanToNodeMapper(() -> createToggle("fas-square-caret-down", "Brand")).get());
        loader.addView(MFXLoaderBean.of("UNIT", loadURL("fxml/inventory/unit_of_measure/UnitOfMeasure.fxml")).setBeanToNodeMapper(() -> createToggle("fas-square-caret-down", "Unit")).get());
        loader.addView(MFXLoaderBean.of("PRODUCTS", loadURL("fxml/inventory/products/Products.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "Products")).setDefaultRoot(true).get());
        loader.addView(MFXLoaderBean.of("REQUISITION", loadURL("fxml/inventory/requisition/Requisition.fxml")).setBeanToNodeMapper(() -> createToggle("fas-square-caret-down", "Requisition")).get());
        loader.addView(MFXLoaderBean.of("ORDERS", loadURL("fxml/inventory/orders/Orders.fxml")).setBeanToNodeMapper(() -> createToggle("fas-square-caret-down", "Orders")).get());
        loader.addView(MFXLoaderBean.of("STOCK IN", loadURL("fxml/inventory/stock_in/StockIn.fxml")).setBeanToNodeMapper(() -> createToggle("fas-square-caret-down", "Stock In")).get());
        loader.addView(MFXLoaderBean.of("TRANSFER", loadURL("fxml/inventory/transfer/Transfer.fxml")).setBeanToNodeMapper(() -> createToggle("fas-square-caret-down", "Transfer")).get());
        loader.addView(MFXLoaderBean.of("ADJUSTMENT", loadURL("fxml/inventory/adjustment/Adjustment.fxml")).setBeanToNodeMapper(() -> createToggle("fas-square-caret-down", "Adjustment")).get());
        loader.addView(MFXLoaderBean.of("QUOTATION", loadURL("fxml/inventory/quotation/Quotation.fxml")).setBeanToNodeMapper(() -> createToggle("fas-square-caret-down", "Quotation")).get());
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
        loader.start();
    }
}
