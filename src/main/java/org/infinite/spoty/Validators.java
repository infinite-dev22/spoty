/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package org.infinite.spoty;

import static io.github.palexdev.materialfx.utils.StringUtils.containsAll;
import static io.github.palexdev.materialfx.utils.StringUtils.containsAny;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;
import static org.infinite.spoty.values.strings.Values.*;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;

public class Validators {
  public static void emailValidator(MFXTextField textField, Label errorDisplay) {
    // Email input validation.
    Constraint alphaCharsConstraint =
        Constraint.Builder.build()
            .setSeverity(Severity.ERROR)
            .setMessage("Invalid email")
            .setCondition(
                Bindings.createBooleanBinding(
                    () ->
                        containsAny(textField.getText(), "", UPPERCHARACTERS)
                            && containsAny(textField.getText(), "", LOWERCHARACTERS)
                            && containsAny(textField.getText(), "", NUMBERS),
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
                  errorDisplay.setText(constraints.get(0).getMessage());
                  errorDisplay.setVisible(true);
                }
              }
            });
  }

  public static void lengthValidator(
      MFXTextField textField, int length, String message, Label errorDisplay) {
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
                  errorDisplay.setText(constraints.get(0).getMessage());
                  errorDisplay.setVisible(true);
                }
              }
            });
  }

  public static void requiredValidator(MFXTextField textField, String message, Label errorDisplay) {
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
                  errorDisplay.setText(constraints.get(0).getMessage());
                  errorDisplay.setVisible(true);
                }
              }
            });
  }
}
