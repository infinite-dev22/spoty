package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.viewModels.stock_ins.StockInDetailViewModel;
import inc.nomard.spoty.core.viewModels.stock_ins.StockInMasterViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextArea;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.network_bridge.dtos.stock_ins.StockInDetail;
import inc.nomard.spoty.utils.AppUtils;
import inc.nomard.spoty.utils.SpotyThreader;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@SuppressWarnings("unchecked")
@Slf4j
public class StockInMasterForm extends VBox {
    private final ModalPane modalPane1;
    private final ModalPane modalPane2;
    public TableView<StockInDetail> tableView;
    public ValidatableTextArea note;
    public Label title;
    public CustomButton saveBtn;
    public Button addBtn, cancelBtn;
    private TableColumn<StockInDetail, StockInDetail> product, quantity;
    private TableColumn<StockInDetail, String> description;

    public StockInMasterForm(ModalPane modalPane1, ModalPane modalPane2) {
        this.modalPane1 = modalPane1;
        this.modalPane2 = modalPane2;
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
        addBtn.setOnAction(event -> showForm());
        addBtn.setPrefWidth(10000d);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        return addBtn;
    }

    private void showForm() {
        var dialog = new ModalContentHolder(450, 250);
        dialog.getChildren().add(new StockInDetailForm(modalPane2));
        dialog.setPadding(new Insets(5d));
        modalPane2.setAlignment(Pos.CENTER_RIGHT);
        modalPane2.show(dialog);
        modalPane2.setPersistent(true);
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

        saveBtn = new CustomButton("Save");
        saveBtn.setId("saveBtn");
        saveBtn.getStyleClass().add(Styles.ACCENT);
        saveBtn.setOnAction(event -> {
            if (StockInDetailViewModel.stockInDetailsList.isEmpty()) {
                errorMessage("Table can't be Empty");
                return;
            }
            saveBtn.startLoading();
            if (StockInMasterViewModel.getId() > 0) {
                StockInMasterViewModel.updateStockInMaster(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
            } else {
                StockInMasterViewModel.saveStockInMaster(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
            }
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
        contextMenu.getItems().addAll(createMenuItem("Edit", event -> editRow(row)), createMenuItem("Delete", event -> new DeleteConfirmationDialog(AppManager.getGlobalModalPane(), () -> deleteRow(row), row.getItem().getProductName()).showDialog()));
        return contextMenu;
    }

    private MenuItem createMenuItem(String text, EventHandler<ActionEvent> handler) {
        MenuItem item = new MenuItem(text);
        item.setOnAction(handler);
        return item;
    }

    private void editRow(TableRow<StockInDetail> row) {
        SpotyThreader.spotyThreadPool(() -> StockInDetailViewModel.getStockInDetail(row.getItem()));
        showForm();
    }

    private void deleteRow(TableRow<StockInDetail> row) {
        StockInDetailViewModel.removeStockInDetail(row.getItem().getId(), StockInDetailViewModel.stockInDetailsList.indexOf(row.getItem()));
    }

    private void onSuccess() {
        this.dispose();
        StockInMasterViewModel.getAllStockInMasters(null, null, null, null);
    }

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        saveBtn.stopLoading();
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
        modalPane1.hide(true);
        modalPane1.setPersistent(false);
        StockInMasterViewModel.resetProperties();
        tableView = null;
        note = null;
        title = null;
        addBtn = null;
        saveBtn = null;
        cancelBtn = null;
    }
}
