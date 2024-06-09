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
import inc.nomard.spoty.core.viewModels.transfers.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.network_bridge.dtos.transfers.*;
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
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class TransferController implements Initializable {
    private static TransferController instance;
    private final Stage stage;
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
    public MFXProgressSpinner progress;
    @FXML
    public HBox leftHeaderPane;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    public TransferController(Stage stage) {
        this.stage = stage;
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
        setSearchBar();
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
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            TransferMasterViewModel.deleteTransfer(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getFromBranchName() + " - " + obj.getData().getToBranchName() + " transfer", stage, contentPane));
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
                TransferMasterViewModel.getAllTransferMasters(null, null);
            }
            progress.setVisible(true);
            TransferMasterViewModel.searchTransfer(nv, () -> {
                progress.setVisible(false);
            }, this::errorMessage);
        });
    }
}
