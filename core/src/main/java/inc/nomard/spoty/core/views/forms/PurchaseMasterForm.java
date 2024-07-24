package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.purchases.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.components.label_components.controls.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.pages.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.dtos.purchases.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class PurchaseMasterForm extends OutlineFormPage {
    private Label supplierValidationLabel;
    private Label dateValidationLabel;
    private Label statusValidationLabel;
    private LabeledDatePicker date;
    private LabeledComboBox<Supplier> supplier;
    private TableView<PurchaseDetail> tableView;
    private LabeledTextField note;
    private LabeledComboBox<String> status;
    private Button saveBtn, cancelBtn, addBtn;

    public PurchaseMasterForm() {
        addNode(init());
        initializeComponentProperties();
    }

    public void initializeComponentProperties() {
        bindProperties();
        configureSupplierComboBox();
        status.setItems(FXCollections.observableArrayList(Values.PURCHASE_STATUSES));
        requiredValidator();
    }

    private BorderPane init() {
        Label purchaseFormTitle = new Label("Purchase Form");
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

    private Button buildAddButton() {
        addBtn = new Button("Add");
        addBtn.setDefaultButton(true);
        addBtn.setOnAction(event -> SpotyDialog.createDialog(new PurchaseDetailForm(), this).showAndWait());
        addBtn.setPrefWidth(10000d);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        return addBtn;
    }

    private TableView<PurchaseDetail> buildTable() {
        tableView = new TableView<>();
        HBox.setHgrow(tableView, Priority.ALWAYS);
        setupTable();
        return tableView;
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
        return vbox;
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
        supplier = new LabeledComboBox<>();
        supplier.setLabel("Supplier");
        supplier.setPrefWidth(10000d);
        supplierValidationLabel = Validators.buildValidationLabel();
        return buildFieldHolder(supplier, supplierValidationLabel);
    }

    private VBox buildStatus() {
        status = new LabeledComboBox<>();
        status.setLabel("Purchase Status");
        status.setPrefWidth(10000d);
        statusValidationLabel = Validators.buildValidationLabel();
        return buildFieldHolder(status, statusValidationLabel);
    }

    private VBox createDatePicker() {
        date = new LabeledDatePicker();
        date.setLabel("Date");
        date.setPrefWidth(10000d);
        dateValidationLabel = Validators.buildValidationLabel();
        return buildFieldHolder(date, dateValidationLabel);
    }

    private VBox buildNote() {
        note = new LabeledTextField();
        note.setLabel("Note");
        note.setPrefWidth(10000d);
        return buildFieldHolder(note);
    }

    private Button buildSaveButton() {
        saveBtn = new Button("Save");
        saveBtn.setDefaultButton(true);
        saveBtn.setOnAction(event -> {
            if (!tableView.isDisabled() && PurchaseDetailViewModel.getPurchaseDetails().isEmpty()) {
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

    private Button buildCancelButton() {
        cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
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
        date.valueProperty().bindBidirectional(PurchaseMasterViewModel.dateProperty());
        supplier.valueProperty().bindBidirectional(PurchaseMasterViewModel.supplierProperty());
        status.valueProperty().bindBidirectional(PurchaseMasterViewModel.statusProperty());
        note.textProperty().bindBidirectional(PurchaseMasterViewModel.noteProperty());
    }

    private void configureSupplierComboBox() {
        StringConverter<Supplier> supplierConverter = FunctionalStringConverter.to(supplier -> (supplier == null) ? "" : supplier.getName());
        Function<String, Predicate<Supplier>> supplierFilterFunction = searchStr -> supplier -> StringUtils.containsIgnoreCase(supplierConverter.toString(supplier), searchStr);

        supplier.setConverter(supplierConverter);

        if (SupplierViewModel.getSuppliers().isEmpty()) {
            SupplierViewModel.getSuppliers().addListener((ListChangeListener<Supplier>) c -> supplier.setItems(SupplierViewModel.getSuppliers()));
        } else {
            supplier.itemsProperty().bindBidirectional(SupplierViewModel.suppliersProperty());
        }
    }

    private void validateFields() {
        validateField(supplier, supplierValidationLabel);
        validateField(date, dateValidationLabel);
        validateField(status, statusValidationLabel);
    }

    private <T> void validateField(LabeledComboBox<T> field, Label validationLabel) {
        List<Constraint> constraints = field.validate();
        if (!constraints.isEmpty()) {
            validationLabel.setManaged(true);
            validationLabel.setVisible(true);
            validationLabel.setText(constraints.getFirst().getMessage());
            field.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
    }

    private void validateField(LabeledDatePicker field, Label validationLabel) {
        List<Constraint> constraints = field.validate();
        if (!constraints.isEmpty()) {
            validationLabel.setManaged(true);
            validationLabel.setVisible(true);
            validationLabel.setText(constraints.getFirst().getMessage());
            field.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
    }

    private boolean isValidForm() {
        return supplier.validate().isEmpty() && date.validate().isEmpty() && status.validate().isEmpty() && !tableView.isDisabled() && !PurchaseDetailViewModel.getPurchaseDetails().isEmpty();
    }

    private void setupTable() {
        setupTableColumns();
        styleTable();
        bindTableItems();
    }

    private void setupTableColumns() {
        TableColumn<PurchaseDetail, String> product = createTableColumn("Product", 1);
        TableColumn<PurchaseDetail, String> quantity = createTableColumn("Quantity", 1);

        var columnList = new LinkedList<>(Stream.of(product, quantity).toList());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tableView.getColumns().addAll(columnList);
    }

    private <U extends Comparable<? super U>> TableColumn<PurchaseDetail, String> createTableColumn(String title, double widthPercentage) {
        TableColumn<PurchaseDetail, String> column = new TableColumn<>(title);
        column.prefWidthProperty().bind(tableView.widthProperty().multiply(widthPercentage));
        return column;
    }

    private void styleTable() {
        tableView.setPrefSize(10000, 10000);

        tableView.setRowFactory(t -> {
            TableRow<PurchaseDetail> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> showContextMenu(row).show(tableView.getScene().getWindow(), event.getScreenX(), event.getScreenY()));
            return row;
        });
    }

    private ContextMenu showContextMenu(TableRow<PurchaseDetail> row) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(createMenuItem("Delete", event -> new DeleteConfirmationDialog(() -> handleDeleteAction(row), row.getItem().getProductName(), this)), createMenuItem("Edit", event -> handleEditAction(row)));
        return contextMenu;
    }

    private MenuItem createMenuItem(String text, EventHandler<ActionEvent> action) {
        MenuItem menuItem = new MenuItem(text);
        menuItem.setOnAction(action);
        return menuItem;
    }

    private void handleDeleteAction(TableRow<PurchaseDetail> row) {
        PurchaseDetailViewModel.removePurchaseDetail(row.getItem().getId(), PurchaseDetailViewModel.getPurchaseDetails().indexOf(row.getItem()));
    }

    private void handleEditAction(TableRow<PurchaseDetail> row) {
        Platform.runLater(() -> PurchaseDetailViewModel.getPurchaseDetail(row.getItem()));
        SpotyDialog.createDialog(new PurchaseDetailForm(), this).showAndWait();
    }

    private void bindTableItems() {
        tableView.setItems(PurchaseDetailViewModel.getPurchaseDetails());
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
        setupValidation(status, "Beneficiary Type is required", statusValidationLabel);
        setupValidation(date, "Date is required", dateValidationLabel);
    }

    private <T> void setupValidation(LabeledComboBox<T> field, String message, Label validationLabel) {
        Constraint constraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(message)
                .setCondition(field.valueProperty().isNotNull())
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

    private void setupValidation(LabeledDatePicker field, String message, Label validationLabel) {
        Constraint constraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(message)
                .setCondition(field.valueProperty().isNotNull())
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
        tableView = null;
        note = null;
        status = null;
        saveBtn = null;
        cancelBtn = null;
        addBtn = null;
    }
}
