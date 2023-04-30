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

public class SaleProductsFormController implements Initializable {
    @FXML
    public MFXTextField saleProductsQnty;
    @FXML
    public MFXFilterComboBox<Product> saleProductsPdct;
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
        dialogOnActions();
    }

    private void dialogOnActions() {
        saleProductsCancelBtn.setOnAction((e) -> {
            final Node source = (Node) e.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        saleProductsSaveBtn.setOnAction((e) -> {});
    }
}
