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

package inc.nomard.spoty.core.views;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.components.navigation.*;
import inc.nomard.spoty.core.viewModels.quotations.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.quotations.*;
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
public class QuotationPage extends OutlinePage {
    private final Stage stage;
    private MFXTextField searchBar;
    private MFXTableView<QuotationMaster> masterTable;
    private MFXProgressSpinner progress;
    private MFXButton createBtn;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    public QuotationPage(Stage stage) {
        this.stage = stage;
        Platform.runLater(() ->
        {
            try {
                viewDialogPane(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
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
        progress.setManaged(false);
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
        MFXTableColumn<QuotationMaster> quotationCustomer =
                new MFXTableColumn<>(
                        "Customer", false, Comparator.comparing(QuotationMaster::getCustomerName));
//        MFXTableColumn<QuotationMaster> quotationDate =
//                new MFXTableColumn<>("Date", false, Comparator.comparing(QuotationMaster::getCreatedAt));
        MFXTableColumn<QuotationMaster> quotationTotalAmount =
                new MFXTableColumn<>(
                        "Total Amount", false, Comparator.comparing(QuotationMaster::getTotal));

        quotationCustomer.setRowCellFactory(
                quotation -> new MFXTableRowCell<>(QuotationMaster::getCustomerName));
//        quotationDate.setRowCellFactory(
//                quotation -> new MFXTableRowCell<>(QuotationMaster::getLocaleDate));
        quotationTotalAmount.setRowCellFactory(
                quotation -> new MFXTableRowCell<>(QuotationMaster::getTotal));

        quotationCustomer.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
//        quotationDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        quotationTotalAmount.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));

        masterTable
                .getTableColumns()
                .addAll(
                        quotationCustomer,
//                        quotationDate,
                        quotationTotalAmount);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference", QuotationMaster::getRef),
                        new StringFilter<>("Customer", QuotationMaster::getCustomerName),
                        new DoubleFilter<>("Grand Total", QuotationMaster::getTotal));
        getQuotationMasterTable();

        if (QuotationMasterViewModel.getQuotations().isEmpty()) {
            QuotationMasterViewModel.getQuotations()
                    .addListener(
                            (ListChangeListener<QuotationMaster>)
                                    c -> masterTable.setItems(QuotationMasterViewModel.getQuotations()));
        } else {
            masterTable
                    .itemsProperty()
                    .bindBidirectional(QuotationMasterViewModel.quotationsProperty());
        }
    }

    private void getQuotationMasterTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<QuotationMaster> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<QuotationMaster>) event.getSource())
                                        .show(
                                                masterTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<QuotationMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem view = new MFXContextMenuItem("View");
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            QuotationMasterViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getCustomerName() + "'s quotation", stage, this));
        // Edit
        edit.setOnAction(
                e -> {
                    QuotationMasterViewModel.getQuotationMaster(obj.getData().getId(), this::createBtnAction, this::errorMessage);
                    e.consume();
                });
        // View
        view.setOnAction(
                event -> {
                    viewShow(obj.getData());
                    event.consume();
                });

        contextMenu.addItems(view, edit, delete);

        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> BaseController.navigation.navigate(Pages.getQuotationMasterFormPane()));
    }

    private void onSuccess() {
        QuotationMasterViewModel.getAllQuotationMasters(null, null);
    }

    private void viewDialogPane(Stage stage) throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/QuotationPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new QuotationPreviewController());
        MFXGenericDialog genericDialog = viewFxmlLoader.load();
        genericDialog.setShowMinimize(false);
        genericDialog.setShowAlwaysOnTop(false);

        genericDialog.setPrefHeight(screenHeight * .98);
        genericDialog.setPrefWidth(700);

        viewDialog =
                MFXGenericDialogBuilder.build(genericDialog)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(this)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .setCenterInOwnerNode(false)
                        .setOverlayClose(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(viewDialog.getScene());
    }

    public void viewShow(QuotationMaster quotationMaster) {
        QuotationPreviewController controller = viewFxmlLoader.getController();
        controller.init(quotationMaster);
        viewDialog.showAndWait();
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
                QuotationMasterViewModel.getAllQuotationMasters(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            QuotationMasterViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
            }, this::errorMessage);
        });
    }
}
