package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.network_bridge.dtos.quotations.*;
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
    public MFXTableView<QuotationDetail> itemsTable;
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
        MFXTableColumn<QuotationDetail> product =
                new MFXTableColumn<>("Name", false, Comparator.comparing(QuotationDetail::getProductName));
        MFXTableColumn<QuotationDetail> quantity =
                new MFXTableColumn<>("Qnty", false, Comparator.comparing(QuotationDetail::getQuantity));
        MFXTableColumn<QuotationDetail> price =
                new MFXTableColumn<>("Price", false, Comparator.comparing(QuotationDetail::getProductPrice));
        MFXTableColumn<QuotationDetail> totalPrice =
                new MFXTableColumn<>(
                        "Sub Total Price", false, Comparator.comparing(QuotationDetail::getSubTotal));

        // Set table column data.
        product.setRowCellFactory(quotationDetail -> {
            var cell = new MFXTableRowCell<>(QuotationDetail::getProductName);
            cell.setAlignment(Pos.CENTER_LEFT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        quantity.setRowCellFactory(quotationDetail -> {
            var cell = new MFXTableRowCell<>(QuotationDetail::getQuantity);
            cell.setAlignment(Pos.CENTER_RIGHT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        price.setRowCellFactory(quotationDetail -> {
            var cell = new MFXTableRowCell<>(QuotationDetail::getProductPrice);
            cell.setAlignment(Pos.CENTER_RIGHT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        totalPrice.setRowCellFactory(
                quotationDetail -> {
                    var cell = new MFXTableRowCell<>(QuotationDetail::getSubTotal);
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
        if (getQuotationDetails().isEmpty()) {
            getQuotationDetails()
                    .addListener(
                            (ListChangeListener<QuotationDetail>)
                                    change -> itemsTable.setItems(getQuotationDetails()));
        } else {
            itemsTable.itemsProperty().bindBidirectional(quotationDetailsProperty());
        }
    }

    private void styleTable() {
        itemsTable.setPrefSize(1000, 1000);
        itemsTable.setFooterVisible(false);
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
