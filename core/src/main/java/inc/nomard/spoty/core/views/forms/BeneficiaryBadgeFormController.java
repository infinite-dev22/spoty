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

package inc.nomard.spoty.core.views.forms;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import inc.nomard.spoty.core.components.notification.SimpleNotification;
import inc.nomard.spoty.core.components.notification.SimpleNotificationHolder;
import inc.nomard.spoty.core.components.notification.enums.NotificationDuration;
import inc.nomard.spoty.core.components.notification.enums.NotificationVariants;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.BeneficiaryType;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import inc.nomard.spoty.core.viewModels.hrm.pay_roll.BeneficiaryBadgeViewModel;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static inc.nomard.spoty.core.Validators.requiredValidator;

public class BeneficiaryBadgeFormController implements Initializable {
    private static BeneficiaryBadgeFormController instance;
    @FXML
    public MFXButton saveBtn;
    @FXML
    public MFXButton cancelBtn;
    @FXML
    public MFXFilterComboBox<BeneficiaryType> beneficiaryType;
    @FXML
    public Label beneficiaryTypeValidationLabel;
    @FXML
    public MFXTextField name;
    @FXML
    public Label nameValidationLabel;
    @FXML
    public MFXTextField description;
    @FXML
    public Label descriptionValidationLabel;
    @FXML
    public ColorPicker color;
    @FXML
    public Label colorValidationLabel;

    public static BeneficiaryBadgeFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new BeneficiaryBadgeFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        beneficiaryType.valueProperty().bindBidirectional(BeneficiaryBadgeViewModel.beneficiaryTypeProperty());
        name.textProperty().bindBidirectional(BeneficiaryBadgeViewModel.nameProperty());
        description.textProperty().bindBidirectional(BeneficiaryBadgeViewModel.descriptionProperty());
//        color.textProperty().bindBidirectional(BeneficiaryBadgeViewModel.endDateProperty());
        // Input listeners.
        requiredValidator(
                beneficiaryType, "Beneficiary type is required.", beneficiaryTypeValidationLabel, saveBtn);
        requiredValidator(
                name, "Name is required.", nameValidationLabel, saveBtn);
        // Input listeners.
        requiredValidator(
                description, "Description is required.", descriptionValidationLabel, saveBtn);
        // Input listeners.
//        requiredValidator(
//                color, "Color is required.", colorValidationLabel, saveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    BeneficiaryBadgeViewModel.resetProperties();

                    closeDialog(event);

                    nameValidationLabel.setVisible(false);
                    descriptionValidationLabel.setVisible(false);
                    colorValidationLabel.setVisible(false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

                    if (!nameValidationLabel.isVisible()
                            && !descriptionValidationLabel.isVisible()
                            && !colorValidationLabel.isVisible()) {
                        if (BeneficiaryBadgeViewModel.getId() > 0) {
                            SpotyThreader.spotyThreadPool(() -> {
                                try {
                                    BeneficiaryBadgeViewModel.updateItem(this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            });

                            SimpleNotification notification =
                                    new SimpleNotification.NotificationBuilder("Leave status updated successfully")
                                            .duration(NotificationDuration.SHORT)
                                            .icon("fas-circle-check")
                                            .type(NotificationVariants.SUCCESS)
                                            .build();
                            notificationHolder.addNotification(notification);

                            closeDialog(event);
                            return;
                        }

                        try {
                            BeneficiaryBadgeViewModel.saveBeneficiaryBadge(this::onAction, this::onSuccess, this::onFailed);
                        } catch (Exception e) {
                            SpotyLogger.writeToFile(e, this.getClass());
                        }

                        SimpleNotification notification =
                                new SimpleNotification.NotificationBuilder("Leave status saved successfully")
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
        System.out.println("Loading employment status...");
    }

    private void onSuccess() {
        System.out.println("Loaded employment status...");
    }

    private void onFailed() {
        System.out.println("failed loading employment status...");
    }
}
