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

package org.infinite.spoty.views.returns.sales;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.infinite.spoty.data_source.daos.returns.sale_returns.SaleReturnMaster;
import org.infinite.spoty.viewModels.returns.sales.SaleReturnMasterViewModel;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked")
public class SaleReturnsController implements Initializable {
    @FXML
    public BorderPane saleReturnContentPane;
    @FXML
    public MFXTextField saleReturnSearchBar;
    @FXML
    public HBox saleReturnActionsPane;
    @FXML
    public MFXButton saleReturnImportBtn;
    @FXML
    private MFXTableView<SaleReturnMaster> saleReturnTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<SaleReturnMaster> saleReturnDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(SaleReturnMaster::getDate));
        MFXTableColumn<SaleReturnMaster> saleReturnCustomer =
                new MFXTableColumn<>(
                        "Customer", false, Comparator.comparing(SaleReturnMaster::getCustomerName));
        MFXTableColumn<SaleReturnMaster> saleReturnBranch =
                new MFXTableColumn<>(
                        "Branch", false, Comparator.comparing(SaleReturnMaster::getBranchName));
        MFXTableColumn<SaleReturnMaster> saleReturnStatus =
                new MFXTableColumn<>("Status", false, Comparator.comparing(SaleReturnMaster::getStatus));
        MFXTableColumn<SaleReturnMaster> saleReturnGrandTotal =
                new MFXTableColumn<>("Total", false, Comparator.comparing(SaleReturnMaster::getTotal));
        MFXTableColumn<SaleReturnMaster> saleReturnAmountPaid =
                new MFXTableColumn<>("Paid", false, Comparator.comparing(SaleReturnMaster::getPaid));
        MFXTableColumn<SaleReturnMaster> saleReturnPaymentStatus =
                new MFXTableColumn<>(
                        "Pay Status", false, Comparator.comparing(SaleReturnMaster::getPaymentStatus));

        saleReturnPaymentStatus.setTooltip(new Tooltip("PurchaseMaster Return Payment Status"));
        saleReturnStatus.setTooltip(new Tooltip("PurchaseMaster Return Status"));
        saleReturnBranch.setTooltip(new Tooltip("Branch, store or warehouse"));

        saleReturnDate.prefWidthProperty().bind(saleReturnTable.widthProperty().multiply(.143));
        saleReturnCustomer.prefWidthProperty().bind(saleReturnTable.widthProperty().multiply(.143));
        saleReturnBranch.prefWidthProperty().bind(saleReturnTable.widthProperty().multiply(.143));
        saleReturnStatus.prefWidthProperty().bind(saleReturnTable.widthProperty().multiply(.143));
        saleReturnGrandTotal.prefWidthProperty().bind(saleReturnTable.widthProperty().multiply(.143));
        saleReturnAmountPaid.prefWidthProperty().bind(saleReturnTable.widthProperty().multiply(.143));
        saleReturnPaymentStatus
                .prefWidthProperty()
                .bind(saleReturnTable.widthProperty().multiply(.143));

        saleReturnDate.setRowCellFactory(
                saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getDate));
        saleReturnCustomer.setRowCellFactory(
                saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getCustomerName));
        saleReturnBranch.setRowCellFactory(
                saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getBranchName));
        saleReturnStatus.setRowCellFactory(
                saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getStatus));
        saleReturnGrandTotal.setRowCellFactory(
                saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getTotal));
        saleReturnAmountPaid.setRowCellFactory(
                saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getPaid));
        saleReturnPaymentStatus.setRowCellFactory(
                saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getPaymentStatus));

        saleReturnTable
                .getTableColumns()
                .addAll(
                        saleReturnCustomer,
                        saleReturnBranch,
                        saleReturnStatus,
                        saleReturnPaymentStatus,
                        saleReturnDate,
                        saleReturnGrandTotal,
                        saleReturnAmountPaid);

        saleReturnTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Ref No.", SaleReturnMaster::getRef),
                        new StringFilter<>("Customer", SaleReturnMaster::getCustomerName),
                        new StringFilter<>("Branch", SaleReturnMaster::getBranchName),
                        new StringFilter<>("Status", SaleReturnMaster::getStatus),
                        new DoubleFilter<>("Total", SaleReturnMaster::getTotal),
                        new DoubleFilter<>("Paid", SaleReturnMaster::getPaid),
                        new StringFilter<>("Pay Status", SaleReturnMaster::getPaymentStatus));

        getSaleReturnMasterTable();

        if (SaleReturnMasterViewModel.getSaleReturns().isEmpty()) {
            SaleReturnMasterViewModel.getSaleReturns()
                    .addListener(
                            (ListChangeListener<SaleReturnMaster>)
                                    c -> saleReturnTable.setItems(SaleReturnMasterViewModel.getSaleReturns()));
        } else {
            saleReturnTable
                    .itemsProperty()
                    .bindBidirectional(SaleReturnMasterViewModel.saleReturnsProperty());
        }
    }

    private void getSaleReturnMasterTable() {
        saleReturnTable.setPrefSize(1200, 1000);
        saleReturnTable.features().enableBounceEffect();
        saleReturnTable.features().enableSmoothScrolling(0.5);
    }
}
