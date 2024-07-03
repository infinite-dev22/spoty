package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.viewModels.*;
import static inc.nomard.spoty.core.viewModels.TaxViewModel.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxcore.controls.*;
import io.github.palexdev.mfxresources.fonts.*;
import java.net.*;
import java.util.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import javafx.util.converter.*;
import lombok.extern.java.*;

@Log
public class TaxFormController implements Initializable {
    @FXML
    public MFXTextField name,
            percentage;
    @FXML
    public Label nameValidationLabel,
            percentageValidationLabel;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    private List<Constraint> nameConstraints,
            percentageConstraints;
    private ActionEvent actionEvent = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Input bindings.
        name.textProperty().bindBidirectional(TaxViewModel.nameProperty());
        percentage.textProperty().bindBidirectional(TaxViewModel.percentageProperty(), new NumberStringConverter());
        percentage.setTrailingIcon(new MFXFontIcon("fas-percent"));
        // Input listeners.
        requiredValidator();
        dialogOnActions();
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
                        if (TaxViewModel.getId() > 0) {
                            TaxViewModel.updateTax(this::onSuccess, this::successMessage, this::errorMessage);
                            actionEvent = event;
                            return;
                        }
                        saveTax(this::onSuccess, this::successMessage, this::errorMessage);
                        actionEvent = event;
                    }
                });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        TaxViewModel.resetProperties();
        TaxViewModel.getTaxes(null, null);
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
