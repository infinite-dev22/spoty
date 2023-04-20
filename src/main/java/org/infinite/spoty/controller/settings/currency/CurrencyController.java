package org.infinite.spoty.controller.settings.currency;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.infinite.spoty.model.Currency;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.currencySampleData;

public class CurrencyController implements Initializable {
    private MFXTableView<Currency> currenciesTable;

    @FXML
    public BorderPane currencyContentPane;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> Platform.runLater(() -> {
            currencyContentPane.setCenter(getCurrencyTable());
            setupTable();
        })).start();
    }

    private void setupTable() {
        MFXTableColumn<Currency> currencyName = new MFXTableColumn<>("Name", true, Comparator.comparing(Currency::getCurrencyName));
        MFXTableColumn<Currency> currencyCode = new MFXTableColumn<>("Code", true, Comparator.comparing(Currency::getCurrencyCode));
        MFXTableColumn<Currency> currencySymbol = new MFXTableColumn<>("Symbol", true, Comparator.comparing(Currency::getCurrencySymbol));

        currencyName.setRowCellFactory(currency -> new MFXTableRowCell<>(Currency::getCurrencyName));
        currencyCode.setRowCellFactory(currency -> new MFXTableRowCell<>(Currency::getCurrencyCode));
        currencySymbol.setRowCellFactory(currency -> new MFXTableRowCell<>(Currency::getCurrencySymbol));

        currenciesTable.getTableColumns().addAll(currencyName, currencyCode, currencySymbol);
        currenciesTable.getFilters().addAll(
                new StringFilter<>("Name", Currency::getCurrencyName),
                new StringFilter<>("Code", Currency::getCurrencyCode),
                new StringFilter<>("Symbol", Currency::getCurrencySymbol)
        );

        currenciesTable.setItems(currencySampleData());
    }

    private MFXTableView<Currency> getCurrencyTable() {
        currenciesTable = new MFXTableView<>();
        currenciesTable.setPrefSize(1200, 1000);
        currenciesTable.features().enableBounceEffect();
        currenciesTable.autosizeColumnsOnInitialization();
        currenciesTable.features().enableSmoothScrolling(0.5);
        return currenciesTable;
    }
}
