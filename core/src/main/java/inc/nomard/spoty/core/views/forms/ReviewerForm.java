package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.util.validation.Constraint;
import inc.nomard.spoty.core.viewModels.TenantSettingViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.EmployeeViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.ValidatableComboBox;
import inc.nomard.spoty.core.views.util.FunctionalStringConverter;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.core.views.util.Validators;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import javafx.beans.property.SimpleStringProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.util.List;

public class ReviewerForm extends BorderPane {
    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    private final ModalPane modalPane;
    public Label employeeValidationLabel,
            levelValidationLabel;
    public CustomButton saveBtn;
    public Button cancelBtn;
    private ValidatableComboBox<Employee> employee;
    private Integer reason;
    private Text subTitle;
    private List<Constraint> employeeConstraints,
            levelConstraints;
    private Spinner<Integer> reviewLevel;

    public ReviewerForm(ModalPane modalPane, Integer reason) {
        this.modalPane = modalPane;
        this.reason = reason;
        buildDialogContent();
        setupComboBox();
        Validators.requiredValidator(employee, employeeValidationLabel, "Employee is required");
//        Validators.requiredValidator(reviewLevel, levelValidationLabel, "Review Level is required");
        dialogOnActions();
        this.setup();
    }

    private VBox buildTitle() {
        var title = new Text("Reviewer");
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
        var label = new Label("Employee");
        employee = new ValidatableComboBox<>();
        employee.setPrefWidth(1000d);
        employee.valueProperty().bindBidirectional(TenantSettingViewModel.employeeProperty());
        employeeValidationLabel = Validators.buildValidationLabel();
        return buildFieldHolder(label, employee, employeeValidationLabel);
    }

    private VBox buildLevel() {
        Label levelLabel = new Label("Review Level");
        reviewLevel = new Spinner<>();
        reviewLevel.setPrefWidth(1000d);
        reviewLevel.getStyleClass().addAll(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        reviewLevel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, TenantSettingViewModel.getReviewLevels(), 0));
        reviewLevel.getValueFactory().valueProperty().bindBidirectional(TenantSettingViewModel.reviewLevelProperty().asObject());
        levelValidationLabel = Validators.buildValidationLabel();

        return buildFieldHolder(levelLabel, reviewLevel, levelValidationLabel);
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(buildTitle(), buildName(), buildLevel());
        return vbox;
    }

    private CustomButton buildSaveButton() {
        saveBtn = new CustomButton("Create");
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
        hbox.setPadding(new Insets(10d));
        hbox.getChildren().addAll(buildSaveButton(), buildCancelButton());
        return hbox;
    }

    private void buildDialogContent() {
        this.setCenter(buildCenter());
        this.setBottom(buildBottom());
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction((_) -> dispose());
        saveBtn.setOnAction(
                (_) -> {
                    employeeConstraints = employee.validate();
//                    levelConstraints = reviewLevel.validate();
                    if (!employeeConstraints.isEmpty()) {
                        employeeValidationLabel.setManaged(true);
                        employeeValidationLabel.setVisible(true);
                        employeeValidationLabel.setText(employeeConstraints.getFirst().getMessage());
                        employee.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
//                    if (!levelConstraints.isEmpty()) {
//                        levelValidationLabel.setManaged(true);
//                        levelValidationLabel.setVisible(true);
//                        levelValidationLabel.setText(levelConstraints.getFirst().getMessage());
//                        reviewLevel.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
//                    }
                    if (employeeConstraints.isEmpty()
                        /*&& levelConstraints.isEmpty()*/) {
                        saveBtn.startLoading();
                        if (reason == 1) {
                            TenantSettingViewModel.editReviewer(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                        } else {
                            TenantSettingViewModel.addReviewer(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                        }
                    }
                });
    }

    private void setupComboBox() {
        StringConverter<Employee> employeeConverter = FunctionalStringConverter.to(
                employee -> (employee == null) ? "" : employee.getName());

        employee.setConverter(employeeConverter);
        employee.setItems(EmployeeViewModel.getEmployees());
    }

    private void onSuccess() {
        dispose();
        TenantSettingViewModel.getTenantSettings(null, null);
    }

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        saveBtn.stopLoading();
    }

    public void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
        TenantSettingViewModel.clearReviewData();
        employee = null;
        employeeValidationLabel = null;
        reviewLevel = null;
        saveBtn = null;
        cancelBtn = null;
        subTitle = null;
        reason = null;
    }

    private void setup() {
        if (reason == 1) {
            saveBtn.textProperty().bindBidirectional(new SimpleStringProperty("Update"));
            subTitle.textProperty().bindBidirectional(new SimpleStringProperty("Update Form"));
        }
    }
}
