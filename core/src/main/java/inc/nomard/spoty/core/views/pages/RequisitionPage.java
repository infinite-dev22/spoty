package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.core.viewModels.SupplierViewModel;
import inc.nomard.spoty.core.viewModels.requisitions.RequisitionMasterViewModel;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.forms.RequisitionMasterForm;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.layout.SideModalPane;
import inc.nomard.spoty.core.views.layout.SpotyDialog;
import inc.nomard.spoty.core.views.previews.RequisitionPreviewController;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.network_bridge.dtos.requisitions.RequisitionMaster;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.navigation.Spacer;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.util.Duration;
import lombok.extern.java.Log;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

@SuppressWarnings("unchecked")
@Log
public class RequisitionPage extends OutlinePage {
    private final SideModalPane modalPane1;
    private final SideModalPane modalPane2;
    private TextField searchBar;
    private TableView<RequisitionMaster> masterTable;
    private MFXProgressSpinner progress;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;
    private TableColumn<RequisitionMaster, String> reference;
    private TableColumn<RequisitionMaster, RequisitionMaster> supplier;
    private TableColumn<RequisitionMaster, RequisitionMaster> status;
    private TableColumn<RequisitionMaster, RequisitionMaster> approvalStatus;
    private TableColumn<RequisitionMaster, RequisitionMaster> createdBy;
    private TableColumn<RequisitionMaster, RequisitionMaster> createdAt;
    private TableColumn<RequisitionMaster, RequisitionMaster> updatedBy;
    private TableColumn<RequisitionMaster, RequisitionMaster> updatedAt;

