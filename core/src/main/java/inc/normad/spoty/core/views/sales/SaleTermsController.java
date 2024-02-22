package inc.normad.spoty.core.views.sales;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.filter.BooleanFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import inc.normad.spoty.network_bridge.dtos.SaleTermAndCondition;
import inc.normad.spoty.utils.SpotyThreader;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class SaleTermsController implements Initializable {
    private static SaleTermsController instance;
    public BorderPane saleTermContentPane;
    public MFXTextField saleTermSearchBar;
    public HBox saleTermActionsPane;
    public MFXButton saleTermImportBtn;
    public MFXButton saleTermCreateBtn;
    public MFXTableView<SaleTermAndCondition> saleTermTable;
    private MFXStageDialog dialog;

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
        Platform.runLater(this::setupTable);
    }

    public void saleTermCreateBtnClicked(MouseEvent mouseEvent) {
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

        name.prefWidthProperty().bind(saleTermTable.widthProperty().multiply(.5));
        status.prefWidthProperty().bind(saleTermTable.widthProperty().multiply(.5));

        name.setColumnResizable(false);
        status.setColumnResizable(false);

        saleTermTable.getTableColumns().addAll(name, status);
        saleTermTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", SaleTermAndCondition::getName),
                        new BooleanFilter<>("Status", SaleTermAndCondition::isActive));

        styleSaleTermAndConditionTable();

//        if (SaleTermAndConditionViewModel.getCategories().isEmpty()) {
//            SaleTermAndConditionViewModel.getCategories().addListener(
//                    (ListChangeListener<SaleTermAndCondition>)
//                            c -> saleTermTable.setItems(SaleTermAndConditionViewModel.getCategories()));
//        } else {
//            saleTermTable
//                    .itemsProperty()
//                    .bindBidirectional(SaleTermAndConditionViewModel.categoriesProperty());
//        }
    }

    private void styleSaleTermAndConditionTable() {
        saleTermTable.setPrefSize(1200, 1000);
        saleTermTable.features().enableBounceEffect();
        saleTermTable.features().enableSmoothScrolling(0.5);

        saleTermTable.setTableRowFactory(
                t -> {
                    MFXTableRow<SaleTermAndCondition> row = new MFXTableRow<>(saleTermTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<SaleTermAndCondition>) event.getSource())
                                        .show(
                                                saleTermTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<SaleTermAndCondition> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(saleTermTable);
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
//                        .setOwnerNode(saleTermContentPane)
//                        .setScrimPriority(ScrimPriority.WINDOW)
//                        .setScrimOwner(true)
//                        .get();
//
//        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void categoryExpenseCreateBtnClicked() {
        dialog.showAndWait();
    }
}
