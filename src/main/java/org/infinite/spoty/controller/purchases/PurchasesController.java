package org.infinite.spoty.controller.purchases;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.infinite.spoty.model.Purchase;
import org.infinite.spoty.model.Purchase;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.purchaseSampleData;

public class PurchasesController implements Initializable {
    
    private MFXTableView<Purchase> purchaseTable;
    
    @FXML
    public BorderPane purchasesContentPane;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> Platform.runLater(() -> {
            purchasesContentPane.setCenter(getPurchaseTable());
            setupTable();
        })).start();
    }

    private void setupTable() {
        MFXTableColumn<Purchase> purchaseDate = new MFXTableColumn<>("Date", true, Comparator.comparing(Purchase::getPurchaseDate));
        MFXTableColumn<Purchase> purchaseReference = new MFXTableColumn<>("Reference", true, Comparator.comparing(Purchase::getPurchaseReference));
        MFXTableColumn<Purchase> purchaseSupplier = new MFXTableColumn<>("Supplier", true, Comparator.comparing(Purchase::getPurchaseSupplier));
        MFXTableColumn<Purchase> purchaseWarehouse = new MFXTableColumn<>("Warehouse", true, Comparator.comparing(Purchase::getPurchaseWarehouse));
        MFXTableColumn<Purchase> purchaseStatus = new MFXTableColumn<>("Purchase Status", true, Comparator.comparing(Purchase::getPurchaseStatus));
        MFXTableColumn<Purchase> purchaseGrandTotal = new MFXTableColumn<>("Grand Total", true, Comparator.comparing(Purchase::getPurchaseGrandTotal));
        MFXTableColumn<Purchase> purchaseAmountPaid = new MFXTableColumn<>("Amount Paid", true, Comparator.comparing(Purchase::getPurchaseAmountPaid));
        MFXTableColumn<Purchase> purchaseAmountDue = new MFXTableColumn<>("Amount Due", true, Comparator.comparing(Purchase::getPurchaseAmountDue));
        MFXTableColumn<Purchase> purchasePaymentStatus = new MFXTableColumn<>("Payment Status", true, Comparator.comparing(Purchase::getPurchasePaymentStatus));

        purchaseDate.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchaseDate));
        purchaseReference.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchaseReference));
        purchaseSupplier.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchaseSupplier));
        purchaseWarehouse.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchaseWarehouse));
        purchaseStatus.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchaseStatus));
        purchaseGrandTotal.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchaseGrandTotal));
        purchaseAmountPaid.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchaseAmountPaid));
        purchaseAmountDue.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchaseAmountDue));
        purchasePaymentStatus.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchasePaymentStatus));

        purchaseTable.getTableColumns().addAll(purchaseDate, purchaseReference, purchaseSupplier, purchaseWarehouse, purchaseStatus, purchaseGrandTotal, purchaseAmountPaid, purchaseAmountDue, purchasePaymentStatus);
        purchaseTable.getFilters().addAll(
                new StringFilter<>("Reference", Purchase::getPurchaseReference),
                new StringFilter<>("Supplier", Purchase::getPurchaseSupplier),
                new StringFilter<>("Warehouse", Purchase::getPurchaseWarehouse),
                new StringFilter<>("Purchase Status", Purchase::getPurchaseStatus),
                new DoubleFilter<>("Grand Total", Purchase::getPurchaseGrandTotal),
                new DoubleFilter<>("Amount Paid", Purchase::getPurchaseAmountPaid),
                new DoubleFilter<>("Amount Due", Purchase::getPurchaseAmountDue),
                new StringFilter<>("Payment Status", Purchase::getPurchasePaymentStatus)
        );

        purchaseTable.setItems(purchaseSampleData());
    }

    private MFXTableView<Purchase> getPurchaseTable() {
        purchaseTable = new MFXTableView<>();
        purchaseTable.setPrefSize(1200, 1000);
        purchaseTable.features().enableBounceEffect();
        purchaseTable.autosizeColumnsOnInitialization();
        purchaseTable.features().enableSmoothScrolling(0.5);
        return purchaseTable;
    }
}
