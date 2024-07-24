package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.viewModels.sales.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.sales.*;
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
public class OrderPage extends OutlinePage {
    private TextField searchBar;
    private TableView<SaleMaster> masterTable;
    private MFXProgressSpinner progress;
    private Button createBtn;
    private MFXStageDialog dialog;
    private FXMLLoader viewFxmlLoader;

    public OrderPage() {
        try {
            viewDialogPane();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addNode(init());
        progress.setManaged(true);
        progress.setVisible(true);
        SaleMasterViewModel.getAllSaleMasters(this::onDataInitializationSuccess, this::errorMessage);
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
        searchBar.setPromptText("Search orders");
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
        createBtn = new Button("Create");
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
        TableColumn<SaleMaster, String> saleCustomer = new TableColumn<>("Customer");
        TableColumn<SaleMaster, String> saleStatus = new TableColumn<>("Order Status");
        TableColumn<SaleMaster, String> salePaymentStatus = new TableColumn<>("Pay Status");
        TableColumn<SaleMaster, String> saleDate = new TableColumn<>("Date");
        TableColumn<SaleMaster, String> saleGrandTotal = new TableColumn<>("Total Amount");
        TableColumn<SaleMaster, String> saleAmountPaid = new TableColumn<>("Paid Amount");
        TableColumn<SaleMaster, String> saleAmountDue = new TableColumn<>("Amount Due");

        saleCustomer.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        salePaymentStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleGrandTotal.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleAmountPaid.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleAmountDue.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));

        var columnList = new LinkedList<>(Stream.of(saleCustomer,
                saleStatus,
                salePaymentStatus,
                saleDate,
                saleGrandTotal,
                saleAmountPaid,
                saleAmountDue).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);
        styleSaleMasterTable();

        masterTable.setItems(SaleMasterViewModel.getSales());
    }

    private void styleSaleMasterTable() {
        masterTable.setPrefSize(1200, 1000);

        masterTable.setRowFactory(
                t -> {
                    TableRow<SaleMaster> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<SaleMaster>) event.getSource())
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

    private MFXContextMenu showContextMenu(TableRow<SaleMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem view = new MFXContextMenuItem("View");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            SaleMasterViewModel.deleteSaleMaster(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getCustomerName() + "'s order", this));
        // View
        view.setOnAction(
                event -> {
                    viewShow(obj.getItem());
                    event.consume();
                });

        contextMenu.addItems(view, delete);

        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> {
        });// BaseController.navigation.navigate(new PointOfSalePage(stage)));
    }

    private void onSuccess() {
        SaleMasterViewModel.getAllSaleMasters(null, null);
    }

    private void viewDialogPane() throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/OrderPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new SalePreviewController());
        MFXGenericDialog dialogContent = viewFxmlLoader.load();
        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialogContent.setPrefHeight(screenHeight * .98);
        dialogContent.setPrefWidth(700);

        dialog = SpotyDialog.createDialog(dialogContent, this);

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void viewShow(SaleMaster saleMaster) {
        SalePreviewController controller = viewFxmlLoader.getController();
        controller.init(saleMaster);
        dialog.showAndWait();
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
                SaleMasterViewModel.getAllSaleMasters(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            SaleMasterViewModel.searchSaleMaster(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }
}
