package inc.nomard.spoty.core.views;

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
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class UnitOfMeasurePage extends OutlinePage {
    private MFXTextField searchBar;
    private MFXTableView<UnitOfMeasure> masterTable;
    private MFXProgressSpinner progress;
    private MFXButton createBtn;
    private MFXStageDialog dialog;

    public UnitOfMeasurePage() {
        Platform.runLater(
                () -> {
                    try {
                        uomFormDialogPane();
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
        MFXTableColumn<UnitOfMeasure> uomName =
                new MFXTableColumn<>("Name", false, Comparator.comparing(UnitOfMeasure::getName));
        MFXTableColumn<UnitOfMeasure> uomShortName =
                new MFXTableColumn<>(
                        "Short Name", false, Comparator.comparing(UnitOfMeasure::getShortName));
        MFXTableColumn<UnitOfMeasure> uomBaseUnit =
                new MFXTableColumn<>(
                        "Base Unit", false, Comparator.comparing(UnitOfMeasure::getBaseUnitName));
        MFXTableColumn<UnitOfMeasure> uomOperator =
                new MFXTableColumn<>("Operator", false, Comparator.comparing(UnitOfMeasure::getOperator));
        MFXTableColumn<UnitOfMeasure> uomOperationValue =
                new MFXTableColumn<>(
                        "Operation Value", false, Comparator.comparing(UnitOfMeasure::getOperatorValue));

        uomName.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getName));
        uomShortName.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getShortName));
        uomBaseUnit.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getBaseUnitName));
        uomOperator.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getOperator));
        uomOperationValue.setRowCellFactory(
                brand -> new MFXTableRowCell<>(UnitOfMeasure::getOperatorValue));

        uomName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        uomShortName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        uomBaseUnit.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        uomOperator.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        uomOperationValue.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));

        masterTable
                .getTableColumns()
                .addAll(uomName, uomShortName, uomBaseUnit, uomOperator, uomOperationValue);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", UnitOfMeasure::getName),
                        new StringFilter<>("Short Name", UnitOfMeasure::getShortName),
                        new StringFilter<>("Base Unit", UnitOfMeasure::getBaseUnitName),
                        new StringFilter<>("Operator", UnitOfMeasure::getOperator),
                        new DoubleFilter<>("Operation Value", UnitOfMeasure::getOperatorValue));
        getUnitOfMeasureTable();

        if (UOMViewModel.getUnitsOfMeasure().isEmpty()) {
            UOMViewModel.getUnitsOfMeasure()
                    .addListener(
                            (ListChangeListener<UnitOfMeasure>)
                                    c -> masterTable.setItems(UOMViewModel.getUnitsOfMeasure()));
        } else {
            masterTable.itemsProperty().bindBidirectional(UOMViewModel.unitsOfMeasureProperty());
        }
    }

    private void getUnitOfMeasureTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<UnitOfMeasure> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<UnitOfMeasure>) event.getSource())
                                        .show(masterTable.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<UnitOfMeasure> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            UOMViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getName(), this));
        // Edit
        edit.setOnAction(
                e -> {
                    UOMViewModel.getItem(obj.getData().getId(), () -> dialog.showAndWait(), this::errorMessage);
                    e.consume();
                });
        contextMenu.addItems(edit, delete);
        return contextMenu;
    }

    private void uomFormDialogPane() throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/UOMForm.fxml");
        fxmlLoader.setControllerFactory(c -> new UOMFormController());

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
        UOMViewModel.getAllUOMs(null, null);
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

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                UOMViewModel.getAllUOMs(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            UOMViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }
}
