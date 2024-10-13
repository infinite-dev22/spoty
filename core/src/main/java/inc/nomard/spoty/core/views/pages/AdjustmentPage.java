package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.core.viewModels.adjustments.AdjustmentMasterViewModel;
import inc.nomard.spoty.core.views.forms.AdjustmentMasterForm;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.layout.SideModalPane;
import inc.nomard.spoty.core.views.previews.AdjustmentPreview;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.network_bridge.dtos.adjustments.AdjustmentMaster;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.navigation.Spacer;
import inc.nomard.spoty.core.views.components.SpotyProgressSpinner;
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
import lombok.extern.log4j.Log4j2;

import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
@Log4j2
public class AdjustmentPage extends OutlinePage {
    private final SideModalPane modalPane1;
    private final SideModalPane modalPane2;
    private TextField searchBar;
    private TableView<AdjustmentMaster> masterTable;
    private SpotyProgressSpinner progress;
    private Button createBtn;
    private TableColumn<AdjustmentMaster, String> reference;
    private TableColumn<AdjustmentMaster, String> note;
    private TableColumn<AdjustmentMaster, AdjustmentMaster> approvalStatus;
    private TableColumn<AdjustmentMaster, AdjustmentMaster> createdBy;
    private TableColumn<AdjustmentMaster, AdjustmentMaster> createdAt;
    private TableColumn<AdjustmentMaster, AdjustmentMaster> updatedBy;
    private TableColumn<AdjustmentMaster, AdjustmentMaster> updatedAt;

    public AdjustmentPage() {
        super();
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
                CompletableFuture.runAsync(() -> AdjustmentMasterViewModel.getAllAdjustmentMasters(null, null, null, null)));

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
        searchBar.setPromptText("Search adjustments");
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
        if (AdjustmentMasterViewModel.getTotalPages() > 0) {
            paging.setVisible(true);
            paging.setManaged(true);
        } else {
            paging.setVisible(false);
            paging.setManaged(false);
        }
        AdjustmentMasterViewModel.totalPagesProperty().addListener((observableValue, oldNum, newNum) -> {
            if (AdjustmentMasterViewModel.getTotalPages() > 0) {
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
        note = new TableColumn<>("Notes");
        approvalStatus = new TableColumn<>("Approval Status");
        createdBy = new TableColumn<>("Created By");
        createdAt = new TableColumn<>("Created At");
        updatedBy = new TableColumn<>("Updated By");
        updatedAt = new TableColumn<>("Updated At");

        reference.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        note.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        approvalStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        createdBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        createdAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        updatedBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        updatedAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(reference, note, approvalStatus, createdBy, createdAt, updatedBy, updatedAt).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);

        getAdjustmentMasterTable();
        masterTable.setItems(AdjustmentMasterViewModel.getAdjustmentMasters());
    }

    private void getAdjustmentMasterTable() {
        masterTable.setPrefSize(1000, 1000);

        masterTable.setRowFactory(
                t -> {
                    TableRow<AdjustmentMaster> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<AdjustmentMaster>) event.getSource())
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

    private ContextMenu showContextMenu(TableRow<AdjustmentMaster> obj) {
        var contextMenu = new ContextMenu();
        var view = new MenuItem("View");
        view.setOnAction(
                event -> {
                    viewShow(obj.getItem());
                    event.consume();
                });
        contextMenu.getItems().add(view);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> {
            var dialog = new ModalContentHolder(500, -1);
            dialog.getChildren().add(new AdjustmentMasterForm(modalPane1, modalPane2));
            dialog.setPadding(new Insets(5d));
            modalPane1.setAlignment(Pos.TOP_RIGHT);
            modalPane1.usePredefinedTransitionFactories(Side.RIGHT);
            modalPane1.setOutTransitionFactory(node -> Animations.slideOutRight(node, Duration.millis(400)));
            modalPane1.setInTransitionFactory(node -> Animations.slideInRight(node, Duration.millis(400)));
            modalPane1.show(dialog);
            modalPane1.setPersistent(true);
        });
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
                AdjustmentMasterViewModel.getAllAdjustmentMasters(null, null, null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            AdjustmentMasterViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }

    private void setupTableColumns() {
        reference.setCellValueFactory(new PropertyValueFactory<>("ref"));
        note.setCellValueFactory(new PropertyValueFactory<>("notes"));
        approvalStatus.setCellValueFactory(new PropertyValueFactory<>("approvalStatus"));
        approvalStatus.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        approvalStatus.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(AdjustmentMaster item, boolean empty) {
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
            public void updateItem(AdjustmentMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedBy()) ? null : item.getCreatedBy().getName());
            }
        });
        createdAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(AdjustmentMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedAt()) ? null : item.getCreatedAt().format(dtf));
            }
        });
        updatedBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(AdjustmentMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedBy()) ? null : item.getUpdatedBy().getName());
            }
        });
        updatedAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(AdjustmentMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedAt()) ? null : item.getUpdatedAt().format(dtf));
            }
        });
    }

    private Pagination buildPagination() {
        var pagination = new Pagination(AdjustmentMasterViewModel.getTotalPages(), 0);
        pagination.setMaxPageIndicatorCount(5);
        pagination.pageCountProperty().bindBidirectional(AdjustmentMasterViewModel.totalPagesProperty());
        pagination.setPageFactory(pageNum -> {
            progress.setManaged(true);
            progress.setVisible(true);
            AdjustmentMasterViewModel.getAllAdjustmentMasters(() -> {
                progress.setManaged(false);
                progress.setVisible(false);
            }, null, pageNum, AdjustmentMasterViewModel.getPageSize());
            AdjustmentMasterViewModel.setPageNumber(pageNum);
            return new StackPane(); // null isn't allowed
        });
        return pagination;
    }

    private ComboBox<Integer> buildPageSize() {
        var pageSize = new ComboBox<Integer>();
        pageSize.setItems(FXCollections.observableArrayList(25, 50, 75, 100));
        pageSize.valueProperty().bindBidirectional(AdjustmentMasterViewModel.pageSizeProperty().asObject());
        pageSize.valueProperty().addListener(
                (observableValue, integer, t1) -> {
                    progress.setManaged(true);
                    progress.setVisible(true);
                    AdjustmentMasterViewModel
                            .getAllAdjustmentMasters(
                                    () -> {
                                        progress.setManaged(false);
                                        progress.setVisible(false);
                                    },
                                    null,
                                    AdjustmentMasterViewModel.getPageNumber(),
                                    t1);
                    AdjustmentMasterViewModel.setPageSize(t1);
                });
        return pageSize;
    }

    public void viewShow(AdjustmentMaster adjustmentMaster) {
        var scrollPane = new ScrollPane(new AdjustmentPreview(adjustmentMaster, modalPane1));
        scrollPane.setMaxHeight(10_000);

        var dialog = new ModalContentHolder(710, -1);
        dialog.getChildren().add(scrollPane);
        modalPane1.show(dialog);
        modalPane1.setAlignment(Pos.TOP_CENTER);
        modalPane1.setPersistent(true);
    }
}
