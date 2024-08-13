package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.network_bridge.dtos.returns.sale_returns.*;
import java.net.*;
import java.util.*;
import java.util.stream.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import lombok.extern.java.*;

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
    public TableView<SaleReturnDetail> itemsTable;
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
        TableColumn<SaleReturnDetail, String> product = new TableColumn<>("Name");
        TableColumn<SaleReturnDetail, String> quantity = new TableColumn<>("Qnty");
        TableColumn<SaleReturnDetail, String> price = new TableColumn<>("Price");
        TableColumn<SaleReturnDetail, String> totalPrice = new TableColumn<>("Total Price");

        // Set table column width.
        product.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.25));
        quantity.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.25));
        price.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.25));
        totalPrice.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.25));

        var columnList = new LinkedList<>(Stream.of(product, quantity, price, totalPrice).toList());
        itemsTable
                .getColumns()
                .addAll(columnList);

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

    public void init(SaleReturnMaster saleReturn) {
        saleReturnDetailsList.clear();
        orderDate.setText(saleReturn.getLocaleDate());
        orderRef.setText(saleReturn.getRef());
        customerName.setText(saleReturn.getCustomer().getName());
        customerNumber.setText(saleReturn.getCustomer().getPhone());
        customerEmail.setText(saleReturn.getCustomer().getEmail());
//        saleReturnDetailsList.addAll(saleReturn.getSaleReturnDetails());
        subTotal.setText(String.valueOf(saleReturn.getSubTotal()));
        discount.setText(String.valueOf(saleReturn.getDiscount()));
//        tax.setText(String.valueOf(saleReturn.getNetTax()));
        shipping.setText(String.valueOf(saleReturn.getShippingFee()));
        netCost.setText(String.valueOf(saleReturn.getTotal()));
        paidAmount.setText(String.valueOf(saleReturn.getAmountPaid()));
        changeDue.setText(String.valueOf(saleReturn.getChangeAmount()));
//        balance.setText(String.valueOf(saleReturn.getBalanceAmount()));
    }
}
