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

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.LongFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.infinite.spoty.database.models.Product;
import org.infinite.spoty.forms.ProductFormController;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.viewModels.ProductViewModel;
import org.infinite.spoty.views.BaseController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotyResourceLoader.fxmlLoader;

@SuppressWarnings("unchecked")
public class ProductController implements Initializable {
    private static ProductController instance;
    @FXML
    public MFXTableView<Product> productMasterTable;
    @FXML
    public BorderPane productsContentPane;
    @FXML
    public MFXTextField productsSearchBar;
    @FXML
    public MFXButton productImportBtn;
    private MFXStageDialog dialog;

    public ProductController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        productFormDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static ProductController getInstance(Stage stage) {
        if (instance == null) instance = new ProductController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Product> productName =
                new MFXTableColumn<>("Name", false, Comparator.comparing(Product::getName));
        MFXTableColumn<Product> productCategory =
                new MFXTableColumn<>("Category", false, Comparator.comparing(Product::getCategoryName));
        MFXTableColumn<Product> productBrand =
                new MFXTableColumn<>("Brand", false, Comparator.comparing(Product::getBrandName));
        MFXTableColumn<Product> productQuantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(Product::getQuantity));
        MFXTableColumn<Product> productPrice =
                new MFXTableColumn<>("Price", false, Comparator.comparing(Product::getPrice));
        MFXTableColumn<Product> productType =
                new MFXTableColumn<>("Type", false, Comparator.comparing(Product::getProductType));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getName));
        productCategory.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getCategoryName));
        productBrand.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getBrandName));
        productQuantity.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getQuantity));
        productPrice.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getPrice));
        productType.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductType));

        productName.prefWidthProperty().bind(productMasterTable.widthProperty().multiply(.25));
        productCategory.prefWidthProperty().bind(productMasterTable.widthProperty().multiply(.25));
        productBrand.prefWidthProperty().bind(productMasterTable.widthProperty().multiply(.25));
        productQuantity.prefWidthProperty().bind(productMasterTable.widthProperty().multiply(.25));
        productPrice.prefWidthProperty().bind(productMasterTable.widthProperty().multiply(.25));
        productType.prefWidthProperty().bind(productMasterTable.widthProperty().multiply(.25));

        productMasterTable
                .getTableColumns()
                .addAll(productName, productCategory, productBrand, productQuantity, productPrice, productType);
        productMasterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", Product::getName),
                        new StringFilter<>("Category", Product::getCategoryName),
                        new StringFilter<>("Brand", Product::getBrandName),
                        new LongFilter<>("Quantity", Product::getQuantity),
                        new DoubleFilter<>("Price", Product::getPrice),
                        new StringFilter<>("Product Type", Product::getProductType));
        getTable();

        if (ProductViewModel.getProducts().isEmpty()) {
            ProductViewModel.getProducts()
                    .addListener(
                            (ListChangeListener<Product>)
                                    c -> productMasterTable.setItems(ProductViewModel.getProducts()));
        } else {
            productMasterTable.itemsProperty().bindBidirectional(ProductViewModel.productsProperty());
        }
    }

    private void getTable() {
        productMasterTable.setPrefSize(1000, 1000);
        productMasterTable.features().enableBounceEffect();
        productMasterTable.features().enableSmoothScrolling(0.5);

        productMasterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Product> row = new MFXTableRow<>(productMasterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<Product>) event.getSource())
                                        .show(
                                                productMasterTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<Product> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(productMasterTable);
        MFXContextMenuItem view = new MFXContextMenuItem("View");
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    ProductViewModel.deleteProduct(obj.getData().getId());
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                            });

                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    ProductViewModel.getProduct(obj.getData().getId());
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                            });
                    productCreateBtnClicked();
                    e.consume();
                });

        contextMenu.addItems(view, edit, delete);

        return contextMenu;
    }

    private void productFormDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("forms/ProductForm.fxml");
        fxmlLoader.setControllerFactory(c -> ProductFormController.getInstance());

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(BaseController.getInstance(stage).contentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void productCreateBtnClicked() {
        dialog.showAndWait();
    }
}
