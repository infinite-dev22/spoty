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

import inc.nomard.spoty.core.components.navigation.Pages;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.MessageDuration;
import inc.nomard.spoty.core.components.message.enums.MessageVariants;
import inc.nomard.spoty.core.viewModels.BranchViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.DesignationViewModel;
import inc.nomard.spoty.core.viewModels.transfers.TransferDetailViewModel;
import inc.nomard.spoty.core.viewModels.transfers.TransferMasterViewModel;
import inc.nomard.spoty.core.views.BaseController;
import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.transfers.TransferDetail;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.fxmlLoader;
import static inc.nomard.spoty.core.Validators.requiredValidator;

@SuppressWarnings("unchecked")
public class TransferMasterFormController implements Initializable {
    private static TransferMasterFormController instance;
    @FXML
    public MFXFilterComboBox<Branch> transferMasterFromBranch;
    @FXML
    public MFXFilterComboBox<Branch> transferMasterToBranch;
    @FXML
    public MFXDatePicker transferMasterDate;
    @FXML
    public MFXTableView<TransferDetail> transferDetailTable;
    @FXML
    public MFXTextField transferMasterNote;
    @FXML
    public BorderPane transferMasterFormContentPane;
    @FXML
    public Label transferMasterFormTitle;
    @FXML
    public MFXButton transferMasterProductAddBtn;
    @FXML
    public Label transferMasterDateValidationLabel;
    @FXML
    public Label transferMasterToBranchValidationLabel;
    @FXML
    public Label transferMasterFromBranchValidationLabel;
    @FXML
    public MFXButton saveBtn, cancelBtn;
    private MFXStageDialog dialog;

    private TransferMasterFormController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        quotationProductDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static TransferMasterFormController getInstance(Stage stage) {
        if (instance == null) instance = new TransferMasterFormController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input binding.
        transferMasterFromBranch
                .valueProperty()
                .bindBidirectional(TransferMasterViewModel.fromBranchProperty());
        transferMasterToBranch
                .valueProperty()
                .bindBidirectional(TransferMasterViewModel.toBranchProperty());
        transferMasterDate.textProperty().bindBidirectional(TransferMasterViewModel.dateProperty());
        transferMasterNote.textProperty().bindBidirectional(TransferMasterViewModel.noteProperty());

        // ComboBox Converters.
        StringConverter<Branch> branchConverter =
                FunctionalStringConverter.to(branch -> (branch == null) ? "" : branch.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<Branch>> branchFilterFunction =
                searchStr ->
                        branch -> StringUtils.containsIgnoreCase(branchConverter.toString(branch), searchStr);

        // ComboBox properties.
        transferMasterFromBranch.setItems(BranchViewModel.getBranches());
        transferMasterFromBranch.setConverter(branchConverter);
        transferMasterFromBranch.setFilterFunction(branchFilterFunction);

        transferMasterToBranch.setItems(BranchViewModel.getBranches());
        transferMasterToBranch.setConverter(branchConverter);
        transferMasterToBranch.setFilterFunction(branchFilterFunction);

        // input validators.
        requiredValidator(
                transferMasterToBranch,
                "Receiving branch is required.",
                transferMasterToBranchValidationLabel,
                saveBtn);
        requiredValidator(
                transferMasterFromBranch,
                "Supplying branch is required.",
                transferMasterFromBranchValidationLabel,
                saveBtn);
        requiredValidator(
                transferMasterDate,
                "Date is required.",
                transferMasterDateValidationLabel,
                saveBtn);

        transferMasterAddProductBtnClicked();

        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<TransferDetail> productName =
                new MFXTableColumn<>(
                        "Product", false, Comparator.comparing(TransferDetail::getProductName));
        MFXTableColumn<TransferDetail> productQuantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(TransferDetail::getQuantity));
        MFXTableColumn<TransferDetail> productDescription =
                new MFXTableColumn<>(
                        "Description", false, Comparator.comparing(TransferDetail::getDescription));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(TransferDetail::getProductName));
        productQuantity.setRowCellFactory(
                product -> new MFXTableRowCell<>(TransferDetail::getQuantity));
        productDescription.setRowCellFactory(
                product -> new MFXTableRowCell<>(TransferDetail::getDescription));

