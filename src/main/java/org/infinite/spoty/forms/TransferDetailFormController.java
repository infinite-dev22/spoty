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

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.requiredValidator;
import static org.infinite.spoty.values.SharedResources.tempIdProperty;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.viewModels.ProductDetailViewModel;
import org.infinite.spoty.viewModels.TransferDetailViewModel;

public class TransferDetailFormController implements Initializable {
  @FXML public MFXTextField transferDetailQnty;
  @FXML public MFXFilterComboBox<ProductDetail> transferDetailPdct;
  @FXML public MFXButton transferDetailSaveBtn;
  @FXML public MFXButton transferDetailCancelBtn;
  @FXML public Label transferDetailTitle;
  @FXML public MFXTextField transferDetailDescription;
  @FXML public Label transferDetailQntyValidationLabel;
  @FXML public Label transferDetailPdctValidationLabel;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Form input binding.
    transferDetailPdct.valueProperty().bindBidirectional(TransferDetailViewModel.productProperty());
    transferDetailQnty.textProperty().bindBidirectional(TransferDetailViewModel.quantityProperty());
    transferDetailDescription
        .textProperty()
        .bindBidirectional(TransferDetailViewModel.descriptionProperty());
    // Combo box properties.
    transferDetailPdct.setItems(ProductDetailViewModel.getProductDetails());
    transferDetailPdct.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(ProductDetail object) {
            return object != null
                ? object.getProduct().getName()
                    + " "
                    + (object.getUnit() != null
                        ? (object.getName().isEmpty() ? "" : object.getName())
                            + " "
                            + object.getUnit().getName()
                        : object.getName())
                : "No products";
          }

          @Override
          public ProductDetail fromString(String string) {
            return null;
          }
        });
    // Input validators.
    requiredValidator(
        transferDetailPdct, "Product is required.", transferDetailPdctValidationLabel);
    requiredValidator(
        transferDetailQnty, "Quantity is required.", transferDetailQntyValidationLabel);
    dialogOnActions();
  }

  private void dialogOnActions() {
    transferDetailCancelBtn.setOnAction(
        (e) -> {
          closeDialog(e);
          TransferDetailViewModel.resetProperties();
          transferDetailPdctValidationLabel.setVisible(false);
          transferDetailQntyValidationLabel.setVisible(false);
        });
    transferDetailSaveBtn.setOnAction(
        (e) -> {
          SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();
          if (!transferDetailPdctValidationLabel.isVisible()
              && !transferDetailQntyValidationLabel.isVisible()) {
            if (tempIdProperty().get() > -1) {
              TransferDetailViewModel.updateTransferDetail(TransferDetailViewModel.getId());
              SimpleNotification notification =
                  new SimpleNotification.NotificationBuilder("Product changed successfully")
                      .duration(NotificationDuration.SHORT)
                      .icon("fas-circle-check")
                      .type(NotificationVariants.SUCCESS)
                      .build();
              notificationHolder.addNotification(notification);
              closeDialog(e);
              return;
            }
            TransferDetailViewModel.addTransferDetails();
            SimpleNotification notification =
                new SimpleNotification.NotificationBuilder("Product added successfully")
                    .duration(NotificationDuration.SHORT)
                    .icon("fas-circle-check")
                    .type(NotificationVariants.SUCCESS)
                    .build();
            notificationHolder.addNotification(notification);
            closeDialog(e);
            return;
            //
            //                if (!transferDetailID.getText().isEmpty()) {
            //                    try {
            //                        if (Integer.parseInt(transferDetailID.getText()) > 0)
            //
            // TransferDetailViewModel.updateItem(Integer.parseInt(transferDetailID.getText()));
            //                    } catch (NumberFormatException ignored) {
            //
            // TransferDetailViewModel.updateTransferDetail(TransferDetailViewModel.getId());
            //                    }
            //                } else TransferDetailViewModel.addTransferDetails();
          }
          SimpleNotification notification =
              new SimpleNotification.NotificationBuilder("Required fields missing")
                  .duration(NotificationDuration.SHORT)
                  .icon("fas-triangle-exclamation")
                  .type(NotificationVariants.ERROR)
                  .build();
          notificationHolder.addNotification(notification);
        });
    transferDetailPdct.clearSelection();
  }
}
