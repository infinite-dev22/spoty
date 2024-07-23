package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.stock_ins.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.components.label_components.controls.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.pages.*;
import inc.nomard.spoty.network_bridge.dtos.stock_ins.*;
import inc.nomard.spoty.utils.*;
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
public class StockInMasterForm extends OutlineFormPage {
    public TableView<StockInDetail> table;
    public LabeledTextField note;
    public Label title;
    public Button saveBtn,
            cancelBtn,
            addBtn;

    public StockInMasterForm() {
        addNode(init());
    }

    private BorderPane init() {
        Label purchaseFormTitle = new Label("StockIn Form");
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

    private Button buildAddButton() {
        addBtn = new Button("Add");
        addBtn.setDefaultButton(true);
        addBtn.setOnAction(event -> SpotyDialog.createDialog(new StockInDetailForm(), this).showAndWait());
        addBtn.setPrefWidth(10000d);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        return addBtn;
    }

    private TableView<StockInDetail> buildTable() {
        table = new TableView<>();
        HBox.setHgrow(table, Priority.ALWAYS);
        setupTable();
        return table;
    }

    private VBox createCustomGrid() {
        var vbox = new VBox();
        vbox.setSpacing(10d);
        vbox.getChildren().add(buildNote());
        HBox.setHgrow(vbox, Priority.ALWAYS);
        UIUtils.anchor(vbox, 40d, 0d, null, 0d);
        return vbox;
    }

    private VBox buildFieldHolder(Node... nodes) {
        VBox vbox = new VBox();
        vbox.setSpacing(5d);
        vbox.setPadding(new Insets(5d, 0d, 0d, 0d));
        vbox.getChildren().addAll(nodes);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        return vbox;
    }

    private VBox buildNote() {
        note = new LabeledTextField();
        note.setLabel("Note");
        note.setPrefWidth(10000d);
        note.textProperty().bindBidirectional(StockInMasterViewModel.noteProperty());
        return buildFieldHolder(note);
    }

    private Button buildSaveButton() {
        saveBtn = new Button("Save");
        saveBtn.setDefaultButton(true);
        saveBtn.setOnAction(event -> {
            if (!table.isDisabled() && StockInDetailViewModel.getStockInDetails().isEmpty()) {
                errorMessage("Table can't be Empty");
            }

            if (isValidForm()) {
                if (StockInMasterViewModel.getId() > 0) {
                    StockInMasterViewModel.updateStockInMaster(this::onSuccess, this::successMessage, this::errorMessage);
                } else {
                    StockInMasterViewModel.saveStockInMaster(this::onSuccess, this::successMessage, this::errorMessage);
                }
            }
        });
        return saveBtn;
    }

    private Button buildCancelButton() {
        cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
        cancelBtn.setOnAction(event -> {
            AppManager.getNavigation().navigate(StockInPage.class);
            StockInMasterViewModel.resetProperties();
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
        TableColumn<StockInDetail, String> productName = new TableColumn<>("Product");
        TableColumn<StockInDetail, String> productQuantity = new TableColumn<>("Quantity");
        TableColumn<StockInDetail, String> productDescription = new TableColumn<>("Description");

        productName.prefWidthProperty().bind(table.widthProperty().multiply(.5));
        productQuantity.prefWidthProperty().bind(table.widthProperty().multiply(.5));
        productDescription.prefWidthProperty().bind(table.widthProperty().multiply(.5));

        getStockInDetailTable();

        table.setItems(StockInDetailViewModel.getStockInDetails());
    }

    private void getStockInDetailTable() {
        table.setPrefSize(10000d, 10000d);

        table.setRowFactory(
                stockInDetail -> {
                    TableRow<StockInDetail> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<StockInDetail>) event.getSource())
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

    private ContextMenu showContextMenu(TableRow<StockInDetail> obj) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        MenuItem edit = new MenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            StockInDetailViewModel.removeStockInDetail(
                    obj.getItem().getId(),
                    StockInDetailViewModel.stockInDetailsList.indexOf(obj.getItem()));
            event.consume();
        }, obj.getItem().getProductName(), this));

        // Edit
        edit.setOnAction(
                event -> {
                    try {
                        StockInDetailViewModel.getStockInDetail(obj.getItem());
                        SpotyDialog.createDialog(new StockInDetailForm(), this).showAndWait();
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, this.getClass());
                    }
                    event.consume();
                });

        contextMenu.getItems().addAll(edit, delete);

        return contextMenu;
    }

    private boolean isValidForm() {
        return !table.isDisabled() && !StockInDetailViewModel.getStockInDetails().isEmpty();
    }

    private void onSuccess() {
        AppManager.getNavigation().navigate(StockInPage.class);
        StockInMasterViewModel.resetProperties();
        StockInMasterViewModel.getAllStockInMasters(null, null);
        ProductViewModel.getAllProducts(null, null);
        this.dispose();
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
        table = null;
        note = null;
        saveBtn = null;
        cancelBtn = null;
        addBtn = null;
    }
}
