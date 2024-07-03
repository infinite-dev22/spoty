package inc.nomard.spoty.core.views.settings;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxcomponents.theming.enums.*;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class RolePage extends OutlinePage {
    private MFXTextField searchBar;
    private MFXTableView<Role> masterTable;
    private MFXButton createBtn;
    private MFXStageDialog dialog;

    public RolePage() {
        Platform.runLater(
                () -> {
                    try {
                        customerFormDialogPane();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
        addNode(init());
    }

    public BorderPane init() {
        var pane = new BorderPane();
        pane.setTop(buildTop());
        pane.setCenter(buildCenter());
        setIcons();
        setupTable();
        createBtnAction();
        return pane;
    }

    private HBox buildLeftTop() {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildCenterTop() {
        searchBar = new MFXTextField();
        searchBar.setPromptText("Search accounts");
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
        createBtn.setVariants(ButtonVariants.FILLED);
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
        MFXTableColumn<Role> roleMasterRole =
                new MFXTableColumn<>("Name", true, Comparator.comparing(Role::getName));
        MFXTableColumn<Role> roleMasterDescription =
                new MFXTableColumn<>("Description", true, Comparator.comparing(Role::getDescription));

        roleMasterRole.setRowCellFactory(roleMaster -> new MFXTableRowCell<>(Role::getName));
        roleMasterDescription.setRowCellFactory(
                roleMaster -> new MFXTableRowCell<>(Role::getDescription));

        roleMasterRole.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));
        roleMasterDescription.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));

        masterTable.getTableColumns().addAll(roleMasterRole, roleMasterDescription);

        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", Role::getName),
                        new StringFilter<>("Description", Role::getDescription));

        getRoleTable();

        if (RoleViewModel.getRoles().isEmpty()) {
            RoleViewModel.getRoles()
                    .addListener(
                            (ListChangeListener<Role>) c -> masterTable.setItems(RoleViewModel.getRoles()));
        } else {
            masterTable.itemsProperty().bindBidirectional(RoleViewModel.rolesProperty());
        }
    }

    private void getRoleTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.autosizeColumnsOnInitialization();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Role> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event ->
                                    showContextMenu((MFXTableRow<Role>) event.getSource())
                                            .show(
                                                    this.getScene().getWindow(),
                                                    event.getScreenX(),
                                                    event.getScreenY());
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<Role> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            RoleViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getName(), this));
        // Edit
        edit.setOnAction(
                e -> {
                    RoleViewModel.getItem(obj.getData().getId(), this::createBtnAction, this::errorMessage);
                    e.consume();
                });
        contextMenu.addItems(edit, delete);
        return contextMenu;
    }

    private void customerFormDialogPane() throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/RoleForm.fxml");
        fxmlLoader.setControllerFactory(c -> new RoleFormController());
        MFXGenericDialog dialogContent = fxmlLoader.load();
        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setShowClose(false);
        dialog = SpotyDialog.createDialog(dialogContent, this);
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> dialog.showAndWait());
    }

    private void onSuccess() {
        RoleViewModel.getAllRoles(null, null);
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

    private void setIcons() {
        searchBar.setTrailingIcon(new MFXFontIcon("fas-magnifying-glass"));
    }
}
