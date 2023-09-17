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

package org.infinite.spoty.forms;

import static org.infinite.spoty.SpotyResourceLoader.fxmlLoader;
import static org.infinite.spoty.Validators.requiredValidator;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.LongFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

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
import org.infinite.spoty.GlobalActions;
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.RequisitionDetail;
import org.infinite.spoty.database.models.Supplier;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.RequisitionDetailViewModel;
import org.infinite.spoty.viewModels.RequisitionMasterViewModel;
import org.infinite.spoty.viewModels.SupplierViewModel;
import org.infinite.spoty.views.BaseController;

@SuppressWarnings("unchecked")
public class RequisitionMasterFormController implements Initializable {
    private static RequisitionMasterFormController instance;
    @FXML
    public MFXFilterComboBox<Branch> requisitionMasterBranch;
    @FXML
    public MFXDatePicker requisitionMasterDate;
    @FXML
    public MFXTableView<RequisitionDetail> requisitionDetailTable;
    @FXML
    public MFXTextField requisitionMasterNote;
    @FXML
    public BorderPane requisitionMasterFormContentPane;
    @FXML
    public Label requisitionMasterFormTitle;
    @FXML
    public MFXButton requisitionMasterProductAddBtn;
    @FXML
    public MFXFilterComboBox<Supplier> requisitionMasterSupplier;
    @FXML
    public MFXTextField requisitionMasterShipVia;
    @FXML
    public MFXTextField requisitionMasterShipMthd;
    @FXML
    public MFXTextField requisitionMasterShipTerms;
    @FXML
    public Label requisitionMasterBranchValidationLabel;
    @FXML
    public Label requisitionMasterSupplierValidationLabel;
    @FXML
    public Label requisitionMasterDateValidationLabel;
    @FXML
    public MFXButton requisitionMasterSaveBtn;
    private MFXStageDialog dialog;

    private RequisitionMasterFormController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        quotationProductDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static RequisitionMasterFormController getInstance(Stage stage) {
        if (instance == null) instance = new RequisitionMasterFormController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input binding.
        requisitionMasterBranch
                .valueProperty()
                .bindBidirectional(RequisitionMasterViewModel.branchProperty());
        requisitionMasterSupplier
                .valueProperty()
                .bindBidirectional(RequisitionMasterViewModel.supplierProperty());
        requisitionMasterDate
                .textProperty()
                .bindBidirectional(RequisitionMasterViewModel.dateProperty());
        requisitionMasterShipVia
                .textProperty()
                .bindBidirectional(RequisitionMasterViewModel.shipViaProperty());
        requisitionMasterShipMthd
                .textProperty()
                .bindBidirectional(RequisitionMasterViewModel.shipMethodProperty());
        requisitionMasterShipTerms
                .textProperty()
                .bindBidirectional(RequisitionMasterViewModel.shippingTermsProperty());
        requisitionMasterNote
                .textProperty()
                .bindBidirectional(RequisitionMasterViewModel.noteProperty());

        // ComboBox Converters.
        StringConverter<Supplier> supplierConverter =
                FunctionalStringConverter.to(supplier -> (supplier == null) ? "" : supplier.getName());

        StringConverter<Branch> branchConverter =
                FunctionalStringConverter.to(branch -> (branch == null) ? "" : branch.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<Supplier>> supplierFilterFunction =
                searchStr ->
                        supplier ->
                                StringUtils.containsIgnoreCase(supplierConverter.toString(supplier), searchStr);

        Function<String, Predicate<Branch>> branchFilterFunction =
                searchStr ->
                        branch -> StringUtils.containsIgnoreCase(branchConverter.toString(branch), searchStr);

        // ComboBox properties.
        requisitionMasterBranch.setItems(BranchViewModel.getBranches());
        requisitionMasterBranch.setConverter(branchConverter);
        requisitionMasterBranch.setFilterFunction(branchFilterFunction);

        requisitionMasterSupplier.setItems(SupplierViewModel.getSuppliers());
        requisitionMasterSupplier.setConverter(supplierConverter);
        requisitionMasterSupplier.setFilterFunction(supplierFilterFunction);

        // input validators.
        requiredValidator(
                requisitionMasterBranch,
                "Branch is required.",
                requisitionMasterBranchValidationLabel,
                requisitionMasterSaveBtn);
        requiredValidator(
                requisitionMasterSupplier,
                "Supplier is required.",
                requisitionMasterSupplierValidationLabel,
                requisitionMasterSaveBtn);
        requiredValidator(
                requisitionMasterDate,
                "Date is required.",
                requisitionMasterDateValidationLabel,
                requisitionMasterSaveBtn);

        requisitionMasterAddProductBtnClicked();

        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<RequisitionDetail> productName =
                new MFXTableColumn<>(
                        "Product", false, Comparator.comparing(RequisitionDetail::getProductName));
        MFXTableColumn<RequisitionDetail> productQuantity =
                new MFXTableColumn<>(
                        "Quantity", false, Comparator.comparing(RequisitionDetail::getQuantity));
        MFXTableColumn<RequisitionDetail> productDescription =
                new MFXTableColumn<>(
                        "Description", false, Comparator.comparing(RequisitionDetail::getDescription));

        productName.setRowCellFactory(
                product -> new MFXTableRowCell<>(RequisitionDetail::getProductName));
        productQuantity.setRowCellFactory(
                product -> new MFXTableRowCell<>(RequisitionDetail::getQuantity));
        productDescription.setRowCellFactory(
                product -> new MFXTableRowCell<>(RequisitionDetail::getDescription));

        productName.prefWidthProperty().bind(requisitionDetailTable.widthProperty().multiply(.5));
        productQuantity.prefWidthProperty().bind(requisitionDetailTable.widthProperty().multiply(.5));
        productDescription.prefWidthProperty().bind(requisitionDetailTable.widthProperty().multiply(.5));

        requisitionDetailTable.getTableColumns().addAll(productName, productQuantity, productDescription);

        requisitionDetailTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", RequisitionDetail::getProductName),
                        new LongFilter<>("Quantity", RequisitionDetail::getQuantity),
                        new StringFilter<>("Description", RequisitionDetail::getDescription));

        getRequisitionDetailTable();

        if (RequisitionDetailViewModel.getRequisitionDetails().isEmpty()) {
            RequisitionDetailViewModel.getRequisitionDetails()
                    .addListener(
                            (ListChangeListener<RequisitionDetail>)
                                    c ->
                                            requisitionDetailTable.setItems(
                                                    RequisitionDetailViewModel.getRequisitionDetails()));
        } else {
            requisitionDetailTable
                    .itemsProperty()
                    .bindBidirectional(RequisitionDetailViewModel.requisitionDetailsProperty());
        }
    }

