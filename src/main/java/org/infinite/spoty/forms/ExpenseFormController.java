package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.infinite.spoty.models.Branch;
import org.infinite.spoty.models.Expense;
import org.infinite.spoty.models.ExpenseCategory;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

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
    public MFXComboBox<Branch> expenseFormBranch;
    @FXML
    public MFXComboBox<ExpenseCategory> expenseFormCategory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogOnActions();
    }

    private void dialogOnActions() {
        expenseFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            expenseFormDate.setText("");
            expenseFormBranch.setText("");
            expenseFormCategory.setText("");
            expenseFormAmount.setText("");
            expenseFormDetails.setText("");
        });
        expenseFormSaveBtn.setOnAction((e) -> {
            Expense expense = new Expense();
            String dateFormat = "yyyy-MM-dd";
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (expenseFormDate.getText().length() == 0) {
                expenseFormDate.setTrailingIcon(icon);
            }
            if (expenseFormBranch.getText().length() == 0) {
                expenseFormBranch.setLeadingIcon(icon);
            }
            if (expenseFormCategory.getText().length() == 0) {
                expenseFormCategory.setLeadingIcon(icon);
            }
            if (expenseFormAmount.getText().length() == 0) {
                expenseFormAmount.setTrailingIcon(icon);
            }
            if (expenseFormDetails.getText().length() == 0) {
                expenseFormDetails.setTrailingIcon(icon);
            }
            if (expenseFormDate.getText().length() > 0
                    && expenseFormBranch.getText().length() > 0
                    && expenseFormCategory.getText().length() > 0
                    && expenseFormAmount.getText().length() > 0
                    && expenseFormDetails.getText().length() > 0) {
                try {
                    expense.setExpenseDate(new SimpleDateFormat(dateFormat).parse(expenseFormDate.getText()));
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                expense.setExpenseBranch(expenseFormBranch.getText());
                expense.setExpenseCategory(expenseFormCategory.getText());
                expense.setExpenseAmount(Double.parseDouble(expenseFormAmount.getText()));
                expense.setExpenseDetails(expenseFormDetails.getText());

                closeDialog(e);
            }
        });
    }
}
