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

package inc.nomard.spoty.core.views.previews.purchases;

import inc.nomard.spoty.network_bridge.dtos.purchases.*;
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
import lombok.extern.java.*;

@Log
public class PurchasePreviewController implements Initializable {
    static final ObservableList<PurchaseDetail> purchaseDetailsList = FXCollections.observableArrayList();
    private static final ListProperty<PurchaseDetail> purchaseDetails =
            new SimpleListProperty<>(purchaseDetailsList);
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
    public MFXTableView<PurchaseDetail> itemsTable;
    @FXML
    public Label grandTotal;
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

    public static ObservableList<PurchaseDetail> getPurchaseDetails() {
        return purchaseDetails.get();
    }

    public static ListProperty<PurchaseDetail> purchaseDetailsProperty() {
        return purchaseDetails;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        // Set table column titles.
        MFXTableColumn<PurchaseDetail> product =
                new MFXTableColumn<>("Name", false, Comparator.comparing(PurchaseDetail::getProductName));
        MFXTableColumn<PurchaseDetail> quantity =
                new MFXTableColumn<>("Qty", false, Comparator.comparing(PurchaseDetail::getQuantity));
        MFXTableColumn<PurchaseDetail> price =
                new MFXTableColumn<>("Unit Cost", false, Comparator.comparing(PurchaseDetail::getCost));
        MFXTableColumn<PurchaseDetail> totalPrice =
                new MFXTableColumn<>(
                        "Cost", false, Comparator.comparing(PurchaseDetail::getSubTotalCost));

        // Set table column data.
        product.setRowCellFactory(purchaseDetail -> {
            var cell = new MFXTableRowCell<>(PurchaseDetail::getProductName);
            cell.setAlignment(Pos.CENTER_LEFT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        quantity.setRowCellFactory(purchaseDetail -> {
            var cell = new MFXTableRowCell<>(PurchaseDetail::getQuantity);
            cell.setAlignment(Pos.CENTER_RIGHT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        price.setRowCellFactory(purchaseDetail -> {
            var cell = new MFXTableRowCell<>(PurchaseDetail::getCost);
            cell.setAlignment(Pos.CENTER_RIGHT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        totalPrice.setRowCellFactory(
                purchaseDetail -> {
                    var cell = new MFXTableRowCell<>(PurchaseDetail::getSubTotalCost);
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
        if (getPurchaseDetails().isEmpty()) {
            getPurchaseDetails()
                    .addListener(
                            (ListChangeListener<PurchaseDetail>)
                                    change -> itemsTable.setItems(getPurchaseDetails()));
        } else {
            itemsTable.itemsProperty().bindBidirectional(purchaseDetailsProperty());
        }
    }

    private void styleTable() {
        itemsTable.setPrefSize(1000, 1000);
        itemsTable.features().enableBounceEffect();
        itemsTable.features().enableSmoothScrolling(0.5);
        itemsTable.setFooterVisible(false);
    }

    public void init(PurchaseMaster purchase) {
        purchaseDetailsList.clear();
        purchaseDate.setText(purchase.getLocaleDate());
        purchaseRef.setText(purchase.getRef());
        supplierName.setText(purchase.getSupplier().getName());
        supplierNumber.setText(purchase.getSupplier().getPhone());
        supplierEmail.setText(purchase.getSupplier().getEmail());
        purchaseDetailsList.addAll(purchase.getPurchaseDetails());
        grandTotal.setText(String.valueOf(purchase.getSubTotal()));
        discount.setText(String.valueOf(purchase.getDiscount()));
        tax.setText(String.valueOf(purchase.getTaxRate()));
        netCost.setText(String.valueOf(purchase.getTotal()));
        paidAmount.setText(String.valueOf(purchase.getAmountPaid()));
        purchaseNote.setText(purchase.getNotes());
//        doneBy.setText(purchase.doneBy());
    }
}
