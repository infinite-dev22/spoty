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

package org.infinite.spoty.views.inventory.products;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.database.models.ProductMaster;
import org.infinite.spoty.viewModels.ProductMasterViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class ProductsController implements Initializable {
    @FXML
    public MFXTableView<ProductMaster> productsTable;
    @FXML
    public BorderPane productsContentPane;
    @FXML
    public MFXTextField productsSearchBar;
    @FXML
    public MFXButton productImportBtn;
    private Dialog<ButtonType> dialog;

    public ProductsController(Stage stage) {
        Platform.runLater(() -> {
            try {
                productFormDialogPane(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<ProductMaster> productName = new MFXTableColumn<>("Name", false, Comparator.comparing(ProductMaster::getName));
        MFXTableColumn<ProductMaster> productCode = new MFXTableColumn<>("Code", false, Comparator.comparing(ProductMaster::getCode));
        MFXTableColumn<ProductMaster> productCategory = new MFXTableColumn<>("Category", false, Comparator.comparing(ProductMaster::getCategoryName));
        MFXTableColumn<ProductMaster> productBrand = new MFXTableColumn<>("Brand", false, Comparator.comparing(ProductMaster::getBrandName));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(ProductMaster::getName));
        productCode.setRowCellFactory(product -> new MFXTableRowCell<>(ProductMaster::getCode));
        productCategory.setRowCellFactory(product -> new MFXTableRowCell<>(ProductMaster::getCategoryName));
        productBrand.setRowCellFactory(product -> new MFXTableRowCell<>(ProductMaster::getBrandName));

        productName.prefWidthProperty().bind(productsTable.widthProperty().multiply(.25));
        productCode.prefWidthProperty().bind(productsTable.widthProperty().multiply(.25));
        productCategory.prefWidthProperty().bind(productsTable.widthProperty().multiply(.25));
        productBrand.prefWidthProperty().bind(productsTable.widthProperty().multiply(.25));

        productsTable.getTableColumns().addAll(productName, productCode, productCategory, productBrand);
        productsTable.getFilters().addAll(
                new StringFilter<>("Name", ProductMaster::getName),
                new StringFilter<>("Code", ProductMaster::getCode),
                new StringFilter<>("Category", ProductMaster::getCategoryName),
                new StringFilter<>("Brand", ProductMaster::getBrandName)
        );
        getTable();
        productsTable.setItems(ProductMasterViewModel.getProductMasters());
    }

    private void getTable() {
        productsTable.setPrefSize(1000, 1000);
        productsTable.features().enableBounceEffect();
        productsTable.features().enableSmoothScrolling(0.5);
    }

    private void productFormDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/ProductForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void productCreateBtnClicked() {
        dialog.showAndWait();
    }
}
