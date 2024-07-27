package inc.nomard.spoty.core.views.pages;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.viewModels.hrm.pay_roll.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import java.io.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class SalaryPage extends OutlinePage {
    private TextField searchBar;
    private TableView<Salary> masterTable;
    private MFXProgressSpinner progress;
    private MFXStageDialog viewDialog;
    private FXMLLoader viewFxmlLoader;
    private TableColumn<Salary, Salary> payslip;
    private TableColumn<Salary, Salary> employee;
    private TableColumn<Salary, String> status;
    private TableColumn<Salary, String> salary;
    private TableColumn<Salary, String> netSalary;

    public SalaryPage() {
        try {
            salaryViewDialogPane();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        addNode(init());
        progress.setManaged(true);
        progress.setVisible(true);
        SalaryViewModel.getAllSalaries(this::onDataInitializationSuccess, this::errorMessage);
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

    private AnchorPane buildCenter() {
        masterTable = new TableView<>();
        NodeUtils.setAnchors(masterTable, new Insets(0d));
        return new AnchorPane(masterTable);
    }

    private void salaryViewDialogPane() throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/SalaryPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new SalaryPreviewController());

        MFXGenericDialog dialogContent = viewFxmlLoader.load();
        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setShowClose(false);

        dialogContent.setPrefHeight(screenHeight * .98);
        dialogContent.setPrefWidth(700);
        viewDialog = SpotyDialog.createDialog(dialogContent, this);
    }

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
                salaryViewShow(obj.getItem());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            event.consume();
        });
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            SalaryViewModel.deleteSalary(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getEmployeeName() + "'s salary", this));
        contextMenu.getItems().addAll(view, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    private void onSuccess() {
        SalaryViewModel.getAllSalaries(null, null);
    }

    public void salaryViewShow(Salary salary) {
        SalaryPreviewController controller = viewFxmlLoader.getController();
        controller.init(salary);
        viewDialog.showAndWait();
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

    public void createBtnAction() {
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                SalaryViewModel.getAllSalaries(null, null);
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
                var employeeName = new Label(item.getEmployee().getName());
                var employeeDesignation = new Label(item.getEmployee().getDesignation().getName());
                employeeDesignation.getStyleClass().add(Styles.TEXT_MUTED);
                var vbox = new VBox(employeeName, employeeDesignation);
                setGraphic(vbox);
                setText(null);
            }
        });
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        netSalary.setCellValueFactory(new PropertyValueFactory<>("netSalary"));
    }
}
