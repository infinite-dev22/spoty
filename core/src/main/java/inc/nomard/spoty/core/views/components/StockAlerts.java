package inc.nomard.spoty.core.views.components;

import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import inc.nomard.spoty.core.viewModels.dashboard.DashboardViewModel;
import inc.nomard.spoty.core.views.layout.navigation.Spacer;
import inc.nomard.spoty.network_bridge.dtos.dashboard.StockAlertModel;
import inc.nomard.spoty.utils.AppUtils;
import inc.nomard.spoty.utils.UIUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
public class StockAlerts extends AnchorPane {
    public TableView<StockAlertModel> stockAlert;
    private TableColumn<StockAlertModel, String> name;
    private TableColumn<StockAlertModel, StockAlertModel> totalQuantity;
    private TableColumn<StockAlertModel, StockAlertModel> costPrice;

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

    private Label buildTitle() {
        var label = new Label("Stock Alerts");
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
        name = new TableColumn<>("Name");
        totalQuantity = new TableColumn<>("Quantity");
        costPrice = new TableColumn<>("Cost Price");

        name.prefWidthProperty().bind(stockAlert.widthProperty().multiply(.4));
        totalQuantity.prefWidthProperty().bind(stockAlert.widthProperty().multiply(.4));
        costPrice.prefWidthProperty().bind(stockAlert.widthProperty().multiply(.4));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(name, totalQuantity, costPrice).toList());
        stockAlert.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        stockAlert.getColumns().addAll(columnList);
        styleTable();
        stockAlert.setItems(DashboardViewModel.getStockAlerts());
        stockAlert.getStyleClass().addAll(Styles.STRIPED, Tweaks.EDGE_TO_EDGE);
    }

    private void styleTable() {
        stockAlert.setPrefSize(1000, 1000);
        stockAlert.setRowFactory(
                _ -> {
                    var row = new TableRow<StockAlertModel>() {
                        @Override
                        public void updateItem(StockAlertModel item, boolean empty) {
                            super.updateItem(item, empty);
                        }
                    };
                    EventHandler<MouseEvent> eventHandler =
                            Event::consume;
                    row.setOnMouseClicked(eventHandler);
                    return row;
                });
    }

    private void setupTableColumns() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        totalQuantity.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        totalQuantity.setCellFactory(_ -> new TableCell<>() {
            @Override
            public void updateItem(StockAlertModel item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getTotalQuantity()));
            }
        });
        costPrice.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        costPrice.setCellFactory(_ -> new TableCell<>() {
            @Override
            public void updateItem(StockAlertModel item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getCostPrice()));
            }
        });
    }
}
