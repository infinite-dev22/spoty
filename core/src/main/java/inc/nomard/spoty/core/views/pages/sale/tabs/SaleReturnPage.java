package inc.nomard.spoty.core.views.pages.sale.tabs;

import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.returns.sales.SaleReturnMasterViewModel;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.components.SpotyProgressSpinner;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.layout.SideModalPane;
import inc.nomard.spoty.core.views.layout.message.SpotyMessage;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import inc.nomard.spoty.core.views.layout.navigation.Spacer;
import inc.nomard.spoty.core.views.previews.SaleReturnPreview;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.network_bridge.dtos.returns.sale_returns.SaleReturnMaster;
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
public class SaleReturnPage extends OutlinePage {
    private final SideModalPane modalPane;
    private TextField searchBar;
    private TableView<SaleReturnMaster> masterTable;
    private SpotyProgressSpinner progress;
    private TableColumn<SaleReturnMaster, SaleReturnMaster> saleCustomer;
    private TableColumn<SaleReturnMaster, SaleReturnMaster> saleDate;
    private TableColumn<SaleReturnMaster, SaleReturnMaster> saleGrandTotal;
    private TableColumn<SaleReturnMaster, SaleReturnMaster> saleAmountPaid;
    private TableColumn<SaleReturnMaster, SaleReturnMaster> saleAmountDue;
    private TableColumn<SaleReturnMaster, String> saleStatus;
    private TableColumn<SaleReturnMaster, String> salePaymentStatus;
    private TableColumn<SaleReturnMaster, SaleReturnMaster> createdBy;
    private TableColumn<SaleReturnMaster, SaleReturnMaster> createdAt;
    private TableColumn<SaleReturnMaster, SaleReturnMaster> updatedBy;
    private TableColumn<SaleReturnMaster, SaleReturnMaster> updatedAt;

    public SaleReturnPage() {
        modalPane = new SideModalPane();
        getChildren().addAll(modalPane, init());
        progress.setManaged(true);
        progress.setVisible(true);
        SaleReturnMasterViewModel.getSaleReturnMasters(this::onDataInitializationSuccess, this::errorMessage, null, null);
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
        searchBar.setPromptText("Search returns");
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
        if (SaleReturnMasterViewModel.getTotalPages() > 0) {
            paging.setVisible(true);
            paging.setManaged(true);
        } else {
            paging.setVisible(false);
            paging.setManaged(false);
        }
        SaleReturnMasterViewModel.totalPagesProperty().addListener((observableValue, oldNum, newNum) -> {
            if (SaleReturnMasterViewModel.getTotalPages() > 0) {
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
        saleCustomer = new TableColumn<>("Customer");
        saleDate = new TableColumn<>("Date");
        saleGrandTotal = new TableColumn<>("Total Amount");
        saleAmountPaid = new TableColumn<>("Paid Amount");
        saleAmountDue = new TableColumn<>("Amount Due");
        saleStatus = new TableColumn<>("Order Status");
        salePaymentStatus = new TableColumn<>("Pay Status");
        createdBy = new TableColumn<>("Created By");
        createdAt = new TableColumn<>("Created At");
        updatedBy = new TableColumn<>("Updated By");
        updatedAt = new TableColumn<>("Updated At");

        saleCustomer.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleGrandTotal.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleAmountPaid.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleAmountDue.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        salePaymentStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        createdBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        createdAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        updatedBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        updatedAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(saleCustomer,
                saleDate,
                saleGrandTotal,
                saleAmountPaid,
                saleAmountDue,
                saleStatus,
                salePaymentStatus,
                createdBy,
                createdAt).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);
        styleSaleReturnMasterTable();

        masterTable.setItems(SaleReturnMasterViewModel.getSales());
    }

    private void styleSaleReturnMasterTable() {
        masterTable.setPrefSize(1200, 1000);

        masterTable.setRowFactory(
                t -> {
                    TableRow<SaleReturnMaster> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<SaleReturnMaster>) event.getSource())
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

    private ContextMenu showContextMenu(TableRow<SaleReturnMaster> obj) {
        var contextMenu = new ContextMenu();
        var delete = new MenuItem("Delete");
        var view = new MenuItem("View");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(AppManager.getGlobalModalPane(), () -> {
            SaleReturnMasterViewModel.deleteSaleReturnMaster(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getCustomerName() + "'s order").showDialog());
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
        SaleReturnMasterViewModel.getSaleReturnMasters(null, null, null, null);
    }

    public void viewShow(SaleReturnMaster saleReturnMaster) {
        var scrollPane = new ScrollPane(new SaleReturnPreview(saleReturnMaster, modalPane));
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
                SaleReturnMasterViewModel.getSaleReturnMasters(null, null, null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            SaleReturnMasterViewModel.searchSaleReturnMaster(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }

    private void setupTableColumns() {
        saleCustomer.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        saleCustomer.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getCustomerName());
            }
        });
        saleDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        saleDate.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleReturnMaster item, boolean empty) {
                super.updateItem(item, empty);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedAt()) ? null : item.getCreatedAt().format(dtf));
            }
        });
        saleGrandTotal.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        saleGrandTotal.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getTotal()));
            }
        });
        saleAmountPaid.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        saleAmountPaid.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getAmountPaid()));
            }
        });
        saleAmountDue.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        saleAmountDue.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getAmountDue()));
            }
        });
        saleStatus.setCellValueFactory(new PropertyValueFactory<>("saleStatus"));
        salePaymentStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        createdBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedBy()) ? null : item.getCreatedBy().getName());
            }
        });
        createdAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedAt()) ? null : item.getCreatedAt().format(dtf));
            }
        });
        updatedBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedBy()) ? null : item.getUpdatedBy().getName());
            }
        });
        updatedAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleReturnMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedAt()) ? null : item.getUpdatedAt().format(dtf));
            }
        });
    }

    private Pagination buildPagination() {
        var pagination = new Pagination(SaleReturnMasterViewModel.getTotalPages(), 0);
        pagination.setMaxPageIndicatorCount(5);
        pagination.pageCountProperty().bindBidirectional(SaleReturnMasterViewModel.totalPagesProperty());
        pagination.setPageFactory(pageNum -> {
            progress.setManaged(true);
            progress.setVisible(true);
            SaleReturnMasterViewModel.getSaleReturnMasters(() -> {
                progress.setManaged(false);
                progress.setVisible(false);
            }, null, pageNum, SaleReturnMasterViewModel.getPageSize());
            SaleReturnMasterViewModel.setPageNumber(pageNum);
            return new StackPane(); // null isn't allowed
        });
        return pagination;
    }

    private ComboBox<Integer> buildPageSize() {
        var pageSize = new ComboBox<Integer>();
        pageSize.setItems(FXCollections.observableArrayList(25, 50, 75, 100));
        pageSize.valueProperty().bindBidirectional(SaleReturnMasterViewModel.pageSizeProperty().asObject());
        pageSize.valueProperty().addListener(
                (observableValue, integer, t1) -> {
                    progress.setManaged(true);
                    progress.setVisible(true);
                    SaleReturnMasterViewModel
                            .getSaleReturnMasters(
                                    () -> {
                                        progress.setManaged(false);
                                        progress.setVisible(false);
                                    },
                                    null,
                                    SaleReturnMasterViewModel.getPageNumber(),
                                    t1);
                    SaleReturnMasterViewModel.setPageSize(t1);
                });
        return pageSize;
    }
}
