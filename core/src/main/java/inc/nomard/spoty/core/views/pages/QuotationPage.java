package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.CustomerViewModel;
import inc.nomard.spoty.core.viewModels.DiscountViewModel;
import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.core.viewModels.TaxViewModel;
import inc.nomard.spoty.core.viewModels.accounting.AccountTransactionViewModel;
import inc.nomard.spoty.core.viewModels.quotations.QuotationMasterViewModel;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.components.SpotyProgressSpinner;
import inc.nomard.spoty.core.views.forms.QuotationMasterForm;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.layout.SideModalPane;
import inc.nomard.spoty.core.views.layout.navigation.Spacer;
import inc.nomard.spoty.core.views.previews.QuotationPreview;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.network_bridge.dtos.quotations.QuotationMaster;
import inc.nomard.spoty.utils.AppUtils;
import inc.nomard.spoty.utils.SpotyLogger;
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
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
@Slf4j
public class QuotationPage extends OutlinePage {
    private final SideModalPane modalPane1;
    private final SideModalPane modalPane2;
    private TextField searchBar;
    private TableView<QuotationMaster> masterTable;
    private SpotyProgressSpinner progress;
    private Button createBtn;
    private TableColumn<QuotationMaster, String> reference;
    private TableColumn<QuotationMaster, QuotationMaster> customer;
    private TableColumn<QuotationMaster, QuotationMaster> total;
    private TableColumn<QuotationMaster, QuotationMaster> quotationStatus;
    private TableColumn<QuotationMaster, QuotationMaster> approvalStatus;
    private TableColumn<QuotationMaster, QuotationMaster> createdBy;
    private TableColumn<QuotationMaster, QuotationMaster> createdAt;
    private TableColumn<QuotationMaster, QuotationMaster> updatedBy;
    private TableColumn<QuotationMaster, QuotationMaster> updatedAt;

