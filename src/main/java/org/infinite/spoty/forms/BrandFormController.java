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
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.viewModels.BrandViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.viewModels.BrandViewModel.clearBrandData;
import static org.infinite.spoty.viewModels.BrandViewModel.saveBrand;

public class BrandFormController implements Initializable {
    public MFXTextField brandID = new MFXTextField();
    public Label brandFormTitle;
    public MFXTextField brandFormName;
    public MFXTextField brandFormDescription;
    public MFXFilledButton brandFormSaveBtn;
    public MFXOutlinedButton brandFormCancelBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        brandFormName.textProperty().addListener((observable, oldValue, newValue) -> brandFormName.setTrailingIcon(null));
        brandFormDescription.textProperty().addListener((observable, oldValue, newValue) -> brandFormDescription.setTrailingIcon(null));

        brandID.textProperty().bindBidirectional(BrandViewModel.idProperty(), new NumberStringConverter());
        brandFormName.textProperty().bindBidirectional(BrandViewModel.nameProperty());
        brandFormDescription.textProperty().bindBidirectional(BrandViewModel.descriptionProperty());

        dialogOnActions();
    }

    private void dialogOnActions() {
        brandFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            clearBrandData();
            brandFormName.setTrailingIcon(null);
            brandFormDescription.setTrailingIcon(null);
        });
        brandFormSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (brandFormName.getText().length() == 0) {
                brandFormName.setTrailingIcon(icon);
            }
            if (brandFormDescription.getText().length() == 0) {
                brandFormDescription.setTrailingIcon(icon);
            }
            if (brandFormName.getText().length() > 0 && brandFormDescription.getText().length() > 0) {
                if (Integer.parseInt(brandID.getText()) > 0)
                    BrandViewModel.updateItem(Integer.parseInt(brandID.getText()));
                else
                    saveBrand();
                clearBrandData();
                closeDialog(e);
            }
        });
    }
}
