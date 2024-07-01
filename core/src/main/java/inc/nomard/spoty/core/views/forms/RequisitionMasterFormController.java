package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.requisitions.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.dtos.requisitions.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class RequisitionMasterFormController implements Initializable {
    private static RequisitionMasterFormController instance;
    private final Stage stage;
    @FXML
    public Label title,
            supplierValidationLabel,
            statusValidationLabel;
    @FXML
    public MFXFilterComboBox<Supplier> supplier;
    @FXML
    public MFXTableView<RequisitionDetail> detailTable;
    @FXML
    public MFXTextField note;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXFilterComboBox<String> status;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    private MFXStageDialog dialog;

    private RequisitionMasterFormController(Stage stage) {
        this.stage = stage;
        Platform.runLater(
                () -> {
                    try {
                        createRequisitionProductDialog(stage);
                    } catch (IOException e) {
                        SpotyLogger.writeToFile(e, this.getClass());
                    }
                });
    }

    public static RequisitionMasterFormController getInstance(Stage stage) {
        if (instance == null) {
            synchronized (RequisitionMasterFormController.class) {
                instance = new RequisitionMasterFormController(stage);
            }
        }
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bi~Directional Binding.
        supplier.valueProperty().bindBidirectional(RequisitionMasterViewModel.supplierProperty());
        note.textProperty().bindBidirectional(RequisitionMasterViewModel.noteProperty());

        // ComboBox Converters.
        StringConverter<Supplier> supplierConverter =
                FunctionalStringConverter.to(supplier -> (supplier == null) ? "" : supplier.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<Supplier>> supplierFilterFunction =
                searchStr ->
                        supplier ->
                                StringUtils.containsIgnoreCase(supplierConverter.toString(supplier), searchStr);

        // Set items to combo boxes and display custom text.
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

        status.setItems(FXCollections.observableArrayList(Values.PURCHASE_STATUSES));

        // input validators.
        requiredValidator();

        setupTable();
    }

    private void createRequisitionProductDialog(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/RequisitionDetailForm.fxml");
        fxmlLoader.setControllerFactory(c -> RequisitionDetailFormController.getInstance(stage));

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setShowClose(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(contentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void saveBtnClicked() {

        if (!detailTable.isDisabled() && RequisitionDetailViewModel.requisitionDetailsList.isEmpty()) {
            errorMessage("Table can't be Empty");
        }
        List<Constraint> supplierConstraints = supplier.validate();
        List<Constraint> statusConstraints = status.validate();
        if (!supplierConstraints.isEmpty()) {
            supplierValidationLabel.setManaged(true);
            supplierValidationLabel.setVisible(true);
            supplierValidationLabel.setText(supplierConstraints.getFirst().getMessage());
            supplier.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (!statusConstraints.isEmpty()) {
            statusValidationLabel.setManaged(true);
            statusValidationLabel.setVisible(true);
            statusValidationLabel.setText(statusConstraints.getFirst().getMessage());
            status.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (supplierConstraints.isEmpty()
                && statusConstraints.isEmpty()) {
            if (RequisitionMasterViewModel.getId() > 0) {
                RequisitionMasterViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                return;
            }
            RequisitionMasterViewModel.saveRequisitionMaster(this::onSuccess, this::successMessage, this::errorMessage);
        }
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
        product.prefWidthProperty().bind(detailTable.widthProperty().multiply(.25));
        quantity.prefWidthProperty().bind(detailTable.widthProperty().multiply(.25));

        // Set table filter.
        detailTable
                .getTableColumns()
                .addAll(product, quantity);

        detailTable
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
                                    change -> detailTable.setItems(RequisitionDetailViewModel.getRequisitionDetails()));
        } else {
            detailTable
                    .itemsProperty()
                    .bindBidirectional(RequisitionDetailViewModel.requisitionDetailsProperty());
        }
    }

    private void styleTable() {
        detailTable.setPrefSize(1000, 1000);
        detailTable.features().enableBounceEffect();
        detailTable.features().enableSmoothScrolling(0.5);

        detailTable.setTableRowFactory(
                t -> {
                    MFXTableRow<RequisitionDetail> row = new MFXTableRow<>(detailTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<RequisitionDetail>) event.getSource())
                                        .show(
                                                detailTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<RequisitionDetail> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(detailTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            RequisitionDetailViewModel.removeRequisitionDetail(
                    obj.getData().getId(),
                    RequisitionDetailViewModel.requisitionDetailsList.indexOf(obj.getData()));
            event.consume();
        }, obj.getData().getProductName(), stage, contentPane));
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
                    dialog.showAndWait();
                    event.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    public void cancelBtnClicked() {
        BaseController.navigation.navigate(new RequisitionPage(stage));
        RequisitionMasterViewModel.resetProperties();
        supplierValidationLabel.setVisible(false);
        statusValidationLabel.setVisible(false);
        supplierValidationLabel.setManaged(false);
        statusValidationLabel.setManaged(false);
        supplier.clearSelection();
        status.clearSelection();
        supplier.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        status.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        supplier.clearSelection();
        status.clearSelection();
    }

    public void addBtnClicked() {
        dialog.showAndWait();
    }

    private void onSuccess() {
        cancelBtnClicked();
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
        Constraint statusConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Status is required")
                        .setCondition(status.textProperty().length().greaterThan(0))
                        .get();
        status.getValidator().constraint(statusConstraint);
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
        if (!BaseController.getInstance(stage).morphPane.getChildren().contains(notification)) {
            BaseController.getInstance(stage).morphPane.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification, stage));
        }
    }
}
