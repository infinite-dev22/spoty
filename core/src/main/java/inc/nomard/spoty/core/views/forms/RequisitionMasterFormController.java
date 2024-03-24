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
import inc.nomard.spoty.core.components.notification.SimpleNotification;
import inc.nomard.spoty.core.components.notification.SimpleNotificationHolder;
import inc.nomard.spoty.core.components.notification.enums.NotificationDuration;
import inc.nomard.spoty.core.components.notification.enums.NotificationVariants;
import inc.nomard.spoty.core.values.strings.Values;
import inc.nomard.spoty.core.viewModels.BranchViewModel;
import inc.nomard.spoty.core.viewModels.SupplierViewModel;
import inc.nomard.spoty.core.viewModels.requisitions.RequisitionDetailViewModel;
import inc.nomard.spoty.core.viewModels.requisitions.RequisitionMasterViewModel;
import inc.nomard.spoty.core.views.BaseController;
import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.dtos.requisitions.RequisitionDetail;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
public class RequisitionMasterFormController implements Initializable {
    private static RequisitionMasterFormController instance;
    @FXML
    public Label requisitionFormTitle;
    @FXML
    public MFXDatePicker requisitionDate;
    @FXML
    public MFXFilterComboBox<Supplier> requisitionSupplier;
    @FXML
    public MFXFilterComboBox<Branch> requisitionBranch;
    @FXML
    public MFXTableView<RequisitionDetail> requisitionDetailTable;
    @FXML
    public MFXTextField requisitionNote;
    @FXML
    public BorderPane requisitionFormContentPane;
    @FXML
    public MFXFilterComboBox<String> requisitionStatus;
    @FXML
    public Label requisitionBranchValidationLabel;
    @FXML
    public Label requisitionSupplierValidationLabel;
    @FXML
    public Label requisitionDateValidationLabel;
    @FXML
    public Label requisitionStatusValidationLabel;
    @FXML
    public MFXButton requisitionMasterSaveBtn;
    private MFXStageDialog dialog;

