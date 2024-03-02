package inc.nomard.spoty.core.views.human_resource.hrm;

import inc.nomard.spoty.core.components.animations.SpotyAnimations;
import inc.nomard.spoty.core.viewModels.BankViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.EmploymentStatusViewModel;
import inc.nomard.spoty.core.views.forms.EmploymentStatusFormController;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.EmploymentStatus;
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

public class EmploymentStatusController implements Initializable {
    private static EmploymentStatusController instance;
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
    public MFXTableView<EmploymentStatus> masterTable;
    @FXML
    public HBox refresh;
    private MFXStageDialog dialog;
    private RotateTransition transition;

    private EmploymentStatusController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        formDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static EmploymentStatusController getInstance(Stage stage) {
        if (instance == null) instance = new EmploymentStatusController(stage);
        return instance;
    }

    private void formDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/EmploymentStatusForm.fxml");
        fxmlLoader.setControllerFactory(c -> EmploymentStatusFormController.getInstance());

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
        MFXTableColumn<EmploymentStatus> name =
                new MFXTableColumn<>("Name", false, Comparator.comparing(EmploymentStatus::getName));
        MFXTableColumn<EmploymentStatus> appearance =
                new MFXTableColumn<>("Appearance", false, Comparator.comparing(EmploymentStatus::getName));
        MFXTableColumn<EmploymentStatus> description =
                new MFXTableColumn<>("EmploymentStatus", false, Comparator.comparing(EmploymentStatus::getDescription));

        name.setRowCellFactory(employee -> new MFXTableRowCell<>(EmploymentStatus::getName));
        description.setRowCellFactory(employee -> new MFXTableRowCell<>(EmploymentStatus::getDescription));

        name.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        appearance.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        description.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));

        masterTable
                .getTableColumns()
                .addAll(name, appearance, description);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", EmploymentStatus::getName));
        styleEmploymentStatusTable();

        if (EmploymentStatusViewModel.getEmploymentStatuses().isEmpty()) {
            EmploymentStatusViewModel.getEmploymentStatuses()
                    .addListener(
                            (ListChangeListener<EmploymentStatus>)
                                    c -> masterTable.setItems(EmploymentStatusViewModel.getEmploymentStatuses()));
        } else {
            masterTable.itemsProperty().bindBidirectional(EmploymentStatusViewModel.employmentStatusesProperty());
        }
    }

    private void styleEmploymentStatusTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<EmploymentStatus> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<EmploymentStatus>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<EmploymentStatus> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            EmploymentStatusViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
                            EmploymentStatusViewModel.getItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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

        refreshIcon.setOnMouseClicked(mouseEvent -> EmploymentStatusViewModel.getAllEmploymentStatuses(this::onAction, this::onSuccess, this::onFailed));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(this::setupTable);
    }
}
