package inc.nomard.spoty.core.views.tax;

import inc.nomard.spoty.core.components.animations.SpotyAnimations;
import inc.nomard.spoty.core.viewModels.TaxViewModel;
import inc.nomard.spoty.core.views.forms.TaxFormController;
import inc.nomard.spoty.network_bridge.dtos.Tax;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.DoubleFilter;
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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

public class TaxesController implements Initializable {
    private static TaxesController instance;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public MFXTableView<Tax> masterTable;
    @FXML
    public HBox refresh;
    private RotateTransition transition;
    private MFXStageDialog formDialog;
    private MFXStageDialog viewDialog;
    private FXMLLoader formFxmlLoader;
    private FXMLLoader viewFxmlLoader;

    public TaxesController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        productFormDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static TaxesController getInstance(Stage stage) {
        if (instance == null) instance = new TaxesController(stage);
        return instance;
    }

    public void createBtnClicked() {
        formDialog.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Tax> name =
                new MFXTableColumn<>("Name", false, Comparator.comparing(Tax::getName));
        MFXTableColumn<Tax> percentage =
                new MFXTableColumn<>("Percentage", false, Comparator.comparing(Tax::getPercentage));

        name.setRowCellFactory(product -> new MFXTableRowCell<>(Tax::getName));
        percentage.setRowCellFactory(product -> new MFXTableRowCell<>(Tax::getPercentage));

        name.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));
        percentage.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));

        masterTable
                .getTableColumns()
                .addAll(name, percentage);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", Tax::getName),
                        new DoubleFilter<>("Percentage", Tax::getPercentage));
        styleTable();

        if (TaxViewModel.getTaxes().isEmpty()) {
            TaxViewModel.getTaxes()
                    .addListener(
                            (ListChangeListener<Tax>)
                                    c -> masterTable.setItems(TaxViewModel.getTaxes()));
        } else {
            masterTable.itemsProperty().bindBidirectional(TaxViewModel.taxesProperty());
        }
    }

    private void styleTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Tax> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<Tax>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<Tax> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        MFXContextMenuItem delete = getDeleteContextMenuItem(obj);
        // Edit
        edit.setOnAction(
                event -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    TaxViewModel.getTax(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            });
                    formDialog.showAndWait();
                    event.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    private @NotNull MFXContextMenuItem getDeleteContextMenuItem(MFXTableRow<Tax> obj) {
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");

        // Actions
        // Delete
        delete.setOnAction(
                event -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    TaxViewModel.deleteTax(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            });

                    event.consume();
                });
        return delete;
    }

    private void productFormDialogPane(Stage stage) throws IOException {
        formFxmlLoader = fxmlLoader("views/forms/TaxForm.fxml");
        formFxmlLoader.setControllerFactory(c -> TaxFormController.getInstance());

        MFXGenericDialog dialogContent = formFxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        formDialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(contentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(formDialog.getScene());
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

        refreshIcon.setOnMouseClicked(mouseEvent -> TaxViewModel.getTaxes(this::onAction, this::onSuccess, this::onFailed));
    }
}
