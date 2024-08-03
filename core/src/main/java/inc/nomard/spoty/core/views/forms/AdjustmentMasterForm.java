package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.*;
import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.adjustments.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.components.validatables.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.dtos.adjustments.*;
import inc.nomard.spoty.utils.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class AdjustmentMasterForm extends VBox {
    private final ModalPane modalPane;
    public TableView<AdjustmentDetail> tableView;
    public ValidatableTextArea note;
    public Label title;
    public Button addBtn, saveBtn, cancelBtn;
    private TableColumn<AdjustmentDetail, AdjustmentDetail> productName;
    private TableColumn<AdjustmentDetail, String> productQuantity;
    private TableColumn<AdjustmentDetail, String> adjustmentType;

    public AdjustmentMasterForm(ModalPane modalPane) {
        this.modalPane = modalPane;
        init();
    }

    private void init() {
        this.getStyleClass().add("card-flat");
        this.setPadding(new Insets(10d));
        this.setSpacing(10d);
        VBox.setVgrow(this, Priority.ALWAYS);
        this.getChildren().addAll(buildTitle(),
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
        var subTitle = new Text("Adjustment");
        subTitle.getStyleClass().add(Styles.TITLE_4);
        return buildFieldHolder(title, subTitle, buildSeparator());
    }

    private TableView<AdjustmentDetail> buildTable() {
        tableView = new TableView<>();
        HBox.setHgrow(tableView, Priority.ALWAYS);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        productName = new TableColumn<>("Product");
        productQuantity = new TableColumn<>("Quantity");
        adjustmentType = new TableColumn<>("Adjustment Type");

        productName.prefWidthProperty().bind(tableView.widthProperty().multiply(.5));
        productQuantity.prefWidthProperty().bind(tableView.widthProperty().multiply(.2));
        adjustmentType.prefWidthProperty().bind(tableView.widthProperty().multiply(.3));

        tableView.getColumns().addAll(productName, productQuantity, adjustmentType);
        tableView.setItems(AdjustmentDetailViewModel.getAdjustmentDetails());
        setupTableColumns();
        configureTable();
        return tableView;
    }

    private Button buildAddButton() {
        addBtn = new Button("Add");
        addBtn.setDefaultButton(true);
        addBtn.setOnAction(event -> SpotyDialog.createDialog(new AdjustmentDetailForm(), this).showAndWait());
        addBtn.setPrefWidth(10000d);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        return addBtn;
    }

    private VBox buildNote() {
        var label = new Label("Note");
        note = new ValidatableTextArea();
        note.setPrefHeight(100d);
        note.setWrapText(true);
        note.textProperty().bindBidirectional(AdjustmentMasterViewModel.noteProperty());
        return buildFieldHolder(label, note);
    }

    private VBox buildFieldHolder(Node... nodes) {
        VBox vbox = new VBox();
        vbox.setSpacing(5d);
        vbox.setPadding(new Insets(5d, 0d, 0d, 0d));
        vbox.getChildren().addAll(nodes);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        return vbox;
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
        cancelBtn.setOnAction(event -> this.dispose());

        buttonBox.getChildren().addAll(saveBtn, cancelBtn);
        return buttonBox;
    }

    private void configureTable() {
        tableView.setRowFactory(adjustmentDetail -> {
            TableRow<AdjustmentDetail> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> showContextMenu(row).show(tableView.getScene().getWindow(), event.getScreenX(), event.getScreenY()));
            return row;
        });
    }

    private ContextMenu showContextMenu(TableRow<AdjustmentDetail> row) {
        var contextMenu = new ContextMenu();
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
        this.dispose();
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

    private void setupTableColumns() {
        productName.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        productName.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(AdjustmentDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getProductName());
            }
        });
        productQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        adjustmentType.setCellValueFactory(new PropertyValueFactory<>("adjustmentType"));
    }

    public void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
        AdjustmentMasterViewModel.resetProperties();
        tableView = null;
        note = null;
        title = null;
        addBtn = null;
        saveBtn = null;
        cancelBtn = null;
    }
}
