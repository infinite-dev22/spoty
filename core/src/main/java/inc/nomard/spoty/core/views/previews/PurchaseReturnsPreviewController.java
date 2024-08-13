package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.network_bridge.dtos.purchases.*;
import inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns.*;
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
public class PurchaseReturnsPreviewController implements Initializable {
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
    public TableView<PurchaseDetail> itemsTable;
    @FXML
    public Label grandTotal;
    @FXML
    public Label discount;
    @FXML
    public Label tax;
    @FXML
    public Label netCost;
    //    @FXML
//    public Label paidAmount;
//    @FXML
//    public Label changeDue;
//    @FXML
//    public Label balance;
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
        TableColumn<PurchaseDetail, String> product = new TableColumn<>("Product");
        TableColumn<PurchaseDetail, String> totalPrice = new TableColumn<>("Unit Cost");
        TableColumn<PurchaseDetail, String> quantity = new TableColumn<>("Quantity");
        TableColumn<PurchaseDetail, String> discount = new TableColumn<>("Discount");
        TableColumn<PurchaseDetail, String> tax = new TableColumn<>("Tax");
        TableColumn<PurchaseDetail, String> subTotal = new TableColumn<>("Sub Total");

        // Set table column width.
        product.prefWidthProperty().bind(itemsTable.widthProperty().multiply(1));
        totalPrice.prefWidthProperty().bind(itemsTable.widthProperty().multiply(1));
        quantity.prefWidthProperty().bind(itemsTable.widthProperty().multiply(1));
        discount.prefWidthProperty().bind(itemsTable.widthProperty().multiply(1));
        tax.prefWidthProperty().bind(itemsTable.widthProperty().multiply(1));
        subTotal.prefWidthProperty().bind(itemsTable.widthProperty().multiply(1));

        var columnList = new LinkedList<>(Stream.of(product, totalPrice, quantity, discount, tax, subTotal).toList());
        itemsTable
                .getColumns()
                .addAll(columnList);

        // Populate table.
        itemsTable.setItems(getPurchaseDetails());
    }

    public void init(PurchaseReturnMaster purchase) {
        purchaseDetailsList.clear();
        purchaseDate.setText(purchase.getLocaleDate());
        purchaseRef.setText(purchase.getRef());
        supplierName.setText(purchase.getSupplier().getName());
        supplierNumber.setText(purchase.getSupplier().getPhone());
        supplierEmail.setText(purchase.getSupplier().getEmail());
        purchaseDetailsList.addAll(purchase.getPurchaseReturnDetails());
        grandTotal.setText(String.valueOf(purchase.getSubTotal()));
        discount.setText(String.valueOf(purchase.getDiscount()));
        tax.setText(String.valueOf(purchase.getTax().getPercentage()));
        netCost.setText(String.valueOf(purchase.getTotal()));
//        paidAmount.setText(String.valueOf(purchase.getAmountPaid()));
        purchaseNote.setText(purchase.getNotes());
//        doneBy.setText(purchase.doneBy());
    }
}
