package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.util.validation.Constraint;
import inc.nomard.spoty.core.util.validation.Severity;
import inc.nomard.spoty.core.viewModels.SupplierViewModel;
import inc.nomard.spoty.core.viewModels.requisitions.RequisitionDetailViewModel;
import inc.nomard.spoty.core.viewModels.requisitions.RequisitionMasterViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.components.validatables.ValidatableComboBox;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextArea;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.util.FunctionalStringConverter;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.core.views.util.Validators;
import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.dtos.requisitions.RequisitionDetail;
import inc.nomard.spoty.utils.AppUtils;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ListChangeListener;
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
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static inc.nomard.spoty.core.util.validation.Validated.INVALID_PSEUDO_CLASS;

@Slf4j
public class RequisitionMasterForm extends VBox {
    private final ModalPane modalPane1;
    private final ModalPane modalPane2;
    public CustomButton saveBtn;
    public Button cancelBtn, addBtn;
    private Label supplierValidationLabel;
    private ValidatableComboBox<Supplier> supplier;
    private TableView<RequisitionDetail> tableView;
    private ValidatableTextArea note;
    private TableColumn<RequisitionDetail, RequisitionDetail> product;
    private TableColumn<RequisitionDetail, RequisitionDetail> quantity;

    public RequisitionMasterForm(ModalPane modalPane1, ModalPane modalPane2) {
        this.modalPane1 = modalPane1;
        this.modalPane2 = modalPane2;
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
        var subTitle = new Text("Requisition");
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
        dialog.getChildren().add(new RequisitionDetailForm(modalPane2));
        dialog.setPadding(new Insets(5d));
        modalPane2.setAlignment(Pos.CENTER_RIGHT);
        modalPane2.show(dialog);
        modalPane2.setPersistent(true);
    }

    private TableView<RequisitionDetail> buildTable() {
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
            if (!tableView.isDisabled() && RequisitionDetailViewModel.getRequisitionDetails().isEmpty()) {
                errorMessage("Table can't be Empty");
            }
            validateFields();

            if (isValidForm()) {
                saveBtn.startLoading();
                if (RequisitionMasterViewModel.getId() > 0) {
                    RequisitionMasterViewModel.updateItem(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                } else {
                    RequisitionMasterViewModel.saveRequisitionMaster(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                }
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
        supplier.valueProperty().bindBidirectional(RequisitionMasterViewModel.supplierProperty());
        note.textProperty().bindBidirectional(RequisitionMasterViewModel.noteProperty());
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
        return supplier.validate().isEmpty() && !tableView.isDisabled() && !RequisitionDetailViewModel.getRequisitionDetails().isEmpty();
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
            TableRow<RequisitionDetail> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> showContextMenu(row).show(tableView.getScene().getWindow(), event.getScreenX(), event.getScreenY()));
            return row;
        });
    }

    private ContextMenu showContextMenu(TableRow<RequisitionDetail> row) {
        var contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(createMenuItem("Delete", event -> new DeleteConfirmationDialog(AppManager.getGlobalModalPane(), () -> handleDeleteAction(row), row.getItem().getProductName()).showDialog()), createMenuItem("Edit", event -> handleEditAction(row)));
        return contextMenu;
    }

    private MenuItem createMenuItem(String text, EventHandler<ActionEvent> action) {
        MenuItem menuItem = new MenuItem(text);
        menuItem.setOnAction(action);
        return menuItem;
    }

    private void handleDeleteAction(TableRow<RequisitionDetail> row) {
        RequisitionDetailViewModel.removeRequisitionDetail(row.getItem().getId(), RequisitionDetailViewModel.getRequisitionDetails().indexOf(row.getItem()));
    }

    private void handleEditAction(TableRow<RequisitionDetail> row) {
        Platform.runLater(() -> RequisitionDetailViewModel.getRequisitionDetail(row.getItem().getId()));
        this.showForm();
    }

    private void bindTableItems() {
        tableView.setItems(RequisitionDetailViewModel.getRequisitionDetails());
    }

    private void onSuccess() {
        this.dispose();
        RequisitionMasterViewModel.getAllRequisitionMasters(null, null, null, null);
    }

    private void requiredValidator() {
        setupValidation(supplier, "Supplier is required", supplierValidationLabel);
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

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        saveBtn.stopLoading();
    }

    private void setupTableColumnData() {
        product.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        product.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(RequisitionDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getProductName());
            }
        });
        quantity.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        quantity.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(RequisitionDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getQuantity()));
            }
        });
    }

    public void dispose() {
        modalPane1.hide(true);
        modalPane1.setPersistent(false);
        RequisitionMasterViewModel.resetProperties();
        supplierValidationLabel = null;
        supplier = null;
        tableView = null;
        note = null;
        saveBtn = null;
        cancelBtn = null;
        addBtn = null;
    }
}
