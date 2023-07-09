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

package org.infinite.spoty.views.inventory.brand;

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
import javafx.collections.WeakListChangeListener;
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
import org.infinite.spoty.database.models.Brand;
import org.infinite.spoty.forms.BrandFormController;
import org.infinite.spoty.viewModels.BrandViewModel;

@SuppressWarnings("unchecked")
public class BrandController implements Initializable {
  private static BrandController instance;
  @FXML public MFXTableView<Brand> brandTable;
  @FXML public MFXTextField brandSearchBar;
  @FXML public HBox brandActionsPane;
  @FXML public MFXButton brandImportBtn;
  private Dialog<ButtonType> dialog;

  private BrandController(Stage stage) {
    Platform.runLater(
        () -> {
          try {
            brandFormDialogPane(stage);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        });
  }

  public static BrandController getInstance(Stage stage) {
    if (instance == null) instance = new BrandController(stage);
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Platform.runLater(this::setupTable);
  }

  private void setupTable() {
    MFXTableColumn<Brand> brandName =
        new MFXTableColumn<>("Name", false, Comparator.comparing(Brand::getName));
    MFXTableColumn<Brand> brandDescription =
        new MFXTableColumn<>("Description", false, Comparator.comparing(Brand::getDescription));

    brandName.setRowCellFactory(brand -> new MFXTableRowCell<>(Brand::getName));
    brandDescription.setRowCellFactory(brand -> new MFXTableRowCell<>(Brand::getDescription));

    brandName.prefWidthProperty().bind(brandTable.widthProperty().multiply(.5));
    brandDescription.prefWidthProperty().bind(brandTable.widthProperty().multiply(.5));

    brandTable.getTableColumns().addAll(brandName, brandDescription);
    brandTable
        .getFilters()
        .addAll(
            new StringFilter<>("Name", Brand::getName),
            new StringFilter<>("Description", Brand::getDescription));
    getBrandTable();
    brandTable.setItems(BrandViewModel.getBrandsList());
    BrandViewModel.brandsList.addListener(
        new WeakListChangeListener<>(c -> brandTable.setItems(BrandViewModel.getBrandsList())));
  }

  private void getBrandTable() {
    brandTable.setPrefSize(1000, 1000);
    brandTable.features().enableBounceEffect();
    brandTable.features().enableSmoothScrolling(0.5);

    brandTable.setTableRowFactory(
        t -> {
          MFXTableRow<Brand> row = new MFXTableRow<>(brandTable, t);
          EventHandler<ContextMenuEvent> eventHandler =
              event -> {
                showContextMenu((MFXTableRow<Brand>) event.getSource())
                    .show(
                        brandTable.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                event.consume();
              };
          row.setOnContextMenuRequested(eventHandler);
          return row;
        });
  }

  private MFXContextMenu showContextMenu(MFXTableRow<Brand> obj) {
    MFXContextMenu contextMenu = new MFXContextMenu(brandTable);
    MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
    MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

    // Actions
    // Delete
    delete.setOnAction(
        e -> {
          BrandViewModel.deleteItem(obj.getData().getId());
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          BrandViewModel.getItem(obj.getData().getId());
          dialog.showAndWait();
          e.consume();
        });

    contextMenu.addItems(edit, delete);

    return contextMenu;
  }

  private void brandFormDialogPane(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = fxmlLoader("forms/BrandForm.fxml");
    fxmlLoader.setControllerFactory(c -> BrandFormController.getInstance(stage));

    dialog = new Dialog<>();
    dialog.setDialogPane(fxmlLoader.load());
    dialog.initOwner(stage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initStyle(StageStyle.UNDECORATED);
  }

  public void brandCreateBtnClicked() {
    dialog.showAndWait();
  }
}
