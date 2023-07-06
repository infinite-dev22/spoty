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

package org.infinite.spoty.views.sales;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXContextMenu;
import io.github.palexdev.materialfx.controls.MFXContextMenuItem;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableRow;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
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
import org.infinite.spoty.database.models.SaleMaster;
import org.infinite.spoty.viewModels.SaleMasterViewModel;
import org.infinite.spoty.views.BaseController;

@SuppressWarnings("unchecked")
public class SalesController implements Initializable {
  private static SalesController instance;
  @FXML public MFXTextField saleSearchBar;
  @FXML public HBox saleActionsPane;
  @FXML public MFXButton saleImportBtn;
  @FXML public MFXButton saleCreateBtn;
  @FXML public BorderPane saleContentPane;
  @FXML private MFXTableView<SaleMaster> saleMasterTable;

  public static SalesController getInstance() {
    if (instance == null) instance = new SalesController();
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Platform.runLater(this::setupTable);
  }

  private void setupTable() {
    MFXTableColumn<SaleMaster> saleDate =
        new MFXTableColumn<>("Date", false, Comparator.comparing(SaleMaster::getDate));
    MFXTableColumn<SaleMaster> saleCustomer =
        new MFXTableColumn<>("Customer", false, Comparator.comparing(SaleMaster::getCustomerName));
    MFXTableColumn<SaleMaster> saleBranch =
        new MFXTableColumn<>("Branch", false, Comparator.comparing(SaleMaster::getBranchName));
    MFXTableColumn<SaleMaster> saleStatus =
        new MFXTableColumn<>("Sale Status", false, Comparator.comparing(SaleMaster::getSaleStatus));
    MFXTableColumn<SaleMaster> saleGrandTotal =
        new MFXTableColumn<>("Total", false, Comparator.comparing(SaleMaster::getTotal));
    MFXTableColumn<SaleMaster> saleAmountPaid =
        new MFXTableColumn<>("Paid", false, Comparator.comparing(SaleMaster::getAmountPaid));
    MFXTableColumn<SaleMaster> saleAmountDue =
        new MFXTableColumn<>("Amount Due", false, Comparator.comparing(SaleMaster::getAmountDue));
    MFXTableColumn<SaleMaster> salePaymentStatus =
        new MFXTableColumn<>(
            "Pay Status", false, Comparator.comparing(SaleMaster::getPaymentStatus));

    saleDate.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getLocaleDate));
    saleCustomer.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getCustomerName));
    saleBranch.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getBranchName));
    saleStatus.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getSaleStatus));
    saleGrandTotal.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getTotal));
    saleAmountPaid.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getAmountPaid));
    saleAmountDue.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getAmountDue));
    salePaymentStatus.setRowCellFactory(
        sale -> new MFXTableRowCell<>(SaleMaster::getPaymentStatus));

    saleDate.prefWidthProperty().bind(saleMasterTable.widthProperty().multiply(.1));
    saleCustomer.prefWidthProperty().bind(saleMasterTable.widthProperty().multiply(.1));
    saleBranch.prefWidthProperty().bind(saleMasterTable.widthProperty().multiply(.1));
    saleStatus.prefWidthProperty().bind(saleMasterTable.widthProperty().multiply(.1));
    saleGrandTotal.prefWidthProperty().bind(saleMasterTable.widthProperty().multiply(.1));
    saleAmountPaid.prefWidthProperty().bind(saleMasterTable.widthProperty().multiply(.1));
    saleAmountDue.prefWidthProperty().bind(saleMasterTable.widthProperty().multiply(.1));
    salePaymentStatus.prefWidthProperty().bind(saleMasterTable.widthProperty().multiply(.1));

    saleMasterTable
        .getTableColumns()
        .addAll(
            saleDate,
            saleCustomer,
            saleBranch,
            saleStatus,
            saleGrandTotal,
            saleAmountPaid,
            saleAmountDue,
            salePaymentStatus);
    saleMasterTable
        .getFilters()
        .addAll(
            new StringFilter<>("Ref No.", SaleMaster::getRef),
            new StringFilter<>("Customer", SaleMaster::getCustomerName),
            new StringFilter<>("Branch", SaleMaster::getBranchName),
            new StringFilter<>("Sale Status", SaleMaster::getSaleStatus),
            new DoubleFilter<>("Grand Total", SaleMaster::getTotal),
            new DoubleFilter<>("Amount Paid", SaleMaster::getAmountPaid),
            new DoubleFilter<>("Amount Due", SaleMaster::getAmountDue),
            new StringFilter<>("Payment Status", SaleMaster::getPaymentStatus));
    styleSaleMasterTable();
    saleMasterTable.setItems(SaleMasterViewModel.saleMasterList);
    SaleMasterViewModel.saleMasterList.addListener(
        new WeakListChangeListener<>(
            c -> saleMasterTable.setItems(SaleMasterViewModel.saleMasterList)));
  }

  private void styleSaleMasterTable() {
    saleMasterTable.setPrefSize(1200, 1000);
    saleMasterTable.features().enableBounceEffect();
    saleMasterTable.features().enableSmoothScrolling(0.5);

    saleMasterTable.setTableRowFactory(
        t -> {
          MFXTableRow<SaleMaster> row = new MFXTableRow<>(saleMasterTable, t);
          EventHandler<ContextMenuEvent> eventHandler =
              event -> {
                showContextMenu((MFXTableRow<SaleMaster>) event.getSource())
                    .show(
                        saleMasterTable.getScene().getWindow(),
                        event.getScreenX(),
                        event.getScreenY());
                event.consume();
              };
          row.setOnContextMenuRequested(eventHandler);
          return row;
        });
  }

  private MFXContextMenu showContextMenu(MFXTableRow<SaleMaster> obj) {
    MFXContextMenu contextMenu = new MFXContextMenu(saleMasterTable);
    MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
    MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

    // Actions
    // Delete
    delete.setOnAction(
        e -> {
          SaleMasterViewModel.deleteItem(obj.getData().getId());
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          SaleMasterViewModel.getItem(obj.getData().getId());
          saleCreateBtnClicked();
          e.consume();
        });

    contextMenu.addItems(edit, delete);

    return contextMenu;
  }

  public void saleCreateBtnClicked() {
    BaseController.navigation.navigate(Pages.getSaleMasterFormPane());
  }
}
