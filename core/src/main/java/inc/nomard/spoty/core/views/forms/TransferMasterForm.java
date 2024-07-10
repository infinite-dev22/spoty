package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.transfers.*;
import inc.nomard.spoty.core.views.pages.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.transfers.*;
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
public class TransferMasterForm extends OutlineFormPage {
    @FXML
    public MFXFilterComboBox<Branch> fromBranch,
            toBranch;
    @FXML
    public MFXDatePicker date;
    @FXML
    public MFXTableView<TransferDetail> table;
    @FXML
    public MFXTextField note;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXButton addBtn,
            saveBtn,
            cancelBtn;
    @FXML
    public Label title,
            dateValidationLabel,
            toBranchValidationLabel,
            fromBranchValidationLabel;

    public TransferMasterForm() {
        addNode(init());
        initializeComponentProperties();
    }

    public void initializeComponentProperties() {
        requiredValidator();
    }

    private BorderPane init() {
        Label purchaseFormTitle = new Label("Transfer Form");
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
        addBtn.setOnAction(event -> SpotyDialog.createDialog(new TransferDetailForm(), this).showAndWait());
        addBtn.setPrefWidth(10000d);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        return addBtn;
    }

    private MFXTableView<TransferDetail> buildTable() {
        table = new MFXTableView<>();
        HBox.setHgrow(table, Priority.ALWAYS);
        setupTable();
        return table;
    }

