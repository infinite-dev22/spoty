package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.infinite.spoty.database.models.Brand;
import org.infinite.spoty.database.models.ProductCategory;
import org.infinite.spoty.values.strings.Values;
import org.infinite.spoty.viewModels.BrandViewModel;
import org.infinite.spoty.viewModels.ProductCategoryViewModel;
import org.infinite.spoty.viewModels.ProductMasterViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class ProductFormController implements Initializable {
    @FXML
    public MFXTextField productFormName;
    @FXML
    public MFXComboBox<ProductCategory> productFormCategory;
    @FXML
    public MFXComboBox<Brand> productFormBrand;
    @FXML
    public MFXComboBox<String> productFormBarCodeType;
    @FXML
    public MFXFilledButton productFormSaveBtn;
    @FXML
    public MFXOutlinedButton productFormCancelBtn;
    @FXML
    public Label productFormTitle;
    @FXML
    public MFXToggleButton notForSaleToggle;
    @FXML
    public MFXToggleButton activeToggle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productFormName.textProperty().addListener((observable, oldValue, newValue) -> productFormName.setTrailingIcon(null));
        productFormCategory.textProperty().addListener((observable, oldValue, newValue) -> productFormCategory.setLeadingIcon(null));
        productFormBrand.textProperty().addListener((observable, oldValue, newValue) -> productFormBrand.setLeadingIcon(null));
        productFormBarCodeType.textProperty().addListener((observable, oldValue, newValue) -> productFormBarCodeType.setLeadingIcon(null));

        productFormName.textProperty().bindBidirectional(ProductMasterViewModel.nameProperty());
        productFormCategory.valueProperty().bindBidirectional(ProductMasterViewModel.categoryProperty());
        productFormBrand.valueProperty().bindBidirectional(ProductMasterViewModel.brandProperty());
        productFormBarCodeType.textProperty().bindBidirectional(ProductMasterViewModel.barcodeTypeProperty());
        notForSaleToggle.selectedProperty().bindBidirectional(ProductMasterViewModel.notForSaleProperty());
        activeToggle.selectedProperty().bindBidirectional(ProductMasterViewModel.isActiveProperty());

        productFormCategory.setItems(ProductCategoryViewModel.categoriesList);
        productFormCategory.setConverter(new StringConverter<>() {
            @Override
            public String toString(ProductCategory object) {
                if (object != null)
                    return object.getName();
                else
                    return null;
            }
            @Override
            public ProductCategory fromString(String string) {
                return null;
            }
        });

        productFormBrand.setItems(BrandViewModel.brandsList);
        productFormBrand.setConverter(new StringConverter<>() {
            @Override
            public String toString(Brand object) {
                if (object != null)
                    return object.getName();
                else
                    return null;
            }
            @Override
            public Brand fromString(String string) {
                return null;
            }
        });
        productFormBarCodeType.setItems(FXCollections.observableArrayList(Values.BARCODETYPES));

        dialogOnActions();
    }

    private void dialogOnActions() {
        productFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            ProductMasterViewModel.resetProperties();

            productFormName.setTrailingIcon(null);
            productFormCategory.setLeadingIcon(null);
            productFormBrand.setLeadingIcon(null);
            productFormBarCodeType.setLeadingIcon(null);
        });
        productFormSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (productFormName.getText().length() == 0) {
                productFormName.setTrailingIcon(icon);
            }
            if (productFormCategory.getText().length() == 0) {
                productFormCategory.setLeadingIcon(icon);
            }
            if (productFormBrand.getText().length() == 0) {
                productFormBrand.setLeadingIcon(icon);
            }
            if (productFormBarCodeType.getText().length() == 0) {
                productFormBarCodeType.setLeadingIcon(icon);
            }
            if (productFormName.getText().length() > 0
                    && productFormCategory.getText().length() > 0
                    && productFormBrand.getText().length() > 0
                    && productFormBarCodeType.getText().length() > 0) {
                ProductMasterViewModel.save();
                ProductMasterViewModel.resetProperties();
                closeDialog(e);
            }
        });
    }
}
