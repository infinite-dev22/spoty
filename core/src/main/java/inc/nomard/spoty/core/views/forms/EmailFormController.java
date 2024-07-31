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

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import static inc.nomard.spoty.core.Validators.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
import static inc.nomard.spoty.core.viewModels.EmailViewModel.*;
import inc.nomard.spoty.core.views.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.net.*;
import java.util.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class EmailFormController implements Initializable {
    private static EmailFormController instance;
    private final Stage stage;
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

    public EmailFormController(Stage stage) {
        this.stage = stage;
    }

    public static EmailFormController getInstance(Stage stage) {
        if (Objects.equals(instance, null)) {
            synchronized (EmailFormController.class) {
                instance = new EmailFormController(stage);
            }
        }
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        bankName.textProperty().bindBidirectional(EmailViewModel.nameProperty());
        accountName.textProperty().bindBidirectional(EmailViewModel.emailProperty());
        accountNumber.textProperty().bindBidirectional(EmailViewModel.phoneProperty());
//        description.textProperty().bindBidirectional(EmailViewModel.townProperty());
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
                    errorMessage("Required fields can't be null");
                });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        EmailViewModel.clearEmailData();
        EmailViewModel.getAllEmails(null, null);
    }

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, "fas-circle-check");
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, "fas-triangle-exclamation");
    }

    private void displayNotification(String message, MessageVariants type, String icon) {
        SpotyMessage notification = new SpotyMessage.MessageBuilder(message)
                .duration(MessageDuration.SHORT)
                .icon(icon)
                .type(type)
                .height(60)
                .build();
        AnchorPane.setTopAnchor(notification, 5.0);
        AnchorPane.setRightAnchor(notification, 5.0);

        var in = Animations.slideInDown(notification, Duration.millis(250));
        if (!BaseController.getInstance(stage).morphPane.getChildren().contains(notification)) {
            BaseController.getInstance(stage).morphPane.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification, stage));
        }
    }
}
