package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.stock_ins.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.dtos.stock_ins.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.util.*;
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
public class StockInMasterForm extends OutlineFormPage {
    public MFXTableView<StockInDetail> table;
    public MFXTextField note;
    public Label title;
    public MFXButton saveBtn,
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

    private MFXButton buildAddButton() {
        addBtn = new MFXButton("Add");
        addBtn.getStyleClass().add("filled");
        addBtn.setOnAction(event -> SpotyDialog.createDialog(new StockInDetailForm(), this).showAndWait());
        addBtn.setPrefWidth(10000d);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        return addBtn;
    }

    private MFXTableView<StockInDetail> buildTable() {
        table = new MFXTableView<>();
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
        note = new MFXTextField();
        note.setFloatMode(FloatMode.BORDER);
        note.setFloatingText("Note");
        note.setPrefWidth(10000d);
        note.textProperty().bindBidirectional(StockInMasterViewModel.noteProperty());
        return buildFieldHolder(note);
    }

    private MFXButton buildSaveButton() {
        saveBtn = new MFXButton("Save");
        saveBtn.getStyleClass().add("filled");
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

    private MFXButton buildCancelButton() {
        cancelBtn = new MFXButton("Cancel");
        cancelBtn.getStyleClass().add("outlined");
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
        MFXTableColumn<StockInDetail> productName =
                new MFXTableColumn<>("Product", false, Comparator.comparing(StockInDetail::getProductName));
        MFXTableColumn<StockInDetail> productQuantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(StockInDetail::getQuantity));
        MFXTableColumn<StockInDetail> productDescription =
                new MFXTableColumn<>(
                        "Description", false, Comparator.comparing(StockInDetail::getDescription));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(StockInDetail::getProductName));
        productQuantity.setRowCellFactory(product -> new MFXTableRowCell<>(StockInDetail::getQuantity));
        productDescription.setRowCellFactory(
                product -> new MFXTableRowCell<>(StockInDetail::getDescription));

        productName.prefWidthProperty().bind(table.widthProperty().multiply(.5));
        productQuantity.prefWidthProperty().bind(table.widthProperty().multiply(.5));
        productDescription.prefWidthProperty().bind(table.widthProperty().multiply(.5));

        table.getTableColumns().addAll(productName, productQuantity, productDescription);

        table
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", StockInDetail::getProductName),
                        new IntegerFilter<>("Quantity", StockInDetail::getQuantity),
                        new StringFilter<>("Description", StockInDetail::getDescription));

        getStockInDetailTable();

        if (StockInDetailViewModel.getStockInDetails().isEmpty()) {
            StockInDetailViewModel.getStockInDetails()
                    .addListener(
                            (ListChangeListener<StockInDetail>)
                                    change -> table.setItems(StockInDetailViewModel.getStockInDetails()));
        } else {
            table
                    .itemsProperty()
                    .bindBidirectional(StockInDetailViewModel.stockInDetailsProperty());
        }
    }

    private void getStockInDetailTable() {
        table.setPrefSize(10000d, 10000d);
        table.features().enableBounceEffect();
        table.features().enableSmoothScrolling(0.5);

        table.setTableRowFactory(
                stockInDetail -> {
                    MFXTableRow<StockInDetail> row = new MFXTableRow<>(table, stockInDetail);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<StockInDetail>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<StockInDetail> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(table);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            StockInDetailViewModel.removeStockInDetail(
                    obj.getData().getId(),
                    StockInDetailViewModel.stockInDetailsList.indexOf(obj.getData()));
            event.consume();
        }, obj.getData().getProductName(), this));

        // Edit
        edit.setOnAction(
                event -> {
                    try {
                        StockInDetailViewModel.getStockInDetail(obj.getData());
                        SpotyDialog.createDialog(new StockInDetailForm(), this).showAndWait();
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, this.getClass());
                    }
                    event.consume();
                });

        contextMenu.addItems(edit, delete);

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
