package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.components.validatables.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class SupplierForm extends ModalPage {
    @FXML
    public Button saveBtn,
            cancelBtn;
    @FXML
    public ValidatableTextField name,
            email,
            phone,
            city,
            country,
            taxNumber,
            address;
    @FXML
    public Label nameValidationLabel,
            emailValidationLabel,
            phoneValidationLabel;
    private List<Constraint> nameConstraints,
            emailConstraints,
            phoneConstraints;
    private ActionEvent actionEvent = null;

    public SupplierForm() {
        initializeComponents();
        initializeComponentProperties();
        setupLayout();
    }

    private void initializeComponents() {
        nameValidationLabel = createValidationLabel();
        emailValidationLabel = createValidationLabel();
        phoneValidationLabel = createValidationLabel();

        name = createTextField();
        email = createTextField();
        phone = createTextField();
        country = createTextField();
        city = createTextField();
        address = createTextField();
        taxNumber = createTextField();

        saveBtn = new Button("Save");
        saveBtn.setDefaultButton(true);

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
        GridPane gridPane = createGridPane();
        addGridPaneContent(gridPane);

        HBox buttonBox = new HBox(20, saveBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 10, 10, 10));

        setCenter(gridPane);
        setBottom(buttonBox);
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
        textField.setPrefWidth(300);
        return textField;
    }

    private VBox buildFieldBox(ValidatableTextField textField, String floatingText) {
        var label = new Label(floatingText);
        return new VBox(label, textField);
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.SOMETIMES);

        gridPane.getColumnConstraints().addAll(col1, col2);

        for (int i = 0; i < 6; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.SOMETIMES);
            gridPane.getRowConstraints().add(row);
        }

        return gridPane;
    }

    private void addGridPaneContent(GridPane gridPane) {
        addFormRow(gridPane, 0, buildFieldBox(name, "Name"), nameValidationLabel, buildFieldBox(email, "Email"), emailValidationLabel);
        addFormRow(gridPane, 1, buildFieldBox(phone, "Phone"), phoneValidationLabel, buildFieldBox(country, "Country"), null);
        addFormRow(gridPane, 2, buildFieldBox(city, "City"), null, buildFieldBox(address, "Address"), null);
        addFormRow(gridPane, 3, buildFieldBox(taxNumber, "Tax No."), null, null, null);
    }

    private void addFormRow(GridPane gridPane, int rowIndex, Node leftControl, Label leftLabel, Node rightControl, Label rightLabel) {
        addControlToGrid(gridPane, 0, rowIndex, leftControl, leftLabel);
        addControlToGrid(gridPane, 1, rowIndex, rightControl, rightLabel);
    }

    private void addControlToGrid(GridPane gridPane, int colIndex, int rowIndex, Node control, Label validationLabel) {
        if (control != null) {
            VBox vbox = new VBox(5, control);
            if (validationLabel != null) {
                vbox.getChildren().add(validationLabel);
            }
            vbox.setPadding(new Insets(0, 0, 2.5, 0));
            gridPane.add(vbox, colIndex, rowIndex);
        }
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
        bindTextField(name, SupplierViewModel.nameProperty());
        bindTextField(email, SupplierViewModel.emailProperty());
        bindTextField(phone, SupplierViewModel.phoneProperty());
        bindTextField(country, SupplierViewModel.countryProperty());
        bindTextField(city, SupplierViewModel.cityProperty());
        bindTextField(address, SupplierViewModel.addressProperty());
        bindTextField(taxNumber, SupplierViewModel.taxNumberProperty());
    }

    private void bindTextField(ValidatableTextField textField, Property<String> property) {
        textField.textProperty().bindBidirectional(property);
    }

    private void setupValidators() {
        Validators.requiredValidator(name, nameValidationLabel, "Name is required");
        Validators.requiredValidator(email, emailValidationLabel, "Email is required");
        Validators.emailValidator(email, emailValidationLabel, "Invalid email");
        Validators.requiredValidator(phone, phoneValidationLabel, "Phone is required");
        Validators.phoneValidator(phone, phoneValidationLabel, "Invalid phone number");
    }

    private void setupListeners() {
        dialogOnActions();
    }

    private void dialogOnActions() {
        saveBtn.setOnAction(this::onSave);
        cancelBtn.setOnAction(this::onCancel);
    }

    private void onSave(ActionEvent event) {
        nameConstraints = name.validate();
        emailConstraints = email.validate();
        phoneConstraints = phone.validate();
        if (!nameConstraints.isEmpty()) {
            nameValidationLabel.setManaged(true);
            nameValidationLabel.setVisible(true);
            nameValidationLabel.setText(nameConstraints.getFirst().getMessage());
            name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
            MFXStageDialog dialog = (MFXStageDialog) name.getScene().getWindow();
            dialog.sizeToScene();
        }
        if (!emailConstraints.isEmpty()) {
            emailValidationLabel.setManaged(true);
            emailValidationLabel.setVisible(true);
            emailValidationLabel.setText(emailConstraints.getFirst().getMessage());
            email.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
            MFXStageDialog dialog = (MFXStageDialog) email.getScene().getWindow();
            dialog.sizeToScene();
        }
        if (!phoneConstraints.isEmpty()) {
            phoneValidationLabel.setManaged(true);
            phoneValidationLabel.setVisible(true);
            phoneValidationLabel.setText(phoneConstraints.getFirst().getMessage());
            phone.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
            MFXStageDialog dialog = (MFXStageDialog) phone.getScene().getWindow();
            dialog.sizeToScene();
        }
        if (nameConstraints.isEmpty()
                && emailConstraints.isEmpty()
                && phoneConstraints.isEmpty()) {
            actionEvent = event;
            if (SupplierViewModel.getId() > 0) {
                SupplierViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                return;
            }
            SupplierViewModel.saveSupplier(this::onSuccess, this::successMessage, this::errorMessage);
        }
    }

    private void onCancel(ActionEvent event) {
        closeDialog(event);
        SupplierViewModel.resetProperties();
        nameValidationLabel.setVisible(false);
        emailValidationLabel.setVisible(false);
        phoneValidationLabel.setVisible(false);

        nameValidationLabel.setManaged(false);
        emailValidationLabel.setManaged(false);
        phoneValidationLabel.setManaged(false);

        name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        email.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        phone.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        this.dispose();
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        SupplierViewModel.resetProperties();
        SupplierViewModel.getAllSuppliers(null, null);
        this.dispose();
    }

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, "fas-circle-check");
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, "fas-triangle-exclamation");
    }

    private void displayNotification(String message, MessageVariants type, String icon) {
        var notification = new SpotyMessage.MessageBuilder(message)
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

    @Override
    public void dispose() {
        super.dispose();
        saveBtn = null;
        cancelBtn = null;
        name = null;
        email = null;
        phone = null;
        city = null;
        country = null;
        taxNumber = null;
        address = null;
        nameValidationLabel = null;
        emailValidationLabel = null;
        phoneValidationLabel = null;
        nameConstraints = null;
        emailConstraints = null;
        phoneConstraints = null;
        actionEvent = null;
    }
}
