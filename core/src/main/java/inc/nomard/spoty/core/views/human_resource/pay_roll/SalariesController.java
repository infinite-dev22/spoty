package inc.nomard.spoty.core.views.human_resource.pay_roll;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;

import inc.nomard.spoty.core.components.animations.*;
import inc.nomard.spoty.core.viewModels.hrm.pay_roll.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.core.views.previews.hrm.pay_roll.*;
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
public class SalariesController implements Initializable {
    private static SalariesController instance;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public MFXTableView<SalaryAdvance> masterTable;
    @FXML
    public HBox refresh;
    private RotateTransition transition;
    private MFXStageDialog formDialog;
    private MFXStageDialog viewDialog;
    private FXMLLoader formFxmlLoader;
    private FXMLLoader viewFxmlLoader;

    public SalariesController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        productFormDialogPane(stage);
                        productViewDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static SalariesController getInstance(Stage stage) {
        if (instance == null) instance = new SalariesController(stage);
        return instance;
    }

    private void productViewDialogPane(Stage stage) throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/hrm/pay_roll/SalaryAdvancePreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new SalaryAdvancePreviewController());

        MFXGenericDialog genericDialog = viewFxmlLoader.load();
        genericDialog.setShowMinimize(false);
        genericDialog.setShowAlwaysOnTop(false);
        genericDialog.setShowClose(false);

        genericDialog.setPrefHeight(screenHeight * .98);
        genericDialog.setPrefWidth(700);

        viewDialog =
                MFXGenericDialogBuilder.build(genericDialog)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(contentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .setCenterInOwnerNode(false)
                        .setOverlayClose(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(viewDialog.getScene());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<SalaryAdvance> productName =
                new MFXTableColumn<>("Employee", false, Comparator.comparing(SalaryAdvance::getEmployeeName));
        MFXTableColumn<SalaryAdvance> productCategory =
                new MFXTableColumn<>("Period", false, Comparator.comparing(SalaryAdvance::getPaySlipPeriod));
        MFXTableColumn<SalaryAdvance> productBrand =
                new MFXTableColumn<>("Status", false, Comparator.comparing(SalaryAdvance::getStatus));
        MFXTableColumn<SalaryAdvance> productQuantity =
                new MFXTableColumn<>("Salary", false, Comparator.comparing(SalaryAdvance::getSalary));
        MFXTableColumn<SalaryAdvance> productPrice =
                new MFXTableColumn<>("Net Salary", false, Comparator.comparing(SalaryAdvance::getNetSalary));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(SalaryAdvance::getEmployeeName));
        productCategory.setRowCellFactory(product -> new MFXTableRowCell<>(SalaryAdvance::getPaySlipPeriod));
        productBrand.setRowCellFactory(product -> new MFXTableRowCell<>(SalaryAdvance::getStatus));
        productQuantity.setRowCellFactory(product -> new MFXTableRowCell<>(SalaryAdvance::getSalary));
        productPrice.setRowCellFactory(product -> new MFXTableRowCell<>(SalaryAdvance::getNetSalary));

        productName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        productCategory.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        productBrand.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        productQuantity.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        productPrice.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));

        masterTable
                .getTableColumns()
                .addAll(productName, productCategory, productBrand, productQuantity, productPrice);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Employee", SalaryAdvance::getEmployeeName),
                        new StringFilter<>("Period", SalaryAdvance::getPaySlipPeriod),
                        new StringFilter<>("Status", SalaryAdvance::getStatus),
                        new DoubleFilter<>("Salary", SalaryAdvance::getSalary),
                        new DoubleFilter<>("Net Salary", SalaryAdvance::getNetSalary));
        styleTable();

        if (SalaryAdvanceViewModel.getSalaryAdvances().isEmpty()) {
            SalaryAdvanceViewModel.getSalaryAdvances()
                    .addListener(
                            (ListChangeListener<SalaryAdvance>)
                                    c -> masterTable.setItems(SalaryAdvanceViewModel.getSalaryAdvances()));
        } else {
            masterTable.itemsProperty().bindBidirectional(SalaryAdvanceViewModel.salaryAdvancesProperty());
        }
    }

    private void styleTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<SalaryAdvance> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<SalaryAdvance>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<SalaryAdvance> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem view = new MFXContextMenuItem("View");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
//        MFXContextMenuItem send = new MFXContextMenuItem("Send");
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");

        // Actions
        // View
        view.setOnAction(event -> {
            try {
                productViewShow(obj.getData());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            event.consume();
        });
        // Send
//        send.setOnAction(
//                event -> {
//                    SpotyThreader.spotyThreadPool(
//                            () -> {
//                                try {
//                                    SalaryAdvanceViewModel.deleteSalaryAdvance(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
//                                } catch (Exception ex) {
//                                    throw new RuntimeException(ex);
//                                }
//                            });
//
//                    event.consume();
//                });
        // Delete
        delete.setOnAction(
                event -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    SalaryAdvanceViewModel.deleteSalaryAdvance(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            });

                    event.consume();
                });
        // Edit
        edit.setOnAction(
                event -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    SalaryAdvanceViewModel.getSalaryAdvance(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            });
                    formDialog.showAndWait();
                    event.consume();
                });

        contextMenu.addItems(view, edit, delete/*, send*/);

        return contextMenu;
    }

    private void productFormDialogPane(Stage stage) throws IOException {
        formFxmlLoader = fxmlLoader("views/forms/SalaryAdvanceForm.fxml");
        formFxmlLoader.setControllerFactory(c -> SalaryAdvanceFormController.getInstance());

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

        refreshIcon.setOnMouseClicked(mouseEvent -> SalaryAdvanceViewModel.getAllSalaryAdvances(this::onAction, this::onSuccess, this::onFailed));
    }

    public void productViewShow(SalaryAdvance product) {
        SalaryAdvancePreviewController controller = viewFxmlLoader.getController();
        controller.init(product);
        viewDialog.showAndWait();
    }
}
