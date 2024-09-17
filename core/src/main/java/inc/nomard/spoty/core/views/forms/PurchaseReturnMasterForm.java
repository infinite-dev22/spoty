package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.SupplierViewModel;
import inc.nomard.spoty.core.viewModels.purchases.PurchaseDetailViewModel;
import inc.nomard.spoty.core.viewModels.purchases.PurchaseMasterViewModel;
import inc.nomard.spoty.core.viewModels.returns.purchases.PurchaseReturnDetailViewModel;
import inc.nomard.spoty.core.viewModels.returns.purchases.PurchaseReturnMasterViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.ValidatableComboBox;
import inc.nomard.spoty.core.views.components.validatables.ValidatableDatePicker;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextArea;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.message.SpotyMessage;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import inc.nomard.spoty.core.views.util.Validators;
import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.dtos.purchases.PurchaseDetail;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.StringConverter;
import lombok.extern.java.Log;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

@Log
public class PurchaseReturnMasterForm extends VBox {
    private final ModalPane modalPane;
    public CustomButton saveBtn;
    public Button cancelBtn, addBtn;
    private Label supplierValidationLabel;
    private Label dateValidationLabel;
    private ValidatableDatePicker date;
    private ValidatableComboBox<Supplier> supplier;
    private TableView<PurchaseDetail> tableView;
    private ValidatableTextArea note;

    public PurchaseReturnMasterForm(ModalPane modalPane) {
        this.modalPane = modalPane;
        init();
        initializeComponentProperties();
    }

    public void initializeComponentProperties() {
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
        var subTitle = new Text("Purchase return");
        subTitle.getStyleClass().add(Styles.TITLE_4);
        return buildFieldHolder(title, subTitle, buildSeparator());
    }

    private Button buildAddButton() {
        addBtn = new Button("Add");
        addBtn.setDefaultButton(true);
        addBtn.setDisable(true);
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
        supplier.setDisable(true);
        return buildFieldHolder(label, supplier);
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
            processProducts();
            if (!tableView.isDisabled() && PurchaseReturnDetailViewModel.getPurchaseReturnDetails().isEmpty()) {
                errorMessage("No products selected");
            }
            validateFields();

            if (isValidForm()) {
                PurchaseReturnMasterViewModel.savePurchaseReturnMaster(this::onSuccess, this::successMessage, this::errorMessage);
            }
        });
        return saveBtn;
    }

    private Button buildCancelButton() {
        cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
        cancelBtn.setOnAction(event -> {
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
        note.textProperty().bindBidirectional(PurchaseMasterViewModel.noteProperty());
    }

    private void configureSupplierComboBox() {
        StringConverter<Supplier> supplierConverter = FunctionalStringConverter.to(supplier -> (supplier == null) ? "" : supplier.getName());

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

    private boolean isValidForm() {
        return supplier.validate().isEmpty() && date.validate().isEmpty() && !tableView.isDisabled() && !PurchaseReturnDetailViewModel.getPurchaseReturnDetails().isEmpty();
    }

    private void processProducts() {
        PurchaseDetailViewModel.getPurchaseDetails().forEach(purchaseDetail -> {
            if (purchaseDetail.getSelected().get()) {
                PurchaseReturnDetailViewModel.getPurchaseReturnDetails().add(purchaseDetail);
            }
        });
    }

    private void setupTable() {
        setupTableColumns();
        styleTable();
        bindTableItems();
    }

    private void setupTableColumns() {
        var selectAll = new CheckBox();

        var select = new TableColumn<PurchaseDetail, Boolean>();
        select.setGraphic(selectAll);
        select.setSortable(false);
        select.setCellValueFactory(c -> c.getValue().getSelected());
        select.setCellFactory(CheckBoxTableCell.forTableColumn(select));
        select.setEditable(true);
        selectAll.setOnAction(evt -> {
            tableView.getItems().forEach(
                    item -> item.getSelected().set(selectAll.isSelected())
            );
            evt.consume();
        });
        var product = new TableColumn<PurchaseDetail, PurchaseDetail>("Product");
        var quantity = new TableColumn<PurchaseDetail, PurchaseDetail>("Quantity");

        product.prefWidthProperty().bind(tableView.widthProperty().multiply(.7));
        quantity.prefWidthProperty().bind(tableView.widthProperty().multiply(.3));

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
                this.setAlignment(Pos.CENTER);
                if (!empty || !Objects.isNull(item)) {
                    Spinner<Integer> quantitySpinner = new Spinner<>(0, 999999999, 1);
                    quantitySpinner.getStyleClass().addAll(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
                    quantitySpinner.getValueFactory().setValue(item.getQuantity());
                    quantitySpinner.valueProperty().addListener(
                            (obs, oldValue, newValue) -> {
                                PurchaseDetailViewModel.getPurchaseDetail(item);
                                PurchaseDetailViewModel.setQuantity(String.valueOf(newValue));
                            }
                    );
                    setGraphic(quantitySpinner);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(null);
                }
            }
        });

        var columnList = new LinkedList<>(Stream.of(select, product, quantity).toList());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tableView.getColumns().addAll(columnList);
        tableView.setEditable(true);
    }

    private void styleTable() {
        tableView.setPrefSize(10000, 10000);
        tableView.setRowFactory(t -> {
            TableRow<PurchaseDetail> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
            });
            return row;
        });
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
        setupValidation(date, "Date is required", dateValidationLabel);
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

    private void setupValidation(ValidatableDatePicker field, String message, Label validationLabel) {
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
        displayNotification(message, MessageVariants.SUCCESS, FontAwesomeSolid.CHECK_CIRCLE);
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, FontAwesomeSolid.EXCLAMATION_TRIANGLE);
    }

    private void displayNotification(String message, MessageVariants type, Ikon icon) {
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

    public void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
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
