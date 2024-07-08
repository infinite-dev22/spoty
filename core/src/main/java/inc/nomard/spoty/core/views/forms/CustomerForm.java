package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.util.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class CustomerForm extends ModalPage {
    public MFXButton saveBtn,
            cancelBtn;
    public MFXTextField name,
            email,
            phone,
            city,
            country,
            taxNumber,
            address;
    public Label nameValidationLabel,
            emailValidationLabel,
            phoneValidationLabel;
    private List<Constraint> nameConstraints,
            emailConstraints,
            phoneConstraints;
    private ActionEvent actionEvent = null;

    public CustomerForm() {
        initializeComponents();
        initializeComponentProperties();
        setupLayout();
    }

    private void initializeComponents() {
        nameValidationLabel = createValidationLabel();
        emailValidationLabel = createValidationLabel();
        phoneValidationLabel = createValidationLabel();

        name = createTextField("Name");
        email = createTextField("Email");
        phone = createTextField("Phone");
        country = createTextField("Country");
        city = createTextField("City");
        address = createTextField("Address");
        taxNumber = createTextField("Tax No.");

        saveBtn = new MFXButton("Save");
        saveBtn.getStyleClass().add("filled");

        cancelBtn = new MFXButton("Cancel");
        cancelBtn.getStyleClass().add("outlined");
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

    private MFXTextField createTextField(String floatingText) {
        MFXTextField textField = new MFXTextField();
        textField.setFloatingText(floatingText);
        textField.setFloatMode(FloatMode.BORDER);
        textField.setPrefWidth(300);
        return textField;
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
        addFormRow(gridPane, 0, name, nameValidationLabel, email, emailValidationLabel);
        addFormRow(gridPane, 1, phone, phoneValidationLabel, country, null);
        addFormRow(gridPane, 2, city, null, address, null);
        addFormRow(gridPane, 3, taxNumber, null, null, null);
    }

    private void addFormRow(GridPane gridPane, int rowIndex, Control leftControl, Label leftLabel, Control rightControl, Label rightLabel) {
        addControlToGrid(gridPane, 0, rowIndex, leftControl, leftLabel);
        addControlToGrid(gridPane, 1, rowIndex, rightControl, rightLabel);
    }

    private void addControlToGrid(GridPane gridPane, int colIndex, int rowIndex, Control control, Label validationLabel) {
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
            if (newValue != oldValue) phone.setLeadingIcon(new Label("+"));
        });
    }

    private void setupBindings() {
        bindTextField(name, CustomerViewModel.nameProperty());
        bindTextField(email, CustomerViewModel.emailProperty());
        bindTextField(phone, CustomerViewModel.phoneProperty());
        bindTextField(country, CustomerViewModel.countryProperty());
        bindTextField(city, CustomerViewModel.cityProperty());
        bindTextField(address, CustomerViewModel.addressProperty());
        bindTextField(taxNumber, CustomerViewModel.taxNumberProperty());
    }

    private void bindTextField(MFXTextField textField, Property<String> property) {
        textField.textProperty().bindBidirectional(property);
    }

    private void setupValidators() {
        requiredValidator(name, nameValidationLabel, "Name is required");
        requiredValidator(email, emailValidationLabel, "Email is required");
        requiredValidator(phone, phoneValidationLabel, "Phone is required");
    }

    private void requiredValidator(MFXTextField control, Label validationLabel, String message) {
        // Name input validation.
        Constraint firstName =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage(message)
                        .setCondition(control.textProperty().length().greaterThan(0))
                        .get();
        control.getValidator().constraint(firstName);
        control
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                validationLabel.setManaged(false);
                                validationLabel.setVisible(false);
                                control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
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
            if (CustomerViewModel.getId() > 0) {
                CustomerViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                return;
            }
            CustomerViewModel.saveCustomer(this::onSuccess, this::successMessage, this::errorMessage);
        }
    }

    private void onCancel(ActionEvent event) {
        closeDialog(event);
        CustomerViewModel.resetProperties();
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
        CustomerViewModel.resetProperties();
        CustomerViewModel.getAllCustomers(null, null);
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