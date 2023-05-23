package org.infinite.spoty.views.people.customers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.database.models.Customer;
import org.infinite.spoty.viewModels.CustomerVewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

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
            try {
                customerFormDialogPane(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Customer> customerCode = new MFXTableColumn<>("Code", true, Comparator.comparing(Customer::getCode));
        MFXTableColumn<Customer> customerName = new MFXTableColumn<>("Name", true, Comparator.comparing(Customer::getName));
        MFXTableColumn<Customer> customerPhone = new MFXTableColumn<>("Phone", true, Comparator.comparing(Customer::getPhone));
        MFXTableColumn<Customer> customerEmail = new MFXTableColumn<>("Email", true, Comparator.comparing(Customer::getEmail));
        MFXTableColumn<Customer> customerTax = new MFXTableColumn<>("Tax No.", true, Comparator.comparing(Customer::getTaxNumber));
//        MFXTableColumn<Customer> customerSalesDue = new MFXTableColumn<>("Sales Due", true, Comparator.comparing(Customer::getTotalSaleDue));
//        MFXTableColumn<Customer> customerSellReturnDue = new MFXTableColumn<>("Returns Due", true, Comparator.comparing(Customer::getTotalSellReturnDue));

        customerCode.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getCode));
        customerName.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getName));
        customerPhone.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getPhone));
        customerEmail.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getEmail));
        customerTax.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getTaxNumber));
//        customerSalesDue.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getTotalSaleDue));
//        customerSellReturnDue.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getTotalSellReturnDue));

        customerCode.prefWidthProperty().bind(customersTable.widthProperty().multiply(.1));
        customerName.prefWidthProperty().bind(customersTable.widthProperty().multiply(.3));
        customerPhone.prefWidthProperty().bind(customersTable.widthProperty().multiply(.2));
        customerEmail.prefWidthProperty().bind(customersTable.widthProperty().multiply(.2));
        customerTax.prefWidthProperty().bind(customersTable.widthProperty().multiply(.2));

        customersTable.getTableColumns().addAll(customerCode, customerName, customerPhone, customerEmail, customerTax);
        customersTable.getFilters().addAll(
                new StringFilter<>("Code", Customer::getCode),
                new StringFilter<>("Name", Customer::getName),
                new StringFilter<>("Phone", Customer::getPhone),
                new StringFilter<>("Email", Customer::getEmail),
                new StringFilter<>("Tax No.", Customer::getTaxNumber)
        );
        styleCustomerTable();
        customersTable.setItems(CustomerVewModel.getCustomers());
    }

    private void styleCustomerTable() {
        customersTable.setPrefSize(1000, 1000);
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
