package inc.nomard.spoty.core.views;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.returns.purchases.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns.*;
import inc.nomard.spoty.utils.*;
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
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class PurchaseReturnPage extends OutlinePage {
    private final Stage stage;
    private MFXTextField searchBar;
    private MFXTableView<PurchaseReturnMaster> masterTable;
    private MFXProgressSpinner progress;
    private MFXButton createBtn;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    public PurchaseReturnPage(Stage stage) {
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
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnSupplier =
                new MFXTableColumn<>(
                        "Supplier", false, Comparator.comparing(PurchaseReturnMaster::getSupplierName));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnStatus =
                new MFXTableColumn<>("Status", false, Comparator.comparing(PurchaseReturnMaster::getStatus));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnPaymentStatus =
                new MFXTableColumn<>(
                        "Pay Status", false, Comparator.comparing(PurchaseReturnMaster::getPaymentStatus));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(PurchaseReturnMaster::getDate));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnGrandTotal =
                new MFXTableColumn<>("Total Amount", false, Comparator.comparing(PurchaseReturnMaster::getTotal));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnAmountPaid =
                new MFXTableColumn<>("Paid Amount", false, Comparator.comparing(PurchaseReturnMaster::getAmountPaid));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnAmountDue =
                new MFXTableColumn<>("Due Amount", false, Comparator.comparing(PurchaseReturnMaster::getAmountDue));

        purchaseReturnSupplier.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getSupplierName));
        purchaseReturnStatus.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getStatus));
        purchaseReturnPaymentStatus.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getPaymentStatus));
        purchaseReturnDate.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getLocaleDate));
        purchaseReturnGrandTotal.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getTotal));
        purchaseReturnAmountPaid.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getAmountPaid));
        purchaseReturnAmountDue.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getAmountDue));
        purchaseReturnPaymentStatus.setTooltip(new Tooltip("PurchaseReturnMaster Return Payment Status"));
        purchaseReturnStatus.setTooltip(new Tooltip("PurchaseReturnMaster Return Status"));

        purchaseReturnDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.13));
        purchaseReturnSupplier
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        purchaseReturnStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        purchaseReturnGrandTotal
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        purchaseReturnAmountPaid
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        purchaseReturnPaymentStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));

        masterTable
                .getTableColumns()
                .addAll(
                        purchaseReturnSupplier,
                        purchaseReturnStatus,
                        purchaseReturnPaymentStatus,
                        purchaseReturnDate,
                        purchaseReturnGrandTotal,
                        purchaseReturnAmountPaid);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference", PurchaseReturnMaster::getRef),
                        new StringFilter<>("Supplier", PurchaseReturnMaster::getSupplierName),
                        new StringFilter<>("Status", PurchaseReturnMaster::getStatus),
                        new StringFilter<>("Pay Status", PurchaseReturnMaster::getPaymentStatus),
                        new DoubleFilter<>("Total", PurchaseReturnMaster::getTotal),
                        new DoubleFilter<>("Paid", PurchaseReturnMaster::getAmountPaid),
                        new DoubleFilter<>("Due", PurchaseReturnMaster::getAmountDue));

        stylePurchaseReturnMasterTable();

        if (PurchaseReturnMasterViewModel.getPurchaseReturns().isEmpty()) {
            PurchaseReturnMasterViewModel.getPurchaseReturns()
                    .addListener(
                            (ListChangeListener<PurchaseReturnMaster>)
                                    c ->
                                            masterTable.setItems(
                                                    PurchaseReturnMasterViewModel.getPurchaseReturns()));
        } else {
            masterTable
                    .itemsProperty()
                    .bindBidirectional(PurchaseReturnMasterViewModel.purchaseReturnsProperty());
        }
    }

    private void stylePurchaseReturnMasterTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<PurchaseReturnMaster> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<PurchaseReturnMaster>) event.getSource())
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

    public void createBtnAction() {
    }

    private void onSuccess() {
        PurchaseReturnMasterViewModel.getPurchaseReturnMasters(null, null);
    }

    private MFXContextMenu showContextMenu(MFXTableRow<PurchaseReturnMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        MFXContextMenuItem view = new MFXContextMenuItem("View");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            PurchaseReturnMasterViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getSupplierName() + "'s purchase return", stage, this));
        // Edit
        edit.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    PurchaseReturnMasterViewModel.getItem(obj.getData().getId(), this::createBtnAction, this::errorMessage);
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            });
                    e.consume();
                });
        // View
        view.setOnAction(
                event -> {
                    try {
                        viewShow(obj.getData());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    event.consume();
                });

        contextMenu.addItems(view, edit, delete);

        return contextMenu;
    }

    private void viewDialogPane(Stage stage) throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/PurchaseReturnsPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new PurchaseReturnsPreviewController());
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

    public void viewShow(PurchaseReturnMaster purchaseReturn) {
        PurchaseReturnsPreviewController controller = viewFxmlLoader.getController();
        controller.init(purchaseReturn);
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
                PurchaseReturnMasterViewModel.getPurchaseReturnMasters(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            PurchaseReturnMasterViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }
}
