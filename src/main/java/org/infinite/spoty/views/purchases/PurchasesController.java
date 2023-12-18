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

package org.infinite.spoty.views.purchases;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.data_source.dtos.purchases.PurchaseMaster;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.viewModels.PurchaseMasterViewModel;
import org.infinite.spoty.views.BaseController;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked")
public class PurchasesController implements Initializable {
    private static PurchasesController instance;
    @FXML
    public MFXTextField purchaseSearchBar;
    @FXML
    public HBox purchaseActionsPane;
    @FXML
    public MFXButton purchaseImportBtn;
    @FXML
    public BorderPane purchaseContentPane;
    @FXML
    private MFXTableView<PurchaseMaster> purchaseMasterTable;

    public static PurchasesController getInstance() {
        if (instance == null) instance = new PurchasesController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<PurchaseMaster> purchaseMasterSupplier =
                new MFXTableColumn<>(
                        "Supplier", false, Comparator.comparing(PurchaseMaster::getSupplierName));
        MFXTableColumn<PurchaseMaster> purchaseMasterBranch =
                new MFXTableColumn<>("Branch", false, Comparator.comparing(PurchaseMaster::getBranchName));
        MFXTableColumn<PurchaseMaster> purchaseMasterStatus =
                new MFXTableColumn<>("Status", false, Comparator.comparing(PurchaseMaster::getStatus));
        MFXTableColumn<PurchaseMaster> purchaseMasterPaymentStatus =
                new MFXTableColumn<>(
                        "Pay Status", false, Comparator.comparing(PurchaseMaster::getPaymentStatus));
        MFXTableColumn<PurchaseMaster> purchaseMasterDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(PurchaseMaster::getDate));
        MFXTableColumn<PurchaseMaster> purchaseMasterGrandTotal =
                new MFXTableColumn<>("Total Amount", false, Comparator.comparing(PurchaseMaster::getTotal));
        MFXTableColumn<PurchaseMaster> purchaseMasterAmountPaid =
                new MFXTableColumn<>("Paid Amount", false, Comparator.comparing(PurchaseMaster::getPaid));
        MFXTableColumn<PurchaseMaster> purchaseMasterAmountDue =
                new MFXTableColumn<>("Due Amount", false, Comparator.comparing(PurchaseMaster::getDue));

        purchaseMasterSupplier.setRowCellFactory(
                purchaseMaster -> new MFXTableRowCell<>(PurchaseMaster::getSupplierName));
        purchaseMasterBranch.setRowCellFactory(
                purchaseMaster -> new MFXTableRowCell<>(PurchaseMaster::getBranchName));
        purchaseMasterStatus.setRowCellFactory(
                purchaseMaster -> new MFXTableRowCell<>(PurchaseMaster::getStatus));
        purchaseMasterPaymentStatus.setRowCellFactory(
                purchaseMaster -> new MFXTableRowCell<>(PurchaseMaster::getPaymentStatus));
        purchaseMasterDate.setRowCellFactory(
                purchaseMaster -> new MFXTableRowCell<>(PurchaseMaster::getLocaleDate));
        purchaseMasterGrandTotal.setRowCellFactory(
                purchaseMaster -> new MFXTableRowCell<>(PurchaseMaster::getTotal));
        purchaseMasterAmountPaid.setRowCellFactory(
                purchaseMaster -> new MFXTableRowCell<>(PurchaseMaster::getPaid));
        purchaseMasterAmountDue.setRowCellFactory(
                purchaseMaster -> new MFXTableRowCell<>(PurchaseMaster::getDue));

        purchaseMasterSupplier
                .prefWidthProperty()
                .bind(purchaseMasterTable.widthProperty().multiply(.25));
        purchaseMasterBranch
                .prefWidthProperty()
                .bind(purchaseMasterTable.widthProperty().multiply(.25));
        purchaseMasterStatus
                .prefWidthProperty()
                .bind(purchaseMasterTable.widthProperty().multiply(.25));
        purchaseMasterPaymentStatus
                .prefWidthProperty()
                .bind(purchaseMasterTable.widthProperty().multiply(.25));
        purchaseMasterDate.prefWidthProperty().bind(purchaseMasterTable.widthProperty().multiply(.25));
        purchaseMasterGrandTotal
                .prefWidthProperty()
                .bind(purchaseMasterTable.widthProperty().multiply(.25));
        purchaseMasterAmountPaid
                .prefWidthProperty()
                .bind(purchaseMasterTable.widthProperty().multiply(.25));
        purchaseMasterAmountDue
                .prefWidthProperty()
                .bind(purchaseMasterTable.widthProperty().multiply(.25));

        purchaseMasterTable
                .getTableColumns()
                .addAll(
                        purchaseMasterSupplier,
                        purchaseMasterBranch,
                        purchaseMasterStatus,
                        purchaseMasterPaymentStatus,
                        purchaseMasterDate,
                        purchaseMasterGrandTotal,
                        purchaseMasterAmountPaid,
                        purchaseMasterAmountDue);
        purchaseMasterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference", PurchaseMaster::getRef),
                        new StringFilter<>("Supplier", PurchaseMaster::getSupplierName),
                        new StringFilter<>("Branch", PurchaseMaster::getBranchName),
                        new StringFilter<>("Status", PurchaseMaster::getStatus),
                        new StringFilter<>("Pay Status", PurchaseMaster::getPaymentStatus),
                        new DoubleFilter<>("Total", PurchaseMaster::getTotal),
                        new DoubleFilter<>("Paid", PurchaseMaster::getPaid),
                        new DoubleFilter<>("Due", PurchaseMaster::getDue));
        getTable();

        if (PurchaseMasterViewModel.getPurchases().isEmpty()) {
            PurchaseMasterViewModel.getPurchases()
                    .addListener(
                            (ListChangeListener<PurchaseMaster>)
                                    c -> purchaseMasterTable.setItems(PurchaseMasterViewModel.getPurchases()));
        } else {
            purchaseMasterTable
                    .itemsProperty()
                    .bindBidirectional(PurchaseMasterViewModel.purchasesProperty());
        }
    }

    private void getTable() {
        purchaseMasterTable.setPrefSize(1200, 1000);
        purchaseMasterTable.features().enableBounceEffect();
        purchaseMasterTable.features().enableSmoothScrolling(0.5);

        purchaseMasterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<PurchaseMaster> row = new MFXTableRow<>(purchaseMasterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<PurchaseMaster>) event.getSource())
                                        .show(
                                                purchaseMasterTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<PurchaseMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(purchaseMasterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            PurchaseMasterViewModel.deleteItem(obj.getData().getId());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            PurchaseMasterViewModel.getItem(obj.getData().getId());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    purchaseCreateBtnClicked();
                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    public void purchaseCreateBtnClicked() {
        BaseController.navigation.navigate(Pages.getPurchaseMasterFormPane());
    }
}
