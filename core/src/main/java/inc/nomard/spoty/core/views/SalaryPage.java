package inc.nomard.spoty.core.views;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.hrm.pay_roll.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class SalaryPage extends OutlinePage {
    private final Stage stage;
    private MFXTextField searchBar;
    private MFXTableView<SalaryAdvance> masterTable;
    private MFXProgressSpinner progress;
    private MFXStageDialog formDialog;
    private MFXStageDialog viewDialog;
    private FXMLLoader viewFxmlLoader;

    public SalaryPage(Stage stage) {
        this.stage = stage;
        Platform.runLater(
                () -> {
                    try {
                        productFormDialogPane(stage);
                        productViewDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
        addNode(init());
    }

    public BorderPane init() {
        var pane = new BorderPane();
        pane.setTop(buildTop());
        pane.setCenter(buildCenter());
        setIcons();
        setSearchBar();
        setupTable();
        createBtnAction();
        return pane;
    }

    private HBox buildLeftTop() {
        progress = new MFXProgressSpinner();
        progress.setMinSize(30d, 30d);
        progress.setPrefSize(30d, 30d);
        progress.setMaxSize(30d, 30d);
        progress.setVisible(false);
        progress.setManaged(false);
        var hbox = new HBox(progress);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildCenterTop() {
        searchBar = new MFXTextField();
        searchBar.setPromptText("Search accounts");
        searchBar.setFloatMode(FloatMode.DISABLED);
        searchBar.setMinWidth(300d);
        searchBar.setPrefWidth(500d);
        searchBar.setMaxWidth(700d);
        var hbox = new HBox(searchBar);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildRightTop() {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildTop() {
        var hbox = new HBox();
        hbox.getStyleClass().add("card-flat");
        BorderPane.setAlignment(hbox, Pos.CENTER);
        hbox.setPadding(new Insets(5d));
        hbox.getChildren().addAll(buildLeftTop(), buildCenterTop(), buildRightTop());
        return hbox;
    }

    private AnchorPane buildCenter() {
        masterTable = new MFXTableView<>();
        AnchorPane.setBottomAnchor(masterTable, 0d);
        AnchorPane.setLeftAnchor(masterTable, 0d);
        AnchorPane.setRightAnchor(masterTable, 0d);
        AnchorPane.setTopAnchor(masterTable, 10d);
        return new AnchorPane(masterTable);
    }

    private void productViewDialogPane(Stage stage) throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/SalaryAdvancePreview.fxml");
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
                        .setOwnerNode(this)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .setCenterInOwnerNode(false)
                        .setOverlayClose(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(viewDialog.getScene());
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
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            SalaryAdvanceViewModel.deleteSalaryAdvance(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getEmployeeName() + "'s salary", stage, this));
        // Edit
        edit.setOnAction(
                event -> {
                    SalaryAdvanceViewModel.getSalaryAdvance(obj.getData().getId(), () -> formDialog.showAndWait(), this::errorMessage);
                    event.consume();
                });

        contextMenu.addItems(view, edit, delete/*, send*/);

        return contextMenu;
    }

    private void productFormDialogPane(Stage stage) throws IOException {
        FXMLLoader formFxmlLoader = fxmlLoader("views/forms/SalaryAdvanceForm.fxml");
        formFxmlLoader.setControllerFactory(c -> SalaryAdvanceFormController.getInstance(stage));

        MFXGenericDialog dialogContent = formFxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        formDialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(this)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(formDialog.getScene());
    }

    private void onSuccess() {
        SalaryAdvanceViewModel.getAllSalaryAdvances(null, null);
    }

    public void productViewShow(SalaryAdvance product) {
        SalaryAdvancePreviewController controller = viewFxmlLoader.getController();
        controller.init(product);
        viewDialog.showAndWait();
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

    public void createBtnAction() {
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
                SalaryAdvanceViewModel.getAllSalaryAdvances(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            SalaryAdvanceViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }
}
