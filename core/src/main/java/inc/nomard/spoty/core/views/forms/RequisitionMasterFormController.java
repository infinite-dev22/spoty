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

import inc.nomard.spoty.core.components.message.SpotyMessage;
import inc.nomard.spoty.core.components.message.SpotyMessageHolder;
import inc.nomard.spoty.core.components.message.enums.MessageDuration;
import inc.nomard.spoty.core.components.message.enums.MessageVariants;
import inc.nomard.spoty.core.components.navigation.Pages;
import inc.nomard.spoty.core.values.strings.Values;
import inc.nomard.spoty.core.viewModels.SupplierViewModel;
import inc.nomard.spoty.core.viewModels.requisitions.RequisitionDetailViewModel;
import inc.nomard.spoty.core.viewModels.requisitions.RequisitionMasterViewModel;
import inc.nomard.spoty.core.views.BaseController;
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
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.fxmlLoader;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

@SuppressWarnings("unchecked")
public class RequisitionMasterFormController implements Initializable {
    private static RequisitionMasterFormController instance;
    @FXML
    public Label title,
            supplierValidationLabel,
            statusValidationLabel;
    @FXML
    public MFXFilterComboBox<Supplier> supplier;
    @FXML
    public MFXTableView<RequisitionDetail> detailTable;
    @FXML
    public MFXTextField note;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXFilterComboBox<String> status;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
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
        supplier.valueProperty().bindBidirectional(RequisitionMasterViewModel.supplierProperty());
        status.textProperty().bindBidirectional(RequisitionMasterViewModel.statusProperty());
        note.textProperty().bindBidirectional(RequisitionMasterViewModel.noteProperty());

        // ComboBox Converters.
        StringConverter<Supplier> supplierConverter =
                FunctionalStringConverter.to(supplier -> (supplier == null) ? "" : supplier.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<Supplier>> supplierFilterFunction =
                searchStr ->
                        supplier ->
                                StringUtils.containsIgnoreCase(supplierConverter.toString(supplier), searchStr);

        // Set items to combo boxes and display custom text.
        supplier.setItems(SupplierViewModel.getSuppliers());
        supplier.setConverter(supplierConverter);
        supplier.setFilterFunction(supplierFilterFunction);

        status.setItems(FXCollections.observableArrayList(Values.PURCHASE_STATUSES));

        // input validators.
        requiredValidator();

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
                        .setOwnerNode(contentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void saveBtnClicked() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();

        if (!detailTable.isDisabled() && RequisitionDetailViewModel.requisitionDetailsList.isEmpty()) {
            SpotyMessage notification =
                    new SpotyMessage.MessageBuilder("Table can't be Empty")
                            .duration(MessageDuration.SHORT)
                            .icon("fas-triangle-exclamation")
                            .type(MessageVariants.ERROR)
                            .build();
            notificationHolder.addMessage(notification);
        }
        List<Constraint> supplierConstraints = supplier.validate();
        List<Constraint> statusConstraints = status.validate();
        if (!supplierConstraints.isEmpty()) {
            supplierValidationLabel.setManaged(true);
            supplierValidationLabel.setVisible(true);
            supplierValidationLabel.setText(supplierConstraints.getFirst().getMessage());
            supplier.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (!statusConstraints.isEmpty()) {
            statusValidationLabel.setManaged(true);
            statusValidationLabel.setVisible(true);
            statusValidationLabel.setText(statusConstraints.getFirst().getMessage());
            status.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (supplierConstraints.isEmpty()
                && statusConstraints.isEmpty()) {
            if (RequisitionMasterViewModel.getId() > 0) {
                try {
                    RequisitionMasterViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                } catch (Exception e) {
                    SpotyLogger.writeToFile(e, this.getClass());
                }
                return;
            }
            try {
                RequisitionMasterViewModel.saveRequisitionMaster(this::onAction, this::onAddSuccess, this::onFailed);
            } catch (Exception e) {
                SpotyLogger.writeToFile(e, this.getClass());
            }
        }
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
        product.prefWidthProperty().bind(detailTable.widthProperty().multiply(.25));
        quantity.prefWidthProperty().bind(detailTable.widthProperty().multiply(.25));
        tax.prefWidthProperty().bind(detailTable.widthProperty().multiply(.25));
        discount.prefWidthProperty().bind(detailTable.widthProperty().multiply(.25));
        price.prefWidthProperty().bind(detailTable.widthProperty().multiply(.25));
        totalPrice.prefWidthProperty().bind(detailTable.widthProperty().multiply(.25));

        // Set table filter.
        detailTable
                .getTableColumns()
                .addAll(product, quantity, tax, discount, price, totalPrice);

        detailTable
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
                                    change -> detailTable.setItems(RequisitionDetailViewModel.getRequisitionDetails()));
        } else {
            detailTable
                    .itemsProperty()
                    .bindBidirectional(RequisitionDetailViewModel.requisitionDetailsProperty());
        }
    }

    private void styleTable() {
        detailTable.setPrefSize(1000, 1000);
        detailTable.features().enableBounceEffect();
        detailTable.features().enableSmoothScrolling(0.5);

        detailTable.setTableRowFactory(
                t -> {
                    MFXTableRow<RequisitionDetail> row = new MFXTableRow<>(detailTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<RequisitionDetail>) event.getSource())
                                        .show(
                                                detailTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<RequisitionDetail> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(detailTable);
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

        supplierValidationLabel.setVisible(false);
        statusValidationLabel.setVisible(false);

        supplierValidationLabel.setManaged(false);
        statusValidationLabel.setManaged(false);

        supplier.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        status.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);

        supplier.clearSelection();
        status.clearSelection();
    }

    public void addBtnClicked() {
        dialog.showAndWait();
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
                new SpotyMessage.MessageBuilder("Requisition added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);
        cancelBtnClicked();

        BaseController.navigation.navigate(Pages.getRequisitionPane());
        RequisitionMasterViewModel.resetProperties();
        RequisitionMasterViewModel.getAllRequisitionMasters(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Requisition updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);
        cancelBtnClicked();

        RequisitionMasterViewModel.getAllRequisitionMasters(null, null, null);
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

        BaseController.navigation.navigate(Pages.getRequisitionPane());
        RequisitionMasterViewModel.resetProperties();
        RequisitionMasterViewModel.getAllRequisitionMasters(null, null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint supplierConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Supplier is required")
                        .setCondition(supplier.textProperty().length().greaterThan(0))
                        .get();
        supplier.getValidator().constraint(supplierConstraint);
        Constraint statusConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Status is required")
                        .setCondition(status.textProperty().length().greaterThan(0))
                        .get();
        status.getValidator().constraint(statusConstraint);
        // Display error.
        supplier
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                supplierValidationLabel.setManaged(false);
                                supplierValidationLabel.setVisible(false);
                                supplier.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        status
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                statusValidationLabel.setManaged(false);
                                statusValidationLabel.setVisible(false);
                                status.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }
}
