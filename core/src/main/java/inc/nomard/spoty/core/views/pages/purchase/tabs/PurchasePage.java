package inc.nomard.spoty.core.views.pages.purchase.tabs;

import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.DiscountViewModel;
import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.core.viewModels.SupplierViewModel;
import inc.nomard.spoty.core.viewModels.TaxViewModel;
import inc.nomard.spoty.core.viewModels.purchases.PurchaseMasterViewModel;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.forms.PurchaseMasterForm;
import inc.nomard.spoty.core.views.forms.PurchaseReturnMasterForm;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.layout.SideModalPane;
import inc.nomard.spoty.core.views.pages.EmployeePage;
import inc.nomard.spoty.core.views.previews.PurchasePreview;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.network_bridge.dtos.purchases.PurchaseMaster;
import inc.nomard.spoty.utils.AppUtils;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.navigation.Spacer;
import atlantafx.base.controls.RingProgressIndicator;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.extern.java.Log;

import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
@Log
public class PurchasePage extends OutlinePage {
    private final SideModalPane modalPane1;
    private final SideModalPane modalPane2;
    private TextField searchBar;
    private TableView<PurchaseMaster> masterTable;
    private RingProgressIndicator progress;
    private TableColumn<PurchaseMaster, String> reference;
    private TableColumn<PurchaseMaster, PurchaseMaster> supplier;
    private TableColumn<PurchaseMaster, PurchaseMaster> purchaseDate;
    private TableColumn<PurchaseMaster, PurchaseMaster> purchaseTotalPrice;
    private TableColumn<PurchaseMaster, PurchaseMaster> purchaseAmountPaid;
    private TableColumn<PurchaseMaster, PurchaseMaster> purchaseAmountDue;
    private TableColumn<PurchaseMaster, PurchaseMaster> approvalStatus;
    private TableColumn<PurchaseMaster, PurchaseMaster> purchaseStatus;
    private TableColumn<PurchaseMaster, PurchaseMaster> paymentStatus;

