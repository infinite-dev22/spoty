package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.viewModels.hrm.employee.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.validation.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.net.*;
import java.util.*;
import javafx.css.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class DesignationFormController implements Initializable {
    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    @FXML
    public MFXTextField name;
    @FXML
    public Label nameValidationLabel;
    @FXML
    public TextArea description;
    @FXML
    public MFXButton saveBtn;
    @FXML
    public MFXButton cancelBtn;
    @FXML
    public StackPane contentPane;
    private List<Constraint> constraints;
    private ActionEvent actionEvent = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        name.textProperty().bindBidirectional(DesignationViewModel.nameProperty());
        description.textProperty().bindBidirectional(DesignationViewModel.descriptionProperty());// Input listeners.
        dialogOnActions();
        requiredValidator();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    DesignationViewModel.clearDesignationData();
                    closeDialog(event);
                    nameValidationLabel.setVisible(false);
                    nameValidationLabel.setManaged(false);
                    name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    constraints = name.validate();
                    if (!constraints.isEmpty()) {
                        nameValidationLabel.setManaged(true);
                        nameValidationLabel.setVisible(true);
                        nameValidationLabel.setText(constraints.getFirst().getMessage());
                        name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) name.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (constraints.isEmpty()) {
                        actionEvent = event;
                        if (DesignationViewModel.getId() > 0) {
                            DesignationViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                            return;
                        }
                        DesignationViewModel.saveDesignation(this::onSuccess, this::successMessage, this::errorMessage);
                    }
                });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        DesignationViewModel.clearDesignationData();
        DesignationViewModel.getAllDesignations(null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint lengthConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Name is required")
                        .setCondition(name.textProperty().length().greaterThan(0))
                        .get();
        name.getValidator().constraint(lengthConstraint);
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
