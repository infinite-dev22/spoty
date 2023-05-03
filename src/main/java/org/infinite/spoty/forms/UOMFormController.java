package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.infinite.spoty.models.Brand;
import org.infinite.spoty.models.UnitOfMeasure;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class UOMFormController implements Initializable {
    @FXML
    public MFXTextField uomFormName;
    @FXML
    public MFXTextField uomFormShortName;
    @FXML
    public MFXFilledButton uomFormSaveBtn;
    @FXML
    public MFXOutlinedButton uomFormCancelBtn;
    @FXML
    public Label uomFormTitle;
    @FXML
    public MFXComboBox<UnitOfMeasure> uomFormBaseUnit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        uomFormName.textProperty().addListener((observable, oldValue, newValue) -> uomFormName.setTrailingIcon(null));
        uomFormShortName.textProperty().addListener((observable, oldValue, newValue) -> uomFormShortName.setTrailingIcon(null));

        setUomFormDialogOnActions();
    }

    private void setUomFormDialogOnActions() {
        uomFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            uomFormName.setText("");
            uomFormShortName.setText("");
            uomFormName.setTrailingIcon(null);
            uomFormShortName.setTrailingIcon(null);
        });
        uomFormSaveBtn.setOnAction((e) -> {
            Brand brand = new Brand();
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (uomFormName.getText().length() == 0) {
                uomFormName.setTrailingIcon(icon);
            }
            if (uomFormShortName.getText().length() == 0) {
                uomFormShortName.setTrailingIcon(icon);
            }
            if (uomFormName.getText().length() > 0 && uomFormShortName.getText().length() > 0) {
                brand.setBrandName(uomFormName.getText());
                brand.setBrandDescription(uomFormShortName.getText());
                uomFormName.setText("");
                uomFormShortName.setText("");

                closeDialog(e);
            }
        });
    }
}
