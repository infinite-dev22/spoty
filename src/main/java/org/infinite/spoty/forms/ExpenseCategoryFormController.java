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
import static org.infinite.spoty.viewModels.ExpenseCategoryViewModel.resetProperties;
import static org.infinite.spoty.viewModels.ExpenseCategoryViewModel.saveExpenseCategory;

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
import org.infinite.spoty.viewModels.ExpenseCategoryViewModel;
import org.infinite.spoty.views.expenses.category.ExpenseCategoryController;

public class ExpenseCategoryFormController implements Initializable {
  private static ExpenseCategoryFormController instance;
  private final Stage stage;
  public MFXTextField expenseCategoryID = new MFXTextField();
  @FXML public MFXTextField categoryExpenseFormName;
  @FXML public MFXTextField categoryExpenseFormDescription;
  @FXML public MFXButton categoryExpenseFormSaveBtn;
  @FXML public MFXButton categoryExpenseFormCancelBtn;
  @FXML public Label categoryExpenseFormTitle;
  @FXML public Label categoryExpenseFormNameValidationLabel;

  private ExpenseCategoryFormController(Stage stage) {
    this.stage = stage;
  }

  public static ExpenseCategoryFormController getInstance(Stage stage) {
    if (Objects.equals(instance, null)) instance = new ExpenseCategoryFormController(stage);
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Input bindings.
    expenseCategoryID
        .textProperty()
        .bindBidirectional(ExpenseCategoryViewModel.idProperty(), new NumberStringConverter());
    categoryExpenseFormName
        .textProperty()
        .bindBidirectional(ExpenseCategoryViewModel.nameProperty());
    categoryExpenseFormDescription
        .textProperty()
        .bindBidirectional(ExpenseCategoryViewModel.descriptionProperty());
    // Input listeners.
    requiredValidator(
        categoryExpenseFormName,
        "Category name is required.",
        categoryExpenseFormNameValidationLabel);
    dialogOnActions();
  }

  private void dialogOnActions() {
    categoryExpenseFormCancelBtn.setOnAction(
        (e) -> {
          closeDialog(e);
          resetProperties();
          categoryExpenseFormNameValidationLabel.setVisible(false);
        });
    categoryExpenseFormSaveBtn.setOnAction(
        (e) -> {
          SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

          if (!categoryExpenseFormNameValidationLabel.isVisible()) {
            if (Integer.parseInt(expenseCategoryID.getText()) > 0) {
              ExpenseCategoryViewModel.updateItem(Integer.parseInt(expenseCategoryID.getText()));

              SimpleNotification notification =
                  new SimpleNotification.NotificationBuilder("Category updated successfully")
                      .duration(NotificationDuration.SHORT)
                      .icon("fas-circle-check")
                      .type(NotificationVariants.SUCCESS)
                      .build();
              notificationHolder.addNotification(notification);

              ExpenseCategoryController.getInstance(stage)
                  .categoryExpenseTable
                  .setItems(ExpenseCategoryViewModel.getCategoryList());

              closeDialog(e);
              return;
            }
            saveExpenseCategory();
            SimpleNotification notification =
                new SimpleNotification.NotificationBuilder("Category saved successfully")
                    .duration(NotificationDuration.SHORT)
                    .icon("fas-circle-check")
                    .type(NotificationVariants.SUCCESS)
                    .build();
            notificationHolder.addNotification(notification);

            ExpenseCategoryController.getInstance(stage)
                .categoryExpenseTable
                .setItems(ExpenseCategoryViewModel.getCategoryList());

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
