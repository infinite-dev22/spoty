package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.viewModels.CustomerViewModel;
import inc.nomard.spoty.core.viewModels.DiscountViewModel;
import inc.nomard.spoty.core.viewModels.TaxViewModel;
import inc.nomard.spoty.core.viewModels.quotations.QuotationDetailViewModel;
import inc.nomard.spoty.core.viewModels.quotations.QuotationMasterViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.components.validatables.ValidatableComboBox;
import inc.nomard.spoty.core.views.components.validatables.ValidatableNumberField;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextArea;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.core.views.util.Validators;
import inc.nomard.spoty.network_bridge.dtos.Customer;
import inc.nomard.spoty.network_bridge.dtos.Discount;
import inc.nomard.spoty.network_bridge.dtos.Tax;
import inc.nomard.spoty.network_bridge.dtos.quotations.QuotationDetail;
import inc.nomard.spoty.utils.AppUtils;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

@SuppressWarnings("unchecked")
@Log
public class QuotationMasterForm extends VBox {
    private final ModalPane modalPane1;
    private final ModalPane modalPane2;
    private ValidatableComboBox<Customer> customer;
    private ValidatableComboBox<Tax> tax;
    private ValidatableComboBox<Discount> discount;
    private TableView<QuotationDetail> table;
    private ValidatableNumberField shippingFee;
    private ValidatableTextArea note;
    private Label customerValidationLabel;
    private CustomButton saveBtn;
    private Button cancelBtn,
            addBtn;
    private TableColumn<QuotationDetail, QuotationDetail> product, price, quantity;

    public QuotationMasterForm(ModalPane modalPane1, ModalPane modalPane2) {
        this.modalPane1 = modalPane1;
        this.modalPane2 = modalPane2;
        init();
        initializeComponentProperties();
    }

    public void initializeComponentProperties() {
        requiredValidator();
    }

    private void init() {
        this.getStyleClass().add("card-flat");
        this.setPadding(new Insets(10d));
        this.setSpacing(10d);
        VBox.setVgrow(this, Priority.ALWAYS);
        this.getChildren().addAll(buildTitle(),
                buildCustomer(),
                buildAddButton(),
                buildTable(),
                new HBox(20, buildTax(), buildDiscount()),
                buildShippingFee(),
                buildNote(),
                createButtonBox());
    }

    private Separator buildSeparator() {
        var separator = new Separator();
        separator.setPrefWidth(200.0);
        HBox.setHgrow(separator, Priority.ALWAYS);
        return separator;
    }

    private Button buildAddButton() {
        addBtn = new Button("Add Product");
        addBtn.setDefaultButton(true);
        addBtn.setOnAction(event -> this.showForm());
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        return addBtn;
    }

    private void showForm() {
        var dialog = new ModalContentHolder(450, 250);
        dialog.getChildren().add(new QuotationDetailForm(modalPane2));
        dialog.setPadding(new Insets(5d));
        modalPane2.setAlignment(Pos.CENTER_RIGHT);
        modalPane2.show(dialog);
        modalPane2.setPersistent(true);
    }

    private TableView<QuotationDetail> buildTable() {
        table = new TableView<>();
        HBox.setHgrow(table, Priority.ALWAYS);
        setupTable();
        return table;
    }

    private VBox buildFieldHolder(Node... nodes) {
        VBox vbox = new VBox();
        vbox.setSpacing(5d);
        vbox.setPadding(new Insets(5d, 0d, 0d, 0d));
        vbox.getChildren().addAll(nodes);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        return vbox;
    }

    private VBox buildTitle() {
        var title = new Text("Create");
        title.getStyleClass().add(Styles.TITLE_3);
        var subTitle = new Text("Quotation");
        subTitle.getStyleClass().add(Styles.TITLE_4);
        return buildFieldHolder(title, subTitle, buildSeparator());
    }

