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

package inc.nomard.spoty.core.views.forms;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.components.navigation.*;
import inc.nomard.spoty.core.viewModels.adjustments.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.network_bridge.dtos.adjustments.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import lombok.extern.slf4j .*;

@SuppressWarnings("unchecked")
@Slf4j
public class AdjustmentMasterFormController implements Initializable {
    private static AdjustmentMasterFormController instance;
    @FXML
    public MFXTableView<AdjustmentDetail> adjustmentDetailTable;
    @FXML
    public MFXTextField adjustmentNote;
    @FXML
    public BorderPane adjustmentFormContentPane;
    @FXML
    public Label adjustmentFormTitle;
    @FXML
    public MFXButton adjustmentProductAddBtn, saveBtn, cancelBtn;
    private MFXStageDialog dialog;

    private AdjustmentMasterFormController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        quotationProductDialogPane(stage);
                    } catch (IOException e) {
                        SpotyLogger.writeToFile(e, this.getClass());
                    }
                });
    }

    public static AdjustmentMasterFormController getInstance(Stage stage) {
        if (instance == null) instance = new AdjustmentMasterFormController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input binding.
        adjustmentNote.textProperty().bindBidirectional(AdjustmentMasterViewModel.noteProperty());

        // combBox properties.
        adjustmentAddProductBtnClicked();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<AdjustmentDetail> productName =
                new MFXTableColumn<>(
                        "Product", false, Comparator.comparing(AdjustmentDetail::getProductName));
        MFXTableColumn<AdjustmentDetail> productQuantity =
                new MFXTableColumn<>(
                        "Quantity", false, Comparator.comparing(AdjustmentDetail::getQuantity));
        MFXTableColumn<AdjustmentDetail> adjustmentType =
                new MFXTableColumn<>(
                        "Adjustment Type", false, Comparator.comparing(AdjustmentDetail::getAdjustmentType));

        productName.setRowCellFactory(
                product -> new MFXTableRowCell<>(AdjustmentDetail::getProductName));
        productQuantity.setRowCellFactory(
                product -> new MFXTableRowCell<>(AdjustmentDetail::getQuantity));
        adjustmentType.setRowCellFactory(
                product -> new MFXTableRowCell<>(AdjustmentDetail::getAdjustmentType));

        productName.prefWidthProperty().bind(adjustmentDetailTable.widthProperty().multiply(.5));
        productQuantity.prefWidthProperty().bind(adjustmentDetailTable.widthProperty().multiply(.5));
        adjustmentType.prefWidthProperty().bind(adjustmentDetailTable.widthProperty().multiply(.5));

        adjustmentDetailTable.getTableColumns().addAll(productName, productQuantity, adjustmentType);

        adjustmentDetailTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", AdjustmentDetail::getProductName),
                        new LongFilter<>("Quantity", AdjustmentDetail::getQuantity),
                        new StringFilter<>("Adjustment Type", AdjustmentDetail::getAdjustmentType));

        getAdjustmentDetailTable();

        if (AdjustmentDetailViewModel.getAdjustmentDetails().isEmpty()) {
            AdjustmentDetailViewModel.getAdjustmentDetails()
                    .addListener(
                            (ListChangeListener<AdjustmentDetail>)
                                    change ->
                                            adjustmentDetailTable.setItems(
                                                    AdjustmentDetailViewModel.getAdjustmentDetails()));
        } else {
            adjustmentDetailTable
                    .itemsProperty()
                    .bindBidirectional(AdjustmentDetailViewModel.adjustmentDetailsProperty());
        }
    }

    private void getAdjustmentDetailTable() {
        adjustmentDetailTable.setPrefSize(1000, 1000);
        adjustmentDetailTable.features().enableBounceEffect();
        adjustmentDetailTable.features().enableSmoothScrolling(0.5);

        adjustmentDetailTable.setTableRowFactory(
                adjustmentDetail -> {
                    MFXTableRow<AdjustmentDetail> row = new MFXTableRow<>(adjustmentDetailTable, adjustmentDetail);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<AdjustmentDetail>) event.getSource())
                                        .show(
                                                adjustmentDetailTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<AdjustmentDetail> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(adjustmentDetailTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        // Actions
        // Delete
        delete.setOnAction(
                event -> {
                    AdjustmentDetailViewModel.removeAdjustmentDetail(
                            obj.getData().getId(),
                            AdjustmentDetailViewModel.adjustmentDetailsList.indexOf(obj.getData()));
                    event.consume();
                });
        // Edit
        edit.setOnAction(
                event -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    AdjustmentDetailViewModel.getAdjustmentDetail(obj.getData());
                                } catch (Exception e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            });
                    dialog.showAndWait();
                    event.consume();
                });
        contextMenu.addItems(edit, delete);
        return contextMenu;
    }

    private void adjustmentAddProductBtnClicked() {
        adjustmentProductAddBtn.setOnAction(e -> dialog.showAndWait());
    }

    private void quotationProductDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/AdjustmentDetailForm.fxml");
        fxmlLoader.setControllerFactory(c -> AdjustmentDetailFormController.getInstance());

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setShowClose(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(adjustmentFormContentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void adjustmentSaveBtnClicked() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        if (AdjustmentDetailViewModel.adjustmentDetailsList.isEmpty()) {
            SpotyMessage notification =
                    new SpotyMessage.MessageBuilder("Table can't be Empty")
                            .duration(MessageDuration.SHORT)
                            .icon("fas-triangle-exclamation")
                            .type(MessageVariants.ERROR)
                            .build();
            notificationHolder.addMessage(notification);
            return;
        }
        if (AdjustmentMasterViewModel.getId() > 0) {
            try {
                AdjustmentMasterViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
            } catch (Exception e) {
                SpotyLogger.writeToFile(e, this.getClass());
            }
            return;
        }
        try {
            AdjustmentMasterViewModel.saveAdjustmentMaster(this::onAction, this::onAddSuccess, this::onFailed);
        } catch (Exception e) {
            SpotyLogger.writeToFile(e, this.getClass());
        }
        onRequiredFieldsMissing();
    }

    public void adjustmentCancelBtnClicked() {
        BaseController.navigation.navigate(Pages.getAdjustmentPane());

        AdjustmentMasterViewModel.resetProperties();
    }

    private void onAction() {
        cancelBtn.setDisable(true);
        saveBtn.setDisable(true);
//        cancelBtn.setManaged(true);
//        saveBtn.setManaged(true);
    }

    private void onAddSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Adjustment added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);
        adjustmentCancelBtnClicked();
        AdjustmentMasterViewModel.getAllAdjustmentMasters(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Adjustment updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);
        adjustmentCancelBtnClicked();
        AdjustmentMasterViewModel.getAllAdjustmentMasters(null, null, null);
    }

    private void onFailed() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("An error occurred")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        AdjustmentMasterViewModel.getAllAdjustmentMasters(null, null, null);
    }

    private void onRequiredFieldsMissing() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Required fields can't be null")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        AdjustmentMasterViewModel.getAllAdjustmentMasters(null, null, null);
    }
}
