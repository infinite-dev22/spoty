package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.network_bridge.dtos.requisitions.RequisitionDetail;
import inc.nomard.spoty.network_bridge.dtos.requisitions.RequisitionMaster;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.extern.java.Log;

import java.net.URL;
import java.util.ResourceBundle;

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
    public TableView<RequisitionDetail> itemsTable;
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
        TableColumn<RequisitionDetail, String> product = new TableColumn<>("Name");
        TableColumn<RequisitionDetail, String> quantity = new TableColumn<>("Qnty");

        // Set table column width.
        product.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.5));
        quantity.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.5));

        // Set table filter.
        itemsTable
                .getColumns()
                .addAll(product, quantity);

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
