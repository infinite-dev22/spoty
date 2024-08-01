package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.viewModels.accounting.*;
import inc.nomard.spoty.core.views.components.validatables.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import java.util.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class AccountForm extends MFXGenericDialog {
    public ValidatableTextField balance,
            accountName,
            accountNumber;
    public TextArea description;
    public Label accountNameValidationLabel,
            accountNumberValidationLabel;
    public Button saveBtn,
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
        balance = new ValidatableTextField();
        balance.setPrefWidth(400d);
        balance.textProperty().bindBidirectional(AccountViewModel.balanceProperty());
        AccountViewModel.idProperty().addListener((observableValue, oV, nV) -> balance.setDisable(Objects.nonNull(oV) && (Double) oV > 0 || Objects.nonNull(nV) && (Double) nV > 0));
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, balance);
        return vbox;
    }

    private VBox buildDescription() {
        // Input.
        var label = new Label("Description (Optional)");
        description = new TextArea();
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

    private Button buildSaveButton() {
        saveBtn = new Button("Save");
        saveBtn.setDefaultButton(true);
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