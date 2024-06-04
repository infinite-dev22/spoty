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

import inc.nomard.spoty.network_bridge.dtos.sales.*;
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
public class SalePreviewController implements Initializable {
    static final ObservableList<SaleDetail> saleDetailsList = FXCollections.observableArrayList();
    private static final ListProperty<SaleDetail> saleDetails =
            new SimpleListProperty<>(saleDetailsList);
    @FXML
    public Label orderDate;
    @FXML
    public Label orderRef;
    @FXML
    public Label customerName;
    @FXML
    public Label customerNumber;
    @FXML
    public Label customerEmail;
    @FXML
    public MFXTableView<SaleDetail> itemsTable;
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
    public Label userName;

    public static ObservableList<SaleDetail> getSaleDetails() {
        return saleDetails.get();
    }

    public static ListProperty<SaleDetail> saleDetailsProperty() {
        return saleDetails;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        // Set table column titles.
        MFXTableColumn<SaleDetail> product =
                new MFXTableColumn<>("Name", false, Comparator.comparing(SaleDetail::getProductName));
        MFXTableColumn<SaleDetail> quantity =
                new MFXTableColumn<>("Qnty", false, Comparator.comparing(SaleDetail::getQuantity));
        MFXTableColumn<SaleDetail> price =
                new MFXTableColumn<>("Price", false, Comparator.comparing(SaleDetail::getPrice));
        MFXTableColumn<SaleDetail> totalPrice =
                new MFXTableColumn<>(
                        "Total Price", false, Comparator.comparing(SaleDetail::getSubTotalPrice));

        // Set table column data.
        product.setRowCellFactory(saleDetail -> {
            var cell = new MFXTableRowCell<>(SaleDetail::getProductName);
            cell.setAlignment(Pos.CENTER_LEFT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        quantity.setRowCellFactory(saleDetail -> {
            var cell = new MFXTableRowCell<>(SaleDetail::getQuantity);
            cell.setAlignment(Pos.CENTER_RIGHT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        price.setRowCellFactory(saleDetail -> {
            var cell = new MFXTableRowCell<>(SaleDetail::getPrice);
            cell.setAlignment(Pos.CENTER_RIGHT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        totalPrice.setRowCellFactory(
                saleDetail -> {
                    var cell = new MFXTableRowCell<>(SaleDetail::getSubTotalPrice);
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
        if (getSaleDetails().isEmpty()) {
            getSaleDetails()
                    .addListener(
                            (ListChangeListener<SaleDetail>)
                                    change -> itemsTable.setItems(getSaleDetails()));
        } else {
            itemsTable.itemsProperty().bindBidirectional(saleDetailsProperty());
        }
    }

    private void styleTable() {
        itemsTable.setPrefSize(1000, 1000);
        itemsTable.features().enableBounceEffect();
        itemsTable.features().enableSmoothScrolling(0.5);
        itemsTable.setFooterVisible(false);
    }

    public void init(SaleMaster sale) {
        saleDetailsList.clear();
        orderDate.setText(sale.getLocaleDate());
        orderRef.setText(sale.getRef());
        customerName.setText(sale.getCustomer().getName());
        customerNumber.setText(sale.getCustomer().getPhone());
        customerEmail.setText(sale.getCustomer().getEmail());
        saleDetailsList.addAll(sale.getSaleDetails());
        subTotal.setText(String.valueOf(sale.getSubTotal()));
        discount.setText(String.valueOf(sale.getDiscount()));
        tax.setText(String.valueOf(sale.getNetTax()));
        shipping.setText(String.valueOf(sale.getShippingFee()));
        netCost.setText(String.valueOf(sale.getTotal()));
        paidAmount.setText(String.valueOf(sale.getAmountPaid()));
        changeDue.setText(String.valueOf(sale.getChangeAmount()));
    }
}
