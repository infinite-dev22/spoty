package org.infinite.spoty.views.inventory.products;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.models.Product;

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
    private Dialog<ButtonType> dialog;

    public ProductsController(Stage stage) {
        Platform.runLater(() -> {
            try {
                productFormDialogPane(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Product> productName = new MFXTableColumn<>("Name", true, Comparator.comparing(Product::getProductName));
        MFXTableColumn<Product> productCode = new MFXTableColumn<>("Code", true, Comparator.comparing(Product::getProductCode));
        MFXTableColumn<Product> productCategory = new MFXTableColumn<>("Category", true, Comparator.comparing(Product::getProductCategory));
        MFXTableColumn<Product> productBrand = new MFXTableColumn<>("Brand", true, Comparator.comparing(Product::getProductBrand));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductName));
        productCode.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductCode));
        productCategory.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductCategory));
        productBrand.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductBrand));

        productsTable.getTableColumns().addAll(productName, productCode, productCategory, productBrand);
        productsTable.getFilters().addAll(
                new StringFilter<>("Name", Product::getProductName),
                new IntegerFilter<>("Code", Product::getProductCode),
                new StringFilter<>("Category", Product::getProductCategory),
                new StringFilter<>("Brand", Product::getProductBrand)
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

    private void productFormDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/ProductForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void productCreateBtnClicked() {
        dialog.showAndWait();
    }
}
