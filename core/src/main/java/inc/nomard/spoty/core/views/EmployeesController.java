package inc.nomard.spoty.core.views;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.hrm.employee.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class EmployeesController implements Initializable {
    private static EmployeesController instance;
    private final Stage stage;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public MFXTableView<User> masterTable;
    @FXML
    public HBox leftHeaderPane;
    @FXML
    public MFXProgressSpinner progress;
    private MFXStageDialog dialog;

    private EmployeesController(Stage stage) {
        this.stage = stage;
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
        fxmlLoader.setControllerFactory(c -> UserFormController.getInstance(stage));

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setShowClose(false);

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
        MFXTableColumn<User> phone =
                new MFXTableColumn<>("Phone", false, Comparator.comparing(User::getPhone));
        MFXTableColumn<User> email =
                new MFXTableColumn<>("Email", false, Comparator.comparing(User::getEmail));
        MFXTableColumn<User> employmentStatus =
                new MFXTableColumn<>("Employment Status", false, Comparator.comparing(User::getEmploymentStatusName));
        MFXTableColumn<User> status =
                new MFXTableColumn<>("Status", false, Comparator.comparing(User::getActive));
        MFXTableColumn<User> workShift =
                new MFXTableColumn<>(
                        "Work Shift", false, Comparator.comparing(User::getWorkShift));

        employeeName.setRowCellFactory(employee -> new MFXTableRowCell<>(User::getName));
        email.setRowCellFactory(employee -> new MFXTableRowCell<>(User::getEmail));
        phone.setRowCellFactory(employee -> new MFXTableRowCell<>(User::getPhone));
        employmentStatus.setRowCellFactory(employee -> new MFXTableRowCell<>(User::getEmploymentStatusName));
        status.setRowCellFactory(
                employee -> new MFXTableRowCell<>(User::getActive));
        workShift.setRowCellFactory(employee -> new MFXTableRowCell<>(User::getWorkShift));

        employeeName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.35));
        email.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        phone.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        employmentStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        status.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        workShift.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));

        masterTable
                .getTableColumns()
                .addAll(employeeName, phone, email, employmentStatus, status, workShift);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", User::getName),
                        new StringFilter<>("Phone", User::getPhone),
                        new StringFilter<>("Email", User::getEmail),
                        new StringFilter<>("Employment Status", User::getEmploymentStatusName),
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
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            UserViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, stage, contentPane));
        // Edit
        edit.setOnAction(
                e -> {
                    UserViewModel.getItem(obj.getData().getId(), () -> dialog.showAndWait(), this::errorMessage);
                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    public void createBtnClicked() {
        dialog.showAndWait();
    }

    private void onSuccess() {
        UserViewModel.getAllUsers(null, null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
        setSearchBar();
        Platform.runLater(this::setupTable);
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
                UserViewModel.getAllUsers(null, null);
            }
            progress.setVisible(true);
            UserViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
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
        if (!BaseController.getInstance(stage).morphPane.getChildren().contains(notification)) {
            BaseController.getInstance(stage).morphPane.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification, stage));
        }
    }
}
