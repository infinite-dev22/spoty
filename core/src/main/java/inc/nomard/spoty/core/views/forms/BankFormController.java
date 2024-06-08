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
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
import static inc.nomard.spoty.core.viewModels.BankViewModel.*;
import inc.nomard.spoty.core.views.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
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
public class BankFormController implements Initializable {
    private static BankFormController instance;
    private final Stage stage;
    @FXML
    public MFXTextField bankName,
            accountName,
            accountNumber,
            branch;
    @FXML
    public Label bankNameValidationLabel,
            accountNameValidationLabel,
            accountNumberValidationLabel,
            branchValidationLabel;
    @FXML
    public MFXButton logo,
            saveBtn,
            cancelBtn;
    private List<Constraint> bankNameConstraints,
            accountNameConstraints,
            accountNumberConstraints,
            branchConstraints;
    private ActionEvent actionEvent = null;

    private BankFormController(Stage stage) {
        this.stage = stage;
    }

    public static BankFormController getInstance(Stage stage) {
        if (Objects.equals(instance, null)) {
            synchronized (BankFormController.class) {
                instance = new BankFormController(stage);
            }
        }
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        bankName.textProperty().bindBidirectional(BankViewModel.nameProperty());
        accountName.textProperty().bindBidirectional(BankViewModel.emailProperty());
        accountNumber.textProperty().bindBidirectional(BankViewModel.phoneProperty());
        branch.textProperty().bindBidirectional(BankViewModel.townProperty());
        // Input listeners.
        requiredValidator();
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    clearBankData();
                    closeDialog(event);

                    bankNameValidationLabel.setVisible(false);
                    accountNameValidationLabel.setVisible(false);
                    accountNumberValidationLabel.setVisible(false);
                    branchValidationLabel.setVisible(false);

                    bankNameValidationLabel.setManaged(false);
                    accountNameValidationLabel.setManaged(false);
                    accountNumberValidationLabel.setManaged(false);
                    branchValidationLabel.setManaged(false);

                    bankName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    accountName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    accountNumber.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    branch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    bankNameConstraints = bankName.validate();
                    accountNameConstraints = accountName.validate();
                    accountNumberConstraints = accountNumber.validate();
                    branchConstraints = branch.validate();
                    if (!bankNameConstraints.isEmpty()) {
                        bankNameValidationLabel.setManaged(true);
                        bankNameValidationLabel.setVisible(true);
                        bankNameValidationLabel.setText(bankNameConstraints.getFirst().getMessage());
                        bankName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) bankName.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!accountNameConstraints.isEmpty()) {
                        accountNameValidationLabel.setManaged(true);
                        accountNameValidationLabel.setVisible(true);
                        accountNameValidationLabel.setText(accountNameConstraints.getFirst().getMessage());
                        accountName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) accountName.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!accountNumberConstraints.isEmpty()) {
                        accountNumberValidationLabel.setManaged(true);
                        accountNumberValidationLabel.setVisible(true);
                        accountNumberValidationLabel.setText(accountNumberConstraints.getFirst().getMessage());
                        accountNumber.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) accountNumber.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!branchConstraints.isEmpty()) {
                        branchValidationLabel.setManaged(true);
                        branchValidationLabel.setVisible(true);
                        branchValidationLabel.setText(branchConstraints.getFirst().getMessage());
                        branch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) branch.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (bankNameConstraints.isEmpty()
                            && accountNameConstraints.isEmpty()
                            && accountNumberConstraints.isEmpty()
                            && branchConstraints.isEmpty()) {
                        actionEvent = event;
                        if (BankViewModel.getId() > 0) {
                            BankViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                            return;
                        }
                        BankViewModel.saveBank(this::onSuccess, this::successMessage, this::errorMessage);
                    }
                });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        BankViewModel.clearBankData();
        BankViewModel.getAllBanks(null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint bankNameConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Bank name is required")
                        .setCondition(bankName.textProperty().length().greaterThan(0))
                        .get();
        bankName.getValidator().constraint(bankNameConstraint);
        Constraint accountNameConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Account name is required")
                        .setCondition(accountName.textProperty().length().greaterThan(0))
                        .get();
        accountName.getValidator().constraint(accountNameConstraint);
        Constraint accountNumberConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Account number is required")
                        .setCondition(accountNumber.textProperty().length().greaterThan(0))
                        .get();
        accountNumber.getValidator().constraint(accountNumberConstraint);
        Constraint branchConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Branch is required")
                        .setCondition(branch.textProperty().length().greaterThan(0))
                        .get();
        branch.getValidator().constraint(branchConstraint);
        // Display error.
        bankName
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                bankNameValidationLabel.setManaged(false);
                                bankNameValidationLabel.setVisible(false);
                                bankName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        accountName
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                accountNameValidationLabel.setManaged(false);
                                accountNameValidationLabel.setVisible(false);
                                accountName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        accountNumber
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                accountNumberValidationLabel.setManaged(false);
                                accountNumberValidationLabel.setVisible(false);
                                accountNumber.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        branch
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                branchValidationLabel.setManaged(false);
                                branchValidationLabel.setVisible(false);
                                branch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
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
