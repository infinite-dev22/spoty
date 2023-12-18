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

package org.infinite.spoty.views.inventory.adjustment;

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
import org.infinite.spoty.data_source.dtos.adjustments.AdjustmentMaster;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.viewModels.AdjustmentMasterViewModel;
import org.infinite.spoty.views.BaseController;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked")
public class AdjustmentController implements Initializable {
    private static AdjustmentController instance;
    @FXML
    public BorderPane adjustmentContentPane;
    @FXML
    public MFXTextField adjustmentSearchBar;
    @FXML
    public HBox adjustmentActionsPane;
    @FXML
    public MFXButton adjustmentImportBtn;
    @FXML
    private MFXTableView<AdjustmentMaster> adjustmentMasterTable;

    public static AdjustmentController getInstance() {
        if (instance == null) instance = new AdjustmentController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<AdjustmentMaster> adjustmentDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(AdjustmentMaster::getDate));
        MFXTableColumn<AdjustmentMaster> adjustmentBranch =
                new MFXTableColumn<>(
                        "Branch", false, Comparator.comparing(AdjustmentMaster::getBranchName));
        MFXTableColumn<AdjustmentMaster> adjustmentStatus =
                new MFXTableColumn<>("Status", false, Comparator.comparing(AdjustmentMaster::getStatus));
        MFXTableColumn<AdjustmentMaster> adjustmentTotalAmount =
                new MFXTableColumn<>(
                        "Total Amount", false, Comparator.comparing(AdjustmentMaster::getTotal));

        adjustmentDate.setRowCellFactory(
                adjustment -> new MFXTableRowCell<>(AdjustmentMaster::getLocaleDate));
        adjustmentBranch.setRowCellFactory(
                adjustment -> new MFXTableRowCell<>(AdjustmentMaster::getBranchName));
        adjustmentStatus.setRowCellFactory(
                adjustment -> new MFXTableRowCell<>(AdjustmentMaster::getBranchName));
        adjustmentTotalAmount.setRowCellFactory(
                adjustment -> new MFXTableRowCell<>(AdjustmentMaster::getBranchName));

        adjustmentDate.prefWidthProperty().bind(adjustmentMasterTable.widthProperty().multiply(.5));
        adjustmentBranch.prefWidthProperty().bind(adjustmentMasterTable.widthProperty().multiply(.5));
        adjustmentStatus.prefWidthProperty().bind(adjustmentMasterTable.widthProperty().multiply(.5));
        adjustmentTotalAmount
                .prefWidthProperty()
                .bind(adjustmentMasterTable.widthProperty().multiply(.5));

        adjustmentMasterTable
                .getTableColumns()
                .addAll(adjustmentDate, adjustmentBranch, adjustmentStatus, adjustmentTotalAmount);
        adjustmentMasterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference", AdjustmentMaster::getRef),
                        new StringFilter<>("Branch", AdjustmentMaster::getBranchName),
                        new StringFilter<>("Status", AdjustmentMaster::getStatus),
                        new DoubleFilter<>("Total Amount", AdjustmentMaster::getTotal));
        getAdjustmentMasterTable();

        if (AdjustmentMasterViewModel.getAdjustmentMasters().isEmpty()) {
            AdjustmentMasterViewModel.getAdjustmentMasters()
                    .addListener(
                            (ListChangeListener<AdjustmentMaster>)
                                    c ->
                                            adjustmentMasterTable.setItems(
                                                    AdjustmentMasterViewModel.getAdjustmentMasters()));
        } else {
            adjustmentMasterTable
                    .itemsProperty()
                    .bindBidirectional(AdjustmentMasterViewModel.adjustmentMastersProperty());
        }
    }

    private void getAdjustmentMasterTable() {
        adjustmentMasterTable.setPrefSize(1000, 1000);
        adjustmentMasterTable.features().enableBounceEffect();
        adjustmentMasterTable.features().enableSmoothScrolling(0.5);

        adjustmentMasterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<AdjustmentMaster> row = new MFXTableRow<>(adjustmentMasterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<AdjustmentMaster>) event.getSource())
                                        .show(
                                                adjustmentMasterTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<AdjustmentMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(adjustmentMasterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    AdjustmentMasterViewModel.deleteItem(obj.getData().getId());
                                } catch (Exception ex) {
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
                                    AdjustmentMasterViewModel.getItem(obj.getData().getId());
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            });

                    adjustmentCreateBtnClicked();
                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    public void adjustmentCreateBtnClicked() {
        BaseController.navigation.navigate(Pages.getAdjustmentMasterFormPane());
    }
}
