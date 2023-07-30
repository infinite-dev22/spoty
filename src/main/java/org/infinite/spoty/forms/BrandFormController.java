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
import static org.infinite.spoty.viewModels.BrandViewModel.clearBrandData;
import static org.infinite.spoty.viewModels.BrandViewModel.saveBrand;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.viewModels.BrandViewModel;

public class BrandFormController implements Initializable {
  private static BrandFormController instance;
  @FXML public MFXTextField brandFormName;
  @FXML public MFXTextField brandFormDescription;
  @FXML public MFXButton brandFormSaveBtn;
  @FXML public MFXButton brandFormCancelBtn;
  @FXML public Label brandFormNameValidationLabel;
  @FXML public Label brandFormDescriptionValidationLabel;

  public static BrandFormController getInstance() {
    if (Objects.equals(instance, null)) instance = new BrandFormController();
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Input bindings.
    brandFormName.textProperty().bindBidirectional(BrandViewModel.nameProperty());
    brandFormDescription.textProperty().bindBidirectional(BrandViewModel.descriptionProperty());
    // Input listeners.
    requiredValidator(
        brandFormName, "Brand name is required.", brandFormNameValidationLabel, brandFormSaveBtn);
    dialogOnActions();
  }

  private void dialogOnActions() {
    brandFormCancelBtn.setOnAction(
        (e) -> {
          closeDialog(e);

          clearBrandData();

          brandFormNameValidationLabel.setVisible(false);
        });
    brandFormSaveBtn.setOnAction(
        (e) -> {
          SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

          if (!brandFormNameValidationLabel.isVisible()) {
            if (BrandViewModel.getId() > 0) {
              BrandViewModel.updateItem(BrandViewModel.getId());

              SimpleNotification notification =
                  new SimpleNotification.NotificationBuilder("Brand updated successfully")
                      .duration(NotificationDuration.SHORT)
                      .icon("fas-circle-check")
                      .type(NotificationVariants.SUCCESS)
                      .build();
              notificationHolder.addNotification(notification);

              closeDialog(e);
              return;
            }
            saveBrand();

            SimpleNotification notification =
                new SimpleNotification.NotificationBuilder("Brand saved successfully")
                    .duration(NotificationDuration.SHORT)
                    .icon("fas-circle-check")
                    .type(NotificationVariants.SUCCESS)
                    .build();
            notificationHolder.addNotification(notification);

            closeDialog(e);
            return;
          }
          SimpleNotification notification =
              new SimpleNotification.NotificationBuilder("Required fields missing")
                  .duration(NotificationDuration.SHORT)
                  .icon("fas-triangle-exclamation")
                  .type(NotificationVariants.ERROR)
                  .build();
          notificationHolder.addNotification(notification);
        });
  }
}
