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
import inc.nomard.spoty.core.viewModels.adjustments.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.network_bridge.dtos.adjustments.*;
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
public class AdjustmentController implements Initializable {
    private static AdjustmentController instance;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXProgressSpinner progress;
    @FXML
    public HBox leftHeaderPane;
    @FXML
    private MFXTableView<AdjustmentMaster> masterTable;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    public AdjustmentController(Stage stage) {
        Platform.runLater(() ->
        {
            try {
                viewDialogPane(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static AdjustmentController getInstance(Stage stage) {
        if (instance == null) instance = new AdjustmentController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        setSearchBar();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<AdjustmentMaster> adjustmentStatus =
                new MFXTableColumn<>("Status", false, Comparator.comparing(AdjustmentMaster::getStatus));
        MFXTableColumn<AdjustmentMaster> adjustmentTotalAmount =
                new MFXTableColumn<>(
                        "Total Amount", false, Comparator.comparing(AdjustmentMaster::getTotal));
        adjustmentStatus.setRowCellFactory(
                adjustment -> new MFXTableRowCell<>(AdjustmentMaster::getStatus));
        adjustmentTotalAmount.setRowCellFactory(
                adjustment -> new MFXTableRowCell<>(AdjustmentMaster::getTotal));
        adjustmentStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));
        adjustmentTotalAmount
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.5));
        masterTable
                .getTableColumns()
                .addAll(adjustmentStatus, adjustmentTotalAmount);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Status", AdjustmentMaster::getStatus),
                        new DoubleFilter<>("Total Amount", AdjustmentMaster::getTotal));
        getAdjustmentMasterTable();
        if (AdjustmentMasterViewModel.getAdjustmentMasters().isEmpty()) {
            AdjustmentMasterViewModel.getAdjustmentMasters()
                    .addListener(
                            (ListChangeListener<AdjustmentMaster>)
                                    c ->
                                            masterTable.setItems(
                                                    AdjustmentMasterViewModel.getAdjustmentMasters()));
        } else {
            masterTable
                    .itemsProperty()
                    .bindBidirectional(AdjustmentMasterViewModel.adjustmentsProperty());
        }
    }

    private void getAdjustmentMasterTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<AdjustmentMaster> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<AdjustmentMaster>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<AdjustmentMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
//        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
//        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        MFXContextMenuItem view = new MFXContextMenuItem("View");

        // Actions
        // Delete
//        delete.setOnAction(
//                e -> {
//                    AdjustmentMasterViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
//                    e.consume();
//                });
        // Edit
//        edit.setOnAction(
//                e -> {
//                    AdjustmentMasterViewModel.getAdjustmentMaster(obj.getData().getId(), this::createBtnClicked, this::errorMessage);
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

    public void createBtnClicked() {
        BaseController.navigation.navigate(Pages.getAdjustmentMasterFormPane());
    }

    private void onSuccess() {
        AdjustmentMasterViewModel.getAllAdjustmentMasters(null, null);
    }

    private void viewDialogPane(Stage stage) throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/AdjustmentPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new AdjustmentPreviewController());
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

    public void viewShow(AdjustmentMaster adjustmentMaster) {
        AdjustmentPreviewController controller = viewFxmlLoader.getController();
        controller.init(adjustmentMaster);
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
                AdjustmentMasterViewModel.getAllAdjustmentMasters(null, null);
            }
            progress.setVisible(true);
            AdjustmentMasterViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
            }, this::errorMessage);
        });
    }
}
