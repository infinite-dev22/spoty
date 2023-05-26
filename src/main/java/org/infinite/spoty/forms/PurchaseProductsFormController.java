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
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.database.models.Brand;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.PurchaseDetail;
import org.infinite.spoty.models.Product;
import org.infinite.spoty.viewModels.ProductDetailViewModel;
import org.infinite.spoty.viewModels.PurchaseDetailsViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.dataShare.DataShare.createPurchaseProduct;
import static org.infinite.spoty.dataShare.DataShare.getAdjustmentProducts;

public class PurchaseProductsFormController implements Initializable {
    @FXML
    public MFXTextField purchaseProductsQnty;
    @FXML
    public MFXFilterComboBox<ProductDetail> purchaseProductsPdct;
    @FXML
    public MFXTextField purchaseProductsOrderTax;
    @FXML
    public MFXTextField purchaseProductsDiscount;
    @FXML
    public MFXFilledButton purchaseProductsSaveBtn;
    @FXML
    public MFXOutlinedButton purchaseProductsCancelBtn;
    @FXML
    public Label purchaseProductsTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        purchaseProductsQnty.textProperty().addListener((observable, oldValue, newValue) -> purchaseProductsQnty.setTrailingIcon(null));
        purchaseProductsPdct.textProperty().addListener((observable, oldValue, newValue) -> purchaseProductsPdct.setLeadingIcon(null));
        purchaseProductsOrderTax.textProperty().addListener((observable, oldValue, newValue) -> purchaseProductsOrderTax.setLeadingIcon(null));
        purchaseProductsDiscount.textProperty().addListener((observable, oldValue, newValue) -> purchaseProductsDiscount.setLeadingIcon(null));

        purchaseProductsQnty.textProperty().bindBidirectional(PurchaseDetailsViewModel.quantityProperty());
        purchaseProductsPdct.valueProperty().bindBidirectional(PurchaseDetailsViewModel.productProperty());
        purchaseProductsOrderTax.textProperty().bindBidirectional(PurchaseDetailsViewModel.netTaxProperty());
        purchaseProductsDiscount.textProperty().bindBidirectional(PurchaseDetailsViewModel.discountProperty());
        purchaseProductsPdct.setItems(ProductDetailViewModel.purchaseDetailsList);
        purchaseProductsPdct.setConverter(new StringConverter<>() {
            @Override
            public String toString(ProductDetail object) {
                if (object != null)
                    return object.getProduct().getName() + " " + object.getName();
                else
                    return null;
            }
            @Override
            public ProductDetail fromString(String string) {
                return null;
            }
        });


        dialogOnActions();
    }

    private void dialogOnActions() {
        purchaseProductsCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            PurchaseDetailsViewModel.resetProperties();
            purchaseProductsQnty.setTrailingIcon(null);
            purchaseProductsPdct.setLeadingIcon(null);
            purchaseProductsOrderTax.setLeadingIcon(null);
            purchaseProductsDiscount.setLeadingIcon(null);
        });
        purchaseProductsSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (purchaseProductsQnty.getText().length() == 0) {
                purchaseProductsQnty.setTrailingIcon(icon);
            }
            if (purchaseProductsPdct.getText().length() == 0) {
                purchaseProductsPdct.setLeadingIcon(icon);
            }
            if (purchaseProductsOrderTax.getText().length() == 0) {
                purchaseProductsOrderTax.setLeadingIcon(icon);
            }
            if (purchaseProductsDiscount.getText().length() == 0) {
                purchaseProductsDiscount.setLeadingIcon(icon);
            }
            if (purchaseProductsQnty.getText().length() > 0
                    && purchaseProductsPdct.getText().length() > 0
                    && purchaseProductsOrderTax.getText().length() > 0
                    && purchaseProductsDiscount.getText().length() > 0) {
                PurchaseDetailsViewModel.addPurchaseDetail();
                PurchaseDetailsViewModel.resetProperties();
                System.out.println(getAdjustmentProducts());

                closeDialog(e);
            }
        });
    }
}
