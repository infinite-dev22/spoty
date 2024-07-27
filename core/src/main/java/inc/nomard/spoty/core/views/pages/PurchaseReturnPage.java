package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.viewModels.returns.purchases.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import java.io.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class PurchaseReturnPage extends OutlinePage {
    private TextField searchBar;
    private TableView<PurchaseReturnMaster> masterTable;
    private MFXProgressSpinner progress;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> supplier;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> purchaseDate;
    private TableColumn<PurchaseReturnMaster, String> purchaseTotalPrice;
    private TableColumn<PurchaseReturnMaster, String> purchaseAmountPaid;
    private TableColumn<PurchaseReturnMaster, String> purchaseAmountDue;
    private TableColumn<PurchaseReturnMaster, String> purchaseStatus;
    private TableColumn<PurchaseReturnMaster, String> masterPaymentStatus;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> createdBy;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> createdAt;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> updatedBy;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> updatedAt;

    public PurchaseReturnPage() {
        try {
            viewDialogPane();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addNode(init());
        progress.setManaged(true);
        progress.setVisible(true);
        PurchaseReturnMasterViewModel.getPurchaseReturnMasters(this::onDataInitializationSuccess, this::errorMessage);
    }

    private void onDataInitializationSuccess() {
        progress.setManaged(false);
        progress.setVisible(false);
    }

    public BorderPane init() {
        var pane = new BorderPane();
        pane.setTop(buildTop());
        pane.setCenter(buildCenter());
        setSearchBar();
        setupTable();
        return pane;
    }

    private HBox buildLeftTop() {
        progress = new MFXProgressSpinner();
        progress.setMinSize(30d, 30d);
        progress.setPrefSize(30d, 30d);
        progress.setMaxSize(30d, 30d);
        progress.setVisible(false);
        progress.setManaged(false);
        var hbox = new HBox(progress);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildCenterTop() {
        searchBar = new TextField();
        searchBar.setPromptText("Search purchases");
        searchBar.setMinWidth(300d);
        searchBar.setPrefWidth(500d);
        searchBar.setMaxWidth(700d);
        var hbox = new HBox(searchBar);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildRightTop() {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildTop() {
        var hbox = new HBox();
        hbox.getStyleClass().add("card-flat-bottom");
        BorderPane.setAlignment(hbox, Pos.CENTER);
        hbox.setPadding(new Insets(5d));
        hbox.getChildren().addAll(buildLeftTop(), buildCenterTop(), buildRightTop());
        return hbox;
    }

    private AnchorPane buildCenter() {
        masterTable = new TableView<>();
        NodeUtils.setAnchors(masterTable, new Insets(0d));
        return new AnchorPane(masterTable);
    }

    private void setupTable() {
        supplier = new TableColumn<>("Supplier");
        purchaseDate = new TableColumn<>("Date");
        purchaseTotalPrice = new TableColumn<>("Total Amount");
        purchaseAmountPaid = new TableColumn<>("Paid Amount");
        purchaseAmountDue = new TableColumn<>("Due Amount");
        purchaseStatus = new TableColumn<>("Status");
        masterPaymentStatus = new TableColumn<>("Pay Status");
        createdBy = new TableColumn<>("Created By");
        createdAt = new TableColumn<>("Created At");
        updatedBy = new TableColumn<>("Updated By");
        updatedAt = new TableColumn<>("Updated At");

        supplier
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        purchaseDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        purchaseTotalPrice
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        purchaseAmountPaid
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        purchaseAmountDue
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        purchaseStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        masterPaymentStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        createdBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        createdAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        updatedBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        updatedAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(supplier,
                purchaseStatus,
                masterPaymentStatus,
                purchaseDate,
                purchaseTotalPrice,
                purchaseAmountPaid,
                purchaseAmountDue).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);
        getTable();

        masterTable.setItems(PurchaseReturnMasterViewModel.getPurchaseReturns());
    }

    private void getTable() {
        masterTable.setPrefSize(1200, 1000);

        masterTable.setRowFactory(
                t -> {
                    TableRow<PurchaseReturnMaster> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<PurchaseReturnMaster>) event.getSource())
                                        .show(
                                                masterTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(TableRow<PurchaseReturnMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem view = new MFXContextMenuItem("View");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            PurchaseReturnMasterViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getSupplierName() + "'s purchase", this));

        // View
        view.setOnAction(
                event -> {
                    viewShow(obj.getItem());
                    event.consume();
                });

        contextMenu.addItems(view, delete);

        return contextMenu;
    }

    private void onSuccess() {
        PurchaseReturnMasterViewModel.getPurchaseReturnMasters(null, null);
    }

    private void viewDialogPane() throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/PurchaseReturnPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new PurchaseReturnsPreviewController());
        MFXGenericDialog dialogContent = viewFxmlLoader.load();
        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialogContent.setPrefHeight(screenHeight * .98);
        dialogContent.setPrefWidth(700);
        viewDialog = SpotyDialog.createDialog(dialogContent, this);
    }

    public void viewShow(PurchaseReturnMaster purchaseMaster) {
        PurchaseReturnsPreviewController controller = viewFxmlLoader.getController();
        controller.init(purchaseMaster);
        viewDialog.showAndWait();
    }

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, "fas-circle-check");
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, "fas-triangle-exclamation");
        progress.setManaged(false);
        progress.setVisible(false);
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

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                PurchaseReturnMasterViewModel.getPurchaseReturnMasters(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            PurchaseReturnMasterViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }

    private void setupTableColumns() {
        supplier.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        supplier.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getSupplierName());
            }
        });
        purchaseDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        purchaseDate.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseReturnMaster item, boolean empty) {
                super.updateItem(item, empty);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : item.getDate().format(dtf));
            }
        });
        purchaseTotalPrice.setCellValueFactory(new PropertyValueFactory<>("total"));
        purchaseAmountPaid.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));
        purchaseAmountDue.setCellValueFactory(new PropertyValueFactory<>("amountDue"));
        purchaseStatus.setCellValueFactory(new PropertyValueFactory<>("purchaseStatus"));
        masterPaymentStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        createdBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedBy()) ? null : item.getCreatedBy().getName());
            }
        });
        createdAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedAt()) ? null : item.getCreatedAt().format(dtf));
            }
        });
        updatedBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedBy()) ? null : item.getUpdatedBy().getName());
            }
        });
        updatedAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedAt()) ? null : item.getUpdatedAt().format(dtf));
            }
        });
    }
}
