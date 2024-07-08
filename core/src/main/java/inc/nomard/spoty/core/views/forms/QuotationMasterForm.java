package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.quotations.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.quotations.*;
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
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class QuotationMasterForm extends OutlineFormPage {
    @FXML
    public MFXFilterComboBox<Customer> customer;
    @FXML
    public MFXTableView<QuotationDetail> table;
    @FXML
    public MFXTextField note;
    @FXML
    public MFXFilterComboBox<String> status;
    @FXML
    public Label customerValidationLabel,
            statusValidationLabel;
    @FXML
    public MFXButton saveBtn,
            cancelBtn,
            addBtn;

    public QuotationMasterForm() {
        addNode(init());
        initializeComponentProperties();
    }

    public void initializeComponentProperties() {
        requiredValidator();
    }

    private BorderPane init() {
        Label purchaseFormTitle = new Label("Quotation Form");
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
        addBtn.setOnAction(event -> SpotyDialog.createDialog(new QuotationDetailForm(), this).showAndWait());
        addBtn.setPrefWidth(10000d);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        return addBtn;
    }

    private MFXTableView<QuotationDetail> buildTable() {
        table = new MFXTableView<>();
        HBox.setHgrow(table, Priority.ALWAYS);
        setupTable();
        return table;
    }

    private VBox createCustomGrid() {
        var hbox1 = new HBox();
        hbox1.getChildren().addAll(buildCustomer(), buildStatus());
        hbox1.setSpacing(20d);
        HBox.setHgrow(hbox1, Priority.ALWAYS);
        var hbox2 = new HBox();
        hbox2.getChildren().add(buildNote());
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

    private VBox buildCustomer() {
        customer = new MFXFilterComboBox<>();
        customer.setFloatMode(FloatMode.BORDER);
        customer.setFloatingText("Supplier");
        customer.setPrefWidth(10000d);
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
        customer.setFilterFunction(customerFilterFunction);
        if (CustomerViewModel.getCustomers().isEmpty()) {
            CustomerViewModel.getCustomers()
                    .addListener(
                            (ListChangeListener<Customer>)
                                    c -> customer.setItems(CustomerViewModel.getCustomers()));
        } else {
            customer.itemsProperty().bindBidirectional(CustomerViewModel.customersProperty());
        }
        customerValidationLabel = buildValidationLabel();
        return buildFieldHolder(customer, customerValidationLabel);
    }

    private VBox buildStatus() {
        status = new MFXFilterComboBox<>();
        status.setFloatMode(FloatMode.BORDER);
        status.setFloatingText("Quotation Status");
        status.setPrefWidth(10000d);
        status
                .valueProperty()
                .bindBidirectional(QuotationMasterViewModel.statusProperty());
        status.setItems(FXCollections.observableArrayList(Values.QUOTATION_TYPE));
        statusValidationLabel = buildValidationLabel();
        return buildFieldHolder(status, statusValidationLabel);
    }

    private VBox buildNote() {
        note = new MFXTextField();
        note.setFloatMode(FloatMode.BORDER);
        note.setFloatingText("Note");
        note.setPrefWidth(10000d);
        note.textProperty().bindBidirectional(QuotationMasterViewModel.noteProperty());
        return buildFieldHolder(note);
    }

    private MFXButton buildSaveButton() {
        saveBtn = new MFXButton("Save");
        saveBtn.getStyleClass().add("filled");
        saveBtn.setOnAction(event -> {
            if (!table.isDisabled() && QuotationDetailViewModel.getQuotationDetails().isEmpty()) {
                errorMessage("Table can't be Empty");
            }
            validateFields();

            if (isValidForm()) {
                if (QuotationMasterViewModel.getId() > 0) {
                    QuotationMasterViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                } else {
                    QuotationMasterViewModel.saveQuotationMaster(this::onSuccess, this::successMessage, this::errorMessage);
                }
            }
        });
        return saveBtn;
    }

    private MFXButton buildCancelButton() {
        cancelBtn = new MFXButton("Cancel");
        cancelBtn.getStyleClass().add("outlined");
        cancelBtn.setOnAction(event -> {
            AppManager.getNavigation().navigate(QuotationPage.class);
            QuotationMasterViewModel.resetProperties();
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

    private void setupTable() {
        MFXTableColumn<QuotationDetail> productName =
                new MFXTableColumn<>(
                        "Product", false, Comparator.comparing(QuotationDetail::getProductName));
        MFXTableColumn<QuotationDetail> productPrice =
                new MFXTableColumn<>("Price", false, Comparator.comparing(QuotationDetail::getProductPrice));
        MFXTableColumn<QuotationDetail> productQuantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(QuotationDetail::getQuantity));

        productName.setRowCellFactory(
                product -> new MFXTableRowCell<>(QuotationDetail::getProductName));
        productPrice.setRowCellFactory(product -> new MFXTableRowCell<>(QuotationDetail::getProductPrice));
        productQuantity.setRowCellFactory(
                product -> new MFXTableRowCell<>(QuotationDetail::getQuantity));

        productName.prefWidthProperty().bind(table.widthProperty().multiply(.25));
        productPrice.prefWidthProperty().bind(table.widthProperty().multiply(.25));
        productQuantity.prefWidthProperty().bind(table.widthProperty().multiply(.25));

        table
                .getTableColumns()
                .addAll(productName, productPrice, productQuantity);

        table
                .getFilters()
                .addAll(
                        new StringFilter<>("Product", QuotationDetail::getProductName),
                        new IntegerFilter<>("Quantity", QuotationDetail::getQuantity),
                        new DoubleFilter<>("Price", QuotationDetail::getProductPrice));

        getQuotationDetailTable();

        if (QuotationDetailViewModel.getQuotationDetails().isEmpty()) {
            QuotationDetailViewModel.getQuotationDetails()
                    .addListener(
                            (ListChangeListener<QuotationDetail>)
                                    change ->
                                            table.setItems(
                                                    QuotationDetailViewModel.getQuotationDetails()));
        } else {
            table
                    .itemsProperty()
                    .bindBidirectional(QuotationDetailViewModel.quotationDetailsProperty());
        }
    }

    private void getQuotationDetailTable() {
        table.setPrefSize(10000, 10000);
        table.features().enableBounceEffect();
        table.features().enableSmoothScrolling(0.5);

        table.setTableRowFactory(
                quotationDetail -> {
                    MFXTableRow<QuotationDetail> row = new MFXTableRow<>(table, quotationDetail);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<QuotationDetail>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<QuotationDetail> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(table);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            QuotationDetailViewModel.removeQuotationDetail(
                    obj.getData().getId(),
                    QuotationDetailViewModel.quotationDetailsList.indexOf(obj.getData()));
            event.consume();
        }, obj.getData().getProductName(), this));
        // Edit
        edit.setOnAction(
                event -> {
                    QuotationDetailViewModel.getQuotationDetail(obj.getData());
                    SpotyDialog.createDialog(new QuotationDetailForm(), this).showAndWait();
                    event.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    private void validateFields() {
        validateField(customer, customerValidationLabel);
        validateField(status, statusValidationLabel);
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
        return customer.validate().isEmpty() && status.validate().isEmpty() && !table.isDisabled() && !QuotationDetailViewModel.getQuotationDetails().isEmpty();
    }

    private void onSuccess() {
        this.dispose();
        AppManager.getNavigation().navigate(QuotationPage.class);
        QuotationMasterViewModel.resetProperties();
        QuotationMasterViewModel.getAllQuotationMasters(null, null);
        ProductViewModel.getAllProducts(null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint customerConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Customer is required")
                        .setCondition(customer.textProperty().length().greaterThan(0))
                        .get();
        customer.getValidator().constraint(customerConstraint);
        Constraint statusConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Color is required")
                        .setCondition(status.textProperty().length().greaterThan(0))
                        .get();
        status.getValidator().constraint(statusConstraint);
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
        customerValidationLabel = null;
        statusValidationLabel = null;
        customer = null;
        table = null;
        note = null;
        status = null;
        saveBtn = null;
        cancelBtn = null;
        addBtn = null;
    }
}
