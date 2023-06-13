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
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.viewModels.ProductDetailViewModel;
import org.infinite.spoty.viewModels.TransferDetailViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class TransferDetailFormController implements Initializable {
    public MFXTextField transferDetailID = new MFXTextField();
    @FXML
    public MFXTextField transferDetailQnty;
    @FXML
    public MFXFilterComboBox<ProductDetail> transferDetailPdct;
    @FXML
    public MFXFilledButton transferDetailSaveBtn;
    @FXML
    public MFXOutlinedButton transferDetailCancelBtn;
    @FXML
    public Label transferDetailTitle;
    @FXML
    public MFXTextField transferDetailDescription;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input listeners.
        transferDetailPdct.textProperty().addListener((observable, oldValue, newValue) -> transferDetailPdct.setLeadingIcon(null));
        transferDetailQnty.textProperty().addListener((observable, oldValue, newValue) -> transferDetailQnty.setTrailingIcon(null));
        transferDetailDescription.textProperty().addListener((observable, oldValue, newValue) -> transferDetailDescription.setLeadingIcon(null));
        // Form input binding.
        transferDetailID.textProperty().bindBidirectional(TransferDetailViewModel.idProperty());
        transferDetailPdct.valueProperty().bindBidirectional(TransferDetailViewModel.productProperty());
        transferDetailQnty.textProperty().bindBidirectional(TransferDetailViewModel.quantityProperty());
        transferDetailDescription.textProperty().bindBidirectional(TransferDetailViewModel.descriptionProperty());
        // Combo box properties.
        transferDetailPdct.setItems(ProductDetailViewModel.purchaseDetailsList);
        transferDetailPdct.setConverter(new StringConverter<>() {
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
        dialogOnActions();
    }

    private void dialogOnActions() {
        transferDetailCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            TransferDetailViewModel.resetProperties();
            transferDetailPdct.setLeadingIcon(null);
            transferDetailQnty.setTrailingIcon(null);
            transferDetailDescription.setLeadingIcon(null);
        });
        transferDetailSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);
            if (transferDetailPdct.getText().length() == 0) {
                transferDetailPdct.setLeadingIcon(icon);
            }
            if (transferDetailQnty.getText().length() == 0) {
                transferDetailQnty.setTrailingIcon(icon);
            }
            if (transferDetailDescription.getText().length() == 0) {
                transferDetailDescription.setLeadingIcon(icon);
            }
            if (transferDetailQnty.getText().length() > 0
                    && transferDetailPdct.getText().length() > 0
                    && transferDetailDescription.getText().length() > 0) {
                if (!transferDetailID.getText().isEmpty()) {
                    try {
                        if (Integer.parseInt(transferDetailID.getText()) > 0)
                            TransferDetailViewModel.updateItem(Integer.parseInt(transferDetailID.getText()));
                    } catch (NumberFormatException ignored) {
                        TransferDetailViewModel.updateTransferDetail(Integer.parseInt(transferDetailID.getText()
                                .substring(transferDetailID.getText().lastIndexOf(':') + 1,
                                        transferDetailID.getText().indexOf(';'))));
                    }
                } else TransferDetailViewModel.addTransferDetails();
                TransferDetailViewModel.resetProperties();
                closeDialog(e);
            }
        });
    }
}
