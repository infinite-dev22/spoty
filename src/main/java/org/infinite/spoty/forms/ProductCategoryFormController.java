package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductCategoryFormController implements Initializable {
    @FXML
    public MFXTextField dialogCategoryCode;
    @FXML
    public MFXTextField dialogCategoryName;
    @FXML
    public MFXFilledButton dialogSaveBtn;
    @FXML
    public MFXOutlinedButton dialogCancelBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogOnActions();
    }

    private void dialogOnActions() {
        dialogCancelBtn.setOnAction((e) -> {
            final Node source = (Node) e.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        dialogSaveBtn.setOnAction((e) -> {
        });
    }

}
