package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.adjustments.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.dtos.adjustments.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.net.*;
import java.util.*;
import java.util.function.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class AdjustmentMasterFormController implements Initializable {
    public MFXTableView<AdjustmentDetail> adjustmentDetailTable;
    public MFXTextField adjustmentNote;
    public BorderPane adjustmentFormContentPane;
    public Label adjustmentFormTitle;
    public MFXButton adjustmentProductAddBtn, saveBtn, cancelBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindProperties();
        setupAddProductButton();
        Platform.runLater(this::setupTable);
    }

    private void bindProperties() {
        adjustmentNote.textProperty().bindBidirectional(AdjustmentMasterViewModel.noteProperty());
    }

    private void setupAddProductButton() {
        adjustmentProductAddBtn.setOnAction(e -> SpotyDialog.createDialog(new AdjustmentDetailForm(), adjustmentFormContentPane).showAndWait());
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
        contextMenu.addItems(createMenuItem("Edit", event -> editRow(row)), createMenuItem("Delete", event -> new DeleteConfirmationDialog(() -> deleteRow(row), row.getData().getProductName(), adjustmentFormContentPane)));
        return contextMenu;
    }

    private MFXContextMenuItem createMenuItem(String text, EventHandler<ActionEvent> handler) {
        MFXContextMenuItem item = new MFXContextMenuItem(text);
        item.setOnAction(handler);
        return item;
    }

    private void editRow(MFXTableRow<AdjustmentDetail> row) {
        SpotyThreader.spotyThreadPool(() -> AdjustmentDetailViewModel.getAdjustmentDetail(row.getData()));
        SpotyDialog.createDialog(new AdjustmentDetailForm(), adjustmentFormContentPane).showAndWait();
    }

    private void deleteRow(MFXTableRow<AdjustmentDetail> row) {
        AdjustmentDetailViewModel.removeAdjustmentDetail(row.getData().getId(), AdjustmentDetailViewModel.adjustmentDetailsList.indexOf(row.getData()));
    }

    public void adjustmentSaveBtnClicked() {
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
    }

    public void adjustmentCancelBtnClicked() {
        // BaseController.navigation.navigate(new AdjustmentPage(stage));
        AdjustmentMasterViewModel.resetProperties();
    }

    private void onSuccess() {
        adjustmentCancelBtnClicked();
        AdjustmentMasterViewModel.resetProperties();
        AdjustmentMasterViewModel.getAllAdjustmentMasters(null, null);
        ProductViewModel.getAllProducts(null, null);
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
}
