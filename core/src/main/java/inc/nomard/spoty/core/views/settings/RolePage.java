package inc.nomard.spoty.core.views.settings;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.RoleViewModel;
import inc.nomard.spoty.core.views.forms.RoleFormController;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.SpotyDialog;
import inc.nomard.spoty.core.views.layout.message.SpotyMessage;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import inc.nomard.spoty.core.views.util.NodeUtils;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.network_bridge.dtos.Role;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Duration;
import lombok.extern.java.Log;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.io.IOException;
import java.util.Comparator;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

@SuppressWarnings("unchecked")
@Log
public class RolePage extends OutlinePage {
    private CustomTextField searchBar;
    private MFXTableView<Role> masterTable;
    private Button createBtn;
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
        searchBar = new CustomTextField();
        searchBar.setPromptText("Search accounts");
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
        createBtn.setDefaultButton(true);
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

    private AnchorPane buildCenter() {
        masterTable = new MFXTableView<>();
        NodeUtils.setAnchors(masterTable, new Insets(0d));
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
        masterTable.autosizeColumnsOnInitialization();

//        masterTable.setTableRowFactory(
//                t -> {
//                    MFXTableRow<Role> row = new MFXTableRow<>(masterTable, t);
//                    EventHandler<ContextMenuEvent> eventHandler =
//                            event ->
//                                    showContextMenu((MFXTableRow<Role>) event.getSource())
//                                            .show(
//                                                    this.getScene().getWindow(),
//                                                    event.getScreenX(),
//                                                    event.getScreenY());
//                    row.setOnContextMenuRequested(eventHandler);
//                    return row;
//                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<Role> obj) {
        var contextMenu = new MFXContextMenu(masterTable);
        var delete = new MFXContextMenuItem("Delete");
        var edit = new MFXContextMenuItem("Edit");
        // Actions
        // Delete
//        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
//            RoleViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
//            event.consume();
//        }, obj.getData().getName(), this));
//        // Edit
//        edit.setOnAction(
//                e -> {
//                    RoleViewModel.getItem(obj.getData().getId(), this::createBtnAction, this::errorMessage);
//                    e.consume();
//                });
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
        RoleViewModel.getAllRoles(null, null, null, null);
    }

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, FontAwesomeSolid.CHECK_CIRCLE);
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, FontAwesomeSolid.EXCLAMATION_TRIANGLE);
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

    private void setIcons() {
        searchBar.setRight(new MFXFontIcon("fas-magnifying-glass"));
    }
}
