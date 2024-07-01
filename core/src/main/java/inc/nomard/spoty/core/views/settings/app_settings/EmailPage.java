package inc.nomard.spoty.core.views.settings.app_settings;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.forms.*;
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
public class EmailPage extends OutlinePage {
    private final Stage stage;
    private MFXTextField searchBar;
    private MFXTableView<Account> masterTable;
    private MFXProgressSpinner progress;
    private MFXButton createBtn;
    private MFXStageDialog dialog;

    public EmailPage(Stage stage) {
        this.stage = stage;
        addNode(init());
    }

    public BorderPane init() {
        var pane = new BorderPane();
        pane.setTop(buildTop());
        pane.setCenter(buildCenter());
        setIcons();
        setSearchBar();
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

    public BorderPane buildCenter() {
        var anchorPane = new AnchorPane();
        anchorPane.getChildren().addAll(
                buildEmailsTable()
        );

        BorderPane.setAlignment(anchorPane, Pos.CENTER);

        this.setPrefHeight(600.0);
        this.setPrefWidth(600.0);
        var pane = new BorderPane();
        pane.setCenter(anchorPane);
        createBtnAction();
        return pane;
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

    public void createBtnAction() {
        createBtn.setOnAction(event -> dialog.showAndWait());
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

    private void setIcons() {
        searchBar.setTrailingIcon(new MFXFontIcon("fas-magnifying-glass"));
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                CurrencyViewModel.getAllCurrencies(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            CurrencyViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
            }, this::errorMessage);
        });
    }
}
