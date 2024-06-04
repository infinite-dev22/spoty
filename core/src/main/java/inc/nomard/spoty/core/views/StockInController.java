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

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.components.navigation.*;
import inc.nomard.spoty.core.viewModels.stock_ins.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.network_bridge.dtos.stock_ins.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxresources.fonts.*;
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
public class StockInController implements Initializable {
    private static StockInController instance;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXTableView<StockInMaster> masterTable;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public HBox leftHeaderPane;
    @FXML
    public MFXProgressSpinner progress;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    public StockInController(Stage stage) {
        Platform.runLater(() ->
        {
            try {
                viewDialogPane(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static StockInController getInstance(Stage stage) {
        if (instance == null) instance = new StockInController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        setSearchBar();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<StockInMaster> note =
                new MFXTableColumn<>("Note", false, Comparator.comparing(StockInMaster::getNotes));
//        MFXTableColumn<StockInMaster> stockInDate =
//                new MFXTableColumn<>("Date", false, Comparator.comparing(StockInMaster::getCreatedAt));
        MFXTableColumn<StockInMaster> stockInTotalCost =
                new MFXTableColumn<>("Total Amount", false, Comparator.comparing(StockInMaster::getTotal));

        note.setRowCellFactory(stockIn -> new MFXTableRowCell<>(StockInMaster::getNotes));
        stockInTotalCost.setRowCellFactory(stockIn -> new MFXTableRowCell<>(StockInMaster::getTotal));
//        stockInDate.setRowCellFactory(stockIn -> new MFXTableRowCell<>(StockInMaster::getLocaleDate));

        note.prefWidthProperty().bind(masterTable.widthProperty().multiply(.3));
        stockInTotalCost.prefWidthProperty().bind(masterTable.widthProperty().multiply(.3));
//        stockInDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.3));

        masterTable
                .getTableColumns()
                .addAll(note, stockInTotalCost);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference", StockInMaster::getRef),
                        new DoubleFilter<>("Total Amount", StockInMaster::getTotal));
        getStockInMasterTable();

        if (StockInMasterViewModel.getStockIns().isEmpty()) {
            StockInMasterViewModel.getStockIns()
                    .addListener(
                            (ListChangeListener<StockInMaster>)
                                    c -> masterTable.setItems(StockInMasterViewModel.getStockIns()));
        } else {
            masterTable
                    .itemsProperty()
                    .bindBidirectional(StockInMasterViewModel.stockInsProperty());
        }
    }

    private void getStockInMasterTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<StockInMaster> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<StockInMaster>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<StockInMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
//        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
//        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        MFXContextMenuItem view = new MFXContextMenuItem("View");

        // Actions
        // Delete
//        delete.setOnAction(
//                e -> {
//                    StockInMasterViewModel.deleteStockInMaster(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
//                    e.consume();
//                });
        // Edit
//        edit.setOnAction(
//                e -> {
//                    StockInMasterViewModel.getStockInMaster(obj.getData().getId(), this::createBtnClicked, this::errorMessage);
//                    e.consume();
//                });
        // View
        view.setOnAction(
                event -> {
                    viewShow(obj.getData());
                    event.consume();
                });

        contextMenu.addItems(view/*, edit, delete*/);

        return contextMenu;
    }

    @FXML
    private void createBtnClicked() {
        BaseController.navigation.navigate(Pages.getStockInMasterFormPane());
    }

    private void onSuccess() {
        StockInMasterViewModel.getAllStockInMasters(null, null);
    }

    private void viewDialogPane(Stage stage) throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/StockInPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new StockInPreviewController());
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
                        .setOwnerNode(contentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .setCenterInOwnerNode(false)
                        .setOverlayClose(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(viewDialog.getScene());
    }

    public void viewShow(StockInMaster stockInMaster) {
        StockInPreviewController controller = viewFxmlLoader.getController();
        controller.init(stockInMaster);
        viewDialog.showAndWait();
    }

    private void successMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
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

    private void setIcons() {
        searchBar.setTrailingIcon(new MFXFontIcon("fas-magnifying-glass"));
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                StockInMasterViewModel.getAllStockInMasters(null, null);
            }
            progress.setVisible(true);
            StockInMasterViewModel.searchStockInMaster(nv, () -> {
                progress.setVisible(false);
            }, this::errorMessage);
        });
    }
}
