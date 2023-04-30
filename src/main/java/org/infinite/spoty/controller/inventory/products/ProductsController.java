package org.infinite.spoty.controller.inventory.products;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.infinite.spoty.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.data.SampleData.productSampleData;

public class ProductsController implements Initializable {
    @FXML
    public MFXTableView<Product> productsTable;
    @FXML
    public BorderPane productsContentPane;
    @FXML
    public MFXTextField productsSearchBar;
    @FXML
    public MFXButton productImportBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Product> productName = new MFXTableColumn<>("Name", true, Comparator.comparing(Product::getProductName));
        MFXTableColumn<Product> productCode = new MFXTableColumn<>("Code", true, Comparator.comparing(Product::getProductCode));
        MFXTableColumn<Product> productCategory = new MFXTableColumn<>("Category", true, Comparator.comparing(Product::getProductCategory));
        MFXTableColumn<Product> productBrand = new MFXTableColumn<>("Brand", true, Comparator.comparing(Product::getProductBrand));
        MFXTableColumn<Product> productPrice = new MFXTableColumn<>("Price", true, Comparator.comparing(Product::getProductPrice));
        MFXTableColumn<Product> productUnit = new MFXTableColumn<>("Unit", true, Comparator.comparing(Product::getProductUnit));
        MFXTableColumn<Product> productQuantity = new MFXTableColumn<>("Quantity", true, Comparator.comparing(Product::getProductQuantity));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductName));
        productCode.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductCode));
        productCategory.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductCategory));
        productBrand.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductBrand));
        productPrice.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductPrice));
        productUnit.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductUnit));
        productQuantity.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductQuantity));

        productsTable.getTableColumns().addAll(productName, productCode, productCategory, productBrand, productPrice, productUnit, productQuantity);
        productsTable.getFilters().addAll(
                new StringFilter<>("Name", Product::getProductName),
                new IntegerFilter<>("Code", Product::getProductCode),
                new StringFilter<>("Category", Product::getProductCategory),
                new StringFilter<>("Brand", Product::getProductBrand),
                new DoubleFilter<>("Price", Product::getProductPrice),
                new StringFilter<>("Unit", Product::getProductUnit),
                new DoubleFilter<>("Quantity", Product::getProductQuantity)
        );
        getProductTable();
        productsTable.setItems(productSampleData());
    }

    private void getProductTable() {
        productsTable.setPrefSize(1000, 1000);
        productsTable.features().enableBounceEffect();
        productsTable.autosizeColumnsOnInitialization();
        productsTable.features().enableSmoothScrolling(0.5);
    }

    public void productCreateBtnClicked() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        try {
            BorderPane productFormPane = fxmlLoader("forms/ProductForm.fxml").load();
            ((StackPane) productsContentPane.getParent()).getChildren().add(productFormPane);
            ((StackPane) productsContentPane.getParent()).getChildren().get(0).setVisible(false);
        } catch (IOException ex) {
            logger.error(ex.getLocalizedMessage());
        }
    }
}
