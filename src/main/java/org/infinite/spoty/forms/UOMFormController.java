package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.infinite.spoty.database.models.Brand;
import org.infinite.spoty.database.models.UnitOfMeasure;
import org.infinite.spoty.viewModels.UOMFormViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.viewModels.BrandFormViewModel.saveBrand;

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
    public MFXTextField uomFormOperator;
    public MFXTextField uomFormOperatorValue;
    @FXML
    public VBox formsHolder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        uomFormName.textProperty().addListener((observable, oldValue, newValue) -> uomFormName.setTrailingIcon(null));
        uomFormShortName.textProperty().addListener((observable, oldValue, newValue) -> uomFormShortName.setTrailingIcon(null));
        uomFormBaseUnit.setItems(UOMFormViewModel.getItems());

        uomFormName.textProperty().bindBidirectional(UOMFormViewModel.nameProperty());
        uomFormShortName.textProperty().bindBidirectional(UOMFormViewModel.shortNameProperty());
        uomFormBaseUnit.valueProperty().bindBidirectional(UOMFormViewModel.baseUnitProperty());

        uomFormBaseUnit.valueProperty().addListener(e -> {
            if (!uomFormBaseUnit.getItems().isEmpty()) {
                uomFormOperator = new MFXTextField("Operator");
                uomFormOperatorValue = new MFXTextField("Operator Value");

                uomFormOperator.setPrefWidth(400);
                uomFormOperatorValue.setPrefWidth(400);

                uomFormOperator.setFloatMode(FloatMode.BORDER);
                uomFormOperatorValue.setFloatMode(FloatMode.BORDER);

                formsHolder.getChildren().addAll(uomFormOperator, uomFormOperatorValue);

                uomFormOperator.textProperty().bindBidirectional(UOMFormViewModel.operatorProperty());

                // bind text and double property.
                uomFormOperatorValue.textProperty().bind(Bindings.createStringBinding(
                        () -> Double.toString(UOMFormViewModel.operatorValueProperty().get()),
                        UOMFormViewModel.operatorValueProperty()
                ));
            } else {
                formsHolder.getChildren().removeAll(uomFormOperator, uomFormOperatorValue);
            }
        });

        setUomFormDialogOnActions();
    }

    private void setUomFormDialogOnActions() {
        uomFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            UOMFormViewModel.resetUOMProperties();
            uomFormName.setTrailingIcon(null);
            uomFormShortName.setTrailingIcon(null);
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
                UOMFormViewModel.saveUOM();
                closeDialog(e);
            }
        });
    }
}