    private void getRequisitionDetailTable() {
        requisitionDetailTable.setPrefSize(1000, 1000);
        requisitionDetailTable.features().enableBounceEffect();
        requisitionDetailTable.features().enableSmoothScrolling(0.5);

        requisitionDetailTable.setTableRowFactory(
                t -> {
                    MFXTableRow<RequisitionDetail> row = new MFXTableRow<>(requisitionDetailTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<RequisitionDetail>) event.getSource())
                                        .show(
                                                requisitionDetailTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<RequisitionDetail> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(requisitionDetailTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    RequisitionDetailViewModel.removeRequisitionDetail(
                            obj.getData().getId(),
                            RequisitionDetailViewModel.requisitionDetailList.indexOf(obj.getData()));
                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    GlobalActions.spotyThreadPool().execute(() -> {
                        try {
                            RequisitionDetailViewModel.getItem(
                                    obj.getData().getId(),
                                    RequisitionDetailViewModel.requisitionDetailList.indexOf(obj.getData()));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    dialog.showAndWait();
                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    private void requisitionMasterAddProductBtnClicked() {
        requisitionMasterProductAddBtn.setOnAction(e -> dialog.showAndWait());
    }

    private void quotationProductDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("forms/RequisitionDetailForm.fxml");
        fxmlLoader.setControllerFactory(c -> RequisitionDetailFormController.getInstance());

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(requisitionMasterFormContentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void requisitionMasterSaveBtnClicked() {
        SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

        if (!requisitionDetailTable.isDisabled()
                && RequisitionDetailViewModel.requisitionDetailList.isEmpty()) {
            SimpleNotification notification =
                    new SimpleNotification.NotificationBuilder("Table can't be Empty")
                            .duration(NotificationDuration.SHORT)
                            .icon("fas-triangle-exclamation")
                            .type(NotificationVariants.ERROR)
                            .build();
            notificationHolder.addNotification(notification);
            return;
        }

        if (!requisitionMasterSupplierValidationLabel.isVisible()
                && !requisitionMasterDateValidationLabel.isVisible()
                && !requisitionMasterBranchValidationLabel.isVisible()) {
            if (RequisitionMasterViewModel.getId() > 0) {
                GlobalActions.spotyThreadPool().execute(() -> {
                    try {
                        RequisitionMasterViewModel.updateItem(RequisitionMasterViewModel.getId());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                SimpleNotification notification =
                        new SimpleNotification.NotificationBuilder("Requisition updated successfully")
                                .duration(NotificationDuration.MEDIUM)
                                .icon("fas-circle-check")
                                .type(NotificationVariants.SUCCESS)
                                .build();
                notificationHolder.addNotification(notification);

                requisitionMasterBranch.clearSelection();
                requisitionMasterSupplier.clearSelection();

                requisitionMasterCancelBtnClicked();
                return;
            }
            GlobalActions.spotyThreadPool().execute(() -> {
                try {
                    RequisitionMasterViewModel.saveRequisitionMaster();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            SimpleNotification notification =
                    new SimpleNotification.NotificationBuilder("Requisition saved successfully")
                            .duration(NotificationDuration.MEDIUM)
                            .icon("fas-circle-check")
                            .type(NotificationVariants.SUCCESS)
                            .build();
            notificationHolder.addNotification(notification);

            requisitionMasterBranch.clearSelection();
            requisitionMasterSupplier.clearSelection();

            requisitionMasterCancelBtnClicked();
            return;
        }
        SimpleNotification notification =
                new SimpleNotification.NotificationBuilder("Required fields missing")
                        .duration(NotificationDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(NotificationVariants.ERROR)
                        .build();
        notificationHolder.addNotification(notification);
    }

    public void requisitionMasterCancelBtnClicked() {
        BaseController.navigation.navigate(Pages.getRequisitionPane());
        RequisitionMasterViewModel.resetProperties();
        requisitionMasterBranchValidationLabel.setVisible(false);
        requisitionMasterSupplierValidationLabel.setVisible(false);
        requisitionMasterDateValidationLabel.setVisible(false);
        requisitionMasterBranch.clearSelection();
        requisitionMasterSupplier.clearSelection();
    }
}
