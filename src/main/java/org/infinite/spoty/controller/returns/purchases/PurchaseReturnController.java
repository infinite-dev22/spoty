package org.infinite.spoty.controller.returns.purchases;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.infinite.spoty.model.PurchaseReturn;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.purchaseReturnSampleData;

public class PurchaseReturnController implements Initializable {

    public MFXTableView<PurchaseReturn> purchaseReturnTable;

    @FXML
    public BorderPane purchaseReturnsContentPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> Platform.runLater(() -> {
            purchaseReturnsContentPane.setCenter(getPurchaseReturnTable());
            setupTable();
        })).start();
    }

    private void setupTable() {
        MFXTableColumn<PurchaseReturn> purchaseReturnDate = new MFXTableColumn<>("Date", true, Comparator.comparing(PurchaseReturn::getPurchaseReturnDate));
        MFXTableColumn<PurchaseReturn> purchaseReturnReference = new MFXTableColumn<>("Ref No.", true, Comparator.comparing(PurchaseReturn::getPurchaseReturnReference));
        MFXTableColumn<PurchaseReturn> purchaseReturnSupplier = new MFXTableColumn<>("Supplier", true, Comparator.comparing(PurchaseReturn::getPurchaseReturnSupplier));
        MFXTableColumn<PurchaseReturn> purchaseReturnWarehouse = new MFXTableColumn<>("Warehouse", true, Comparator.comparing(PurchaseReturn::getPurchaseReturnWarehouse));
        MFXTableColumn<PurchaseReturn> purchaseReturnReturnRef = new MFXTableColumn<>("Purchase Ref", true, Comparator.comparing(PurchaseReturn::getPurchaseReturnRef));
        MFXTableColumn<PurchaseReturn> purchaseReturnStatus = new MFXTableColumn<>("Status", true, Comparator.comparing(PurchaseReturn::getPurchaseReturnStatus));
        MFXTableColumn<PurchaseReturn> purchaseReturnGrandTotal = new MFXTableColumn<>("Total", true, Comparator.comparing(PurchaseReturn::getPurchaseReturnGrandTotal));
        MFXTableColumn<PurchaseReturn> purchaseReturnAmountPaid = new MFXTableColumn<>("Paid", true, Comparator.comparing(PurchaseReturn::getPurchaseReturnAmountPaid));
        MFXTableColumn<PurchaseReturn> purchaseReturnAmountDue = new MFXTableColumn<>("Due", true, Comparator.comparing(PurchaseReturn::getPurchaseReturnAmountDue));
        MFXTableColumn<PurchaseReturn> purchaseReturnPaymentStatus = new MFXTableColumn<>("Payment Status", true, Comparator.comparing(PurchaseReturn::getPurchaseReturnPaymentStatus));

        purchaseReturnDate.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnDate));
        purchaseReturnReference.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnReference));
        purchaseReturnSupplier.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnSupplier));
        purchaseReturnWarehouse.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnWarehouse));
        purchaseReturnReturnRef.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnRef));
        purchaseReturnStatus.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnStatus));
        purchaseReturnGrandTotal.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnGrandTotal));
        purchaseReturnAmountPaid.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnAmountPaid));
        purchaseReturnAmountDue.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnAmountDue));
        purchaseReturnPaymentStatus.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnPaymentStatus));

        purchaseReturnTable.getTableColumns().addAll(purchaseReturnDate, purchaseReturnReference, purchaseReturnSupplier, purchaseReturnWarehouse, purchaseReturnReturnRef, purchaseReturnStatus, purchaseReturnGrandTotal, purchaseReturnAmountPaid, purchaseReturnAmountDue, purchaseReturnPaymentStatus);
        purchaseReturnTable.getFilters().addAll(new StringFilter<>("Ref No.", PurchaseReturn::getPurchaseReturnReference), new StringFilter<>("Supplier", PurchaseReturn::getPurchaseReturnSupplier), new StringFilter<>("Warehouse", PurchaseReturn::getPurchaseReturnWarehouse), new StringFilter<>("Purchase Ref", PurchaseReturn::getPurchaseReturnRef), new StringFilter<>("Status", PurchaseReturn::getPurchaseReturnStatus), new DoubleFilter<>("Total", PurchaseReturn::getPurchaseReturnGrandTotal), new DoubleFilter<>("Paid", PurchaseReturn::getPurchaseReturnAmountPaid), new DoubleFilter<>("Due", PurchaseReturn::getPurchaseReturnAmountDue), new StringFilter<>("Payment Status", PurchaseReturn::getPurchaseReturnPaymentStatus));

        purchaseReturnTable.setItems(purchaseReturnSampleData());
    }

    private MFXTableView<PurchaseReturn> getPurchaseReturnTable() {
        purchaseReturnTable = new MFXTableView<>();
        purchaseReturnTable.setPrefSize(1200, 1000);
        purchaseReturnTable.features().enableBounceEffect();
        purchaseReturnTable.autosizeColumnsOnInitialization();
        purchaseReturnTable.features().enableSmoothScrolling(0.5);
        return purchaseReturnTable;
    }
}
