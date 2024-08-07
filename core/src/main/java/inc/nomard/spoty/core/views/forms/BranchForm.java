package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.viewModels.*;
import static inc.nomard.spoty.core.viewModels.BranchViewModel.*;
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
public class BranchForm extends MFXGenericDialog {
    public CustomButton saveBtn;
    public Button cancelBtn;
    public ValidatableTextField name,
            email,
            phone,
            town,
            city;
    public Label emailValidationLabel,
            cityValidationLabel,
            townValidationLabel,
            phoneValidationLabel,
            nameValidationLabel;
    private List<Constraint> nameConstraints,
            townConstraints,
            cityConstraints;
    private Event actionEvent = null;

    public BranchForm() {
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
        name.textProperty().bindBidirectional(BranchViewModel.nameProperty());
        // Validation.
        nameValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, name, nameValidationLabel);
        return vbox;
    }

    private VBox buildPhone() {
        // Input.
        phone = new ValidatableTextField();
        var label = new Label("Phone");
        phone.setPrefWidth(400d);
        phone.textProperty().bindBidirectional(BranchViewModel.phoneProperty());
        // Validation.
        phoneValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, phone, phoneValidationLabel);
        return vbox;
    }

    private VBox buildTown() {
        // Input.
        town = new ValidatableTextField();
        var label = new Label("Town");
        town.setPrefWidth(400d);
        town.textProperty().bindBidirectional(BranchViewModel.townProperty());
        // Validation.
        townValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, town);
        return vbox;
    }

    private VBox buildCity() {
        // Input.
        city = new ValidatableTextField();
        var label = new Label("City");
        city.setPrefWidth(400d);
        city.textProperty().bindBidirectional(BranchViewModel.cityProperty());
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, city);
        return vbox;
    }

    private VBox buildEmail() {
        // Input.
        email = new ValidatableTextField();
        var label = new Label("Email");
        email.setPrefWidth(400d);
        email.textProperty().bindBidirectional(BranchViewModel.emailProperty());
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, email);
        return vbox;
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(buildName(), buildPhone(), buildEmail(), buildCity(), buildTown());
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
                    clearBranchData();
                    closeDialog(event);

                    nameValidationLabel.setVisible(false);
                    emailValidationLabel.setVisible(false);
                    phoneValidationLabel.setVisible(false);
                    townValidationLabel.setVisible(false);
                    cityValidationLabel.setVisible(false);

                    nameValidationLabel.setManaged(false);
                    emailValidationLabel.setManaged(false);
                    phoneValidationLabel.setManaged(false);
                    townValidationLabel.setManaged(false);
                    cityValidationLabel.setManaged(false);

                    name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    town.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    city.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    nameConstraints = name.validate();
                    townConstraints = town.validate();
                    cityConstraints = city.validate();
                    if (!nameConstraints.isEmpty()) {
                        nameValidationLabel.setManaged(true);
                        nameValidationLabel.setVisible(true);
                        nameValidationLabel.setText(nameConstraints.getFirst().getMessage());
                        name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) name.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!townConstraints.isEmpty()) {
                        townValidationLabel.setManaged(true);
                        townValidationLabel.setVisible(true);
                        townValidationLabel.setText(townConstraints.getFirst().getMessage());
                        town.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) town.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!cityConstraints.isEmpty()) {
                        cityValidationLabel.setManaged(true);
                        cityValidationLabel.setVisible(true);
                        cityValidationLabel.setText(cityConstraints.getFirst().getMessage());
                        city.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) city.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (nameConstraints.isEmpty()
                            && townConstraints.isEmpty()
                            && cityConstraints.isEmpty()) {
                        actionEvent = event;
                        if (BranchViewModel.getId() > 0) {
                            BranchViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                            return;
                        }
                        BranchViewModel.saveBranch(this::onSuccess, this::successMessage, this::errorMessage);
                    }
                });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        BranchViewModel.clearBranchData();
        BranchViewModel.getAllBranches(null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint branchFormNameConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Name is required")
                        .setCondition(name.textProperty().length().greaterThan(0))
                        .get();
        name.getValidator().constraint(branchFormNameConstraint);
        Constraint branchFormCityConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Beneficiary Type is required")
                        .setCondition(city.textProperty().length().greaterThan(0))
                        .get();
        city.getValidator().constraint(branchFormCityConstraint);
        Constraint branchFormTownConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Color is required")
                        .setCondition(town.textProperty().length().greaterThan(0))
                        .get();
        town.getValidator().constraint(branchFormTownConstraint);
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
        city
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                cityValidationLabel.setManaged(false);
                                cityValidationLabel.setVisible(false);
                                city.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        town
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                townValidationLabel.setManaged(false);
                                townValidationLabel.setVisible(false);
                                town.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
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
