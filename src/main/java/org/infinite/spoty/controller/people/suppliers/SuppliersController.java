package org.infinite.spoty.controller.people.suppliers;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.infinite.spoty.model.Supplier;
import org.infinite.spoty.model.ExpenseCategory;
import org.infinite.spoty.model.Supplier;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.supplierSampleData;
import static org.infinite.spoty.data.SampleData.expenseCategorySampleData;

public class SuppliersController implements Initializable {
    private MFXTableView<Supplier> suppliersTable;

    @FXML
    public BorderPane suppliersContentPane;

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
            suppliersContentPane.setCenter(getSupplierTable());
            setupTable();
        })).start();
    }

    private void setupTable() {
        MFXTableColumn<Supplier> supplierCode = new MFXTableColumn<>("Code", true, Comparator.comparing(Supplier::getSupplierCode));
        MFXTableColumn<Supplier> supplierName = new MFXTableColumn<>("Name", true, Comparator.comparing(Supplier::getSupplierName));
        MFXTableColumn<Supplier> supplierPhone = new MFXTableColumn<>("Phone", true, Comparator.comparing(Supplier::getSupplierPhoneNumber));
        MFXTableColumn<Supplier> supplierEmail = new MFXTableColumn<>("Email", true, Comparator.comparing(Supplier::getSupplierEmail));
        MFXTableColumn<Supplier> supplierTax = new MFXTableColumn<>("Tax No.", true, Comparator.comparing(Supplier::getSupplierTaxNumber));
        MFXTableColumn<Supplier> supplierPurchasesDue = new MFXTableColumn<>("Purchases Due", true, Comparator.comparing(Supplier::getSupplierTotalPurchaseDue));
        MFXTableColumn<Supplier> supplierPurchaseReturnDue = new MFXTableColumn<>("Returns Due", true, Comparator.comparing(Supplier::getSupplierTotalPurchaseReturnDue));

        supplierCode.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getSupplierCode));
        supplierName.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getSupplierName));
        supplierPhone.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getSupplierPhoneNumber));
        supplierEmail.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getSupplierEmail));
        supplierTax.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getSupplierTaxNumber));
        supplierPurchasesDue.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getSupplierTotalPurchaseDue));
        supplierPurchaseReturnDue.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getSupplierTotalPurchaseReturnDue));

        suppliersTable.getTableColumns().addAll(supplierCode, supplierName, supplierPhone, supplierEmail, supplierTax, supplierPurchasesDue, supplierPurchaseReturnDue);
        suppliersTable.getFilters().addAll(
                new IntegerFilter<>("Code", Supplier::getSupplierCode),
                new StringFilter<>("Name", Supplier::getSupplierName),
                new StringFilter<>("Phone", Supplier::getSupplierPhoneNumber),
                new StringFilter<>("Email", Supplier::getSupplierEmail),
                new StringFilter<>("Tax No.", Supplier::getSupplierTaxNumber),
                new DoubleFilter<>("Purchases Due", Supplier::getSupplierTotalPurchaseDue),
                new DoubleFilter<>("Returns Due", Supplier::getSupplierTotalPurchaseReturnDue)
        );

        suppliersTable.setItems(supplierSampleData());
    }

    private MFXTableView<Supplier> getSupplierTable() {
        suppliersTable = new MFXTableView<>();
        suppliersTable.setPrefSize(1000, 1000);
        suppliersTable.autosizeColumnsOnInitialization();
        suppliersTable.features().enableBounceEffect();
        suppliersTable.features().enableSmoothScrolling(0.5);
        return suppliersTable;
    }
}
