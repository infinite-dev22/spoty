package inc.nomard.spoty.core.views.pages;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.hrm.employee.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import io.github.palexdev.materialfx.controls.*;
import java.util.*;
import java.util.stream.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class EmployeePage extends OutlinePage {
    private TextField searchBar;
    private TableView<User> masterTable;
    private MFXProgressSpinner progress;
    private Button createBtn;
    private TableColumn<User, User> employeeName;
    private TableColumn<User, User> phone;
    private TableColumn<User, String> email;
    private TableColumn<User, User> employmentStatus;
    private TableColumn<User, User> department;
    private TableColumn<User, User> status;
    private TableColumn<User, String> workShift;
    private TableColumn<User, String> salary;
    private TableColumn<User, User> role;

    public EmployeePage() {
        super();
        addNode(init());
        progress.setManaged(true);
        progress.setVisible(true);
        UserViewModel.getAllUsers(this::onDataInitializationSuccess, this::errorMessage);
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
        searchBar.setPromptText("Search employees");
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
        employeeName = new TableColumn<>("Name");
        phone = new TableColumn<>("Phone");
        email = new TableColumn<>("Email");
        employmentStatus = new TableColumn<>("Employment Status");
        department = new TableColumn<>("Department");
        status = new TableColumn<>("Status");
        workShift = new TableColumn<>("Work Shift");
        salary = new TableColumn<>("Salary");
        role = new TableColumn<>("Role");

        employeeName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        email.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        phone.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        employmentStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        department.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        status.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        workShift.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        salary.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        role.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(
                employeeName,
                phone,
                email,
                employmentStatus,
                department,
                status,
                workShift,
                salary,
                role).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);
        styleUserTable();

        masterTable.setItems(UserViewModel.getUsers());
    }

    private void styleUserTable() {
        masterTable.setPrefSize(1200, 1000);

        masterTable.setRowFactory(
                t -> {
                    TableRow<User> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<User>) event.getSource())
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

    private ContextMenu showContextMenu(TableRow<User> obj) {
        var contextMenu = new ContextMenu();
        var delete = new MenuItem("Delete");
        var edit = new MenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            UserViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getName(), this));
        // Edit
        edit.setOnAction(
                e -> {
                    UserViewModel.getItem(obj.getItem().getId(), () -> SpotyDialog.createDialog(new UserForm(), this).showAndWait(), this::errorMessage);
                    e.consume();
                });
        contextMenu.getItems().addAll(edit, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> SpotyDialog.createDialog(new UserForm(), this).showAndWait());
    }

    private void onSuccess() {
        UserViewModel.getAllUsers(null, null);
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                UserViewModel.getAllUsers(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            UserViewModel.searchItem(nv, () -> {
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
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && !Objects.isNull(item) && !Objects.isNull(item.getDesignationName())) {
                    var employeeName = new Label(item.getName());
                    var employeeDesignation = new Label(item.getDesignationName());
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
        phone.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        phone.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty && Objects.isNull(item) ? null : Objects.isNull(item.getCreatedBy()) ? null : item.getUserProfile().getPhone());
            }
        });
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        employmentStatus.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        employmentStatus.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && !Objects.isNull(item) && !Objects.isNull(item.getEmploymentStatusColor()) && !Objects.isNull(item.getEmploymentStatusName())) {
                    this.setAlignment(Pos.CENTER);

                    var chip = new Label(item.getEmploymentStatusName());
                    chip.setAlignment(Pos.CENTER);
                    chip.setPadding(new Insets(5, 10, 5, 10));
                    chip.setPrefWidth(50);
                    chip.setStyle("-fx-background-color: " + item.getEmploymentStatusColor() + ";"
                            + "-fx-foreground-color: white;"
                            + "-fx-background-radius: 50;"
                            + "-fx-border-radius: 50;");
                    setGraphic(chip);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(null);
                }
            }
        });
        status.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        status.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && Objects.nonNull(item)) {
                    this.setAlignment(Pos.CENTER);

                    var chip = new Label(item.isActive());
                    chip.setAlignment(Pos.CENTER);
                    chip.setPadding(new Insets(5, 10, 5, 10));
                    chip.setPrefWidth(150);
                    chip.setStyle("-fx-foreground-color: white;"
                            + "-fx-background-radius: 50;"
                            + "-fx-border-radius: 50;");
                    if (item.getActive()) {
                        chip.getStyleClass().add(Styles.SUCCESS);
                    } else {
                        chip.getStyleClass().add(Styles.DANGER);
                    }
                    setGraphic(chip);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(null);
                }
            }
        });
        workShift.setCellValueFactory(new PropertyValueFactory<>("workShift"));
        salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        role.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        role.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && Objects.nonNull(item)) {
                    this.setAlignment(Pos.CENTER);
                    setText(item.getRole().getName());
                } else {
                    setGraphic(null);
                    setText(null);
                }
            }
        });
        department.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        department.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && Objects.nonNull(item)) {
                    this.setAlignment(Pos.CENTER);
                    setText(item.getDepartmentName());
                } else {
                    setGraphic(null);
                    setText(null);
                }
            }
        });
    }
}
