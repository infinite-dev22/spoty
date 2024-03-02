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

package inc.nomard.spoty.core.views.returns.purchases;

import inc.nomard.spoty.core.components.animations.SpotyAnimations;
import inc.nomard.spoty.core.viewModels.BankViewModel;
import inc.nomard.spoty.core.viewModels.returns.purchases.PurchaseReturnMasterViewModel;
import inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns.PurchaseReturnMaster;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked")
public class PurchaseReturnController implements Initializable {
    @FXML
    public MFXTableView<PurchaseReturnMaster> masterTable;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton importBtn;
    @FXML
    public MFXButton createBtn;
    @FXML
    public HBox refresh;
    private RotateTransition transition;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(PurchaseReturnMaster::getDate));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnSupplier =
                new MFXTableColumn<>(
                        "Supplier", false, Comparator.comparing(PurchaseReturnMaster::getSupplierName));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnStatus =
                new MFXTableColumn<>(
                        "Status", false, Comparator.comparing(PurchaseReturnMaster::getStatus));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnGrandTotal =
                new MFXTableColumn<>("Total", false, Comparator.comparing(PurchaseReturnMaster::getTotal));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnAmountPaid =
                new MFXTableColumn<>("Paid", false, Comparator.comparing(PurchaseReturnMaster::getPaid));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnPaymentStatus =
                new MFXTableColumn<>(
                        "Pay Status", false, Comparator.comparing(PurchaseReturnMaster::getPaymentStatus));
        purchaseReturnPaymentStatus.setTooltip(new Tooltip("PurchaseMaster Return Payment Status"));
        purchaseReturnStatus.setTooltip(new Tooltip("PurchaseMaster Return Status"));

        purchaseReturnDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.13));
        purchaseReturnSupplier
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        purchaseReturnStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        purchaseReturnGrandTotal
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        purchaseReturnAmountPaid
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        purchaseReturnPaymentStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));

        purchaseReturnDate.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getDate));
        purchaseReturnSupplier.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getSupplierName));
        purchaseReturnStatus.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getStatus));
        purchaseReturnGrandTotal.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getTotal));
        purchaseReturnAmountPaid.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getPaid));
        purchaseReturnPaymentStatus.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getPaymentStatus));

        masterTable
                .getTableColumns()
                .addAll(
                        purchaseReturnSupplier,
                        purchaseReturnStatus,
                        purchaseReturnPaymentStatus,
                        purchaseReturnDate,
                        purchaseReturnGrandTotal,
                        purchaseReturnAmountPaid);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Ref No.", PurchaseReturnMaster::getRef),
                        new StringFilter<>("Supplier", PurchaseReturnMaster::getSupplierName),
                        new StringFilter<>("PurchaseMaster Ref", PurchaseReturnMaster::getRef),
                        new StringFilter<>("Status", PurchaseReturnMaster::getStatus),
                        new DoubleFilter<>("Total", PurchaseReturnMaster::getTotal),
                        new DoubleFilter<>("Paid", PurchaseReturnMaster::getPaid),
                        new StringFilter<>("Pay Status", PurchaseReturnMaster::getPaymentStatus));

        stylePurchaseReturnMasterTable();

        if (PurchaseReturnMasterViewModel.getPurchaseReturns().isEmpty()) {
            PurchaseReturnMasterViewModel.getPurchaseReturns()
                    .addListener(
                            (ListChangeListener<PurchaseReturnMaster>)
                                    c ->
                                            masterTable.setItems(
                                                    PurchaseReturnMasterViewModel.getPurchaseReturns()));
        } else {
            masterTable
                    .itemsProperty()
                    .bindBidirectional(PurchaseReturnMasterViewModel.purchaseReturnsProperty());
        }
    }

    private void stylePurchaseReturnMasterTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);
    }

    public void createBtnClicked(MouseEvent mouseEvent) {
    }

    private void onAction() {
        transition.playFromStart();
        transition.setOnFinished((ActionEvent event) -> transition.playFromStart());
    }

    private void onSuccess() {
        transition.setOnFinished(null);
    }

    private void onFailed() {
        transition.setOnFinished(null);
    }

    private void setIcons() {
        var refreshIcon = new MFXFontIcon("fas-arrows-rotate", 24);
        refresh.getChildren().addFirst(refreshIcon);

        transition = SpotyAnimations.rotateTransition(refreshIcon, Duration.millis(1000), 360);

        refreshIcon.setOnMouseClicked(mouseEvent -> PurchaseReturnMasterViewModel.getPurchaseReturnMasters(this::onAction, this::onSuccess, this::onFailed));
    }
}
