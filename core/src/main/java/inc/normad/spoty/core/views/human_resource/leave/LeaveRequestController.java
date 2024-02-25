package inc.normad.spoty.core.views.human_resource.leave;

import inc.normad.spoty.core.views.forms.LeaveStatusFormController;
import inc.normad.spoty.network_bridge.dtos.hrm.leave.LeaveStatus;
import inc.normad.spoty.utils.SpotyThreader;
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
import inc.normad.spoty.core.viewModels.hrm.leave.LeaveStatusViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static inc.normad.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

public class LeaveRequestController implements Initializable {
    private static LeaveRequestController instance;
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
    public MFXTableView<LeaveStatus> masterTable;
    private MFXStageDialog dialog;

    private LeaveRequestController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        formDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static LeaveRequestController getInstance(Stage stage) {
        if (instance == null) instance = new LeaveRequestController(stage);
        return instance;
    }

    private void formDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/LeaveStatusForm.fxml");
        fxmlLoader.setControllerFactory(c -> LeaveStatusFormController.getInstance());

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
        MFXTableColumn<LeaveStatus> employeeName =
                new MFXTableColumn<>("Employee Name", false, Comparator.comparing(LeaveStatus::getEmployeeName));
        MFXTableColumn<LeaveStatus> designation =
                new MFXTableColumn<>("Designation", false, Comparator.comparing(LeaveStatus::getDesignationName));
        MFXTableColumn<LeaveStatus> dateAndTime =
                new MFXTableColumn<>("Date & Time", false, Comparator.comparing(LeaveStatus::getStartDate));
        MFXTableColumn<LeaveStatus> duration =
                new MFXTableColumn<>("Duration", false, Comparator.comparing(LeaveStatus::getDuration));
        MFXTableColumn<LeaveStatus> leaveType =
                new MFXTableColumn<>(
                        "Leave Type", false, Comparator.comparing(LeaveStatus::getLeaveTypeName));
        MFXTableColumn<LeaveStatus> attachment =
                new MFXTableColumn<>("Attachment", false, Comparator.comparing(LeaveStatus::getAttachment));
        MFXTableColumn<LeaveStatus> status =
                new MFXTableColumn<>("Status", false, Comparator.comparing(LeaveStatus::getStatusName));

        employeeName.setRowCellFactory(employee -> new MFXTableRowCell<>(LeaveStatus::getEmployeeName));
        designation.setRowCellFactory(employee -> new MFXTableRowCell<>(LeaveStatus::getDesignationName));
        dateAndTime.setRowCellFactory(employee -> new MFXTableRowCell<>(LeaveStatus::getStartDate));
        duration.setRowCellFactory(
                employee -> new MFXTableRowCell<>(LeaveStatus::getLocaleDuration));
        leaveType.setRowCellFactory(employee -> new MFXTableRowCell<>(LeaveStatus::getLeaveTypeName));
        attachment.setRowCellFactory(employee -> new MFXTableRowCell<>(LeaveStatus::getAttachment));

        employeeName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        designation.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        dateAndTime.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        duration.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        leaveType.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        attachment.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));

        masterTable
                .getTableColumns()
                .addAll(employeeName, designation, dateAndTime, duration, leaveType, attachment, status);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Employee Name", LeaveStatus::getEmployeeName),
                        new StringFilter<>("Designation", LeaveStatus::getDesignationName),
//                        new StringFilter<>("Start On/At", LeaveStatus::getStartDate),
                        new StringFilter<>("Status", LeaveStatus::getStatusName),
                        new StringFilter<>("Duration", LeaveStatus::getLocaleDuration));
        styleLeaveStatusTable();

        if (LeaveStatusViewModel.getLeaveStatuses().isEmpty()) {
            LeaveStatusViewModel.getLeaveStatuses()
                    .addListener(
                            (ListChangeListener<LeaveStatus>)
                                    c -> masterTable.setItems(LeaveStatusViewModel.getLeaveStatuses()));
        } else {
            masterTable.itemsProperty().bindBidirectional(LeaveStatusViewModel.leaveStatusProperty());
        }
    }

    private void styleLeaveStatusTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<LeaveStatus> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<LeaveStatus>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<LeaveStatus> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            LeaveStatusViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
                            LeaveStatusViewModel.getItem(obj.getData().getId(), this::onAction, this::onFailed);
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
