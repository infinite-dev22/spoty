package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.viewModels.DiscountViewModel;
import inc.nomard.spoty.core.viewModels.SupplierViewModel;
import inc.nomard.spoty.core.viewModels.TaxViewModel;
import inc.nomard.spoty.core.viewModels.purchases.PurchaseDetailViewModel;
import inc.nomard.spoty.core.viewModels.purchases.PurchaseMasterViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.components.validatables.ValidatableComboBox;
import inc.nomard.spoty.core.views.components.validatables.ValidatableDatePicker;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextArea;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextField;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.util.FunctionalStringConverter;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.core.views.util.Validators;
import inc.nomard.spoty.network_bridge.dtos.Discount;
import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.dtos.Tax;
import inc.nomard.spoty.network_bridge.dtos.purchases.PurchaseDetail;
import inc.nomard.spoty.utils.AppUtils;
import inc.nomard.spoty.core.util.validation.Constraint;
import inc.nomard.spoty.core.util.validation.Severity;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import lombok.extern.java.Log;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static inc.nomard.spoty.core.util.validation.Validated.INVALID_PSEUDO_CLASS;

@Log
public class PurchaseMasterForm extends VBox {
    private final ModalPane modalPane1;
    private final ModalPane modalPane2;
    public CustomButton saveBtn;
    public Button cancelBtn, addBtn;
    private Label supplierValidationLabel;
    private Label paidAmountValidationLabel;
    private Label shippingValidationLabel;
    private Label dateValidationLabel;
    private ValidatableComboBox<Discount> discount;
    private ValidatableComboBox<Tax> tax;
    private ValidatableDatePicker date;
    private ValidatableTextField paidAmount;
    private ValidatableTextField shipping;
    private ValidatableComboBox<Supplier> supplier;
    private TableView<PurchaseDetail> tableView;
    private ValidatableTextArea note;
    private TableColumn<PurchaseDetail, PurchaseDetail> product;
    private TableColumn<PurchaseDetail, PurchaseDetail> quantity;

    public PurchaseMasterForm(ModalPane modalPane1, ModalPane modalPane2) {
        this.modalPane1 = modalPane1;
        this.modalPane2 = modalPane2;
        init();
        initializeComponentProperties();
    }

    public void initializeComponentProperties() {
        setPurchaseComboBoxes();
        bindProperties();
        configureSupplierComboBox();
        requiredValidator();
    }

    private void init() {
        this.getStyleClass().add("card-flat");
        this.setPadding(new Insets(10d));
        this.setSpacing(10d);
        VBox.setVgrow(this, Priority.ALWAYS);
        this.getChildren().addAll(buildTitle(),
                buildSupplier(),
                createDatePicker(),
                buildAddButton(),
                buildTable(),
                buildDeductions(),
                buildFees(),
                buildNote(),
                createButtonBox());
    }

    private Separator buildSeparator() {
        var separator = new Separator();
        separator.setPrefWidth(200.0);
        HBox.setHgrow(separator, Priority.ALWAYS);
        return separator;
    }

    private VBox buildTitle() {
        var title = new Text("Create");
        title.getStyleClass().add(Styles.TITLE_3);
        var subTitle = new Text("Purchase");
        subTitle.getStyleClass().add(Styles.TITLE_4);
        return buildFieldHolder(title, subTitle, buildSeparator());
    }

    private Button buildAddButton() {
        addBtn = new Button("Add");
        addBtn.setDefaultButton(true);
        addBtn.setOnAction(event -> this.showForm());
        addBtn.setPrefWidth(10000d);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        return addBtn;
    }

    private void showForm() {
        var dialog = new ModalContentHolder(450, 250);
        dialog.getChildren().add(new PurchaseDetailForm(modalPane2));
        dialog.setPadding(new Insets(5d));
        modalPane2.setAlignment(Pos.CENTER_RIGHT);
        modalPane2.show(dialog);
        modalPane2.setPersistent(true);
    }

    private TableView<PurchaseDetail> buildTable() {
        tableView = new TableView<>();
        HBox.setHgrow(tableView, Priority.ALWAYS);
        setupTable();
        return tableView;
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
        var label = new Label("Supplier");
        supplier = new ValidatableComboBox<>();
        supplier.setPrefWidth(10000d);
        supplierValidationLabel = Validators.buildValidationLabel();
        return buildFieldHolder(label, supplier, supplierValidationLabel);
    }

