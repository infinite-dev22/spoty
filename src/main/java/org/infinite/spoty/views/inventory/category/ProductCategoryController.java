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

package org.infinite.spoty.views.inventory.category;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.StringFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.database.dao.ProductCategoryDao;
import org.infinite.spoty.database.models.ProductCategory;
import org.infinite.spoty.forms.ProductCategoryFormController;
import org.infinite.spoty.viewModels.ProductCategoryViewModel;

@SuppressWarnings("unchecked")
public class ProductCategoryController implements Initializable {
  private static ProductCategoryController instance;
  @FXML public MFXTableView<ProductCategory> categoryTable;
  @FXML public MFXTextField categorySearchBar;
  @FXML public HBox categoryActionsPane;
  @FXML public MFXButton categoryImportBtn;
  private Dialog<ButtonType> dialog;

  private ProductCategoryController(Stage stage) {
    Platform.runLater(
        () -> {
          try {
            productProductCategoryDialogPane(stage);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        });
  }

  public static ProductCategoryController getInstance(Stage stage) {
    if (instance == null) instance = new ProductCategoryController(stage);
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Platform.runLater(this::setupTable);
  }

  private void setupTable() {
    MFXTableColumn<ProductCategory> categoryCode =
        new MFXTableColumn<>("Code", false, Comparator.comparing(ProductCategory::getCode));
    MFXTableColumn<ProductCategory> categoryName =
        new MFXTableColumn<>("Name", false, Comparator.comparing(ProductCategory::getName));

    categoryCode.setRowCellFactory(category -> new MFXTableRowCell<>(ProductCategory::getCode));
    categoryName.setRowCellFactory(category -> new MFXTableRowCell<>(ProductCategory::getName));

    categoryCode.prefWidthProperty().bind(categoryTable.widthProperty().multiply(.5));
    categoryName.prefWidthProperty().bind(categoryTable.widthProperty().multiply(.5));

    categoryTable.getTableColumns().addAll(categoryCode, categoryName);
    categoryTable
        .getFilters()
        .addAll(
            new StringFilter<>("Code", ProductCategory::getCode),
            new StringFilter<>("Name", ProductCategory::getName));
    getProductCategoryTable();
    categoryTable.setItems(ProductCategoryViewModel.getItems());
  }

  private void getProductCategoryTable() {
    categoryTable.setPrefSize(1000, 1000);
    categoryTable.features().enableBounceEffect();
    categoryTable.features().enableSmoothScrolling(0.5);

    categoryTable.setTableRowFactory(
        t -> {
          MFXTableRow<ProductCategory> row = new MFXTableRow<>(categoryTable, t);
          EventHandler<ContextMenuEvent> eventHandler =
              event -> {
                showContextMenu((MFXTableRow<ProductCategory>) event.getSource())
                    .show(
                        categoryTable.getScene().getWindow(),
                        event.getScreenX(),
                        event.getScreenY());
                event.consume();
              };
          row.setOnContextMenuRequested(eventHandler);
          return row;
        });
  }

  private MFXContextMenu showContextMenu(MFXTableRow<ProductCategory> obj) {
    MFXContextMenu contextMenu = new MFXContextMenu(categoryTable);
    MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
    MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

    // Actions
    // Delete
    delete.setOnAction(
        e -> {
          ProductCategoryDao.deleteProductCategory(obj.getData().getId());
          ProductCategoryViewModel.getItems();
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          ProductCategoryViewModel.getItem(obj.getData().getId());
          dialog.showAndWait();
          e.consume();
        });

    contextMenu.addItems(edit, delete);

    return contextMenu;
  }

  private void productProductCategoryDialogPane(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = fxmlLoader("forms/ProductCategoryForm.fxml");
    fxmlLoader.setControllerFactory(c -> ProductCategoryFormController.getInstance(stage));

    dialog = new Dialog<>();
    dialog.setDialogPane(fxmlLoader.load());
    dialog.initOwner(stage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initStyle(StageStyle.UNDECORATED);
    // To update dialog title dynamically, create a bind in the viewModel with the formTile.
    // ProductCategoryFormController.formTitle.setText(Labels.CREATE);
  }

  public void categoryCreateBtnClicked() {
    dialog.showAndWait();
  }
}
