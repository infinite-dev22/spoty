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

package org.infinite.spoty.views.inventory;

import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.infinite.spoty.views.inventory.adjustment.AdjustmentController;
import org.infinite.spoty.views.inventory.brand.BrandController;
import org.infinite.spoty.views.inventory.category.ProductCategoryController;
import org.infinite.spoty.views.inventory.products.ProductController;
import org.infinite.spoty.views.inventory.quotation.QuotationController;
import org.infinite.spoty.views.inventory.unit_of_measure.UnitOfMeasureController;

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
        loader.addView(MFXLoaderBean.of("CATEGORY", loadURL("fxml/inventory/category/ProductCategory.fxml")).setBeanToNodeMapper(() -> createToggle("fas-cubes-stacked", "Category")).setControllerFactory(c -> new ProductCategoryController(stage)).get());
        loader.addView(MFXLoaderBean.of("BRAND", loadURL("fxml/inventory/brand/Brand.fxml")).setBeanToNodeMapper(() -> createToggle("fas-tags", "Brand")).setControllerFactory(c -> new BrandController(stage)).get());
        loader.addView(MFXLoaderBean.of("UNIT", loadURL("fxml/inventory/unit_of_measure/UnitOfMeasure.fxml")).setBeanToNodeMapper(() -> createToggle("fas-weight-hanging", "Unit")).setControllerFactory(c -> new UnitOfMeasureController(stage)).get());
        loader.addView(MFXLoaderBean.of("PRODUCTS", loadURL("fxml/inventory/products/Products.fxml")).setBeanToNodeMapper(() -> createToggle("fas-box", "Products")).setControllerFactory(c -> new ProductController(stage)).setDefaultRoot(true).get());
        loader.addView(MFXLoaderBean.of("ADJUSTMENT", loadURL("fxml/inventory/adjustment/Adjustment.fxml")).setBeanToNodeMapper(() -> createToggle("fas-sliders", "Adjustment")).setControllerFactory(c -> new AdjustmentController(stage)).get());
        loader.addView(MFXLoaderBean.of("QUOTATION", loadURL("fxml/inventory/quotation/Quotation.fxml")).setBeanToNodeMapper(() -> createToggle("fas-money-bill-1", "Quotation")).setControllerFactory(c -> new QuotationController(stage)).get());
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
