package org.infinite.spoty.controller.returns.sales;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.infinite.spoty.model.SaleReturn;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.saleReturnSampleData;
import static org.infinite.spoty.data.SampleData.saleSampleData;

public class SaleReturnsController implements Initializable {
    
    private MFXTableView<SaleReturn> saleReturnsTable;
    
    @FXML
    public BorderPane saleReturnsContentPane;

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
            saleReturnsContentPane.setCenter(getSaleReturnTable());
            setupTable();
        })).start();
    }

    private void setupTable() {
        MFXTableColumn<SaleReturn> saleDate = new MFXTableColumn<>("Date", true, Comparator.comparing(SaleReturn::getSaleReturnDate));
        MFXTableColumn<SaleReturn> saleReference = new MFXTableColumn<>("Ref No.", true, Comparator.comparing(SaleReturn::getSaleReturnReference));
        MFXTableColumn<SaleReturn> saleCustomer = new MFXTableColumn<>("Customer", true, Comparator.comparing(SaleReturn::getSaleReturnCustomer));
        MFXTableColumn<SaleReturn> saleWarehouse = new MFXTableColumn<>("Warehouse", true, Comparator.comparing(SaleReturn::getSaleReturnWarehouse));
        MFXTableColumn<SaleReturn> saleRef = new MFXTableColumn<>("Sale Ref", true, Comparator.comparing(SaleReturn::getSaleReturnStatus));
        MFXTableColumn<SaleReturn> saleStatus = new MFXTableColumn<>("Status", true, Comparator.comparing(SaleReturn::getSaleReturnStatus));
        MFXTableColumn<SaleReturn> saleGrandTotal = new MFXTableColumn<>("Total", true, Comparator.comparing(SaleReturn::getSaleReturnGrandTotal));
        MFXTableColumn<SaleReturn> saleAmountPaid = new MFXTableColumn<>("Paid", true, Comparator.comparing(SaleReturn::getSaleReturnAmountPaid));
        MFXTableColumn<SaleReturn> saleAmountDue = new MFXTableColumn<>("Due", true, Comparator.comparing(SaleReturn::getSaleReturnAmountDue));
        MFXTableColumn<SaleReturn> salePaymentStatus = new MFXTableColumn<>("Payment Status", true, Comparator.comparing(SaleReturn::getSaleReturnPaymentStatus));

        saleDate.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleReturn::getSaleReturnDate));
        saleReference.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleReturn::getSaleReturnReference));
        saleCustomer.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleReturn::getSaleReturnCustomer));
        saleWarehouse.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleReturn::getSaleReturnWarehouse));
        saleRef.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleReturn::getSaleReturnStatus));
        saleStatus.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleReturn::getSaleReturnStatus));
        saleGrandTotal.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleReturn::getSaleReturnGrandTotal));
        saleAmountPaid.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleReturn::getSaleReturnAmountPaid));
        saleAmountDue.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleReturn::getSaleReturnAmountDue));
        salePaymentStatus.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleReturn::getSaleReturnPaymentStatus));

        saleReturnsTable.getTableColumns().addAll(saleDate, saleReference, saleCustomer, saleWarehouse, saleRef, saleStatus, saleGrandTotal, saleAmountPaid, saleAmountDue, salePaymentStatus);
        saleReturnsTable.getFilters().addAll(
                new StringFilter<>("Reference", SaleReturn::getSaleReturnReference),
                new StringFilter<>("Customer", SaleReturn::getSaleReturnCustomer),
                new StringFilter<>("Warehouse", SaleReturn::getSaleReturnWarehouse),
                new StringFilter<>("Sale Ref", SaleReturn::getSaleReturnStatus),
                new StringFilter<>("Status", SaleReturn::getSaleReturnStatus),
                new DoubleFilter<>("Total", SaleReturn::getSaleReturnGrandTotal),
                new DoubleFilter<>("Paid", SaleReturn::getSaleReturnAmountPaid),
                new DoubleFilter<>("Due", SaleReturn::getSaleReturnAmountDue),
                new StringFilter<>("Payment Status", SaleReturn::getSaleReturnPaymentStatus)
        );

        saleReturnsTable.setItems(saleReturnSampleData());
    }

    private MFXTableView<SaleReturn> getSaleReturnTable() {
        saleReturnsTable = new MFXTableView<>();
        saleReturnsTable.setPrefSize(1200, 1000);
        saleReturnsTable.features().enableBounceEffect();
        saleReturnsTable.autosizeColumnsOnInitialization();
        saleReturnsTable.features().enableSmoothScrolling(0.5);
        return saleReturnsTable;
    }
}
