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

package org.infinite.spoty.views.settings.currency;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.StringFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.WeakListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.infinite.spoty.database.models.Currency;
import org.infinite.spoty.forms.CurrencyFormController;
import org.infinite.spoty.viewModels.CurrencyViewModel;

@SuppressWarnings("unchecked")
public class CurrencyController implements Initializable {
  private static CurrencyController instance;
  @FXML public MFXTextField currencySearchBar;
  @FXML public HBox currencyActionsPane;
  @FXML public MFXButton currencyImportBtn;
  @FXML public MFXTableView<Currency> currencyTable;
  @FXML public BorderPane currencyContentPane;
  private MFXStageDialog dialog;

  private CurrencyController(Stage stage) {
    Platform.runLater(
        () -> {
          try {
            currencyFormDialogPane(stage);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        });
  }

  public static CurrencyController getInstance(Stage stage) {
    if (instance == null) instance = new CurrencyController(stage);
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Platform.runLater(this::setupTable);
  }

  private void setupTable() {
    MFXTableColumn<Currency> currencyName =
        new MFXTableColumn<>("Name", false, Comparator.comparing(Currency::getName));
    MFXTableColumn<Currency> currencyCode =
        new MFXTableColumn<>("Code", false, Comparator.comparing(Currency::getCode));
    MFXTableColumn<Currency> currencySymbol =
        new MFXTableColumn<>("Symbol", false, Comparator.comparing(Currency::getSymbol));

    currencyName.setRowCellFactory(currency -> new MFXTableRowCell<>(Currency::getName));
    currencyCode.setRowCellFactory(currency -> new MFXTableRowCell<>(Currency::getCode));
    currencySymbol.setRowCellFactory(currency -> new MFXTableRowCell<>(Currency::getSymbol));

    currencyName.prefWidthProperty().bind(currencyTable.widthProperty().multiply(.34));
    currencyCode.prefWidthProperty().bind(currencyTable.widthProperty().multiply(.34));
    currencySymbol.prefWidthProperty().bind(currencyTable.widthProperty().multiply(.34));

    currencyTable.getTableColumns().addAll(currencyName, currencyCode, currencySymbol);
    currencyTable
        .getFilters()
        .addAll(
            new StringFilter<>("Name", Currency::getName),
            new StringFilter<>("Code", Currency::getCode),
            new StringFilter<>("Symbol", Currency::getSymbol));

    getCurrencyTable();

    currencyTable.setItems(CurrencyViewModel.getCurrenciesList());

    CurrencyViewModel.currenciesList.addListener(
        new WeakListChangeListener<>(
            c -> currencyTable.setItems(CurrencyViewModel.getCurrenciesList())));
  }

  private void getCurrencyTable() {
    currencyTable.setPrefSize(1200, 1000);
    currencyTable.features().enableBounceEffect();
    currencyTable.features().enableSmoothScrolling(0.5);

    currencyTable.setTableRowFactory(
        t -> {
          MFXTableRow<Currency> row = new MFXTableRow<>(currencyTable, t);
          EventHandler<ContextMenuEvent> eventHandler =
              event ->
                  showContextMenu((MFXTableRow<Currency>) event.getSource())
                      .show(
                          currencyContentPane.getScene().getWindow(),
                          event.getScreenX(),
                          event.getScreenY());
          row.setOnContextMenuRequested(eventHandler);
          return row;
        });
  }

  private MFXContextMenu showContextMenu(MFXTableRow<Currency> obj) {
    MFXContextMenu contextMenu = new MFXContextMenu(currencyTable);

    MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
    MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

    // Actions
    // Delete
    delete.setOnAction(
        e -> {
          CurrencyViewModel.deleteItem(obj.getData().getId());
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          CurrencyViewModel.getItem(obj.getData().getId());
          currencyCreateBtnClicked();
          e.consume();
        });

    contextMenu.addItems(edit, delete);

    return contextMenu;
  }

  @FXML
  private void currencyCreateBtnClicked() {
    dialog.showAndWait();
  }

  private void currencyFormDialogPane(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = fxmlLoader("forms/CurrencyForm.fxml");
    fxmlLoader.setControllerFactory(c -> CurrencyFormController.getInstance(stage));

    MFXGenericDialog dialogContent = fxmlLoader.load();

    dialogContent.setShowMinimize(false);
    dialogContent.setShowAlwaysOnTop(false);

    dialog =
        MFXGenericDialogBuilder.build(dialogContent)
            .toStageDialogBuilder()
            .initOwner(stage)
            .initModality(Modality.WINDOW_MODAL)
            .setOwnerNode(currencyContentPane)
            .setScrimPriority(ScrimPriority.WINDOW)
            .setScrimOwner(true)
            .get();

    io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
  }
}
