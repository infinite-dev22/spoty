package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.requisitions.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.dtos.requisitions.*;
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
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class RequisitionMasterForm extends OutlineFormPage {
    public Label supplierValidationLabel;
    public MFXFilterComboBox<Supplier> supplier;
    public MFXTableView<RequisitionDetail> table;
    public MFXTextField note;
    public MFXButton saveBtn,
            cancelBtn,
            addBtn;

    public RequisitionMasterForm() {
        addNode(init());
        initializeComponentProperties();
    }

    public void initializeComponentProperties() {
        requiredValidator();
    }

    private BorderPane init() {
        Label purchaseFormTitle = new Label("Requisition Form");
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
        addBtn.setOnAction(event -> SpotyDialog.createDialog(new RequisitionDetailForm(), this).showAndWait());
        addBtn.setPrefWidth(10000d);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        return addBtn;
    }

    private MFXTableView<RequisitionDetail> buildTable() {
        table = new MFXTableView<>();
        HBox.setHgrow(table, Priority.ALWAYS);
        setupTable();
        return table;
    }

    private VBox createCustomGrid() {
        var hbox = new HBox();
        hbox.getChildren().addAll(buildSupplier(), buildNote());
        hbox.setSpacing(20d);
        HBox.setHgrow(hbox, Priority.ALWAYS);
        var vbox = new VBox();
        vbox.setSpacing(10d);
        vbox.getChildren().add(hbox);
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

    private VBox buildSupplier() {
        supplier = new MFXFilterComboBox<>();
        supplier.setFloatMode(FloatMode.BORDER);
        supplier.setFloatingText("Supplier");
        supplier.setPrefWidth(10000d);
        supplier
                .valueProperty()
                .bindBidirectional(RequisitionMasterViewModel.supplierProperty());
        // ComboBox Converters.
        StringConverter<Supplier> supplierConverter =
                FunctionalStringConverter.to(supplier -> (supplier == null) ? "" : supplier.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<Supplier>> supplierFilterFunction =
                searchStr ->
                        supplier ->
                                StringUtils.containsIgnoreCase(supplierConverter.toString(supplier), searchStr);

        // Combo box properties.
        supplier.setConverter(supplierConverter);
        supplier.setFilterFunction(supplierFilterFunction);
        if (SupplierViewModel.getSuppliers().isEmpty()) {
            SupplierViewModel.getSuppliers()
                    .addListener(
                            (ListChangeListener<Supplier>)
                                    c -> supplier.setItems(SupplierViewModel.getSuppliers()));
        } else {
            supplier.itemsProperty().bindBidirectional(SupplierViewModel.suppliersProperty());
        }
        supplierValidationLabel = buildValidationLabel();
        return buildFieldHolder(supplier, supplierValidationLabel);
    }

    private VBox buildNote() {
        note = new MFXTextField();
        note.setFloatMode(FloatMode.BORDER);
        note.setFloatingText("Note");
        note.setPrefWidth(10000d);
        note.textProperty().bindBidirectional(RequisitionMasterViewModel.noteProperty());
        return buildFieldHolder(note);
    }

    private MFXButton buildSaveButton() {
        saveBtn = new MFXButton("Save");
        saveBtn.getStyleClass().add("filled");
        saveBtn.setOnAction(event -> {
            if (!table.isDisabled() && RequisitionDetailViewModel.getRequisitionDetails().isEmpty()) {
                errorMessage("Table can't be Empty");
            }
            validateFields();

            if (isValidForm()) {
                if (RequisitionMasterViewModel.getId() > 0) {
                    RequisitionMasterViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                } else {
                    RequisitionMasterViewModel.saveRequisitionMaster(this::onSuccess, this::successMessage, this::errorMessage);
                }
            }
        });
        return saveBtn;
    }

    private MFXButton buildCancelButton() {
        cancelBtn = new MFXButton("Cancel");
        cancelBtn.getStyleClass().add("outlined");
        cancelBtn.setOnAction(event -> {
            AppManager.getNavigation().navigate(RequisitionPage.class);
            RequisitionMasterViewModel.resetProperties();
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
        // Set table column titles.
        MFXTableColumn<RequisitionDetail> product =
                new MFXTableColumn<>(
                        "Product", false, Comparator.comparing(RequisitionDetail::getProductName));
        MFXTableColumn<RequisitionDetail> quantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(RequisitionDetail::getQuantity));

        // Set table column data.
        product.setRowCellFactory(
                requisitionDetail -> new MFXTableRowCell<>(RequisitionDetail::getProductName));
        quantity.setRowCellFactory(
                requisitionDetail -> new MFXTableRowCell<>(RequisitionDetail::getQuantity));

        // Set table column width.
        product.prefWidthProperty().bind(table.widthProperty().multiply(.25));
        quantity.prefWidthProperty().bind(table.widthProperty().multiply(.25));

        // Set table filter.
        table
                .getTableColumns()
                .addAll(product, quantity);

        table
                .getFilters()
                .addAll(
                        new StringFilter<>("Product", RequisitionDetail::getProductName),
                        new IntegerFilter<>("Quantity", RequisitionDetail::getQuantity));

        styleTable();

        // Populate table.
        if (RequisitionDetailViewModel.getRequisitionDetails().isEmpty()) {
            RequisitionDetailViewModel.getRequisitionDetails()
                    .addListener(
                            (ListChangeListener<RequisitionDetail>)
                                    change -> table.setItems(RequisitionDetailViewModel.getRequisitionDetails()));
        } else {
            table
                    .itemsProperty()
                    .bindBidirectional(RequisitionDetailViewModel.requisitionDetailsProperty());
        }
    }

    private void styleTable() {
        table.setPrefSize(10000d, 10000d);
        table.features().enableBounceEffect();
        table.features().enableSmoothScrolling(0.5);

        table.setTableRowFactory(
                t -> {
                    MFXTableRow<RequisitionDetail> row = new MFXTableRow<>(table, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<RequisitionDetail>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<RequisitionDetail> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(table);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            RequisitionDetailViewModel.removeRequisitionDetail(
                    obj.getData().getId(),
                    RequisitionDetailViewModel.requisitionDetailsList.indexOf(obj.getData()));
            event.consume();
        }, obj.getData().getProductName(), this));
        // Edit
        edit.setOnAction(
                event -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            RequisitionDetailViewModel.getRequisitionDetail(obj.getData().getId());
                        } catch (Exception e) {
                            SpotyLogger.writeToFile(e, this.getClass());
                        }
                    });
                    SpotyDialog.createDialog(new RequisitionDetailForm(), this).showAndWait();
                    event.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    public void addBtnClicked() {
        SpotyDialog.createDialog(new RequisitionDetailForm(), this).showAndWait();
    }

    private void validateFields() {
        validateField(supplier, supplierValidationLabel);
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
        return supplier.validate().isEmpty() && !table.isDisabled() && !RequisitionDetailViewModel.getRequisitionDetails().isEmpty();
    }

    private void onSuccess() {
        this.dispose();
        AppManager.getNavigation().navigate(RequisitionPage.class);
        RequisitionMasterViewModel.resetProperties();
        RequisitionMasterViewModel.getAllRequisitionMasters(null, null);
        ProductViewModel.getAllProducts(null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint supplierConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Supplier is required")
                        .setCondition(supplier.textProperty().length().greaterThan(0))
                        .get();
        supplier.getValidator().constraint(supplierConstraint);
        // Display error.
        supplier
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                supplierValidationLabel.setManaged(false);
                                supplierValidationLabel.setVisible(false);
                                supplier.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
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
        supplier = null;
        table = null;
        note = null;
        saveBtn = null;
        cancelBtn = null;
        addBtn = null;
    }
}
