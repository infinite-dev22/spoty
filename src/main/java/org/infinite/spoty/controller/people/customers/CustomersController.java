package org.infinite.spoty.controller.people.customers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.SpotResourceLoader;
import org.infinite.spoty.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.data.SampleData.customerSampleData;

public class CustomersController implements Initializable {
    @FXML
    public MFXTextField customerSearchBar;
    @FXML
    public HBox customerActionsPane;
    @FXML
    public MFXButton customerImportBtn;
    @FXML
    public BorderPane customersContentPane;
    @FXML
    private MFXTableView<Customer> customersTable;
    private Dialog<ButtonType> dialog;

    public CustomersController(Stage stage) {
        Platform.runLater(() -> {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            try {
                customerFormDialogPane(stage);
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage());
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
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
        styleCustomerTable();
        customersTable.setItems(customerSampleData());
    }

    private void styleCustomerTable() {
        customersTable.setPrefSize(1000, 1000);
        customersTable.autosizeColumnsOnInitialization();
        customersTable.features().enableBounceEffect();
        customersTable.features().enableSmoothScrolling(0.5);
    }

    private void customerFormDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/CustomerForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void customerCreateBtnClicked() {
        dialog.showAndWait();
    }
}