    public QuotationPage() {
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
                CompletableFuture.runAsync(() -> CustomerViewModel.getAllCustomers(null, null, null, null)),
                CompletableFuture.runAsync(() -> TaxViewModel.getTaxes(null, null, null, null)),
                CompletableFuture.runAsync(() -> DiscountViewModel.getDiscounts(null, null, null, null)),
                CompletableFuture.runAsync(() -> AccountTransactionViewModel.getAllTransactions(null, null, null, null)));

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
        createBtnAction();
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
        searchBar.setPromptText("Search quotations");
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
        createBtn = new Button("Create");
        var hbox = new HBox(createBtn);
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
        if (QuotationMasterViewModel.getTotalPages() > 0) {
            paging.setVisible(true);
            paging.setManaged(true);
        } else {
            paging.setVisible(false);
            paging.setManaged(false);
        }
        QuotationMasterViewModel.totalPagesProperty().addListener((observableValue, oldNum, newNum) -> {
            if (QuotationMasterViewModel.getTotalPages() > 0) {
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
        customer = new TableColumn<>("Customer");
        total = new TableColumn<>("Total Amount");
        quotationStatus = new TableColumn<>("Quotation Status");
        approvalStatus = new TableColumn<>("Approval Status");
        createdBy = new TableColumn<>("Created By");
        createdAt = new TableColumn<>("Created At");
        updatedBy = new TableColumn<>("Updated By");
        updatedAt = new TableColumn<>("Updated At");

        reference.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        customer.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        total.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        quotationStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        approvalStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        createdBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        createdAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        updatedBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        updatedAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(reference,
                customer,
                total,
                quotationStatus,
                approvalStatus,
                createdBy,
                createdAt,
                updatedBy,
                updatedAt).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);
        getQuotationMasterTable();

        masterTable.setItems(QuotationMasterViewModel.getQuotations());
    }

    private void getQuotationMasterTable() {
        masterTable.setPrefSize(1000, 1000);

        masterTable.setRowFactory(
                t -> {
                    TableRow<QuotationMaster> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<QuotationMaster>) event.getSource())
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

    private ContextMenu showContextMenu(TableRow<QuotationMaster> obj) {
        var contextMenu = new ContextMenu();
        var view = new MenuItem("View");
        var delete = new MenuItem("Delete");
        var edit = new MenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(AppManager.getGlobalModalPane(), () -> {
            QuotationMasterViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getCustomerName() + "'s quotation").showDialog());
        // Edit
        edit.setOnAction(
                e -> {
                    QuotationMasterViewModel.getQuotationMaster(obj.getItem().getId(), this::createBtnAction, this::errorMessage);
                    e.consume();
                });
        // View
        view.setOnAction(
                event -> {
                    viewShow(obj.getItem());
                    event.consume();
                });
        contextMenu.getItems().addAll(view, edit, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> showForm());
    }

    private void showForm() {
        var dialog = new ModalContentHolder(500, -1);
        dialog.getChildren().add(new QuotationMasterForm(modalPane1, modalPane2));
        dialog.setPadding(new Insets(5d));
        modalPane1.setAlignment(Pos.TOP_RIGHT);
        modalPane1.usePredefinedTransitionFactories(Side.RIGHT);
        modalPane1.setOutTransitionFactory(node -> Animations.fadeOutRight(node, Duration.millis(400)));
        modalPane1.setInTransitionFactory(node -> Animations.slideInRight(node, Duration.millis(400)));
        modalPane1.show(dialog);
        modalPane1.setPersistent(true);
    }

    private void onSuccess() {
        QuotationMasterViewModel.getAllQuotationMasters(null, null, null, null);
    }

    public void viewShow(QuotationMaster quotationMaster) {
        var dialog = new ModalContentHolder(710, -1);
        dialog.getChildren().add(new QuotationPreview(quotationMaster, modalPane1));
        dialog.setPadding(new Insets(5d));
        modalPane1.show(dialog);
        modalPane1.setPersistent(true);
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
                QuotationMasterViewModel.getAllQuotationMasters(null, null, null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            QuotationMasterViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }

    private void setupTableColumns() {
        reference.setCellValueFactory(new PropertyValueFactory<>("ref"));
        customer.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        customer.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(QuotationMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getCustomerName());
            }
        });
        total.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        total.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(QuotationMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getTotal()));
            }
        });
        quotationStatus.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        quotationStatus.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(QuotationMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                if (!empty && !Objects.isNull(item)) {
                    var chip = new Label(item.getStatus());
                    chip.setPadding(new Insets(5, 10, 5, 10));
                    chip.setAlignment(Pos.CENTER);

                    Color col;
                    Color color;
                    switch (item.getStatus().toLowerCase()) {
                        case "sent" -> {
                            col = Color.rgb(50, 215, 75);
                            color = Color.rgb(50, 215, 75, .1);
                        }
                        case "pending" -> {
                            col = Color.web("#9a1fe6");
                            color = Color.web("#9a1fe6", .1);
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
        approvalStatus.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        approvalStatus.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(QuotationMaster item, boolean empty) {
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
        createdBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(QuotationMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedBy()) ? null : item.getCreatedBy().getName());
            }
        });
        createdAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(QuotationMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedAt()) ? null : item.getCreatedAt().format(dtf));
            }
        });
        updatedBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(QuotationMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedBy()) ? null : item.getUpdatedBy().getName());
            }
        });
        updatedAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(QuotationMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedAt()) ? null : item.getUpdatedAt().format(dtf));
            }
        });
    }

    private Pagination buildPagination() {
        var pagination = new Pagination(QuotationMasterViewModel.getTotalPages(), 0);
        pagination.setMaxPageIndicatorCount(5);
        pagination.pageCountProperty().bindBidirectional(QuotationMasterViewModel.totalPagesProperty());
        pagination.setPageFactory(pageNum -> {
            progress.setManaged(true);
            progress.setVisible(true);
            QuotationMasterViewModel.getAllQuotationMasters(() -> {
                progress.setManaged(false);
                progress.setVisible(false);
            }, null, pageNum, QuotationMasterViewModel.getPageSize());
            QuotationMasterViewModel.setPageNumber(pageNum);
            return new StackPane(); // null isn't allowed
        });
        return pagination;
    }

    private ComboBox<Integer> buildPageSize() {
        var pageSize = new ComboBox<Integer>();
        pageSize.setItems(FXCollections.observableArrayList(25, 50, 75, 100));
        pageSize.valueProperty().bindBidirectional(QuotationMasterViewModel.pageSizeProperty().asObject());
        pageSize.valueProperty().addListener(
                (observableValue, integer, t1) -> {
                    progress.setManaged(true);
                    progress.setVisible(true);
                    QuotationMasterViewModel
                            .getAllQuotationMasters(
                                    () -> {
                                        progress.setManaged(false);
                                        progress.setVisible(false);
                                    },
                                    null,
                                    QuotationMasterViewModel.getPageNumber(),
                                    t1);
                    QuotationMasterViewModel.setPageSize(t1);
                });
        return pageSize;
    }
}
