package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.network_bridge.dtos.sales.*;
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
    public TableView<SaleDetail> itemsTable;
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
        TableColumn<SaleDetail, String> product = new TableColumn<>("Name");
        TableColumn<SaleDetail, String> quantity = new TableColumn<>("Qnty");
        TableColumn<SaleDetail, String> price = new TableColumn<>("Price");
        TableColumn<SaleDetail, String> totalPrice = new TableColumn<>("Total Price");

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
        itemsTable.setItems(getSaleDetails());
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
        tax.setText(String.valueOf(sale.getTax()));
        shipping.setText(String.valueOf(sale.getShippingFee()));
        netCost.setText(String.valueOf(sale.getTotal()));
        paidAmount.setText(String.valueOf(sale.getAmountPaid()));
        changeDue.setText(String.valueOf(sale.getChangeAmount()));
    }
}
