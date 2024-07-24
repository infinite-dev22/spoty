package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.quotations.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.components.label_components.controls.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.core.views.pages.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.quotations.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
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
    public LabeledComboBox<Customer> customer;
    @FXML
    public TableView<QuotationDetail> table;
    @FXML
    public LabeledTextField note;
    @FXML
    public LabeledComboBox<String> status;
    @FXML
    public Label customerValidationLabel,
            statusValidationLabel;
    @FXML
    public Button saveBtn,
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

    private Button buildAddButton() {
        addBtn = new Button("Add");
        addBtn.setDefaultButton(true);
        addBtn.setOnAction(event -> SpotyDialog.createDialog(new QuotationDetailForm(), this).showAndWait());
        addBtn.setPrefWidth(10000d);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        return addBtn;
    }

    private TableView<QuotationDetail> buildTable() {
        table = new TableView<>();
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

    private VBox buildFieldHolder(Node... nodes) {
        VBox vbox = new VBox();
        vbox.setSpacing(5d);
        vbox.setPadding(new Insets(5d, 0d, 0d, 0d));
        vbox.getChildren().addAll(nodes);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        return vbox;
    }

    private VBox buildCustomer() {
        customer = new LabeledComboBox<>();
        customer.setLabel("Supplier");
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
        if (CustomerViewModel.getCustomers().isEmpty()) {
            CustomerViewModel.getCustomers()
                    .addListener(
                            (ListChangeListener<Customer>)
                                    c -> customer.setItems(CustomerViewModel.getCustomers()));
        } else {
            customer.itemsProperty().bindBidirectional(CustomerViewModel.customersProperty());
        }
        customerValidationLabel = Validators.buildValidationLabel();
        return buildFieldHolder(customer, customerValidationLabel);
    }

    private VBox buildStatus() {
        status = new LabeledComboBox<>();
        status.setLabel("Quotation Status");
        status.setPrefWidth(10000d);
        status
                .valueProperty()
                .bindBidirectional(QuotationMasterViewModel.statusProperty());
        status.setItems(FXCollections.observableArrayList(Values.QUOTATION_TYPE));
        statusValidationLabel = Validators.buildValidationLabel();
        return buildFieldHolder(status, statusValidationLabel);
    }

    private VBox buildNote() {
        note = new LabeledTextField();
        note.setLabel("Note");
        note.setPrefWidth(10000d);
        note.textProperty().bindBidirectional(QuotationMasterViewModel.noteProperty());
        return buildFieldHolder(note);
    }

    private Button buildSaveButton() {
        saveBtn = new Button("Save");
        saveBtn.setDefaultButton(true);
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

    private Button buildCancelButton() {
        cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
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
        TableColumn<QuotationDetail, String> productName = new TableColumn<>("Product");
        TableColumn<QuotationDetail, String> productPrice = new TableColumn<>("Price");
        TableColumn<QuotationDetail, String> productQuantity = new TableColumn<>("Quantity");

        productName.prefWidthProperty().bind(table.widthProperty().multiply(.25));
        productPrice.prefWidthProperty().bind(table.widthProperty().multiply(.25));
        productQuantity.prefWidthProperty().bind(table.widthProperty().multiply(.25));

        table.getColumns().addAll(productName, productPrice, productQuantity);
        getQuotationDetailTable();
        table.setItems(QuotationDetailViewModel.getQuotationDetails());
    }

    private void getQuotationDetailTable() {
        table.setPrefSize(10000, 10000);

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
        ContextMenu contextMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        MenuItem edit = new MenuItem("Edit");

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
                    SpotyDialog.createDialog(new QuotationDetailForm(), this).showAndWait();
                    event.consume();
                });

        contextMenu.getItems().addAll(edit, delete);

        return contextMenu;
    }

    private void validateFields() {
        validateField(customer, customerValidationLabel);
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
                        .setCondition(customer.valueProperty().isNotNull())
                        .get();
        customer.getValidator().constraint(customerConstraint);
        Constraint statusConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Color is required")
                        .setCondition(status.valueProperty().isNotNull())
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
