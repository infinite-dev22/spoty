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
import org.infinite.spoty.viewModels.PurchaseDetailsViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class PurchaseDetailFormController implements Initializable {
    @FXML
    public MFXTextField purchaseDetailQnty;
    @FXML
    public MFXFilterComboBox<ProductDetail> purchaseDetail;
    @FXML
    public MFXTextField purchaseDetailOrderTax;
    @FXML
    public MFXTextField purchaseDetailDiscount;
    @FXML
    public MFXFilledButton purchaseDetailSaveBtn;
    @FXML
    public MFXOutlinedButton purchaseDetailCancelBtn;
    @FXML
    public Label purchaseDetailTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        purchaseDetailQnty.textProperty().addListener((observable, oldValue, newValue) -> purchaseDetailQnty.setTrailingIcon(null));
        purchaseDetail.textProperty().addListener((observable, oldValue, newValue) -> purchaseDetail.setLeadingIcon(null));
        purchaseDetailOrderTax.textProperty().addListener((observable, oldValue, newValue) -> purchaseDetailOrderTax.setLeadingIcon(null));
        purchaseDetailDiscount.textProperty().addListener((observable, oldValue, newValue) -> purchaseDetailDiscount.setLeadingIcon(null));

        purchaseDetailQnty.textProperty().bindBidirectional(PurchaseDetailsViewModel.quantityProperty());
        purchaseDetail.valueProperty().bindBidirectional(PurchaseDetailsViewModel.productProperty());
        purchaseDetailOrderTax.textProperty().bindBidirectional(PurchaseDetailsViewModel.netTaxProperty());
        purchaseDetailDiscount.textProperty().bindBidirectional(PurchaseDetailsViewModel.discountProperty());
        purchaseDetail.setItems(ProductDetailViewModel.purchaseDetailsList);
        purchaseDetail.setConverter(new StringConverter<>() {
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
        purchaseDetailCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            PurchaseDetailsViewModel.resetProperties();
            purchaseDetailQnty.setTrailingIcon(null);
            purchaseDetail.setLeadingIcon(null);
            purchaseDetailOrderTax.setLeadingIcon(null);
            purchaseDetailDiscount.setLeadingIcon(null);
        });
        purchaseDetailSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (purchaseDetailQnty.getText().length() == 0) {
                purchaseDetailQnty.setTrailingIcon(icon);
            }
            if (purchaseDetail.getText().length() == 0) {
                purchaseDetail.setLeadingIcon(icon);
            }
            if (purchaseDetailOrderTax.getText().length() == 0) {
                purchaseDetailOrderTax.setLeadingIcon(icon);
            }
            if (purchaseDetailDiscount.getText().length() == 0) {
                purchaseDetailDiscount.setLeadingIcon(icon);
            }
            if (purchaseDetailQnty.getText().length() > 0
                    && purchaseDetail.getText().length() > 0
                    && purchaseDetailOrderTax.getText().length() > 0
                    && purchaseDetailDiscount.getText().length() > 0) {
                PurchaseDetailsViewModel.addPurchaseDetail();
                PurchaseDetailsViewModel.resetProperties();
                closeDialog(e);
            }
        });
    }
}
