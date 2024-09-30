package inc.nomard.spoty.core.views.pages;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.hrm.pay_roll.SalaryViewModel;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.message.SpotyMessage;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import inc.nomard.spoty.core.views.previews.SalaryPreviewController;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.Salary;
import inc.nomard.spoty.utils.navigation.Spacer;
import inc.nomard.spoty.core.views.components.SpotyProgressSpinner;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.util.Duration;
import lombok.extern.java.Log;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

@Log
public class SalaryPage extends OutlinePage {
    private TextField searchBar;
    private TableView<Salary> masterTable;
    private SpotyProgressSpinner progress;
    private FXMLLoader viewFxmlLoader;
    private TableColumn<Salary, Salary> payslip;
    private TableColumn<Salary, Salary> employee;
    private TableColumn<Salary, String> status;
    private TableColumn<Salary, String> salary;
    private TableColumn<Salary, String> netSalary;

    public SalaryPage() {
        addNode(init());
        progress.setManaged(true);
        progress.setVisible(true);
        SalaryViewModel.getAllSalaries(this::onDataInitializationSuccess, this::errorMessage, null, null);
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
        searchBar.setPromptText("Search salaries");
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
        if (SalaryViewModel.getTotalPages() > 0) {
            paging.setVisible(true);
            paging.setManaged(true);
        } else {
            paging.setVisible(false);
            paging.setManaged(false);
        }
        SalaryViewModel.totalPagesProperty().addListener((observableValue, oldNum, newNum) -> {
            if (SalaryViewModel.getTotalPages() > 0) {
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

//    private void salaryViewDialogPane() throws IOException {
//        double screenHeight = Screen.getPrimary().getBounds().getHeight();
//        viewFxmlLoader = fxmlLoader("views/previews/SalaryPreview.fxml");
//        viewFxmlLoader.setControllerFactory(c -> new SalaryPreviewController());
//
//        MFXGenericDialog dialogContent = viewFxmlLoader.load();
//        dialogContent.setShowMinimize(false);
//        dialogContent.setShowAlwaysOnTop(false);
//        dialogContent.setShowClose(false);
//
//        dialogContent.setPrefHeight(screenHeight * .98);
//        dialogContent.setPrefWidth(700);
////        viewDialog = SpotyDialog.createDialog(dialogContent).showDialog();
//    }

    private void setupTable() {
        payslip = new TableColumn<>("Status");
        employee = new TableColumn<>("Employee");
        status = new TableColumn<>("Period");
        salary = new TableColumn<>("Salary");
        netSalary = new TableColumn<>("Net Salary");

        employee.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        status.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        payslip.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        salary.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        netSalary.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(employee, status, payslip, salary, netSalary).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);
        styleTable();

        masterTable.setItems(SalaryViewModel.getSalaries());
    }

    private void styleTable() {
        masterTable.setPrefSize(1000, 1000);

        masterTable.setRowFactory(
                t -> {
                    TableRow<Salary> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<Salary>) event.getSource())
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

    private ContextMenu showContextMenu(TableRow<Salary> obj) {
        var contextMenu = new ContextMenu();
        var view = new MenuItem("View");
        var delete = new MenuItem("Delete");

        // Actions
        // View
        view.setOnAction(event -> {
            try {
//                salaryViewShow(obj.getItem());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            event.consume();
        });
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(AppManager.getGlobalModalPane(), () -> {
            SalaryViewModel.deleteSalary(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getEmployeeName() + "'s salary").showDialog());
        contextMenu.getItems().addAll(view, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    private void onSuccess() {
        SalaryViewModel.getAllSalaries(null, null, null, null);
    }

//    public void salaryViewShow(Salary salary) {
//        SalaryPreviewController controller = viewFxmlLoader.getController();
//        controller.init(salary);
//        viewDialog.showAndWait();
//    }

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

    public void createBtnAction() {
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                SalaryViewModel.getAllSalaries(null, null, null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            SalaryViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }

    private void setupTableColumns() {
        payslip.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        payslip.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Salary item, boolean empty) {
                super.updateItem(item, empty);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());
                setText(empty || Objects.isNull(item) ? null : item.getPaySlip().getStartDate().format(dtf) + " - " + item.getPaySlip().getEndDate().format(dtf));
            }
        });
        employee.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        employee.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Salary item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && !Objects.isNull(item)) {
                    var employeeName = new Label(item.getEmployee().getName());
                    var employeeDesignation = new Label(item.getEmployee().getDesignation().getName());
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
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        netSalary.setCellValueFactory(new PropertyValueFactory<>("netSalary"));
    }

    private Pagination buildPagination() {
        var pagination = new Pagination(SalaryViewModel.getTotalPages(), 0);
        pagination.setMaxPageIndicatorCount(5);
        pagination.pageCountProperty().bindBidirectional(SalaryViewModel.totalPagesProperty());
        pagination.setPageFactory(pageNum -> {
            progress.setManaged(true);
            progress.setVisible(true);
            SalaryViewModel.getAllSalaries(() -> {
                progress.setManaged(false);
                progress.setVisible(false);
            }, null, pageNum, SalaryViewModel.getPageSize());
            SalaryViewModel.setPageNumber(pageNum);
            return new StackPane(); // null isn't allowed
        });
        return pagination;
    }

    private ComboBox<Integer> buildPageSize() {
        var pageSize = new ComboBox<Integer>();
        pageSize.setItems(FXCollections.observableArrayList(25, 50, 75, 100));
        pageSize.valueProperty().bindBidirectional(SalaryViewModel.pageSizeProperty().asObject());
        pageSize.valueProperty().addListener(
                (observableValue, integer, t1) -> {
                    progress.setManaged(true);
                    progress.setVisible(true);
                    SalaryViewModel
                            .getAllSalaries(
                                    () -> {
                                        progress.setManaged(false);
                                        progress.setVisible(false);
                                    },
                                    null,
                                    SalaryViewModel.getPageNumber(),
                                    t1);
                    SalaryViewModel.setPageSize(t1);
                });
        return pageSize;
    }
}
