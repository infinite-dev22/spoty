package inc.nomard.spoty.core.views.util;

import atlantafx.base.theme.*;
import inc.nomard.spoty.core.views.components.label_components.controls.*;
import inc.nomard.spoty.core.views.components.validatables.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import java.util.regex.*;
import javafx.beans.binding.*;
import javafx.scene.control.*;

public class Validators {
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String PHONE_REGEX =
            "^\\+?(\\d{1,3})?[\\s-]?\\(?\\d{1,4}\\)?[\\s-]" +
                    "?\\d{1,4}[\\s-]?\\d{1,4}[\\s-]?\\d{1,4}$";
    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])" +
                    "[A-Za-z\\d@$!%*?&]{8,}$";

    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
    private static final Pattern phonePattern = Pattern.compile(PHONE_REGEX);
    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);

    private static boolean isValidEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    private static boolean isValidPassword(String password) {
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.matches();
    }

    private static boolean isValidPhone(String phone) {
        Matcher matcher = phonePattern.matcher(phone);
        if (!matcher.matches()) {
            return false;
        }

        // Remove all non-digit characters and check the length
        String digitsOnly = phone.replaceAll("\\D", "");
        return digitsOnly.length() >= 7 && digitsOnly.length() <= 15;
    }

    public static void requiredValidator(ValidatableTextField control, Label validationLabel, String message) {
        Constraint constraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage(message)
                        .setCondition(control.textProperty().length().greaterThan(0))
                        .get();
        control.getValidator().constraint(constraint);
        control
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                validationLabel.setManaged(false);
                                validationLabel.setVisible(false);
                                control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    public static void requiredValidator(
            ValidatablePasswordField control,
            Label validationLabel,
            String message1,
            String message2,
            String message3) {
        Constraint constraint1 =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage(message1)
                        .setCondition(control.textProperty().length().greaterThan(0))
                        .get();
        Constraint constraint2 =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage(message2)
                        .setCondition(control.textProperty().length().greaterThan(7))
                        .get();
        Constraint constraint3 =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage(message3)
                        .setCondition(
                                Bindings.createBooleanBinding(
                                        () -> isValidPassword(control.getText()),
                                        control.textProperty()))
                        .get();
        control.getValidator().constraint(constraint1).constraint(constraint2).constraint(constraint3);
        control
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                validationLabel.setManaged(false);
                                validationLabel.setVisible(false);
                                control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    public static void requiredValidator(ValidatablePasswordField control, Label validationLabel, String message) {
        Constraint constraint1 =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage(message)
                        .setCondition(control.textProperty().length().greaterThan(0))
                        .get();
        control.getValidator().constraint(constraint1);
        control
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                validationLabel.setManaged(false);
                                validationLabel.setVisible(false);
                                control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    public static void matchingValidator(ValidatablePasswordField control1, ValidatablePasswordField control2, Label validationLabel, String message) {
        Constraint constraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage(message)
                        .setCondition(
                                Bindings.createBooleanBinding(
                                        () -> control2.getText().equals(control1.getText()),
                                        control1.textProperty(),
                                        control2.textProperty()))
                        .get();
        control2.getValidator().constraint(constraint);
        control2
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                validationLabel.setManaged(false);
                                validationLabel.setVisible(false);
                                control2.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    public static void requiredValidator(LabeledDatePicker control, Label validationLabel, String message) {
        Constraint constraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage(message)
                        .setCondition(control.valueProperty().isNotNull())
                        .get();
        control.getValidator().constraint(constraint);
        control
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                validationLabel.setManaged(false);
                                validationLabel.setVisible(false);
                                control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    public static <T> void requiredValidator(LabeledComboBox<T> control, Label validationLabel, String message) {
        Constraint constraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage(message)
                        .setCondition(control.valueProperty().isNotNull())
                        .get();
        control.getValidator().constraint(constraint);
        control
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                validationLabel.setManaged(false);
                                validationLabel.setVisible(false);
                                control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    public static void emailValidator(ValidatableTextField control, Label validationLabel, String message) {
        Constraint constraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage(message)
                        .setCondition(
                                Bindings.createBooleanBinding(
                                        () -> isValidEmail(control.getText()),
                                        control.textProperty()))
                        .get();
        control.getValidator().constraint(constraint);
        control
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                validationLabel.setManaged(false);
                                validationLabel.setVisible(false);
                                control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    public static void phoneValidator(ValidatableTextField control, Label validationLabel, String message) {
        Constraint validPhoneConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage(message)
                        .setCondition(
                                Bindings.createBooleanBinding(
                                        () -> isValidPhone(control.getText()),
                                        control.textProperty()))
                        .get();
        control.getValidator().constraint(validPhoneConstraint);
        control
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                validationLabel.setManaged(false);
                                validationLabel.setVisible(false);
                                control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    public static Label buildValidationLabel() {
        var label = new Label();
        label.setManaged(false);
        label.setVisible(false);
        label.setWrapText(true);
        label.getStyleClass().addAll(Styles.DANGER, Styles.TEXT_SMALL, "validation-label");
        return label;
    }
}
