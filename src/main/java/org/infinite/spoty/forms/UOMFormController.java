package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import io.github.palexdev.virtualizedfx.cell.Cell;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.database.models.UnitOfMeasure;
import org.infinite.spoty.viewModels.UOMViewModel;

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
//        uomFormBaseUnit.setItems(UOMViewModel.getItems());
        uomFormBaseUnit.setCellFactory(t -> new Cell<>() {
            @Override
            public Node getNode() {
                return null;
            }

            @Override
            public void updateItem(UnitOfMeasure item) {
                item.getName();
            }
        });

        uomFormName.textProperty().bindBidirectional(UOMViewModel.nameProperty());
        uomFormShortName.textProperty().bindBidirectional(UOMViewModel.shortNameProperty());
        uomFormBaseUnit.valueProperty().bindBidirectional(UOMViewModel.baseUnitProperty());
        uomFormOperator.textProperty().bindBidirectional(UOMViewModel.operatorProperty());

        // bind text and double property.
//                uomFormOperatorValue.textProperty().bind(Bindings.createStringBinding(
//                        () -> Double.toString(UOMViewModel.operatorValueProperty().get()),
//                        UOMViewModel.operatorValueProperty()
//                ));
        uomFormOperatorValue.textProperty().bindBidirectional(UOMViewModel.operatorValueProperty(), new NumberStringConverter());

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
                UOMViewModel.saveUOM();
                closeDialog(e);
            }
        });
    }
}
