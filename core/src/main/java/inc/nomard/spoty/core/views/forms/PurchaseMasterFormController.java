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
import inc.nomard.spoty.core.viewModels.purchases.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.dtos.purchases.*;
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
public class PurchaseMasterFormController implements Initializable {
    private static PurchaseMasterFormController instance;
    @FXML
    public Label purchaseFormTitle,
            supplierValidationLabel,
            dateValidationLabel,
            statusValidationLabel;
    @FXML
    public MFXDatePicker date;
    @FXML
    public MFXFilterComboBox<Supplier> supplier;
    @FXML
    public MFXTableView<PurchaseDetail> detailTable;
    @FXML
    public MFXTextField note;
    @FXML
    public BorderPane purchaseFormContentPane;
    @FXML
    public MFXFilterComboBox<String> purchaseStatus;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    private MFXStageDialog dialog;

    private PurchaseMasterFormController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        createPurchaseProductDialog(stage);
                    } catch (IOException e) {
                        SpotyLogger.writeToFile(e, this.getClass());
                    }
                });
    }

    public static PurchaseMasterFormController getInstance(Stage stage) {
        if (instance == null) instance = new PurchaseMasterFormController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bi~Directional Binding.
        date.textProperty().bindBidirectional(PurchaseMasterViewModel.dateProperty());
        supplier.valueProperty().bindBidirectional(PurchaseMasterViewModel.supplierProperty());
        purchaseStatus.textProperty().bindBidirectional(PurchaseMasterViewModel.statusProperty());
        note.textProperty().bindBidirectional(PurchaseMasterViewModel.noteProperty());

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

        purchaseStatus.setItems(FXCollections.observableArrayList(Values.PURCHASE_STATUSES));

        requiredValidator();

        setupTable();
    }

    private void createPurchaseProductDialog(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/PurchaseDetailForm.fxml");
        fxmlLoader.setControllerFactory(c -> PurchaseDetailFormController.getInstance());

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setShowClose(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(purchaseFormContentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void saveBtnClicked() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();

        if (!detailTable.isDisabled() && PurchaseDetailViewModel.purchaseDetailsList.isEmpty()) {
            SpotyMessage notification =
                    new SpotyMessage.MessageBuilder("Table can't be Empty")
                            .duration(MessageDuration.SHORT)
                            .icon("fas-triangle-exclamation")
                            .type(MessageVariants.ERROR)
                            .build();
            notificationHolder.addMessage(notification);
        }
        List<Constraint> supplierConstraints = supplier.validate();
        List<Constraint> dateConstraints = date.validate();
        List<Constraint> statusConstraints = purchaseStatus.validate();
        if (!supplierConstraints.isEmpty()) {
            supplierValidationLabel.setManaged(true);
            supplierValidationLabel.setVisible(true);
            supplierValidationLabel.setText(supplierConstraints.getFirst().getMessage());
            supplier.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (!dateConstraints.isEmpty()) {
            dateValidationLabel.setManaged(true);
            dateValidationLabel.setVisible(true);
            dateValidationLabel.setText(dateConstraints.getFirst().getMessage());
            date.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (!statusConstraints.isEmpty()) {
            statusValidationLabel.setManaged(true);
            statusValidationLabel.setVisible(true);
            statusValidationLabel.setText(statusConstraints.getFirst().getMessage());
            purchaseStatus.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (!supplierConstraints.isEmpty()
                && !dateConstraints.isEmpty()
                && !statusConstraints.isEmpty()
                && detailTable.isDisabled()
                && PurchaseDetailViewModel.purchaseDetailsList.isEmpty()) {
            cancelBtn.setDisable(false);
            saveBtn.setDisable(false);
        }
        if (supplierConstraints.isEmpty()
                && dateConstraints.isEmpty()
                && statusConstraints.isEmpty()
                && !detailTable.isDisabled()
                && !PurchaseDetailViewModel.purchaseDetailsList.isEmpty()) {
            if (PurchaseMasterViewModel.getId() > 0) {
                PurchaseMasterViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                return;
            }
            PurchaseMasterViewModel.savePurchaseMaster(this::onSuccess, this::successMessage, this::errorMessage);
        }
    }

    private void setupTable() {
        // Set table column titles.
        MFXTableColumn<PurchaseDetail> product =
                new MFXTableColumn<>(
                        "Product", false, Comparator.comparing(PurchaseDetail::getProductName));
        MFXTableColumn<PurchaseDetail> quantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(PurchaseDetail::getQuantity));
        MFXTableColumn<PurchaseDetail> cost =
                new MFXTableColumn<>("Unit Cost", false, Comparator.comparing(PurchaseDetail::getCost));

        // Set table column data.
        product.setRowCellFactory(
                purchaseDetail -> new MFXTableRowCell<>(PurchaseDetail::getProductName));
        quantity.setRowCellFactory(
                purchaseDetail -> new MFXTableRowCell<>(PurchaseDetail::getQuantity));
        cost.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(PurchaseDetail::getCost));

        // Set table column width.
        product.prefWidthProperty().bind(detailTable.widthProperty().multiply(.34));
        quantity.prefWidthProperty().bind(detailTable.widthProperty().multiply(.34));
        cost.prefWidthProperty().bind(detailTable.widthProperty().multiply(.34));

        // Set table filter.
        detailTable
                .getTableColumns()
                .addAll(product, quantity, cost);

        detailTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Product", PurchaseDetail::getProductName),
                        new IntegerFilter<>("Quantity", PurchaseDetail::getQuantity),
                        new DoubleFilter<>("Cost", PurchaseDetail::getCost));

        styleTable();

        // Populate table.
        if (PurchaseDetailViewModel.getPurchaseDetails().isEmpty()) {
            PurchaseDetailViewModel.getPurchaseDetails()
                    .addListener(
                            (ListChangeListener<PurchaseDetail>)
                                    change -> detailTable.setItems(PurchaseDetailViewModel.getPurchaseDetails()));
        } else {
            detailTable
                    .itemsProperty()
                    .bindBidirectional(PurchaseDetailViewModel.purchaseDetailsProperty());
        }
    }

    private void styleTable() {
        detailTable.setPrefSize(1000, 1000);
        detailTable.features().enableBounceEffect();
        detailTable.features().enableSmoothScrolling(0.5);

        detailTable.setTableRowFactory(
                t -> {
                    MFXTableRow<PurchaseDetail> row = new MFXTableRow<>(detailTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<PurchaseDetail>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<PurchaseDetail> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(detailTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                event -> {
                    PurchaseDetailViewModel.removePurchaseDetail(
                            obj.getData().getId(),
                            PurchaseDetailViewModel.purchaseDetailsList.indexOf(obj.getData()));
                    event.consume();
                });
        // Edit
        edit.setOnAction(
                event -> {
                    PurchaseDetailViewModel.getPurchaseDetail(obj.getData());
                    dialog.showAndWait();
                    event.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    public void cancelBtnClicked() {
        BaseController.navigation.navigate(Pages.getPurchasePane());

        PurchaseMasterViewModel.resetProperties();

        supplierValidationLabel.setVisible(false);
        dateValidationLabel.setVisible(false);
        statusValidationLabel.setVisible(false);

        supplierValidationLabel.setManaged(false);
        dateValidationLabel.setManaged(false);
        statusValidationLabel.setManaged(false);

        supplier.clearSelection();
        purchaseStatus.clearSelection();
        date.setValue(null);

        supplier.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        date.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        purchaseStatus.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);

        supplier.clearSelection();
        purchaseStatus.clearSelection();
    }

    public void addBtnClicked() {
        dialog.showAndWait();
    }

    private void onSuccess() {
        cancelBtnClicked();
        supplier.clearSelection();
        purchaseStatus.clearSelection();
        date.setValue(null);
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
                        .setMessage("Beneficiary Type is required")
                        .setCondition(purchaseStatus.textProperty().length().greaterThan(0))
                        .get();
        purchaseStatus.getValidator().constraint(statusConstraint);
        Constraint dateConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Date is required")
                        .setCondition(date.textProperty().length().greaterThan(0))
                        .get();
        date.getValidator().constraint(dateConstraint);
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
        purchaseStatus
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                statusValidationLabel.setManaged(false);
                                statusValidationLabel.setVisible(false);
                                purchaseStatus.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        date
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                dateValidationLabel.setManaged(false);
                                dateValidationLabel.setVisible(false);
                                date.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    private void successMessage(String message) {
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
