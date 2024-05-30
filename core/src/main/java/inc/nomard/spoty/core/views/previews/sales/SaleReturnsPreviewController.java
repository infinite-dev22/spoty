package inc.nomard.spoty.core.views.previews.sales;

import inc.nomard.spoty.network_bridge.dtos.returns.sale_returns.SaleReturnDetail;
import inc.nomard.spoty.network_bridge.dtos.returns.sale_returns.SaleReturnMaster;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import lombok.extern.java.Log;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

@Log
public class SaleReturnsPreviewController implements Initializable {
    static final ObservableList<SaleReturnDetail> saleReturnDetailsList = FXCollections.observableArrayList();
    private static final ListProperty<SaleReturnDetail> saleReturnDetails =
            new SimpleListProperty<>(saleReturnDetailsList);
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
    public MFXTableView<SaleReturnDetail> itemsTable;
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

    public static ObservableList<SaleReturnDetail> getSaleReturnDetails() {
        return saleReturnDetails.get();
    }

    public static ListProperty<SaleReturnDetail> saleReturnDetailsProperty() {
        return saleReturnDetails;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        // Set table column titles.
        MFXTableColumn<SaleReturnDetail> product =
                new MFXTableColumn<>("Name", false, Comparator.comparing(SaleReturnDetail::getProductName));
        MFXTableColumn<SaleReturnDetail> quantity =
                new MFXTableColumn<>("Qnty", false, Comparator.comparing(SaleReturnDetail::getQuantity));
        MFXTableColumn<SaleReturnDetail> price =
                new MFXTableColumn<>("Price", false, Comparator.comparing(SaleReturnDetail::getPrice));
        MFXTableColumn<SaleReturnDetail> totalPrice =
                new MFXTableColumn<>(
                        "Total Price", false, Comparator.comparing(SaleReturnDetail::getSubTotalPrice));

        // Set table column data.
        product.setRowCellFactory(saleReturnDetail -> {
            var cell = new MFXTableRowCell<>(SaleReturnDetail::getProductName);
            cell.setAlignment(Pos.CENTER_LEFT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        quantity.setRowCellFactory(saleReturnDetail -> {
            var cell = new MFXTableRowCell<>(SaleReturnDetail::getQuantity);
            cell.setAlignment(Pos.CENTER_RIGHT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        price.setRowCellFactory(saleReturnDetail -> {
            var cell = new MFXTableRowCell<>(SaleReturnDetail::getPrice);
            cell.setAlignment(Pos.CENTER_RIGHT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        totalPrice.setRowCellFactory(
                saleReturnDetail -> {
                    var cell = new MFXTableRowCell<>(SaleReturnDetail::getSubTotalPrice);
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
        if (getSaleReturnDetails().isEmpty()) {
            getSaleReturnDetails()
                    .addListener(
                            (ListChangeListener<SaleReturnDetail>)
                                    change -> itemsTable.setItems(getSaleReturnDetails()));
        } else {
            itemsTable.itemsProperty().bindBidirectional(saleReturnDetailsProperty());
        }
    }

    private void styleTable() {
        itemsTable.setPrefSize(1000, 1000);
        itemsTable.features().enableBounceEffect();
        itemsTable.features().enableSmoothScrolling(0.5);
        itemsTable.setFooterVisible(false);
    }

    public void init(SaleReturnMaster saleReturn) {
        saleReturnDetailsList.clear();
        orderDate.setText(saleReturn.getLocaleDate());
        orderRef.setText(saleReturn.getRef());
        customerName.setText(saleReturn.getCustomer().getName());
        customerNumber.setText(saleReturn.getCustomer().getPhone());
        customerEmail.setText(saleReturn.getCustomer().getEmail());
        saleReturnDetailsList.addAll(saleReturn.getSaleReturnDetails());
        subTotal.setText(String.valueOf(saleReturn.getSubTotal()));
        discount.setText(String.valueOf(saleReturn.getDiscount()));
        tax.setText(String.valueOf(saleReturn.getNetTax()));
        shipping.setText(String.valueOf(saleReturn.getShippingFee()));
        netCost.setText(String.valueOf(saleReturn.getTotal()));
        paidAmount.setText(String.valueOf(saleReturn.getAmountPaid()));
        changeDue.setText(String.valueOf(saleReturn.getChangeAmount()));
        balance.setText(String.valueOf(saleReturn.getBalanceAmount()));
    }
}
