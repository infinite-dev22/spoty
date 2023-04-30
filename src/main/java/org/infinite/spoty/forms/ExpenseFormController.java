package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.infinite.spoty.model.ExpenseCategory;

import java.net.URL;
import java.util.ResourceBundle;

public class ExpenseFormController implements Initializable {
    @FXML
    public MFXTextField expenseFormAmount;
    @FXML
    public MFXTextField expenseFormDetails;
    @FXML
    public MFXFilledButton expenseFormSaveBtn;
    @FXML
    public MFXOutlinedButton expenseFormCancelBtn;
    @FXML
    public Label expenseFormTitle;
    @FXML
    public MFXDatePicker expenseFormDate;
    @FXML
    public MFXComboBox<?> expenseFormWarehouse;
    @FXML
    public MFXComboBox<ExpenseCategory> expenseFormCategory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogOnActions();
    }

    private void dialogOnActions() {
        expenseFormCancelBtn.setOnAction((e) -> {
            final Node source = (Node) e.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        expenseFormSaveBtn.setOnAction((e) -> {});
    }
}
