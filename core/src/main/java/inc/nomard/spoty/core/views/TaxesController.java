package inc.nomard.spoty.core.views;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.network_bridge.dtos.*;
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
public class TaxesController implements Initializable {
    private static TaxesController instance;
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
    public MFXTableView<Tax> masterTable;
    @FXML
    public HBox refresh;
    @FXML
    public MFXProgressSpinner progress;
    @FXML
    public HBox leftHeaderPane;
    private MFXStageDialog dialog;

    public TaxesController(Stage stage) {
        this.stage = stage;
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
        dialog.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
        setSearchBar();
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
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        // Edit
        edit.setOnAction(
                event -> {
                    TaxViewModel.getTax(obj.getData().getId(), () -> dialog.showAndWait(), this::errorMessage);
                    event.consume();
                });
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            TaxViewModel.deleteTax(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, stage, contentPane));
        contextMenu.addItems(edit, delete);
        return contextMenu;
    }

    private void productFormDialogPane(Stage stage) throws IOException {
        FXMLLoader formFxmlLoader = fxmlLoader("views/forms/TaxForm.fxml");
        formFxmlLoader.setControllerFactory(c -> TaxFormController.getInstance(stage));
        MFXGenericDialog dialogContent = formFxmlLoader.load();
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

    private void onSuccess() {
        TaxViewModel.getTaxes(null, null);
    }

    private void setIcons() {
        searchBar.setTrailingIcon(new MFXFontIcon("fas-magnifying-glass"));
    }

    public void setSearchBar() {
        searchBar.setDisable(true);
//        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
//            if (Objects.equals(ov, nv)) {
//                return;
//            }
//            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
//                TaxViewModel.getTaxes(null, null);
//            }
//            progress.setVisible(true);
//            TaxViewModel.searchItem(nv, () -> {
//                progress.setVisible(false);
//            }, this::errorMessage);
//        });
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
