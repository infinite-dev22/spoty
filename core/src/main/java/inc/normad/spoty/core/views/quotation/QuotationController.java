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

package inc.normad.spoty.core.views.quotation;

import inc.normad.spoty.core.components.navigation.Pages;
import inc.normad.spoty.core.viewModels.quotations.QuotationMasterViewModel;
import inc.normad.spoty.core.views.BaseController;
import inc.normad.spoty.network_bridge.dtos.quotations.QuotationMaster;
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
public class QuotationController implements Initializable {
    private static QuotationController instance;
    @FXML
    public MFXTextField quotationSearchBar;
    @FXML
    public HBox quotationActionsPane;
    @FXML
    public MFXButton quotationImportBtn;
    @FXML
    public BorderPane quotationContentPane;
    @FXML
    private MFXTableView<QuotationMaster> quotationsTable;

    public static QuotationController getInstance() {
        if (instance == null) instance = new QuotationController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<QuotationMaster> quotationCustomer =
                new MFXTableColumn<>(
                        "Customer", false, Comparator.comparing(QuotationMaster::getCustomerName));
        MFXTableColumn<QuotationMaster> quotationStatus =
                new MFXTableColumn<>("Status", false, Comparator.comparing(QuotationMaster::getStatus));
        MFXTableColumn<QuotationMaster> quotationDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(QuotationMaster::getDate));
        MFXTableColumn<QuotationMaster> quotationTotalAmount =
                new MFXTableColumn<>(
                        "Total Amount", false, Comparator.comparing(QuotationMaster::getTotal));

        quotationCustomer.setRowCellFactory(
                quotation -> new MFXTableRowCell<>(QuotationMaster::getCustomerName));
        quotationStatus.setRowCellFactory(
                quotation -> new MFXTableRowCell<>(QuotationMaster::getStatus));
        quotationDate.setRowCellFactory(
                quotation -> new MFXTableRowCell<>(QuotationMaster::getLocaleDate));
        quotationTotalAmount.setRowCellFactory(
                quotation -> new MFXTableRowCell<>(QuotationMaster::getTotal));

        quotationCustomer.prefWidthProperty().bind(quotationsTable.widthProperty().multiply(.25));
        quotationStatus.prefWidthProperty().bind(quotationsTable.widthProperty().multiply(.25));
        quotationDate.prefWidthProperty().bind(quotationsTable.widthProperty().multiply(.25));
        quotationTotalAmount.prefWidthProperty().bind(quotationsTable.widthProperty().multiply(.25));

        quotationsTable
                .getTableColumns()
                .addAll(
                        quotationCustomer,
                        quotationStatus,
                        quotationDate,
                        quotationTotalAmount);
        quotationsTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference", QuotationMaster::getRef),
                        new StringFilter<>("Customer", QuotationMaster::getCustomerName),
                        new StringFilter<>("Status", QuotationMaster::getStatus),
                        new DoubleFilter<>("Grand Total", QuotationMaster::getTotal));
        getQuotationMasterTable();

        if (QuotationMasterViewModel.getQuotations().isEmpty()) {
            QuotationMasterViewModel.getQuotations()
                    .addListener(
                            (ListChangeListener<QuotationMaster>)
                                    c -> quotationsTable.setItems(QuotationMasterViewModel.getQuotations()));
        } else {
            quotationsTable
                    .itemsProperty()
                    .bindBidirectional(QuotationMasterViewModel.quotationsProperty());
        }
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
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            QuotationMasterViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
                            QuotationMasterViewModel.getQuotationMaster(obj.getData().getId(), this::onAction, this::onFailed);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    quotationCreateBtnClicked();
                    e.consume();
                });

        contextMenu.addItems(view, edit, delete);

        return contextMenu;
    }

    public void quotationCreateBtnClicked() {
        BaseController.navigation.navigate(Pages.getQuotationMasterFormPane());
    }

    private void onAction() {
        System.out.println("Loading quotation...");
    }

    private void onSuccess() {
        System.out.println("Loaded quotation...");
    }

    private void onFailed() {
        System.out.println("failed loading quotation...");
    }
}
