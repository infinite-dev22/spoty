package inc.nomard.spoty.core.views.components;

import inc.nomard.spoty.core.components.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.dashboard.*;
import inc.nomard.spoty.network_bridge.dtos.dashboard.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.navigation.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import java.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import lombok.extern.java.*;

@Log
public class StockAlerts extends AnchorPane {
    public MFXTableView<StockAlertModel> stockAlert;

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
        var viewAll = new ViewAll();
        return viewAll;
    }

    private MFXTableView<StockAlertModel> buildBottom() {
        stockAlert = new MFXTableView<>();
        stockAlert.setFooterVisible(false);
        stockAlert.setBorder(null);
        setupTable();
        UIUtils.anchor(stockAlert, 32d, 0d, 0d, 0d);
        return stockAlert;
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
