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

package inc.normad.spoty.core.views.stock_in;

import inc.normad.spoty.core.components.navigation.Pages;
import inc.normad.spoty.core.viewModels.stock_ins.StockInMasterViewModel;
import inc.normad.spoty.core.views.BaseController;
import inc.normad.spoty.network_bridge.dtos.stock_ins.StockInMaster;
import inc.normad.spoty.utils.SpotyThreader;
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

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked")
public class StockInController implements Initializable {
    private static StockInController instance;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton importBtn;
    @FXML
    public MFXTableView<StockInMaster> masterTable;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXButton createBtn;

    public static StockInController getInstance() {
        if (instance == null) instance = new StockInController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<StockInMaster> status =
                new MFXTableColumn<>("Status", false, Comparator.comparing(StockInMaster::getStatus));
        MFXTableColumn<StockInMaster> stockInDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(StockInMaster::getDate));
        MFXTableColumn<StockInMaster> stockInTotalCost =
                new MFXTableColumn<>("Total Amount", false, Comparator.comparing(StockInMaster::getTotal));

        status.setRowCellFactory(stockIn -> new MFXTableRowCell<>(StockInMaster::getStatus));
        stockInTotalCost.setRowCellFactory(stockIn -> new MFXTableRowCell<>(StockInMaster::getTotal));
        stockInDate.setRowCellFactory(stockIn -> new MFXTableRowCell<>(StockInMaster::getLocaleDate));

        status.prefWidthProperty().bind(masterTable.widthProperty().multiply(.3));
        stockInTotalCost.prefWidthProperty().bind(masterTable.widthProperty().multiply(.3));
        stockInDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.3));

        masterTable
                .getTableColumns()
                .addAll(status, stockInDate, stockInTotalCost);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference", StockInMaster::getRef),
                        new StringFilter<>("Status", StockInMaster::getStatus),
                        new DoubleFilter<>("Total Amount", StockInMaster::getTotal));
        getStockInMasterTable();

        if (StockInMasterViewModel.getStockIns().isEmpty()) {
            StockInMasterViewModel.getStockIns()
                    .addListener(
                            (ListChangeListener<StockInMaster>)
                                    c -> masterTable.setItems(StockInMasterViewModel.getStockIns()));
        } else {
            masterTable
                    .itemsProperty()
                    .bindBidirectional(StockInMasterViewModel.stockInsProperty());
        }
    }

    private void getStockInMasterTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<StockInMaster> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<StockInMaster>) event.getSource())
                                        .show(
                                                masterTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<StockInMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    StockInMasterViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
                                    StockInMasterViewModel.getItem(obj.getData().getId(), this::onAction, this::onFailed);
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            });

                    createBtnClicked();

                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    @FXML
    private void createBtnClicked() {
        BaseController.navigation.navigate(Pages.getStockInMasterFormPane());
    }

    private void onAction() {
        System.out.println("Loading stock ins...");
    }

    private void onSuccess() {
        System.out.println("Loaded stock ins...");
    }

    private void onFailed() {
        System.out.println("failed loading stock ins...");
    }
}
