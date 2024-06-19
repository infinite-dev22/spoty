package inc.nomard.spoty.core.views.components;

import inc.nomard.spoty.core.components.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.dashboard.*;
import inc.nomard.spoty.network_bridge.dtos.dashboard.*;
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
public class StockAlertsController implements Initializable {
    public MFXTableView<StockAlertModel> stockAlert;
    public Label cardTitle;
    public ViewAll viewAll;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cardTitle.setText(Labels.STOCK_ALERTS);
        stockAlert.setFooterVisible(false);
        stockAlert.setBorder(null);
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<StockAlertModel> name =
                new MFXTableColumn<>("Name", false, Comparator.comparing(StockAlertModel::getName));
        MFXTableColumn<StockAlertModel> totalQuantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(StockAlertModel::getTotalQuantity));
        MFXTableColumn<StockAlertModel> costPrice =
                new MFXTableColumn<>("Cost Price", false, Comparator.comparing(StockAlertModel::getCostPrice));

        name.setRowCellFactory(customer -> new MFXTableRowCell<>(StockAlertModel::getName));
        totalQuantity.setRowCellFactory(customer -> new MFXTableRowCell<>(StockAlertModel::getTotalQuantity));
        costPrice.setRowCellFactory(customer -> new MFXTableRowCell<>(StockAlertModel::getCostPrice));

        name.prefWidthProperty().bind(stockAlert.widthProperty().multiply(.4));
        totalQuantity.prefWidthProperty().bind(stockAlert.widthProperty().multiply(.4));
        costPrice.prefWidthProperty().bind(stockAlert.widthProperty().multiply(.4));

        stockAlert
                .getTableColumns()
                .addAll(name, totalQuantity, costPrice);
        styleTable();

        if (DashboardViewModel.getStockAlerts().isEmpty()) {
            DashboardViewModel.getStockAlerts().addListener(
                    (ListChangeListener<StockAlertModel>)
                            c -> stockAlert.setItems(DashboardViewModel.getStockAlerts()));
        } else {
            stockAlert.itemsProperty().bindBidirectional(DashboardViewModel.stockAlertsProperty());
        }
    }

    private void styleTable() {
        stockAlert.setPrefSize(1000, 1000);
        stockAlert.features().enableBounceEffect();
        stockAlert.features().enableSmoothScrolling(0.5);

        stockAlert.setTableRowFactory(
                t -> {
                    MFXTableRow<StockAlertModel> row = new MFXTableRow<>(stockAlert, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
//                                showContextMenu((MFXTableRow<StockAlertModel>) event.getSource())
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
