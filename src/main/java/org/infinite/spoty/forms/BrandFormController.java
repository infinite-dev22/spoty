package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.infinite.spoty.models.Brand;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class BrandFormController implements Initializable {
    public Label brandFormTitle;
    public MFXTextField brandFormName;
    public MFXTextField brandFormDescription;
    public MFXFilledButton brandFormSaveBtn;
    public MFXOutlinedButton brandFormCancelBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        brandFormName.textProperty().addListener((observable, oldValue, newValue) -> brandFormName.setTrailingIcon(null));
        brandFormDescription.textProperty().addListener((observable, oldValue, newValue) -> brandFormDescription.setTrailingIcon(null));

        dialogOnActions();
    }

    private void dialogOnActions() {
        brandFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            brandFormName.setText("");
            brandFormDescription.setText("");
            brandFormName.setTrailingIcon(null);
            brandFormDescription.setTrailingIcon(null);
        });
        brandFormSaveBtn.setOnAction((e) -> {
            Brand brand = new Brand();
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (brandFormName.getText().length() == 0) {
                brandFormName.setTrailingIcon(icon);
            }
            if (brandFormDescription.getText().length() == 0) {
                brandFormDescription.setTrailingIcon(icon);
            }
            if (brandFormName.getText().length() > 0 && brandFormDescription.getText().length() > 0) {
                brand.setBrandName(brandFormName.getText());
                brand.setBrandDescription(brandFormDescription.getText());
                brandFormName.setText("");
                brandFormDescription.setText("");

                closeDialog(e);
            }
        });
    }
}
