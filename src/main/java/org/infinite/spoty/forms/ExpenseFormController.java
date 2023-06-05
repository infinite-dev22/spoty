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
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.ExpenseCategory;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.ExpenseCategoryViewModel;
import org.infinite.spoty.viewModels.ExpenseViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class ExpenseFormController implements Initializable {
    public MFXTextField expenseID = new MFXTextField();
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
    @FXML
    public MFXTextField expenseFormName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form event listeners.
        expenseFormName.textProperty().addListener((observable, oldValue, newValue) -> expenseFormName.setTrailingIcon(null));
        expenseFormDate.textProperty().addListener((observable, oldValue, newValue) -> expenseFormDate.setLeadingIcon(null));
        expenseFormBranch.textProperty().addListener((observable, oldValue, newValue) -> expenseFormBranch.setLeadingIcon(null));
        expenseFormCategory.textProperty().addListener((observable, oldValue, newValue) -> expenseFormCategory.setLeadingIcon(null));
        expenseFormAmount.textProperty().addListener((observable, oldValue, newValue) -> expenseFormAmount.setTrailingIcon(null));
        expenseFormDetails.textProperty().addListener((observable, oldValue, newValue) -> expenseFormDetails.setTrailingIcon(null));
        // Combo box properties.
        expenseFormBranch.setItems(BranchViewModel.branchesList);
        expenseFormCategory.setItems(ExpenseCategoryViewModel.categoryList);
        // Set Object property as combo display name.
        expenseFormBranch.setConverter(new StringConverter<>() {
            @Override
            public String toString(Branch object) {
                if (object != null)
                    return object.getName();
                else
                    return null;
            }

            @Override
            public Branch fromString(String string) {
                return null;
            }
        });
        expenseFormCategory.setConverter(new StringConverter<>() {
            @Override
            public String toString(ExpenseCategory object) {
                if (object != null)
                    return object.getName();
                else
                    return null;
            }

            @Override
            public ExpenseCategory fromString(String string) {
                return null;
            }
        });
        // Form Bindings.
        expenseID.textProperty().bindBidirectional(ExpenseViewModel.idProperty(), new NumberStringConverter());
        expenseFormName.textProperty().bindBidirectional(ExpenseViewModel.nameProperty());
        expenseFormDate.textProperty().bindBidirectional(ExpenseViewModel.dateProperty());
        expenseFormBranch.valueProperty().bindBidirectional(ExpenseViewModel.branchProperty());
        expenseFormCategory.valueProperty().bindBidirectional(ExpenseViewModel.categoryProperty());
        expenseFormAmount.textProperty().bindBidirectional(ExpenseViewModel.amountProperty());
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
                if (Integer.parseInt(expenseID.getText()) > 0)
                    ExpenseViewModel.updateItem(Integer.parseInt(expenseID.getText()));
                else
                    ExpenseViewModel.saveExpense();
                closeDialog(e);
            }
        });
    }
}
