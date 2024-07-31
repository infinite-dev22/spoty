package inc.nomard.spoty.core.views;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.hrm.pay_roll.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class BeneficiaryBadgeController implements Initializable {
    private static BeneficiaryBadgeController instance;
    private final Stage stage;
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
    public HBox leftHeaderPane;
    @FXML
    public MFXProgressSpinner progress;
    private MFXStageDialog dialog;

    private BeneficiaryBadgeController(Stage stage) {
        this.stage = stage;
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
        fxmlLoader.setControllerFactory(c -> BeneficiaryBadgeFormController.getInstance(stage));

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setShowClose(false);

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
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            BeneficiaryBadgeViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getName(), stage, contentPane));
        // Edit
        edit.setOnAction(
                e -> {
                    BeneficiaryBadgeViewModel.getItem(obj.getData().getId(), () -> dialog.showAndWait(), this::errorMessage);
                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    public void createBtnClicked() {
        dialog.showAndWait();
    }

    private void onSuccess() {
        BeneficiaryBadgeViewModel.getAllBeneficiaryBadges(null, null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
        setSearchBar();
        Platform.runLater(this::setupTable);
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
                BeneficiaryBadgeViewModel.getAllBeneficiaryBadges(null, null);
            }
            progress.setVisible(true);
            BeneficiaryBadgeViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
            }, this::errorMessage);
        });
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