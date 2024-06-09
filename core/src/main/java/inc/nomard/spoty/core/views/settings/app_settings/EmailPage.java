package inc.nomard.spoty.core.views.settings.app_settings;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxcomponents.theming.enums.*;
import java.io.*;
import java.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class EmailPage extends BorderPane {
    private final Stage stage = new Stage();
    private MFXStageDialog dialog;

    public EmailPage() {
        init();
    }

    private HBox buildBreadcrumbHolder() {
        var breadCrumbHolder = new HBox();
        breadCrumbHolder.setAlignment(Pos.CENTER);
        breadCrumbHolder.setPadding(new Insets(0.0, 10.0, 0.0, 10.0));

        AnchorPane.setTopAnchor(breadCrumbHolder, 0.0);
        AnchorPane.setLeftAnchor(breadCrumbHolder, 0.0);
        AnchorPane.setBottomAnchor(breadCrumbHolder, 0.0);

        return breadCrumbHolder;
    }

    private MFXTextField buildSearchField() {
        var searchField = new MFXTextField();
        searchField.setFloatMode(FloatMode.DISABLED);
        searchField.setPromptText("Search");
        searchField.setPrefWidth(300.0);

        AnchorPane.setTopAnchor(searchField, 0.0);
        AnchorPane.setLeftAnchor(searchField, 50.0);
        AnchorPane.setBottomAnchor(searchField, 0.0);

        return searchField;
    }

    private HBox buildActions() {
        var actions = new HBox();
        actions.setAlignment(Pos.CENTER_RIGHT);
        actions.setSpacing(20.0);
        actions.setPadding(new Insets(0.0, 10.0, 0.0, 10.0));

        var createBtn = new MFXButton();
        createBtn.setText("Create");
        createBtn.setVariants(ButtonVariants.FILLED);

        actions.getChildren().add(createBtn);

        AnchorPane.setTopAnchor(actions, 0.0);
        AnchorPane.setRightAnchor(actions, 0.0);
        AnchorPane.setBottomAnchor(actions, 0.0);

        return actions;
    }

    private AnchorPane buildActionsPane() {
        var anchorPane = new AnchorPane();
        anchorPane.setPadding(new Insets(5.0));
        anchorPane.getStyleClass().add("card-flat");

        AnchorPane.setTopAnchor(anchorPane, 0.0);
        AnchorPane.setLeftAnchor(anchorPane, 0.0);
        AnchorPane.setRightAnchor(anchorPane, 0.0);

        BorderPane.setAlignment(anchorPane, Pos.CENTER);

        anchorPane.getChildren().addAll(
                buildBreadcrumbHolder(),
                buildSearchField(),
                buildActions()
        );

        return anchorPane;
    }

    private void init() {
        var anchorPane = new AnchorPane();
        anchorPane.getChildren().addAll(
                buildActionsPane(),
                buildEmailsTable()
        );

        BorderPane.setAlignment(anchorPane, Pos.CENTER);

        this.setPrefHeight(600.0);
        this.setPrefWidth(600.0);
        this.setCenter(anchorPane);
    }

    private MFXTableView<Email> buildEmailsTable() {
        var dataTable = new MFXTableView<Email>();

        AnchorPane.setTopAnchor(dataTable, 50.0);
        AnchorPane.setLeftAnchor(dataTable, 0.0);
        AnchorPane.setRightAnchor(dataTable, 0.0);
        AnchorPane.setBottomAnchor(dataTable, 0.0);

        styleEmailTable(dataTable);

        MFXTableColumn<Email> name =
                new MFXTableColumn<>("Name", false, Comparator.comparing(Email::getName));
        MFXTableColumn<Email> description =
                new MFXTableColumn<>("Description", false, Comparator.comparing(Email::getDescription));
        MFXTableColumn<Email> usage =
                new MFXTableColumn<>("Template Usage", false, Comparator.comparing(Email::getUsage));

        name.setRowCellFactory(customer -> new MFXTableRowCell<>(Email::getName));
        description.setRowCellFactory(customer -> new MFXTableRowCell<>(Email::getDescription));
        usage.setRowCellFactory(customer -> new MFXTableRowCell<>(Email::getUsage));

        name.prefWidthProperty().bind(dataTable.widthProperty().multiply(.1));
        description.prefWidthProperty().bind(dataTable.widthProperty().multiply(.3));
        usage.prefWidthProperty().bind(dataTable.widthProperty().multiply(.3));

        dataTable
                .getTableColumns()
                .addAll(name, description, usage);
        dataTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Email Name", Email::getName),
                        new StringFilter<>("A/C Name", Email::getDescription),
                        new StringFilter<>("A/C Number", Email::getUsage));

        if (EmailViewModel.getEmails().isEmpty()) {
            EmailViewModel.getEmails().addListener(
                    (ListChangeListener<Email>)
                            c -> dataTable.setItems(EmailViewModel.emailsList));
        } else {
            dataTable.itemsProperty().bindBidirectional(EmailViewModel.emailsProperty());
        }

        return dataTable;
    }

    private void styleEmailTable(MFXTableView<Email> dataTable) {
        dataTable.setPrefSize(1000, 1000);
        dataTable.features().enableBounceEffect();
        dataTable.features().enableSmoothScrolling(0.5);

        dataTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Email> row = new MFXTableRow<>(dataTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu(dataTable, (MFXTableRow<Email>) event.getSource())
                                        .show(
                                                dataTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableView<Email> dataTable, MFXTableRow<Email> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(dataTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    EmailViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    EmailViewModel.getItem(obj.getData().getId(), () -> dialog.showAndWait(), this::errorMessage);
                    e.consume();
                });
        contextMenu.addItems(edit, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    private void customerFormDialogPane() throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/EmailForm.fxml");
        fxmlLoader.setControllerFactory(c -> EmailFormController.getInstance(stage));
        MFXGenericDialog dialogContent = fxmlLoader.load();
        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(this.getParent().getScene().getWindow())
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(this)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();
        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void createBtnClicked() {
        dialog.showAndWait();
    }

    private void onSuccess() {
        EmailViewModel.getAllEmails(null, null);
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
