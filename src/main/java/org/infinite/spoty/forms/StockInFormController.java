package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.infinite.spoty.database.models.Brand;
import org.infinite.spoty.database.models.ProductCategory;
import org.infinite.spoty.database.models.UnitOfMeasure;

import java.net.URL;
import java.util.ResourceBundle;

public class StockInFormController implements Initializable {
    @FXML
    public GridPane productFormContentPane;
    @FXML
    public MFXTextField productName;
    @FXML
    public MFXTextField productPrice;
    @FXML
    public MFXTextField productStockAlert;
    @FXML
    public MFXTextField productOrderTax;
    @FXML
    public MFXTextField productDescription;
    @FXML
    public MFXComboBox<ProductCategory> productCategory;
    @FXML
    public MFXComboBox<Brand> productBrand;
    @FXML
    public MFXComboBox<?> productBarCodeType;
    @FXML
    public MFXComboBox<UnitOfMeasure> productUnit;
    @FXML
    public MFXComboBox<UnitOfMeasure> productSaleUnit;
    @FXML
    public MFXComboBox<UnitOfMeasure> productPurchaseUnit;
    @FXML
    public MFXComboBox<?> productTaxType;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void productSaveBtnClicked() {
//        Product product = new Product();
//        MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);
//
//        if (productName.getText().length() == 0) {
//            productName.setTrailingIcon(icon);
//        }
//        if (productPrice.getText().length() == 0) {
//            productPrice.setTrailingIcon(icon);
//        }
//        if (productCategory.getText().length() == 0) {
//            productCategory.setLeadingIcon(icon);
//        }
//        if (productBrand.getText().length() == 0) {
//            productBrand.setLeadingIcon(icon);
//        }
//        if (productUnit.getText().length() == 0) {
//            productUnit.setLeadingIcon(icon);
//        }
//        if (productSaleUnit.getText().length() == 0) {
//            productSaleUnit.setLeadingIcon(icon);
//        }
//        if (productPurchaseUnit.getText().length() == 0) {
//            productPurchaseUnit.setLeadingIcon(icon);
//        }
//        if (
//                productName.getText().length() > 0
//                        && productPrice.getText().length() > 0
//                        && productCategory.getText().length() > 0
//                        && productBrand.getText().length() > 0
//                        && productUnit.getText().length() > 0
//                        && productSaleUnit.getText().length() > 0
//                        && productPurchaseUnit.getText().length() > 0) {
//            product.setProductName(productName.getText());
//            product.setProductPrice(Double.parseDouble(productPrice.getText()));
//            product.setProductBrand(productBrand.getText());
//            product.setProductCategory(productCategory.getText());
//            product.setProductUnit(productUnit.getText());
//            product.setProductSaleUnit(productSaleUnit.getText());
//            product.setProductPurchaseUnit(productPurchaseUnit.getText());
////            product.setProductCode(productCode.getText());
////            product.setProductQuantity(productQuantity.getText());
//            // Must add more fields in products class.
//
//            // Empty the fields after.
//            productName.setText("");
//            productPrice.setText("");
//            productBrand.setText("");
//            productCategory.setText("");
//            productUnit.setText("");
//            productSaleUnit.setText("");
//            productPurchaseUnit.setText("");
//
//            // Show notification on Product status
//        }
    }

    public void productCancelBtnClicked() {
        ((StackPane) productFormContentPane.getParent().getParent().getParent()).getChildren().get(0).setVisible(true);
        // Remove Screen
        ((StackPane) productFormContentPane.getParent().getParent().getParent()).getChildren().remove(1);

        // Empty the fields after.
        productName.setText("");
        productPrice.setText("");
        productBrand.setText("");
        productCategory.setText("");
        productUnit.setText("");
        productSaleUnit.setText("");
        productPurchaseUnit.setText("");
    }
}
