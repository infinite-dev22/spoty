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
import org.infinite.spoty.models.Product;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.dataShare.DataShare.createQuotationProduct;
import static org.infinite.spoty.dataShare.DataShare.getAdjustmentProducts;

public class QuotationProductsFormController implements Initializable {
    @FXML
    public MFXTextField quotationProductsQnty;
    @FXML
    public MFXFilterComboBox<Product> quotationProductsPdct;
    @FXML
    public MFXTextField quotationProductsOrderTax;
    @FXML
    public MFXTextField quotationProductsDiscount;
    @FXML
    public MFXFilledButton quotationProductsSaveBtn;
    @FXML
    public MFXOutlinedButton quotationProductsCancelBtn;
    @FXML
    public Label quotationProductsTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        quotationProductsQnty.textProperty().addListener((observable, oldValue, newValue) -> quotationProductsQnty.setTrailingIcon(null));
        quotationProductsPdct.textProperty().addListener((observable, oldValue, newValue) -> quotationProductsPdct.setLeadingIcon(null));
        quotationProductsOrderTax.textProperty().addListener((observable, oldValue, newValue) -> quotationProductsOrderTax.setLeadingIcon(null));
        quotationProductsDiscount.textProperty().addListener((observable, oldValue, newValue) -> quotationProductsDiscount.setLeadingIcon(null));

        dialogOnActions();
    }

    private void dialogOnActions() {
        quotationProductsCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            quotationProductsQnty.setText("");
            quotationProductsPdct.setText("");
            quotationProductsOrderTax.setText("");
            quotationProductsDiscount.setText("");
            quotationProductsQnty.setTrailingIcon(null);
            quotationProductsPdct.setLeadingIcon(null);
            quotationProductsOrderTax.setLeadingIcon(null);
            quotationProductsDiscount.setLeadingIcon(null);
        });
        quotationProductsSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (quotationProductsQnty.getText().length() == 0) {
                quotationProductsQnty.setTrailingIcon(icon);
            }
            if (quotationProductsPdct.getText().length() == 0) {
                quotationProductsPdct.setLeadingIcon(icon);
            }
            if (quotationProductsOrderTax.getText().length() == 0) {
                quotationProductsOrderTax.setLeadingIcon(icon);
            }
            if (quotationProductsDiscount.getText().length() == 0) {
                quotationProductsDiscount.setLeadingIcon(icon);
            }
            if (quotationProductsQnty.getText().length() > 0
                    && quotationProductsPdct.getText().length() > 0
                    && quotationProductsOrderTax.getText().length() > 0
                    && quotationProductsDiscount.getText().length() > 0) {
                createQuotationProduct(Double.parseDouble(quotationProductsQnty.getText()),
                        quotationProductsPdct.getText(),
                        Double.parseDouble(quotationProductsOrderTax.getText()),
                        Double.parseDouble(quotationProductsDiscount.getText()));

                quotationProductsQnty.setText("");
                quotationProductsPdct.setText("");
                quotationProductsOrderTax.setText("");
                quotationProductsDiscount.setText("");
                System.out.println(getAdjustmentProducts());

                closeDialog(e);
            }
        });
    }
}
