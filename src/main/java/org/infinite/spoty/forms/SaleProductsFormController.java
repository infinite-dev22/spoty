package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.viewModels.ProductDetailViewModel;
import org.infinite.spoty.viewModels.SaleDetailViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.dataShare.DataShare.getAdjustmentProducts;

public class SaleProductsFormController implements Initializable {
    @FXML
    public MFXTextField saleProductsQnty;
    @FXML
    public MFXFilterComboBox<ProductDetail> saleProductsPdct;
    @FXML
    public MFXTextField saleProductsOrderTax;
    @FXML
    public MFXTextField saleProductsDiscount;
    @FXML
    public MFXFilledButton saleProductsSaveBtn;
    @FXML
    public MFXOutlinedButton saleProductsCancelBtn;
    @FXML
    public Label saleProductsTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add form event listeners.
        saleProductsQnty.textProperty().addListener((observable, oldValue, newValue) -> saleProductsQnty.setTrailingIcon(null));
        saleProductsPdct.textProperty().addListener((observable, oldValue, newValue) -> saleProductsPdct.setLeadingIcon(null));
        saleProductsOrderTax.textProperty().addListener((observable, oldValue, newValue) -> saleProductsOrderTax.setLeadingIcon(null));
        saleProductsDiscount.textProperty().addListener((observable, oldValue, newValue) -> saleProductsDiscount.setLeadingIcon(null));
        // Set combo box options.
        saleProductsPdct.setItems(ProductDetailViewModel.purchaseDetailsList);
        saleProductsPdct.setConverter(new StringConverter<>() {
            @Override
            public String toString(ProductDetail object) {
                if (object != null)
                    return object.getProduct().getName() + " " + object.getName();
                else
                    return "Empty, Add products";
            }

            @Override
            public ProductDetail fromString(String string) {
                return null;
            }
        });
        // Property binding.
        saleProductsQnty.textProperty().bindBidirectional(SaleDetailViewModel.quantityProperty());
        saleProductsPdct.valueProperty().bindBidirectional(SaleDetailViewModel.productProperty());
        saleProductsOrderTax.textProperty().bindBidirectional(SaleDetailViewModel.netTaxProperty());
        saleProductsDiscount.textProperty().bindBidirectional(SaleDetailViewModel.discountProperty());
        dialogOnActions();
    }

    private void dialogOnActions() {
        saleProductsCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            SaleDetailViewModel.resetProperties();
            saleProductsQnty.setTrailingIcon(null);
            saleProductsPdct.setLeadingIcon(null);
            saleProductsOrderTax.setLeadingIcon(null);
            saleProductsDiscount.setLeadingIcon(null);
        });
        saleProductsSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (saleProductsQnty.getText().length() == 0) {
                saleProductsQnty.setTrailingIcon(icon);
            }
            if (saleProductsPdct.getText().length() == 0) {
                saleProductsPdct.setLeadingIcon(icon);
            }
            if (saleProductsOrderTax.getText().length() == 0) {
                saleProductsOrderTax.setLeadingIcon(icon);
            }
            if (saleProductsDiscount.getText().length() == 0) {
                saleProductsDiscount.setLeadingIcon(icon);
            }
            if (saleProductsQnty.getText().length() > 0
                    && saleProductsPdct.getText().length() > 0
                    && saleProductsOrderTax.getText().length() > 0
                    && saleProductsDiscount.getText().length() > 0) {
                SaleDetailViewModel.addSaleDetail();
                SaleDetailViewModel.resetProperties();
                System.out.println(getAdjustmentProducts());
                closeDialog(e);
//                SaleFormController.setupTable();
            }
        });
    }
}
