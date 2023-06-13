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

package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.viewModels.CustomerViewModel;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static io.github.palexdev.materialfx.utils.StringUtils.containsAll;
import static io.github.palexdev.materialfx.utils.StringUtils.containsAny;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;
import static org.infinite.spoty.GlobalActions.closeDialog;

public class CustomerFormController implements Initializable {
    private static final String[] SPECIALS = "@ . ".split(" ");
    private static final String[] ALPHANUMERICS = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z a b c d e f g h i j k l m n o p q r s t u v w x y z 0 1 2 3 4 5 6 7 8 9".split(" ");
    public MFXTextField customerID = new MFXTextField();
    @FXML
    public MFXFilledButton customerFormSaveBtn;
    @FXML
    public MFXOutlinedButton customerFormCancelBtn;
    @FXML
    public Label customerFormTitle;
    @FXML
    public MFXTextField customerFormName;
    @FXML
    public MFXTextField customerFormEmail;
    @FXML
    public MFXTextField customerFormPhone;
    @FXML
    public MFXTextField customerFormCity;
    @FXML
    public MFXTextField customerFormCountry;
    @FXML
    public MFXTextField customerFormTaxNumber;
    @FXML
    public MFXTextField customerFormAddress;
    @FXML
    public Label validationLabel1, validationLabel2, validationLabel3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input listeners.
        customerFormPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                customerFormPhone.setText(newValue.replaceAll("\\D", ""));
        });
        customerFormPhone.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != oldValue) customerFormPhone.setLeadingIcon(new Label("+"));
            System.out.println("newValue oldValue");
        });
        // Label sizing.
        validationLabel1.setStyle("-fx-font-size: 8;");
        validationLabel2.setStyle("-fx-font-size: 8;");
        validationLabel3.setStyle("-fx-font-size: 8;");
        // Form input binding.
        customerID.textProperty().bindBidirectional(CustomerViewModel.idProperty(), new NumberStringConverter());
        customerFormName.textProperty().bindBidirectional(CustomerViewModel.nameProperty());
        customerFormEmail.textProperty().bindBidirectional(CustomerViewModel.emailProperty());
        customerFormPhone.textProperty().bindBidirectional(CustomerViewModel.phoneProperty());
        customerFormCity.textProperty().bindBidirectional(CustomerViewModel.cityProperty());
        customerFormCountry.textProperty().bindBidirectional(CustomerViewModel.countryProperty());
        customerFormTaxNumber.textProperty().bindBidirectional(CustomerViewModel.taxNumberProperty());
        customerFormAddress.textProperty().bindBidirectional(CustomerViewModel.addressProperty());
        // Name input validation.
        Constraint lengthConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Name field is required.")
                .setCondition(customerFormName.textProperty().length().greaterThan(0))
                .get();
        customerFormName.getValidator().constraint(lengthConstraint);
        customerFormName.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                validationLabel1.setVisible(false);
                customerFormName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
            }
        });
        customerFormName.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                List<Constraint> constraints = customerFormName.validate();
                if (!constraints.isEmpty()) {
                    customerFormName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    validationLabel1.setText(constraints.get(0).getMessage());
                    validationLabel1.setVisible(true);
                }
            }
        });
        // Email input validation.
        Constraint alphaCharsConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Invalid email")
                .setCondition(Bindings.createBooleanBinding(
                        () -> containsAny(customerFormEmail.getText(), "", ALPHANUMERICS),
                        customerFormEmail.textProperty()
                ))
                .get();
        Constraint specialCharsConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Invalid email")
                .setCondition(Bindings.createBooleanBinding(
                        () -> containsAll(customerFormEmail.getText(), "", SPECIALS),
                        customerFormEmail.textProperty()
                ))
                .get();
        customerFormEmail.getValidator().constraint(alphaCharsConstraint).constraint(specialCharsConstraint);
        customerFormEmail.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                validationLabel2.setVisible(false);
                customerFormEmail.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
            }
        });
        customerFormEmail.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                List<Constraint> constraints = customerFormEmail.validate();
                if (!constraints.isEmpty()) {
                    customerFormEmail.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    validationLabel2.setText(constraints.get(0).getMessage());
                    validationLabel2.setVisible(true);
                }
            }
        });
        // Phone input validation.
        Constraint phoneLength = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Invalid length")
                .setCondition(customerFormName.textProperty().length().lessThan(11))
                .get();
        customerFormPhone.getValidator().constraint(phoneLength);
        customerFormPhone.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                validationLabel3.setVisible(false);
                customerFormPhone.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
            }
        });
        customerFormPhone.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                List<Constraint> constraints = customerFormPhone.validate();
                if (!constraints.isEmpty()) {
                    customerFormPhone.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    validationLabel3.setText(constraints.get(0).getMessage());
                    validationLabel3.setVisible(true);
                }
            }
        });
        dialogOnActions();
    }

    private void dialogOnActions() {
        customerFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            validationLabel1.setVisible(false);
            validationLabel2.setVisible(false);
            validationLabel3.setVisible(false);

            CustomerViewModel.resetProperties();
        });
        customerFormSaveBtn.setOnAction((e) -> {
            if (!validationLabel1.isVisible() && !validationLabel2.isVisible() && !validationLabel3.isVisible()) {
                if (Integer.parseInt(customerID.getText()) > 0)
                    CustomerViewModel.updateItem(Integer.parseInt(customerID.getText()));
                else
                    CustomerViewModel.saveCustomer();
                CustomerViewModel.resetProperties();
                closeDialog(e);
                validationLabel1.setVisible(false);
                validationLabel2.setVisible(false);
                validationLabel3.setVisible(false);
            }
        });
    }
}
