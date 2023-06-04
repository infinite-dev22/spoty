package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.database.models.UnitOfMeasure;
import org.infinite.spoty.viewModels.UOMViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class UOMFormController implements Initializable {
    public MFXTextField uomID = new MFXTextField();
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
    @FXML
    public MFXTextField uomFormOperator;
    @FXML
    public MFXTextField uomFormOperatorValue;
    @FXML
    public VBox formsHolder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        uomFormName.textProperty().addListener((observable, oldValue, newValue) -> uomFormName.setTrailingIcon(null));
        uomFormShortName.textProperty().addListener((observable, oldValue, newValue) -> uomFormShortName.setTrailingIcon(null));
        uomFormBaseUnit.setItems(UOMViewModel.uomList);
        uomFormBaseUnit.setConverter(new StringConverter<>() {
            @Override
            public String toString(UnitOfMeasure object) {
                if (object != null)
                    return object.getName();
                else
                    return null;
            }

            @Override
            public UnitOfMeasure fromString(String string) {
                return null;
            }
        });

        uomID.textProperty().bindBidirectional(UOMViewModel.idProperty(), new NumberStringConverter());
        uomFormName.textProperty().bindBidirectional(UOMViewModel.nameProperty());
        uomFormShortName.textProperty().bindBidirectional(UOMViewModel.shortNameProperty());
        uomFormBaseUnit.valueProperty().bindBidirectional(UOMViewModel.baseUnitProperty());
        uomFormOperator.textProperty().bindBidirectional(UOMViewModel.operatorProperty());

        uomFormOperatorValue.textProperty().bindBidirectional(UOMViewModel.operatorValueProperty());

        uomFormBaseUnit.valueProperty().addListener(e -> {
            if (!uomFormBaseUnit.getItems().isEmpty()) {
                formsHolder.setVisible(true);
                formsHolder.setManaged(true);
            } else {
                formsHolder.setManaged(false);
                formsHolder.setVisible(false);
            }
        });

        setUomFormDialogOnActions();
    }

    private void setUomFormDialogOnActions() {
        uomFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            UOMViewModel.resetUOMProperties();
            uomFormName.setTrailingIcon(null);
            uomFormShortName.setTrailingIcon(null);
            formsHolder.setVisible(false);
            formsHolder.setManaged(false);
        });
        uomFormSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (uomFormName.getText().length() == 0) {
                uomFormName.setTrailingIcon(icon);
            }
            if (uomFormShortName.getText().length() == 0) {
                uomFormShortName.setTrailingIcon(icon);
            }
            if (uomFormName.getText().length() > 0 && uomFormShortName.getText().length() > 0) {
                if (Integer.parseInt(uomID.getText()) > 0)
                    UOMViewModel.updateItem(Integer.parseInt(uomID.getText()));
                else
                    UOMViewModel.saveUOM();
                closeDialog(e);
            }
        });
    }
}
