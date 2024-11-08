package inc.nomard.spoty.core.views.pages.purchase.tabs;

import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.returns.purchases.PurchaseReturnMasterViewModel;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.components.SpotyProgressSpinner;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.layout.SideModalPane;
import inc.nomard.spoty.core.views.layout.message.SpotyMessage;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import inc.nomard.spoty.core.views.layout.navigation.Spacer;
import inc.nomard.spoty.core.views.previews.PurchaseReturnPreview;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns.PurchaseReturnMaster;
import inc.nomard.spoty.utils.AppUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
@Slf4j
public class PurchaseReturnPage extends OutlinePage {
    private final SideModalPane modalPane;
    private TextField searchBar;
    private TableView<PurchaseReturnMaster> masterTable;
    private SpotyProgressSpinner progress;
    private TableColumn<PurchaseReturnMaster, String> reference;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> supplier;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> purchaseDate;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> purchaseTotalPrice;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> purchaseAmountPaid;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> purchaseAmountDue;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> approvalStatus;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> purchaseStatus;
    private TableColumn<PurchaseReturnMaster, PurchaseReturnMaster> paymentStatus;

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
        progress = new SpotyProgressSpinner();
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
        reference = new TableColumn<>("Ref");
        supplier = new TableColumn<>("Supplier");
        purchaseDate = new TableColumn<>("Date");
        purchaseTotalPrice = new TableColumn<>("Total Amount");
        purchaseAmountPaid = new TableColumn<>("Amount Paid");
        purchaseAmountDue = new TableColumn<>("Amount Due");
        purchaseStatus = new TableColumn<>("Purchase Status");
        approvalStatus = new TableColumn<>("Approval Status");
        paymentStatus = new TableColumn<>("Pay Status");

        reference.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
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
                .bind(masterTable.widthProperty().multiply(.15));
        approvalStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        purchaseStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.2));
        paymentStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.2));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(reference,
                supplier,
                purchaseDate,
                purchaseTotalPrice,
                purchaseAmountPaid,
                purchaseAmountDue,
                purchaseStatus,
                approvalStatus,
                paymentStatus).toList());
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
        delete.setOnAction(event -> new DeleteConfirmationDialog(AppManager.getGlobalModalPane(), () -> {
            PurchaseReturnMasterViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getSupplierName() + "'s purchase").showDialog());

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
        var scrollPane = new ScrollPane(new PurchaseReturnPreview(purchaseReturnMaster, modalPane));
        scrollPane.setMaxHeight(10_000);

        var dialog = new ModalContentHolder(710, -1);
        dialog.getChildren().add(scrollPane);
        dialog.setPadding(new Insets(5d));
        modalPane.show(dialog);
    }

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, FontAwesomeSolid.CHECK_CIRCLE);
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, FontAwesomeSolid.EXCLAMATION_TRIANGLE);
        progress.setManaged(false);
        progress.setVisible(false);
    }

    private void displayNotification(String message, MessageVariants type, Ikon icon) {
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
        reference.setCellValueFactory(new PropertyValueFactory<>("ref"));
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
                this.setAlignment(Pos.CENTER_RIGHT);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getAmountDue()));
            }
        });
        approvalStatus.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        approvalStatus.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                if (!empty && !Objects.isNull(item)) {
                    var chip = new Label(item.getApprovalStatus());
                    chip.setPadding(new Insets(5, 10, 5, 10));
                    chip.setAlignment(Pos.CENTER);

                    Color col;
                    Color color;
                    switch (item.getApprovalStatus().toLowerCase()) {
                        case "approved" -> {
                            col = Color.rgb(50, 215, 75);
                            color = Color.rgb(50, 215, 75, .1);
                        }
                        case "pending" -> {
                            col = Color.web("#9a1fe6");
                            color = Color.web("#9a1fe6", .1);
                        }
                        case "rejected" -> {
                            col = Color.rgb(255, 69, 58);
                            color = Color.rgb(255, 69, 58, .1);
                        }
                        case "returned" -> {
                            col = Color.rgb(255, 159, 10);
                            color = Color.rgb(255, 159, 10, .1);
                        }
                        default -> {
                            col = Color.web("#aeaeb2");
                            color = Color.web("#aeaeb2", .1);
                        }
                    }

                    chip.setTextFill(col);
                    chip.setBackground(new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY)));

                    setGraphic(chip);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(null);
                }
            }
        });
        purchaseStatus.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        purchaseStatus.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                if (!empty && !Objects.isNull(item)) {
                    var chip = new Label(item.getPurchaseStatus());
                    chip.setPadding(new Insets(5, 10, 5, 10));
                    chip.setAlignment(Pos.CENTER);

                    Color col;
                    Color color;
                    switch (item.getPurchaseStatus().toLowerCase()) {
                        case "received" -> {
                            col = Color.rgb(50, 215, 75);
                            color = Color.rgb(50, 215, 75, .1);
                        }
                        case "pending" -> {
                            col = Color.web("#9a1fe6");
                            color = Color.web("#9a1fe6", .1);
                        }
                        case "ordered" -> {
                            col = Color.rgb(255, 159, 10);
                            color = Color.rgb(255, 159, 10, .1);
                        }
                        default -> {
                            col = Color.web("#aeaeb2");
                            color = Color.web("#aeaeb2", .1);
                        }
                    }

                    chip.setTextFill(col);
                    chip.setBackground(new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY)));

                    setGraphic(chip);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(null);
                }
            }
        });
        paymentStatus.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        paymentStatus.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                if (!empty && !Objects.isNull(item)) {
                    var chip = new Label(item.getPaymentStatus());
                    chip.setPadding(new Insets(5, 10, 5, 10));
                    chip.setAlignment(Pos.CENTER);

                    Color col;
                    Color color;
                    switch (item.getPaymentStatus().toLowerCase()) {
                        case "paid" -> {
                            col = Color.rgb(50, 215, 75);
                            color = Color.rgb(50, 215, 75, .1);
                        }
                        case "unpaid" -> {
                            col = Color.web("#9a1fe6");
                            color = Color.web("#9a1fe6", .1);
                        }
                        case "partial" -> {
                            col = Color.rgb(255, 159, 10);
                            color = Color.rgb(255, 159, 10, .1);
                        }
                        default -> {
                            col = Color.web("#aeaeb2");
                            color = Color.web("#aeaeb2", .1);
                        }
                    }

                    chip.setTextFill(col);
                    chip.setBackground(new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY)));

                    setGraphic(chip);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(null);
                }
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
