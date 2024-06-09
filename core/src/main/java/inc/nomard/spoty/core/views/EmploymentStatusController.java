package inc.nomard.spoty.core.views;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.hrm.employee.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class EmploymentStatusController implements Initializable {
    private static EmploymentStatusController instance;
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
    public MFXTableView<EmploymentStatus> masterTable;
    @FXML
    public MFXProgressSpinner progress;
    @FXML
    public HBox leftHeaderPane;
    private MFXStageDialog dialog;

    private EmploymentStatusController(Stage stage) {
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

    public static EmploymentStatusController getInstance(Stage stage) {
        if (instance == null) instance = new EmploymentStatusController(stage);
        return instance;
    }

    private void formDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/EmploymentStatusForm.fxml");
        fxmlLoader.setControllerFactory(c -> EmploymentStatusFormController.getInstance(stage));

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
        MFXTableColumn<EmploymentStatus> name =
                new MFXTableColumn<>("Name", false, Comparator.comparing(EmploymentStatus::getName));
        MFXTableColumn<EmploymentStatus> appearance =
                new MFXTableColumn<>("Appearance", false, Comparator.comparing(EmploymentStatus::getName));
        MFXTableColumn<EmploymentStatus> description =
                new MFXTableColumn<>("Description", false, Comparator.comparing(EmploymentStatus::getDescription));

        name.setRowCellFactory(employmentStatus -> new MFXTableRowCell<>(EmploymentStatus::getName));
        appearance.setRowCellFactory(employmentStatus -> {
            var col = Color.valueOf(employmentStatus.getColor());
            var color = Color.rgb((int) col.getRed() * 255, (int) col.getGreen() * 255, (int) col.getBlue() * 255, .2);

            var cell = new MFXTableRowCell<>(EmploymentStatus::getName);

            var label = new Label(employmentStatus.getName());
            label.setTextFill(Color.valueOf(employmentStatus.getColor()).darker());
            label.setPadding(new Insets(5, 10, 5, 10));
            label.setBorder(new Border(new BorderStroke(Color.valueOf(employmentStatus.getColor()).darker(), BorderStrokeStyle.SOLID, new CornerRadii(50), BorderWidths.DEFAULT)));
            label.setBackground(new Background(new BackgroundFill(color, new CornerRadii(50), Insets.EMPTY)));

            var hBox = new VBox(label);
            hBox.setAlignment(Pos.CENTER);

            cell.setGraphic(hBox);
            cell.setAlignment(Pos.CENTER);
            cell.setText(null);
            return cell;
        });
        appearance.setAlignment(Pos.CENTER);
        description.setRowCellFactory(employmentStatus -> new MFXTableRowCell<>(EmploymentStatus::getDescription));

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
                    row.setPrefHeight(50);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<EmploymentStatus> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            EmploymentStatusViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, stage, contentPane));
        // Edit
        edit.setOnAction(
                e -> {
                    EmploymentStatusViewModel.getItem(obj.getData().getId(), () -> dialog.showAndWait(), this::errorMessage);
                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    public void createBtnClicked() {
        dialog.showAndWait();
    }

    private void onSuccess() {
        EmploymentStatusViewModel.getAllEmploymentStatuses(null, null);
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
                EmploymentStatusViewModel.getAllEmploymentStatuses(null, null);
            }
            progress.setVisible(true);
            EmploymentStatusViewModel.searchItem(nv, () -> {
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
