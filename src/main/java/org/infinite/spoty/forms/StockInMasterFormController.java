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

package org.infinite.spoty.forms;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.Validators.requiredValidator;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.StockInDetail;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.StockInDetailViewModel;
import org.infinite.spoty.viewModels.StockInMasterViewModel;
import org.infinite.spoty.views.BaseController;

@SuppressWarnings("unchecked")
public class StockInMasterFormController implements Initializable {
  private static StockInMasterFormController instance;
  public MFXTextField stockInMasterID = new MFXTextField();
  @FXML public MFXFilterComboBox<Branch> stockInMasterBranch;
  @FXML public MFXDatePicker stockInMasterDate;
  @FXML public MFXTableView<StockInDetail> stockInDetailTable;
  @FXML public MFXTextField stockInMasterNote;
  @FXML public BorderPane stockInMasterFormContentPane;
  @FXML public Label stockInMasterFormTitle;
  @FXML public MFXButton stockInMasterProductAddBtn;
  @FXML public Label stockInMasterDateValidationLabel;
  @FXML public Label stockInMasterBranchValidationLabel;
  private Dialog<ButtonType> dialog;

  private StockInMasterFormController(Stage stage) {
    Platform.runLater(
        () -> {
          try {
            quotationProductDialogPane(stage);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        });
  }

  public static StockInMasterFormController getInstance(Stage stage) {
    if (instance == null) instance = new StockInMasterFormController(stage);
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Input binding.
    stockInMasterID
        .textProperty()
        .bindBidirectional(StockInMasterViewModel.idProperty(), new NumberStringConverter());
    stockInMasterBranch.valueProperty().bindBidirectional(StockInMasterViewModel.branchProperty());
    stockInMasterBranch.setItems(BranchViewModel.branchesList);
    stockInMasterBranch.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(Branch object) {
            if (object != null) return object.getName();
            else return null;
          }

          @Override
          public Branch fromString(String string) {
            return null;
          }
        });
    stockInMasterDate.textProperty().bindBidirectional(StockInMasterViewModel.dateProperty());
    stockInMasterNote.textProperty().bindBidirectional(StockInMasterViewModel.noteProperty());
    // input validators.
    requiredValidator(
        stockInMasterBranch, "Branch is required.", stockInMasterBranchValidationLabel);
    requiredValidator(stockInMasterDate, "Date is required.", stockInMasterDateValidationLabel);
    stockInMasterAddProductBtnClicked();
    Platform.runLater(this::setupTable);
  }

  private void setupTable() {
    MFXTableColumn<StockInDetail> productName =
        new MFXTableColumn<>(
            "Product", false, Comparator.comparing(StockInDetail::getProductDetailName));
    MFXTableColumn<StockInDetail> productQuantity =
        new MFXTableColumn<>("Quantity", false, Comparator.comparing(StockInDetail::getQuantity));

    productName.setRowCellFactory(
        product -> new MFXTableRowCell<>(StockInDetail::getProductDetailName));
    productQuantity.setRowCellFactory(product -> new MFXTableRowCell<>(StockInDetail::getQuantity));

    productName.prefWidthProperty().bind(stockInDetailTable.widthProperty().multiply(.4));
    productQuantity.prefWidthProperty().bind(stockInDetailTable.widthProperty().multiply(.4));

    stockInDetailTable.getTableColumns().addAll(productName, productQuantity);
    stockInDetailTable
        .getFilters()
        .addAll(
            new StringFilter<>("Name", StockInDetail::getProductDetailName),
            new IntegerFilter<>("Quantity", StockInDetail::getQuantity));
    getStockInDetailTable();
    stockInDetailTable.setItems(StockInDetailViewModel.stockInDetailsTempList);
  }

  private void getStockInDetailTable() {
    stockInDetailTable.setPrefSize(1000, 1000);
    stockInDetailTable.features().enableBounceEffect();
    stockInDetailTable.features().enableSmoothScrolling(0.5);

    stockInDetailTable.setTableRowFactory(
        t -> {
          MFXTableRow<StockInDetail> row = new MFXTableRow<>(stockInDetailTable, t);
          EventHandler<ContextMenuEvent> eventHandler =
              event -> {
                showContextMenu((MFXTableRow<StockInDetail>) event.getSource())
                    .show(
                        stockInDetailTable.getScene().getWindow(),
                        event.getScreenX(),
                        event.getScreenY());
                event.consume();
              };
          row.setOnContextMenuRequested(eventHandler);
          return row;
        });
  }

  private MFXContextMenu showContextMenu(MFXTableRow<StockInDetail> obj) {
    MFXContextMenu contextMenu = new MFXContextMenu(stockInDetailTable);
    MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
    MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

    // Actions
    // Delete
    delete.setOnAction(
        e -> {
          StockInDetailViewModel.removeStockInDetail(
              obj.getData().getId(),
              StockInDetailViewModel.stockInDetailsTempList.indexOf(obj.getData()));
          StockInDetailViewModel.getStockInDetails();
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          StockInDetailViewModel.getItem(
              obj.getData().getId(),
              StockInDetailViewModel.stockInDetailsTempList.indexOf(obj.getData()));
          dialog.showAndWait();
          e.consume();
        });

    contextMenu.addItems(edit, delete);

    return contextMenu;
  }

  private void stockInMasterAddProductBtnClicked() {
    stockInMasterProductAddBtn.setOnAction(e -> dialog.showAndWait());
  }

  private void quotationProductDialogPane(Stage stage) throws IOException {
    DialogPane dialogPane = fxmlLoader("forms/StockInDetailForm.fxml").load();
    dialog = new Dialog<>();
    dialog.setDialogPane(dialogPane);
    dialog.initOwner(stage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initStyle(StageStyle.UNDECORATED);
  }

  public void stockInMasterSaveBtnClicked() {
    if (!stockInDetailTable.isDisabled()
        && StockInDetailViewModel.stockInDetailsTempList.isEmpty()) {
      // Notify table can't be empty
      System.out.println("Table can't be empty");
    }
    if (!stockInMasterBranchValidationLabel.isVisible()
        && !stockInMasterDateValidationLabel.isVisible()) {
      if (Integer.parseInt(stockInMasterID.getText()) > 0) {
        StockInMasterViewModel.updateItem(Integer.parseInt(stockInMasterID.getText()));
        stockInMasterCancelBtnClicked();
      } else StockInMasterViewModel.saveStockInMaster();
      StockInMasterViewModel.resetProperties();
      StockInDetailViewModel.stockInDetailsTempList.clear();
    }
  }

  public void stockInMasterCancelBtnClicked() {
    BaseController.navigation.navigate(Pages.getStockInPane());
    StockInMasterViewModel.resetProperties();
    StockInDetailViewModel.stockInDetailsTempList.clear();
    stockInMasterBranchValidationLabel.setVisible(false);
    stockInMasterDateValidationLabel.setVisible(false);
  }
}
