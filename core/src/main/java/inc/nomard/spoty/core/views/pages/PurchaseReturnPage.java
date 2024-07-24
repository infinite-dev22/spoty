package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.viewModels.returns.purchases.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
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
    private TextField searchBar;
    private TableView<PurchaseReturnMaster> masterTable;
    private MFXProgressSpinner progress;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    public PurchaseReturnPage() {
        try {
            viewDialogPane();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addNode(init());
        progress.setManaged(true);
        progress.setVisible(true);
        PurchaseReturnMasterViewModel.getPurchaseReturnMasters(this::onDataInitializationSuccess, this::errorMessage);
    }

    private void onDataInitializationSuccess() {
        progress.setManaged(false);
        progress.setVisible(false);
    }

    public BorderPane init() {
        var pane = new BorderPane();
        pane.setTop(buildTop());
        pane.setCenter(buildCenter());
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
        searchBar = new TextField();
        searchBar.setPromptText("Search purchase returns");
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
        var createBtn = new Button("Create");
        var hbox = new HBox(createBtn);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildTop() {
        var hbox = new HBox();
        hbox.getStyleClass().add("card-flat-bottom");
        BorderPane.setAlignment(hbox, Pos.CENTER);
        hbox.setPadding(new Insets(5d));
        hbox.getChildren().addAll(buildLeftTop(), buildCenterTop(), buildRightTop());
        return hbox;
    }

    private AnchorPane buildCenter() {
        masterTable = new TableView<>();
        NodeUtils.setAnchors(masterTable, new Insets(0d));
        return new AnchorPane(masterTable);
    }

    private void setupTable() {
        TableColumn<PurchaseReturnMaster, String> purchaseReturnSupplier = new TableColumn<>(
                "Supplier");
        TableColumn<PurchaseReturnMaster, String> purchaseReturnStatus = new TableColumn<>("Status");
        TableColumn<PurchaseReturnMaster, String> purchaseReturnPaymentStatus = new TableColumn<>(
                "Pay Status");
        TableColumn<PurchaseReturnMaster, String> purchaseReturnDate = new TableColumn<>("Date");
        TableColumn<PurchaseReturnMaster, String> purchaseReturnGrandTotal = new TableColumn<>("Total Amount");
        TableColumn<PurchaseReturnMaster, String> purchaseReturnAmountPaid = new TableColumn<>("Paid Amount");
        TableColumn<PurchaseReturnMaster, String> purchaseReturnAmountDue = new TableColumn<>("Due Amount");

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

        var columnList = new LinkedList<>(Stream.of(purchaseReturnSupplier,
                purchaseReturnStatus,
                purchaseReturnPaymentStatus,
                purchaseReturnDate,
                purchaseReturnGrandTotal,
                purchaseReturnAmountPaid).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);

        stylePurchaseReturnMasterTable();

        masterTable.setItems(PurchaseReturnMasterViewModel.getPurchaseReturns());
    }

    private void stylePurchaseReturnMasterTable() {
        masterTable.setPrefSize(1200, 1000);

        masterTable.setRowFactory(
                t -> {
                    TableRow<PurchaseReturnMaster> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<PurchaseReturnMaster>) event.getSource())
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

    private MFXContextMenu showContextMenu(TableRow<PurchaseReturnMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        MFXContextMenuItem view = new MFXContextMenuItem("View");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            PurchaseReturnMasterViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getSupplierName() + "'s purchase return", this));
        // Edit
        edit.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    PurchaseReturnMasterViewModel.getItem(obj.getItem().getId(), this::createBtnAction, this::errorMessage);
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
                        viewShow(obj.getItem());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    event.consume();
                });

        contextMenu.addItems(view, edit, delete);

        return contextMenu;
    }

    private void viewDialogPane() throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/PurchaseReturnsPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new PurchaseReturnsPreviewController());
        MFXGenericDialog dialogContent = viewFxmlLoader.load();
        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialogContent.setPrefHeight(screenHeight * .98);
        dialogContent.setPrefWidth(700);
        viewDialog = SpotyDialog.createDialog(dialogContent, this);
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
        progress.setManaged(false);
        progress.setVisible(false);
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
