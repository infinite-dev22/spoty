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

package inc.normad.spoty.core.views.settings.system_settings;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import inc.normad.spoty.utils.SpotyLogger;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class POSController implements Initializable {
    private static POSController instance;
    @FXML
    public AnchorPane posSettingsPane;
    @FXML
    public MFXTextField posReceiptMessage;
    @FXML
    public MFXToggleButton showReceiptBranchPhone,
            showReceiptBranchAddress,
            showReceiptBranchEmail,
            showReceiptCustomer,
            showReceiptTax,
            showReceiptDiscount,
            showReceiptQRCode,
            showReceiptCustomerMessage,
            autoPrintReceipt,
            showServedBy;
    private Preferences preferences;

    public POSController() {
        initPrefs();
    }

    public static POSController getInstance() {
        if (Objects.equals(instance, null)) instance = new POSController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initGUI();
        setPOSPreferences();
    }

    private void initPrefs() {
        if (Objects.equals(preferences, null)) {
            preferences = Preferences.userRoot().node(this.getClass().getName());
        }
    }

    private void initGUI() {
        posReceiptMessage.setText(preferences.get("receiptMessage", "Thank you for choosing us."));
        showReceiptBranchPhone.setSelected(preferences.getBoolean("branch_phone", false));
        showReceiptBranchAddress.setSelected(preferences.getBoolean("branch_address", true));
        showReceiptBranchEmail.setSelected(preferences.getBoolean("branch_email", false));
        showReceiptCustomer.setSelected(preferences.getBoolean("customer_name", false));
        showReceiptTax.setSelected(preferences.getBoolean("receipt_tax", true));
        showReceiptDiscount.setSelected(preferences.getBoolean("receipt_discount", true));
        showReceiptQRCode.setSelected(preferences.getBoolean("receipt_qr_code", true));
        showReceiptCustomerMessage.setSelected(preferences.getBoolean("customer_message", true));
        autoPrintReceipt.setSelected(preferences.getBoolean("auto_print_receipt", true));
        showServedBy.setSelected(preferences.getBoolean("show_served_by", true));
    }

    private void setPOSPreferences() {
        posReceiptMessage
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.put("receiptMessage", newValue);
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });
        showReceiptBranchPhone
                .selectedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.putBoolean("branch_phone", newValue);
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });
        showReceiptBranchAddress
                .selectedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.putBoolean("branch_address", newValue);
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });
        showReceiptBranchEmail
                .selectedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.putBoolean("branch_email", newValue);
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });
        showReceiptCustomer
                .selectedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.putBoolean("customer_name", newValue);
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });
        showReceiptTax
                .selectedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.putBoolean("receipt_tax", newValue);
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });
        showReceiptDiscount
                .selectedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.putBoolean("receipt_discount", newValue);
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });
        showReceiptQRCode
                .selectedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.putBoolean("receipt_qr_code", newValue);
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });
        showReceiptCustomerMessage
                .selectedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.putBoolean("customer_message", newValue);
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });
        autoPrintReceipt
                .selectedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.putBoolean("auto_print_receipt", newValue);
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });
        showServedBy
                .selectedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.putBoolean("show_served_by", newValue);
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });
    }
}
