package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.adjustments.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.dtos.adjustments.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.util.*;
import java.util.function.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class AdjustmentMasterForm extends OutlineFormPage {
    public MFXTableView<AdjustmentDetail> adjustmentDetailTable;
    public MFXTextField adjustmentNote;
    public Label adjustmentFormTitle;
    public MFXButton adjustmentProductAddBtn, saveBtn, cancelBtn;

    public AdjustmentMasterForm() {
        addNode(initialize());
        bindProperties();
        setupAddProductButton();
        setupTable();
    }

    private BorderPane initialize() {
        adjustmentFormTitle = new Label("Adjustment Form");
        adjustmentFormTitle.setId("adjustmentFormTitle");

        Separator separator = new Separator();
        separator.setPrefWidth(200.0);

        GridPane gridPane = createGridPane();

        adjustmentProductAddBtn = new MFXButton("Add Product");
        adjustmentProductAddBtn.getStyleClass().add("filled");

        adjustmentDetailTable = new MFXTableView<>();

        adjustmentNote = new MFXTextField();
        adjustmentNote.setFloatMode(FloatMode.BORDER);
        adjustmentNote.setFloatingText("Note");

        HBox buttonBox = createButtonBox();

        AnchorPane centerPane = new AnchorPane();
        centerPane.getStyleClass().add("card-flat");
        centerPane.setPadding(new Insets(5.0));

        AnchorPane.setTopAnchor(adjustmentFormTitle, 0.0);
        AnchorPane.setLeftAnchor(adjustmentFormTitle, 0.0);
        AnchorPane.setRightAnchor(adjustmentFormTitle, 0.0);

        AnchorPane.setTopAnchor(separator, 20.0);
        AnchorPane.setLeftAnchor(separator, 0.0);
        AnchorPane.setRightAnchor(separator, 0.0);

        AnchorPane.setTopAnchor(gridPane, 40.0);
        AnchorPane.setLeftAnchor(gridPane, 0.0);
        AnchorPane.setRightAnchor(gridPane, 180.0);

        AnchorPane.setTopAnchor(adjustmentNote, 40.0);
        AnchorPane.setLeftAnchor(adjustmentNote, 0.0);
        AnchorPane.setRightAnchor(adjustmentNote, 0.0);

        AnchorPane.setTopAnchor(adjustmentProductAddBtn, 100.0);
        AnchorPane.setLeftAnchor(adjustmentProductAddBtn, 0.0);
        AnchorPane.setRightAnchor(adjustmentProductAddBtn, 0.0);

        AnchorPane.setTopAnchor(adjustmentDetailTable, 160.0);
        AnchorPane.setBottomAnchor(adjustmentDetailTable, 78.0);
        AnchorPane.setLeftAnchor(adjustmentDetailTable, 5.0);
        AnchorPane.setRightAnchor(adjustmentDetailTable, 5.0);

        AnchorPane.setBottomAnchor(buttonBox, 10.0);
        AnchorPane.setLeftAnchor(buttonBox, 0.0);
        AnchorPane.setRightAnchor(buttonBox, 0.0);

        centerPane.getChildren().addAll(adjustmentFormTitle, separator, gridPane, adjustmentNote, adjustmentProductAddBtn, adjustmentDetailTable, buttonBox);

        var pane = new BorderPane();
        pane.setCenter(centerPane);
        return pane;
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20.0);
        gridPane.setPadding(new Insets(40.0, 180.0, 0.0, 0.0));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        col1.setMinWidth(10.0);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.SOMETIMES);
        col2.setMinWidth(10.0);

        RowConstraints row = new RowConstraints();
        row.setVgrow(Priority.SOMETIMES);
        row.setMinHeight(10.0);

        gridPane.getColumnConstraints().addAll(col1, col2);
        gridPane.getRowConstraints().add(row);

        return gridPane;
    }

    private HBox createButtonBox() {
        HBox buttonBox = new HBox(20.0);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10.0));

        saveBtn = new MFXButton("Save");
        saveBtn.setId("saveBtn");
        saveBtn.getStyleClass().add("filled");
        saveBtn.setOnAction(event -> {
            if (AdjustmentDetailViewModel.adjustmentDetailsList.isEmpty()) {
                showErrorMessage("Table can't be Empty");
                return;
            }
            if (AdjustmentMasterViewModel.getId() > 0) {
                AdjustmentMasterViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
            } else {
                AdjustmentMasterViewModel.saveAdjustmentMaster(this::onSuccess, this::successMessage, this::errorMessage);
            }
            onRequiredFieldsMissing();
        });

        cancelBtn = new MFXButton("Cancel");
        cancelBtn.setId("cancelBtn");
        cancelBtn.getStyleClass().add("outlined");
        cancelBtn.setOnAction(event -> {
            AppManager.getNavigation().navigate(AdjustmentPage.class);
            AdjustmentMasterViewModel.resetProperties();
            this.dispose();
        });

        buttonBox.getChildren().addAll(saveBtn, cancelBtn);
        return buttonBox;
    }

    private void bindProperties() {
        adjustmentNote.textProperty().bindBidirectional(AdjustmentMasterViewModel.noteProperty());
    }

    private void setupAddProductButton() {
        adjustmentProductAddBtn.setOnAction(e -> SpotyDialog.createDialog(new AdjustmentDetailForm(), this).showAndWait());
    }

    private void setupTable() {
        MFXTableColumn<AdjustmentDetail> productName = createColumn("Product", AdjustmentDetail::getProductName, 0.5);
        MFXTableColumn<AdjustmentDetail> productQuantity = createColumn("Quantity", AdjustmentDetail::getQuantity, 0.5);
        MFXTableColumn<AdjustmentDetail> adjustmentType = createColumn("Adjustment Type", AdjustmentDetail::getAdjustmentType, 0.5);

        adjustmentDetailTable.getTableColumns().addAll(productName, productQuantity, adjustmentType);
        addFilters();
        loadTableData();
        configureTableRow();
    }

    private <U extends Comparable<? super U>> MFXTableColumn<AdjustmentDetail> createColumn(String title, Function<AdjustmentDetail, U> mapper, double widthFactor) {
        MFXTableColumn<AdjustmentDetail> column = new MFXTableColumn<>(title, false, Comparator.comparing(mapper));
        column.setRowCellFactory(data -> new MFXTableRowCell<>(mapper));
        column.prefWidthProperty().bind(adjustmentDetailTable.widthProperty().multiply(widthFactor));
        return column;
    }

    private void addFilters() {
        adjustmentDetailTable.getFilters().addAll(
                new StringFilter<>("Name", AdjustmentDetail::getProductName),
                new LongFilter<>("Quantity", AdjustmentDetail::getQuantity),
                new StringFilter<>("Adjustment Type", AdjustmentDetail::getAdjustmentType)
        );
    }

    private void loadTableData() {
        if (AdjustmentDetailViewModel.getAdjustmentDetails().isEmpty()) {
            AdjustmentDetailViewModel.getAdjustmentDetails().addListener((ListChangeListener<AdjustmentDetail>) change -> adjustmentDetailTable.setItems(AdjustmentDetailViewModel.getAdjustmentDetails()));
        } else {
            adjustmentDetailTable.itemsProperty().bindBidirectional(AdjustmentDetailViewModel.adjustmentDetailsProperty());
        }
    }

    private void configureTableRow() {
        adjustmentDetailTable.setPrefSize(1000, 1000);
        adjustmentDetailTable.features().enableBounceEffect();
        adjustmentDetailTable.features().enableSmoothScrolling(0.5);

        adjustmentDetailTable.setTableRowFactory(adjustmentDetail -> {
            MFXTableRow<AdjustmentDetail> row = new MFXTableRow<>(adjustmentDetailTable, adjustmentDetail);
            row.setOnContextMenuRequested(event -> showContextMenu(row).show(adjustmentDetailTable.getScene().getWindow(), event.getScreenX(), event.getScreenY()));
            return row;
        });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<AdjustmentDetail> row) {
        MFXContextMenu contextMenu = new MFXContextMenu(adjustmentDetailTable);
        contextMenu.addItems(createMenuItem("Edit", event -> editRow(row)), createMenuItem("Delete", event -> new DeleteConfirmationDialog(() -> deleteRow(row), row.getData().getProductName(), this)));
        return contextMenu;
    }

    private MFXContextMenuItem createMenuItem(String text, EventHandler<ActionEvent> handler) {
        MFXContextMenuItem item = new MFXContextMenuItem(text);
        item.setOnAction(handler);
        return item;
    }

    private void editRow(MFXTableRow<AdjustmentDetail> row) {
        SpotyThreader.spotyThreadPool(() -> AdjustmentDetailViewModel.getAdjustmentDetail(row.getData()));
        SpotyDialog.createDialog(new AdjustmentDetailForm(), this).showAndWait();
    }

    private void deleteRow(MFXTableRow<AdjustmentDetail> row) {
        AdjustmentDetailViewModel.removeAdjustmentDetail(row.getData().getId(), AdjustmentDetailViewModel.adjustmentDetailsList.indexOf(row.getData()));
    }

    private void onSuccess() {
        AppManager.getNavigation().navigate(AdjustmentPage.class);
        AdjustmentMasterViewModel.resetProperties();
        AdjustmentMasterViewModel.getAllAdjustmentMasters(null, null);
        ProductViewModel.getAllProducts(null, null);
        this.dispose();
    }

    private void onRequiredFieldsMissing() {
        showErrorMessage("Required fields can't be null");
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);
        AdjustmentMasterViewModel.getAllAdjustmentMasters(null, null);
    }

    private void showErrorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, "fas-triangle-exclamation");
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
        adjustmentDetailTable = null;
        adjustmentNote = null;
        adjustmentFormTitle = null;
        adjustmentProductAddBtn = null;
        saveBtn = null;
        cancelBtn = null;
    }
}
