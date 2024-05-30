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

package inc.nomard.spoty.core.views.transfer;

import inc.nomard.spoty.core.components.animations.SpotyAnimations;
import inc.nomard.spoty.core.components.navigation.Pages;
import inc.nomard.spoty.core.viewModels.transfers.TransferMasterViewModel;
import inc.nomard.spoty.core.views.BaseController;
import inc.nomard.spoty.core.views.previews.TransferPreviewController;
import inc.nomard.spoty.network_bridge.dtos.transfers.TransferMaster;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.fxmlLoader;
import lombok.extern.java.Log;

@SuppressWarnings("unchecked")
@Log
public class TransferController implements Initializable {
    private static TransferController instance;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXTableView<TransferMaster> masterTable;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public HBox refresh;
    private RotateTransition transition;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    public TransferController(Stage stage) {
        Platform.runLater(() ->
        {
            try {
                viewDialogPane(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static TransferController getInstance(Stage stage) {
        if (instance == null) instance = new TransferController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<TransferMaster> transferFromBranch =
                new MFXTableColumn<>(
                        "Branch(From)", false, Comparator.comparing(TransferMaster::getFromBranchName));
        MFXTableColumn<TransferMaster> transferToBranch =
                new MFXTableColumn<>(
                        "Branch(To)", false, Comparator.comparing(TransferMaster::getToBranchName));
        MFXTableColumn<TransferMaster> status =
                new MFXTableColumn<>("Status", false, Comparator.comparing(TransferMaster::getStatus));
        MFXTableColumn<TransferMaster> transferDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(TransferMaster::getDate));
        MFXTableColumn<TransferMaster> transferTotalCost =
                new MFXTableColumn<>("Total Amount", false, Comparator.comparing(TransferMaster::getTotal));

        transferFromBranch.setRowCellFactory(
                transfer -> new MFXTableRowCell<>(TransferMaster::getFromBranchName));
        transferToBranch.setRowCellFactory(
                transfer -> new MFXTableRowCell<>(TransferMaster::getToBranchName));
        status.setRowCellFactory(transfer -> new MFXTableRowCell<>(TransferMaster::getStatus));
        transferDate.setRowCellFactory(
                transfer -> new MFXTableRowCell<>(TransferMaster::getLocaleDate));
        transferTotalCost.setRowCellFactory(
                transfer -> new MFXTableRowCell<>(TransferMaster::getTotal));

        transferFromBranch.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        transferToBranch.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        status.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        transferTotalCost.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        transferDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));

        masterTable
                .getTableColumns()
                .addAll(
                        transferFromBranch, transferToBranch, status, transferDate, transferTotalCost);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference", TransferMaster::getRef),
                        new StringFilter<>("Branch(From)", TransferMaster::getFromBranchName),
                        new StringFilter<>("Branch(To)", TransferMaster::getToBranchName),
                        new StringFilter<>("Status", TransferMaster::getStatus),
                        new DoubleFilter<>("Total Amount", TransferMaster::getTotal));
        getTransferMasterTable();

        if (TransferMasterViewModel.getTransfers().isEmpty()) {
            TransferMasterViewModel.getTransfers()
                    .addListener(
                            (ListChangeListener<TransferMaster>)
                                    c -> masterTable.setItems(TransferMasterViewModel.getTransfers()));
        } else {
            masterTable
                    .itemsProperty()
                    .bindBidirectional(TransferMasterViewModel.transfersProperty());
        }
    }

    private void getTransferMasterTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<TransferMaster> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<TransferMaster>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<TransferMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        MFXContextMenuItem view = new MFXContextMenuItem("View");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    TransferMasterViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            });

                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    TransferMasterViewModel.getItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            });

                    createBtnClicked();

                    e.consume();
                });
        // View
        view.setOnAction(
                event -> {
                    try {
                        viewShow(obj.getData());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    event.consume();
                });

        contextMenu.addItems(view, edit, delete);

        return contextMenu;
    }

    @FXML
    private void createBtnClicked() {
        BaseController.navigation.navigate(Pages.getTransferMasterFormPane());
    }

    private void onAction() {
        transition.playFromStart();
        transition.setOnFinished((ActionEvent event) -> transition.playFromStart());
    }

    private void onSuccess() {
        transition.setOnFinished(null);
    }

    private void onFailed() {
        transition.setOnFinished(null);
    }

    private void setIcons() {
        var refreshIcon = new MFXFontIcon("fas-arrows-rotate", 24);
        refresh.getChildren().addFirst(refreshIcon);

        transition = SpotyAnimations.rotateTransition(refreshIcon, Duration.millis(1000), 360);

        refreshIcon.setOnMouseClicked(mouseEvent -> TransferMasterViewModel.getTransferMasters(this::onAction, this::onSuccess, this::onFailed));
    }

    private void viewDialogPane(Stage stage) throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/TransferPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new TransferPreviewController());
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

    public void viewShow(TransferMaster transferMaster) {
        TransferPreviewController controller = viewFxmlLoader.getController();
        controller.init(transferMaster);
        viewDialog.showAndWait();
    }
}
