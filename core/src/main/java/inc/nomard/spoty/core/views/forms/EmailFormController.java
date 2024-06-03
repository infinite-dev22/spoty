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

import static inc.nomard.spoty.core.GlobalActions.*;
import static inc.nomard.spoty.core.Validators.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
import static inc.nomard.spoty.core.viewModels.EmailViewModel.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.net.*;
import java.util.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import lombok.extern.java.*;

@Log
public class EmailFormController implements Initializable {
    private static EmailFormController instance;
    @FXML
    public MFXTextField bankName;
    @FXML
    public Label bankNameValidationLabel;
    @FXML
    public MFXTextField accountName;
    @FXML
    public Label accountNameValidationLabel;
    @FXML
    public MFXTextField accountNumber;
    @FXML
    public Label accountNumberValidationLabel;
    @FXML
    public MFXTextField branch;
    @FXML
    public Label branchValidationLabel;
    @FXML
    public MFXButton logo;
    @FXML
    public MFXButton saveBtn;
    @FXML
    public MFXButton cancelBtn;
    private ActionEvent actionEvent = null;

    public static EmailFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new EmailFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        bankName.textProperty().bindBidirectional(EmailViewModel.nameProperty());
        accountName.textProperty().bindBidirectional(EmailViewModel.emailProperty());
        accountNumber.textProperty().bindBidirectional(EmailViewModel.phoneProperty());
//        branch.textProperty().bindBidirectional(EmailViewModel.townProperty());
        // Input listeners.
        requiredValidator(
                bankName, "Name is required.", bankNameValidationLabel, saveBtn);
        requiredValidator(
                accountName, "Email is required.", accountNameValidationLabel, saveBtn);
        requiredValidator(
                accountNumber, "Phone is required.", accountNumberValidationLabel, saveBtn);
        requiredValidator(
                branch, "Town is required.", branchValidationLabel, saveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    clearEmailData();

                    closeDialog(event);

                    bankNameValidationLabel.setVisible(false);
                    accountNameValidationLabel.setVisible(false);
                    accountNumberValidationLabel.setVisible(false);
                    branchValidationLabel.setVisible(false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    if (!bankNameValidationLabel.isVisible()
                            && !accountNameValidationLabel.isVisible()
                            && !accountNumberValidationLabel.isVisible()
                            && !branchValidationLabel.isVisible()) {
                        if (EmailViewModel.getId() > 0) {
                            EmailViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                            actionEvent = event;
                            return;
                        }
                        EmailViewModel.saveEmail(this::onSuccess, this::successMessage, this::errorMessage);
                        actionEvent = event;
                        return;
                    }
                    onRequiredFieldsMissing();
                });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        EmailViewModel.clearEmailData();
        EmailViewModel.getAllEmails(null, null);
    }

    private void onRequiredFieldsMissing() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Required fields can't be null")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
    }

    private void successMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);
    }

    private void errorMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);
    }
}
