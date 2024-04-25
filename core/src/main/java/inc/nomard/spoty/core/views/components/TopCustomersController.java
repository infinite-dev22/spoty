package inc.nomard.spoty.core.views.components;

import inc.nomard.spoty.core.components.ViewAll;
import inc.nomard.spoty.core.values.strings.Labels;
import inc.nomard.spoty.core.viewModels.CustomerViewModel;
import inc.nomard.spoty.network_bridge.dtos.Customer;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableRow;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class TopCustomersController implements Initializable {
    public MFXTableView<Customer> customers;
    public Label cardTitle;
    public ViewAll viewAll;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cardTitle.setText(Labels.TOP_CUSTOMERS);
        customers.setFooterVisible(false);
        customers.setBorder(null);
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Customer> customerName =
                new MFXTableColumn<>("Name", false, Comparator.comparing(Customer::getName));
        MFXTableColumn<Customer> customerPhone =
                new MFXTableColumn<>("Phone", false, Comparator.comparing(Customer::getPhone));
        MFXTableColumn<Customer> customerEmail =
                new MFXTableColumn<>("Email", false, Comparator.comparing(Customer::getEmail));

        customerName.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getName));
        customerPhone.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getPhone));
        customerEmail.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getEmail));

        customerName.prefWidthProperty().bind(customers.widthProperty().multiply(.4));
        customerPhone.prefWidthProperty().bind(customers.widthProperty().multiply(.4));
        customerEmail.prefWidthProperty().bind(customers.widthProperty().multiply(.4));

        customers
                .getTableColumns()
                .addAll(customerName, customerPhone, customerEmail);
        styleTable();

        if (CustomerViewModel.getCustomers().isEmpty()) {
            CustomerViewModel.getCustomers().addListener(
                    (ListChangeListener<Customer>)
                            c -> customers.setItems(CustomerViewModel.getCustomers()));
        } else {
            customers.itemsProperty().bindBidirectional(CustomerViewModel.customersProperty());
        }
    }

    private void styleTable() {
        customers.setPrefSize(1000, 1000);
        customers.features().enableBounceEffect();
        customers.features().enableSmoothScrolling(0.5);

        customers.setTableRowFactory(
                t -> {
                    MFXTableRow<Customer> row = new MFXTableRow<>(customers, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
//                                showContextMenu((MFXTableRow<Customer>) event.getSource())
//                                        .show(
//                                                customers.getScene().getWindow(),
//                                                event.getScreenX(),
//                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }
}