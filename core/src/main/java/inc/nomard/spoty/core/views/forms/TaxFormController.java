package inc.nomard.spoty.core.views.forms;

import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
import static inc.nomard.spoty.core.viewModels.TaxViewModel.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxcore.controls.*;
import java.net.*;
import java.util.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.util.converter.*;
import lombok.extern.slf4j.*;

@Slf4j
public class TaxFormController implements Initializable {
    private static TaxFormController instance;
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

    public static TaxFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new TaxFormController();
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Input bindings.
        name.textProperty().bindBidirectional(TaxViewModel.nameProperty());
        percentage.textProperty().bindBidirectional(TaxViewModel.percentageProperty(), new NumberStringConverter());
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
                            try {
                                TaxViewModel.updateTax(this::onAction, this::onUpdatedSuccess, this::onFailed);
                                actionEvent = event;
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                            return;
                        }

                        try {
                            saveTax(this::onAction, this::onAddSuccess, this::onFailed);
                            actionEvent = event;
                        } catch (Exception e) {
                            SpotyLogger.writeToFile(e, this.getClass());
                        }
                    }
                });
    }

    private void onAction() {
        cancelBtn.setDisable(true);
        saveBtn.setDisable(true);
//        cancelBtn.setManaged(true);
//        saveBtn.setManaged(true);
    }

    private void onAddSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Tax added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        closeDialog(actionEvent);
        resetProperties();
        TaxViewModel.getTaxes(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Tax updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        closeDialog(actionEvent);
        resetProperties();
        TaxViewModel.getTaxes(null, null, null);
    }

    private void onFailed() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("An error occurred")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        TaxViewModel.getTaxes(null, null, null);
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
}
