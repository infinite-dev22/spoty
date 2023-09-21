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

package org.infinite.spoty.views.stock_in;

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
import org.infinite.spoty.database.models.StockInMaster;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.viewModels.StockInMasterViewModel;
import org.infinite.spoty.views.BaseController;

import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked")
public class StockInController implements Initializable {
    private static StockInController instance;
    @FXML
    public MFXTextField stockInSearchBar;
    @FXML
    public HBox stockInActionsPane;
    @FXML
    public MFXButton stockInImportBtn;
    @FXML
    public MFXTableView<StockInMaster> stockInMasterTable;
    @FXML
    public BorderPane stockInContentPane;

    public static StockInController getInstance() {
        if (instance == null) instance = new StockInController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<StockInMaster> stockInBranch =
                new MFXTableColumn<>("Branch", false, Comparator.comparing(StockInMaster::getBranchName));
        MFXTableColumn<StockInMaster> stockInStatus =
                new MFXTableColumn<>("Status", false, Comparator.comparing(StockInMaster::getStatus));
        MFXTableColumn<StockInMaster> stockInDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(StockInMaster::getDate));
        MFXTableColumn<StockInMaster> stockInTotalCost =
                new MFXTableColumn<>("Total Amount", false, Comparator.comparing(StockInMaster::getTotal));

        stockInBranch.setRowCellFactory(stockIn -> new MFXTableRowCell<>(StockInMaster::getBranchName));
        stockInStatus.setRowCellFactory(stockIn -> new MFXTableRowCell<>(StockInMaster::getStatus));
        stockInTotalCost.setRowCellFactory(stockIn -> new MFXTableRowCell<>(StockInMaster::getTotal));
        stockInDate.setRowCellFactory(stockIn -> new MFXTableRowCell<>(StockInMaster::getLocaleDate));

        stockInBranch.prefWidthProperty().bind(stockInMasterTable.widthProperty().multiply(.25));
        stockInStatus.prefWidthProperty().bind(stockInMasterTable.widthProperty().multiply(.25));
        stockInTotalCost.prefWidthProperty().bind(stockInMasterTable.widthProperty().multiply(.25));
        stockInDate.prefWidthProperty().bind(stockInMasterTable.widthProperty().multiply(.25));

        stockInMasterTable
                .getTableColumns()
                .addAll(stockInBranch, stockInStatus, stockInDate, stockInTotalCost);
        stockInMasterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference", StockInMaster::getRef),
                        new StringFilter<>("Branch", StockInMaster::getBranchName),
                        new StringFilter<>("Status", StockInMaster::getStatus),
                        new DoubleFilter<>("Total Amount", StockInMaster::getTotal));
        getStockInMasterTable();

        if (StockInMasterViewModel.getStockIns().isEmpty()) {
            StockInMasterViewModel.getStockIns()
                    .addListener(
                            (ListChangeListener<StockInMaster>)
                                    c -> stockInMasterTable.setItems(StockInMasterViewModel.getStockIns()));
        } else {
            stockInMasterTable
                    .itemsProperty()
                    .bindBidirectional(StockInMasterViewModel.stockInsProperty());
        }
    }

    private void getStockInMasterTable() {
        stockInMasterTable.setPrefSize(1000, 1000);
        stockInMasterTable.features().enableBounceEffect();
        stockInMasterTable.features().enableSmoothScrolling(0.5);

        stockInMasterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<StockInMaster> row = new MFXTableRow<>(stockInMasterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<StockInMaster>) event.getSource())
                                        .show(
                                                stockInMasterTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<StockInMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(stockInMasterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    StockInMasterViewModel.deleteItem(obj.getData().getId());
                                } catch (SQLException ex) {
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
                                    StockInMasterViewModel.getItem(obj.getData().getId());
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                            });

                    stockInCreateBtnClicked();

                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    @FXML
    private void stockInCreateBtnClicked() {
        BaseController.navigation.navigate(Pages.getStockinMasterFormPane());
    }
}
