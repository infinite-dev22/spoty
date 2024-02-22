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

package inc.normad.spoty.core.views.forms;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import inc.normad.spoty.core.components.notification.SimpleNotification;
import inc.normad.spoty.core.components.notification.SimpleNotificationHolder;
import inc.normad.spoty.core.components.notification.enums.NotificationDuration;
import inc.normad.spoty.core.components.notification.enums.NotificationVariants;
import inc.normad.spoty.utils.SpotyLogger;
import inc.normad.spoty.utils.SpotyThreader;
import inc.normad.spoty.core.viewModels.ExpenseCategoryViewModel;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static inc.normad.spoty.core.GlobalActions.closeDialog;
import static inc.normad.spoty.core.Validators.requiredValidator;
import static inc.normad.spoty.core.viewModels.ExpenseCategoryViewModel.resetProperties;
import static inc.normad.spoty.core.viewModels.ExpenseCategoryViewModel.saveExpenseCategory;

public class ExpenseCategoryFormController implements Initializable {
    private static ExpenseCategoryFormController instance;
    @FXML
    public MFXTextField categoryExpenseFormName;
    @FXML
    public MFXTextField categoryExpenseFormDescription;
    @FXML
    public MFXButton categoryExpenseFormSaveBtn;
    @FXML
    public MFXButton categoryExpenseFormCancelBtn;
    @FXML
    public Label categoryExpenseFormNameValidationLabel;

    public static ExpenseCategoryFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new ExpenseCategoryFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
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
                categoryExpenseFormNameValidationLabel,
                categoryExpenseFormSaveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        categoryExpenseFormCancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);

                    categoryExpenseFormNameValidationLabel.setVisible(false);
                });
        categoryExpenseFormSaveBtn.setOnAction(
                (event) -> {
                    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

                    if (!categoryExpenseFormNameValidationLabel.isVisible()) {
                        if (ExpenseCategoryViewModel.getId() > 0) {
                            SpotyThreader.spotyThreadPool(() -> {
                                try {
                                    ExpenseCategoryViewModel.updateItem(this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            });

                            SimpleNotification notification =
                                    new SimpleNotification.NotificationBuilder("Category updated successfully")
                                            .duration(NotificationDuration.SHORT)
                                            .icon("fas-circle-check")
                                            .type(NotificationVariants.SUCCESS)
                                            .build();
                            notificationHolder.addNotification(notification);

                            closeDialog(event);
                            return;
                        }
                        SpotyThreader.spotyThreadPool(() -> {
                            try {
                                saveExpenseCategory(this::onAction, this::onSuccess, this::onFailed);
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });
                        SimpleNotification notification =
                                new SimpleNotification.NotificationBuilder("Category saved successfully")
                                        .duration(NotificationDuration.SHORT)
                                        .icon("fas-circle-check")
                                        .type(NotificationVariants.SUCCESS)
                                        .build();
                        notificationHolder.addNotification(notification);

                        closeDialog(event);
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

    private void onAction() {
        System.out.println("Loading expense category...");
    }

    private void onSuccess() {
        System.out.println("Loaded expense category...");
    }

    private void onFailed() {
        System.out.println("failed loading expense category...");
    }
}
