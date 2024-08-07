package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.accounting.AccountTransactionViewModel;
import inc.nomard.spoty.core.viewModels.quotations.QuotationMasterViewModel;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.forms.QuotationMasterForm;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.layout.SideModalPane;
import inc.nomard.spoty.core.views.layout.SpotyDialog;
import inc.nomard.spoty.core.views.layout.message.SpotyMessage;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import inc.nomard.spoty.core.views.previews.QuotationPreviewController;
import inc.nomard.spoty.core.views.util.NodeUtils;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.network_bridge.dtos.quotations.QuotationMaster;
import inc.nomard.spoty.utils.AppUtils;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Screen;
import javafx.util.Duration;
import lombok.extern.java.Log;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

@SuppressWarnings("unchecked")
@Log
public class QuotationPage extends OutlinePage {
    private final SideModalPane modalPane;
    private TextField searchBar;
    private TableView<QuotationMaster> masterTable;
    private MFXProgressSpinner progress;
    private Button createBtn;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;
    private TableColumn<QuotationMaster, String> reference;
    private TableColumn<QuotationMaster, QuotationMaster> customer;
    private TableColumn<QuotationMaster, QuotationMaster> total;
    private TableColumn<QuotationMaster, String> status;
    private TableColumn<QuotationMaster, QuotationMaster> createdBy;
    private TableColumn<QuotationMaster, QuotationMaster> createdAt;
    private TableColumn<QuotationMaster, QuotationMaster> updatedBy;
    private TableColumn<QuotationMaster, QuotationMaster> updatedAt;

    public QuotationPage() {
        modalPane = new SideModalPane();

        try {
            viewDialogPane();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        getChildren().addAll(modalPane, init());
        progress.setManaged(true);
        progress.setVisible(true);
        AccountTransactionViewModel.getAllTransactions(this::onDataInitializationSuccess, this::errorMessage);

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
        searchBar.setPromptText("Search quotations");
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
        reference = new TableColumn<>("Ref");
        customer = new TableColumn<>("Customer");
        total = new TableColumn<>("Total Amount");
        status = new TableColumn<>("Status");
        createdBy = new TableColumn<>("Created By");
        createdAt = new TableColumn<>("Created At");
        updatedBy = new TableColumn<>("Updated By");
        updatedAt = new TableColumn<>("Updated At");

        reference.prefWidthProperty().bind(masterTable.widthProperty().multiply(.05));
        customer.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        total.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        status.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        createdBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        createdAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        updatedBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        updatedAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(reference,
                customer,
                total,
                status,
                createdBy,
                createdAt,
                updatedBy,
                updatedAt).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);
        getQuotationMasterTable();

        masterTable.setItems(QuotationMasterViewModel.getQuotations());
    }

    private void getQuotationMasterTable() {
        masterTable.setPrefSize(1000, 1000);

        masterTable.setRowFactory(
                t -> {
                    TableRow<QuotationMaster> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<QuotationMaster>) event.getSource())
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

    private ContextMenu showContextMenu(TableRow<QuotationMaster> obj) {
        var contextMenu = new ContextMenu();
        var view = new MenuItem("View");
        var delete = new MenuItem("Delete");
        var edit = new MenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            QuotationMasterViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getCustomerName() + "'s quotation", this));
        // Edit
        edit.setOnAction(
                e -> {
                    QuotationMasterViewModel.getQuotationMaster(obj.getItem().getId(), this::createBtnAction, this::errorMessage);
                    e.consume();
                });
        // View
        view.setOnAction(
                event -> {
                    viewShow(obj.getItem());
                    event.consume();
                });
        contextMenu.getItems().addAll(view, edit, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> {
            var dialog = new ModalContentHolder(500, -1);
            dialog.getChildren().add(new QuotationMasterForm(modalPane));
            dialog.setPadding(new Insets(5d));
            modalPane.setAlignment(Pos.TOP_RIGHT);
            modalPane.usePredefinedTransitionFactories(Side.RIGHT);
            modalPane.setOutTransitionFactory(node -> Animations.fadeOutRight(node, Duration.millis(400)));
            modalPane.setInTransitionFactory(node -> Animations.slideInRight(node, Duration.millis(400)));
            modalPane.show(dialog);
            modalPane.setPersistent(true);
        });
    }

    private void onSuccess() {
        QuotationMasterViewModel.getAllQuotationMasters(null, null);
    }

    private void viewDialogPane() throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/QuotationPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new QuotationPreviewController());
        MFXGenericDialog dialogContent = viewFxmlLoader.load();
        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialogContent.setPrefHeight(screenHeight * .98);
        dialogContent.setPrefWidth(700);
        viewDialog = SpotyDialog.createDialog(dialogContent, this);
    }

    public void viewShow(QuotationMaster quotationMaster) {
        QuotationPreviewController controller = viewFxmlLoader.getController();
        controller.init(quotationMaster);
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
                QuotationMasterViewModel.getAllQuotationMasters(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            QuotationMasterViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }

    private void setupTableColumns() {
        reference.setCellValueFactory(new PropertyValueFactory<>("ref"));
        customer.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        customer.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(QuotationMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getCustomerName());
            }
        });
        total.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        total.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(QuotationMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getTotal()));
            }
        });
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        createdBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(QuotationMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedBy()) ? null : item.getCreatedBy().getName());
            }
        });
        createdAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(QuotationMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedAt()) ? null : item.getCreatedAt().format(dtf));
            }
        });
        updatedBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(QuotationMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedBy()) ? null : item.getUpdatedBy().getName());
            }
        });
        updatedAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(QuotationMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedAt()) ? null : item.getUpdatedAt().format(dtf));
            }
        });
    }
}
