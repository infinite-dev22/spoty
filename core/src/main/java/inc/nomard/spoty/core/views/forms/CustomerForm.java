package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.viewModels.CustomerViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextField;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.core.views.util.Validators;
import io.github.palexdev.materialfx.validation.Constraint;
import javafx.beans.property.Property;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.extern.java.Log;

import java.util.List;

@Log
public class CustomerForm extends BorderPane {
    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    private final ModalPane modalPane;
    public CustomButton saveBtn;
    public Button cancelBtn;
    public ValidatableTextField firstName,
            otherName,
            lastName,
            email,
            phone,
            city,
            country,
            taxNumber,
            address;
    public Label firstNameValidationLabel,
            lastNameValidationLabel,
            emailValidationLabel,
            phoneValidationLabel;
    private List<Constraint> firstNameConstraints,
            lastNameConstraints,
            emailConstraints,
            phoneConstraints;

    public CustomerForm(ModalPane modalPane) {
        this.modalPane = modalPane;
        initializeComponents();
        initializeComponentProperties();
        setupLayout();
    }

    private void initializeComponents() {
        firstNameValidationLabel = createValidationLabel();
        lastNameValidationLabel = createValidationLabel();
        emailValidationLabel = createValidationLabel();
        phoneValidationLabel = createValidationLabel();

        firstName = createTextField();
        otherName = createTextField();
        lastName = createTextField();
        email = createTextField();
        phone = createTextField();
        country = createTextField();
        city = createTextField();
        address = createTextField();
        taxNumber = createTextField();

        saveBtn = new CustomButton("Save");
        saveBtn.getStyleClass().add(Styles.ACCENT);

        cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
    }

    private void initializeComponentProperties() {
        setupPhoneField();
        setupBindings();
        setupValidators();
        setupListeners();
    }

    private void setupLayout() {
        HBox buttonBox = new HBox(20, saveBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 10, 10, 10));

        setCenter(buildCenter());
        setBottom(buttonBox);
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(
                buildFieldBox(firstName, "First name", firstNameValidationLabel),
                buildFieldBox(otherName, "Middle name (Optional)"),
                buildFieldBox(lastName, "Last name", lastNameValidationLabel),
                buildFieldBox(email, "Email", emailValidationLabel),
                buildFieldBox(phone, "Phone (Optional)"),
                buildFieldBox(country, "Country (Optional)"),
                buildFieldBox(city, "City (Optional)"),
                buildFieldBox(address, "Address (Optional)"),
                buildFieldBox(taxNumber, "Tax No. (Optional)"));
        return vbox;
    }

    private Label createValidationLabel() {
        Label label = new Label();
        label.setManaged(false);
        label.setVisible(false);
        label.setWrapText(true);
        label.getStyleClass().add("input-validation-error");
        return label;
    }

    private ValidatableTextField createTextField() {
        ValidatableTextField textField = new ValidatableTextField();
        textField.setPrefWidth(1000);
        return textField;
    }

    private VBox buildFieldBox(Control control, String floatingText, Label validationLabel) {
        var label = new Label(floatingText);
        return new VBox(label, control, validationLabel);
    }

    private VBox buildFieldBox(ValidatableTextField textField, String floatingText) {
        var label = new Label(floatingText);
        return new VBox(label, textField);
    }

    private void setupPhoneField() {
        phone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) phone.setText(newValue.replaceAll("\\D", ""));
        });
        phone.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != oldValue) phone.setLeft(new Label("+"));
        });
    }

    private void setupBindings() {
        bindTextField(firstName, CustomerViewModel.firstNameProperty());
        bindTextField(otherName, CustomerViewModel.otherNameProperty());
        bindTextField(lastName, CustomerViewModel.lastNameProperty());
        bindTextField(email, CustomerViewModel.emailProperty());
        bindTextField(phone, CustomerViewModel.phoneProperty());
        bindTextField(country, CustomerViewModel.countryProperty());
        bindTextField(city, CustomerViewModel.cityProperty());
        bindTextField(address, CustomerViewModel.addressProperty());
        bindTextField(taxNumber, CustomerViewModel.taxNumberProperty());
    }

    private void bindTextField(ValidatableTextField textField, Property<String> property) {
        textField.textProperty().bindBidirectional(property);
    }

    private void setupValidators() {
        Validators.requiredValidator(firstName, firstNameValidationLabel, "First name is required");
        Validators.requiredValidator(lastName, lastNameValidationLabel, "Last name is required");
        Validators.requiredValidator(email, emailValidationLabel, "Email is required");
        Validators.emailValidator(email, emailValidationLabel, "Invalid email");
        Validators.phoneValidator(phone, phoneValidationLabel, "Invalid phone number");
    }

    private void setupListeners() {
        dialogOnActions();
    }

    private void dialogOnActions() {
        saveBtn.setOnAction(this::onSave);
        cancelBtn.setOnAction(actionEvent -> this.dispose());
    }

    private void onSave(Event event) {
        firstNameConstraints = firstName.validate();
        lastNameConstraints = lastName.validate();
        emailConstraints = email.validate();
        if (!phone.getText().isEmpty()) {
            phoneConstraints = phone.validate();
        }
        if (!firstNameConstraints.isEmpty()) {
            firstNameValidationLabel.setManaged(true);
            firstNameValidationLabel.setVisible(true);
            firstNameValidationLabel.setText(firstNameConstraints.getFirst().getMessage());
            firstName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (!lastNameConstraints.isEmpty()) {
            lastNameValidationLabel.setManaged(true);
            lastNameValidationLabel.setVisible(true);
            lastNameValidationLabel.setText(lastNameConstraints.getFirst().getMessage());
            lastName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (!emailConstraints.isEmpty()) {
            emailValidationLabel.setManaged(true);
            emailValidationLabel.setVisible(true);
            emailValidationLabel.setText(emailConstraints.getFirst().getMessage());
            email.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (!phone.getText().isEmpty() && !phoneConstraints.isEmpty()) {
            phoneValidationLabel.setManaged(true);
            phoneValidationLabel.setVisible(true);
            phoneValidationLabel.setText(phoneConstraints.getFirst().getMessage());
            phone.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (firstNameConstraints.isEmpty()
                && lastNameConstraints.isEmpty()
                && emailConstraints.isEmpty()
                && phoneConstraints.isEmpty()) {
            if (CustomerViewModel.getId() > 0) {
                CustomerViewModel.updateItem(this::onSuccess, SpotyUtils::successMessage, SpotyUtils::errorMessage);
            } else {
                CustomerViewModel.saveCustomer(this::onSuccess, SpotyUtils::successMessage, SpotyUtils::errorMessage);
            }
        }
    }

    private void onSuccess() {
        this.dispose();
        CustomerViewModel.getAllCustomers(null, null, null, null);
    }

    public void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
        CustomerViewModel.resetProperties();
        saveBtn = null;
        cancelBtn = null;
        firstName = null;
        lastName = null;
        email = null;
        phone = null;
        city = null;
        country = null;
        taxNumber = null;
        address = null;
        firstNameValidationLabel = null;
        lastNameValidationLabel = null;
        emailValidationLabel = null;
        phoneValidationLabel = null;
        firstNameConstraints = null;
        lastNameConstraints = null;
        emailConstraints = null;
        phoneConstraints = null;
    }
}
