package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.*;
import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.stock_ins.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.components.validatables.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.dtos.stock_ins.*;
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
public class StockInMasterForm extends VBox {
    private final ModalPane modalPane;
    public TableView<StockInDetail> tableView;
    public ValidatableTextArea note;
    public Label title;
    public Button addBtn, saveBtn, cancelBtn;
    private TableColumn<StockInDetail, StockInDetail> product, quantity;
    private TableColumn<StockInDetail, String> description;

    public StockInMasterForm(ModalPane modalPane) {
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
        var subTitle = new Text("Stock In");
        subTitle.getStyleClass().add(Styles.TITLE_4);
        return buildFieldHolder(title, subTitle, buildSeparator());
    }

    private TableView<StockInDetail> buildTable() {
        tableView = new TableView<>();
        HBox.setHgrow(tableView, Priority.ALWAYS);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        product = new TableColumn<>("Product");
        quantity = new TableColumn<>("Quantity");
        description = new TableColumn<>("Description");

        product.prefWidthProperty().bind(tableView.widthProperty().multiply(.5));
        quantity.prefWidthProperty().bind(tableView.widthProperty().multiply(.2));
        description.prefWidthProperty().bind(tableView.widthProperty().multiply(.3));

        tableView.getColumns().addAll(product, quantity, description);
        tableView.setItems(StockInDetailViewModel.getStockInDetails());
        setupTableColumns();
        configureTable();
        return tableView;
    }

    private Button buildAddButton() {
        addBtn = new Button("Add");
        addBtn.setDefaultButton(true);
        addBtn.setOnAction(event -> SpotyDialog.createDialog(new StockInDetailForm(), this).showAndWait());
        addBtn.setPrefWidth(10000d);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        return addBtn;
    }

    private VBox buildNote() {
        var label = new Label("Note");
        note = new ValidatableTextArea();
        note.setPrefHeight(100d);
        note.setWrapText(true);
        note.textProperty().bindBidirectional(StockInMasterViewModel.noteProperty());
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
            if (StockInDetailViewModel.stockInDetailsList.isEmpty()) {
                showErrorMessage("Table can't be Empty");
                return;
            }
            if (StockInMasterViewModel.getId() > 0) {
                StockInMasterViewModel.updateStockInMaster(this::onSuccess, this::successMessage, this::errorMessage);
            } else {
                StockInMasterViewModel.saveStockInMaster(this::onSuccess, this::successMessage, this::errorMessage);
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
        tableView.setRowFactory(stockInDetail -> {
            TableRow<StockInDetail> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> showContextMenu(row).show(tableView.getScene().getWindow(), event.getScreenX(), event.getScreenY()));
            return row;
        });
    }

    private ContextMenu showContextMenu(TableRow<StockInDetail> row) {
        var contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(createMenuItem("Edit", event -> editRow(row)), createMenuItem("Delete", event -> new DeleteConfirmationDialog(() -> deleteRow(row), row.getItem().getProductName(), this)));
        return contextMenu;
    }

    private MenuItem createMenuItem(String text, EventHandler<ActionEvent> handler) {
        MenuItem item = new MenuItem(text);
        item.setOnAction(handler);
        return item;
    }

    private void editRow(TableRow<StockInDetail> row) {
        SpotyThreader.spotyThreadPool(() -> StockInDetailViewModel.getStockInDetail(row.getItem()));
        SpotyDialog.createDialog(new StockInDetailForm(), this).showAndWait();
    }

    private void deleteRow(TableRow<StockInDetail> row) {
        StockInDetailViewModel.removeStockInDetail(row.getItem().getId(), StockInDetailViewModel.stockInDetailsList.indexOf(row.getItem()));
    }

    private void onSuccess() {
        this.dispose();
        StockInMasterViewModel.getAllStockInMasters(null, null);
        ProductViewModel.getAllProducts(null, null);
    }

    private void onRequiredFieldsMissing() {
        showErrorMessage("Required fields can't be null");
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);
        StockInMasterViewModel.getAllStockInMasters(null, null);
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
        product.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        product.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(StockInDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getProductName());
            }
        });
        quantity.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        quantity.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(StockInDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getQuantity()));
            }
        });
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    public void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
        StockInMasterViewModel.resetProperties();
        tableView = null;
        note = null;
        title = null;
        addBtn = null;
        saveBtn = null;
        cancelBtn = null;
    }
}
