package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.network_bridge.dtos.requisitions.*;
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
public class RequisitionPreviewController implements Initializable {
    static final ObservableList<RequisitionDetail> requisitionDetailsList = FXCollections.observableArrayList();
    private static final ListProperty<RequisitionDetail> requisitionDetails =
            new SimpleListProperty<>(requisitionDetailsList);
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
    public MFXTableView<RequisitionDetail> itemsTable;
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

    public static ObservableList<RequisitionDetail> getRequisitionDetails() {
        return requisitionDetails.get();
    }

    public static ListProperty<RequisitionDetail> requisitionDetailsProperty() {
        return requisitionDetails;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        // Set table column titles.
        MFXTableColumn<RequisitionDetail> product =
                new MFXTableColumn<>("Name", false, Comparator.comparing(RequisitionDetail::getProductName));
        MFXTableColumn<RequisitionDetail> quantity =
                new MFXTableColumn<>("Qnty", false, Comparator.comparing(RequisitionDetail::getQuantity));

        // Set table column data.
        product.setRowCellFactory(requisitionDetail -> {
            var cell = new MFXTableRowCell<>(RequisitionDetail::getProductName);
            cell.setAlignment(Pos.CENTER_LEFT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        quantity.setRowCellFactory(requisitionDetail -> {
            var cell = new MFXTableRowCell<>(RequisitionDetail::getQuantity);
            cell.setAlignment(Pos.CENTER_RIGHT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });

        // Set table column width.
        product.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.5));
        quantity.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.5));

        // Set table filter.
        itemsTable
                .getTableColumns()
                .addAll(product, quantity);

        styleTable();

        // Populate table.
        if (getRequisitionDetails().isEmpty()) {
            getRequisitionDetails()
                    .addListener(
                            (ListChangeListener<RequisitionDetail>)
                                    change -> itemsTable.setItems(getRequisitionDetails()));
        } else {
            itemsTable.itemsProperty().bindBidirectional(requisitionDetailsProperty());
        }
    }

    private void styleTable() {
        itemsTable.setPrefSize(1000, 1000);
        itemsTable.features().enableBounceEffect();
        itemsTable.features().enableSmoothScrolling(0.5);
        itemsTable.setFooterVisible(false);
    }

    public void init(RequisitionMaster requisition) {
//        requisitionDetailsList.clear();
//        purchaseDate.setText(requisition.getLocaleDate());
//        purchaseRef.setText(requisition.getRef());
//        supplierName.setText(requisition.getSupplier().getName());
//        supplierNumber.setText(requisition.getSupplier().getPhone());
//        supplierEmail.setText(requisition.getSupplier().getEmail());
//        requisitionDetailsList.addAll(requisition.getRequisitionDetails());
//        subTotal.setText(String.valueOf(requisition.getSubTotal()));
//        discount.setText(String.valueOf(requisition.getDiscount()));
//        tax.setText(String.valueOf(requisition.getTaxRate()));
//        shipping.setText(String.valueOf(requisition.getShippingFee()));
//        netCost.setText(String.valueOf(requisition.getTotal()));
//        paidAmount.setText(String.valueOf(requisition.getAmountPaid()));
//        changeDue.setText(String.valueOf(requisition.getChangeAmount()));
//        balance.setText(String.valueOf(requisition.getBalanceAmount()));
//        purchaseNote.setText(requisition.getNotes());
//        doneBy.setText(purchase.doneBy());
    }
}
