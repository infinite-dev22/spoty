package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BrandFormController implements Initializable {
    public Label brandFormTitle;
    public MFXTextField brandFormName;
    public MFXTextField brandFormDescription;
    public MFXFilledButton brandFormSaveBtn;
    public MFXOutlinedButton brandFormCancelBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogOnActions();
    }

    private void dialogOnActions() {
        brandFormCancelBtn.setOnAction((e) -> {
            final Node source = (Node) e.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        brandFormSaveBtn.setOnAction((e) -> {});
    }
}
