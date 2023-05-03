package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.infinite.spoty.models.Product;
import org.infinite.spoty.models.Brand;
import org.infinite.spoty.models.Category;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class ProductFormController implements Initializable {
    @FXML
    public MFXTextField productFormName;
    @FXML
    public MFXComboBox<Category> productFormCategory;
    @FXML
    public MFXComboBox<Brand> productFormBrand;
    @FXML
    public MFXComboBox<?> productFormBarCodeType;
    @FXML
    public MFXFilledButton productFormSaveBtn;
    @FXML
    public MFXOutlinedButton productFormCancelBtn;
    @FXML
    public Label productFormTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productFormName.textProperty().addListener((observable, oldValue, newValue) -> productFormName.setTrailingIcon(null));
        productFormCategory.textProperty().addListener((observable, oldValue, newValue) -> productFormCategory.setTrailingIcon(null));
        productFormBrand.textProperty().addListener((observable, oldValue, newValue) -> productFormBrand.setTrailingIcon(null));
        productFormBarCodeType.textProperty().addListener((observable, oldValue, newValue) -> productFormBarCodeType.setTrailingIcon(null));

        dialogOnActions();
    }

    private void dialogOnActions() {
        productFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            productFormName.setText("");
            productFormCategory.setText("");
            productFormBrand.setText("");
            productFormBarCodeType.setText("");

            productFormName.setTrailingIcon(null);
            productFormCategory.setTrailingIcon(null);
            productFormBrand.setTrailingIcon(null);
            productFormBarCodeType.setTrailingIcon(null);
        });
        productFormSaveBtn.setOnAction((e) -> {
            Product product = new Product();
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (productFormName.getText().length() == 0) {
                productFormName.setTrailingIcon(icon);
            }
            if (productFormCategory.getText().length() == 0) {
                productFormCategory.setTrailingIcon(icon);
            }
            if (productFormBrand.getText().length() == 0) {
                productFormBrand.setTrailingIcon(icon);
            }
            if (productFormBarCodeType.getText().length() == 0) {
                productFormBarCodeType.setTrailingIcon(icon);
            }
            if (productFormName.getText().length() > 0
                    && productFormCategory.getText().length() > 0
                    && productFormBrand.getText().length() > 0
                    && productFormBarCodeType.getText().length() > 0) {
                product.setProductName(productFormName.getText());
                product.setProductCategory(productFormCategory.getText());
                product.setProductBrand(productFormBrand.getText());
                product.setProductBarcodeType(productFormBarCodeType.getText());

                productFormName.setText("");
                productFormCategory.setText("");
                productFormBrand.setText("");
                productFormBarCodeType.setText("");

                closeDialog(e);
            }
        });
    }

    public void productFormSaveBtnClicked() {
    }

    public void productFormCancelBtnClicked() {
    }
}
