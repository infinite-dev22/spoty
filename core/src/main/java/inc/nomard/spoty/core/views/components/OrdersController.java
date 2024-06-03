package inc.nomard.spoty.core.views.components;

import inc.nomard.spoty.core.components.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.sales.*;
import inc.nomard.spoty.network_bridge.dtos.sales.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import java.net.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import lombok.extern.java.*;

@Log
public class OrdersController implements Initializable {
    public MFXTableView<SaleMaster> saleOrders;
    public ViewAll viewAll;
    public Label cardTitle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cardTitle.setText(Labels.RECENT_ORDERS);
        saleOrders.setFooterVisible(false);
        saleOrders.setBorder(null);
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<SaleMaster> saleCustomer =
                new MFXTableColumn<>("Customer", false, Comparator.comparing(SaleMaster::getCustomerName));
        MFXTableColumn<SaleMaster> saleStatus =
                new MFXTableColumn<>("Order Status", false, Comparator.comparing(SaleMaster::getSaleStatus));
        MFXTableColumn<SaleMaster> salePaymentStatus =
                new MFXTableColumn<>(
                        "Pay Status", false, Comparator.comparing(SaleMaster::getPaymentStatus));
        MFXTableColumn<SaleMaster> saleGrandTotal =
                new MFXTableColumn<>("Total Amount", false, Comparator.comparing(SaleMaster::getTotal));
        MFXTableColumn<SaleMaster> saleAmountPaid =
                new MFXTableColumn<>("Paid Amount", false, Comparator.comparing(SaleMaster::getAmountPaid));

        saleCustomer.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getCustomerName));
        saleStatus.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getSaleStatus));
        salePaymentStatus.setRowCellFactory(
                sale -> new MFXTableRowCell<>(SaleMaster::getPaymentStatus));
        saleGrandTotal.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getTotal));
        saleAmountPaid.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getAmountPaid));

        saleCustomer.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));
        saleStatus.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));
        salePaymentStatus.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));
        saleGrandTotal.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));
        saleAmountPaid.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));

        saleOrders
                .getTableColumns()
                .addAll(
                        saleCustomer,
                        saleStatus,
                        salePaymentStatus,
                        saleGrandTotal,
                        saleAmountPaid);
        styleSaleMasterTable();

        if (SaleMasterViewModel.getSales().isEmpty()) {
            SaleMasterViewModel.getSales()
                    .addListener(
                            (ListChangeListener<SaleMaster>)
                                    c -> saleOrders.setItems(SaleMasterViewModel.getSales()));
        } else {
            saleOrders.itemsProperty().bindBidirectional(SaleMasterViewModel.salesProperty());
        }
    }

    private void styleSaleMasterTable() {
        saleOrders.setPrefSize(1200, 1000);
        saleOrders.features().enableBounceEffect();
        saleOrders.features().enableSmoothScrolling(0.5);

        saleOrders.setTableRowFactory(
                t -> {
                    MFXTableRow<SaleMaster> row = new MFXTableRow<>(saleOrders, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
//                                showContextMenu((MFXTableRow<SaleMaster>) event.getSource())
//                                        .show(
//                                                saleOrders.getScene().getWindow(),
//                                                event.getScreenX(),
//                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }
}
