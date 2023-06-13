/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.viewModels.ExpenseCategoryViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.viewModels.ExpenseCategoryViewModel.resetProperties;
import static org.infinite.spoty.viewModels.ExpenseCategoryViewModel.saveExpenseCategory;

public class ExpenseCategoryFormController implements Initializable {
    public MFXTextField expenseCategoryID = new MFXTextField();
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
        expenseCategoryID.textProperty().bindBidirectional(ExpenseCategoryViewModel.idProperty(), new NumberStringConverter());
        categoryExpenseFormName.textProperty().bindBidirectional(ExpenseCategoryViewModel.nameProperty());
        categoryExpenseFormDescription.textProperty().bindBidirectional(ExpenseCategoryViewModel.descriptionProperty());
        dialogOnActions();
    }

    private void dialogOnActions() {
        categoryExpenseFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            resetProperties();
            categoryExpenseFormName.setTrailingIcon(null);
            categoryExpenseFormDescription.setTrailingIcon(null);
        });
        categoryExpenseFormSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (categoryExpenseFormName.getText().length() == 0) {
                categoryExpenseFormName.setTrailingIcon(icon);
            }
            if (categoryExpenseFormDescription.getText().length() == 0) {
                categoryExpenseFormDescription.setTrailingIcon(icon);
            }
            if (categoryExpenseFormName.getText().length() > 0 && categoryExpenseFormDescription.getText().length() > 0) {
                if (Integer.parseInt(expenseCategoryID.getText()) > 0)
                    ExpenseCategoryViewModel.updateItem(Integer.parseInt(expenseCategoryID.getText()));
                else
                    saveExpenseCategory();
                resetProperties();
                closeDialog(e);
            }
        });
    }
}
