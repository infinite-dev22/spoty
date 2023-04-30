package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.infinite.spoty.model.Brand;
import org.infinite.spoty.model.Category;
import org.infinite.spoty.model.UnitOfMeasure;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductFormController implements Initializable {
    @FXML
    public MFXOutlinedButton productCancelBtn;
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
    public MFXFilledButton productSaveBtn;
    @FXML
    public MFXComboBox<Category> productCategory;
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
        productCancel();
    }

    private void productCancel() {
        productCancelBtn.setOnAction((e) -> {
            ((StackPane) productFormContentPane.getParent().getParent().getParent()).getChildren().get(0).setVisible(true);
            ((StackPane) productFormContentPane.getParent().getParent().getParent()).getChildren().remove(1);
        });
    }
}
