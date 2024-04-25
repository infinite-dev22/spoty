package inc.nomard.spoty.core.views.components;

import inc.nomard.spoty.core.components.ViewAll;
import inc.nomard.spoty.core.values.strings.Labels;
import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.core.viewModels.sales.SaleMasterViewModel;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.network_bridge.dtos.sales.SaleMaster;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableRow;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.LongFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class TopProductsController implements Initializable {
    public ViewAll viewAll;
    public Label cardTitle;
    public MFXTableView<Product> products;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cardTitle.setText(Labels.TOP_PRODUCTS);
        products.setFooterVisible(false);
        products.setBorder(null);
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Product> productName =
                new MFXTableColumn<>("Name", false, Comparator.comparing(Product::getName));
        MFXTableColumn<Product> productPrice =
                new MFXTableColumn<>("Price", false, Comparator.comparing(Product::getPrice));
        MFXTableColumn<Product> productType =
                new MFXTableColumn<>("Total Sales", false, Comparator.comparing(Product::getProductType));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getName));
        productPrice.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getPrice));
        productType.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductType));

        productName.prefWidthProperty().bind(products.widthProperty().multiply(.4));
        productPrice.prefWidthProperty().bind(products.widthProperty().multiply(.4));
        productType.prefWidthProperty().bind(products.widthProperty().multiply(.4));

        products
                .getTableColumns()
                .addAll(productName, productPrice, productType);
        getTable();

        if (ProductViewModel.getProducts().isEmpty()) {
            ProductViewModel.getProducts()
                    .addListener(
                            (ListChangeListener<Product>)
                                    c -> products.setItems(ProductViewModel.getProducts()));
        } else {
            products.itemsProperty().bindBidirectional(ProductViewModel.productsProperty());
        }
    }

    private void getTable() {
        products.setPrefSize(1000, 1000);
        products.features().enableBounceEffect();
        products.features().enableSmoothScrolling(0.5);

        products.setTableRowFactory(
                t -> {
                    MFXTableRow<Product> row = new MFXTableRow<>(products, t);
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
