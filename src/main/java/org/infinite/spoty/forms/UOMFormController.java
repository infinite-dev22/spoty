package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.infinite.spoty.model.UnitOfMeasure;

import java.net.URL;
import java.util.ResourceBundle;

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
        setUomFormDialogOnActions();
    }

    private void setUomFormDialogOnActions() {
        uomFormCancelBtn.setOnAction((e) -> {
            final Node source = (Node) e.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        uomFormSaveBtn.setOnAction((e) -> {});
    }
}
