package org.infinite.spoty.views.stock_in;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class StockInController implements Initializable {
    @FXML
    public MFXTextField stockInSearchBar;
    @FXML
    public HBox stockInActionsPane;
    @FXML
    public MFXButton stockInImportBtn;
    @FXML
    public MFXTableView<?> stockInTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

//        Platform.runLater(this::setupTable);
//}
//
//    private void setupTable() {
//        MFXTableColumn<Product> productName = new MFXTableColumn<>("Name", true, Comparator.comparing(Product::getProductName));
//        MFXTableColumn<Product> productCode = new MFXTableColumn<>("Code", true, Comparator.comparing(Product::getProductCode));
//        MFXTableColumn<Product> productCategory = new MFXTableColumn<>("Category", true, Comparator.comparing(Product::getProductCategory));
//        MFXTableColumn<Product> productBrand = new MFXTableColumn<>("Brand", true, Comparator.comparing(Product::getProductBrand));
//        MFXTableColumn<Product> productPrice = new MFXTableColumn<>("Price", true, Comparator.comparing(Product::getProductPrice));
//        MFXTableColumn<Product> productUnit = new MFXTableColumn<>("Unit", true, Comparator.comparing(Product::getProductUnit));
//        MFXTableColumn<Product> productQuantity = new MFXTableColumn<>("Quantity", true, Comparator.comparing(Product::getProductQuantity));
//
//        productName.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductName));
//        productCode.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductCode));
//        productCategory.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductCategory));
//        productBrand.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductBrand));
//        productPrice.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductPrice));
//        productUnit.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductUnit));
//        productQuantity.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getProductQuantity));
//
//        productsTable.getTableColumns().addAll(productName, productCode, productCategory, productBrand, productPrice, productUnit, productQuantity);
//        productsTable.getFilters().addAll(
//                new StringFilter<>("Name", Product::getProductName),
//                new IntegerFilter<>("Code", Product::getProductCode),
//                new StringFilter<>("Category", Product::getProductCategory),
//                new StringFilter<>("Brand", Product::getProductBrand),
//                new DoubleFilter<>("Price", Product::getProductPrice),
//                new StringFilter<>("Unit", Product::getProductUnit),
//                new DoubleFilter<>("Quantity", Product::getProductQuantity)
//        );
//        getProductTable();
//        productsTable.setItems(productSampleData());
//    }
//
//    private void getProductTable() {
//        productsTable.setPrefSize(1000, 1000);
//        productsTable.features().enableBounceEffect();
//        productsTable.autosizeColumnsOnInitialization();
//        productsTable.features().enableSmoothScrolling(0.5);
//    }
//
//    public void productCreateBtnClicked() {
//        try {
//            BorderPane productFormPane = fxmlLoader("forms/StockInForm.fxml").load();
//            ((StackPane) productsContentPane.getParent()).getChildren().add(productFormPane);
//            ((StackPane) productsContentPane.getParent()).getChildren().get(0).setVisible(false);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//    }

    public void stockInCreateBtnClicked() {
        // TODO Auto-generated method stub
    }
}
