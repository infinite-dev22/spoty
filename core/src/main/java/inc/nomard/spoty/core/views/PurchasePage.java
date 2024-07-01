package inc.nomard.spoty.core.views;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.viewModels.purchases.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.layout.navigation.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.purchases.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxcomponents.theming.enums.*;
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

@SuppressWarnings("unchecked")
@Log
public class PurchasePage extends OutlinePage {
    private final Stage stage;
    private MFXTextField searchBar;
    private MFXTableView<PurchaseMaster> masterTable;
    private MFXProgressSpinner progress;
    private MFXButton createBtn;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    public PurchasePage(Stage stage) {
        this.stage = stage;
        Platform.runLater(() ->
        {
            try {
                viewDialogPane(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
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
        createBtn = new MFXButton("Create");
        createBtn.setVariants(ButtonVariants.FILLED);
        var hbox = new HBox(createBtn);
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

    private void setupTable() {
        MFXTableColumn<PurchaseMaster> masterSupplier =
                new MFXTableColumn<>(
                        "Supplier", false, Comparator.comparing(PurchaseMaster::getSupplierName));
        MFXTableColumn<PurchaseMaster> masterStatus =
                new MFXTableColumn<>("Status", false, Comparator.comparing(PurchaseMaster::getPurchaseStatus));
        MFXTableColumn<PurchaseMaster> masterPaymentStatus =
                new MFXTableColumn<>(
                        "Pay Status", false, Comparator.comparing(PurchaseMaster::getPaymentStatus));
        MFXTableColumn<PurchaseMaster> masterDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(PurchaseMaster::getDate));
        MFXTableColumn<PurchaseMaster> masterGrandTotal =
                new MFXTableColumn<>("Total Amount", false, Comparator.comparing(PurchaseMaster::getTotal));
        MFXTableColumn<PurchaseMaster> masterAmountPaid =
                new MFXTableColumn<>("Paid Amount", false, Comparator.comparing(PurchaseMaster::getAmountPaid));
        MFXTableColumn<PurchaseMaster> masterAmountDue =
                new MFXTableColumn<>("Due Amount", false, Comparator.comparing(PurchaseMaster::getAmountDue));

        masterSupplier.setRowCellFactory(
                master -> new MFXTableRowCell<>(PurchaseMaster::getSupplierName));
        masterStatus.setRowCellFactory(
                master -> new MFXTableRowCell<>(PurchaseMaster::getPurchaseStatus));
        masterPaymentStatus.setRowCellFactory(
                master -> new MFXTableRowCell<>(PurchaseMaster::getPaymentStatus));
        masterDate.setRowCellFactory(
                master -> new MFXTableRowCell<>(PurchaseMaster::getLocaleDate));
        masterGrandTotal.setRowCellFactory(
                master -> new MFXTableRowCell<>(PurchaseMaster::getTotal));
        masterAmountPaid.setRowCellFactory(
                master -> new MFXTableRowCell<>(PurchaseMaster::getAmountPaid));
        masterAmountDue.setRowCellFactory(
                master -> new MFXTableRowCell<>(PurchaseMaster::getAmountDue));

        masterSupplier
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        masterStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        masterPaymentStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        masterDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        masterGrandTotal
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        masterAmountPaid
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        masterAmountDue
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));

        masterTable
                .getTableColumns()
                .addAll(
                        masterSupplier,
                        masterStatus,
                        masterPaymentStatus,
                        masterDate,
                        masterGrandTotal,
                        masterAmountPaid,
                        masterAmountDue);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference", PurchaseMaster::getRef),
                        new StringFilter<>("Supplier", PurchaseMaster::getSupplierName),
                        new StringFilter<>("Status", PurchaseMaster::getPurchaseStatus),
                        new StringFilter<>("Pay Status", PurchaseMaster::getPaymentStatus),
                        new DoubleFilter<>("Total", PurchaseMaster::getTotal),
                        new DoubleFilter<>("Paid", PurchaseMaster::getAmountPaid),
                        new DoubleFilter<>("Due", PurchaseMaster::getAmountDue));
        getTable();

        if (PurchaseMasterViewModel.getPurchases().isEmpty()) {
            PurchaseMasterViewModel.getPurchases()
                    .addListener(
                            (ListChangeListener<PurchaseMaster>)
                                    c -> masterTable.setItems(PurchaseMasterViewModel.getPurchases()));
        } else {
            masterTable
                    .itemsProperty()
                    .bindBidirectional(PurchaseMasterViewModel.purchasesProperty());
        }
    }

    private void getTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<PurchaseMaster> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<PurchaseMaster>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<PurchaseMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        MFXContextMenuItem view = new MFXContextMenuItem("View");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            PurchaseMasterViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getSupplierName() + "'s purchase", stage, this));
        // Edit
        edit.setOnAction(
                e -> {
                    Platform.runLater(() -> PurchaseMasterViewModel.getPurchaseMaster(obj.getData().getId(), this::createBtnAction, this::errorMessage));
                    e.consume();
                });
        // View
        view.setOnAction(
                event -> {
                    viewShow(obj.getData());
                    event.consume();
                });

        contextMenu.addItems(view, edit, delete);

        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> BaseController.navigation.navigate(Pages.getPurchaseMasterFormPane()));
    }

    private void onSuccess() {
        PurchaseMasterViewModel.getAllPurchaseMasters(null, null);
    }

    private void viewDialogPane(Stage stage) throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/PurchasePreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new PurchasePreviewController());
        MFXGenericDialog genericDialog = viewFxmlLoader.load();
        genericDialog.setShowMinimize(false);
        genericDialog.setShowAlwaysOnTop(false);

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

    public void viewShow(PurchaseMaster purchaseMaster) {
        PurchasePreviewController controller = viewFxmlLoader.getController();
        controller.init(purchaseMaster);
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

    private void setIcons() {
        searchBar.setTrailingIcon(new MFXFontIcon("fas-magnifying-glass"));
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                PurchaseMasterViewModel.getAllPurchaseMasters(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            PurchaseMasterViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }
}
