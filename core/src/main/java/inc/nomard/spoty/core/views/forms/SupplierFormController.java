package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import static inc.nomard.spoty.core.Validators.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
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
public class SupplierFormController implements Initializable {
    private static SupplierFormController instance;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    @FXML
    public MFXTextField name,
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

    public static SupplierFormController getInstance() {
        if (Objects.equals(instance, null)) {
            synchronized (SupplierFormController.class) {
                instance = new SupplierFormController();
            }
        }
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input binding.
        name.textProperty().bindBidirectional(SupplierViewModel.nameProperty());
        email.textProperty().bindBidirectional(SupplierViewModel.emailProperty());
        phone.textProperty().bindBidirectional(SupplierViewModel.phoneProperty());
        city.textProperty().bindBidirectional(SupplierViewModel.cityProperty());
        country.textProperty().bindBidirectional(SupplierViewModel.countryProperty());
        taxNumber.textProperty().bindBidirectional(SupplierViewModel.taxNumberProperty());
        address.textProperty().bindBidirectional(SupplierViewModel.addressProperty());

        // Input listeners.
        phone
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (!newValue.matches("\\d*"))
                                phone.setText(newValue.replaceAll("\\D", ""));
                        });
        phone
                .focusedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue != oldValue) phone.setLeadingIcon(new Label("+"));
                        });

        // Input validations.
        // Name input validation.
        requiredValidator();

        // Email input validation.
        emailValidator(email, emailValidationLabel, saveBtn);

        // Phone input validation.
        lengthValidator(
                phone,
                11,
                "Invalid length",
                phoneValidationLabel,
                saveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
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
                });
        saveBtn.setOnAction(
                (event) -> {
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
                        if (SupplierViewModel.getId() > 0) {
                            SupplierViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                            actionEvent = event;
                            return;
                        }
                        SupplierViewModel.saveSupplier(this::onSuccess, this::successMessage, this::errorMessage);
                        actionEvent = event;
                    }
                });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        SupplierViewModel.resetProperties();
        SupplierViewModel.getAllSuppliers(null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint name =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Name is required")
                        .setCondition(this.name.textProperty().length().greaterThan(0))
                        .get();
        this.name.getValidator().constraint(name);
        Constraint email =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Email is required")
                        .setCondition(this.email.textProperty().length().greaterThan(0))
                        .get();
        this.email.getValidator().constraint(email);
        Constraint phone =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Phone is required")
                        .setCondition(this.phone.textProperty().length().greaterThan(0))
                        .get();
        this.phone.getValidator().constraint(phone);
        // Display error.
        this.name
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                nameValidationLabel.setManaged(false);
                                nameValidationLabel.setVisible(false);
                                this.name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        this.email
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                emailValidationLabel.setManaged(false);
                                emailValidationLabel.setVisible(false);
                                this.email.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        this.phone
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                phoneValidationLabel.setManaged(false);
                                phoneValidationLabel.setVisible(false);
                                this.phone.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
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
