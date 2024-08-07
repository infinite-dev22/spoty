package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.viewModels.*;
import static inc.nomard.spoty.core.viewModels.CurrencyViewModel.*;
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
public class CurrencyForm extends MFXGenericDialog {
    public ValidatableTextField name,
            code,
            symbol;
    public CustomButton saveBtn;
    public Button cancelBtn;
    public Label codeValidationLabel,
            nameValidationLabel,
            symbolValidationLabel;
    private List<Constraint> currencyFormNameConstraints,
            colorConstraints,
            currencyFormCodeConstraints;
    private Event actionEvent = null;

    public CurrencyForm() {
        init();
    }

    public void init() {
        buildDialogContent();
        requiredValidator();
        dialogOnActions();
    }


    private VBox buildCode() {
        // Input.
        code = new ValidatableTextField();
        var label = new Label("Currency Code");
        code.setPrefWidth(400d);
        code.textProperty().bindBidirectional(CurrencyViewModel.codeProperty());
        // Validation.
        codeValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, code, codeValidationLabel);
        return vbox;
    }

    private VBox buildName() {
        // Input.
        name = new ValidatableTextField();
        var label = new Label("Currency Name");
        name.setPrefWidth(400d);
        name.textProperty().bindBidirectional(CurrencyViewModel.nameProperty());
        // Validation.
        nameValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, name, nameValidationLabel);
        return vbox;
    }

    private VBox buildSymbol() {
        // Input.
        symbol = new ValidatableTextField();
        var label = new Label("Currency Symbol");
        symbol.setPrefWidth(400d);
        symbol.textProperty().bindBidirectional(CurrencyViewModel.symbolProperty());
        CurrencyViewModel.idProperty().addListener((observableValue, oV, nV) -> symbol.setDisable(Objects.nonNull(oV) && (Double) oV > 0 || Objects.nonNull(nV) && (Double) nV > 0));
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, symbol);
        return vbox;
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(buildCode(), buildName(), buildSymbol());
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
                    clearCurrencyData();
                    closeDialog(event);

                    nameValidationLabel.setVisible(false);
                    codeValidationLabel.setVisible(false);
                    symbolValidationLabel.setVisible(false);

                    nameValidationLabel.setManaged(false);
                    codeValidationLabel.setManaged(false);
                    symbolValidationLabel.setManaged(false);

                    name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    code.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    symbol.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    currencyFormNameConstraints = name.validate();
                    colorConstraints = symbol.validate();
                    currencyFormCodeConstraints = code.validate();
                    if (!currencyFormNameConstraints.isEmpty()) {
                        nameValidationLabel.setManaged(true);
                        nameValidationLabel.setVisible(true);
                        nameValidationLabel.setText(currencyFormNameConstraints.getFirst().getMessage());
                        name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!colorConstraints.isEmpty()) {
                        symbolValidationLabel.setManaged(true);
                        symbolValidationLabel.setVisible(true);
                        symbolValidationLabel.setText(colorConstraints.getFirst().getMessage());
                        symbol.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!currencyFormCodeConstraints.isEmpty()) {
                        codeValidationLabel.setManaged(true);
                        codeValidationLabel.setVisible(true);
                        codeValidationLabel.setText(currencyFormCodeConstraints.getFirst().getMessage());
                        code.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (currencyFormNameConstraints.isEmpty()
                            && colorConstraints.isEmpty()
                            && currencyFormCodeConstraints.isEmpty()) {
                        actionEvent = event;
                        if (CurrencyViewModel.getId() > 0) {
                            CurrencyViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                            return;
                        }
                        CurrencyViewModel.saveCurrency(this::onSuccess, this::successMessage, this::errorMessage);
                    }
                });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        CurrencyViewModel.clearCurrencyData();
        CurrencyViewModel.getAllCurrencies(null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint currencyFormNameConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Name is required")
                        .setCondition(name.textProperty().length().greaterThan(0))
                        .get();
        name.getValidator().constraint(currencyFormNameConstraint);
        Constraint currencyFormCodeConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Code is required")
                        .setCondition(code.textProperty().length().greaterThan(0))
                        .get();
        code.getValidator().constraint(currencyFormCodeConstraint);
        Constraint currencyFormSymbolConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Symbol is required")
                        .setCondition(symbol.textProperty().length().greaterThan(0))
                        .get();
        symbol.getValidator().constraint(currencyFormSymbolConstraint);
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
        code
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                codeValidationLabel.setManaged(false);
                                codeValidationLabel.setVisible(false);
                                code.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        symbol
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                symbolValidationLabel.setManaged(false);
                                symbolValidationLabel.setVisible(false);
                                symbol.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
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
