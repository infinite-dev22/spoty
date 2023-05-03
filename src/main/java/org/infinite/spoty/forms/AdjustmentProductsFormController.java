package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
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
import static org.infinite.spoty.dataShare.DataShare.createAdjustmentProduct;
import static org.infinite.spoty.dataShare.DataShare.getAdjustmentProducts;

public class AdjustmentProductsFormController implements Initializable {
    @FXML
    public MFXTextField adjustmentProductsQnty;
    @FXML
    public MFXFilterComboBox<Product> adjustmentProductsPdct;
    @FXML
    public MFXFilledButton adjustmentProductsSaveBtn;
    @FXML
    public MFXOutlinedButton adjustmentProductsCancelBtn;
    @FXML
    public Label adjustmentProductsTitle;
    @FXML
    public MFXComboBox<?> adjustmentType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        adjustmentProductsPdct.textProperty().addListener((observable, oldValue, newValue) -> adjustmentProductsPdct.setLeadingIcon(null));
        adjustmentProductsQnty.textProperty().addListener((observable, oldValue, newValue) -> adjustmentProductsQnty.setTrailingIcon(null));
        adjustmentType.textProperty().addListener((observable, oldValue, newValue) -> adjustmentType.setLeadingIcon(null));

        dialogOnActions();
    }

    private void dialogOnActions() {
        adjustmentProductsCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            adjustmentProductsPdct.setText("");
            adjustmentProductsQnty.setText("");
            adjustmentType.setText("");
            adjustmentProductsPdct.setLeadingIcon(null);
            adjustmentProductsQnty.setTrailingIcon(null);
            adjustmentType.setLeadingIcon(null);
        });
        adjustmentProductsSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (adjustmentProductsPdct.getText().length() == 0) {
                adjustmentProductsPdct.setLeadingIcon(icon);
            }
            if (adjustmentProductsQnty.getText().length() == 0) {
                adjustmentProductsQnty.setTrailingIcon(icon);
            }
            if (adjustmentType.getText().length() == 0) {
                adjustmentType.setLeadingIcon(icon);
            }
            if (adjustmentProductsPdct.getText().length() > 0
                    && adjustmentProductsQnty.getText().length() > 0
                    && adjustmentType.getText().length() > 0) {
                createAdjustmentProduct(adjustmentProductsPdct.getText(),
                        Double.parseDouble(adjustmentProductsQnty.getText()),
                        adjustmentType.getText());

                adjustmentProductsPdct.setText("");
                adjustmentProductsQnty.setText("");
                adjustmentType.setText("");
                System.out.println(getAdjustmentProducts());

                closeDialog(e);
            }
        });
    }
}
