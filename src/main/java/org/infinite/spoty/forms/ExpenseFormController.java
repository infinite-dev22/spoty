package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.Expense;
import org.infinite.spoty.database.models.ExpenseCategory;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.ExpenseCategoryViewModel;
import org.infinite.spoty.viewModels.ExpenseViewModel;

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
        expenseFormBranch.setItems(BranchViewModel.getBranches());
        expenseFormCategory.setItems(ExpenseCategoryViewModel.getCategories());

        expenseFormDate.textProperty().bindBidirectional(ExpenseViewModel.dateProperty());
        expenseFormBranch.valueProperty().bindBidirectional(ExpenseViewModel.branchProperty());
        expenseFormCategory.valueProperty().bindBidirectional(ExpenseViewModel.categoryProperty());
        expenseFormAmount.textProperty().bindBidirectional(ExpenseViewModel.amountProperty(), new NumberStringConverter());
        expenseFormDetails.textProperty().bindBidirectional(ExpenseViewModel.detailsProperty());

        dialogOnActions();
    }

    private void dialogOnActions() {
        expenseFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            ExpenseViewModel.resetProperties();
        });
        expenseFormSaveBtn.setOnAction((e) -> {
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
                ExpenseViewModel.saveExpense();
                closeDialog(e);
            }
        });
    }
}
