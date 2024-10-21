package inc.nomard.spoty.core.views.components;

import inc.nomard.spoty.core.viewModels.dashboard.DashboardViewModel;
import inc.nomard.spoty.core.views.layout.navigation.Spacer;
import inc.nomard.spoty.network_bridge.dtos.dashboard.ProductSalesModel;
import inc.nomard.spoty.utils.AppUtils;
import inc.nomard.spoty.utils.UIUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
public class TopProducts extends AnchorPane {
    public TableView<ProductSalesModel> products;
    private TableColumn<ProductSalesModel, String> name;
    private TableColumn<ProductSalesModel, ProductSalesModel> cost;
    private TableColumn<ProductSalesModel, ProductSalesModel> price;
    private TableColumn<ProductSalesModel, ProductSalesModel> totalSale;

    public TopProducts() {
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
        var label = new Label("Top Products");
        label.getStyleClass().add("card-title");
        UIUtils.anchor(label, 0d, null, 0d, 0d);
        return label;
    }

    private ViewAll buildViewAll() {
        return new ViewAll();
    }

    private TableView<ProductSalesModel> buildBottom() {
        products = new TableView<>();
        products.setBorder(null);
        setupTable();
        UIUtils.anchor(products, 32d, 0d, 0d, 0d);
        return products;
    }

    private void setupTable() {
        name = new TableColumn<>("Name");
        cost = new TableColumn<>("Cost");
        price = new TableColumn<>("Price");
        totalSale = new TableColumn<>("Total Sales");

        name.prefWidthProperty().bind(products.widthProperty().multiply(.3));
        cost.prefWidthProperty().bind(products.widthProperty().multiply(.25));
        price.prefWidthProperty().bind(products.widthProperty().multiply(.25));
        totalSale.prefWidthProperty().bind(products.widthProperty().multiply(.25));
        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(name, cost, price, totalSale).toList());
        products.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        products.getColumns().addAll(columnList);
        getTable();

        products.setItems(DashboardViewModel.getTopProducts());
    }

    private void getTable() {
        products.setPrefSize(1000, 1000);

        products.setRowFactory(
                t -> {
                    TableRow<ProductSalesModel> row = new TableRow<>();
                    EventHandler<MouseEvent> eventHandler =
                            event -> {
                                if (Objects.equals(event.getEventType(), MouseEvent.MOUSE_CLICKED)) {
                                    log.info("Row with Product \"" + row.getItem().getName() + "\" has been clicked.");
                                }
                                event.consume();
                            };
                    row.setOnMouseClicked(eventHandler);
                    return row;
                });
    }

    private void setupTableColumns() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        cost.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        cost.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(ProductSalesModel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getCostPrice()));
            }
        });
        price.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        price.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(ProductSalesModel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getSalePrice()));
            }
        });
        totalSale.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        totalSale.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(ProductSalesModel item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getTotalQuantity()));
            }
        });
    }
}
