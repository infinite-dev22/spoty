package inc.nomard.spoty.core.views.components;

import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.dashboard.*;
import inc.nomard.spoty.network_bridge.dtos.dashboard.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.navigation.*;
import java.util.*;
import java.util.stream.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import lombok.extern.java.*;

@Log
public class StockAlerts extends AnchorPane {
    public TableView<StockAlertModel> stockAlert;

    public StockAlerts() {
        this.setMinHeight(351d);
        this.setPrefHeight(551d);
        this.setMaxHeight(751d);
        this.getStyleClass().add("card-flat");
        this.getChildren().addAll(buildTop(), buildBottom());
    }

    private HBox buildTop() {
        var hbox = new HBox();
        hbox.setSpacing(20d);
        hbox.setPadding(new Insets(5d));
        hbox.getChildren().addAll(
                buildTitle(),
                new Spacer(),
                buildViewAll()
        );
        UIUtils.anchor(hbox, 0d, 0d, null, 0d);
        return hbox;
    }

    private io.github.palexdev.mfxcore.controls.Label buildTitle() {
        var label = new io.github.palexdev.mfxcore.controls.Label(Labels.RECENT_ORDERS);
        label.getStyleClass().add("card-title");
        UIUtils.anchor(label, 0d, null, 0d, 0d);
        return label;
    }

    private ViewAll buildViewAll() {
        return new ViewAll();
    }

    private TableView<StockAlertModel> buildBottom() {
        stockAlert = new TableView<>();
        stockAlert.setBorder(null);
        setupTable();
        UIUtils.anchor(stockAlert, 32d, 0d, 0d, 0d);
        return stockAlert;
    }

    private void setupTable() {
        TableColumn<StockAlertModel, String> name = new TableColumn<>("Name");
        TableColumn<StockAlertModel, String> totalQuantity = new TableColumn<>("Quantity");
        TableColumn<StockAlertModel, String> costPrice = new TableColumn<>("Cost Price");

        name.prefWidthProperty().bind(stockAlert.widthProperty().multiply(.4));
        totalQuantity.prefWidthProperty().bind(stockAlert.widthProperty().multiply(.4));
        costPrice.prefWidthProperty().bind(stockAlert.widthProperty().multiply(.4));

        var columnList = new LinkedList<>(Stream.of(name, totalQuantity, costPrice).toList());
        stockAlert.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        stockAlert.getColumns().addAll(columnList);
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

        stockAlert.setRowFactory(
                t -> {
                    TableRow<StockAlertModel> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
//                                showContextMenu((TableRow<StockAlertModel>) event.getSource())
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