    public RequisitionPage() {
        try {
            viewDialogPane();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                CompletableFuture.runAsync(() -> RequisitionMasterViewModel.getAllRequisitionMasters(null, null, null, null)));

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
        searchBar.setPromptText("Search requisitions");
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
        if (RequisitionMasterViewModel.getTotalPages() > 0) {
            paging.setVisible(true);
            paging.setManaged(true);
        } else {
            paging.setVisible(false);
            paging.setManaged(false);
        }
        RequisitionMasterViewModel.totalPagesProperty().addListener((observableValue, oldNum, newNum) -> {
            if (RequisitionMasterViewModel.getTotalPages() > 0) {
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
        status = new TableColumn<>("Approved Status");
        approvalStatus = new TableColumn<>("Status");
        createdBy = new TableColumn<>("Created By");
        createdAt = new TableColumn<>("Created At");
        updatedBy = new TableColumn<>("Updated By");
        updatedAt = new TableColumn<>("Updated At");

        reference.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        supplier.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        status.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        approvalStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        createdBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        createdAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        updatedBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        updatedAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(reference,
                supplier,
                status,
                approvalStatus,
                createdBy,
                createdAt,
                updatedBy,
                updatedAt).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);
        getTable();

        masterTable.setItems(RequisitionMasterViewModel.getRequisitions());
    }

    private void getTable() {
        masterTable.setPrefSize(1200, 1000);

        masterTable.setRowFactory(
                t -> {
                    TableRow<RequisitionMaster> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<RequisitionMaster>) event.getSource())
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

    private ContextMenu showContextMenu(TableRow<RequisitionMaster> obj) {
        var contextMenu = new ContextMenu();
        var delete = new MenuItem("Delete");
        var edit = new MenuItem("Edit");
        var view = new MenuItem("View");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            RequisitionMasterViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getSupplierName() + "'s requisition", this));
        // Edit
        edit.setOnAction(
                e -> {
                    RequisitionMasterViewModel.getRequisitionMaster(obj.getItem().getId(), this::showForm, this::errorMessage);
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

    private void showForm() {
        var dialog = new ModalContentHolder(500, -1);
        dialog.getChildren().add(new RequisitionMasterForm(modalPane1, modalPane2));
        dialog.setPadding(new Insets(5d));
        modalPane1.setAlignment(Pos.TOP_RIGHT);
        modalPane1.usePredefinedTransitionFactories(Side.RIGHT);
        modalPane1.setOutTransitionFactory(node -> Animations.slideOutRight(node, Duration.millis(400)));
        modalPane1.setInTransitionFactory(node -> Animations.slideInRight(node, Duration.millis(400)));
        modalPane1.show(dialog);
        modalPane1.setPersistent(true);
    }

    private void onSuccess() {
        RequisitionMasterViewModel.getAllRequisitionMasters(null, null, null, null);
    }

    private void viewDialogPane() throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/RequisitionPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new RequisitionPreviewController());
        MFXGenericDialog dialogContent = viewFxmlLoader.load();
        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialogContent.setPrefHeight(screenHeight * .98);
        dialogContent.setPrefWidth(700);
        viewDialog = SpotyDialog.createDialog(dialogContent, this);
    }

    public void viewShow(RequisitionMaster requisitionMaster) {
        RequisitionPreviewController controller = viewFxmlLoader.getController();
        controller.init(requisitionMaster);
        viewDialog.showAndWait();
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
                RequisitionMasterViewModel.getAllRequisitionMasters(null, null, null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            RequisitionMasterViewModel.searchItem(nv, () -> {
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
            public void updateItem(RequisitionMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getSupplierName());
            }
        });
        status.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        status.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(RequisitionMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                if (!empty && !Objects.isNull(item)) {
                    var chip = new Label(item.getStatus());
                    chip.setPadding(new Insets(5, 10, 5, 10));
                    chip.setAlignment(Pos.CENTER);

                    Color col;
                    Color color;
                    switch (item.getStatus().toLowerCase()) {
                        case "processed" -> {
                            col = Color.rgb(50, 215, 75);
                            color = Color.rgb(50, 215, 75, .15);
                        }
                        case "pending" -> {
                            col = Color.web("#9a1fe6");
                            color = Color.web("#9a1fe6", .15);
                        }
                        case "canceled" -> {
                            col = Color.rgb(255, 69, 58);
                            color = Color.rgb(255, 69, 58, .15);
                        }
                        default -> {
                            col = Color.web("#aeaeb2");
                            color = Color.web("#aeaeb2", .15);
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
            public void updateItem(RequisitionMaster item, boolean empty) {
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
                            color = Color.rgb(50, 215, 75, .15);
                        }
                        case "pending" -> {
                            col = Color.web("#9a1fe6");
                            color = Color.web("#9a1fe6", .15);
                        }
                        case "rejected" -> {
                            col = Color.rgb(255, 69, 58);
                            color = Color.rgb(255, 69, 58, .15);
                        }
                        case "returned" -> {
                            col = Color.rgb(255, 159, 10);
                            color = Color.rgb(255, 159, 10, .15);
                        }
                        default -> {
                            col = Color.web("#aeaeb2");
                            color = Color.web("#aeaeb2", .15);
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
            public void updateItem(RequisitionMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedBy()) ? null : item.getCreatedBy().getName());
            }
        });
        createdAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(RequisitionMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedAt()) ? null : item.getCreatedAt().format(dtf));
            }
        });
        updatedBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(RequisitionMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedBy()) ? null : item.getUpdatedBy().getName());
            }
        });
        updatedAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(RequisitionMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedAt()) ? null : item.getUpdatedAt().format(dtf));
            }
        });
    }

    private Pagination buildPagination() {
        var pagination = new Pagination(RequisitionMasterViewModel.getTotalPages(), 0);
        pagination.setMaxPageIndicatorCount(5);
        pagination.pageCountProperty().bindBidirectional(RequisitionMasterViewModel.totalPagesProperty());
        pagination.setPageFactory(pageNum -> {
            progress.setManaged(true);
            progress.setVisible(true);
            RequisitionMasterViewModel.getAllRequisitionMasters(() -> {
                progress.setManaged(false);
                progress.setVisible(false);
            }, null, pageNum, RequisitionMasterViewModel.getPageSize());
            RequisitionMasterViewModel.setPageNumber(pageNum);
            return new StackPane(); // null isn't allowed
        });
        return pagination;
    }

    private ComboBox<Integer> buildPageSize() {
        var pageSize = new ComboBox<Integer>();
        pageSize.setItems(FXCollections.observableArrayList(25, 50, 75, 100));
        pageSize.valueProperty().bindBidirectional(RequisitionMasterViewModel.pageSizeProperty().asObject());
        pageSize.valueProperty().addListener(
                (observableValue, integer, t1) -> {
                    progress.setManaged(true);
                    progress.setVisible(true);
                    RequisitionMasterViewModel
                            .getAllRequisitionMasters(
                                    () -> {
                                        progress.setManaged(false);
                                        progress.setVisible(false);
                                    },
                                    null,
                                    RequisitionMasterViewModel.getPageNumber(),
                                    t1);
                    RequisitionMasterViewModel.setPageSize(t1);
                });
        return pageSize;
    }
}
