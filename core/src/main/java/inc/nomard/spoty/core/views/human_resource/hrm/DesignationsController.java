package inc.nomard.spoty.core.views.human_resource.hrm;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.animations.*;
import inc.nomard.spoty.core.viewModels.hrm.employee.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import inc.nomard.spoty.utils.*;
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
import javafx.animation.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;

public class DesignationsController implements Initializable {
    private static DesignationsController instance;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public MFXTableView<Designation> masterTable;
    @FXML
    public HBox refresh;
    private MFXStageDialog dialog;
    private RotateTransition transition;

    private DesignationsController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        formDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static DesignationsController getInstance(Stage stage) {
        if (instance == null) instance = new DesignationsController(stage);
        return instance;
    }

    private void formDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/DesignationForm.fxml");
        fxmlLoader.setControllerFactory(c -> DesignationFormController.getInstance());

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowClose(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(contentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setOverlayClose(true)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    private void setupTable() {
        MFXTableColumn<Designation> name =
                new MFXTableColumn<>("Name", false, Comparator.comparing(Designation::getName));
        MFXTableColumn<Designation> description =
                new MFXTableColumn<>("Description", false, Comparator.comparing(Designation::getDescription));

        name.setRowCellFactory(employee -> new MFXTableRowCell<>(Designation::getName));
        description.setRowCellFactory(employee -> new MFXTableRowCell<>(Designation::getDescription));

        name.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));
        description.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));

        masterTable
                .getTableColumns()
                .addAll(name, description);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", Designation::getName));
        styleDesignationTable();

        if (DesignationViewModel.getDesignations().isEmpty()) {
            DesignationViewModel.getDesignations()
                    .addListener(
                            (ListChangeListener<Designation>)
                                    c -> masterTable.setItems(DesignationViewModel.getDesignations()));
        } else {
            masterTable.itemsProperty().bindBidirectional(DesignationViewModel.designationsProperty());
        }
    }

    private void styleDesignationTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Designation> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<Designation>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<Designation> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            DesignationViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
                            DesignationViewModel.getItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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

        refreshIcon.setOnMouseClicked(mouseEvent -> DesignationViewModel.getAllDesignations(this::onAction, this::onSuccess, this::onFailed));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
        Platform.runLater(this::setupTable);
    }
}
