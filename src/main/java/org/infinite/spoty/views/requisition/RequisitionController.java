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

package org.infinite.spoty.views.requisition;

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
import org.infinite.spoty.data_source.dtos.requisitions.RequisitionMaster;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.viewModels.requisitions.RequisitionMasterViewModel;
import org.infinite.spoty.views.BaseController;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked")
public class RequisitionController implements Initializable {
    private static RequisitionController instance;
    @FXML
    public MFXTextField requisitionSearchBar;
    @FXML
    public HBox requisitionActionsPane;
    @FXML
    public MFXButton requisitionImportBtn;
    @FXML
    public MFXTableView<RequisitionMaster> requisitionMasterTable;
    @FXML
    public BorderPane requisitionContentPane;

    public static RequisitionController getInstance() {
        if (instance == null) instance = new RequisitionController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<RequisitionMaster> requisitionSupplier =
                new MFXTableColumn<>(
                        "Supplier", false, Comparator.comparing(RequisitionMaster::getSupplierName));
        MFXTableColumn<RequisitionMaster> requisitionStatus =
                new MFXTableColumn<>("Status", false, Comparator.comparing(RequisitionMaster::getStatus));
        MFXTableColumn<RequisitionMaster> requisitionShippingMethod =
                new MFXTableColumn<>(
                        "Shipping Method", false, Comparator.comparing(RequisitionMaster::getShipMethod));
        MFXTableColumn<RequisitionMaster> requisitionDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(RequisitionMaster::getDate));
        MFXTableColumn<RequisitionMaster> requisitionTotalAmount =
                new MFXTableColumn<>(
                        "Total Amount", false, Comparator.comparing(RequisitionMaster::getTotalCost));

        requisitionSupplier.setRowCellFactory(
                requisition -> new MFXTableRowCell<>(RequisitionMaster::getSupplierName));
        requisitionStatus.setRowCellFactory(
                requisition -> new MFXTableRowCell<>(RequisitionMaster::getStatus));
        requisitionShippingMethod.setRowCellFactory(
                requisition -> new MFXTableRowCell<>(RequisitionMaster::getShipMethod));
        requisitionDate.setRowCellFactory(
                requisition -> new MFXTableRowCell<>(RequisitionMaster::getLocaleDate));
        requisitionTotalAmount.setRowCellFactory(
                requisition -> new MFXTableRowCell<>(RequisitionMaster::getTotalCost));

        requisitionSupplier
                .prefWidthProperty()
                .bind(requisitionMasterTable.widthProperty().multiply(.25));
        requisitionStatus
                .prefWidthProperty()
                .bind(requisitionMasterTable.widthProperty().multiply(.25));
        requisitionShippingMethod
                .prefWidthProperty()
                .bind(requisitionMasterTable.widthProperty().multiply(.25));
        requisitionDate.prefWidthProperty().bind(requisitionMasterTable.widthProperty().multiply(.25));
        requisitionTotalAmount
                .prefWidthProperty()
                .bind(requisitionMasterTable.widthProperty().multiply(.25));

        requisitionMasterTable
                .getTableColumns()
                .addAll(
                        requisitionSupplier,
                        requisitionStatus,
                        requisitionShippingMethod,
                        requisitionDate,
                        requisitionTotalAmount);
        requisitionMasterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference", RequisitionMaster::getRef),
                        new StringFilter<>("Supplier", RequisitionMaster::getSupplierName),
                        new StringFilter<>("Status", RequisitionMaster::getStatus),
                        new StringFilter<>("Shipping Method", RequisitionMaster::getShipMethod),
                        new DoubleFilter<>("Total Amount", RequisitionMaster::getTotalCost));
        getRequisitionMasterTable();

        if (RequisitionMasterViewModel.getRequisitions().isEmpty()) {
            RequisitionMasterViewModel.getRequisitions()
                    .addListener(
                            (ListChangeListener<RequisitionMaster>)
                                    c ->
                                            requisitionMasterTable.setItems(
                                                    RequisitionMasterViewModel.getRequisitions()));
        } else {
            requisitionMasterTable
                    .itemsProperty()
                    .bindBidirectional(RequisitionMasterViewModel.requisitionsProperty());
        }
    }

    private void getRequisitionMasterTable() {
        requisitionMasterTable.setPrefSize(1000, 1000);
        requisitionMasterTable.features().enableBounceEffect();
        requisitionMasterTable.features().enableSmoothScrolling(0.5);

        requisitionMasterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<RequisitionMaster> row = new MFXTableRow<>(requisitionMasterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<RequisitionMaster>) event.getSource())
                                        .show(
                                                requisitionMasterTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<RequisitionMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(requisitionMasterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            RequisitionMasterViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
                            RequisitionMasterViewModel.getItem(obj.getData().getId(), this::onAction, this::onFailed);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    requisitionCreateBtnClicked();
                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    public void requisitionCreateBtnClicked() {
        BaseController.navigation.navigate(Pages.getRequisitionMasterFormPane());
    }

    private void onAction() {
        System.out.println("Loading requisition...");
    }

    private void onSuccess() {
        System.out.println("Loaded requisition...");
    }

    private void onFailed() {
        System.out.println("failed loading requisition...");
    }
}
