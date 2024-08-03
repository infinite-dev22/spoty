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
import inc.nomard.spoty.network_bridge.dtos.accounting.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import java.time.*;
import java.util.*;
import java.util.function.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class ExpenseForm extends MFXGenericDialog {
    public ValidatableTextField amount,
            name;
    public ValidatableTextArea note;
    public Button saveBtn,
            cancelBtn;
    public ValidatableDatePicker date;
    public ValidatableComboBox<Account> account;
    public Label nameValidationLabel,
            dateValidationLabel,
            accountValidationLabel,
            amountValidationLabel;
    private List<Constraint> nameConstraints,
            dateConstraints,
            categoryConstraints,
            amountConstraints;
    private ActionEvent actionEvent = null;

    public ExpenseForm() {
        init();
    }

    public void init() {
        buildDialogContent();
        requiredValidator();
        dialogOnActions();
    }

    private VBox buildAccount() {
        // Input.
        account = new ValidatableComboBox<>();
        var label = new Label("Account");
        account.setPrefWidth(400d);
        account.valueProperty().bindBidirectional(ExpensesViewModel.accountProperty());
        // Converter
        StringConverter<Account> accountConverter =
                FunctionalStringConverter.to(
                        account -> (account == null) ? "" : account.getAccountName());
        // Filter function
        Function<String, Predicate<Account>> accountFilterFunction =
                searchStr ->
                        account ->
                                StringUtils.containsIgnoreCase(accountConverter.toString(account), searchStr);

        // Combo box properties.
        account.setItems(AccountViewModel.getAccounts());
        account.setConverter(accountConverter);
        // Validation.
        accountValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, account, accountValidationLabel);
        return vbox;
    }

    private VBox buildName() {
        // Input.
        name = new ValidatableTextField();
        var label = new Label("Name");
        name.setPrefWidth(400d);
        name.textProperty().bindBidirectional(ExpensesViewModel.nameProperty());
        // Validation.
        nameValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, name, nameValidationLabel);
        return vbox;
    }

    private VBox buildDate() {
        // Input.
        date = new ValidatableDatePicker(LocalDate.now());
        var label = new Label("Expense Date");
        date.setPrefWidth(400d);
        date.valueProperty().bindBidirectional(ExpensesViewModel.dateProperty());
        // Validation.
        dateValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, date, dateValidationLabel);
        return vbox;
    }

    private VBox buildAmount() {
        // Input.
        amount = new ValidatableTextField();
        var label = new Label("Amount");
        amount.setPrefWidth(400d);
        amount.textProperty().bindBidirectional(ExpensesViewModel.amountProperty());
        ExpensesViewModel.idProperty().addListener((observableValue, oV, nV) -> amount.setDisable(Objects.nonNull(oV) && (Double) oV > 0 || Objects.nonNull(nV) && (Double) nV > 0));
        amountValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, amount);
        return vbox;
    }

    private VBox buildNote() {
        // Input.
        note = new ValidatableTextArea();
        var label = new Label("Note");
        note.setPrefWidth(400d);
        note.textProperty().bindBidirectional(ExpensesViewModel.noteProperty());
        note.setWrapText(true);
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, note);
        return vbox;
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(buildAccount(), buildName(), buildDate(), buildAmount(), buildNote());
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
                    closeDialog(event);
                    ExpensesViewModel.resetProperties();

                    nameValidationLabel.setVisible(false);
                    dateValidationLabel.setVisible(false);
                    accountValidationLabel.setVisible(false);
                    amountValidationLabel.setVisible(false);

                    nameValidationLabel.setManaged(false);
                    dateValidationLabel.setManaged(false);
                    accountValidationLabel.setManaged(false);
                    amountValidationLabel.setManaged(false);

                    name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    date.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    account.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    amount.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    nameConstraints = name.validate();
                    dateConstraints = date.validate();
                    categoryConstraints = account.validate();
                    amountConstraints = amount.validate();
                    if (!nameConstraints.isEmpty()) {
                        nameValidationLabel.setManaged(true);
                        nameValidationLabel.setVisible(true);
                        nameValidationLabel.setText(nameConstraints.getFirst().getMessage());
                        name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) name.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!dateConstraints.isEmpty()) {
                        dateValidationLabel.setManaged(true);
                        dateValidationLabel.setVisible(true);
                        dateValidationLabel.setText(dateConstraints.getFirst().getMessage());
                        date.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) date.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!categoryConstraints.isEmpty()) {
                        accountValidationLabel.setManaged(true);
                        accountValidationLabel.setVisible(true);
                        accountValidationLabel.setText(categoryConstraints.getFirst().getMessage());
                        account.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) account.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!amountConstraints.isEmpty()) {
                        amountValidationLabel.setManaged(true);
                        amountValidationLabel.setVisible(true);
                        amountValidationLabel.setText(amountConstraints.getFirst().getMessage());
                        amount.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) amount.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (nameConstraints.isEmpty()
                            && dateConstraints.isEmpty()
                            && categoryConstraints.isEmpty()
                            && amountConstraints.isEmpty()) {
                        if (ExpensesViewModel.getId() > 0) {
                            ExpensesViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                            actionEvent = event;
                            return;
                        }
                        ExpensesViewModel.saveExpense(this::onSuccess, this::successMessage, this::errorMessage);
                        actionEvent = event;
                    }
                });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        ExpensesViewModel.resetProperties();
        ExpensesViewModel.getAllExpenses(null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint nameConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Name is required")
                        .setCondition(name.textProperty().length().greaterThan(0))
                        .get();
        name.getValidator().constraint(nameConstraint);
        Constraint dateConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Date is required")
                        .setCondition(date.valueProperty().isNotNull())
                        .get();
        date.getValidator().constraint(dateConstraint);
        Constraint categoryConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Category is required")
                        .setCondition(account.valueProperty().isNotNull())
                        .get();
        account.getValidator().constraint(categoryConstraint);
        Constraint amountConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Amount is required")
                        .setCondition(amount.textProperty().length().greaterThan(0))
                        .get();
        amount.getValidator().constraint(amountConstraint);
        // Display error.
        name
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                nameValidationLabel.setManaged(false);
                                nameValidationLabel.setVisible(false);
                                name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        date
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                dateValidationLabel.setManaged(false);
                                dateValidationLabel.setVisible(false);
                                date.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        account
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                accountValidationLabel.setManaged(false);
                                accountValidationLabel.setVisible(false);
                                account.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        amount
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                amountValidationLabel.setManaged(false);
                                amountValidationLabel.setVisible(false);
                                amount.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
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
