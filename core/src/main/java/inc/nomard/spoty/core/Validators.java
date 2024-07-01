package inc.nomard.spoty.core;

import static inc.nomard.spoty.core.values.strings.Values.*;
import io.github.palexdev.materialfx.controls.*;
import static io.github.palexdev.materialfx.utils.StringUtils.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.util.*;
import javafx.beans.binding.*;
import javafx.scene.control.*;
import lombok.extern.java.*;

@Log
public class Validators {
    static MFXValidator validator;
    static List<Constraint> validate;

    public static void emailValidator(
            MFXTextField textField, Label errorDisplay, MFXButton actionButton) {
        // Email input validation.
        Constraint alphaCharsConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Invalid email")
                        .setCondition(
                                Bindings.createBooleanBinding(
                                        () -> containsAny(textField.getText(), "", ALPHANUMERIC),
                                        textField.textProperty()))
                        .get();
        Constraint specialCharsConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Invalid email")
                        .setCondition(
                                Bindings.createBooleanBinding(
                                        () -> containsAll(textField.getText(), "", SPECIALS), textField.textProperty()))
                        .get();
        textField.getValidator().constraint(alphaCharsConstraint).constraint(specialCharsConstraint);
        // Display error.
        textField
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                errorDisplay.setManaged(false);
                                errorDisplay.setVisible(false);
                                textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        textField
                .delegateFocusedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (oldValue && !newValue) {
                                List<Constraint> constraints = textField.validate();
                                if (!constraints.isEmpty()) {
                                    textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                                    errorDisplay.setText(constraints.getFirst().getMessage());
                                    errorDisplay.setManaged(true);
                                    errorDisplay.setVisible(true);
                                }
                            }
                        });

        actionButton.setOnAction(
                e -> {
                    validator = textField.getValidator();
                    validate = validator.validate();
                    if (!validate.isEmpty()) {
                        errorDisplay.setManaged(true);
                        errorDisplay.setVisible(true);
                        textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        errorDisplay.setText(validate.getFirst().getMessage());
                    }
                });
    }

    public static void lengthValidator(
            MFXTextField textField,
            int length,
            String message,
            Label errorDisplay,
            MFXButton actionButton) {
        // Phone input validation.
        Constraint phoneLength =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage(message)
                        .setCondition(textField.textProperty().length().lessThan(length))
                        .get();
        textField.getValidator().constraint(phoneLength);
        // Display error.
        textField
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                errorDisplay.setManaged(false);
                                errorDisplay.setVisible(false);
                                textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        textField
                .delegateFocusedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (oldValue && !newValue) {
                                List<Constraint> constraints = textField.validate();
                                if (!constraints.isEmpty()) {
                                    textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                                    errorDisplay.setText(constraints.getFirst().getMessage());
                                    errorDisplay.setManaged(true);
                                    errorDisplay.setVisible(true);
                                }
                            }
                        });

        actionButton.setOnAction(
                e -> {
                    validator = textField.getValidator();
                    validate = validator.validate();
                    if (!validate.isEmpty()) {
                        errorDisplay.setManaged(true);
                        errorDisplay.setVisible(true);
                        textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        errorDisplay.setText(validate.getFirst().getMessage());
                    }
                });
    }

    public static void requiredValidator(
            MFXTextField textField, String message, Label errorDisplay, MFXButton actionButton) {
        // Name input validation.
        Constraint lengthConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage(message)
                        .setCondition(textField.textProperty().length().greaterThan(0))
                        .get();
        textField.getValidator().constraint(lengthConstraint);
        // Display error.
        textField
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                errorDisplay.setManaged(false);
                                errorDisplay.setVisible(false);
                                textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });

        actionButton.setOnAction(
                e -> {
                    validate = textField.validate();
                    if (!validate.isEmpty()) {
                        errorDisplay.setManaged(true);
                        errorDisplay.setVisible(true);
                        textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        errorDisplay.setText(validate.getFirst().getMessage());
                    }
                });
    }
}