    public PurchasePage() {
        modalPane1 = new SideModalPane();
        modalPane2 = new SideModalPane();
        getChildren().addAll(modalPane1, modalPane2, init());
        progress.setManaged(true);
        progress.setVisible(true);
        modalPane1.displayProperty().addListener((observableValue, closed, open) -> {
            if (!open) {
                modalPane1.setAlignment(Pos.CENTER);
                modalPane1.usePredefinedTransitionFactories(null);
            }
        });
        modalPane2.displayProperty().addListener((observableValue, closed, open) -> {
            if (!open) {
                modalPane2.setAlignment(Pos.CENTER);
                modalPane2.usePredefinedTransitionFactories(null);
            }
        });

        CompletableFuture<Void> allDataInitialization = CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> ProductViewModel.getAllProducts(null, null, null, null)),
                CompletableFuture.runAsync(() -> SupplierViewModel.getAllSuppliers(null, null, null, null)),
                CompletableFuture.runAsync(() -> TaxViewModel.getTaxes(null, null, null, null)),
                CompletableFuture.runAsync(() -> DiscountViewModel.getDiscounts(null, null, null, null)),
                CompletableFuture.runAsync(() -> PurchaseMasterViewModel.getAllPurchaseMasters(null, null, null, null)));

        allDataInitialization.thenRun(this::onDataInitializationSuccess)
                .exceptionally(this::onDataInitializationFailure);
    }

    private Void onDataInitializationFailure(Throwable throwable) {
        SpotyLogger.writeToFile(throwable, EmployeePage.class);
        this.errorMessage("An error occurred while loading view");
        return null;
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
        progress = new RingProgressIndicator();
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
        var createBtn = new Button("Create");
        createBtn.setOnAction(event -> showForm());
        var hbox = new HBox(createBtn);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private void showForm() {
        var dialog = new ModalContentHolder(500, -1);
        dialog.getChildren().add(new PurchaseMasterForm(modalPane1, modalPane2));
        dialog.setPadding(new Insets(5d));
        modalPane1.setAlignment(Pos.TOP_RIGHT);
        modalPane1.usePredefinedTransitionFactories(Side.RIGHT);
        modalPane1.setOutTransitionFactory(node -> Animations.slideOutRight(node, Duration.millis(400)));
        modalPane1.setInTransitionFactory(node -> Animations.slideInRight(node, Duration.millis(400)));
        modalPane1.show(dialog);
        modalPane1.setPersistent(true);
    }

    private void showReturnForm() {
        var dialog = new ModalContentHolder(500, -1);
        dialog.getChildren().add(new PurchaseReturnMasterForm(modalPane1));
        dialog.setPadding(new Insets(5d));
        modalPane1.setAlignment(Pos.TOP_RIGHT);
        modalPane1.usePredefinedTransitionFactories(Side.RIGHT);
        modalPane1.setOutTransitionFactory(node -> Animations.slideOutRight(node, Duration.millis(400)));
        modalPane1.setInTransitionFactory(node -> Animations.slideInRight(node, Duration.millis(400)));
        modalPane1.show(dialog);
        modalPane1.setPersistent(true);
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
        if (PurchaseMasterViewModel.getTotalPages() > 0) {
            paging.setVisible(true);
            paging.setManaged(true);
        } else {
            paging.setVisible(false);
            paging.setManaged(false);
        }
        PurchaseMasterViewModel.totalPagesProperty().addListener((observableValue, oldNum, newNum) -> {
            if (PurchaseMasterViewModel.getTotalPages() > 0) {
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
        purchaseDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        purchaseTotalPrice
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.15));
        purchaseAmountPaid
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.15));
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
        masterTable.setItems(PurchaseMasterViewModel.getPurchases());
    }

    private void getTable() {
        masterTable.setRowFactory(
                t -> {
                    TableRow<PurchaseMaster> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<PurchaseMaster>) event.getSource())
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

    private ContextMenu showContextMenu(TableRow<PurchaseMaster> obj) {
        var contextMenu = new ContextMenu();
        var delete = new MenuItem("Delete");
        var edit = new MenuItem("Edit");
        var view = new MenuItem("View");
        var returnPurchase = new MenuItem("Return");

        delete.setOnAction(event -> new DeleteConfirmationDialog(AppManager.getGlobalModalPane(), () -> {
            PurchaseMasterViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getSupplierName() + "'s purchase").showDialog());
        edit.setOnAction(
                e -> {
                    PurchaseMasterViewModel.getPurchaseMaster(obj.getItem().getId(), this::showForm, this::errorMessage);
                    e.consume();
                });
        returnPurchase.setOnAction(
                e -> {
                    PurchaseMasterViewModel.getPurchaseMaster(obj.getItem().getId(), this::showReturnForm, this::errorMessage);
                    e.consume();
                });
        view.setOnAction(
                event -> {
                    viewShow(obj.getItem());
                    event.consume();
                });
        contextMenu.getItems().addAll(view, edit, returnPurchase, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    private void onSuccess() {
        PurchaseMasterViewModel.getAllPurchaseMasters(null, null, null, null);
    }

    public void viewShow(PurchaseMaster purchaseMaster) {
        var scrollPane = new ScrollPane(new PurchasePreview(purchaseMaster, modalPane1));
        scrollPane.setMaxHeight(10_000);

        var dialog = new ModalContentHolder(710, -1);
        dialog.getChildren().add(scrollPane);
        dialog.setPadding(new Insets(5d));
        modalPane1.show(dialog);
    }

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        progress.setManaged(false);
        progress.setVisible(false);
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                PurchaseMasterViewModel.getAllPurchaseMasters(null, null, null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            PurchaseMasterViewModel.searchItem(nv, () -> {
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
            public void updateItem(PurchaseMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getSupplierName());
            }
        });
        purchaseDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        purchaseDate.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : item.getDate().format(dtf));
            }
        });
        purchaseTotalPrice.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        purchaseTotalPrice.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getTotal()));
            }
        });
        purchaseAmountPaid.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        purchaseAmountPaid.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getAmountPaid()));
            }
        });
        purchaseAmountDue.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        purchaseAmountDue.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getAmountDue()));
            }
        });
        approvalStatus.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        approvalStatus.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseMaster item, boolean empty) {
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
            public void updateItem(PurchaseMaster item, boolean empty) {
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
            public void updateItem(PurchaseMaster item, boolean empty) {
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
        var pagination = new Pagination(PurchaseMasterViewModel.getTotalPages(), 0);
        pagination.setMaxPageIndicatorCount(5);
        pagination.pageCountProperty().bindBidirectional(PurchaseMasterViewModel.totalPagesProperty());
        pagination.setPageFactory(pageNum -> {
            progress.setManaged(true);
            progress.setVisible(true);
            PurchaseMasterViewModel.getAllPurchaseMasters(() -> {
                progress.setManaged(false);
                progress.setVisible(false);
            }, null, pageNum, PurchaseMasterViewModel.getPageSize());
            PurchaseMasterViewModel.setPageNumber(pageNum);
            return new StackPane(); // null isn't allowed
        });
        return pagination;
    }

    private ComboBox<Integer> buildPageSize() {
        var pageSize = new ComboBox<Integer>();
        pageSize.setItems(FXCollections.observableArrayList(25, 50, 75, 100));
        pageSize.valueProperty().bindBidirectional(PurchaseMasterViewModel.pageSizeProperty().asObject());
        pageSize.valueProperty().addListener(
                (observableValue, integer, t1) -> {
                    progress.setManaged(true);
                    progress.setVisible(true);
                    PurchaseMasterViewModel
                            .getAllPurchaseMasters(
                                    () -> {
                                        progress.setManaged(false);
                                        progress.setVisible(false);
                                    },
                                    null,
                                    PurchaseMasterViewModel.getPageNumber(),
                                    t1);
                    PurchaseMasterViewModel.setPageSize(t1);
                });
        return pageSize;
    }
}
