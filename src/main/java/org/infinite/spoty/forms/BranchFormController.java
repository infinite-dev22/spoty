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
import org.infinite.spoty.viewModels.BranchViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.requiredValidator;
import static org.infinite.spoty.viewModels.BranchViewModel.clearBranchData;
import static org.infinite.spoty.viewModels.BranchViewModel.saveBranch;

public class BranchFormController implements Initializable {
    public MFXTextField branchFormID = new MFXTextField();
    @FXML
    public MFXFilledButton branchFormSaveBtn;
    @FXML
    public MFXOutlinedButton branchFormCancelBtn;
    @FXML
    public Label branchFormTitle;
    @FXML
    public MFXTextField branchFormName;
    @FXML
    public MFXTextField branchFormEmail;
    @FXML
    public MFXTextField branchFormPhone;
    @FXML
    public MFXTextField branchFormTown;
    @FXML
    public MFXTextField branchFormCity;
    @FXML
    public MFXTextField branchFormZipCode;
    @FXML
    public Label branchFormEmailValidationLabel;
    @FXML
    public Label branchFormCityValidationLabel;
    @FXML
    public Label branchFormTownValidationLabel;
    @FXML
    public Label branchFormPhoneValidationLabel;
    @FXML
    public Label branchFormNameValidationLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        branchFormID.textProperty().bindBidirectional(BranchViewModel.idProperty(), new NumberStringConverter());
        branchFormName.textProperty().bindBidirectional(BranchViewModel.nameProperty());
        branchFormEmail.textProperty().bindBidirectional(BranchViewModel.emailProperty());
        branchFormPhone.textProperty().bindBidirectional(BranchViewModel.phoneProperty());
        branchFormTown.textProperty().bindBidirectional(BranchViewModel.townProperty());
        branchFormCity.textProperty().bindBidirectional(BranchViewModel.cityProperty());
        branchFormZipCode.textProperty().bindBidirectional(BranchViewModel.zipcodeProperty());
        // Input listeners.
        requiredValidator(branchFormName, "Name is required.", branchFormNameValidationLabel);
        requiredValidator(branchFormEmail, "Email is required.", branchFormEmailValidationLabel);
        requiredValidator(branchFormPhone, "Phone is required.", branchFormPhoneValidationLabel);
        requiredValidator(branchFormTown, "Town is required.", branchFormTownValidationLabel);
        requiredValidator(branchFormCity, "City is required", branchFormCityValidationLabel);
        dialogOnActions();
    }

    private void dialogOnActions() {
        branchFormCancelBtn.setOnAction((e) -> {
            clearBranchData();
            closeDialog(e);
            branchFormNameValidationLabel.setVisible(false);
            branchFormEmailValidationLabel.setVisible(false);
            branchFormPhoneValidationLabel.setVisible(false);
            branchFormTownValidationLabel.setVisible(false);
            branchFormCityValidationLabel.setVisible(false);
        });
        branchFormSaveBtn.setOnAction((e) -> {
            if (!branchFormNameValidationLabel.isVisible()
                    && !branchFormEmailValidationLabel.isVisible()
                    && !branchFormPhoneValidationLabel.isVisible()
                    && !branchFormTownValidationLabel.isVisible()
                    && !branchFormCityValidationLabel.isVisible()) {
                if (Integer.parseInt(branchFormID.getText()) > 0)
                    BranchViewModel.updateItem(Integer.parseInt(branchFormID.getText()));
                else
                    saveBranch();
                closeDialog(e);
            }
        });
    }
}
