package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.viewModels.*;
import static inc.nomard.spoty.core.viewModels.TaxViewModel.*;
import inc.nomard.spoty.core.views.components.label_components.controls.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;import javafx.scene.control.Label;
import io.github.palexdev.mfxresources.fonts.*;
import java.util.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import javafx.util.converter.*;
import lombok.extern.java.*;

@Log
public class DiscountForm extends MFXGenericDialog {
    @FXML
    public LabeledTextField name,
            percentage;
    @FXML
    public Label nameValidationLabel,
            percentageValidationLabel;
    @FXML
    public Button saveBtn,
            cancelBtn;
    private List<Constraint> nameConstraints,
            percentageConstraints;
    private ActionEvent actionEvent = null;

    public DiscountForm() {
        init();
    }

    public void init() {
        buildDialogContent();
        requiredValidator();
        dialogOnActions();
    }


    private VBox buildName() {
        // Input.
        name = new LabeledTextField();
        name.setLabel("Name");
        name.setPrefWidth(400d);
        name.textProperty().bindBidirectional(DiscountViewModel.nameProperty());
        // Validation.
        nameValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(name, nameValidationLabel);
        return vbox;
    }

    private VBox buildPercentage() {
        // Input.
        percentage = new LabeledTextField();
        percentage.setLabel("Percentage");
        percentage.setPrefWidth(400d);
        percentage.setRight(new MFXFontIcon("fas-percent"));
        percentage.textProperty().bindBidirectional(DiscountViewModel.percentageProperty(), new NumberStringConverter());
        // Validation.
        percentageValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(percentage, percentageValidationLabel);
        return vbox;
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(buildName(), buildPercentage());
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
                    resetProperties();
                    closeDialog(event);
                    nameValidationLabel.setVisible(false);
                    percentageValidationLabel.setVisible(false);
                    nameValidationLabel.setManaged(false);
                    percentageValidationLabel.setManaged(false);
                    name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    percentage.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    nameConstraints = name.validate();
                    percentageConstraints = percentage.validate();
                    if (!nameConstraints.isEmpty()) {
                        nameValidationLabel.setManaged(true);
                        nameValidationLabel.setVisible(true);
                        nameValidationLabel.setText(nameConstraints.getFirst().getMessage());
                        name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) name.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!percentageConstraints.isEmpty()) {
                        percentageValidationLabel.setManaged(true);
                        percentageValidationLabel.setVisible(true);
                        percentageValidationLabel.setText(percentageConstraints.getFirst().getMessage());
                        percentage.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) percentage.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (nameConstraints.isEmpty()
                            && percentageConstraints.isEmpty()) {
                        if (DiscountViewModel.getId() > 0) {
                            DiscountViewModel.updateDiscount(this::onSuccess, this::successMessage, this::errorMessage);
                            actionEvent = event;
                            return;
                        }
                        DiscountViewModel.saveDiscount(this::onSuccess, this::successMessage, this::errorMessage);
                        actionEvent = event;
                    }
                });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        DiscountViewModel.resetProperties();
        DiscountViewModel.getDiscounts(null, null);
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
        Constraint percentageConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Percentage is required")
                        .setCondition(percentage.textProperty().length().greaterThan(0))
                        .get();
        percentage.getValidator().constraint(percentageConstraint);
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
        percentage
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                percentageValidationLabel.setManaged(false);
                                percentageValidationLabel.setVisible(false);
                                percentage.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
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
