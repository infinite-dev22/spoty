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

package org.infinite.spoty.views.settings.system;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.checkbox.MFXCheckbox;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import org.infinite.spoty.GlobalActions;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.Currency;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.CurrencyViewModel;

public class SystemController implements Initializable {
    private static SystemController instance;
    @FXML
    public MFXFilterComboBox<Currency> defaultCurrency;
    @FXML
    public MFXFilterComboBox<Locale> defaultLanguage;
    @FXML
    public MFXTextField defaultEmail;
    @FXML
    public MFXTextField companyName;
    @FXML
    public MFXTextField companyPhone;
    @FXML
    public MFXFilterComboBox<Branch> defaultBranch;
    @FXML
    public MFXTextField branchAddress;
    @FXML
    public MFXCheckbox invoiceFooter;
    @FXML
    public GridPane systemSettings;
    private Preferences preferences;

    public SystemController() {
        initPrefs();
    }

    public static SystemController getInstance() {
        if (Objects.equals(instance, null)) instance = new SystemController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Locale> list = List.of(Locale.getAvailableLocales());
        ObservableList<Locale> localeList = FXCollections.observableArrayList(list);
        // Input listeners.
        // Input bindings.

        // Combo box Converter.
        StringConverter<Currency> currencyConverter =
                FunctionalStringConverter.to(currency -> (currency == null) ? "" : currency.getName());

        StringConverter<Branch> branchConverter =
                FunctionalStringConverter.to(branch -> (branch == null) ? "" : branch.getName());

        StringConverter<Locale> localeConverter =
                FunctionalStringConverter.to(locale -> (locale == null) ? "" : locale.getCountry());

        // Combo box Filter Function.
        Function<String, Predicate<Currency>> currencyFilterFunction =
                searchStr ->
                        currency ->
                                StringUtils.containsIgnoreCase(currencyConverter.toString(currency), searchStr);

        Function<String, Predicate<Branch>> branchFilterFunction =
                searchStr ->
                        branch -> StringUtils.containsIgnoreCase(branchConverter.toString(branch), searchStr);

        Function<String, Predicate<Locale>> localeFilterFunction =
                searchStr ->
                        locale -> StringUtils.containsIgnoreCase(localeConverter.toString(locale), searchStr);

        // Combo box properties.
        defaultCurrency.setItems(CurrencyViewModel.getCurrencies());
        defaultCurrency.setConverter(currencyConverter);
        defaultCurrency.setFilterFunction(currencyFilterFunction);

        defaultBranch.setItems(BranchViewModel.getBranches());
        defaultBranch.setConverter(branchConverter);
        defaultBranch.setFilterFunction(branchFilterFunction);

        defaultLanguage.setItems(localeList);
        defaultLanguage.setConverter(localeConverter);
        defaultLanguage.setFilterFunction(localeFilterFunction);

        try {
            initGUI();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setSystemPreferences();
    }

    private void initPrefs() {
        if (Objects.equals(preferences, null)) {
            preferences = Preferences.userRoot().node(this.getClass().getName());
        }
    }

    private void initGUI() throws SQLException {
        GlobalActions.spotyThreadPool().execute(() -> {
            try {
                BranchViewModel.getItem(preferences.getInt("default_branch", 1));
                CurrencyViewModel.getItem(preferences.getInt("default_currency", 1));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        Locale locale = new Locale(preferences.get("default_language", "EN"));

        defaultCurrency.setValue(CurrencyViewModel.getCurrency());
        defaultLanguage.setValue(locale);
        defaultBranch.setValue(BranchViewModel.getBranch());
        defaultEmail.setText(preferences.get("branch_email", ""));
        companyName.setText(preferences.get("company_name", ""));
        companyPhone.setText(preferences.get("company_phone", ""));
        branchAddress.setText(preferences.get("branch_address", ""));
        invoiceFooter.setSelected(preferences.getBoolean("invoice_footer", false));
    }

    private void setSystemPreferences() {
        defaultCurrency
                .valueProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.putInt("default_currency", newValue.getId());
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                throw new RuntimeException(e);
                            }
                        });
        defaultLanguage
                .valueProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.put("default_language", newValue.getDisplayLanguage());
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                throw new RuntimeException(e);
                            }
                        });
        defaultBranch
                .valueProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.putInt("default_branch", newValue.getId());
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                throw new RuntimeException(e);
                            }
                        });
        defaultEmail
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.put("branch_email", newValue);
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                throw new RuntimeException(e);
                            }
                        });
        companyName
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.put("company_name", newValue);
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                throw new RuntimeException(e);
                            }
                        });
        companyPhone
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.put("company_phone", newValue);
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                throw new RuntimeException(e);
                            }
                        });
        branchAddress
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.put("branch_address", newValue);
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                throw new RuntimeException(e);
                            }
                        });
        invoiceFooter
                .selectedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.putBoolean("invoice_footer", newValue);
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                throw new RuntimeException(e);
                            }
                        });
    }
}
