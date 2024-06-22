package inc.nomard.spoty.core.views.components;

import inc.nomard.spoty.core.components.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.dashboard.*;
import inc.nomard.spoty.network_bridge.dtos.dashboard.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.navigation.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.mfxcore.controls.*;
import java.net.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import lombok.extern.java.*;

@Log
public class TopProducts extends AnchorPane {
    public MFXTableView<ProductSalesModel> products;

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
        var viewAll = new ViewAll();
        return viewAll;
    }

    private MFXTableView<ProductSalesModel> buildBottom() {
        products = new MFXTableView<>();
        products.setFooterVisible(false);
        products.setBorder(null);
        setupTable();
        UIUtils.anchor(products, 32d, 0d, 0d, 0d);
        return products;
    }

    private void setupTable() {
        MFXTableColumn<ProductSalesModel> name =
                new MFXTableColumn<>("Name", false, Comparator.comparing(ProductSalesModel::getName));
        MFXTableColumn<ProductSalesModel> cost =
                new MFXTableColumn<>("Cost", false, Comparator.comparing(ProductSalesModel::getCostPrice));
        MFXTableColumn<ProductSalesModel> price =
                new MFXTableColumn<>("Price", false, Comparator.comparing(ProductSalesModel::getSalePrice));
        MFXTableColumn<ProductSalesModel> totalSale =
                new MFXTableColumn<>("Total Sales", false, Comparator.comparing(ProductSalesModel::getTotalQuantity));

        name.setRowCellFactory(product -> new MFXTableRowCell<>(ProductSalesModel::getName));
        cost.setRowCellFactory(product -> new MFXTableRowCell<>(ProductSalesModel::getCostPrice));
        price.setRowCellFactory(product -> new MFXTableRowCell<>(ProductSalesModel::getSalePrice));
        totalSale.setRowCellFactory(product -> new MFXTableRowCell<>(ProductSalesModel::getTotalQuantity));

        name.prefWidthProperty().bind(products.widthProperty().multiply(.3));
        cost.prefWidthProperty().bind(products.widthProperty().multiply(.25));
        price.prefWidthProperty().bind(products.widthProperty().multiply(.25));
        totalSale.prefWidthProperty().bind(products.widthProperty().multiply(.25));

        products
                .getTableColumns()
                .addAll(name, cost, price, totalSale);
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
        products.features().enableBounceEffect();
        products.features().enableSmoothScrolling(0.5);

        products.setTableRowFactory(
                t -> {
                    MFXTableRow<ProductSalesModel> row = new MFXTableRow<>(products, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
//                                showContextMenu((MFXTableRow<Product>) event.getSource())
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
