package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.viewModels.*;
import static inc.nomard.spoty.core.viewModels.CurrencyViewModel.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import io.github.palexdev.materialfx.controls.*;
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
public class CurrencyFormController implements Initializable {
    @FXML
    public MFXTextField name,
            code,
            symbol;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    @FXML
    public Label codeValidationLabel,
            nameValidationLabel,
            symbolValidationLabel;
    private List<Constraint> currencyFormNameConstraints,
            colorConstraints,
            currencyFormCodeConstraints;
    private ActionEvent actionEvent = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        code.textProperty().bindBidirectional(CurrencyViewModel.codeProperty());
        name.textProperty().bindBidirectional(CurrencyViewModel.nameProperty());
        symbol.textProperty().bindBidirectional(CurrencyViewModel.symbolProperty());
        // Input listeners.
        requiredValidator();
        dialogOnActions();
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