    private VBox createDatePicker() {
        var label = new Label("Date");
        date = new ValidatableDatePicker(LocalDate.now());
        date.setPrefWidth(10000d);
        dateValidationLabel = Validators.buildValidationLabel();
        return buildFieldHolder(label, date, dateValidationLabel);
    }

    private VBox buildNote() {
        var label = new Label("Note");
        note = new ValidatableTextArea();
        note.setMinHeight(100d);
        note.setWrapText(true);
        return buildFieldHolder(label, note);
    }

    private CustomButton buildSaveButton() {
        saveBtn = new CustomButton("Save");
        saveBtn.getStyleClass().add(Styles.ACCENT);
        saveBtn.setOnAction(event -> {
            if (!tableView.isDisabled() && PurchaseDetailViewModel.getPurchaseDetails().isEmpty()) {
                errorMessage("Table can't be Empty");
            }
            validateFields();

            if (isValidForm()) {
                saveBtn.startLoading();
                if (PurchaseMasterViewModel.getId() > 0) {
                    PurchaseMasterViewModel.updateItem(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                } else {
                    PurchaseMasterViewModel.savePurchaseMaster(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                }
            }
        });
        return saveBtn;
    }

    private Button buildCancelButton() {
        cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
        cancelBtn.setOnAction(event -> this.dispose());
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

    // Deductions UI.
    private HBox buildDeductions() {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10d);
        hbox.getChildren().addAll(buildDiscount(), buildTax());
        return hbox;
    }

    private VBox buildDiscount() {
        var label = new Label("Discount");
        discount = new ValidatableComboBox<>();
        discount.setPrefWidth(10000d);
        return buildFieldHolder(label, discount);
    }

    private VBox buildTax() {
        var label = new Label("Tax");
        tax = new ValidatableComboBox<>();
        tax.setPrefWidth(10000d);
        return buildFieldHolder(label, tax);
    }

    private void setPurchaseComboBoxes() {
        setupComboBox(discount, DiscountViewModel.getDiscounts(), PurchaseMasterViewModel.discountProperty(), discount -> (discount == null) ? "" : discount.getName() + " (" + discount.getPercentage() + "%)");
        setupComboBox(tax, TaxViewModel.getTaxes(), PurchaseMasterViewModel.netTaxProperty(), tax -> (tax == null) ? "" : tax.getName() + " (" + tax.getPercentage() + "%)");
    }

    private <T> void setupComboBox(ComboBox<T> comboBox, ObservableList<T> items,
                                   Property<T> property, Function<T, String> converter) {
        if (property != null) {
            comboBox.valueProperty().bindBidirectional(property);
        }
        if (converter != null) {
            StringConverter<T> itemConverter = FunctionalStringConverter.to(converter);
            comboBox.setConverter(itemConverter);
        }
        if (items.isEmpty()) {
            items.addListener((ListChangeListener<T>) c -> comboBox.setItems(items));
        } else {
            comboBox.itemsProperty().bindBidirectional(new SimpleListProperty<>(items));
        }
    }

    private VBox buildShipping() {
        var label = new Label("Shipping");
        shipping = new ValidatableTextField();
        shipping.setLeft(new Text("UGX"));
        shipping.setPrefWidth(10000d);
        shippingValidationLabel = Validators.buildValidationLabel();
        return buildFieldHolder(label, shipping, shippingValidationLabel);
    }

    private VBox buildPaidAmount() {
        var label = new Label("Paid Amount");
        paidAmount = new ValidatableTextField();
        paidAmount.setLeft(new Text("UGX"));
        paidAmount.setPrefWidth(10000d);
        paidAmountValidationLabel = Validators.buildValidationLabel();
        return buildFieldHolder(label, paidAmount, paidAmountValidationLabel);
    }

    private HBox buildFees() {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10d);
        hbox.getChildren().addAll(buildShipping(), buildPaidAmount());
        return hbox;
    }

    private void bindProperties() {
        date.valueProperty().bindBidirectional(PurchaseMasterViewModel.dateProperty());
        supplier.valueProperty().bindBidirectional(PurchaseMasterViewModel.supplierProperty());
        note.textProperty().bindBidirectional(PurchaseMasterViewModel.noteProperty());
        paidAmount.textProperty().bindBidirectional(PurchaseMasterViewModel.amountPaidProperty(), new NumberStringConverter());
        shipping.textProperty().bindBidirectional(PurchaseMasterViewModel.shippingFeeProperty(), new NumberStringConverter());
    }

    private void configureSupplierComboBox() {
        StringConverter<Supplier> supplierConverter = FunctionalStringConverter.to(supplier -> (supplier == null) ? "" : supplier.getName());
        Function<String, Predicate<Supplier>> supplierFilterFunction = searchStr -> supplier -> supplierConverter.toString(supplier).toLowerCase().contains(searchStr);

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
        validateField(paidAmount, paidAmountValidationLabel);
        validateField(shipping, shippingValidationLabel);
    }

    private <T> void validateField(ValidatableComboBox<T> field, Label validationLabel) {
        List<Constraint> constraints = field.validate();
        if (!constraints.isEmpty()) {
            validationLabel.setManaged(true);
            validationLabel.setVisible(true);
            validationLabel.setText(constraints.getFirst().getMessage());
            field.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
    }

    private void validateField(ValidatableDatePicker field, Label validationLabel) {
        List<Constraint> constraints = field.validate();
        if (!constraints.isEmpty()) {
            validationLabel.setManaged(true);
            validationLabel.setVisible(true);
            validationLabel.setText(constraints.getFirst().getMessage());
            field.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
    }

    private void validateField(ValidatableTextField field, Label validationLabel) {
        List<Constraint> constraints = field.validate();
        if (!constraints.isEmpty()) {
            validationLabel.setManaged(true);
            validationLabel.setVisible(true);
            validationLabel.setText(constraints.getFirst().getMessage());
            field.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
    }

    private boolean isValidForm() {
        return supplier.validate().isEmpty() && date.validate().isEmpty() && !tableView.isDisabled() && !PurchaseDetailViewModel.getPurchaseDetails().isEmpty();
    }

    private void setupTable() {
        setupTableColumns();
        styleTable();
        bindTableItems();
    }

    private void setupTableColumns() {
        product = new TableColumn<>("Product");
        quantity = new TableColumn<>("Quantity");

        product.prefWidthProperty().bind(tableView.widthProperty().multiply(.7));
        quantity.prefWidthProperty().bind(tableView.widthProperty().multiply(.3));

        setupTableColumnData();

        var columnList = new LinkedList<>(Stream.of(product, quantity).toList());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tableView.getColumns().addAll(columnList);
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
        var contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(createMenuItem("Delete", event -> new DeleteConfirmationDialog(AppManager.getGlobalModalPane(), () -> handleDeleteAction(row), row.getItem().getProductName()).showDialog()), createMenuItem("Edit", event -> handleEditAction(row)));
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
        this.showForm();
    }

    private void bindTableItems() {
        tableView.setItems(PurchaseDetailViewModel.getPurchaseDetails());
    }

    private void onSuccess() {
        this.dispose();
        PurchaseMasterViewModel.getAllPurchaseMasters(null, null, null, null);
    }

    private void requiredValidator() {
        setupValidation(supplier, "Supplier is required", supplierValidationLabel);
        setupValidation(date, dateValidationLabel);
        setupValidation(paidAmount, "Paid Amount is required", paidAmountValidationLabel);
        setupValidation(shipping, "Shipping is required", shippingValidationLabel);
    }

    private <T> void setupValidation(ValidatableComboBox<T> field, String message, Label validationLabel) {
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

    private void setupValidation(ValidatableDatePicker field, Label validationLabel) {
        Constraint constraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Date is required")
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

    private void setupValidation(ValidatableTextField field, String message, Label validationLabel) {
        Constraint constraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(message)
                .setCondition(field.textProperty().isNotNull())
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

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        saveBtn.stopLoading();
    }

    private void setupTableColumnData() {
        product.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        product.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getProductName());
            }
        });
        quantity.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        quantity.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getQuantity()));
            }
        });
    }

    public void dispose() {
        modalPane1.hide(true);
        modalPane1.setPersistent(false);
        PurchaseMasterViewModel.resetProperties();
        supplierValidationLabel = null;
        dateValidationLabel = null;
        date = null;
        supplier = null;
        tableView = null;
        note = null;
        saveBtn = null;
        cancelBtn = null;
        addBtn = null;
    }
}
