package org.infinite.spoty.controller.sales;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.infinite.spoty.model.Sale;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.saleSampleData;

public class SalesController implements Initializable {

    private MFXTableView<Sale> saleTable;
    
    @FXML
    public BorderPane salesContentPane;

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
            salesContentPane.setCenter(getSaleTable());
            setupTable();
        })).start();
    }

    private void setupTable() {
        MFXTableColumn<Sale> saleDate = new MFXTableColumn<>("Date", true, Comparator.comparing(Sale::getSaleDate));
        MFXTableColumn<Sale> saleReference = new MFXTableColumn<>("Reference", true, Comparator.comparing(Sale::getSaleReference));
        MFXTableColumn<Sale> saleAddedBy = new MFXTableColumn<>("Added By", true, Comparator.comparing(Sale::getSaleAddedBy));
        MFXTableColumn<Sale> saleCustomer = new MFXTableColumn<>("Customer", true, Comparator.comparing(Sale::getSaleCustomer));
        MFXTableColumn<Sale> saleWarehouse = new MFXTableColumn<>("Warehouse", true, Comparator.comparing(Sale::getSaleWarehouse));
        MFXTableColumn<Sale> saleStatus = new MFXTableColumn<>("Sale Status", true, Comparator.comparing(Sale::getSaleStatus));
        MFXTableColumn<Sale> saleGrandTotal = new MFXTableColumn<>("Grand Total", true, Comparator.comparing(Sale::getSaleGrandTotal));
        MFXTableColumn<Sale> saleAmountPaid = new MFXTableColumn<>("Amount Paid", true, Comparator.comparing(Sale::getSaleAmountPaid));
        MFXTableColumn<Sale> saleAmountDue = new MFXTableColumn<>("Amount Due", true, Comparator.comparing(Sale::getSaleAmountDue));
        MFXTableColumn<Sale> salePaymentStatus = new MFXTableColumn<>("Payment Status", true, Comparator.comparing(Sale::getSalePaymentStatus));

        saleDate.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleDate));
        saleReference.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleReference));
        saleAddedBy.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleAddedBy));
        saleCustomer.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleCustomer));
        saleWarehouse.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleWarehouse));
        saleStatus.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleStatus));
        saleGrandTotal.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleGrandTotal));
        saleAmountPaid.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleAmountPaid));
        saleAmountDue.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleAmountDue));
        salePaymentStatus.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSalePaymentStatus));

        saleTable.getTableColumns().addAll(saleDate, saleReference, saleAddedBy, saleCustomer, saleWarehouse, saleStatus, saleGrandTotal, saleAmountPaid, saleAmountDue, salePaymentStatus);
        saleTable.getFilters().addAll(
                new StringFilter<>("Reference", Sale::getSaleReference),
                new StringFilter<>("Added By", Sale::getSaleAddedBy),
                new StringFilter<>("Customer", Sale::getSaleCustomer),
                new StringFilter<>("Warehouse", Sale::getSaleWarehouse),
                new StringFilter<>("Sale Status", Sale::getSaleStatus),
                new DoubleFilter<>("Grand Total", Sale::getSaleGrandTotal),
                new DoubleFilter<>("Amount Paid", Sale::getSaleAmountPaid),
                new DoubleFilter<>("Amount Due", Sale::getSaleAmountDue),
                new StringFilter<>("Payment Status", Sale::getSalePaymentStatus)
        );

        saleTable.setItems(saleSampleData());
    }

    private MFXTableView<Sale> getSaleTable() {
        saleTable = new MFXTableView<>();
        saleTable.setPrefSize(1200, 1000);
        saleTable.features().enableBounceEffect();
        saleTable.autosizeColumnsOnInitialization();
        saleTable.features().enableSmoothScrolling(0.5);
        return saleTable;
    }
}
