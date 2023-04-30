package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.infinite.spoty.model.Product;
import org.infinite.spoty.model.Quotation;

import java.net.URL;
import java.util.ResourceBundle;

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
        dialogOnActions();
    }

    private void dialogOnActions() {
        quotationProductsCancelBtn.setOnAction((e) -> {
            final Node source = (Node) e.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        quotationProductsSaveBtn.setOnAction((e) -> {});
    }
}
