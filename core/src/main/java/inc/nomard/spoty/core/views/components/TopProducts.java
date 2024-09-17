package inc.nomard.spoty.core.views.components;

import inc.nomard.spoty.core.values.strings.Labels;
import inc.nomard.spoty.core.viewModels.dashboard.DashboardViewModel;
import inc.nomard.spoty.network_bridge.dtos.dashboard.ProductSalesModel;
import inc.nomard.spoty.utils.UIUtils;
import inc.nomard.spoty.utils.navigation.Spacer;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import lombok.extern.java.Log;

import java.util.LinkedList;
import java.util.stream.Stream;

@Log
public class TopProducts extends AnchorPane {
    public TableView<ProductSalesModel> products;

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
        var label = new Label(Labels.TOP_PRODUCTS);
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
        TableColumn<ProductSalesModel, String> name = new TableColumn<>("Name");
        TableColumn<ProductSalesModel, String> cost = new TableColumn<>("Cost");
        TableColumn<ProductSalesModel, String> price = new TableColumn<>("Price");
        TableColumn<ProductSalesModel, String> totalSale = new TableColumn<>("Total Sales");

        name.prefWidthProperty().bind(products.widthProperty().multiply(.3));
        cost.prefWidthProperty().bind(products.widthProperty().multiply(.25));
        price.prefWidthProperty().bind(products.widthProperty().multiply(.25));
        totalSale.prefWidthProperty().bind(products.widthProperty().multiply(.25));

        var columnList = new LinkedList<>(Stream.of(name, cost, price, totalSale).toList());
        products.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        products.getColumns().addAll(columnList);
        getTable();

        if (DashboardViewModel.getTopProducts().isEmpty()) {
            DashboardViewModel.getTopProducts()
                    .addListener(
                            (ListChangeListener<ProductSalesModel>)
                                    c -> products.setItems(DashboardViewModel.getTopProducts()));
        } else {
            products.itemsProperty().bindBidirectional(DashboardViewModel.topProductsProperty());
        }
    }

    private void getTable() {
        products.setPrefSize(1000, 1000);

        products.setRowFactory(
                t -> {
                    TableRow<ProductSalesModel> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
//                                showContextMenu((TableRow<Product>) event.getSource())
//                                        .show(
//                                                products.getScene().getWindow(),
//                                                event.getScreenX(),
//                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }
}
