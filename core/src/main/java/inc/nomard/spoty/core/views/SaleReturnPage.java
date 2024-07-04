package inc.nomard.spoty.core.views;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.viewModels.returns.sales.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.returns.sale_returns.*;
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
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class SaleReturnPage extends OutlinePage {
    private MFXTextField searchBar;
    private MFXTableView<SaleReturnMaster> masterTable;
    private MFXProgressSpinner progress;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    public SaleReturnPage() {
        Platform.runLater(() ->
        {
            try {
                viewDialogPane();
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
        searchBar.setPromptText("Search sale/order returns");
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

    private void setupTable() {
        MFXTableColumn<SaleReturnMaster> saleReturnCustomer =
                new MFXTableColumn<>("Customer", false, Comparator.comparing(SaleReturnMaster::getCustomerName));
        MFXTableColumn<SaleReturnMaster> saleReturnStatus =
                new MFXTableColumn<>("Order Status", false, Comparator.comparing(SaleReturnMaster::getSaleStatus));
        MFXTableColumn<SaleReturnMaster> saleReturnPaymentStatus =
                new MFXTableColumn<>(
                        "Pay Status", false, Comparator.comparing(SaleReturnMaster::getPaymentStatus));
        MFXTableColumn<SaleReturnMaster> saleReturnDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(SaleReturnMaster::getDate));
        MFXTableColumn<SaleReturnMaster> saleReturnGrandTotal =
                new MFXTableColumn<>("Total Amount", false, Comparator.comparing(SaleReturnMaster::getTotal));
        MFXTableColumn<SaleReturnMaster> saleReturnAmountPaid =
                new MFXTableColumn<>("Paid Amount", false, Comparator.comparing(SaleReturnMaster::getAmountPaid));
        MFXTableColumn<SaleReturnMaster> saleReturnAmountDue =
                new MFXTableColumn<>("Amount Due", false, Comparator.comparing(SaleReturnMaster::getAmountDue));

        saleReturnCustomer.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getCustomerName));
        saleReturnStatus.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getSaleStatus));
        saleReturnPaymentStatus.setRowCellFactory(
                saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getPaymentStatus));
        saleReturnDate.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getLocaleDate));
        saleReturnGrandTotal.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getTotal));
        saleReturnAmountPaid.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getAmountPaid));
        saleReturnAmountDue.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getAmountDue));

        saleReturnPaymentStatus.setTooltip(new Tooltip("SaleMaster Return Payment Status"));
        saleReturnStatus.setTooltip(new Tooltip("SaleMaster Return Status"));

        saleReturnDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.143));
        saleReturnCustomer.prefWidthProperty().bind(masterTable.widthProperty().multiply(.143));
        saleReturnStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.143));
        saleReturnGrandTotal.prefWidthProperty().bind(masterTable.widthProperty().multiply(.143));
        saleReturnAmountPaid.prefWidthProperty().bind(masterTable.widthProperty().multiply(.143));
        saleReturnPaymentStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.143));

        masterTable
                .getTableColumns()
                .addAll(
                        saleReturnCustomer,
                        saleReturnStatus,
                        saleReturnPaymentStatus,
                        saleReturnDate,
                        saleReturnGrandTotal,
                        saleReturnAmountPaid);

        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Ref No.", SaleReturnMaster::getRef),
                        new StringFilter<>("Customer", SaleReturnMaster::getCustomerName),
                        new StringFilter<>("Sale Status", SaleReturnMaster::getSaleStatus),
                        new StringFilter<>("Payment Status", SaleReturnMaster::getPaymentStatus),
                        new DoubleFilter<>("Grand Total", SaleReturnMaster::getTotal),
                        new DoubleFilter<>("Amount Paid", SaleReturnMaster::getAmountPaid),
                        new DoubleFilter<>("Amount Due", SaleReturnMaster::getAmountDue));

        getSaleReturnMasterTable();

        if (SaleReturnMasterViewModel.getSaleReturns().isEmpty()) {
            SaleReturnMasterViewModel.getSaleReturns()
                    .addListener(
                            (ListChangeListener<SaleReturnMaster>)
                                    c -> masterTable.setItems(SaleReturnMasterViewModel.getSaleReturns()));
        } else {
            masterTable
                    .itemsProperty()
                    .bindBidirectional(SaleReturnMasterViewModel.saleReturnsProperty());
        }
    }

    private void getSaleReturnMasterTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<SaleReturnMaster> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<SaleReturnMaster>) event.getSource())
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
        SaleReturnMasterViewModel.getSaleReturnMasters(null, null);
    }

    private MFXContextMenu showContextMenu(MFXTableRow<SaleReturnMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        MFXContextMenuItem view = new MFXContextMenuItem("View");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            SaleReturnMasterViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getCustomerName() + "'s sale returns", this));
        // Edit
        edit.setOnAction(
                e -> {
                    SaleReturnMasterViewModel.getItem(obj.getData().getId(), this::createBtnAction, this::errorMessage);
                    createBtnAction();
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

    private void viewDialogPane() throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/SaleReturnsPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new SaleReturnsPreviewController());
        MFXGenericDialog dialogContent = viewFxmlLoader.load();
        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialogContent.setPrefHeight(screenHeight * .98);
        dialogContent.setPrefWidth(700);
        viewDialog = SpotyDialog.createDialog(dialogContent, this);
    }

    public void viewShow(SaleReturnMaster saleReturnsMaster) {
        SaleReturnsPreviewController controller = viewFxmlLoader.getController();
        controller.init(saleReturnsMaster);
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
        if (!AppManager.getMorphPane().getChildren().contains(notification)) {
            AppManager.getMorphPane().getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification));
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
                SaleReturnMasterViewModel.getSaleReturnMasters(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            SaleReturnMasterViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }
}
