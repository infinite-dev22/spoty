package inc.nomard.spoty.core.views.pages;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.hrm.leave.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.leave.*;
import io.github.palexdev.materialfx.controls.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class LeaveRequestPage extends OutlinePage {
    private TextField searchBar;
    private TableView<LeaveStatus> masterTable;
    private MFXProgressSpinner progress;
    private Button createBtn;
    private TableColumn<LeaveStatus, LeaveStatus> employeeName;
    private TableColumn<LeaveStatus, LeaveStatus> dateAndTime;
    private TableColumn<LeaveStatus, LeaveStatus> leaveType;
    private TableColumn<LeaveStatus, LeaveStatus> createdBy;
    private TableColumn<LeaveStatus, LeaveStatus> createdAt;
    private TableColumn<LeaveStatus, LeaveStatus> updatedBy;
    private TableColumn<LeaveStatus, LeaveStatus> updatedAt;

    public LeaveRequestPage() {
        addNode(init());
        progress.setManaged(true);
        progress.setVisible(true);
        LeaveStatusViewModel.getAllLeaveStatuses(this::onDataInitializationSuccess, this::errorMessage);
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
        searchBar.setPromptText("Search leave requests");
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

    private AnchorPane buildCenter() {
        masterTable = new TableView<>();
        NodeUtils.setAnchors(masterTable, new Insets(0d));
        return new AnchorPane(masterTable);
    }

    private void setupTable() {
        employeeName = new TableColumn<>("Employee");
        dateAndTime = new TableColumn<>("Date & Time");
        leaveType = new TableColumn<>("Leave Type");
        createdBy = new TableColumn<>("Created By");
        createdAt = new TableColumn<>("Created At");
        updatedBy = new TableColumn<>("Updated By");
        updatedAt = new TableColumn<>("Updated At");

        employeeName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        dateAndTime.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        leaveType.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        createdBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        createdAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        updatedBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        updatedAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(employeeName, dateAndTime, leaveType, createdBy, createdAt, updatedBy, updatedAt).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);
        styleLeaveStatusTable();

        masterTable.setItems(LeaveStatusViewModel.getLeaveStatuses());
    }

    private void styleLeaveStatusTable() {
        masterTable.setPrefSize(1200, 1000);

        masterTable.setRowFactory(
                t -> {
                    TableRow<LeaveStatus> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<LeaveStatus>) event.getSource())
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

    private ContextMenu showContextMenu(TableRow<LeaveStatus> obj) {
        var contextMenu = new ContextMenu();
        var delete = new MenuItem("Delete");
        var edit = new MenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            LeaveStatusViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getEmployeeName() + "'s leave request", this));
        // Edit
        edit.setOnAction(
                e -> {
                    LeaveStatusViewModel.getItem(obj.getItem().getId(), () -> SpotyDialog.createDialog(new LeaveRequestForm(), this).showAndWait(), this::errorMessage);
                    e.consume();
                });
        contextMenu.getItems().addAll(edit, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> SpotyDialog.createDialog(new LeaveRequestForm(), this).showAndWait());
    }

    private void onSuccess() {
        LeaveStatusViewModel.getAllLeaveStatuses(null, null);
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                LeaveStatusViewModel.getAllLeaveStatuses(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            LeaveStatusViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
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

    private void setupTableColumns() {
        employeeName.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        employeeName.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(LeaveStatus item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && !Objects.isNull(item)) {
                    var employeeName = new Label(item.getEmployeeName());
                    var employeeDesignation = new Label(item.getDesignation().getName());
                    employeeDesignation.getStyleClass().add(Styles.TEXT_MUTED);
                    var vbox = new VBox(employeeName, employeeDesignation);
                    setGraphic(vbox);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(null);
                }
            }
        });
        dateAndTime.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        dateAndTime.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(LeaveStatus item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && !Objects.isNull(item)) {
                    this.setAlignment(Pos.CENTER);

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                    var from = new Label("from:");
                    from.getStyleClass().add(Styles.TEXT_MUTED);
                    var to = new Label("to:");
                    to.getStyleClass().add(Styles.TEXT_MUTED);
                    var vbox1 = new VBox(from, to);
                    var fromTime = new Label(item.getStartDate().format(dtf));
                    var toTime = new Label(item.getEndDate().format(dtf));
                    var vbox2 = new VBox(fromTime, toTime);
                    var hbox = new HBox(2d, vbox1, vbox2);
                    setGraphic(hbox);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(null);
                }
            }
        });
        leaveType.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        leaveType.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(LeaveStatus item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);
                setText(item.getLeaveType());
            }
        });
        createdBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(LeaveStatus item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedBy()) ? null : item.getCreatedBy().getName());
            }
        });
        createdAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(LeaveStatus item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedAt()) ? null : item.getCreatedAt().format(dtf));
            }
        });
        updatedBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(LeaveStatus item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedBy()) ? null : item.getUpdatedBy().getName());
            }
        });
        updatedAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(LeaveStatus item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedAt()) ? null : item.getUpdatedAt().format(dtf));
            }
        });
    }
}
