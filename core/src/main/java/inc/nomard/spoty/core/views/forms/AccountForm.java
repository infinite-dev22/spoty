package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.viewModels.*;
import static inc.nomard.spoty.core.viewModels.AccountViewModel.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxcomponents.theming.enums.*;
import java.util.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class AccountForm extends MFXGenericDialog {
    public MFXTextField balance,
            accountName,
            accountNumber;
    public TextArea description;
    public Label accountNameValidationLabel,
            accountNumberValidationLabel;
    public MFXButton saveBtn,
            cancelBtn;
    private List<Constraint> accountNameConstraints,
            accountNumberConstraints;
    private ActionEvent actionEvent = null;

    public AccountForm() {
        init();
    }

    public void init() {
        buildDialogContent();
        requiredValidator();
        dialogOnActions();
    }

    // Validation label.
    private Label buildValidationLabel() {
        var label = new Label();
        label.setManaged(false);
        label.setVisible(false);
        label.setWrapText(true);
        label.getStyleClass().add("input-validation-error");
        label.setId("validationLabel");
        return label;
    }


    private VBox buildName() {
        // Input.
        accountName = new MFXTextField();
        accountName.setFloatMode(FloatMode.BORDER);
        accountName.setFloatingText("Account Name");
        accountName.setPrefWidth(400d);
        accountName.textProperty().bindBidirectional(AccountViewModel.accountNameProperty());
        // Validation.
        accountNameValidationLabel = buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(accountName, accountNameValidationLabel);
        return vbox;
    }

    private VBox buildNumber() {
        // Input.
        accountNumber = new MFXTextField();
        accountNumber.setFloatMode(FloatMode.BORDER);
        accountNumber.setFloatingText("Account Number");
        accountNumber.setPrefWidth(400d);
        accountNumber.textProperty().bindBidirectional(AccountViewModel.accountNumberProperty());
        // Validation.
        accountNumberValidationLabel = buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(accountNumber, accountNumberValidationLabel);
        return vbox;
    }

    private VBox buildBalance() {
        // Input.
        balance = new MFXTextField();
        balance.setFloatMode(FloatMode.BORDER);
        balance.setFloatingText("Balance");
        balance.setPrefWidth(400d);
        balance.textProperty().bindBidirectional(AccountViewModel.balanceProperty());
        AccountViewModel.idProperty().addListener((observableValue, oV, nV) -> balance.setDisable(Objects.nonNull(oV) && (Double) oV > 0 || Objects.nonNull(nV) && (Double) nV > 0));
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(balance);
        return vbox;
    }

    private VBox buildDescription() {
        // Input.
        description = new TextArea();
        description.setPromptText("Description");
        description.setPrefWidth(400d);
        description.textProperty().bindBidirectional(AccountViewModel.descriptionProperty());
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(description);
        return vbox;
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(buildName(), buildNumber(), buildBalance(), buildDescription());
        return vbox;
    }

    private MFXButton buildSaveButton() {
        saveBtn = new MFXButton("Save");
        saveBtn.getStyleClass().add("filled");
        return saveBtn;
    }

    private MFXButton buildCancelButton() {
        cancelBtn = new MFXButton("Cancel");
        cancelBtn.getStyleClass().add("outlined");
        return cancelBtn;
    }

    private HBox buildBottom() {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setSpacing(20d);
        hbox.getChildren().addAll(buildSaveButton(), buildCancelButton());
        return hbox;
    }

    private void buildDialogContent() {
        this.setCenter(buildCenter());
        this.setBottom(buildBottom());
        this.setShowMinimize(false);
        this.setShowAlwaysOnTop(false);
        this.setShowClose(false);
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    clearBankData();
                    closeDialog(event);

                    accountNameValidationLabel.setVisible(false);
                    accountNumberValidationLabel.setVisible(false);
                    accountNameValidationLabel.setManaged(false);
                    accountNumberValidationLabel.setManaged(false);

                    accountName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    accountNumber.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    accountNameConstraints = accountName.validate();
                    accountNumberConstraints = accountNumber.validate();
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
                    if (accountNameConstraints.isEmpty()
                            && accountNumberConstraints.isEmpty()) {
                        actionEvent = event;
                        if (AccountViewModel.getId() > 0) {
                            AccountViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                            return;
                        }
                        AccountViewModel.saveBank(this::onSuccess, this::successMessage, this::errorMessage);
                    }
                });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        AccountViewModel.clearBankData();
        AccountViewModel.getAllAccounts(null, null);
    }

    public void requiredValidator() {
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
        // Display error.
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
        if (!AppManager.getMorphPane().getChildren().contains(notification)) {
            AppManager.getMorphPane().getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification));
        }
    }
}
