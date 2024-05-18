package inc.nomard.spoty.core.views.previews.purchases;

import inc.nomard.spoty.network_bridge.dtos.purchases.*;
import inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns.*;
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
    public MFXTableView<PurchaseDetail> itemsTable;
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
        MFXTableColumn<PurchaseDetail> cost =
                new MFXTableColumn<>("Unit Cost", false, Comparator.comparing(PurchaseDetail::getCost));
        MFXTableColumn<PurchaseDetail> subTotalCost =
                new MFXTableColumn<>(
                        "Total", false, Comparator.comparing(PurchaseDetail::getSubTotalCost));

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
        cost.setRowCellFactory(purchaseDetail -> {
            var cell = new MFXTableRowCell<>(PurchaseDetail::getCost);
            cell.setAlignment(Pos.CENTER_RIGHT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        subTotalCost.setRowCellFactory(
                purchaseDetail -> {
                    var cell = new MFXTableRowCell<>(PurchaseDetail::getSubTotalCost);
                    cell.setAlignment(Pos.CENTER_RIGHT);
                    cell.getStyleClass().add("table-cell-border");
                    return cell;
                });

        // Set table column width.
        product.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.25));
        quantity.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.25));
        cost.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.25));
        subTotalCost.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.25));

        // Set table filter.
        itemsTable
                .getTableColumns()
                .addAll(product, quantity, cost, subTotalCost);

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

    public void init(PurchaseReturnMaster purchaseMaster) {
        purchaseDetailsList.clear();
        purchaseDate.setText(purchaseMaster.getLocaleDate());
        purchaseRef.setText(purchaseMaster.getRef());
        supplierName.setText(purchaseMaster.getSupplier().getName());
        supplierNumber.setText(purchaseMaster.getSupplier().getPhone());
        supplierEmail.setText(purchaseMaster.getSupplier().getEmail());
        purchaseDetailsList.addAll(purchaseMaster.getPurchaseDetails());
        subTotal.setText(String.valueOf(purchaseMaster.getSubTotal()));
        discount.setText(String.valueOf(purchaseMaster.getDiscount()));
        tax.setText(String.valueOf(purchaseMaster.getTaxRate()));
        shipping.setText(String.valueOf(purchaseMaster.getShippingFee()));
        netCost.setText(String.valueOf(purchaseMaster.getTotal()));
        paidAmount.setText(String.valueOf(purchaseMaster.getAmountPaid()));
        changeDue.setText(String.valueOf(purchaseMaster.getChangeAmount()));
        balance.setText(String.valueOf(purchaseMaster.getBalanceAmount()));
        purchaseNote.setText(purchaseMaster.getNotes());
//        doneBy.setText(purchase.doneBy());
    }
}
