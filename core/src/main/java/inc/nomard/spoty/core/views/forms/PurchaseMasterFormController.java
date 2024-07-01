/*
 * Copyright (c) 2023, Jonathan Mark Mwigo.
 * All rights reserved. Unauthorized use of this code is prohibited.
 */

package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.purchases.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
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
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class PurchaseMasterFormController implements Initializable {
    private static PurchaseMasterFormController instance;
    private final Stage stage;
    @FXML
    private Label purchaseFormTitle, supplierValidationLabel, dateValidationLabel, statusValidationLabel;
    @FXML
    private MFXDatePicker date;
    @FXML
    private MFXFilterComboBox<Supplier> supplier;
    @FXML
    private MFXTableView<PurchaseDetail> detailTable;
    @FXML
    private MFXTextField note;
    @FXML
    private BorderPane purchaseFormContentPane;
    @FXML
    private MFXFilterComboBox<String> purchaseStatus;
    @FXML
    private MFXButton saveBtn, cancelBtn;
    private MFXStageDialog dialog;

    private PurchaseMasterFormController(Stage stage) {
        this.stage = stage;
        Platform.runLater(() -> initializePurchaseProductDialog(stage));
    }

    public static PurchaseMasterFormController getInstance(Stage stage) {
        if (instance == null) {
            synchronized (PurchaseMasterFormController.class) {
                instance = new PurchaseMasterFormController(stage);
            }
        }
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindProperties();
        configureSupplierComboBox();
        purchaseStatus.setItems(FXCollections.observableArrayList(Values.PURCHASE_STATUSES));
        requiredValidator();
        setupTable();
    }

    private void initializePurchaseProductDialog(Stage stage) {
        try {
            FXMLLoader fxmlLoader = fxmlLoader("views/forms/PurchaseDetailForm.fxml");
            fxmlLoader.setControllerFactory(c -> PurchaseDetailFormController.getInstance(stage));
            MFXGenericDialog dialogContent = fxmlLoader.load();

            dialogContent.setShowMinimize(false);
            dialogContent.setShowAlwaysOnTop(false);
            dialogContent.setShowClose(false);

            dialog = MFXGenericDialogBuilder.build(dialogContent)
                    .toStageDialogBuilder()
                    .initOwner(stage)
                    .initModality(Modality.WINDOW_MODAL)
                    .setOwnerNode(purchaseFormContentPane)
                    .setScrimPriority(ScrimPriority.WINDOW)
                    .setScrimOwner(true)
                    .get();

            io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
        } catch (IOException e) {
            SpotyLogger.writeToFile(e, this.getClass());
        }
    }

    private void bindProperties() {
        date.textProperty().bindBidirectional(PurchaseMasterViewModel.dateProperty());
        supplier.valueProperty().bindBidirectional(PurchaseMasterViewModel.supplierProperty());
        purchaseStatus.textProperty().bindBidirectional(PurchaseMasterViewModel.statusProperty());
        note.textProperty().bindBidirectional(PurchaseMasterViewModel.noteProperty());
    }

    private void configureSupplierComboBox() {
        StringConverter<Supplier> supplierConverter = FunctionalStringConverter.to(supplier -> (supplier == null) ? "" : supplier.getName());
        Function<String, Predicate<Supplier>> supplierFilterFunction = searchStr -> supplier -> StringUtils.containsIgnoreCase(supplierConverter.toString(supplier), searchStr);

        supplier.setConverter(supplierConverter);
        supplier.setFilterFunction(supplierFilterFunction);

        if (SupplierViewModel.getSuppliers().isEmpty()) {
            SupplierViewModel.getSuppliers().addListener((ListChangeListener<Supplier>) c -> supplier.setItems(SupplierViewModel.getSuppliers()));
        } else {
            supplier.itemsProperty().bindBidirectional(SupplierViewModel.suppliersProperty());
        }
    }

    public void saveBtnClicked() {
        if (!detailTable.isDisabled() && PurchaseDetailViewModel.getPurchaseDetails().isEmpty()) {
            errorMessage("Table can't be Empty");
        }
        validateFields();

        if (isValidForm()) {
            if (PurchaseMasterViewModel.getId() > 0) {
                PurchaseMasterViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
            } else {
                PurchaseMasterViewModel.savePurchaseMaster(this::onSuccess, this::successMessage, this::errorMessage);
            }
        }
    }

    private void validateFields() {
        validateField(supplier, supplierValidationLabel);
        validateField(date, dateValidationLabel);
        validateField(purchaseStatus, statusValidationLabel);
    }

    private void validateField(MFXTextField field, Label validationLabel) {
        List<Constraint> constraints = field.validate();
        if (!constraints.isEmpty()) {
            validationLabel.setManaged(true);
            validationLabel.setVisible(true);
            validationLabel.setText(constraints.getFirst().getMessage());
            field.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
    }

    private boolean isValidForm() {
        return supplier.validate().isEmpty() && date.validate().isEmpty() && purchaseStatus.validate().isEmpty() && !detailTable.isDisabled() && !PurchaseDetailViewModel.getPurchaseDetails().isEmpty();
    }

    private void setupTable() {
        setupTableColumns();
        setupTableFilters();
        styleTable();
        bindTableItems();
    }

    private void setupTableColumns() {
        MFXTableColumn<PurchaseDetail> product = createTableColumn("Product", PurchaseDetail::getProductName, 1);
        MFXTableColumn<PurchaseDetail> quantity = createTableColumn("Quantity", PurchaseDetail::getQuantity, 1);

        detailTable.getTableColumns().addAll(product, quantity);
    }

    private <U extends Comparable<? super U>> MFXTableColumn<PurchaseDetail> createTableColumn(String title, Function<PurchaseDetail, U> mapper, double widthPercentage) {
        MFXTableColumn<PurchaseDetail> column = new MFXTableColumn<>(title, false, Comparator.comparing(mapper));
        column.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(mapper));
        column.prefWidthProperty().bind(detailTable.widthProperty().multiply(widthPercentage));
        return column;
    }

    private void setupTableFilters() {
        detailTable.getFilters().addAll(
                new StringFilter<>("Product", PurchaseDetail::getProductName),
                new IntegerFilter<>("Quantity", PurchaseDetail::getQuantity)
        );
    }

    private void styleTable() {
        detailTable.setPrefSize(1000, 1000);
        detailTable.features().enableBounceEffect();
        detailTable.features().enableSmoothScrolling(0.5);

        detailTable.setTableRowFactory(t -> {
            MFXTableRow<PurchaseDetail> row = new MFXTableRow<>(detailTable, t);
            row.setOnContextMenuRequested(event -> showContextMenu(row).show(detailTable.getScene().getWindow(), event.getScreenX(), event.getScreenY()));
            return row;
        });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<PurchaseDetail> row) {
        MFXContextMenu contextMenu = new MFXContextMenu(detailTable);
        contextMenu.addItems(createMenuItem("Delete", event -> new DeleteConfirmationDialog(() -> handleDeleteAction(row), row.getData().getProductName(), stage, purchaseFormContentPane)), createMenuItem("Edit", event -> handleEditAction(row)));
        return contextMenu;
    }

    private MFXContextMenuItem createMenuItem(String text, EventHandler<ActionEvent> action) {
        MFXContextMenuItem menuItem = new MFXContextMenuItem(text);
        menuItem.setOnAction(action);
        return menuItem;
    }

    private void handleDeleteAction(MFXTableRow<PurchaseDetail> row) {
        PurchaseDetailViewModel.removePurchaseDetail(row.getData().getId(), PurchaseDetailViewModel.getPurchaseDetails().indexOf(row.getData()));
    }

    private void handleEditAction(MFXTableRow<PurchaseDetail> row) {
        Platform.runLater(() -> PurchaseDetailViewModel.getPurchaseDetail(row.getData()));
        dialog.showAndWait();
    }

    private void bindTableItems() {
        if (PurchaseDetailViewModel.getPurchaseDetails().isEmpty()) {
            PurchaseDetailViewModel.getPurchaseDetails().addListener((ListChangeListener<PurchaseDetail>) change -> detailTable.setItems(PurchaseDetailViewModel.getPurchaseDetails()));
        } else {
            detailTable.itemsProperty().bindBidirectional(PurchaseDetailViewModel.purchaseDetailsProperty());
        }
    }

    public void cancelBtnClicked() {
        BaseController.navigation.navigate(new PurchasePage(stage));
        resetForm();
    }

    private void resetForm() {
        PurchaseMasterViewModel.resetProperties();
        resetValidationLabels();
        clearFormFields();
    }

    private void resetValidationLabels() {
        supplierValidationLabel.setVisible(false);
        dateValidationLabel.setVisible(false);
        statusValidationLabel.setVisible(false);
        supplierValidationLabel.setManaged(false);
        dateValidationLabel.setManaged(false);
        statusValidationLabel.setManaged(false);
    }

    private void clearFormFields() {
        supplier.clearSelection();
        purchaseStatus.clearSelection();
        date.setValue(null);
        clearFieldPseudoClasses();
    }

    private void clearFieldPseudoClasses() {
        supplier.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        date.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        purchaseStatus.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
    }

    public void addBtnClicked() {
        dialog.showAndWait();
    }

    private void onSuccess() {
        cancelBtnClicked();
        clearFormFields();
        PurchaseMasterViewModel.getAllPurchaseMasters(null, null);
        ProductViewModel.getAllProducts(null, null);
    }

    private void requiredValidator() {
        setupValidation(supplier, "Supplier is required", supplierValidationLabel);
        setupValidation(purchaseStatus, "Beneficiary Type is required", statusValidationLabel);
        setupValidation(date, "Date is required", dateValidationLabel);
    }

    private void setupValidation(MFXTextField field, String message, Label validationLabel) {
        Constraint constraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(message)
                .setCondition(field.textProperty().length().greaterThan(0))
                .get();

        field.getValidator().constraint(constraint);
        field.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                validationLabel.setManaged(false);
                validationLabel.setVisible(false);
                field.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
            }
        });
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
}
