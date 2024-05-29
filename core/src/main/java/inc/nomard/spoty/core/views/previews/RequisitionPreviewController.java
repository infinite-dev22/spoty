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

package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.network_bridge.dtos.requisitions.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import java.net.*;
import java.util.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import lombok.extern.slf4j.*;

@Slf4j
public class RequisitionPreviewController implements Initializable {
    static final ObservableList<RequisitionDetail> requisitionDetailsList = FXCollections.observableArrayList();
    private static final ListProperty<RequisitionDetail> requisitionDetails =
            new SimpleListProperty<>(requisitionDetailsList);
    @FXML
    public Label purchaseDate;
    @FXML
    public Label purchaseRef;
    @FXML
    public Label supplierName;
    @FXML
    public Label supplierNumber;
    @FXML
    public Label supplierEmail;
    @FXML
    public MFXTableView<RequisitionDetail> itemsTable;
    @FXML
    public Label subTotal;
    @FXML
    public Label discount;
    @FXML
    public Label tax;
    @FXML
    public Label shipping;
    @FXML
    public Label netCost;
    @FXML
    public Label paidAmount;
    @FXML
    public Label changeDue;
    @FXML
    public Label balance;
    @FXML
    public Label purchaseNote;
    @FXML
    public Label doneBy;

    public static ObservableList<RequisitionDetail> getRequisitionDetails() {
        return requisitionDetails.get();
    }

    public static ListProperty<RequisitionDetail> requisitionDetailsProperty() {
        return requisitionDetails;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        // Set table column titles.
        MFXTableColumn<RequisitionDetail> product =
                new MFXTableColumn<>("Name", false, Comparator.comparing(RequisitionDetail::getProductName));
        MFXTableColumn<RequisitionDetail> quantity =
                new MFXTableColumn<>("Qnty", false, Comparator.comparing(RequisitionDetail::getQuantity));
        MFXTableColumn<RequisitionDetail> price =
                new MFXTableColumn<>("Price", false, Comparator.comparing(RequisitionDetail::getPrice));
        MFXTableColumn<RequisitionDetail> totalPrice =
                new MFXTableColumn<>(
                        "Total Price", false, Comparator.comparing(RequisitionDetail::getSubTotalPrice));

        // Set table column data.
        product.setRowCellFactory(requisitionDetail -> {
            var cell = new MFXTableRowCell<>(RequisitionDetail::getProductName);
            cell.setAlignment(Pos.CENTER_LEFT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        quantity.setRowCellFactory(requisitionDetail -> {
            var cell = new MFXTableRowCell<>(RequisitionDetail::getQuantity);
            cell.setAlignment(Pos.CENTER_RIGHT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        price.setRowCellFactory(requisitionDetail -> {
            var cell = new MFXTableRowCell<>(RequisitionDetail::getPrice);
            cell.setAlignment(Pos.CENTER_RIGHT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        totalPrice.setRowCellFactory(
                requisitionDetail -> {
                    var cell = new MFXTableRowCell<>(RequisitionDetail::getSubTotalPrice);
                    cell.setAlignment(Pos.CENTER_RIGHT);
                    cell.getStyleClass().add("table-cell-border");
                    return cell;
                });

        // Set table column width.
        product.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.25));
        quantity.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.25));
        price.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.25));
        totalPrice.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.25));

        // Set table filter.
        itemsTable
                .getTableColumns()
                .addAll(product, quantity, price, totalPrice);

        styleTable();

        // Populate table.
        if (getRequisitionDetails().isEmpty()) {
            getRequisitionDetails()
                    .addListener(
                            (ListChangeListener<RequisitionDetail>)
                                    change -> itemsTable.setItems(getRequisitionDetails()));
        } else {
            itemsTable.itemsProperty().bindBidirectional(requisitionDetailsProperty());
        }
    }

    private void styleTable() {
        itemsTable.setPrefSize(1000, 1000);
        itemsTable.features().enableBounceEffect();
        itemsTable.features().enableSmoothScrolling(0.5);
        itemsTable.setFooterVisible(false);
    }

    public void init(RequisitionMaster requisition) {
        requisitionDetailsList.clear();
        purchaseDate.setText(requisition.getLocaleDate());
        purchaseRef.setText(requisition.getRef());
        supplierName.setText(requisition.getSupplier().getName());
        supplierNumber.setText(requisition.getSupplier().getPhone());
        supplierEmail.setText(requisition.getSupplier().getEmail());
        requisitionDetailsList.addAll(requisition.getRequisitionDetails());
        subTotal.setText(String.valueOf(requisition.getSubTotal()));
        discount.setText(String.valueOf(requisition.getDiscount()));
        tax.setText(String.valueOf(requisition.getTaxRate()));
        shipping.setText(String.valueOf(requisition.getShippingFee()));
        netCost.setText(String.valueOf(requisition.getTotal()));
        paidAmount.setText(String.valueOf(requisition.getAmountPaid()));
        changeDue.setText(String.valueOf(requisition.getChangeAmount()));
        balance.setText(String.valueOf(requisition.getBalanceAmount()));
        purchaseNote.setText(requisition.getNotes());
//        doneBy.setText(purchase.doneBy());
    }
}
