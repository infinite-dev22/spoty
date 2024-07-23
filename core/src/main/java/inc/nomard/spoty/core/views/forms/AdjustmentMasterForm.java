package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.adjustments.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.components.label_components.controls.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.pages.*;
import inc.nomard.spoty.network_bridge.dtos.adjustments.*;
import inc.nomard.spoty.utils.*;
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
    public TableView<AdjustmentDetail> tableView;
    public LabeledTextField note;
    public Label title;
    public Button addBtn, saveBtn, cancelBtn;

    public AdjustmentMasterForm() {
        addNode(initialize());
        bindProperties();
        setupAddProductButton();
        setupTable();
    }

    private BorderPane initialize() {
        title = new Label("Adjustment Form");
        title.setId("title");

        Separator separator = new Separator();
        separator.setPrefWidth(200.0);

        addBtn = new Button("Add Product");
        addBtn.setDefaultButton(true);

        tableView = new TableView<>();

        note = new LabeledTextField();
        note.setLabel("Note");

        HBox buttonBox = createButtonBox();

        AnchorPane centerPane = new AnchorPane();
        centerPane.getStyleClass().add("card-flat");
        centerPane.setPadding(new Insets(5.0));

        AnchorPane.setTopAnchor(title, 0.0);
        AnchorPane.setLeftAnchor(title, 0.0);
        AnchorPane.setRightAnchor(title, 0.0);

        AnchorPane.setTopAnchor(separator, 20.0);
        AnchorPane.setLeftAnchor(separator, 0.0);
        AnchorPane.setRightAnchor(separator, 0.0);

        AnchorPane.setTopAnchor(note, 40.0);
        AnchorPane.setLeftAnchor(note, 0.0);
        AnchorPane.setRightAnchor(note, 0.0);

        AnchorPane.setTopAnchor(addBtn, 100.0);
        AnchorPane.setLeftAnchor(addBtn, 0.0);
        AnchorPane.setRightAnchor(addBtn, 0.0);

        AnchorPane.setTopAnchor(tableView, 160.0);
        AnchorPane.setBottomAnchor(tableView, 78.0);
        AnchorPane.setLeftAnchor(tableView, 5.0);
        AnchorPane.setRightAnchor(tableView, 5.0);

        AnchorPane.setBottomAnchor(buttonBox, 10.0);
        AnchorPane.setLeftAnchor(buttonBox, 0.0);
        AnchorPane.setRightAnchor(buttonBox, 0.0);

        centerPane.getChildren().addAll(title, separator, note, addBtn, tableView, buttonBox);

        var pane = new BorderPane();
        pane.setCenter(centerPane);
        return pane;
    }

    private HBox createButtonBox() {
        HBox buttonBox = new HBox(20.0);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10.0));

        saveBtn = new Button("Save");
        saveBtn.setId("saveBtn");
        saveBtn.setDefaultButton(true);
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

        cancelBtn = new Button("Cancel");
        cancelBtn.setId("cancelBtn");
        cancelBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
        cancelBtn.setOnAction(event -> {
            AppManager.getNavigation().navigate(AdjustmentPage.class);
            AdjustmentMasterViewModel.resetProperties();
            this.dispose();
        });

        buttonBox.getChildren().addAll(saveBtn, cancelBtn);
        return buttonBox;
    }

    private void bindProperties() {
        note.textProperty().bindBidirectional(AdjustmentMasterViewModel.noteProperty());
    }

    private void setupAddProductButton() {
        addBtn.setOnAction(e -> SpotyDialog.createDialog(new AdjustmentDetailForm(), this).showAndWait());
    }

    private void setupTable() {
        TableColumn<AdjustmentDetail, String> productName = createColumn("Product", 0.5);
        TableColumn<AdjustmentDetail, String> productQuantity = createColumn("Quantity", 0.5);
        TableColumn<AdjustmentDetail, String> adjustmentType = createColumn("Adjustment Type", 0.5);

        tableView.getColumns().addAll(productName, productQuantity, adjustmentType);
        loadTableData();
        configureTableRow();
    }

    private <U extends Comparable<? super U>> TableColumn<AdjustmentDetail, String> createColumn(String title, double widthFactor) {
        TableColumn<AdjustmentDetail, String> column = new TableColumn<>(title);
        column.prefWidthProperty().bind(tableView.widthProperty().multiply(widthFactor));
        return column;
    }

    private void loadTableData() {
        tableView.setItems(AdjustmentDetailViewModel.getAdjustmentDetails());
    }

    private void configureTableRow() {
        tableView.setPrefSize(1000, 1000);

        tableView.setRowFactory(adjustmentDetail -> {
            TableRow<AdjustmentDetail> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> showContextMenu(row).show(tableView.getScene().getWindow(), event.getScreenX(), event.getScreenY()));
            return row;
        });
    }

    private ContextMenu showContextMenu(TableRow<AdjustmentDetail> row) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(createMenuItem("Edit", event -> editRow(row)), createMenuItem("Delete", event -> new DeleteConfirmationDialog(() -> deleteRow(row), row.getItem().getProductName(), this)));
        return contextMenu;
    }

    private MenuItem createMenuItem(String text, EventHandler<ActionEvent> handler) {
        MenuItem item = new MenuItem(text);
        item.setOnAction(handler);
        return item;
    }

    private void editRow(TableRow<AdjustmentDetail> row) {
        SpotyThreader.spotyThreadPool(() -> AdjustmentDetailViewModel.getAdjustmentDetail(row.getItem()));
        SpotyDialog.createDialog(new AdjustmentDetailForm(), this).showAndWait();
    }

    private void deleteRow(TableRow<AdjustmentDetail> row) {
        AdjustmentDetailViewModel.removeAdjustmentDetail(row.getItem().getId(), AdjustmentDetailViewModel.adjustmentDetailsList.indexOf(row.getItem()));
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
        tableView = null;
        note = null;
        title = null;
        addBtn = null;
        saveBtn = null;
        cancelBtn = null;
    }
}