    private VBox createCustomGrid() {
        var hbox1 = new HBox();
        hbox1.getChildren().addAll(buildFromBranch(), buildToBranch());
        hbox1.setSpacing(20d);
        HBox.setHgrow(hbox1, Priority.ALWAYS);
        var hbox2 = new HBox();
        hbox2.getChildren().addAll(buildDatePicker(), buildNote());
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

    private VBox buildFromBranch() {
        fromBranch = new MFXFilterComboBox<>();
        fromBranch.setFloatMode(FloatMode.BORDER);
        fromBranch.setFloatingText("From Branch");
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
        fromBranch.setFilterFunction(fromBranchFilterFunction);
        if (BranchViewModel.getBranches().isEmpty()) {
            BranchViewModel.getBranches()
                    .addListener(
                            (ListChangeListener<Branch>)
                                    c -> fromBranch.setItems(BranchViewModel.getBranches()));
        } else {
            fromBranch.itemsProperty().bindBidirectional(BranchViewModel.branchesProperty());
        }
        fromBranchValidationLabel = buildValidationLabel();
        return buildFieldHolder(fromBranch, fromBranchValidationLabel);
    }

    private VBox buildToBranch() {
        toBranch = new MFXFilterComboBox<>();
        toBranch.setFloatMode(FloatMode.BORDER);
        toBranch.setFloatingText("To Branch");
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
        toBranch.setFilterFunction(toBranchFilterFunction);
        if (BranchViewModel.getBranches().isEmpty()) {
            BranchViewModel.getBranches()
                    .addListener(
                            (ListChangeListener<Branch>)
                                    c -> toBranch.setItems(BranchViewModel.getBranches()));
        } else {
            toBranch.itemsProperty().bindBidirectional(BranchViewModel.branchesProperty());
        }
        toBranchValidationLabel = buildValidationLabel();
        return buildFieldHolder(toBranch, toBranchValidationLabel);
    }

    private VBox buildDatePicker() {
        date = new MFXDatePicker();
        date.setFloatMode(FloatMode.BORDER);
        date.setFloatingText("Date");
        date.setPrefWidth(10000d);
        date.textProperty()
                .bindBidirectional(TransferMasterViewModel.dateProperty());
        dateValidationLabel = buildValidationLabel();
        return buildFieldHolder(date, dateValidationLabel);
    }

    private VBox buildNote() {
        note = new MFXTextField();
        note.setFloatMode(FloatMode.BORDER);
        note.setFloatingText("Note");
        note.setPrefWidth(10000d);
        note.textProperty().bindBidirectional(TransferMasterViewModel.noteProperty());
        return buildFieldHolder(note);
    }

    private MFXButton buildSaveButton() {
        saveBtn = new MFXButton("Save");
        saveBtn.getStyleClass().add("filled");
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

    private MFXButton buildCancelButton() {
        cancelBtn = new MFXButton("Cancel");
        cancelBtn.getStyleClass().add("outlined");
        cancelBtn.setOnAction(event -> {
            AppManager.getNavigation().navigate(TransferPage.class);
            TransferMasterViewModel.resetProperties();
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
        MFXTableColumn<TransferDetail> productName =
                new MFXTableColumn<>(
                        "Product", false, Comparator.comparing(TransferDetail::getProductName));
        MFXTableColumn<TransferDetail> stock =
                new MFXTableColumn<>(
                        "Stock", false, Comparator.comparing(TransferDetail::getProductQuantity));
        MFXTableColumn<TransferDetail> productQuantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(TransferDetail::getQuantity));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(TransferDetail::getProductName));
        productQuantity.setRowCellFactory(
                product -> new MFXTableRowCell<>(TransferDetail::getQuantity));
        stock.setRowCellFactory(
                product -> new MFXTableRowCell<>(TransferDetail::getProductQuantity));

        productName.prefWidthProperty().bind(table.widthProperty().multiply(.5));
        productQuantity.prefWidthProperty().bind(table.widthProperty().multiply(.5));
        stock.prefWidthProperty().bind(table.widthProperty().multiply(.5));

        table.getTableColumns().addAll(productName, productQuantity, stock);

        table
                .getFilters()
                .addAll(new StringFilter<>("Name", TransferDetail::getProductName));

        getTransferDetailTable();

        if (TransferDetailViewModel.getTransferDetails().isEmpty()) {
            TransferDetailViewModel.getTransferDetails()
                    .addListener(
                            (ListChangeListener<TransferDetail>)
                                    change -> table.setItems(TransferDetailViewModel.getTransferDetails()));
        } else {
            table
                    .itemsProperty()
                    .bindBidirectional(TransferDetailViewModel.transferDetailsProperty());
        }
    }

    private void getTransferDetailTable() {
        table.setPrefSize(10000d, 10000d);
        table.features().enableBounceEffect();
        table.features().enableSmoothScrolling(0.5);

        table.setTableRowFactory(
                transferDetail -> {
                    MFXTableRow<TransferDetail> row = new MFXTableRow<>(table, transferDetail);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<TransferDetail>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<TransferDetail> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(table);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            SpotyThreader.spotyThreadPool(
                    () ->
                            TransferDetailViewModel.removeTransferDetail(
                                    obj.getData().getId(),
                                    TransferDetailViewModel.transferDetailsList.indexOf(obj.getData())));
            event.consume();
        }, obj.getData().getProductName(), contentPane));
        // Edit
        edit.setOnAction(
                event -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    TransferDetailViewModel.getTransferDetail(obj.getData());
                                } catch (Exception e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            });

                    SpotyDialog.createDialog(new TransferDetailForm(), contentPane).showAndWait();
                    event.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    private void validateFields() {
        validateField(fromBranch, fromBranchValidationLabel);
        validateField(toBranch, toBranchValidationLabel);
        validateField(date, dateValidationLabel);
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
        return fromBranch.validate().isEmpty() && toBranch.validate().isEmpty() && date.validate().isEmpty() && !table.isDisabled() && !TransferDetailViewModel.getTransferDetails().isEmpty();
    }

    private void onSuccess() {
        AppManager.getNavigation().navigate(TransferPage.class);
        TransferMasterViewModel.resetProperties();
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
                        .setCondition(date.textProperty().length().greaterThan(0))
                        .get();
        date.getValidator().constraint(dateConstraint);
        Constraint fromBranchConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("From Branch is required")
                        .setCondition(fromBranch.textProperty().length().greaterThan(0))
                        .get();
        fromBranch.getValidator().constraint(fromBranchConstraint);
        Constraint toBranchConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("To Branch is required")
                        .setCondition(toBranch.textProperty().length().greaterThan(0))
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

    @Override
    public void dispose() {
        super.dispose();
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
