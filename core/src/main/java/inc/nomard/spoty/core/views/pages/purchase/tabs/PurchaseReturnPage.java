package inc.nomard.spoty.core.views.pages.purchase.tabs;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.returns.purchases.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.navigation.*;
import io.github.palexdev.materialfx.controls.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class PurchaseReturnPage extends OutlinePage {
    private final SideModalPane modalPane;
    private TextField searchBar;
    private TableView<PurchaseReturnMaster> masterTable;
    private MFXProgressSpinner progress;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> supplier;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> purchaseDate;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> purchaseTotalPrice;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> purchaseAmountPaid;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> purchaseAmountDue;
    private TableColumn<PurchaseReturnMaster, String> purchaseStatus;
    private TableColumn<PurchaseReturnMaster, String> masterPaymentStatus;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> createdBy;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> createdAt;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> updatedBy;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> updatedAt;

    public PurchaseReturnPage() {
        modalPane = new SideModalPane();
        getChildren().addAll(modalPane, init());
        progress.setManaged(true);
        progress.setVisible(true);
        PurchaseReturnMasterViewModel.getAllPurchaseReturnMasters(this::onDataInitializationSuccess, this::errorMessage, null, null);
        modalPane.displayProperty().addListener((observableValue, closed, open) -> {
            if (!open) {
                modalPane.setAlignment(Pos.CENTER);
                modalPane.usePredefinedTransitionFactories(null);
            }
        });
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

    private VBox buildCenter() {
        masterTable = new TableView<>();
        VBox.setVgrow(masterTable, Priority.ALWAYS);
        HBox.setHgrow(masterTable, Priority.ALWAYS);
        var paging = new HBox(new Spacer(), buildPagination(), new Spacer(), buildPageSize());
        paging.setPadding(new Insets(0d, 20d, 0d, 5d));
        paging.setAlignment(Pos.CENTER);
        if (PurchaseReturnMasterViewModel.getTotalPages() > 0) {
            paging.setVisible(true);
            paging.setManaged(true);
        } else {
            paging.setVisible(false);
            paging.setManaged(false);
        }
        PurchaseReturnMasterViewModel.totalPagesProperty().addListener((observableValue, oldNum, newNum) -> {
            if (PurchaseReturnMasterViewModel.getTotalPages() > 0) {
                paging.setVisible(true);
                paging.setManaged(true);
            } else {
                paging.setVisible(false);
                paging.setManaged(false);
            }
        });
        var centerHolder = new VBox(masterTable, paging);
        VBox.setVgrow(centerHolder, Priority.ALWAYS);
        HBox.setHgrow(centerHolder, Priority.ALWAYS);
        return centerHolder;
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
                purchaseAmountDue,
                createdBy,
                createdAt).toList());
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

    private ContextMenu showContextMenu(TableRow<PurchaseReturnMaster> obj) {
        var contextMenu = new ContextMenu();
        var delete = new MenuItem("Delete");
        var view = new MenuItem("View");

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
        contextMenu.getItems().addAll(view, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    private void onSuccess() {
        PurchaseReturnMasterViewModel.getAllPurchaseReturnMasters(null, null, null, null);
    }

    public void viewShow(PurchaseReturnMaster purchaseReturnMaster) {
        var scrollPane = new ScrollPane(new PurchaseReturnPreview(purchaseReturnMaster));
        scrollPane.setMaxHeight(10_000);

        var dialog = new ModalContentHolder(710, -1);
        dialog.getChildren().add(scrollPane);
        dialog.setPadding(new Insets(5d));
        modalPane.show(dialog);
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
                PurchaseReturnMasterViewModel.getAllPurchaseReturnMasters(null, null, null, null);
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
        purchaseTotalPrice.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        purchaseTotalPrice.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getTotal()));
            }
        });
        purchaseAmountPaid.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        purchaseAmountPaid.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getAmountPaid()));
            }
        });
        purchaseAmountDue.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        purchaseAmountDue.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getAmountDue()));
            }
        });
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

    private Pagination buildPagination() {
        var pagination = new Pagination(PurchaseReturnMasterViewModel.getTotalPages(), 0);
        pagination.setMaxPageIndicatorCount(5);
        pagination.pageCountProperty().bindBidirectional(PurchaseReturnMasterViewModel.totalPagesProperty());
        pagination.setPageFactory(pageNum -> {
            progress.setManaged(true);
            progress.setVisible(true);
            PurchaseReturnMasterViewModel.getAllPurchaseReturnMasters(() -> {
                progress.setManaged(false);
                progress.setVisible(false);
            }, null, pageNum, PurchaseReturnMasterViewModel.getPageSize());
            PurchaseReturnMasterViewModel.setPageNumber(pageNum);
            return new StackPane(); // null isn't allowed
        });
        return pagination;
    }

    private ComboBox<Integer> buildPageSize() {
        var pageSize = new ComboBox<Integer>();
        pageSize.setItems(FXCollections.observableArrayList(25, 50, 75, 100));
        pageSize.valueProperty().bindBidirectional(PurchaseReturnMasterViewModel.pageSizeProperty().asObject());
        pageSize.valueProperty().addListener(
                (observableValue, integer, t1) -> {
                    progress.setManaged(true);
                    progress.setVisible(true);
                    PurchaseReturnMasterViewModel
                            .getAllPurchaseReturnMasters(
                                    () -> {
                                        progress.setManaged(false);
                                        progress.setVisible(false);
                                    },
                                    null,
                                    PurchaseReturnMasterViewModel.getPageNumber(),
                                    t1);
                    PurchaseReturnMasterViewModel.setPageSize(t1);
                });
        return pageSize;
    }
}
