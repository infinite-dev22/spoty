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

package inc.nomard.spoty.core.views.settings;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.network_bridge.dtos.Currency;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class CurrencyController implements Initializable {
    private static CurrencyController instance;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXTableView<Currency> masterTable;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXButton createBtn;
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

        currencyName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.34));
        currencyCode.prefWidthProperty().bind(masterTable.widthProperty().multiply(.34));
        currencySymbol.prefWidthProperty().bind(masterTable.widthProperty().multiply(.34));

        masterTable.getTableColumns().addAll(currencyName, currencyCode, currencySymbol);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", Currency::getName),
                        new StringFilter<>("Code", Currency::getCode),
                        new StringFilter<>("Symbol", Currency::getSymbol));

        getCurrencyTable();

        if (CurrencyViewModel.getCurrencies().isEmpty()) {
            CurrencyViewModel.getCurrencies()
                    .addListener(
                            (ListChangeListener<Currency>)
                                    c -> masterTable.setItems(CurrencyViewModel.getCurrencies()));
        } else {
            masterTable.itemsProperty().bindBidirectional(CurrencyViewModel.currencyProperty());
        }
    }

    private void getCurrencyTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Currency> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event ->
                                    showContextMenu((MFXTableRow<Currency>) event.getSource())
                                            .show(
                                                    contentPane.getScene().getWindow(),
                                                    event.getScreenX(),
                                                    event.getScreenY());
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<Currency> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    CurrencyViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);

                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    CurrencyViewModel.getItem(obj.getData().getId(), this::createBtnClicked, this::errorMessage);

                    e.consume();
                });
        contextMenu.addItems(edit, delete);
        return contextMenu;
    }

    @FXML
    private void createBtnClicked() {
        dialog.showAndWait();
    }

    private void currencyFormDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/CurrencyForm.fxml");
        fxmlLoader.setControllerFactory(c -> CurrencyFormController.getInstance());
        MFXGenericDialog dialogContent = fxmlLoader.load();
        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setShowClose(false);
        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(contentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();
        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    private void onSuccess() {
    }

    private void successMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);
    }

    private void errorMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);
    }
}
