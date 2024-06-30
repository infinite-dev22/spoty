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

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.Currency;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxcomponents.theming.enums.*;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class CurrencyPage extends OutlinePage {
    private final Stage stage;
    private MFXTextField searchBar;
    private MFXTableView<Currency> masterTable;
    private MFXProgressSpinner progress;
    private MFXButton createBtn;
    private MFXStageDialog dialog;

    public CurrencyPage(Stage stage) {
        this.stage = stage;
        Platform.runLater(
                () -> {
                    try {
                        currencyFormDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
        addNode(init());
    }

    public BorderPane init() {
        var pane = new BorderPane();
        pane.setTop(buildTop());
        pane.setCenter(buildCenter());
        setIcons();
        setSearchBar();
        setupTable();
        createBtnAction();
        return pane;
    }

    private HBox buildLeftTop() {
        progress = new MFXProgressSpinner();
        progress.setMinSize(30d, 30d);
        progress.setPrefSize(30d, 30d);
        progress.setMaxSize(30d, 30d);
        progress.setVisible(false);
        var hbox = new HBox(progress);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildCenterTop() {
        searchBar = new MFXTextField();
        searchBar.setPromptText("Search accounts");
        searchBar.setFloatMode(FloatMode.DISABLED);
        searchBar.setMinWidth(300d);
        searchBar.setPrefWidth(500d);
        searchBar.setMaxWidth(700d);
        var hbox = new HBox(searchBar);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildRightTop() {
        createBtn = new MFXButton("Create");
        createBtn.setVariants(ButtonVariants.FILLED);
        var hbox = new HBox(createBtn);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildTop() {
        var hbox = new HBox();
        hbox.getStyleClass().add("card-flat");
        BorderPane.setAlignment(hbox, Pos.CENTER);
        hbox.setPadding(new Insets(5d));
        hbox.getChildren().addAll(buildLeftTop(), buildCenterTop(), buildRightTop());
        return hbox;
    }

    private AnchorPane buildCenter() {
        masterTable = new MFXTableView<>();
        AnchorPane.setBottomAnchor(masterTable, 0d);
        AnchorPane.setLeftAnchor(masterTable, 0d);
        AnchorPane.setRightAnchor(masterTable, 0d);
        AnchorPane.setTopAnchor(masterTable, 10d);
        return new AnchorPane(masterTable);
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
                                                    this.getScene().getWindow(),
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
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            CurrencyViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getName(), stage, this));
        // Edit
        edit.setOnAction(
                e -> {
                    CurrencyViewModel.getItem(obj.getData().getId(), this::createBtnAction, this::errorMessage);

                    e.consume();
                });
        contextMenu.addItems(edit, delete);
        return contextMenu;
    }

    private void createBtnAction() {
        createBtn.setOnAction(event -> dialog.showAndWait());
    }

    private void currencyFormDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/CurrencyForm.fxml");
        fxmlLoader.setControllerFactory(c -> CurrencyFormController.getInstance(stage));
        MFXGenericDialog dialogContent = fxmlLoader.load();
        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setShowClose(false);
        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(this)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();
        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    private void onSuccess() {
        CurrencyViewModel.getAllCurrencies(null, null);
    }

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, "fas-circle-check");
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, "fas-triangle-exclamation");
    }

    private void displayNotification(String message, MessageVariants type, String icon) {
        SpotyMessage notification = new SpotyMessage.MessageBuilder(message)
                .duration(MessageDuration.SHORT)
                .icon(icon)
                .type(type)
                .height(60)
                .build();
        AnchorPane.setTopAnchor(notification, 5.0);
        AnchorPane.setRightAnchor(notification, 5.0);

        var in = Animations.slideInDown(notification, Duration.millis(250));
        if (!BaseController.getInstance(stage).morphPane.getChildren().contains(notification)) {
            BaseController.getInstance(stage).morphPane.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification, stage));
        }
    }

    private void setIcons() {
        searchBar.setTrailingIcon(new MFXFontIcon("fas-magnifying-glass"));
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                CurrencyViewModel.getAllCurrencies(null, null);
            }
            progress.setVisible(true);
            CurrencyViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
            }, this::errorMessage);
        });
    }
}
