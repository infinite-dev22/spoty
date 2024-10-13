package inc.nomard.spoty.core.views.pages;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.hrm.employee.EmploymentStatusViewModel;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.forms.EmploymentStatusForm;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.layout.SideModalPane;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.EmploymentStatus;
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
import java.util.stream.Stream;

@Log4j2
public class EmploymentStatusPage extends OutlinePage {
    private final ModalPane modalPane;
    private TextField searchBar;
    private TableView<EmploymentStatus> masterTable;
    private SpotyProgressSpinner progress;
    private Button createBtn;
    private TableColumn<EmploymentStatus, String> name;
    private TableColumn<EmploymentStatus, EmploymentStatus> appearance;
    private TableColumn<EmploymentStatus, String> description;
    private TableColumn<EmploymentStatus, EmploymentStatus> createdBy;
    private TableColumn<EmploymentStatus, EmploymentStatus> createdAt;
    private TableColumn<EmploymentStatus, EmploymentStatus> updatedBy;
    private TableColumn<EmploymentStatus, EmploymentStatus> updatedAt;

    public EmploymentStatusPage() {
        super();
        modalPane = new SideModalPane();
        getChildren().addAll(modalPane, init());
        progress.setManaged(true);
        progress.setVisible(true);
        EmploymentStatusViewModel.getAllEmploymentStatuses(this::onDataInitializationSuccess, this::errorMessage, null, null);
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
        searchBar.setPromptText("Search employment statuses");
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
        createBtn.setDefaultButton(true);
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
        if (EmploymentStatusViewModel.getTotalPages() > 0) {
            paging.setVisible(true);
            paging.setManaged(true);
        } else {
            paging.setVisible(false);
            paging.setManaged(false);
        }
        EmploymentStatusViewModel.totalPagesProperty().addListener((observableValue, oldNum, newNum) -> {
            if (EmploymentStatusViewModel.getTotalPages() > 0) {
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
        name = new TableColumn<>("Name");
        appearance = new TableColumn<>("Appearance");
        description = new TableColumn<>("Description");
        createdBy = new TableColumn<>("Created By");
        createdAt = new TableColumn<>("Created At");
        updatedBy = new TableColumn<>("Updated By");
        updatedAt = new TableColumn<>("Updated At");

        name.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        appearance.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        description.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        createdBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        createdAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        updatedBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        updatedAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(name,
                appearance,
                description,
                createdBy,
                createdAt,
                updatedBy,
                updatedAt).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);
        styleEmploymentStatusTable();

        masterTable.setItems(EmploymentStatusViewModel.getEmploymentStatuses());
    }

    private void styleEmploymentStatusTable() {
        masterTable.setPrefSize(1200, 1000);

        masterTable.setRowFactory(
                t -> {
                    TableRow<EmploymentStatus> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<EmploymentStatus>) event.getSource())
                                        .show(
                                                masterTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    row.setPrefHeight(50);
                    return row;
                });
    }

    private ContextMenu showContextMenu(TableRow<EmploymentStatus> obj) {
        var contextMenu = new ContextMenu();
        var delete = new MenuItem("Delete");
        var edit = new MenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(AppManager.getGlobalModalPane(), () -> {
            EmploymentStatusViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getName()).showDialog());
        // Edit
        edit.setOnAction(
                e -> {
                    EmploymentStatusViewModel.getItem(obj.getItem().getId(), this::showDialog, this::errorMessage);
                    e.consume();
                });
        contextMenu.getItems().addAll(edit, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> this.showDialog());
    }

    private void showDialog() {
        var dialog = new ModalContentHolder(500, -1);
        dialog.getChildren().add(new EmploymentStatusForm(modalPane));
        dialog.setPadding(new Insets(5d));
        modalPane.setAlignment(Pos.TOP_RIGHT);
        modalPane.usePredefinedTransitionFactories(Side.RIGHT);
        modalPane.setOutTransitionFactory(node -> Animations.fadeOutRight(node, Duration.millis(400)));
        modalPane.setInTransitionFactory(node -> Animations.slideInRight(node, Duration.millis(400)));
        modalPane.show(dialog);
        modalPane.setPersistent(true);
    }

    private void onSuccess() {
        EmploymentStatusViewModel.getAllEmploymentStatuses(null, null, null, null);
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                EmploymentStatusViewModel.getAllEmploymentStatuses(null, null, null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            EmploymentStatusViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        progress.setManaged(false);
        progress.setVisible(false);
    }

    private void setupTableColumns() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        appearance.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        appearance.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(EmploymentStatus item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty
                        && !Objects.isNull(item)
                        && !Objects.isNull(item.getColor())
                        && !item.getColor().isEmpty()
                        && !item.getColor().isBlank()) {
                    this.setAlignment(Pos.CENTER);

                    var col = Color.valueOf(item.getColor());
                    var color = Color.rgb((int) col.getRed() * 255, (int) col.getGreen() * 255, (int) col.getBlue() * 255, .2);

                    var chip = new Label(item.getName());
                    chip.setTextFill(Color.valueOf(item.getColor()).darker());
                    chip.setPadding(new Insets(5, 10, 5, 10));
                    chip.setBorder(new Border(new BorderStroke(Color.valueOf(item.getColor()).darker(), BorderStrokeStyle.SOLID, new CornerRadii(50), BorderWidths.DEFAULT)));
                    chip.setBackground(new Background(new BackgroundFill(color, new CornerRadii(50), Insets.EMPTY)));
                    chip.setAlignment(Pos.CENTER);
                    chip.setPrefWidth(150d);

                    setGraphic(chip);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(null);
                }
            }
        });
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        createdBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(EmploymentStatus item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedBy()) ? null : item.getCreatedBy().getName());
            }
        });
        createdAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(EmploymentStatus item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedAt()) ? null : item.getCreatedAt().format(dtf));
            }
        });
        updatedBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(EmploymentStatus item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedBy()) ? null : item.getUpdatedBy().getName());
            }
        });
        updatedAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(EmploymentStatus item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedAt()) ? null : item.getUpdatedAt().format(dtf));
            }
        });
    }

    private Pagination buildPagination() {
        var pagination = new Pagination(EmploymentStatusViewModel.getTotalPages(), 0);
        pagination.setMaxPageIndicatorCount(5);
        pagination.pageCountProperty().bindBidirectional(EmploymentStatusViewModel.totalPagesProperty());
        pagination.setPageFactory(pageNum -> {
            progress.setManaged(true);
            progress.setVisible(true);
            EmploymentStatusViewModel.getAllEmploymentStatuses(() -> {
                progress.setManaged(false);
                progress.setVisible(false);
            }, null, pageNum, EmploymentStatusViewModel.getPageSize());
            EmploymentStatusViewModel.setPageNumber(pageNum);
            return new StackPane(); // null isn't allowed
        });
        return pagination;
    }

    private ComboBox<Integer> buildPageSize() {
        var pageSize = new ComboBox<Integer>();
        pageSize.setItems(FXCollections.observableArrayList(25, 50, 75, 100));
        pageSize.valueProperty().bindBidirectional(EmploymentStatusViewModel.pageSizeProperty().asObject());
        pageSize.valueProperty().addListener(
                (observableValue, integer, t1) -> {
                    progress.setManaged(true);
                    progress.setVisible(true);
                    EmploymentStatusViewModel
                            .getAllEmploymentStatuses(
                                    () -> {
                                        progress.setManaged(false);
                                        progress.setVisible(false);
                                    },
                                    null,
                                    EmploymentStatusViewModel.getPageNumber(),
                                    t1);
                    EmploymentStatusViewModel.setPageSize(t1);
                });
        return pageSize;
    }
}
