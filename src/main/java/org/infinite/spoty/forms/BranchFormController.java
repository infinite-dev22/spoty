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
import static org.infinite.spoty.viewModels.BranchViewModel.clearBranchData;
import static org.infinite.spoty.viewModels.BranchViewModel.saveBranch;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.viewModels.BranchViewModel;

public class BranchFormController implements Initializable {
  private static BranchFormController instance;
  private final Stage stage;
  public MFXTextField branchFormID = new MFXTextField();
  @FXML public MFXButton branchFormSaveBtn;
  @FXML public MFXButton branchFormCancelBtn;
  @FXML public MFXTextField branchFormName;
  @FXML public MFXTextField branchFormEmail;
  @FXML public MFXTextField branchFormPhone;
  @FXML public MFXTextField branchFormTown;
  @FXML public MFXTextField branchFormCity;
  @FXML public MFXTextField branchFormZipCode;
  @FXML public Label branchFormEmailValidationLabel;
  @FXML public Label branchFormCityValidationLabel;
  @FXML public Label branchFormTownValidationLabel;
  @FXML public Label branchFormPhoneValidationLabel;
  @FXML public Label branchFormNameValidationLabel;

  private BranchFormController(Stage stage) {
    this.stage = stage;
  }

  public static BranchFormController getInstance(Stage stage) {
    if (Objects.equals(instance, null)) instance = new BranchFormController(stage);
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Input bindings.
    branchFormID
        .textProperty()
        .bindBidirectional(BranchViewModel.idProperty(), new NumberStringConverter());
    branchFormName.textProperty().bindBidirectional(BranchViewModel.nameProperty());
    branchFormEmail.textProperty().bindBidirectional(BranchViewModel.emailProperty());
    branchFormPhone.textProperty().bindBidirectional(BranchViewModel.phoneProperty());
    branchFormTown.textProperty().bindBidirectional(BranchViewModel.townProperty());
    branchFormCity.textProperty().bindBidirectional(BranchViewModel.cityProperty());
    branchFormZipCode.textProperty().bindBidirectional(BranchViewModel.zipcodeProperty());
    // Input listeners.
    requiredValidator(
        branchFormName, "Name is required.", branchFormNameValidationLabel, branchFormSaveBtn);
    requiredValidator(
        branchFormEmail, "Email is required.", branchFormEmailValidationLabel, branchFormSaveBtn);
    requiredValidator(
        branchFormPhone, "Phone is required.", branchFormPhoneValidationLabel, branchFormSaveBtn);
    requiredValidator(
        branchFormTown, "Town is required.", branchFormTownValidationLabel, branchFormSaveBtn);
    requiredValidator(
        branchFormCity, "City is required", branchFormCityValidationLabel, branchFormSaveBtn);
    dialogOnActions();
  }

  private void dialogOnActions() {
    branchFormCancelBtn.setOnAction(
        (e) -> {
          clearBranchData();

          closeDialog(e);

          branchFormNameValidationLabel.setVisible(false);
          branchFormEmailValidationLabel.setVisible(false);
          branchFormPhoneValidationLabel.setVisible(false);
          branchFormTownValidationLabel.setVisible(false);
          branchFormCityValidationLabel.setVisible(false);
        });
    branchFormSaveBtn.setOnAction(
        (e) -> {
          SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

          if (!branchFormNameValidationLabel.isVisible()
              && !branchFormEmailValidationLabel.isVisible()
              && !branchFormPhoneValidationLabel.isVisible()
              && !branchFormTownValidationLabel.isVisible()
              && !branchFormCityValidationLabel.isVisible()) {
            if (Integer.parseInt(branchFormID.getText()) > 0) {
              BranchViewModel.updateItem(Integer.parseInt(branchFormID.getText()));

              SimpleNotification notification =
                  new SimpleNotification.NotificationBuilder("Branch updated successfully")
                      .duration(NotificationDuration.SHORT)
                      .icon("fas-circle-check")
                      .type(NotificationVariants.SUCCESS)
                      .build();
              notificationHolder.addNotification(notification);

              closeDialog(e);
              return;
            }
            saveBranch();

            SimpleNotification notification =
                new SimpleNotification.NotificationBuilder("Branch saved successfully")
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
