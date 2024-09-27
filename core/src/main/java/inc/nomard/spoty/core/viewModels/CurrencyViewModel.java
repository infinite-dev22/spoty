package inc.nomard.spoty.core.viewModels;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;
import java.util.Currency;

public class CurrencyViewModel {
    public static ObservableList<Currency> currenciesList = FXCollections.observableArrayList();
    public static final ListProperty<Currency> currencies = new SimpleListProperty<>(currenciesList);

    public static ObservableList<Currency> getCurrencies() {
        return currencies.get();
    }

    public static ListProperty<Currency> currencyProperty() {
        return currencies;
    }

    public static void getAllCurrencies() {
        currenciesList.clear();
        currenciesList.setAll(Currency.getAvailableCurrencies().stream().toList());
        currenciesList.sort(Comparator.comparing(Currency::getDisplayName));
    }

    public static void searchItem(String search) {
        currenciesList.setAll(
                currenciesList.stream()
                        .filter(
                                currency -> currency.getCurrencyCode().toLowerCase().contains(search.toLowerCase())
                                        || currency.getSymbol().toLowerCase().contains(search.toLowerCase()))
                        .toList());
    }
}
