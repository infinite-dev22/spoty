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

package org.infinite.spoty.views.transfer;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.WeakListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.database.models.TransferMaster;
import org.infinite.spoty.viewModels.TransferMasterViewModel;
import org.infinite.spoty.views.BaseController;

@SuppressWarnings("unchecked")
public class TransferController implements Initializable {
  private static TransferController instance;
  @FXML public MFXTextField transferSearchBar;
  @FXML public HBox transferActionsPane;
  @FXML public MFXButton transferImportBtn;
  @FXML public MFXTableView<TransferMaster> transferMasterTable;
  @FXML public BorderPane transferContentPane;

  public static TransferController getInstance() {
    if (instance == null) instance = new TransferController();
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Platform.runLater(this::setupTable);
  }

  private void setupTable() {
    MFXTableColumn<TransferMaster> transferDate =
        new MFXTableColumn<>("Date", false, Comparator.comparing(TransferMaster::getDate));
    MFXTableColumn<TransferMaster> transferFromBranch =
        new MFXTableColumn<>(
            "Branch(From)", false, Comparator.comparing(TransferMaster::getFromBranchName));
    MFXTableColumn<TransferMaster> transferToBranch =
        new MFXTableColumn<>(
            "Branch(To)", false, Comparator.comparing(TransferMaster::getToBranchName));
    MFXTableColumn<TransferMaster> transferStatus =
        new MFXTableColumn<>("Status", false, Comparator.comparing(TransferMaster::getStatus));
    MFXTableColumn<TransferMaster> transferTotalCost =
        new MFXTableColumn<>("Total Cost", false, Comparator.comparing(TransferMaster::getTotal));

    transferDate.setRowCellFactory(
        transfer -> new MFXTableRowCell<>(TransferMaster::getLocaleDate));
    transferFromBranch.setRowCellFactory(
        transfer -> new MFXTableRowCell<>(TransferMaster::getFromBranchName));
    transferToBranch.setRowCellFactory(
        transfer -> new MFXTableRowCell<>(TransferMaster::getToBranchName));
    transferStatus.setRowCellFactory(transfer -> new MFXTableRowCell<>(TransferMaster::getStatus));
    transferTotalCost.setRowCellFactory(
        transfer -> new MFXTableRowCell<>(TransferMaster::getTotal));

    transferDate.prefWidthProperty().bind(transferMasterTable.widthProperty().multiply(.17));
    transferFromBranch.prefWidthProperty().bind(transferMasterTable.widthProperty().multiply(.17));
    transferToBranch.prefWidthProperty().bind(transferMasterTable.widthProperty().multiply(.17));
    transferStatus.prefWidthProperty().bind(transferMasterTable.widthProperty().multiply(.17));
    transferTotalCost.prefWidthProperty().bind(transferMasterTable.widthProperty().multiply(.17));

    transferMasterTable
        .getTableColumns()
        .addAll(
            transferDate, transferFromBranch, transferToBranch, transferStatus, transferTotalCost);
    transferMasterTable
        .getFilters()
        .addAll(
            new StringFilter<>("Reference", TransferMaster::getRef),
            new StringFilter<>("Branch(From)", TransferMaster::getFromBranchName),
            new StringFilter<>("Branch(To)", TransferMaster::getToBranchName),
            new StringFilter<>("Status", TransferMaster::getStatus),
            new DoubleFilter<>("Total Cost", TransferMaster::getTotal));
    getTransferMasterTable();
    transferMasterTable.setItems(TransferMasterViewModel.transferMasterList);
    TransferMasterViewModel.transferMasterList.addListener(
        new WeakListChangeListener<>(
            c -> transferMasterTable.setItems(TransferMasterViewModel.transferMasterList)));
  }

  private void getTransferMasterTable() {
    transferMasterTable.setPrefSize(1000, 1000);
    transferMasterTable.features().enableBounceEffect();
    transferMasterTable.features().enableSmoothScrolling(0.5);

    transferMasterTable.setTableRowFactory(
        t -> {
          MFXTableRow<TransferMaster> row = new MFXTableRow<>(transferMasterTable, t);
          EventHandler<ContextMenuEvent> eventHandler =
              event -> {
                showContextMenu((MFXTableRow<TransferMaster>) event.getSource())
                    .show(
                        transferMasterTable.getScene().getWindow(),
                        event.getScreenX(),
                        event.getScreenY());
                event.consume();
              };
          row.setOnContextMenuRequested(eventHandler);
          return row;
        });
  }

  private MFXContextMenu showContextMenu(MFXTableRow<TransferMaster> obj) {
    MFXContextMenu contextMenu = new MFXContextMenu(transferMasterTable);
    MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
    MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

    // Actions
    // Delete
    delete.setOnAction(
        e -> {
          TransferMasterViewModel.deleteItem(obj.getData().getId());
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          TransferMasterViewModel.getItem(obj.getData().getId());
          transferCreateBtnClicked();
          e.consume();
        });

    contextMenu.addItems(edit, delete);

    return contextMenu;
  }

  @FXML
  private void transferCreateBtnClicked() {
    BaseController.navigation.navigate(Pages.getTransferMasterFormPane());
  }
}
