package inc.normad.spoty.core.views.human_resource.hrm;

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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import inc.normad.spoty.network_bridge.dtos.hrm.employee.Designation;
import inc.normad.spoty.utils.SpotyThreader;
import inc.normad.spoty.core.viewModels.hrm.employee.DesignationViewModel;
import inc.normad.spoty.core.views.forms.DesignationFormController;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static inc.normad.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

public class DesignationsController implements Initializable {
    private static DesignationsController instance;
    public BorderPane contentPane;
    public MFXTextField searchBar;
    public HBox actionsPane;
    public MFXButton importBtn;
    public MFXButton createBtn;
    public MFXTableView<Designation> masterTable;
    private MFXStageDialog dialog;

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
        MFXTableColumn<Designation> name =
                new MFXTableColumn<>("Name", false, Comparator.comparing(Designation::getName));
        MFXTableColumn<Designation> description =
                new MFXTableColumn<>("Designation", false, Comparator.comparing(Designation::getDescription));

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
                            DesignationViewModel.getItem(obj.getData().getId(), this::onAction, this::onFailed);
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
