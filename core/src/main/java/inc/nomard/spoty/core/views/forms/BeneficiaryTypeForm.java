package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.viewModels.hrm.pay_roll.*;
import inc.nomard.spoty.core.views.components.*;
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
public class BeneficiaryTypeForm extends MFXGenericDialog {
    public CustomButton saveBtn;
    public Button cancelBtn;
    public Label nameValidationLabel,
            colorPickerValidationLabel;
    public ValidatableTextField name;
    public ValidatableTextArea description;
    public ValidatableComboBox<String> colorPicker;
    private List<Constraint> nameConstraints,
            colorConstraints;
    private Event actionEvent = null;

    public BeneficiaryTypeForm() {
        init();
    }

    public void init() {
        buildDialogContent();
        requiredValidator();
        dialogOnActions();
    }


    private VBox buildName() {
        // Input.
        name = new ValidatableTextField();
        var label = new Label("Name");
        name.setPrefWidth(400d);
        name.textProperty().bindBidirectional(BeneficiaryTypeViewModel.nameProperty());
        // Validation.
        nameValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, name, nameValidationLabel);
        return vbox;
    }

    private VBox buildColor() {
        // Input.
        colorPicker = new ValidatableComboBox<>();
        var label = new Label("Color");
        colorPicker.setPrefWidth(400d);
        colorPicker.valueProperty().bindBidirectional(BeneficiaryTypeViewModel.colorProperty());
        colorPicker.setItems(BeneficiaryTypeViewModel.getColorsList());
        // Validation.
        colorPickerValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, colorPicker, colorPickerValidationLabel);
        return vbox;
    }

    private VBox buildDescription() {
        // Input.
        description = new ValidatableTextArea();
        var label = new Label("Description");
        description.setPrefWidth(400d);
        description.textProperty().bindBidirectional(BeneficiaryTypeViewModel.descriptionProperty());
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
        vbox.getChildren().addAll(buildName(), buildColor(), buildDescription());
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
                    BeneficiaryTypeViewModel.resetProperties();

                    closeDialog(event);

                    nameValidationLabel.setVisible(false);
                    colorPickerValidationLabel.setVisible(false);

                    nameValidationLabel.setManaged(false);
                    colorPickerValidationLabel.setManaged(false);

                    name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    colorPicker.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    nameConstraints = name.validate();
                    colorConstraints = colorPicker.validate();
                    if (!nameConstraints.isEmpty()) {
                        nameValidationLabel.setManaged(true);
                        nameValidationLabel.setVisible(true);
                        nameValidationLabel.setText(nameConstraints.getFirst().getMessage());
                        name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) name.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!colorConstraints.isEmpty()) {
                        colorPickerValidationLabel.setManaged(true);
                        colorPickerValidationLabel.setVisible(true);
                        colorPickerValidationLabel.setText(colorConstraints.getFirst().getMessage());
                        colorPicker.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) colorPicker.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (nameConstraints.isEmpty()
                            && colorConstraints.isEmpty()) {
                        if (BeneficiaryTypeViewModel.getId() > 0) {
                            BeneficiaryTypeViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                            actionEvent = event;
                            return;
                        }
                        BeneficiaryTypeViewModel.saveBeneficiaryType(this::onSuccess, this::successMessage, this::errorMessage);
                        actionEvent = event;
                    }
                });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        BeneficiaryTypeViewModel.resetProperties();
        BeneficiaryTypeViewModel.getAllBeneficiaryTypes(null, null);
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
        Constraint colorPickerConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Color is required")
                        .setCondition(colorPicker.valueProperty().isNotNull())
                        .get();
        colorPicker.getValidator().constraint(colorPickerConstraint);
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
        colorPicker
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                colorPickerValidationLabel.setManaged(false);
                                colorPickerValidationLabel.setVisible(false);
                                colorPicker.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
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
