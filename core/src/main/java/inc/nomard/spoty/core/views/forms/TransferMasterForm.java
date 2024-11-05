package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.util.validation.Constraint;
import inc.nomard.spoty.core.util.validation.Severity;
import inc.nomard.spoty.core.viewModels.BranchViewModel;
import inc.nomard.spoty.core.viewModels.transfers.TransferDetailViewModel;
import inc.nomard.spoty.core.viewModels.transfers.TransferMasterViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.components.validatables.ValidatableComboBox;
import inc.nomard.spoty.core.views.components.validatables.ValidatableDatePicker;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextArea;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.util.FunctionalStringConverter;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.core.views.util.Validators;
import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.transfers.TransferDetail;
import inc.nomard.spoty.utils.AppUtils;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
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
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static inc.nomard.spoty.core.util.validation.Validated.INVALID_PSEUDO_CLASS;

@SuppressWarnings("unchecked")
@Slf4j
public class TransferMasterForm extends VBox {
    private final ModalPane modalPane1;
    private final ModalPane modalPane2;
    public ValidatableComboBox<Branch> fromBranch,
            toBranch;
    public ValidatableDatePicker date;
    public TableView<TransferDetail> table;
    public ValidatableTextArea note;
    public CustomButton saveBtn;
    public Button addBtn,
            cancelBtn;
    public Label title,
            dateValidationLabel,
            toBranchValidationLabel,
            fromBranchValidationLabel;
    private TableColumn<TransferDetail, TransferDetail> product, quantity;

    public TransferMasterForm(ModalPane modalPane1, ModalPane modalPane2) {
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
        addBtn.setOnAction(_ -> showForm());
        addBtn.setPrefWidth(10000d);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        return addBtn;
    }

    private void showForm() {
        var dialog = new ModalContentHolder(450, 250);
        dialog.getChildren().add(new TransferDetailForm(modalPane2));
        dialog.setPadding(new Insets(5d));
        modalPane2.setAlignment(Pos.CENTER_RIGHT);
        modalPane2.show(dialog);
        modalPane2.setPersistent(true);
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
        StringConverter<Branch> fromBranchConverter =
                FunctionalStringConverter.to(fromBranch -> (fromBranch == null) ? "" : fromBranch.getName());
        Function<String, Predicate<Branch>> fromBranchFilterFunction =
                searchStr ->
                        fromBranch ->
                                fromBranchConverter.toString(fromBranch).toLowerCase().contains(searchStr);
        fromBranch.setConverter(fromBranchConverter);
        if (BranchViewModel.getBranches().isEmpty()) {
            BranchViewModel.getBranches()
                    .addListener(
                            (ListChangeListener<Branch>)
                                    _ -> fromBranch.setItems(BranchViewModel.getBranches()));
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
        StringConverter<Branch> toBranchConverter =
                FunctionalStringConverter.to(toBranch -> (toBranch == null) ? "" : toBranch.getName());
        Function<String, Predicate<Branch>> toBranchFilterFunction =
                searchStr ->
                        toBranch ->
                                toBranchConverter.toString(toBranch).toLowerCase().contains(searchStr);
        toBranch.setConverter(toBranchConverter);
        if (BranchViewModel.getBranches().isEmpty()) {
            BranchViewModel.getBranches()
                    .addListener(
                            (ListChangeListener<Branch>)
                                    _ -> toBranch.setItems(BranchViewModel.getBranches()));
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

    private CustomButton buildSaveButton() {
        saveBtn = new CustomButton("Save");
        saveBtn.getStyleClass().add(Styles.ACCENT);
        saveBtn.setOnAction(_ -> {
            if (!table.isDisabled() && TransferDetailViewModel.getTransferDetails().isEmpty()) {
                errorMessage("Table can't be Empty");
            }
            validateFields();

            if (isValidForm()) {
                saveBtn.startLoading();
                if (TransferMasterViewModel.getId() > 0) {
                    TransferMasterViewModel.updateTransfer(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                } else {
                    TransferMasterViewModel.saveTransferMaster(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                }
            }
        });
        return saveBtn;
    }

    private Button buildCancelButton() {
        cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
        cancelBtn.setOnAction(_ -> this.dispose());
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
                _ -> new TableRow<>() {
                    @Override
                    public void updateItem(TransferDetail item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setStyle("");
                        } else {
                            EventHandler<ContextMenuEvent> eventHandler =
                                    event -> {
                                        showContextMenu((TableRow<TransferDetail>) event.getSource())
                                                .show(
                                                        table.getScene().getWindow(),
                                                        event.getScreenX(),
                                                        event.getScreenY());
                                        event.consume();
                                    };
                            setOnContextMenuRequested(eventHandler);
                        }
                    }
                });
    }

    private ContextMenu showContextMenu(TableRow<TransferDetail> obj) {
        var contextMenu = new ContextMenu();
        var delete = new MenuItem("Delete");
        var edit = new MenuItem("Edit");

        delete.setOnAction(event -> new DeleteConfirmationDialog(AppManager.getGlobalModalPane(), () -> {
            SpotyThreader.spotyThreadPool(
                    () ->
                            TransferDetailViewModel.removeTransferDetail(
                                    obj.getItem().getId(),
                                    TransferDetailViewModel.transferDetailsList.indexOf(obj.getItem())));
            event.consume();
        }, obj.getItem().getProductName()).showDialog());
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
                    this.showForm();
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
        this.dispose();
        TransferMasterViewModel.getAllTransferMasters(null, null, null, null);
    }

    public void requiredValidator() {
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
        date
                .getValidator()
                .validProperty()
                .addListener(
                        (_, _, newValue) -> {
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
                        (_, _, newValue) -> {
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
                        (_, _, newValue) -> {
                            if (newValue) {
                                toBranchValidationLabel.setManaged(false);
                                toBranchValidationLabel.setVisible(false);
                                toBranch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        saveBtn.stopLoading();
    }

    private void setupTableColumns() {
        product.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        product.setCellFactory(_ -> new TableCell<>() {
            @Override
            public void updateItem(TransferDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getProductName());
            }
        });
        quantity.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        quantity.setCellFactory(_ -> new TableCell<>() {
            @Override
            public void updateItem(TransferDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getQuantity()));
            }
        });
    }

    public void dispose() {
        modalPane1.hide(true);
        modalPane1.setPersistent(false);
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
