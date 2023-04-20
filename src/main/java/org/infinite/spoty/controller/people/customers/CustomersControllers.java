package org.infinite.spoty.controller.people.customers;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.infinite.spoty.model.Customer;
import org.infinite.spoty.model.Customer;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.customerSampleData;

public class CustomersControllers implements Initializable {
    private MFXTableView<Customer> customersTable;

    @FXML
    public BorderPane customersContentPane;

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
            customersContentPane.setCenter(getCustomerTable());
            setupTable();
        })).start();
    }

    private void setupTable() {
        MFXTableColumn<Customer> customerCode = new MFXTableColumn<>("Code", true, Comparator.comparing(Customer::getCustomerCode));
        MFXTableColumn<Customer> customerName = new MFXTableColumn<>("Name", true, Comparator.comparing(Customer::getCustomerName));
        MFXTableColumn<Customer> customerPhone = new MFXTableColumn<>("Phone", true, Comparator.comparing(Customer::getCustomerPhoneNumber));
        MFXTableColumn<Customer> customerEmail = new MFXTableColumn<>("Email", true, Comparator.comparing(Customer::getCustomerEmail));
        MFXTableColumn<Customer> customerTax = new MFXTableColumn<>("Tax No.", true, Comparator.comparing(Customer::getCustomerTaxNumber));
        MFXTableColumn<Customer> customerSalesDue = new MFXTableColumn<>("Sales Due", true, Comparator.comparing(Customer::getCustomerTotalSaleDue));
        MFXTableColumn<Customer> customerSellReturnDue = new MFXTableColumn<>("Returns Due", true, Comparator.comparing(Customer::getCustomerTotalSellReturnDue));

        customerCode.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getCustomerCode));
        customerName.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getCustomerName));
        customerPhone.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getCustomerPhoneNumber));
        customerEmail.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getCustomerEmail));
        customerTax.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getCustomerTaxNumber));
        customerSalesDue.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getCustomerTotalSaleDue));
        customerSellReturnDue.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getCustomerTotalSellReturnDue));

        customersTable.getTableColumns().addAll(customerCode, customerName, customerPhone, customerEmail, customerTax, customerSalesDue, customerSellReturnDue);
        customersTable.getFilters().addAll(
                new IntegerFilter<>("Code", Customer::getCustomerCode),
                new StringFilter<>("Name", Customer::getCustomerName),
                new StringFilter<>("Phone", Customer::getCustomerPhoneNumber),
                new StringFilter<>("Email", Customer::getCustomerEmail),
                new StringFilter<>("Tax No.", Customer::getCustomerTaxNumber),
                new DoubleFilter<>("Sales Due", Customer::getCustomerTotalSaleDue),
                new DoubleFilter<>("Returns Due", Customer::getCustomerTotalSellReturnDue)
        );

        customersTable.setItems(customerSampleData());
    }

    private MFXTableView<Customer> getCustomerTable() {
        customersTable = new MFXTableView<>();
        customersTable.setPrefSize(1000, 1000);
        customersTable.autosizeColumnsOnInitialization();
        customersTable.features().enableBounceEffect();
        customersTable.features().enableSmoothScrolling(0.5);
        return customersTable;
    }
}
