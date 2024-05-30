package inc.nomard.spoty.core.views.human_resource.pay_roll;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;

import inc.nomard.spoty.core.components.animations.*;
import inc.nomard.spoty.core.viewModels.hrm.pay_roll.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.*;
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

import lombok.extern.java.Log;

@Log
public class BeneficiaryTypeController implements Initializable {
    private static BeneficiaryTypeController instance;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public MFXTableView<BeneficiaryType> masterTable;
    @FXML
    public HBox refresh;
    private MFXStageDialog dialog;
    private RotateTransition transition;

    private BeneficiaryTypeController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        formDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static BeneficiaryTypeController getInstance(Stage stage) {
        if (instance == null) instance = new BeneficiaryTypeController(stage);
        return instance;
    }

    private void formDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/BeneficiaryTypeForm.fxml");
        fxmlLoader.setControllerFactory(c -> BeneficiaryTypeFormController.getInstance());

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
        MFXTableColumn<BeneficiaryType> name =
                new MFXTableColumn<>("Name", false, Comparator.comparing(BeneficiaryType::getName));
        MFXTableColumn<BeneficiaryType> appearance =
                new MFXTableColumn<>("Appearance", false, Comparator.comparing(BeneficiaryType::getColor));
        MFXTableColumn<BeneficiaryType> description =
                new MFXTableColumn<>("Description", false, Comparator.comparing(BeneficiaryType::getDescription));

        name.setRowCellFactory(employee -> new MFXTableRowCell<>(BeneficiaryType::getName));
        appearance.setRowCellFactory(employee -> new MFXTableRowCell<>(BeneficiaryType::getColor));
        description.setRowCellFactory(
                employee -> new MFXTableRowCell<>(BeneficiaryType::getDescription));

        name.prefWidthProperty().bind(masterTable.widthProperty().multiply(.333));
        appearance.prefWidthProperty().bind(masterTable.widthProperty().multiply(.333));
        description.prefWidthProperty().bind(masterTable.widthProperty().multiply(.333));

        masterTable
                .getTableColumns()
                .addAll(name, appearance, description);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", BeneficiaryType::getName));
        styleBeneficiaryTypeTable();

        if (BeneficiaryTypeViewModel.getBeneficiaryTypes().isEmpty()) {
            BeneficiaryTypeViewModel.getBeneficiaryTypes()
                    .addListener(
                            (ListChangeListener<BeneficiaryType>)
                                    c -> masterTable.setItems(BeneficiaryTypeViewModel.getBeneficiaryTypes()));
        } else {
            masterTable.itemsProperty().bindBidirectional(BeneficiaryTypeViewModel.beneficiaryTypesProperty());
        }
    }

    private void styleBeneficiaryTypeTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<BeneficiaryType> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<BeneficiaryType>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<BeneficiaryType> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            BeneficiaryTypeViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
                            BeneficiaryTypeViewModel.getItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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

        refreshIcon.setOnMouseClicked(mouseEvent -> BeneficiaryTypeViewModel.getAllBeneficiaryTypes(this::onAction, this::onSuccess, this::onFailed));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
        Platform.runLater(this::setupTable);
    }
}
