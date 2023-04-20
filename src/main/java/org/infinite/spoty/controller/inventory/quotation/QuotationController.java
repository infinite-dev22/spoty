package org.infinite.spoty.controller.inventory.quotation;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.infinite.spoty.model.Adjustment;
import org.infinite.spoty.model.Quotation;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.quotationSampleData;

public class QuotationController implements Initializable {

    private MFXTableView<Quotation> quotationsTable;
    
    @FXML
    public BorderPane quotationContentPane;

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
            quotationContentPane.setCenter(getQuotationTable());
            setupTable();
        })).start();
    }

    private void setupTable() {
        MFXTableColumn<Quotation> quotationDate = new MFXTableColumn<>("Date", true, Comparator.comparing(Quotation::getQuotationDate));
        MFXTableColumn<Quotation> quotationReference = new MFXTableColumn<>("Reference", true, Comparator.comparing(Quotation::getQuotationReference));
        MFXTableColumn<Quotation> quotationCustomer = new MFXTableColumn<>("Customer", true, Comparator.comparing(Quotation::getQuotationCustomer));
        MFXTableColumn<Quotation> quotationWarehouse = new MFXTableColumn<>("Warehouse", true, Comparator.comparing(Quotation::getQuotationWarehouse));
        MFXTableColumn<Quotation> quotationStatus = new MFXTableColumn<>("Status", true, Comparator.comparing(Quotation::getQuotationStatus));
        MFXTableColumn<Quotation> quotationGrandTotal = new MFXTableColumn<>("Grand Total", true, Comparator.comparing(Quotation::getQuotationGrandTotal));

        quotationDate.setRowCellFactory(quotation -> new MFXTableRowCell<>(Quotation::getQuotationDate));
        quotationReference.setRowCellFactory(quotation -> new MFXTableRowCell<>(Quotation::getQuotationReference));
        quotationCustomer.setRowCellFactory(quotation -> new MFXTableRowCell<>(Quotation::getQuotationCustomer));
        quotationWarehouse.setRowCellFactory(quotation -> new MFXTableRowCell<>(Quotation::getQuotationWarehouse));
        quotationStatus.setRowCellFactory(quotation -> new MFXTableRowCell<>(Quotation::getQuotationStatus));
        quotationGrandTotal.setRowCellFactory(quotation -> new MFXTableRowCell<>(Quotation::getQuotationGrandTotal));

        quotationsTable.getTableColumns().addAll(quotationDate, quotationReference, quotationCustomer, quotationWarehouse, quotationStatus, quotationGrandTotal);
        quotationsTable.getFilters().addAll(
                new StringFilter<>("Reference", Quotation::getQuotationReference),
                new StringFilter<>("Customer", Quotation::getQuotationCustomer),
                new StringFilter<>("Warehouse", Quotation::getQuotationWarehouse),
                new StringFilter<>("Status", Quotation::getQuotationStatus),
                new DoubleFilter<>("Grand Total", Quotation::getQuotationGrandTotal)
        );

        quotationsTable.setItems(quotationSampleData());
    }

    private MFXTableView<Quotation> getQuotationTable() {
        quotationsTable = new MFXTableView<>();
        quotationsTable.setPrefSize(1000, 1000);
        quotationsTable.features().enableBounceEffect();
        quotationsTable.autosizeColumnsOnInitialization();
        quotationsTable.features().enableSmoothScrolling(0.5);
        return quotationsTable;
    }
}
