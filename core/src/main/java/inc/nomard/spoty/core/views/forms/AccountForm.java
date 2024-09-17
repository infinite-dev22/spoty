package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.accounting.AccountViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.ValidatableNumberField;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextArea;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextField;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.message.SpotyMessage;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import inc.nomard.spoty.core.views.util.Validators;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;
import lombok.extern.java.Log;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.List;
import java.util.Objects;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

@Log
public class AccountForm extends MFXGenericDialog {
    public ValidatableNumberField balance;
    public ValidatableTextField accountName,
            accountNumber;
    public ValidatableTextArea description;
    public Label accountNameValidationLabel,
            accountNumberValidationLabel;
    public CustomButton saveBtn;
    public Button cancelBtn;
    private List<Constraint> accountNameConstraints,
            accountNumberConstraints;
    private Event actionEvent = null;

    public AccountForm() {
        init();
    }

    public void init() {
        buildDialogContent();
        requiredValidator();
        dialogOnActions();
    }


    private VBox buildName() {
        // Input.
        var label = new Label("Account Name");
        accountName = new ValidatableTextField();
        accountName.setPrefWidth(400d);
        accountName.textProperty().bindBidirectional(AccountViewModel.accountNameProperty());
        // Validation.
        accountNameValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, accountName, accountNameValidationLabel);
        return vbox;
    }

    private VBox buildNumber() {
        // Input.
        var label = new Label("Account Number");
        accountNumber = new ValidatableTextField();
        accountNumber.setPrefWidth(400d);
        accountNumber.textProperty().bindBidirectional(AccountViewModel.accountNumberProperty());
        // Validation.
        accountNumberValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, accountNumber, accountNumberValidationLabel);
        return vbox;
    }

    private VBox buildBalance() {
        // Input.
        var label = new Label("Balance (Optional)");
        balance = new ValidatableNumberField();
        balance.setPrefWidth(400d);
        balance.textProperty().bindBidirectional(AccountViewModel.balanceProperty(), new NumberStringConverter());
        if (Objects.equals(balance.getText(), "0")) {
            balance.setText(null);
        }
        if (!Objects.isNull(balance.getText()) && !balance.getText().isBlank() && !balance.getText().isEmpty()) {
            balance.setDisable(true);
        }
        if (AccountViewModel.getId() > 0) {
            balance.setDisable(true);
        }
        balance.textProperty().addListener(observable -> {
            if (Objects.equals(balance.getText(), "0")) {
                balance.setText(null);
            }
        });
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, balance);
        return vbox;
    }

    private VBox buildDescription() {
        // Input.
        var label = new Label("Description (Optional)");
        description = new ValidatableTextArea();
        description.setPrefWidth(400d);
        description.textProperty().bindBidirectional(AccountViewModel.descriptionProperty());
        description.setWrapText(true);
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, description);
        return vbox;
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(buildName(), buildNumber(), buildBalance(), buildDescription());
        return vbox;
    }

    private CustomButton buildSaveButton() {
        saveBtn = new CustomButton("Save");
        saveBtn.getStyleClass().add(Styles.ACCENT);
        return saveBtn;
    }

    private Button buildCancelButton() {
        cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
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
                    AccountViewModel.clearAccountData();
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
                        AccountViewModel.saveAccount(this::onSuccess, this::successMessage, this::errorMessage);
                    }
                });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        AccountViewModel.clearAccountData();
        AccountViewModel.getAllAccounts(null, null, null, null);
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
        displayNotification(message, MessageVariants.SUCCESS, FontAwesomeSolid.CHECK_CIRCLE);
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, FontAwesomeSolid.EXCLAMATION_TRIANGLE);
    }

    private void displayNotification(String message, MessageVariants type, Ikon icon) {
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
