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

import java.net.URL;
import java.util.ResourceBundle;

public class PurchaseProductsFormController implements Initializable {
    @FXML
    public MFXTextField purchaseProductsQnty;
    @FXML
    public MFXFilterComboBox<Product> purchaseProductsPdct;
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
        dialogOnActions();
    }

    private void dialogOnActions() {
        purchaseProductsCancelBtn.setOnAction((e) -> {
            final Node source = (Node) e.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        purchaseProductsSaveBtn.setOnAction((e) -> {});
    }
}