    private RequisitionMasterFormController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        createRequisitionProductDialog(stage);
                    } catch (IOException e) {
                        SpotyLogger.writeToFile(e, this.getClass());
                    }
                });
    }

    public static RequisitionMasterFormController getInstance(Stage stage) {
        if (instance == null) instance = new RequisitionMasterFormController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bi~Directional Binding.
        requisitionDate.textProperty().bindBidirectional(RequisitionMasterViewModel.dateProperty());
        requisitionSupplier.valueProperty().bindBidirectional(RequisitionMasterViewModel.supplierProperty());
        requisitionBranch.valueProperty().bindBidirectional(RequisitionMasterViewModel.branchProperty());
        requisitionStatus.textProperty().bindBidirectional(RequisitionMasterViewModel.statusProperty());
        requisitionNote.textProperty().bindBidirectional(RequisitionMasterViewModel.noteProperty());

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

        // Set items to combo boxes and display custom text.
        requisitionSupplier.setItems(SupplierViewModel.getSuppliers());
        requisitionSupplier.setConverter(supplierConverter);
        requisitionSupplier.setFilterFunction(supplierFilterFunction);

        requisitionBranch.setItems(BranchViewModel.getBranches());
        requisitionBranch.setConverter(branchConverter);
        requisitionBranch.setFilterFunction(branchFilterFunction);

        requisitionStatus.setItems(FXCollections.observableArrayList(Values.PURCHASE_STATUSES));

        // input validators.
        requiredValidator(
                requisitionBranch,
                "Branch is required.",
                requisitionBranchValidationLabel,
                requisitionMasterSaveBtn);
        requiredValidator(
                requisitionSupplier,
                "Supplier is required.",
                requisitionSupplierValidationLabel,
                requisitionMasterSaveBtn);
        requiredValidator(
                requisitionDate, "Date is required.", requisitionDateValidationLabel, requisitionMasterSaveBtn);
        requiredValidator(
                requisitionStatus,
                "Status is required.",
                requisitionStatusValidationLabel,
                requisitionMasterSaveBtn);

        setupTable();
    }

    private void createRequisitionProductDialog(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/RequisitionDetailForm.fxml");
        fxmlLoader.setControllerFactory(c -> RequisitionDetailFormController.getInstance());

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(requisitionFormContentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void saveBtnClicked() {
        SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

        if (!requisitionDetailTable.isDisabled() && RequisitionDetailViewModel.requisitionDetailsList.isEmpty()) {
            SimpleNotification notification =
                    new SimpleNotification.NotificationBuilder("Table can't be Empty")
                            .duration(NotificationDuration.SHORT)
                            .icon("fas-triangle-exclamation")
                            .type(NotificationVariants.ERROR)
                            .build();
            notificationHolder.addNotification(notification);
            return;
        }
        if (!requisitionBranchValidationLabel.isVisible()
                && !requisitionSupplierValidationLabel.isVisible()
                && !requisitionDateValidationLabel.isVisible()
                && !requisitionStatusValidationLabel.isVisible()) {
            if (RequisitionMasterViewModel.getId() > 0) {
                SpotyThreader.spotyThreadPool(() -> {
                    try {
                        RequisitionMasterViewModel.updateItem(this::onAction, this::onSuccess, this::onFailed);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, this.getClass());
                    }
                });

                SimpleNotification notification =
                        new SimpleNotification.NotificationBuilder("Requisition updated successfully")
                                .duration(NotificationDuration.MEDIUM)
                                .icon("fas-circle-check")
                                .type(NotificationVariants.SUCCESS)
                                .build();
                notificationHolder.addNotification(notification);

                requisitionSupplier.clearSelection();
                requisitionBranch.clearSelection();
                requisitionStatus.clearSelection();

                cancelBtnClicked();
                return;
            }
            SpotyThreader.spotyThreadPool(() -> {
                try {
                    RequisitionMasterViewModel.saveRequisitionMaster(this::onAction, this::onSuccess, this::onFailed);
                } catch (Exception e) {
                    SpotyLogger.writeToFile(e, this.getClass());
                }
            });

            SimpleNotification notification =
                    new SimpleNotification.NotificationBuilder("Requisition saved successfully")
                            .duration(NotificationDuration.MEDIUM)
                            .icon("fas-circle-check")
                            .type(NotificationVariants.SUCCESS)
                            .build();
            notificationHolder.addNotification(notification);

            requisitionSupplier.clearSelection();
            requisitionBranch.clearSelection();
            requisitionStatus.clearSelection();

            cancelBtnClicked();
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

    private void setupTable() {
        // Set table column titles.
        MFXTableColumn<RequisitionDetail> product =
                new MFXTableColumn<>(
                        "Product", false, Comparator.comparing(RequisitionDetail::getProductName));
        MFXTableColumn<RequisitionDetail> quantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(RequisitionDetail::getQuantity));
        MFXTableColumn<RequisitionDetail> tax =
                new MFXTableColumn<>("Tax", false, Comparator.comparing(RequisitionDetail::getNetTax));
        MFXTableColumn<RequisitionDetail> discount =
                new MFXTableColumn<>("Discount", false, Comparator.comparing(RequisitionDetail::getDiscount));
        MFXTableColumn<RequisitionDetail> price =
                new MFXTableColumn<>("Price", false, Comparator.comparing(RequisitionDetail::getPrice));
        MFXTableColumn<RequisitionDetail> totalPrice =
                new MFXTableColumn<>(
                        "Total Price", false, Comparator.comparing(RequisitionDetail::getTotal));

        // Set table column data.
        product.setRowCellFactory(
                requisitionDetail -> new MFXTableRowCell<>(RequisitionDetail::getProductName));
        quantity.setRowCellFactory(
                requisitionDetail -> new MFXTableRowCell<>(RequisitionDetail::getQuantity));
        tax.setRowCellFactory(requisitionDetail -> new MFXTableRowCell<>(RequisitionDetail::getNetTax));
        discount.setRowCellFactory(
                requisitionDetail -> new MFXTableRowCell<>(RequisitionDetail::getDiscount));
        price.setRowCellFactory(requisitionDetail -> new MFXTableRowCell<>(RequisitionDetail::getPrice));
        totalPrice.setRowCellFactory(
                requisitionDetail -> new MFXTableRowCell<>(RequisitionDetail::getTotal));

        // Set table column width.
        product.prefWidthProperty().bind(requisitionDetailTable.widthProperty().multiply(.25));
        quantity.prefWidthProperty().bind(requisitionDetailTable.widthProperty().multiply(.25));
        tax.prefWidthProperty().bind(requisitionDetailTable.widthProperty().multiply(.25));
        discount.prefWidthProperty().bind(requisitionDetailTable.widthProperty().multiply(.25));
        price.prefWidthProperty().bind(requisitionDetailTable.widthProperty().multiply(.25));
        totalPrice.prefWidthProperty().bind(requisitionDetailTable.widthProperty().multiply(.25));

        // Set table filter.
        requisitionDetailTable
                .getTableColumns()
                .addAll(product, quantity, tax, discount, price, totalPrice);

        requisitionDetailTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Product", RequisitionDetail::getProductName),
                        new IntegerFilter<>("Quantity", RequisitionDetail::getQuantity),
                        new DoubleFilter<>("Tax", RequisitionDetail::getNetTax),
                        new DoubleFilter<>("Discount", RequisitionDetail::getDiscount),
                        new DoubleFilter<>("Price", RequisitionDetail::getPrice),
                        new DoubleFilter<>("Total Price", RequisitionDetail::getTotal));

        styleTable();

        // Populate table.
        if (RequisitionDetailViewModel.getRequisitionDetails().isEmpty()) {
            RequisitionDetailViewModel.getRequisitionDetails()
                    .addListener(
                            (ListChangeListener<RequisitionDetail>)
                                    change -> requisitionDetailTable.setItems(RequisitionDetailViewModel.getRequisitionDetails()));
        } else {
            requisitionDetailTable
                    .itemsProperty()
                    .bindBidirectional(RequisitionDetailViewModel.requisitionDetailsProperty());
        }
    }

    private void styleTable() {
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
                event -> {
                    RequisitionDetailViewModel.removeRequisitionDetail(
                            obj.getData().getId(),
                            RequisitionDetailViewModel.requisitionDetailsList.indexOf(obj.getData()));
                    event.consume();
                });
        // Edit
        edit.setOnAction(
                event -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            RequisitionDetailViewModel.getItem(
                                    obj.getData().getId(),
                                    RequisitionDetailViewModel.requisitionDetailsList.indexOf(obj.getData()), this::onAction, this::onFailed);
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

    public void cancelBtnClicked() {
        BaseController.navigation.navigate(Pages.getRequisitionPane());

        RequisitionMasterViewModel.resetProperties();

        requisitionBranchValidationLabel.setVisible(false);
        requisitionSupplierValidationLabel.setVisible(false);
        requisitionDateValidationLabel.setVisible(false);
        requisitionStatusValidationLabel.setVisible(false);
        requisitionSupplier.clearSelection();
        requisitionBranch.clearSelection();
        requisitionStatus.clearSelection();
    }

    public void addBtnClicked() {
        dialog.showAndWait();
    }

    private void onAction() {
        System.out.println("Loading requisition master...");
    }

    private void onSuccess() {
        System.out.println("Loaded requisition master...");
    }

    private void onFailed() {
        System.out.println("failed loading requisition master...");
    }
}
