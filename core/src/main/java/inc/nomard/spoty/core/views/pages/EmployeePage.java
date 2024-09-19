package inc.nomard.spoty.core.views.pages;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.RoleViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.DepartmentViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.DesignationViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.EmployeeViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.EmploymentStatusViewModel;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.forms.EmployeeForm;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.layout.SideModalPane;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.navigation.Spacer;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
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
import javafx.util.Duration;
import lombok.extern.java.Log;

import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Log
public class EmployeePage extends OutlinePage {
    private final ModalPane modalPane;
    private TextField searchBar;
    private TableView<Employee> masterTable;
    private MFXProgressSpinner progress;
    private Button createBtn;
    private TableColumn<Employee, Employee> employeeName;
    private TableColumn<Employee, Employee> phone;
    private TableColumn<Employee, String> email;
    private TableColumn<Employee, Employee> employmentStatus;
    private TableColumn<Employee, Employee> department;
    private TableColumn<Employee, Employee> status;
    private TableColumn<Employee, String> workShift;
    private TableColumn<Employee, String> salary;
    private TableColumn<Employee, Employee> role;

    public EmployeePage() {
        super();
        modalPane = new SideModalPane();
        getChildren().addAll(modalPane, init());
        progress.setManaged(true);
        progress.setVisible(true);
        modalPane.displayProperty().addListener((observableValue, closed, open) -> {
            if (!open) {
                modalPane.setAlignment(Pos.CENTER);
                modalPane.usePredefinedTransitionFactories(null);
            }
        });

        CompletableFuture<Void> allDataInitialization = CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> DepartmentViewModel.getAllDepartments(null, null, null, null)),
                CompletableFuture.runAsync(() -> DesignationViewModel.getAllDesignations(null, null, null, null)),
                CompletableFuture.runAsync(() -> EmploymentStatusViewModel.getAllEmploymentStatuses(null, null, null, null)),
                CompletableFuture.runAsync(() -> RoleViewModel.getAllRoles(null, null, null, null)),
                CompletableFuture.runAsync(() -> EmployeeViewModel.getAllEmployees(null, null, null, null)));

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

    private VBox buildCenter() {
        masterTable = new TableView<>();
        VBox.setVgrow(masterTable, Priority.ALWAYS);
        HBox.setHgrow(masterTable, Priority.ALWAYS);
        var paging = new HBox(new Spacer(), buildPagination(), new Spacer(), buildPageSize());
        paging.setPadding(new Insets(0d, 20d, 0d, 5d));
        paging.setAlignment(Pos.CENTER);
        if (EmployeeViewModel.getTotalPages() > 0) {
            paging.setVisible(true);
            paging.setManaged(true);
        } else {
            paging.setVisible(false);
            paging.setManaged(false);
        }
        EmployeeViewModel.totalPagesProperty().addListener((observableValue, oldNum, newNum) -> {
            if (EmployeeViewModel.getTotalPages() > 0) {
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
        styleEmployeeTable();

        masterTable.setItems(EmployeeViewModel.getEMPLOYEES());
    }

    private void styleEmployeeTable() {
        masterTable.setPrefSize(1200, 1000);

        masterTable.setRowFactory(
                t -> {
                    TableRow<Employee> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<Employee>) event.getSource())
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

    private ContextMenu showContextMenu(TableRow<Employee> obj) {
        var contextMenu = new ContextMenu();
        var delete = new MenuItem("Delete");
        var edit = new MenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            EmployeeViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, SpotyUtils::successMessage, SpotyUtils::errorMessage);
            event.consume();
        }, obj.getItem().getName(), this));
        // Edit
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
        createBtn.setOnAction(event -> this.showDialog());
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
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
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
        employeeName.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Employee item, boolean empty) {
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
            public void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty && Objects.isNull(item) ? null : Objects.isNull(item.getCreatedBy()) ? null : item.getPhone());
            }
        });
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        employmentStatus.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        employmentStatus.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Employee item, boolean empty) {
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
            public void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && Objects.nonNull(item)) {
                    this.setAlignment(Pos.CENTER);

                    var chip = new Label(item.getIsActive());
                    chip.setAlignment(Pos.CENTER);
                    chip.setPadding(new Insets(5, 10, 5, 10));
                    chip.setPrefWidth(150);
                    chip.setStyle("-fx-foreground-color: white;"
                            + "-fx-background-radius: 50;"
                            + "-fx-border-radius: 50;");
                    if (item.isActive()) {
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
            public void updateItem(Employee item, boolean empty) {
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
            public void updateItem(Employee item, boolean empty) {
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

    private Pagination buildPagination() {
        var pagination = new Pagination(EmployeeViewModel.getTotalPages(), 0);
        pagination.setMaxPageIndicatorCount(5);
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

    private ComboBox<Integer> buildPageSize() {
        var pageSize = new ComboBox<Integer>();
        pageSize.setItems(FXCollections.observableArrayList(25, 50, 75, 100));
        pageSize.valueProperty().bindBidirectional(EmployeeViewModel.pageSizeProperty().asObject());
        pageSize.valueProperty().addListener(
                (observableValue, integer, t1) -> {
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
        return pageSize;
    }
}
