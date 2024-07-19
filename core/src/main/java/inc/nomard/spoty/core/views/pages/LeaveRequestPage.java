package inc.nomard.spoty.core.views.pages;

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
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxresources.fonts.*;
import java.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class LeaveRequestPage extends OutlinePage {
    private MFXTextField searchBar;
    private MFXTableView<LeaveStatus> masterTable;
    private MFXProgressSpinner progress;
    private MFXButton createBtn;

    public LeaveRequestPage() {
        addNode(init());
    }

    public BorderPane init() {
        var pane = new BorderPane();
        pane.setTop(buildTop());
        pane.setCenter(buildCenter());
        setIcons();
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
        searchBar = new MFXTextField();
        searchBar.setPromptText("Search leave requests");
        searchBar.setFloatMode(FloatMode.DISABLED);
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
        createBtn = new MFXButton("Create");
        createBtn.getStyleClass().add("filled");
        var hbox = new HBox(createBtn);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildTop() {
        var hbox = new HBox();
        hbox.getStyleClass().add("card-flat");
        BorderPane.setAlignment(hbox, Pos.CENTER);
        hbox.setPadding(new Insets(5d));
        hbox.getChildren().addAll(buildLeftTop(), buildCenterTop(), buildRightTop());
        return hbox;
    }

    private AnchorPane buildCenter() {
        masterTable = new MFXTableView<>();
        AnchorPane.setBottomAnchor(masterTable, 0d);
        AnchorPane.setLeftAnchor(masterTable, 0d);
        AnchorPane.setRightAnchor(masterTable, 0d);
        AnchorPane.setTopAnchor(masterTable, 10d);
        return new AnchorPane(masterTable);
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
                        "Leave Type", false, Comparator.comparing(LeaveStatus::getLeaveType));
        MFXTableColumn<LeaveStatus> attachment =
                new MFXTableColumn<>("Attachment", false, Comparator.comparing(LeaveStatus::getAttachment));
        MFXTableColumn<LeaveStatus> status =
                new MFXTableColumn<>("Status", false, Comparator.comparing(LeaveStatus::getStatusName));

        employeeName.setRowCellFactory(employee -> new MFXTableRowCell<>(LeaveStatus::getEmployeeName));
        designation.setRowCellFactory(employee -> new MFXTableRowCell<>(LeaveStatus::getDesignationName));
        dateAndTime.setRowCellFactory(employee -> new MFXTableRowCell<>(LeaveStatus::getStartDate));
        duration.setRowCellFactory(
                employee -> new MFXTableRowCell<>(LeaveStatus::getLocaleDuration));
        leaveType.setRowCellFactory(employee -> new MFXTableRowCell<>(LeaveStatus::getLeaveType));
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
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            LeaveStatusViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getEmployeeName() + "'s leave request", this));
        // Edit
        edit.setOnAction(
                e -> {
                    LeaveStatusViewModel.getItem(obj.getData().getId(), () -> SpotyDialog.createDialog(new LeaveRequestForm(), this).showAndWait(), this::errorMessage);
                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> SpotyDialog.createDialog(new LeaveRequestForm(), this).showAndWait());
    }

    private void onSuccess() {
        LeaveStatusViewModel.getAllLeaveStatuses(null, null);
    }

    private void setIcons() {
        searchBar.setTrailingIcon(new MFXFontIcon("fas-magnifying-glass"));
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
}