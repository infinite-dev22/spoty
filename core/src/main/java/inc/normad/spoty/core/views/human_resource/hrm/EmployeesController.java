package inc.normad.spoty.core.views.human_resource.hrm;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import inc.normad.spoty.network_bridge.dtos.hrm.employee.User;
import inc.normad.spoty.utils.SpotyThreader;
import inc.normad.spoty.core.viewModels.hrm.employee.UserViewModel;
import inc.normad.spoty.core.views.forms.UserFormController;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static inc.normad.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

public class EmployeesController implements Initializable {
    private static EmployeesController instance;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton importBtn;
    @FXML
    public MFXButton createBtn;
    @FXML
    public MFXTableView<User> masterTable;
    private MFXStageDialog dialog;

    private EmployeesController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        formDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static EmployeesController getInstance(Stage stage) {
        if (instance == null) instance = new EmployeesController(stage);
        return instance;
    }

    private void formDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/UserForm.fxml");
        fxmlLoader.setControllerFactory(c -> UserFormController.getInstance());

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(contentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    private void setupTable() {
        MFXTableColumn<User> employeeName =
                new MFXTableColumn<>("Name", false, Comparator.comparing(User::getName));
        MFXTableColumn<User> designation =
                new MFXTableColumn<>("Designation", false, Comparator.comparing(User::getDesignationName));
        MFXTableColumn<User> employmentStatus =
                new MFXTableColumn<>("Employment Status", false, Comparator.comparing(User::getEmploymentStatusName));
        MFXTableColumn<User> department =
                new MFXTableColumn<>("Department", false, Comparator.comparing(User::getDepartmentName));
        MFXTableColumn<User> workShift =
                new MFXTableColumn<>(
                        "Work Shift", false, Comparator.comparing(User::getWorkShift));
        MFXTableColumn<User> joiningDate =
                new MFXTableColumn<>("Joining Date", false, Comparator.comparing(User::getJoiningDate));
        MFXTableColumn<User> salary =
                new MFXTableColumn<>("Salary", false, Comparator.comparing(User::getSalary));

        employeeName.setRowCellFactory(employee -> new MFXTableRowCell<>(User::getName));
        designation.setRowCellFactory(employee -> new MFXTableRowCell<>(User::getDesignationName));
        employmentStatus.setRowCellFactory(employee -> new MFXTableRowCell<>(User::getEmploymentStatusName));
        department.setRowCellFactory(
                employee -> new MFXTableRowCell<>(User::getDepartmentName));
        workShift.setRowCellFactory(employee -> new MFXTableRowCell<>(User::getWorkShift));
        joiningDate.setRowCellFactory(employee -> new MFXTableRowCell<>(User::getJoiningDate));
        salary.setRowCellFactory(employee -> new MFXTableRowCell<>(User::getSalary));

        employeeName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        designation.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        employmentStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        department.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        workShift.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        joiningDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        salary.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));

        masterTable
                .getTableColumns()
                .addAll(employeeName, designation, employmentStatus, department, workShift, joiningDate, salary);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", User::getName),
                        new StringFilter<>("Designation", User::getDesignationName),
                        new StringFilter<>("Employment Status", User::getEmploymentStatusName),
                        new StringFilter<>("Department", User::getDepartmentName),
                        new StringFilter<>("Work Shift", User::getWorkShift));
        styleUserTable();

        if (UserViewModel.getUsers().isEmpty()) {
            UserViewModel.getUsers()
                    .addListener(
                            (ListChangeListener<User>)
                                    c -> masterTable.setItems(UserViewModel.getUsers()));
        } else {
            masterTable.itemsProperty().bindBidirectional(UserViewModel.usersProperty());
        }
    }

    private void styleUserTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<User> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<User>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<User> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            UserViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            UserViewModel.getItem(obj.getData().getId(), this::onAction, this::onFailed);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    dialog.showAndWait();
                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    public void createBtnClicked() {
        dialog.showAndWait();
    }

    private void onAction() {
        System.out.println("Loading employees...");
    }

    private void onSuccess() {
        System.out.println("Loaded employees...");
    }

    private void onFailed() {
        System.out.println("failed loading employees...");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(this::setupTable);
    }
}