    private VBox buildCustomer() {
        customer = new ValidatableComboBox<>();
        var label = new Label("Customer");
        HBox.setHgrow(customer, Priority.ALWAYS);
        customer.setPrefWidth(1000d);
        customer
                .valueProperty()
                .bindBidirectional(QuotationMasterViewModel.customerProperty());
        // ComboBox Converters.
        StringConverter<Customer> customerConverter =
                FunctionalStringConverter.to(customer -> (customer == null) ? "" : customer.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<Customer>> customerFilterFunction =
                searchStr ->
                        customer ->
                                StringUtils.containsIgnoreCase(customerConverter.toString(customer), searchStr);

        // Combo box properties.
        customer.setConverter(customerConverter);
        if (CustomerViewModel.getCustomers().isEmpty()) {
            CustomerViewModel.getCustomers()
                    .addListener(
                            (ListChangeListener<Customer>)
                                    c -> customer.setItems(CustomerViewModel.getCustomers()));
        } else {
            customer.itemsProperty().bindBidirectional(CustomerViewModel.customersProperty());
        }
        customerValidationLabel = Validators.buildValidationLabel();
        return buildFieldHolder(label, customer, customerValidationLabel);
    }

    private VBox buildTax() {
        var label = new Label("Tax");
        tax = new ValidatableComboBox<>();
        tax.setPrefWidth(1000d);
        tax
                .valueProperty()
                .bindBidirectional(QuotationMasterViewModel.taxProperty());
        // ComboBox Converters.
        StringConverter<Tax> taxConverter =
                FunctionalStringConverter.to(tax -> (tax == null) ? "" : tax.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<Tax>> taxFilterFunction =
                searchStr ->
                        tax ->
                                StringUtils.containsIgnoreCase(taxConverter.toString(tax), searchStr);

        // Combo box properties.
        tax.setConverter(taxConverter);
        if (TaxViewModel.getTaxes().isEmpty()) {
            TaxViewModel.getTaxes()
                    .addListener(
                            (ListChangeListener<Tax>)
                                    c -> tax.setItems(TaxViewModel.getTaxes()));
        } else {
            tax.itemsProperty().bindBidirectional(TaxViewModel.taxesProperty());
        }
        return buildFieldHolder(label, tax);
    }

    private VBox buildDiscount() {
        var label = new Label("Discount");
        discount = new ValidatableComboBox<>();
        discount.setPrefWidth(1000d);
        discount
                .valueProperty()
                .bindBidirectional(QuotationMasterViewModel.discountProperty());
        // ComboBox Converters.
        StringConverter<Discount> discountConverter =
                FunctionalStringConverter.to(discount -> (discount == null) ? "" : discount.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<Discount>> discountFilterFunction =
                searchStr ->
                        discount ->
                                StringUtils.containsIgnoreCase(discountConverter.toString(discount), searchStr);

        // Combo box properties.
        discount.setConverter(discountConverter);
        if (DiscountViewModel.getDiscounts().isEmpty()) {
            DiscountViewModel.getDiscounts()
                    .addListener(
                            (ListChangeListener<Discount>)
                                    c -> discount.setItems(DiscountViewModel.getDiscounts()));
        } else {
            discount.itemsProperty().bindBidirectional(DiscountViewModel.discountsProperty());
        }
        return buildFieldHolder(label, discount);
    }

    private VBox buildShippingFee() {
        var label = new Label("Shipping Fee");
        shippingFee = new ValidatableNumberField();
        shippingFee.setPrefWidth(1000d);
        shippingFee.setLeft(new Label("UGX"));
        shippingFee.textProperty().bindBidirectional(QuotationMasterViewModel.shippingFeeProperty());
        return buildFieldHolder(label, shippingFee);
    }

    private VBox buildNote() {
        note = new ValidatableTextArea();
        note.setPrefHeight(100d);
        note.setWrapText(true);
        var label = new Label("Note");
        note.textProperty().bindBidirectional(QuotationMasterViewModel.noteProperty());
        return buildFieldHolder(label, note);
    }

    private CustomButton buildSaveButton() {
        saveBtn = new CustomButton("Save");
        saveBtn.getStyleClass().add(Styles.ACCENT);
        saveBtn.setOnAction(event -> {
            if (!table.isDisabled() && QuotationDetailViewModel.getQuotationDetails().isEmpty()) {
                errorMessage("Table can't be Empty");
            }
            validateFields();

            if (isValidForm()) {
                saveBtn.startLoading();
                if (QuotationMasterViewModel.getId() > 0) {
                    QuotationMasterViewModel.updateItem(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                } else {
                    QuotationMasterViewModel.saveQuotationMaster(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
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

    private void setupTable() {
        product = new TableColumn<>("Product");
        price = new TableColumn<>("Price");
        quantity = new TableColumn<>("Quantity");

        product.prefWidthProperty().bind(table.widthProperty().multiply(.4));
        price.prefWidthProperty().bind(table.widthProperty().multiply(.3));
        quantity.prefWidthProperty().bind(table.widthProperty().multiply(.3));

        setupTableColumnData();

        table.getColumns().addAll(product, price, quantity);
        getQuotationDetailTable();
        table.setItems(QuotationDetailViewModel.getQuotationDetails());
    }

    private void getQuotationDetailTable() {
        table.setRowFactory(
                quotationDetail -> {
                    TableRow<QuotationDetail> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<QuotationDetail>) event.getSource())
                                        .show(
                                                table.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private ContextMenu showContextMenu(TableRow<QuotationDetail> obj) {
        var contextMenu = new ContextMenu();
        var delete = new MenuItem("Delete");
        var edit = new MenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            QuotationDetailViewModel.removeQuotationDetail(
                    obj.getItem().getId(),
                    QuotationDetailViewModel.quotationDetailsList.indexOf(obj.getItem()));
            event.consume();
        }, obj.getItem().getProductName(), this));
        // Edit
        edit.setOnAction(
                event -> {
                    QuotationDetailViewModel.getQuotationDetail(obj.getItem());
                    this.showForm();
                    event.consume();
                });

        contextMenu.getItems().addAll(edit, delete);

        return contextMenu;
    }

    private void validateFields() {
        validateField(customer, customerValidationLabel);
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

    private boolean isValidForm() {
        return customer.validate().isEmpty() && !table.isDisabled() && !QuotationDetailViewModel.getQuotationDetails().isEmpty();
    }

    private void onSuccess() {
        this.dispose();
        QuotationMasterViewModel.getAllQuotationMasters(null, null, null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint customerConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Customer is required")
                        .setCondition(customer.valueProperty().isNotNull())
                        .get();
        customer.getValidator().constraint(customerConstraint);
        // Display error.
        customer
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                customerValidationLabel.setManaged(false);
                                customerValidationLabel.setVisible(false);
                                customer.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
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
            public void updateItem(QuotationDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getProductName());
            }
        });
        price.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        price.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(QuotationDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getProductPrice()));
            }
        });
        quantity.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        quantity.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(QuotationDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getQuantity()));
            }
        });
    }

    public void dispose() {
        modalPane1.hide(true);
        modalPane1.setPersistent(false);
        QuotationMasterViewModel.resetProperties();
        customerValidationLabel = null;
        customer = null;
        table = null;
        note = null;
        tax = null;
        discount = null;
        shippingFee = null;
        saveBtn = null;
        cancelBtn = null;
        addBtn = null;
    }
}
