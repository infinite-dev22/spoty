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

package inc.normad.spoty.core.views.purchases;

import inc.normad.spoty.core.components.navigation.Pages;
import inc.normad.spoty.core.viewModels.purchases.PurchaseMasterViewModel;
import inc.normad.spoty.core.views.BaseController;
import inc.normad.spoty.network_bridge.dtos.purchases.PurchaseMaster;
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
public class PurchasesController implements Initializable {
    private static PurchasesController instance;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton importBtn;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    private MFXTableView<PurchaseMaster> masterTable;

    public static PurchasesController getInstance() {
        if (instance == null) instance = new PurchasesController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<PurchaseMaster> masterSupplier =
                new MFXTableColumn<>(
                        "Supplier", false, Comparator.comparing(PurchaseMaster::getSupplierName));
        MFXTableColumn<PurchaseMaster> masterStatus =
                new MFXTableColumn<>("Status", false, Comparator.comparing(PurchaseMaster::getStatus));
        MFXTableColumn<PurchaseMaster> masterPaymentStatus =
                new MFXTableColumn<>(
                        "Pay Status", false, Comparator.comparing(PurchaseMaster::getPaymentStatus));
        MFXTableColumn<PurchaseMaster> masterDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(PurchaseMaster::getDate));
        MFXTableColumn<PurchaseMaster> masterGrandTotal =
                new MFXTableColumn<>("Total Amount", false, Comparator.comparing(PurchaseMaster::getTotal));
        MFXTableColumn<PurchaseMaster> masterAmountPaid =
                new MFXTableColumn<>("Paid Amount", false, Comparator.comparing(PurchaseMaster::getPaid));
        MFXTableColumn<PurchaseMaster> masterAmountDue =
                new MFXTableColumn<>("Due Amount", false, Comparator.comparing(PurchaseMaster::getDue));

        masterSupplier.setRowCellFactory(
                master -> new MFXTableRowCell<>(PurchaseMaster::getSupplierName));
        masterStatus.setRowCellFactory(
                master -> new MFXTableRowCell<>(PurchaseMaster::getStatus));
        masterPaymentStatus.setRowCellFactory(
                master -> new MFXTableRowCell<>(PurchaseMaster::getPaymentStatus));
        masterDate.setRowCellFactory(
                master -> new MFXTableRowCell<>(PurchaseMaster::getLocaleDate));
        masterGrandTotal.setRowCellFactory(
                master -> new MFXTableRowCell<>(PurchaseMaster::getTotal));
        masterAmountPaid.setRowCellFactory(
                master -> new MFXTableRowCell<>(PurchaseMaster::getPaid));
        masterAmountDue.setRowCellFactory(
                master -> new MFXTableRowCell<>(PurchaseMaster::getDue));

        masterSupplier
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        masterStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        masterPaymentStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        masterDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        masterGrandTotal
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        masterAmountPaid
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        masterAmountDue
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));

        masterTable
                .getTableColumns()
                .addAll(
                        masterSupplier,
                        masterStatus,
                        masterPaymentStatus,
                        masterDate,
                        masterGrandTotal,
                        masterAmountPaid,
                        masterAmountDue);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference", PurchaseMaster::getRef),
                        new StringFilter<>("Supplier", PurchaseMaster::getSupplierName),
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
                                    c -> masterTable.setItems(PurchaseMasterViewModel.getPurchases()));
        } else {
            masterTable
                    .itemsProperty()
                    .bindBidirectional(PurchaseMasterViewModel.purchasesProperty());
        }
    }

    private void getTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<PurchaseMaster> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<PurchaseMaster>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<PurchaseMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            PurchaseMasterViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
                            PurchaseMasterViewModel.getPurchaseMaster(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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

    public void createBtnClicked() {
        BaseController.navigation.navigate(Pages.getPurchaseMasterFormPane());
    }

    private void onAction() {
        System.out.println("Loading purchase...");
    }

    private void onSuccess() {
        System.out.println("Loaded purchase...");
    }

    private void onFailed() {
        System.out.println("failed loading purchase...");
    }
}
