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

package org.infinite.spoty.views.inventory.quotation;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
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
import javafx.scene.layout.HBox;
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.database.models.QuotationMaster;
import org.infinite.spoty.viewModels.QuotationMasterViewModel;
import org.infinite.spoty.views.BaseController;

@SuppressWarnings("unchecked")
public class QuotationController implements Initializable {
  private static QuotationController instance;
  @FXML public MFXTextField quotationSearchBar;
  @FXML public HBox quotationActionsPane;
  @FXML public MFXButton quotationImportBtn;
  @FXML public BorderPane quotationContentPane;
  @FXML private MFXTableView<QuotationMaster> quotationsTable;

  public static QuotationController getInstance() {
    if (instance == null) instance = new QuotationController();
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Platform.runLater(this::setupTable);
  }

  private void setupTable() {
    MFXTableColumn<QuotationMaster> quotationDate =
        new MFXTableColumn<>("Date", false, Comparator.comparing(QuotationMaster::getDate));
    MFXTableColumn<QuotationMaster> quotationCustomer =
        new MFXTableColumn<>(
            "Customer", false, Comparator.comparing(QuotationMaster::getCustomerName));
    MFXTableColumn<QuotationMaster> quotationBranch =
        new MFXTableColumn<>("Branch", false, Comparator.comparing(QuotationMaster::getBranchName));
    MFXTableColumn<QuotationMaster> quotationStatus =
        new MFXTableColumn<>("Status", false, Comparator.comparing(QuotationMaster::getStatus));
    MFXTableColumn<QuotationMaster> quotationGrandTotal =
        new MFXTableColumn<>("Grand Total", false, Comparator.comparing(QuotationMaster::getTotal));

    quotationDate.setRowCellFactory(
        quotation -> new MFXTableRowCell<>(QuotationMaster::getLocaleDate));
    quotationCustomer.setRowCellFactory(
        quotation -> new MFXTableRowCell<>(QuotationMaster::getCustomerName));
    quotationBranch.setRowCellFactory(
        quotation -> new MFXTableRowCell<>(QuotationMaster::getBranchName));
    quotationStatus.setRowCellFactory(
        quotation -> new MFXTableRowCell<>(QuotationMaster::getStatus));
    quotationGrandTotal.setRowCellFactory(
        quotation -> new MFXTableRowCell<>(QuotationMaster::getTotal));

    quotationDate.prefWidthProperty().bind(quotationsTable.widthProperty().multiply(.17));
    quotationCustomer.prefWidthProperty().bind(quotationsTable.widthProperty().multiply(.17));
    quotationBranch.prefWidthProperty().bind(quotationsTable.widthProperty().multiply(.17));
    quotationStatus.prefWidthProperty().bind(quotationsTable.widthProperty().multiply(.17));
    quotationGrandTotal.prefWidthProperty().bind(quotationsTable.widthProperty().multiply(.17));

    quotationsTable
        .getTableColumns()
        .addAll(
            quotationDate,
            quotationCustomer,
            quotationBranch,
            quotationStatus,
            quotationGrandTotal);
    quotationsTable
        .getFilters()
        .addAll(
            new StringFilter<>("Reference", QuotationMaster::getRef),
            new StringFilter<>("Customer", QuotationMaster::getCustomerName),
            new StringFilter<>("Branch", QuotationMaster::getBranchName),
            new StringFilter<>("Status", QuotationMaster::getStatus),
            new DoubleFilter<>("Grand Total", QuotationMaster::getTotal));
    getQuotationMasterTable();
    quotationsTable.setItems(QuotationMasterViewModel.quotationMasterList);
  }

  private void getQuotationMasterTable() {
    quotationsTable.setPrefSize(1000, 1000);
    quotationsTable.features().enableBounceEffect();
    quotationsTable.features().enableSmoothScrolling(0.5);

    quotationsTable.setTableRowFactory(
        t -> {
          MFXTableRow<QuotationMaster> row = new MFXTableRow<>(quotationsTable, t);
          EventHandler<ContextMenuEvent> eventHandler =
              event -> {
                showContextMenu((MFXTableRow<QuotationMaster>) event.getSource())
                    .show(
                        quotationsTable.getScene().getWindow(),
                        event.getScreenX(),
                        event.getScreenY());
                event.consume();
              };
          row.setOnContextMenuRequested(eventHandler);
          return row;
        });
  }

  private MFXContextMenu showContextMenu(MFXTableRow<QuotationMaster> obj) {
    MFXContextMenu contextMenu = new MFXContextMenu(quotationsTable);
    MFXContextMenuItem view = new MFXContextMenuItem("View");
    MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
    MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

    // Actions
    // Delete
    delete.setOnAction(
        e -> {
          QuotationMasterViewModel.deleteItem(obj.getData().getId());
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          QuotationMasterViewModel.getItem(obj.getData().getId());
          quotationCreateBtnClicked();
          e.consume();
        });

    contextMenu.addItems(view, edit, delete);

    return contextMenu;
  }

  public void quotationCreateBtnClicked() {
    BaseController.navigation.navigate(Pages.getQuotationMasterFormPane());
  }
}
