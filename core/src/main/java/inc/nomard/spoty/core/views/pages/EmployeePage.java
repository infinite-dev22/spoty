package inc.nomard.spoty.core.views.pages;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.RoleViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.DepartmentViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.DesignationViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.EmployeeViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.EmploymentStatusViewModel;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.components.SpotyProgressSpinner;
import inc.nomard.spoty.core.views.forms.EmployeeForm;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.layout.SideModalPane;
import inc.nomard.spoty.core.views.layout.navigation.Spacer;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import inc.nomard.spoty.utils.SpotyLogger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
@Slf4j
public class EmployeePage extends OutlinePage {
    private final ModalPane modalPane;
    private TextField searchBar;
    private TableView<Employee> masterTable;
    private SpotyProgressSpinner progress;
    private Button createBtn;
    private TableColumn<Employee, Employee> employeeName;
    private TableColumn<Employee, Employee> phone;
    private TableColumn<Employee, Employee> employmentStatus;
    private TableColumn<Employee, Employee> department;
    private TableColumn<Employee, Employee> status;
    private TableColumn<Employee, Employee> salary;
    private TableColumn<Employee, Employee> role;

    public EmployeePage() {
        modalPane = new SideModalPane();
        getChildren().addAll(modalPane, init());
        progress.setManaged(true);
        progress.setVisible(true);
        modalPane.displayProperty().addListener((_, _, open) -> {
            if (!open) {
                modalPane.setAlignment(Pos.CENTER);
                modalPane.usePredefinedTransitionFactories(null);
            }
        });
        createBtn.setDisable(true);

        CompletableFuture<Void> allDataInitialization = CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> DepartmentViewModel.getAllDepartments(null, null, null, null)),
                CompletableFuture.runAsync(() -> DesignationViewModel.getAllDesignations(null, null, null, null)),
                CompletableFuture.runAsync(() -> EmploymentStatusViewModel.getAllEmploymentStatuses(null, null, null, null)),
                CompletableFuture.runAsync(() -> RoleViewModel.getAllRoles(null, null, null, null)));

        allDataInitialization.thenRun(this::onDataInitializationSuccess)
                .exceptionally(this::onDataInitializationFailure);
    }

    private Void onDataInitializationFailure(Throwable throwable) {
        SpotyLogger.writeToFile(throwable, EmployeePage.class);
        this.errorMessage("An error occurred while loading view");
        createBtn.setDisable(true);
        return null;
    }

    private void onDataInitializationSuccess() {
        progress.setManaged(false);
        progress.setVisible(false);
        createBtn.setDisable(true);
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

    private VBox buildCenter() {
        masterTable = new TableView<>();
        VBox.setVgrow(masterTable, Priority.ALWAYS);
        HBox.setHgrow(masterTable, Priority.ALWAYS);
        var paging = new HBox(buildPageSize(), new Spacer(), buildPagination());
        paging.setPadding(new Insets(0d, 20d, 0d, 5d));
        paging.setAlignment(Pos.CENTER);
        if (EmployeeViewModel.getTotalPages() > 0) {
            paging.setVisible(true);
            paging.setManaged(true);
        } else {
            paging.setVisible(false);
            paging.setManaged(false);
        }
        EmployeeViewModel.totalPagesProperty().addListener((_, _, _) -> {
            if (EmployeeViewModel.getTotalPages() > 0) {
                paging.setVisible(true);
                paging.setManaged(true);
            } else {
                paging.setVisible(false);
                paging.setManaged(false);
            }
        });
        var centerHolder = new VBox(masterTable, paging);
        centerHolder.getStyleClass().add("card-flat-top");
        VBox.setVgrow(centerHolder, Priority.ALWAYS);
        HBox.setHgrow(centerHolder, Priority.ALWAYS);
        return centerHolder;
    }

    private void setupTable() {
        employeeName = new TableColumn<>("Name");
        department = new TableColumn<>("Department");
        phone = new TableColumn<>("Phone");
        salary = new TableColumn<>("Salary");
        role = new TableColumn<>("Role");
        employmentStatus = new TableColumn<>("Employment Status");
        status = new TableColumn<>("Status");

        employeeName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        department.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        phone.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        salary.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        role.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        employmentStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        status.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(
                employeeName,
                department,
                phone,
                salary,
                role,
                employmentStatus,
                status).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);
        styleEmployeeTable();
        masterTable.setItems(EmployeeViewModel.employeesList);
        masterTable.getStyleClass().addAll(Styles.STRIPED, Tweaks.EDGE_TO_EDGE);
    }

    private void styleEmployeeTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.setRowFactory(_ -> new TableRow<>() {
            @Override
            public void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else {
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<Employee>) event.getSource())
                                        .show(
                                                masterTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    setMinHeight(40);
                    setPrefHeight(50);
                    setMinHeight(60);
                    setOnContextMenuRequested(eventHandler);
                }
            }
        });
    }

    private ContextMenu showContextMenu(TableRow<Employee> obj) {
        var contextMenu = new ContextMenu();
        var delete = new MenuItem("Delete");
        var edit = new MenuItem("Edit");

        delete.setOnAction(event -> new DeleteConfirmationDialog(AppManager.getGlobalModalPane(), () -> {
            EmployeeViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, SpotyUtils::successMessage, SpotyUtils::errorMessage);
            event.consume();
        }, obj.getItem().getName()).showDialog());
        edit.setOnAction(
                e -> {
                    EmployeeViewModel.getItem(obj.getItem().getId(), this::showDialog, SpotyUtils::errorMessage);
                    e.consume();
                });
        contextMenu.getItems().addAll(edit, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(_ -> this.showDialog());
    }

    private void showDialog() {
        var dialog = new ModalContentHolder(500, -1);
        dialog.getChildren().add(new EmployeeForm(modalPane));
        dialog.setPadding(new Insets(5d));
        modalPane.setAlignment(Pos.TOP_RIGHT);
        modalPane.usePredefinedTransitionFactories(Side.RIGHT);
        modalPane.setOutTransitionFactory(node -> Animations.fadeOutRight(node, Duration.millis(400)));
        modalPane.setInTransitionFactory(node -> Animations.slideInRight(node, Duration.millis(400)));
        modalPane.show(dialog);
        modalPane.setPersistent(true);
    }

    private void onSuccess() {
        EmployeeViewModel.getAllEmployees(null, null, null, null);
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((_, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                EmployeeViewModel.getAllEmployees(null, null, null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            EmployeeViewModel.searchItem(nv, () -> {
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
        employeeName.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        employeeName.setCellFactory(_ -> new TableCell<>() {
            @Override
            public void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                if (empty && Objects.isNull(item)) {
                    setGraphic(null);
                } else {
                    this.setAlignment(Pos.CENTER);
                    var employeeName = new Text(item.getName());
                    var employeeEmail = new Text(item.getEmail());
                    employeeEmail.getStyleClass().add(Styles.TEXT_MUTED);
                    var vbox = new VBox(employeeName, employeeEmail);
                    vbox.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(vbox);
                }
            }
        });
        phone.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        phone.setCellFactory(_ -> new TableCell<>() {
            @Override
            public void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);
                setText(empty && Objects.isNull(item) ? null : Objects.isNull(item.getCreatedBy()) ? null : item.getPhone());
            }
        });
        employmentStatus.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        employmentStatus.setCellFactory(_ -> new TableCell<>() {
            @Override
            public void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                if (empty && Objects.isNull(item)) {
                    setGraphic(null);
                } else {
                    this.setAlignment(Pos.CENTER);
                    var chip = new Label(item.getEmploymentStatusName());
                    chip.setAlignment(Pos.CENTER);
                    chip.setPadding(new Insets(5, 10, 5, 10));
                    chip.setPrefWidth(150);

                    Color col;
                    Color color;
                    switch (item.getEmploymentStatusColor().toLowerCase()) {
                        case "green" -> {
                            col = Color.rgb(50, 215, 75);
                            color = Color.rgb(50, 215, 75, .1);
                        }
                        case "blue" -> {
                            col = Color.rgb(10, 132, 255);
                            color = Color.rgb(10, 132, 255, .1);
                        }
                        case "red" -> {
                            col = Color.rgb(255, 69, 58);
                            color = Color.rgb(255, 69, 58, .1);
                        }
                        case "orange" -> {
                            col = Color.rgb(255, 159, 10);
                            color = Color.rgb(255, 159, 10, .1);
                        }
                        case "purple" -> {
                            col = Color.rgb(158, 0, 255);
                            color = Color.rgb(158, 0, 255, .1);
                        }
                        case "brown" -> {
                            col = Color.rgb(255, 128, 0);
                            color = Color.rgb(255, 128, 0, .1);
                        }
                        default -> {
                            col = Color.web("#aeaeb2");
                            color = Color.web("#aeaeb2", .1);
                        }
                    }

                    chip.setTextFill(col);
                    chip.setBackground(new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY)));
                    setGraphic(chip);
                }
            }
        });
        status.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        status.setCellFactory(_ -> new TableCell<>() {
            @Override
            public void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                if (empty && Objects.isNull(item)) {
                    setGraphic(null);
                } else {
                    this.setAlignment(Pos.CENTER);
                    var chip = new Label(item.getIsActive());
                    chip.setPadding(new Insets(5, 10, 5, 10));
                    chip.setPrefWidth(150);
                    chip.setAlignment(Pos.CENTER);

                    Color col;
                    Color color;
                    if (item.isActive()) {
                        col = Color.rgb(50, 215, 75);
                        color = Color.rgb(50, 215, 75, .1);
                    } else {
                        col = Color.rgb(255, 69, 58);
                        color = Color.rgb(255, 69, 58, .1);
                    }

                    chip.setTextFill(col);
                    chip.setBackground(new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY)));
                    setGraphic(chip);
                }
            }
        });
        salary.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        salary.setCellFactory(_ -> new TableCell<>() {
            @Override
            public void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);
                setText(empty && Objects.isNull(item) ? null : item.getSalary());
            }
        });
        role.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        role.setCellFactory(_ -> new TableCell<>() {
            @Override
            public void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_LEFT);
                setText(empty && Objects.isNull(item) ? null : item.getRole().getName());
            }
        });
        department.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        department.setCellFactory(_ -> new TableCell<>() {
            @Override
            public void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_LEFT);
                if (empty && Objects.isNull(item)) {
                    setGraphic(null);
                } else {
                    this.setAlignment(Pos.CENTER);
                    var employeeDepartment = new Text(item.getDepartmentName());
                    var employeeDesignation = new Text(item.getDesignationName());
                    employeeDesignation.getStyleClass().add(Styles.TEXT_MUTED);
                    var vbox = new VBox(employeeDepartment, employeeDesignation);
                    vbox.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(vbox);
                }
            }
        });
    }

    private Pagination buildPagination() {
        var pagination = new Pagination(EmployeeViewModel.getTotalPages(), 0);
        pagination.setMaxPageIndicatorCount(6);
        pagination.pageCountProperty().bindBidirectional(EmployeeViewModel.totalPagesProperty());
        pagination.setPageFactory(pageNum -> {
            progress.setManaged(true);
            progress.setVisible(true);
            EmployeeViewModel.getAllEmployees(() -> {
                progress.setManaged(false);
                progress.setVisible(false);
            }, null, pageNum, EmployeeViewModel.getPageSize());
            EmployeeViewModel.setPageNumber(pageNum);
            return new StackPane(); // null isn't allowed
        });
        return pagination;
    }

    private HBox buildPageSize() {
        var pageSize = new ComboBox<Integer>();
        pageSize.setItems(FXCollections.observableArrayList(25, 50, 75, 100));
        pageSize.valueProperty().bindBidirectional(EmployeeViewModel.pageSizeProperty().asObject());
        pageSize.valueProperty().addListener(
                (_, _, t1) -> {
                    progress.setManaged(true);
                    progress.setVisible(true);
                    EmployeeViewModel
                            .getAllEmployees(
                                    () -> {
                                        progress.setManaged(false);
                                        progress.setVisible(false);
                                    },
                                    null,
                                    EmployeeViewModel.getPageNumber(),
                                    t1);
                    EmployeeViewModel.setPageSize(t1);
                });
        var hbox = new HBox(10, new Text("Rows per page:"), pageSize);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }
}
