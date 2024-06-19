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
public class TopProductsController implements Initializable {
    public ViewAll viewAll;
    public Label cardTitle;
    public MFXTableView<ProductSalesModel> products;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cardTitle.setText(Labels.TOP_PRODUCTS);
        products.setFooterVisible(false);
        products.setBorder(null);
        Platform.runLater(this::setupTable);
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
