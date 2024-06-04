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
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.requisitions.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.dtos.requisitions.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
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
        supplier.setConverter(supplierConverter);
        supplier.setFilterFunction(supplierFilterFunction);
        if (SupplierViewModel.getSuppliers().isEmpty()) {
            SupplierViewModel.getSuppliers()
                    .addListener(
                            (ListChangeListener<Supplier>)
                                    c -> supplier.setItems(SupplierViewModel.getSuppliers()));
        } else {
            supplier.itemsProperty().bindBidirectional(SupplierViewModel.suppliersProperty());
        }

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
        dialogContent.setShowClose(false);

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
                RequisitionMasterViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                return;
            }
            RequisitionMasterViewModel.saveRequisitionMaster(this::onSuccess, this::successMessage, this::errorMessage);
        }
    }

    private void setupTable() {
        // Set table column titles.
        MFXTableColumn<RequisitionDetail> product =
                new MFXTableColumn<>(
                        "Product", false, Comparator.comparing(RequisitionDetail::getProductName));
        MFXTableColumn<RequisitionDetail> quantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(RequisitionDetail::getQuantity));

        // Set table column data.
        product.setRowCellFactory(
                requisitionDetail -> new MFXTableRowCell<>(RequisitionDetail::getProductName));
        quantity.setRowCellFactory(
                requisitionDetail -> new MFXTableRowCell<>(RequisitionDetail::getQuantity));

        // Set table column width.
        product.prefWidthProperty().bind(detailTable.widthProperty().multiply(.25));
        quantity.prefWidthProperty().bind(detailTable.widthProperty().multiply(.25));

        // Set table filter.
        detailTable
                .getTableColumns()
                .addAll(product, quantity);

        detailTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Product", RequisitionDetail::getProductName),
                        new IntegerFilter<>("Quantity", RequisitionDetail::getQuantity));

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
                            RequisitionDetailViewModel.getRequisitionDetail(obj.getData().getId());
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
        supplier.clearSelection();
        status.clearSelection();
        supplier.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        status.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        supplier.clearSelection();
        status.clearSelection();
    }

    public void addBtnClicked() {
        dialog.showAndWait();
    }

    private void onSuccess() {
        cancelBtnClicked();
        RequisitionMasterViewModel.resetProperties();
        RequisitionMasterViewModel.getAllRequisitionMasters(null, null);
        ProductViewModel.getAllProducts(null, null);
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
