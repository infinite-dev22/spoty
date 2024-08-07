package inc.nomard.spoty.core.views.pages.sale.tabs;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.viewModels.sales.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.pos.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.sales.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import java.io.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;
import org.kordamp.ikonli.feather.*;
import org.kordamp.ikonli.javafx.*;

@SuppressWarnings("unchecked")
@Log
public class OrderPage extends OutlinePage {
    private final SideModalPane modalPane;
    private TextField searchBar;
    private TableView<SaleMaster> masterTable;
    private MFXProgressSpinner progress;
    private MFXStageDialog dialog;
    private FXMLLoader viewFxmlLoader;
    private TableColumn<SaleMaster, SaleMaster> saleCustomer;
    private TableColumn<SaleMaster, SaleMaster> saleDate;
    private TableColumn<SaleMaster, SaleMaster> saleGrandTotal;
    private TableColumn<SaleMaster, SaleMaster> saleAmountPaid;
    private TableColumn<SaleMaster, SaleMaster> saleAmountDue;
    private TableColumn<SaleMaster, String> saleStatus;
    private TableColumn<SaleMaster, String> salePaymentStatus;
    private TableColumn<SaleMaster, SaleMaster> createdBy;
    private TableColumn<SaleMaster, SaleMaster> createdAt;
    private TableColumn<SaleMaster, SaleMaster> updatedBy;
    private TableColumn<SaleMaster, SaleMaster> updatedAt;

    public OrderPage() {
        try {
            viewDialogPane();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        modalPane = new SideModalPane();

        getChildren().addAll(modalPane, init());
        progress.setManaged(true);
        progress.setVisible(true);
        SaleMasterViewModel.getAllSaleMasters(this::onDataInitializationSuccess, this::errorMessage);

        modalPane.displayProperty().addListener((observableValue, closed, open) -> {
            if (!open) {
                modalPane.setAlignment(Pos.CENTER);
                modalPane.usePredefinedTransitionFactories(null);
            }
        });
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
        var posBtn = new Button("POS", new FontIcon(Feather.SHOPPING_CART));
        posBtn.setOnAction(actionEvent -> AppManager.getNavigation().navigate(PointOfSalePage.class));
        var hbox = new HBox(posBtn);
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
        saleCustomer = new TableColumn<>("Customer");
        saleDate = new TableColumn<>("Date");
        saleGrandTotal = new TableColumn<>("Total Amount");
        saleAmountPaid = new TableColumn<>("Paid Amount");
        saleAmountDue = new TableColumn<>("Amount Due");
        saleStatus = new TableColumn<>("Order Status");
        salePaymentStatus = new TableColumn<>("Pay Status");
        createdBy = new TableColumn<>("Created By");
        createdAt = new TableColumn<>("Created At");
        updatedBy = new TableColumn<>("Updated By");
        updatedAt = new TableColumn<>("Updated At");

        saleCustomer.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleGrandTotal.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleAmountPaid.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleAmountDue.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        salePaymentStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        createdBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        createdAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        updatedBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        updatedAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(saleCustomer,
                saleDate,
                saleGrandTotal,
                saleAmountPaid,
                saleAmountDue,
                saleStatus,
                salePaymentStatus,
                createdBy,
                createdAt).toList());
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

    private ContextMenu showContextMenu(TableRow<SaleMaster> obj) {
        var contextMenu = new ContextMenu();
        var delete = new MenuItem("Delete");
        var view = new MenuItem("View");
        var returnSale = new MenuItem("Return");

        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            SaleMasterViewModel.deleteSaleMaster(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getCustomerName() + "'s order", this));
        view.setOnAction(
                event -> {
                    viewShow(obj.getItem());
                    event.consume();
                });
        returnSale.setOnAction(
                e -> {
                    SaleMasterViewModel.getSaleMaster(obj.getItem().getId(), this::showReturnForm, this::errorMessage);
                    e.consume();
                });
        contextMenu.getItems().addAll(view, returnSale, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    private void showReturnForm() {
        var dialog = new ModalContentHolder(500, -1);
        dialog.getChildren().add(new SaleReturnMasterForm(modalPane));
        dialog.setPadding(new Insets(5d));
        modalPane.setAlignment(Pos.TOP_RIGHT);
        modalPane.usePredefinedTransitionFactories(Side.RIGHT);
        modalPane.setOutTransitionFactory(node -> Animations.fadeOutRight(node, Duration.millis(400)));
        modalPane.setInTransitionFactory(node -> Animations.slideInRight(node, Duration.millis(400)));
        modalPane.show(dialog);
        modalPane.setPersistent(true);
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

    private void setupTableColumns() {
        saleCustomer.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        saleCustomer.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getCustomerName());
            }
        });
        saleDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        saleDate.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleMaster item, boolean empty) {
                super.updateItem(item, empty);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedAt()) ? null : item.getCreatedAt().format(dtf));
            }
        });
        saleGrandTotal.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        saleGrandTotal.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getTotal()));
            }
        });
        saleAmountPaid.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        saleAmountPaid.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getAmountPaid()));
            }
        });
        saleAmountDue.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        saleAmountDue.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getAmountDue()));
            }
        });
        saleStatus.setCellValueFactory(new PropertyValueFactory<>("saleStatus"));
        salePaymentStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        createdBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedBy()) ? null : item.getCreatedBy().getName());
            }
        });
        createdAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedAt()) ? null : item.getCreatedAt().format(dtf));
            }
        });
        updatedBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedBy()) ? null : item.getUpdatedBy().getName());
            }
        });
        updatedAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedAt()) ? null : item.getUpdatedAt().format(dtf));
            }
        });
    }
}
