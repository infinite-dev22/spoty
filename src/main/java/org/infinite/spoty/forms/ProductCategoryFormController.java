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

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.viewModels.ProductCategoryViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.requiredValidator;
import static org.infinite.spoty.viewModels.ProductCategoryViewModel.*;

public class ProductCategoryFormController implements Initializable {
    @FXML
    public static Label formTitle;
    public MFXTextField dialogCategoryID = new MFXTextField();
    @FXML
    public MFXTextField dialogCategoryCode;
    @FXML
    public MFXTextField dialogCategoryName;
    @FXML
    public MFXFilledButton dialogSaveBtn;
    @FXML
    public MFXOutlinedButton dialogCancelBtn;
    @FXML
    public Label dialogCategoryCodeValidationLabel;
    @FXML
    public Label dialogCategoryNameValidationLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        dialogCategoryID.textProperty().bindBidirectional(ProductCategoryViewModel.idProperty(), new NumberStringConverter());
        dialogCategoryCode.textProperty().bindBidirectional(ProductCategoryViewModel.codeProperty());
        dialogCategoryName.textProperty().bindBidirectional(ProductCategoryViewModel.nameProperty());
        //Input validators.
        requiredValidator(dialogCategoryCode, "Name field is required.", dialogCategoryCodeValidationLabel);
        requiredValidator(dialogCategoryName, "Name field is required.", dialogCategoryNameValidationLabel);
        dialogOnActions();
    }

    private void dialogOnActions() {
        dialogCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            clearProductCategoryData();
            dialogCategoryCodeValidationLabel.setVisible(false);
            dialogCategoryNameValidationLabel.setVisible(false);
        });
        dialogSaveBtn.setOnAction((e) -> {
            if (!dialogCategoryCodeValidationLabel.isVisible()
                    && !dialogCategoryNameValidationLabel.isVisible()) {
                if (Integer.parseInt(dialogCategoryID.getText()) > 0)
                    updateItem(Integer.parseInt(dialogCategoryID.getText()));
                else
                    saveProductCategory();
                closeDialog(e);
            }
        });
    }
}
