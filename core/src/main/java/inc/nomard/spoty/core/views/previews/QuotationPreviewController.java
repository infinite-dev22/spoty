package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.network_bridge.dtos.quotations.*;
import io.github.palexdev.materialfx.controls.*;
import java.net.*;
import java.util.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import lombok.extern.java.*;

@Log
public class QuotationPreviewController implements Initializable {
    static final ObservableList<QuotationDetail> quotationDetailsList = FXCollections.observableArrayList();
    private static final ListProperty<QuotationDetail> quotationDetails =
            new SimpleListProperty<>(quotationDetailsList);
    @FXML
    public Label quotationDate;
    @FXML
    public Label quotationRef;
    @FXML
    public Label customerName;
    @FXML
    public Label customerNumber;
    @FXML
    public Label customerEmail;
    @FXML
    public TableView<QuotationDetail> itemsTable;
    @FXML
    public Label discount;
    @FXML
    public Label tax;
    @FXML
    public Label shipping;
    @FXML
    public Label netPrice;
    @FXML
    public Label doneBy;

    public static ObservableList<QuotationDetail> getQuotationDetails() {
        return quotationDetails.get();
    }

    public static ListProperty<QuotationDetail> quotationDetailsProperty() {
        return quotationDetails;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        // Set table column titles.
        TableColumn<QuotationDetail, String> product = new TableColumn<>("Name");
        TableColumn<QuotationDetail, String> quantity = new TableColumn<>("Qnty");
        TableColumn<QuotationDetail, String> price = new TableColumn<>("Price");
        TableColumn<QuotationDetail, String> totalPrice = new TableColumn<>("Sub Total Price");

        // Set table column width.
        product.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.25));
        quantity.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.25));
        price.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.25));
        totalPrice.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.25));

        // Set table filter.
        itemsTable
                .getColumns()
                .addAll(product, quantity, price, totalPrice);

        // Populate table.
        if (getQuotationDetails().isEmpty()) {
            getQuotationDetails()
                    .addListener(
                            (ListChangeListener<QuotationDetail>)
                                    change -> itemsTable.setItems(getQuotationDetails()));
        } else {
            itemsTable.itemsProperty().bindBidirectional(quotationDetailsProperty());
        }
    }

    public void init(QuotationMaster quotation) {
        quotationDetailsList.clear();
        quotationRef.setText(quotation.getRef());
        customerName.setText(quotation.getCustomer().getName());
        customerNumber.setText(quotation.getCustomer().getPhone());
        customerEmail.setText(quotation.getCustomer().getEmail());
        quotationDetailsList.addAll(quotation.getQuotationDetails());
        discount.setText(String.valueOf(quotation.getDiscount()));
        tax.setText(String.valueOf(quotation.getNetTax()));
        shipping.setText(String.valueOf(quotation.getShippingFee()));
        netPrice.setText(String.valueOf(quotation.getTotal()));
    }
}
