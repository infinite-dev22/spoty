package inc.nomard.spoty.core.views.human_resource.pay_roll;

import inc.nomard.spoty.core.components.animations.SpotyAnimations;
import inc.nomard.spoty.core.viewModels.hrm.pay_roll.BeneficiaryBadgeViewModel;
import inc.nomard.spoty.core.views.forms.BeneficiaryBadgeFormController;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.BeneficiaryBadge;
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

public class BeneficiaryBadgeController implements Initializable {
    private static BeneficiaryBadgeController instance;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public MFXTableView<BeneficiaryBadge> masterTable;
    @FXML
    public HBox refresh;
    private MFXStageDialog dialog;
    private RotateTransition transition;

    private BeneficiaryBadgeController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        formDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static BeneficiaryBadgeController getInstance(Stage stage) {
        if (instance == null) instance = new BeneficiaryBadgeController(stage);
        return instance;
    }

    private void formDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/BeneficiaryBadgeForm.fxml");
        fxmlLoader.setControllerFactory(c -> BeneficiaryBadgeFormController.getInstance());

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
        MFXTableColumn<BeneficiaryBadge> name =
                new MFXTableColumn<>("Name", false, Comparator.comparing(BeneficiaryBadge::getName));
        MFXTableColumn<BeneficiaryBadge> type =
                new MFXTableColumn<>("Type", false, Comparator.comparing(BeneficiaryBadge::getBeneficiaryTypeName));
        MFXTableColumn<BeneficiaryBadge> appearance =
                new MFXTableColumn<>("Appearance", false, Comparator.comparing(BeneficiaryBadge::getColor));
        MFXTableColumn<BeneficiaryBadge> description =
                new MFXTableColumn<>("Description", false, Comparator.comparing(BeneficiaryBadge::getDescription));

        name.setRowCellFactory(employee -> new MFXTableRowCell<>(BeneficiaryBadge::getName));
        type.setRowCellFactory(employee -> new MFXTableRowCell<>(BeneficiaryBadge::getBeneficiaryType));
        appearance.setRowCellFactory(employee -> new MFXTableRowCell<>(BeneficiaryBadge::getColor));
        description.setRowCellFactory(
                employee -> new MFXTableRowCell<>(BeneficiaryBadge::getDescription));

        name.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        type.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        appearance.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        description.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));

        masterTable
                .getTableColumns()
                .addAll(name, type, appearance, description);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", BeneficiaryBadge::getName),
                        new StringFilter<>("Type", BeneficiaryBadge::getBeneficiaryTypeName));
        styleBeneficiaryBadgeTable();

        if (BeneficiaryBadgeViewModel.getBeneficiaryBadges().isEmpty()) {
            BeneficiaryBadgeViewModel.getBeneficiaryBadges()
                    .addListener(
                            (ListChangeListener<BeneficiaryBadge>)
                                    c -> masterTable.setItems(BeneficiaryBadgeViewModel.getBeneficiaryBadges()));
        } else {
            masterTable.itemsProperty().bindBidirectional(BeneficiaryBadgeViewModel.beneficiaryBadgeProperty());
        }
    }

    private void styleBeneficiaryBadgeTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<BeneficiaryBadge> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<BeneficiaryBadge>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<BeneficiaryBadge> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            BeneficiaryBadgeViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
                            BeneficiaryBadgeViewModel.getItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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

        refreshIcon.setOnMouseClicked(mouseEvent -> BeneficiaryBadgeViewModel.getAllBeneficiaryBadges(this::onAction, this::onSuccess, this::onFailed));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
        Platform.runLater(this::setupTable);
    }
}
