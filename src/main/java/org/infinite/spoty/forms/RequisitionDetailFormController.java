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

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.viewModels.ProductDetailViewModel;
import org.infinite.spoty.viewModels.RequisitionDetailViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.requiredValidator;

public class RequisitionDetailFormController implements Initializable {
    public MFXTextField requisitionDetailID = new MFXTextField();
    @FXML
    public MFXTextField requisitionDetailQnty;
    @FXML
    public MFXFilterComboBox<ProductDetail> requisitionDetailPdct;
    @FXML
    public MFXFilledButton requisitionDetailSaveBtn;
    @FXML
    public MFXOutlinedButton requisitionDetailCancelBtn;
    @FXML
    public Label requisitionDetailTitle;
    @FXML
    public MFXTextField requisitionDetailDescription;
    @FXML
    public Label requisitionDetailPdctValidationLabel;
    @FXML
    public Label requisitionDetailQntyValidationLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input binding.
        requisitionDetailID.textProperty().bindBidirectional(RequisitionDetailViewModel.idProperty());
        requisitionDetailPdct.valueProperty().bindBidirectional(RequisitionDetailViewModel.productProperty());
        requisitionDetailQnty.textProperty().bindBidirectional(RequisitionDetailViewModel.quantityProperty());
        requisitionDetailDescription.textProperty().bindBidirectional(RequisitionDetailViewModel.descriptionProperty());
        // Combo box properties.
        requisitionDetailPdct.setItems(ProductDetailViewModel.productDetailsList);
        requisitionDetailPdct.setConverter(new StringConverter<>() {
            @Override
            public String toString(ProductDetail object) {
                if (object != null)
                    return object.getProduct().getName() + " " + object.getName();
                else
                    return null;
            }

            @Override
            public ProductDetail fromString(String string) {
                return null;
            }
        });
        // Input validators.
        requiredValidator(requisitionDetailPdct, "Product is required.", requisitionDetailPdctValidationLabel);
        requiredValidator(requisitionDetailQnty, "Quantity is required.", requisitionDetailQntyValidationLabel);
        dialogOnActions();
    }

    private void dialogOnActions() {
        requisitionDetailCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            RequisitionDetailViewModel.resetProperties();
            requisitionDetailPdctValidationLabel.setVisible(false);
            requisitionDetailQntyValidationLabel.setVisible(false);
        });
        requisitionDetailSaveBtn.setOnAction((e) -> {
            if (!requisitionDetailPdctValidationLabel.isVisible()
                    && !requisitionDetailQntyValidationLabel.isVisible()) {
                if (!requisitionDetailID.getText().isEmpty()) {
                    try {
                        if (Integer.parseInt(requisitionDetailID.getText()) > 0)
                            RequisitionDetailViewModel.updateItem(Integer.parseInt(requisitionDetailID.getText()));
                    } catch (NumberFormatException ignored) {
                        RequisitionDetailViewModel.updateRequisitionDetail(Integer.parseInt(requisitionDetailID.getText()
                                .substring(requisitionDetailID.getText().lastIndexOf(':') + 1,
                                        requisitionDetailID.getText().indexOf(';'))));
                    }
                } else RequisitionDetailViewModel.addRequisitionDetails();
                RequisitionDetailViewModel.resetProperties();
                closeDialog(e);
            }
        });
    }
}
