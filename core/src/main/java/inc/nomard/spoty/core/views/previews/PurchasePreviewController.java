package inc.nomard.spoty.core.views.previews;

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
        MFXTableColumn<PurchaseDetail> product =
                new MFXTableColumn<>("Name", false, Comparator.comparing(PurchaseDetail::getProductName));
        MFXTableColumn<PurchaseDetail> quantity =
                new MFXTableColumn<>("Qty", false, Comparator.comparing(PurchaseDetail::getQuantity));
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
        totalPrice.setRowCellFactory(
                purchaseDetail -> {
                    var cell = new MFXTableRowCell<>(PurchaseDetail::getSubTotalCost);
                    cell.setAlignment(Pos.CENTER_RIGHT);
                    cell.getStyleClass().add("table-cell-border");
                    return cell;
                });

        // Set table column width.
        product.prefWidthProperty().bind(itemsTable.widthProperty().multiply(1));
        quantity.prefWidthProperty().bind(itemsTable.widthProperty().multiply(1));
        totalPrice.prefWidthProperty().bind(itemsTable.widthProperty().multiply(1));

        // Set table filter.
        itemsTable
                .getTableColumns()
                .addAll(product, quantity, totalPrice);

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
        tax.setText(String.valueOf(purchase.getTax().getPercentage()));
        netCost.setText(String.valueOf(purchase.getTotal()));
//        paidAmount.setText(String.valueOf(purchase.getAmountPaid()));
        purchaseNote.setText(purchase.getNotes());
//        doneBy.setText(purchase.doneBy());
    }
}
