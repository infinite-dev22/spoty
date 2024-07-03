package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.net.*;
import java.util.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class ExpenseFormController implements Initializable {
    @FXML
    public MFXTextField amount,
            name;
    @FXML
    public TextArea description;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    @FXML
    public MFXDatePicker date;
    @FXML
    public MFXComboBox<ExpenseCategory> category;
    @FXML
    public Label nameValidationLabel,
            dateValidationLabel,
            categoryValidationLabel,
            amountValidationLabel;
    private List<Constraint> nameConstraints,
            dateConstraints,
            categoryConstraints,
            amountConstraints;
    private ActionEvent actionEvent = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form Bindings.
        name.textProperty().bindBidirectional(ExpensesViewModel.nameProperty());
        date.textProperty().bindBidirectional(ExpensesViewModel.dateProperty());
        category.valueProperty().bindBidirectional(ExpensesViewModel.categoryProperty());
        amount.textProperty().bindBidirectional(ExpensesViewModel.amountProperty());
        description.textProperty().bindBidirectional(ExpensesViewModel.detailsProperty());

        // ComboBox Converters.
        StringConverter<ExpenseCategory> expenseCategoryConverter =
                FunctionalStringConverter.to(
                        expenseCategory -> (expenseCategory == null) ? "" : expenseCategory.getName());

        // Combo box properties.
        category.setItems(ExpenseCategoryViewModel.getCategories());
        category.setConverter(expenseCategoryConverter);

        // Input listeners.
        requiredValidator();

        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    ExpensesViewModel.resetProperties();
                    category.clearSelection();

                    nameValidationLabel.setVisible(false);
                    dateValidationLabel.setVisible(false);
                    categoryValidationLabel.setVisible(false);
                    amountValidationLabel.setVisible(false);

                    nameValidationLabel.setManaged(false);
                    dateValidationLabel.setManaged(false);
                    categoryValidationLabel.setManaged(false);
                    amountValidationLabel.setManaged(false);

                    date.setValue(null);
                    category.clearSelection();

                    name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    date.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    category.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    amount.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    nameConstraints = name.validate();
                    dateConstraints = date.validate();
                    categoryConstraints = category.validate();
                    amountConstraints = amount.validate();
                    if (!nameConstraints.isEmpty()) {
                        nameValidationLabel.setManaged(true);
                        nameValidationLabel.setVisible(true);
                        nameValidationLabel.setText(nameConstraints.getFirst().getMessage());
                        name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) name.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!dateConstraints.isEmpty()) {
                        dateValidationLabel.setManaged(true);
                        dateValidationLabel.setVisible(true);
                        dateValidationLabel.setText(dateConstraints.getFirst().getMessage());
                        date.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) date.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!categoryConstraints.isEmpty()) {
                        categoryValidationLabel.setManaged(true);
                        categoryValidationLabel.setVisible(true);
                        categoryValidationLabel.setText(categoryConstraints.getFirst().getMessage());
                        category.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) category.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!amountConstraints.isEmpty()) {
                        amountValidationLabel.setManaged(true);
                        amountValidationLabel.setVisible(true);
                        amountValidationLabel.setText(amountConstraints.getFirst().getMessage());
                        amount.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) amount.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (nameConstraints.isEmpty()
                            && dateConstraints.isEmpty()
                            && categoryConstraints.isEmpty()
                            && amountConstraints.isEmpty()) {
                        if (ExpensesViewModel.getId() > 0) {
                            ExpensesViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                            actionEvent = event;
                            return;
                        }
                        ExpensesViewModel.saveExpense(this::onSuccess, this::successMessage, this::errorMessage);
                        actionEvent = event;
                    }
                });
    }

    private void onSuccess() {
        date.setValue(null);
        category.clearSelection();
        category.clearSelection();
        closeDialog(actionEvent);
        ExpensesViewModel.resetProperties();
        ExpensesViewModel.getAllExpenses(null, null);
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
        Constraint dateConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Date is required")
                        .setCondition(date.textProperty().length().greaterThan(0))
                        .get();
        date.getValidator().constraint(dateConstraint);
        Constraint categoryConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Category is required")
                        .setCondition(category.textProperty().length().greaterThan(0))
                        .get();
        category.getValidator().constraint(categoryConstraint);
        Constraint amountConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Amount is required")
                        .setCondition(amount.textProperty().length().greaterThan(0))
                        .get();
        amount.getValidator().constraint(amountConstraint);
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
        date
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                dateValidationLabel.setManaged(false);
                                dateValidationLabel.setVisible(false);
                                date.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        category
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                categoryValidationLabel.setManaged(false);
                                categoryValidationLabel.setVisible(false);
                                category.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        amount
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                amountValidationLabel.setManaged(false);
                                amountValidationLabel.setVisible(false);
                                amount.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
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
