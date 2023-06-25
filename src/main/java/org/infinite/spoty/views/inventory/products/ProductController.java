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
import io.github.palexdev.materialfx.filter.StringFilter;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.database.dao.ProductMasterDao;
import org.infinite.spoty.database.models.ProductMaster;
import org.infinite.spoty.viewModels.ProductMasterViewModel;
import org.infinite.spoty.views.BaseController;

@SuppressWarnings("unchecked")
public class ProductController implements Initializable {
  private static ProductController instance;
  @FXML public MFXTableView<ProductMaster> productMasterTable;
  @FXML public BorderPane productsContentPane;
  @FXML public MFXTextField productsSearchBar;
  @FXML public MFXButton productImportBtn;

  public static ProductController getInstance() {
    if (instance == null) instance = new ProductController();
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Platform.runLater(this::setupTable);
  }

  private void setupTable() {
    MFXTableColumn<ProductMaster> productName =
        new MFXTableColumn<>("Name", false, Comparator.comparing(ProductMaster::getName));
    MFXTableColumn<ProductMaster> productCode =
        new MFXTableColumn<>("Code", false, Comparator.comparing(ProductMaster::getCode));
    MFXTableColumn<ProductMaster> productCategory =
        new MFXTableColumn<>(
            "Category", false, Comparator.comparing(ProductMaster::getCategoryName));
    MFXTableColumn<ProductMaster> productBrand =
        new MFXTableColumn<>("Brand", false, Comparator.comparing(ProductMaster::getBrandName));

    productName.setRowCellFactory(product -> new MFXTableRowCell<>(ProductMaster::getName));
    productCode.setRowCellFactory(product -> new MFXTableRowCell<>(ProductMaster::getCode));
    productCategory.setRowCellFactory(
        product -> new MFXTableRowCell<>(ProductMaster::getCategoryName));
    productBrand.setRowCellFactory(product -> new MFXTableRowCell<>(ProductMaster::getBrandName));

    productName.prefWidthProperty().bind(productMasterTable.widthProperty().multiply(.25));
    productCode.prefWidthProperty().bind(productMasterTable.widthProperty().multiply(.25));
    productCategory.prefWidthProperty().bind(productMasterTable.widthProperty().multiply(.25));
    productBrand.prefWidthProperty().bind(productMasterTable.widthProperty().multiply(.25));

    productMasterTable
        .getTableColumns()
        .addAll(productName, productCode, productCategory, productBrand);
    productMasterTable
        .getFilters()
        .addAll(
            new StringFilter<>("Name", ProductMaster::getName),
            new StringFilter<>("Code", ProductMaster::getCode),
            new StringFilter<>("Category", ProductMaster::getCategoryName),
            new StringFilter<>("Brand", ProductMaster::getBrandName));
    getTable();
    productMasterTable.setItems(ProductMasterViewModel.getProductMasters());
  }

  private void getTable() {
    productMasterTable.setPrefSize(1000, 1000);
    productMasterTable.features().enableBounceEffect();
    productMasterTable.features().enableSmoothScrolling(0.5);

    productMasterTable.setTableRowFactory(
        t -> {
          MFXTableRow<ProductMaster> row = new MFXTableRow<>(productMasterTable, t);
          EventHandler<ContextMenuEvent> eventHandler =
              event -> {
                showContextMenu((MFXTableRow<ProductMaster>) event.getSource())
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

  private MFXContextMenu showContextMenu(MFXTableRow<ProductMaster> obj) {
    MFXContextMenu contextMenu = new MFXContextMenu(productMasterTable);
    MFXContextMenuItem view = new MFXContextMenuItem("View");
    MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
    MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

    // Actions
    // Delete
    delete.setOnAction(
        e -> {
          ProductMasterDao.deleteProductMaster(obj.getData().getId());
          ProductMasterViewModel.getProductMasters();
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          ProductMasterViewModel.getItem(obj.getData().getId());
          productCreateBtnClicked();
          e.consume();
        });

    contextMenu.addItems(view, edit, delete);

    return contextMenu;
  }

  public void productCreateBtnClicked() {
    BaseController.navigation.navigate(Pages.getProductMasterFormPane());
  }
}
