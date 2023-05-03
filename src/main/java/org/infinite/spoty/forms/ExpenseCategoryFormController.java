package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.infinite.spoty.models.ExpenseCategory;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class ExpenseCategoryFormController implements Initializable {
    @FXML
    public MFXTextField categoryExpenseFormName;
    @FXML
    public MFXTextField categoryExpenseFormDescription;
    @FXML
    public MFXFilledButton categoryExpenseFormSaveBtn;
    @FXML
    public MFXOutlinedButton categoryExpenseFormCancelBtn;
    @FXML
    public Label categoryExpenseFormTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogOnActions();
    }

    private void dialogOnActions() {
        categoryExpenseFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            categoryExpenseFormName.setText("");
            categoryExpenseFormDescription.setText("");
            categoryExpenseFormName.setTrailingIcon(null);
            categoryExpenseFormDescription.setTrailingIcon(null);
        });
        categoryExpenseFormSaveBtn.setOnAction((e) -> {
            ExpenseCategory expenseCategory = new ExpenseCategory();
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (categoryExpenseFormName.getText().length() == 0) {
                categoryExpenseFormName.setTrailingIcon(icon);
            }
            if (categoryExpenseFormDescription.getText().length() == 0) {
                categoryExpenseFormDescription.setTrailingIcon(icon);
            }
            if (categoryExpenseFormName.getText().length() > 0 && categoryExpenseFormDescription.getText().length() > 0) {
                expenseCategory.setCategoryName(categoryExpenseFormName.getText());
                expenseCategory.setCategoryDescription(categoryExpenseFormDescription.getText());
                categoryExpenseFormName.setText("");
                categoryExpenseFormDescription.setText("");

                closeDialog(e);
            }
        });
    }
}
