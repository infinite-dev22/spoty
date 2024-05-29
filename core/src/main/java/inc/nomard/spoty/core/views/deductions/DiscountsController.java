package inc.nomard.spoty.core.views.deductions;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.animations.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
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
import lombok.extern.slf4j.*;

@Slf4j
public class DiscountsController implements Initializable {
    private static DiscountsController instance;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public MFXTableView<Discount> masterTable;
    @FXML
    public HBox refresh;
    private RotateTransition transition;
    private MFXStageDialog formDialog;
    private MFXStageDialog viewDialog;
    private FXMLLoader formFxmlLoader;
    private FXMLLoader viewFxmlLoader;

    public DiscountsController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        productFormDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static DiscountsController getInstance(Stage stage) {
        if (instance == null) instance = new DiscountsController(stage);
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
        MFXTableColumn<Discount> name =
                new MFXTableColumn<>("Name", false, Comparator.comparing(Discount::getName));
        MFXTableColumn<Discount> percentage =
                new MFXTableColumn<>("Percentage", false, Comparator.comparing(Discount::getPercentage));

        name.setRowCellFactory(product -> new MFXTableRowCell<>(Discount::getName));
        percentage.setRowCellFactory(product -> new MFXTableRowCell<>(Discount::getPercentage));

        name.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));
        percentage.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));

        masterTable
                .getTableColumns()
                .addAll(name, percentage);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", Discount::getName),
                        new DoubleFilter<>("Percentage", Discount::getPercentage));
        styleTable();

        if (DiscountViewModel.getDiscounts().isEmpty()) {
            DiscountViewModel.getDiscounts()
                    .addListener(
                            (ListChangeListener<Discount>)
                                    c -> masterTable.setItems(DiscountViewModel.getDiscounts()));
        } else {
            masterTable.itemsProperty().bindBidirectional(DiscountViewModel.discountsProperty());
        }
    }

    private void styleTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Discount> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<Discount>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<Discount> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        MFXContextMenuItem delete = getDeleteContextMenuItem(obj);
        // Edit
        edit.setOnAction(
                event -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    DiscountViewModel.getDiscount(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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

    private MFXContextMenuItem getDeleteContextMenuItem(MFXTableRow<Discount> obj) {
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");

        // Actions
        // Delete
        delete.setOnAction(
                event -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    DiscountViewModel.deleteDiscount(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            });

                    event.consume();
                });
        return delete;
    }

    private void productFormDialogPane(Stage stage) throws IOException {
        formFxmlLoader = fxmlLoader("views/forms/DiscountForm.fxml");
        formFxmlLoader.setControllerFactory(c -> DiscountFormController.getInstance());

        MFXGenericDialog dialogContent = formFxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setShowClose(false);

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

        refreshIcon.setOnMouseClicked(mouseEvent -> DiscountViewModel.getDiscounts(this::onAction, this::onSuccess, this::onFailed));
    }
}
