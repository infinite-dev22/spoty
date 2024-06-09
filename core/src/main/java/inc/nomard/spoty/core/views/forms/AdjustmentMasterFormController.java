/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.components.navigation.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.adjustments.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.network_bridge.dtos.adjustments.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
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
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class AdjustmentMasterFormController implements Initializable {
    private static AdjustmentMasterFormController instance;
    private final Stage stage;
    @FXML
    public MFXTableView<AdjustmentDetail> adjustmentDetailTable;
    @FXML
    public MFXTextField adjustmentNote;
    @FXML
    public BorderPane adjustmentFormContentPane;
    @FXML
    public Label adjustmentFormTitle;
    @FXML
    public MFXButton adjustmentProductAddBtn, saveBtn, cancelBtn;
    private MFXStageDialog dialog;

    private AdjustmentMasterFormController(Stage stage) {
        this.stage = stage;
        Platform.runLater(() -> {
            try {
                initProductDialogPane(stage);
            } catch (IOException e) {
                SpotyLogger.writeToFile(e, this.getClass());
            }
        });
    }

    public static AdjustmentMasterFormController getInstance(Stage stage) {
        if (instance == null) {
            synchronized (AdjustmentMasterFormController.class) {
                instance = new AdjustmentMasterFormController(stage);
            }
        }
        return instance;
    }

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
        adjustmentProductAddBtn.setOnAction(e -> dialog.showAndWait());
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
        contextMenu.addItems(createMenuItem("Edit", event -> editRow(row)), createMenuItem("Delete", event -> new DeleteConfirmationDialog(() -> deleteRow(row), stage, adjustmentFormContentPane)));
        return contextMenu;
    }

    private MFXContextMenuItem createMenuItem(String text, EventHandler<ActionEvent> handler) {
        MFXContextMenuItem item = new MFXContextMenuItem(text);
        item.setOnAction(handler);
        return item;
    }

    private void editRow(MFXTableRow<AdjustmentDetail> row) {
        SpotyThreader.spotyThreadPool(() -> AdjustmentDetailViewModel.getAdjustmentDetail(row.getData()));
        dialog.showAndWait();
    }

    private void deleteRow(MFXTableRow<AdjustmentDetail> row) {
        AdjustmentDetailViewModel.removeAdjustmentDetail(row.getData().getId(), AdjustmentDetailViewModel.adjustmentDetailsList.indexOf(row.getData()));
    }

    private void initProductDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/AdjustmentDetailForm.fxml");
        fxmlLoader.setControllerFactory(c -> AdjustmentDetailFormController.getInstance(stage));

        MFXGenericDialog dialogContent = fxmlLoader.load();
        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setShowClose(false);

        dialog = MFXGenericDialogBuilder.build(dialogContent)
                .toStageDialogBuilder()
                .initOwner(stage)
                .initModality(Modality.WINDOW_MODAL)
                .setOwnerNode(adjustmentFormContentPane)
                .setScrimPriority(ScrimPriority.WINDOW)
                .setScrimOwner(true)
                .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
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
        BaseController.navigation.navigate(Pages.getAdjustmentPane());
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
        if (!BaseController.getInstance(stage).morphPane.getChildren().contains(notification)) {
            BaseController.getInstance(stage).morphPane.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification, stage));
        }
    }
}
