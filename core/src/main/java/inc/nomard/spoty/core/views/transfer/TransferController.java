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

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.components.navigation.*;
import inc.nomard.spoty.core.viewModels.transfers.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.network_bridge.dtos.transfers.*;
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
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<TransferMaster> transferFromBranch =
                new MFXTableColumn<>(
                        "Branch(From)", false, Comparator.comparing(TransferMaster::getFromBranchName));
        MFXTableColumn<TransferMaster> transferToBranch =
                new MFXTableColumn<>(
                        "Branch(To)", false, Comparator.comparing(TransferMaster::getToBranchName));
        MFXTableColumn<TransferMaster> transferDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(TransferMaster::getDate));
        MFXTableColumn<TransferMaster> note =
                new MFXTableColumn<>("Total Amount", false, Comparator.comparing(TransferMaster::getNotes));

        transferFromBranch.setRowCellFactory(
                transfer -> new MFXTableRowCell<>(TransferMaster::getFromBranchName));
        transferToBranch.setRowCellFactory(
                transfer -> new MFXTableRowCell<>(TransferMaster::getToBranchName));
        transferDate.setRowCellFactory(
                transfer -> new MFXTableRowCell<>(TransferMaster::getLocaleDate));
        note.setRowCellFactory(
                transfer -> new MFXTableRowCell<>(TransferMaster::getNotes));

        transferFromBranch.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        transferToBranch.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        note.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        transferDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));

        masterTable
                .getTableColumns()
                .addAll(
                        transferFromBranch, transferToBranch, transferDate, note);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference", TransferMaster::getRef),
                        new StringFilter<>("Branch(From)", TransferMaster::getFromBranchName),
                        new StringFilter<>("Branch(To)", TransferMaster::getToBranchName));
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
                    TransferMasterViewModel.deleteTransfer(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    TransferMasterViewModel.getTransfer(obj.getData().getId(), this::createBtnClicked, this::errorMessage);
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

    @FXML
    private void createBtnClicked() {
        BaseController.navigation.navigate(Pages.getTransferMasterFormPane());
    }

    private void onSuccess() {
        TransferMasterViewModel.getAllTransferMasters(null, null);
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
}
