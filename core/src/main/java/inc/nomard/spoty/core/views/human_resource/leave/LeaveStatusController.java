package inc.nomard.spoty.core.views.human_resource.leave;

import inc.nomard.spoty.core.components.animations.SpotyAnimations;
import inc.nomard.spoty.core.viewModels.BankViewModel;
import inc.nomard.spoty.core.viewModels.hrm.leave.LeaveStatusViewModel;
import inc.nomard.spoty.core.views.forms.LeaveStatusFormController;
import inc.nomard.spoty.network_bridge.dtos.hrm.leave.LeaveStatus;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

public class LeaveStatusController implements Initializable {
    private static LeaveStatusController instance;
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
    @FXML
    public HBox refresh;
    private MFXStageDialog dialog;
    private RotateTransition transition;

    private LeaveStatusController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        formDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static LeaveStatusController getInstance(Stage stage) {
        if (instance == null) instance = new LeaveStatusController(stage);
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
                .addAll(employeeName, designation, dateAndTime, duration, leaveType, attachment);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Employee Name", LeaveStatus::getEmployeeName),
                        new StringFilter<>("Designation", LeaveStatus::getDesignationName),
//                        new StringFilter<>("Start On/At", LeaveStatus::getStartDate),
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
                            LeaveStatusViewModel.getItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
        transition.playFromStart();
        transition.setOnFinished((ActionEvent event) -> transition.playFromStart());
    }

    private void onSuccess() {
        transition.setOnFinished(null);
    }

    private void onFailed() {
        transition.setOnFinished(null);
    }

    private void setIcons() {
        var refreshIcon = new MFXFontIcon("fas-arrows-rotate", 24);
        refresh.getChildren().addFirst(refreshIcon);

        transition = SpotyAnimations.rotateTransition(refreshIcon, Duration.millis(1000), 360);

        refreshIcon.setOnMouseClicked(mouseEvent -> LeaveStatusViewModel.getAllLeaveStatuses(this::onAction, this::onSuccess, this::onFailed));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
        Platform.runLater(this::setupTable);
    }
}