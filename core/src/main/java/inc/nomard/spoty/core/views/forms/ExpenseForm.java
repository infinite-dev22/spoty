package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.util.validation.Constraint;
import inc.nomard.spoty.core.util.validation.Severity;
import inc.nomard.spoty.core.viewModels.accounting.AccountViewModel;
import inc.nomard.spoty.core.viewModels.accounting.ExpensesViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.*;
import inc.nomard.spoty.core.views.util.FunctionalStringConverter;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.core.views.util.Validators;
import inc.nomard.spoty.network_bridge.dtos.accounting.Account;
import javafx.beans.property.SimpleStringProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ExpenseForm extends BorderPane {
    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    private final ModalPane modalPane;
    public ValidatableNumberField amount;
    public ValidatableTextField name;
    public ValidatableTextArea note;
    public CustomButton saveBtn;
    public Button cancelBtn;
    public ValidatableDatePicker date;
    public ValidatableComboBox<Account> account;
    public Label nameValidationLabel,
            dateValidationLabel,
            accountValidationLabel,
            amountValidationLabel;
    private Integer reason;
    private Text subTitle;
    private List<Constraint> nameConstraints,
            dateConstraints,
            categoryConstraints,
            amountConstraints;

    public ExpenseForm(ModalPane modalPane, Integer reason) {
        this.modalPane = modalPane;
        this.reason = reason;
        buildDialogContent();
        requiredValidator();
        dialogOnActions();
        setup();
    }

    private VBox buildTitle() {
        var title = new Text("Expenses");
        title.getStyleClass().add(Styles.TITLE_3);
        subTitle = new Text("Create Form");
        subTitle.getStyleClass().add(Styles.TITLE_4);
        return buildFieldHolder(title, subTitle, buildSeparator());
    }

    private VBox buildFieldHolder(Node... nodes) {
        VBox vbox = new VBox();
        vbox.setSpacing(5d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(nodes);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        return vbox;
    }

    private Separator buildSeparator() {
        var separator = new Separator();
        separator.setPrefWidth(200.0);
        HBox.setHgrow(separator, Priority.ALWAYS);
        return separator;
    }

    private VBox buildAccount() {
        // Input.
        account = new ValidatableComboBox<>();
        var label = new Label("Account");
        account.setPrefWidth(1000d);
        account.valueProperty().bindBidirectional(ExpensesViewModel.accountProperty());
        // Converter
        StringConverter<Account> accountConverter =
                FunctionalStringConverter.to(
                        account -> (account == null) ? "" : account.getAccountName());

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
        var label = new Label("Name(Reason)");
        name.setPrefWidth(1000d);
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
        date.setPrefWidth(1000d);
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
        amount = new ValidatableNumberField();
        var label = new Label("Amount");
        amount.setPrefWidth(1000d);
        amount.setLeft(new Label("UGX"));
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
        note.setPrefWidth(1000d);
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
        vbox.getChildren().addAll(buildTitle(), buildAccount(), buildName(), buildDate(), buildAmount(), buildNote());
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
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction((event) -> dispose());
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
                    }
                    if (!dateConstraints.isEmpty()) {
                        dateValidationLabel.setManaged(true);
                        dateValidationLabel.setVisible(true);
                        dateValidationLabel.setText(dateConstraints.getFirst().getMessage());
                        date.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!categoryConstraints.isEmpty()) {
                        accountValidationLabel.setManaged(true);
                        accountValidationLabel.setVisible(true);
                        accountValidationLabel.setText(categoryConstraints.getFirst().getMessage());
                        account.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!amountConstraints.isEmpty()) {
                        amountValidationLabel.setManaged(true);
                        amountValidationLabel.setVisible(true);
                        amountValidationLabel.setText(amountConstraints.getFirst().getMessage());
                        amount.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (nameConstraints.isEmpty()
                            && dateConstraints.isEmpty()
                            && categoryConstraints.isEmpty()
                            && amountConstraints.isEmpty()) {
                        saveBtn.startLoading();
                        if (reason == 1) {
                            ExpensesViewModel.updateItem(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                        } else {
                            ExpensesViewModel.saveExpense(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                        }
                    }
                });
    }

    private void onSuccess() {
        this.dispose();
        ExpensesViewModel.getAllExpenses(null, null, null, null);
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

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        saveBtn.stopLoading();
    }

    public void dispose() {
        modalPane.setPersistent(false);
        modalPane.hide(true);
        ExpensesViewModel.resetProperties();
        amount = null;
        name = null;
        note = null;
        saveBtn = null;
        cancelBtn = null;
        subTitle = null;
        date = null;
        account = null;
        nameValidationLabel = null;
        dateValidationLabel = null;
        accountValidationLabel = null;
        amountValidationLabel = null;
        nameConstraints = null;
        dateConstraints = null;
        categoryConstraints = null;
        amountConstraints = null;
        reason = null;
    }

    private void setup() {
        if (reason == 1) {
            saveBtn.textProperty().bindBidirectional(new SimpleStringProperty("Update"));
            subTitle.textProperty().bindBidirectional(new SimpleStringProperty("Update Form"));
        } else {
            saveBtn.textProperty().bindBidirectional(new SimpleStringProperty("Create"));
            subTitle.textProperty().bindBidirectional(new SimpleStringProperty("Create Form"));
        }
    }
}
