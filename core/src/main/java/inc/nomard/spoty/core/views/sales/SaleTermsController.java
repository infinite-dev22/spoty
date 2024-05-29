package inc.nomard.spoty.core.views.sales;

import inc.nomard.spoty.core.components.animations.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.slf4j.*;

@Slf4j
public class SaleTermsController implements Initializable {
    private static SaleTermsController instance;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public MFXTableView<SaleTermAndCondition> masterTable;
    @FXML
    public HBox refresh;
    private MFXStageDialog dialog;
    private RotateTransition transition;

    private SaleTermsController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        saleTermFormDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static SaleTermsController getInstance(Stage stage) {
        if (instance == null) instance = new SaleTermsController(stage);
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
        Platform.runLater(this::setupTable);
    }

    public void createBtnClicked(MouseEvent mouseEvent) {
    }

    private void setupTable() {
        MFXTableColumn<SaleTermAndCondition> name =
                new MFXTableColumn<>("Name", true, Comparator.comparing(SaleTermAndCondition::getName));
        MFXTableColumn<SaleTermAndCondition> status =
                new MFXTableColumn<>(
                        "Status", true, Comparator.comparing(SaleTermAndCondition::isActive));

        name.setRowCellFactory(category -> new MFXTableRowCell<>(SaleTermAndCondition::getName));
        status.setRowCellFactory(
                category -> new MFXTableRowCell<>(SaleTermAndCondition::isActive));

        name.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));
        status.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));

        name.setColumnResizable(false);
        status.setColumnResizable(false);

        masterTable.getTableColumns().addAll(name, status);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", SaleTermAndCondition::getName),
                        new BooleanFilter<>("Status", SaleTermAndCondition::isActive));

        styleSaleTermAndConditionTable();

//        if (SaleTermAndConditionViewModel.getCategories().isEmpty()) {
//            SaleTermAndConditionViewModel.getCategories().addListener(
//                    (ListChangeListener<SaleTermAndCondition>)
//                            c -> masterTable.setItems(SaleTermAndConditionViewModel.getCategories()));
//        } else {
//            masterTable
//                    .itemsProperty()
//                    .bindBidirectional(SaleTermAndConditionViewModel.categoriesProperty());
//        }
    }

    private void styleSaleTermAndConditionTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<SaleTermAndCondition> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<SaleTermAndCondition>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<SaleTermAndCondition> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
//                            SaleTermAndConditionViewModel.deleteItem(obj.getData().getId());
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
//                            SaleTermAndConditionViewModel.getItem(obj.getData().getId());
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

    private void saleTermFormDialogPane(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = fxmlLoader("views/forms/SaleTermAndConditionForm.fxml");
//        fxmlLoader.setControllerFactory(c -> SaleTermAndConditionFormController.getInstance());
//
//        MFXGenericDialog dialogContent = fxmlLoader.load();
//
//        dialogContent.setShowMinimize(false);
//        dialogContent.setShowAlwaysOnTop(false);
//
//        dialog =
//                MFXGenericDialogBuilder.build(dialogContent)
//                        .toStageDialogBuilder()
//                        .initOwner(stage)
//                        .initModality(Modality.WINDOW_MODAL)
//                        .setOwnerNode(contentPane)
//                        .setScrimPriority(ScrimPriority.WINDOW)
//                        .setScrimOwner(true)
//                        .get();
//
//        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void categoryExpenseCreateBtnClicked() {
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

//        refreshIcon.setOnMouseClicked(mouseEvent -> SaleTermsViewModel.getAllSaleTermss(this::onAction, this::onSuccess, this::onFailed));
    }
}
