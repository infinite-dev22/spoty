package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.purchases.*;
import inc.nomard.spoty.core.views.pages.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.dtos.purchases.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.util.*;
import java.util.function.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class PurchaseMasterForm extends OutlineFormPage {
    private Label supplierValidationLabel;
    private Label dateValidationLabel;
    private Label statusValidationLabel;
    private MFXDatePicker date;
    private MFXFilterComboBox<Supplier> supplier;
    private MFXTableView<PurchaseDetail> detailTable;
    private MFXTextField note;
    private MFXFilterComboBox<String> purchaseStatus;
    private MFXButton saveBtn, cancelBtn, addBtn;

    public PurchaseMasterForm() {
        addNode(init());
        initializeComponentProperties();
    }

    public void initializeComponentProperties() {
        bindProperties();
        configureSupplierComboBox();
        purchaseStatus.setItems(FXCollections.observableArrayList(Values.PURCHASE_STATUSES));
        requiredValidator();
    }

    private BorderPane init() {
        Label purchaseFormTitle = new Label("Purchase Form");
        UIUtils.anchor(purchaseFormTitle, 0d, null, null, 0d);

        var vbox = new VBox();
        vbox.getStyleClass().add("card-flat");
        vbox.setPadding(new Insets(10d));
        vbox.setSpacing(10d);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        vbox.getChildren().addAll(purchaseFormTitle, buildSeparator(), createCustomGrid(), buildAddButton(), buildTable(), createButtonBox());

        return new BorderPane(vbox);
    }

    private Separator buildSeparator() {
        var separator = new Separator();
        separator.setPrefWidth(200.0);
        HBox.setHgrow(separator, Priority.ALWAYS);
        return separator;
    }

    private MFXButton buildAddButton() {
        addBtn = new MFXButton("Add");
        addBtn.getStyleClass().add("filled");
        addBtn.setOnAction(event -> SpotyDialog.createDialog(new PurchaseDetailForm(), this).showAndWait());
        addBtn.setPrefWidth(10000d);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        return addBtn;
    }

    private MFXTableView<PurchaseDetail> buildTable() {
        detailTable = new MFXTableView<>();
        HBox.setHgrow(detailTable, Priority.ALWAYS);
        setupTable();
        return detailTable;
    }

    private VBox createCustomGrid() {
        var hbox1 = new HBox();
        hbox1.getChildren().addAll(buildSupplier(), createDatePicker());
        hbox1.setSpacing(20d);
        HBox.setHgrow(hbox1, Priority.ALWAYS);
        var hbox2 = new HBox();
        hbox2.getChildren().addAll(buildStatus(), buildNote());
        hbox2.setSpacing(20d);
        HBox.setHgrow(hbox2, Priority.ALWAYS);
        var vbox = new VBox();
        vbox.setSpacing(10d);
        vbox.getChildren().addAll(hbox1, hbox2);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        UIUtils.anchor(vbox, 40d, 0d, null, 0d);
        return vbox;
    }

    // Validation label.
    private Label buildValidationLabel() {
        var label = new Label();
        label.setManaged(false);
        label.setVisible(false);
        label.setWrapText(true);
        label.getStyleClass().add("input-validation-error");
        return label;
    }

    private VBox buildFieldHolder(Node... nodes) {
        VBox vbox = new VBox();
        vbox.setSpacing(5d);
        vbox.setPadding(new Insets(5d, 0d, 0d, 0d));
        vbox.getChildren().addAll(nodes);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        return vbox;
    }

    private VBox buildSupplier() {
        supplier = new MFXFilterComboBox<>();
        supplier.setFloatMode(FloatMode.BORDER);
        supplier.setFloatingText("Supplier");
        supplier.setPrefWidth(10000d);
        supplierValidationLabel = buildValidationLabel();
        return buildFieldHolder(supplier, supplierValidationLabel);
    }

    private VBox buildStatus() {
        purchaseStatus = new MFXFilterComboBox<>();
        purchaseStatus.setFloatMode(FloatMode.BORDER);
        purchaseStatus.setFloatingText("Purchase Status");
        purchaseStatus.setPrefWidth(10000d);
        statusValidationLabel = buildValidationLabel();
        return buildFieldHolder(purchaseStatus, statusValidationLabel);
    }

    private VBox createDatePicker() {
        date = new MFXDatePicker();
        date.setFloatMode(FloatMode.BORDER);
        date.setFloatingText("Date");
        date.setPrefWidth(10000d);
        dateValidationLabel = buildValidationLabel();
        return buildFieldHolder(date, dateValidationLabel);
    }

    private VBox buildNote() {
        note = new MFXTextField();
        note.setFloatMode(FloatMode.BORDER);
        note.setFloatingText("Note");
        note.setPrefWidth(10000d);
        return buildFieldHolder(note);
    }

    private MFXButton buildSaveButton() {
        saveBtn = new MFXButton("Save");
        saveBtn.getStyleClass().add("filled");
        saveBtn.setOnAction(event -> {
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
        });
        return saveBtn;
    }

    private MFXButton buildCancelButton() {
        cancelBtn = new MFXButton("Cancel");
        cancelBtn.getStyleClass().add("outlined");
        cancelBtn.setOnAction(event -> {
            AppManager.getNavigation().navigate(PurchasePage.class);
            PurchaseMasterViewModel.resetProperties();
            this.dispose();
        });
        return cancelBtn;
    }

    private HBox createButtonBox() {
        var buttonBox = new HBox(20.0);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10.0));

        buttonBox.getChildren().addAll(buildSaveButton(), buildCancelButton());
        HBox.setHgrow(buttonBox, Priority.ALWAYS);
        return buttonBox;
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
        detailTable.setPrefSize(10000, 10000);
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
        contextMenu.addItems(createMenuItem("Delete", event -> new DeleteConfirmationDialog(() -> handleDeleteAction(row), row.getData().getProductName(), this)), createMenuItem("Edit", event -> handleEditAction(row)));
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
        SpotyDialog.createDialog(new PurchaseDetailForm(), this).showAndWait();
    }

    private void bindTableItems() {
        if (PurchaseDetailViewModel.getPurchaseDetails().isEmpty()) {
            PurchaseDetailViewModel.getPurchaseDetails().addListener((ListChangeListener<PurchaseDetail>) change -> detailTable.setItems(PurchaseDetailViewModel.getPurchaseDetails()));
        } else {
            detailTable.itemsProperty().bindBidirectional(PurchaseDetailViewModel.purchaseDetailsProperty());
        }
    }

    private void onSuccess() {
        this.dispose();
        AppManager.getNavigation().navigate(PurchasePage.class);
        PurchaseMasterViewModel.resetProperties();
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
        if (!AppManager.getMorphPane().getChildren().contains(notification)) {
            AppManager.getMorphPane().getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        supplierValidationLabel = null;
        dateValidationLabel = null;
        statusValidationLabel = null;
        date = null;
        supplier = null;
        detailTable = null;
        note = null;
        purchaseStatus = null;
        saveBtn = null;
        cancelBtn = null;
        addBtn = null;
    }
}
