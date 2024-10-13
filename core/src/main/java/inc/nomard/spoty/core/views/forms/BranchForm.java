package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.viewModels.BranchViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextField;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.core.views.util.Validators;
import inc.nomard.spoty.core.util.validation.Constraint;
import inc.nomard.spoty.core.util.validation.Severity;
import javafx.beans.property.SimpleStringProperty;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;

@Log4j2
public class BranchForm extends BorderPane {
    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    private final ModalPane modalPane;
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
    private Integer reason;
    private Text subTitle;
    private List<Constraint> nameConstraints,
            townConstraints,
            cityConstraints;
    private Event actionEvent = null;

    public BranchForm(ModalPane modalPane, Integer reason) {
        this.modalPane = modalPane;
        this.reason = reason;
        buildDialogContent();
        requiredValidator();
        dialogOnActions();
        setup();
    }

    private VBox buildTitle() {
        var title = new Text("Branch");
        title.getStyleClass().add(Styles.TITLE_3);
        subTitle = new Text("Create Form");
        subTitle.getStyleClass().add(Styles.TITLE_4);
        return buildFieldHolder(title, subTitle, buildSeparator());
    }

    private VBox buildFieldHolder(Node... nodes) {
        VBox vbox = new VBox();
        vbox.setSpacing(5d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(nodes);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        return vbox;
    }

    private Separator buildSeparator() {
        var separator = new Separator();
        separator.setPrefWidth(200.0);
        HBox.setHgrow(separator, Priority.ALWAYS);
        return separator;
    }

    private VBox buildName() {
        // Input.
        name = new ValidatableTextField();
        var label = new Label("Name");
        name.setPrefWidth(1000d);
        name.textProperty().bindBidirectional(BranchViewModel.nameProperty());
        // Validation.
        nameValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        return buildFieldHolder(label, name, nameValidationLabel);
    }

    private VBox buildPhone() {
        // Input.
        phone = new ValidatableTextField();
        var label = new Label("Phone");
        phone.setPrefWidth(1000d);
        phone.textProperty().bindBidirectional(BranchViewModel.phoneProperty());
        // Validation.
        phoneValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        return buildFieldHolder(label, phone, phoneValidationLabel);
    }

    private VBox buildTown() {
        // Input.
        town = new ValidatableTextField();
        var label = new Label("Town");
        town.setPrefWidth(1000d);
        town.textProperty().bindBidirectional(BranchViewModel.townProperty());
        // Validation.
        townValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        return buildFieldHolder(label, town);
    }

    private VBox buildCity() {
        // Input.
        city = new ValidatableTextField();
        var label = new Label("City");
        city.setPrefWidth(1000d);
        city.textProperty().bindBidirectional(BranchViewModel.cityProperty());
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        return buildFieldHolder(label, city);
    }

    private VBox buildEmail() {
        // Input.
        email = new ValidatableTextField();
        var label = new Label("Email");
        email.setPrefWidth(1000d);
        email.textProperty().bindBidirectional(BranchViewModel.emailProperty());
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        return buildFieldHolder(label, email);
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(buildTitle(), buildName(), buildPhone(), buildEmail(), buildCity(), buildTown());
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
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction((event) -> dispose());
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
                    }
                    if (!townConstraints.isEmpty()) {
                        townValidationLabel.setManaged(true);
                        townValidationLabel.setVisible(true);
                        townValidationLabel.setText(townConstraints.getFirst().getMessage());
                        town.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!cityConstraints.isEmpty()) {
                        cityValidationLabel.setManaged(true);
                        cityValidationLabel.setVisible(true);
                        cityValidationLabel.setText(cityConstraints.getFirst().getMessage());
                        city.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (nameConstraints.isEmpty()
                            && townConstraints.isEmpty()
                            && cityConstraints.isEmpty()) {
                        saveBtn.startLoading();
                        if (BranchViewModel.getId() > 0) {
                            BranchViewModel.updateItem(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                        } else {
                            BranchViewModel.saveBranch(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                        }
                    }
                });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        BranchViewModel.clearBranchData();
        BranchViewModel.getAllBranches(null, null, null, null);
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

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        saveBtn.stopLoading();
    }

    public void dispose() {
        modalPane.setPersistent(false);
        modalPane.hide(true);
        BranchViewModel.clearBranchData();
        saveBtn = null;
        cancelBtn = null;
        name = null;
        email = null;
        phone = null;
        town = null;
        city = null;
        emailValidationLabel = null;
        cityValidationLabel = null;
        townValidationLabel = null;
        phoneValidationLabel = null;
        nameValidationLabel = null;
        reason = null;
        subTitle = null;
        nameConstraints = null;
        townConstraints = null;
        cityConstraints = null;
    }

    private void setup() {
        if (reason == 1) {
            saveBtn.textProperty().bindBidirectional(new SimpleStringProperty("Update"));
            subTitle.textProperty().bindBidirectional(new SimpleStringProperty("Update Form"));
        } else {
            saveBtn.textProperty().bindBidirectional(new SimpleStringProperty("Create"));
            subTitle.textProperty().bindBidirectional(new SimpleStringProperty("Create Form"));
        }
    }
}
