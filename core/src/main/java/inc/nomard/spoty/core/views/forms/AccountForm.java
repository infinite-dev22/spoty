package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.viewModels.accounting.AccountViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.ValidatableNumberField;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextArea;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextField;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.core.views.util.Validators;
import inc.nomard.spoty.core.util.validation.Constraint;
import inc.nomard.spoty.core.util.validation.Severity;
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
import javafx.util.converter.NumberStringConverter;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Objects;

@Log4j2
public class AccountForm extends BorderPane {
    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    private final ModalPane modalPane;
    public ValidatableNumberField balance;
    public ValidatableTextField accountName,
            accountNumber;
    public ValidatableTextArea description;
    public Label accountNameValidationLabel,
            accountNumberValidationLabel,
            balanceValidationLabel;
    public CustomButton saveBtn;
    public Button cancelBtn;
    private Integer reason;
    private Text subTitle;
    private Label balanceLabel;
    private List<Constraint> accountNameConstraints,
            accountNumberConstraints,
            balanceConstraints;

    public AccountForm(ModalPane modalPane, Integer reason) {
        this.modalPane = modalPane;
        this.reason = reason;
        buildDialogContent();
        requiredValidator();
        dialogOnActions();
        this.setup();
    }

    private VBox buildTitle() {
        var title = new Text("Accounts");
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

    private VBox buildName() {
        // Input.
        var label = new Label("Account Name");
        accountName = new ValidatableTextField();
        accountName.setPrefWidth(1000d);
        accountName.textProperty().bindBidirectional(AccountViewModel.accountNameProperty());
        // Validation.
        accountNameValidationLabel = Validators.buildValidationLabel();
        return buildFieldHolder(label, accountName, accountNameValidationLabel);
    }

    private VBox buildNumber() {
        // Input.
        var label = new Label("Account Number");
        accountNumber = new ValidatableTextField();
        accountNumber.setPrefWidth(1000d);
        accountNumber.textProperty().bindBidirectional(AccountViewModel.accountNumberProperty());
        // Validation.
        accountNumberValidationLabel = Validators.buildValidationLabel();
        return buildFieldHolder(label, accountNumber, accountNumberValidationLabel);
    }

    private VBox buildBalance() {
        // Input.
        balanceLabel = new Label("Balance (Optional)");
        balance = new ValidatableNumberField();
        balance.setPrefWidth(1000d);
        if (Objects.equals(balance.getText(), "0")) {
            balance.setText(null);
        }
        balance.textProperty().addListener(observable -> {
            if (Objects.equals(balance.getText(), "0")) {
                balance.setText(null);
            }
        });
        // Validation.
        balanceValidationLabel = Validators.buildValidationLabel();
        return buildFieldHolder(balanceLabel, balance, balanceValidationLabel);
    }

    private VBox buildDescription() {
        // Input.
        var label = new Label("Description (Optional)");
        description = new ValidatableTextArea();
        description.setPrefWidth(1000d);
        description.textProperty().bindBidirectional(AccountViewModel.descriptionProperty());
        description.setWrapText(true);
        return buildFieldHolder(label, description);
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(buildTitle(), buildName(), buildNumber(), buildBalance(), buildDescription());
        return vbox;
    }

    private CustomButton buildSaveButton() {
        saveBtn = new CustomButton("Create");
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
        hbox.setPadding(new Insets(10d));
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
                    accountNameConstraints = accountName.validate();
                    accountNumberConstraints = accountNumber.validate();
                    if (!accountNameConstraints.isEmpty()) {
                        accountNameValidationLabel.setManaged(true);
                        accountNameValidationLabel.setVisible(true);
                        accountNameValidationLabel.setText(accountNameConstraints.getFirst().getMessage());
                        accountName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!accountNumberConstraints.isEmpty()) {
                        accountNumberValidationLabel.setManaged(true);
                        accountNumberValidationLabel.setVisible(true);
                        accountNumberValidationLabel.setText(accountNumberConstraints.getFirst().getMessage());
                        accountNumber.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (reason == 2) {
                        balanceConstraints = balance.validate();
                        if (!balanceConstraints.isEmpty()) {
                            balanceValidationLabel.setManaged(true);
                            balanceValidationLabel.setVisible(true);
                            balanceValidationLabel.setText(balanceConstraints.getFirst().getMessage());
                            balance.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        }
                    }
                    if (accountNameConstraints.isEmpty()
                            && accountNumberConstraints.isEmpty()) {
                        if (reason == 2 && !balanceConstraints.isEmpty()) {
                            return;
                        }
                        saveBtn.startLoading();
                        switch (reason) {
                            case 1:
                                AccountViewModel.updateItem(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                                break;
                            case 2:
                                AccountViewModel.deposit(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                                break;
                            default:
                                AccountViewModel.saveAccount(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                                break;
                        }
                    }
                });
    }

    private void onSuccess() {
        dispose();
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

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        saveBtn.stopLoading();
    }

    public void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
        AccountViewModel.clearAccountData();
        accountName = null;
        accountNameValidationLabel = null;
        accountNumber = null;
        accountNumberValidationLabel = null;
        balance = null;
        description = null;
        saveBtn = null;
        cancelBtn = null;
        subTitle = null;
        reason = null;
    }

    private void setup() {
        switch (reason) {
            case 1:
                accountName.setDisable(false);
                accountNameValidationLabel.setDisable(false);
                accountNumber.setDisable(false);
                accountNumberValidationLabel.setDisable(false);
                balance.setDisable(true);
                balance.textProperty().bindBidirectional(AccountViewModel.balanceProperty(), new NumberStringConverter());
                description.setDisable(false);
                saveBtn.textProperty().bindBidirectional(new SimpleStringProperty("Update"));
                subTitle.textProperty().bindBidirectional(new SimpleStringProperty("Update Form"));
                balanceLabel.textProperty().bindBidirectional(new SimpleStringProperty("Balance"));
                balanceLabel.setDisable(true);
                break;
            case 2:
                accountName.setDisable(true);
                accountNameValidationLabel.setDisable(true);
                accountNumber.setDisable(true);
                accountNumberValidationLabel.setDisable(true);
                balance.setDisable(false);
                description.setDisable(true);
                saveBtn.textProperty().bindBidirectional(new SimpleStringProperty("Deposit"));
                subTitle.textProperty().bindBidirectional(new SimpleStringProperty("Deposit Form"));
                balanceLabel.textProperty().bindBidirectional(new SimpleStringProperty("Deposit Amount"));
                break;
            default:
                accountName.setDisable(false);
                accountNameValidationLabel.setDisable(false);
                accountNumber.setDisable(false);
                accountNumberValidationLabel.setDisable(false);
                balance.setDisable(false);
                balance.textProperty().bindBidirectional(AccountViewModel.balanceProperty(), new NumberStringConverter());
                description.setDisable(false);
                break;
        }
    }
}