        productName.prefWidthProperty().bind(transferDetailTable.widthProperty().multiply(.5));
        productQuantity.prefWidthProperty().bind(transferDetailTable.widthProperty().multiply(.5));
        productDescription.prefWidthProperty().bind(transferDetailTable.widthProperty().multiply(.5));

        transferDetailTable.getTableColumns().addAll(productName, productQuantity, productDescription);

        transferDetailTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", TransferDetail::getProductName),
                        new IntegerFilter<>("Quantity", TransferDetail::getQuantity),
                        new StringFilter<>("Description", TransferDetail::getDescription));

        getTransferDetailTable();

        if (TransferDetailViewModel.getTransferDetails().isEmpty()) {
            TransferDetailViewModel.getTransferDetails()
                    .addListener(
                            (ListChangeListener<TransferDetail>)
                                    change -> transferDetailTable.setItems(TransferDetailViewModel.getTransferDetails()));
        } else {
            transferDetailTable
                    .itemsProperty()
                    .bindBidirectional(TransferDetailViewModel.transferDetailsProperty());
        }
    }

    private void getTransferDetailTable() {
        transferDetailTable.setPrefSize(1000, 1000);
        transferDetailTable.features().enableBounceEffect();
        transferDetailTable.features().enableSmoothScrolling(0.5);

        transferDetailTable.setTableRowFactory(
                transferDetail -> {
                    MFXTableRow<TransferDetail> row = new MFXTableRow<>(transferDetailTable, transferDetail);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<TransferDetail>) event.getSource())
                                        .show(
                                                transferDetailTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<TransferDetail> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(transferDetailTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                event -> {
                    SpotyThreader.spotyThreadPool(
                            () ->
                                    TransferDetailViewModel.removeTransferDetail(
                                            obj.getData().getId(),
                                            TransferDetailViewModel.transferDetailsList.indexOf(obj.getData())));
                    event.consume();
                });
        // Edit
        edit.setOnAction(
                event -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    TransferDetailViewModel.getItem(obj.getData());
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

    private void transferMasterAddProductBtnClicked() {
        transferMasterProductAddBtn.setOnAction(e -> dialog.showAndWait());
    }

    private void quotationProductDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/TransferDetailForm.fxml");
        fxmlLoader.setControllerFactory(c -> TransferDetailFormController.getInstance());

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(transferMasterFormContentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void transferMasterSaveBtnClicked() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();

        if (!transferDetailTable.isDisabled()
                && TransferDetailViewModel.transferDetailsList.isEmpty()) {
            SpotyMessage notification =
                    new SpotyMessage.MessageBuilder("Table can't be Empty")
                            .duration(MessageDuration.SHORT)
                            .icon("fas-triangle-exclamation")
                            .type(MessageVariants.ERROR)
                            .build();
            notificationHolder.addMessage(notification);
            return;
        }
        if (!transferMasterToBranchValidationLabel.isVisible()
                && !transferMasterFromBranchValidationLabel.isVisible()
                && !transferMasterDateValidationLabel.isVisible()) {
            if (TransferMasterViewModel.getId() > 0) {
                try {
                    TransferMasterViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                } catch (Exception e) {
                    SpotyLogger.writeToFile(e, this.getClass());
                }
                return;
            }
            try {
                TransferMasterViewModel.saveTransferMaster(this::onAction, this::onAddSuccess, this::onFailed);
            } catch (Exception e) {
                SpotyLogger.writeToFile(e, this.getClass());
            }
            return;
        }
        onRequiredFieldsMissing();
    }

    public void transferMasterCancelBtnClicked() {
        BaseController.navigation.navigate(Pages.getTransferPane());
        TransferMasterViewModel.resetProperties();
        transferMasterToBranchValidationLabel.setVisible(false);
        transferMasterFromBranchValidationLabel.setVisible(false);
        transferMasterDateValidationLabel.setVisible(false);
        transferMasterFromBranch.clearSelection();
        transferMasterToBranch.clearSelection();
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
                new SpotyMessage.MessageBuilder("Designation added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);
        transferMasterCancelBtnClicked();

        DesignationViewModel.getAllDesignations(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Designation updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);
        transferMasterCancelBtnClicked();

        DesignationViewModel.getAllDesignations(null, null, null);
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

        DesignationViewModel.getAllDesignations(null, null, null);
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

        DesignationViewModel.getAllDesignations(null, null, null);
    }
}
