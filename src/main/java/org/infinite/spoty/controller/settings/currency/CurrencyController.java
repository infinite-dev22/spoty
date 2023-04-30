package org.infinite.spoty.controller.settings.currency;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.model.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.data.SampleData.currencySampleData;

public class CurrencyController implements Initializable {
    @FXML
    public MFXTextField currencySearchBar;
    @FXML
    public HBox currencyActionsPane;
    @FXML
    public MFXButton currencyImportBtn;
    @FXML
    public MFXTableView<Currency> currencyTable;
    @FXML
    public BorderPane currencyContentPane;
    private Dialog<ButtonType> dialog;

    public CurrencyController(Stage stage) {
        Platform.runLater(() -> {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            try {
                currencyFormDialogPane(stage);
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage());
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Currency> currencyName = new MFXTableColumn<>("Name", true, Comparator.comparing(Currency::getCurrencyName));
        MFXTableColumn<Currency> currencyCode = new MFXTableColumn<>("Code", true, Comparator.comparing(Currency::getCurrencyCode));
        MFXTableColumn<Currency> currencySymbol = new MFXTableColumn<>("Symbol", true, Comparator.comparing(Currency::getCurrencySymbol));

        currencyName.setRowCellFactory(currency -> new MFXTableRowCell<>(Currency::getCurrencyName));
        currencyCode.setRowCellFactory(currency -> new MFXTableRowCell<>(Currency::getCurrencyCode));
        currencySymbol.setRowCellFactory(currency -> new MFXTableRowCell<>(Currency::getCurrencySymbol));

        currencyTable.getTableColumns().addAll(currencyName, currencyCode, currencySymbol);
        currencyTable.getFilters().addAll(
                new StringFilter<>("Name", Currency::getCurrencyName),
                new StringFilter<>("Code", Currency::getCurrencyCode),
                new StringFilter<>("Symbol", Currency::getCurrencySymbol)
        );
        getCurrencyTable();
        currencyTable.setItems(currencySampleData());
    }

    private void getCurrencyTable() {
        currencyTable.setPrefSize(1200, 1000);
        currencyTable.features().enableBounceEffect();
        currencyTable.autosizeColumnsOnInitialization();
        currencyTable.features().enableSmoothScrolling(0.5);
    }

    @FXML
    private void currencyCreateBtnClicked() {
        dialog.showAndWait();
    }

    private void currencyFormDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/CurrencyForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }
}
