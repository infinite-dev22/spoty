package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
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
    public MFXComboBox adjustmentType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogOnActions();
    }

    private void dialogOnActions() {
        adjustmentProductsCancelBtn.setOnAction((e) -> {
            final Node source = (Node) e.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        adjustmentProductsSaveBtn.setOnAction((e) -> {});
    }
}
