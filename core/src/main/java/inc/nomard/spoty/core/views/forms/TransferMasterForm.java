package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.*;
import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.transfers.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.components.validatables.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.transfers.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import java.util.*;
import java.util.function.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class TransferMasterForm extends VBox {
    private final ModalPane modalPane;
    public ValidatableComboBox<Branch> fromBranch,
            toBranch;
    public ValidatableDatePicker date;
    public TableView<TransferDetail> table;
    public ValidatableTextArea note;
    public BorderPane contentPane;
    public Button addBtn,
            saveBtn,
            cancelBtn;
    public Label title,
            dateValidationLabel,
            toBranchValidationLabel,
            fromBranchValidationLabel;
    private TableColumn<TransferDetail, TransferDetail> product, quantity;

    public TransferMasterForm(ModalPane modalPane) {
        this.modalPane = modalPane;
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
                createCustomGrid(),
                buildDatePicker(),
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
        var subTitle = new Text("Stock transfer");
        subTitle.getStyleClass().add(Styles.TITLE_4);
        return buildFieldHolder(title, subTitle, buildSeparator());
    }

    private Button buildAddButton() {
        addBtn = new Button("Add");
        addBtn.setDefaultButton(true);
        addBtn.setOnAction(event -> SpotyDialog.createDialog(new TransferDetailForm(), this).showAndWait());
        addBtn.setPrefWidth(10000d);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        return addBtn;
    }

    private TableView<TransferDetail> buildTable() {
        table = new TableView<>();
        HBox.setHgrow(table, Priority.ALWAYS);
        setupTable();
        return table;
    }

    private HBox createCustomGrid() {
        var hbox = new HBox();
        hbox.getChildren().addAll(buildFromBranch(), buildToBranch());
        hbox.setSpacing(20d);
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private VBox buildFieldHolder(Node... nodes) {
        VBox vbox = new VBox();
        vbox.setSpacing(5d);
        vbox.setPadding(new Insets(5d, 0d, 0d, 0d));
        vbox.getChildren().addAll(nodes);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        return vbox;
    }

    private VBox buildFromBranch() {
        fromBranch = new ValidatableComboBox<>();
        var label = new Label("From Branch");
        fromBranch.setPrefWidth(10000d);
        fromBranch
                .valueProperty()
                .bindBidirectional(TransferMasterViewModel.fromBranchProperty());
        // ComboBox Converters.
        StringConverter<Branch> fromBranchConverter =
                FunctionalStringConverter.to(fromBranch -> (fromBranch == null) ? "" : fromBranch.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<Branch>> fromBranchFilterFunction =
                searchStr ->
                        fromBranch ->
                                StringUtils.containsIgnoreCase(fromBranchConverter.toString(fromBranch), searchStr);

        // Combo box properties.
        fromBranch.setConverter(fromBranchConverter);
        if (BranchViewModel.getBranches().isEmpty()) {
            BranchViewModel.getBranches()
                    .addListener(
                            (ListChangeListener<Branch>)
                                    c -> fromBranch.setItems(BranchViewModel.getBranches()));
        } else {
            fromBranch.itemsProperty().bindBidirectional(BranchViewModel.branchesProperty());
        }
        fromBranchValidationLabel = Validators.buildValidationLabel();
        return buildFieldHolder(label, fromBranch, fromBranchValidationLabel);
    }

    private VBox buildToBranch() {
        toBranch = new ValidatableComboBox<>();
        var label = new Label("To Branch");
        toBranch.setPrefWidth(10000d);
        toBranch
                .valueProperty()
                .bindBidirectional(TransferMasterViewModel.toBranchProperty());
        // ComboBox Converters.
        StringConverter<Branch> toBranchConverter =
                FunctionalStringConverter.to(toBranch -> (toBranch == null) ? "" : toBranch.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<Branch>> toBranchFilterFunction =
                searchStr ->
                        toBranch ->
                                StringUtils.containsIgnoreCase(toBranchConverter.toString(toBranch), searchStr);

        // Combo box properties.
        toBranch.setConverter(toBranchConverter);
        if (BranchViewModel.getBranches().isEmpty()) {
            BranchViewModel.getBranches()
                    .addListener(
                            (ListChangeListener<Branch>)
                                    c -> toBranch.setItems(BranchViewModel.getBranches()));
        } else {
            toBranch.itemsProperty().bindBidirectional(BranchViewModel.branchesProperty());
        }
        toBranchValidationLabel = Validators.buildValidationLabel();
        return buildFieldHolder(label, toBranch, toBranchValidationLabel);
    }

    private VBox buildDatePicker() {
        date = new ValidatableDatePicker();
        var label = new Label("Date");
        date.setPrefWidth(10000d);
        date.valueProperty()
                .bindBidirectional(TransferMasterViewModel.dateProperty());
        dateValidationLabel = Validators.buildValidationLabel();
        return buildFieldHolder(label, date, dateValidationLabel);
    }

    private VBox buildNote() {
        var label = new Label("Note");
        note = new ValidatableTextArea();
        note.setMinHeight(100d);
        note.textProperty().bindBidirectional(TransferMasterViewModel.noteProperty());
        return buildFieldHolder(label, note);
    }

    private Button buildSaveButton() {
        saveBtn = new Button("Save");
        saveBtn.setDefaultButton(true);
        saveBtn.setOnAction(event -> {
            if (!table.isDisabled() && TransferDetailViewModel.getTransferDetails().isEmpty()) {
                errorMessage("Table can't be Empty");
            }
            validateFields();

            if (isValidForm()) {
                if (TransferMasterViewModel.getId() > 0) {
                    TransferMasterViewModel.updateTransfer(this::onSuccess, this::successMessage, this::errorMessage);
                } else {
                    TransferMasterViewModel.saveTransferMaster(this::onSuccess, this::successMessage, this::errorMessage);
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
        quantity = new TableColumn<>("Quantity");
        product.prefWidthProperty().bind(table.widthProperty().multiply(.7));
        quantity.prefWidthProperty().bind(table.widthProperty().multiply(.3));

        table.getColumns().addAll(product, quantity);
        table.setItems(TransferDetailViewModel.getTransferDetails());
        setupTableColumns();
        getTransferDetailTable();
    }

    private void getTransferDetailTable() {
        table.setPrefSize(10000d, 10000d);

        table.setRowFactory(
                transferDetail -> {
                    TableRow<TransferDetail> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<TransferDetail>) event.getSource())
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

    private ContextMenu showContextMenu(TableRow<TransferDetail> obj) {
        var contextMenu = new ContextMenu();
        var delete = new MenuItem("Delete");
        var edit = new MenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            SpotyThreader.spotyThreadPool(
                    () ->
                            TransferDetailViewModel.removeTransferDetail(
                                    obj.getItem().getId(),
                                    TransferDetailViewModel.transferDetailsList.indexOf(obj.getItem())));
            event.consume();
        }, obj.getItem().getProductName(), contentPane));
        // Edit
        edit.setOnAction(
                event -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    TransferDetailViewModel.getTransferDetail(obj.getItem());
                                } catch (Exception e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            });

                    SpotyDialog.createDialog(new TransferDetailForm(), contentPane).showAndWait();
                    event.consume();
                });

        contextMenu.getItems().addAll(edit, delete);

        return contextMenu;
    }

    private void validateFields() {
        validateField(fromBranch, fromBranchValidationLabel);
        validateField(toBranch, toBranchValidationLabel);
        validateField(date, dateValidationLabel);
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
        return fromBranch.validate().isEmpty() && toBranch.validate().isEmpty() && date.validate().isEmpty() && !table.isDisabled() && !TransferDetailViewModel.getTransferDetails().isEmpty();
    }

    private void onSuccess() {
        TransferMasterViewModel.getAllTransferMasters(null, null);
        ProductViewModel.getAllProducts(null, null);
        this.dispose();
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint dateConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Date is required")
                        .setCondition(date.valueProperty().isNotNull())
                        .get();
        date.getValidator().constraint(dateConstraint);
        Constraint fromBranchConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("From Branch is required")
                        .setCondition(fromBranch.valueProperty().isNotNull())
                        .get();
        fromBranch.getValidator().constraint(fromBranchConstraint);
        Constraint toBranchConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("To Branch is required")
                        .setCondition(toBranch.valueProperty().isNotNull())
                        .get();
        toBranch.getValidator().constraint(toBranchConstraint);
        // Display error.
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
        fromBranch
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                fromBranchValidationLabel.setManaged(false);
                                fromBranchValidationLabel.setVisible(false);
                                fromBranch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        toBranch
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                toBranchValidationLabel.setManaged(false);
                                toBranchValidationLabel.setVisible(false);
                                toBranch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
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

    private void setupTableColumns() {
        product.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        product.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(TransferDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getProductName());
            }
        });
        quantity.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        quantity.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(TransferDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getQuantity()));
            }
        });
    }

    public void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
        TransferMasterViewModel.resetProperties();
        fromBranchValidationLabel = null;
        toBranchValidationLabel = null;
        dateValidationLabel = null;
        fromBranch = null;
        toBranch = null;
        table = null;
        note = null;
        date = null;
        saveBtn = null;
        cancelBtn = null;
        addBtn = null;
    }
}
