package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import java.util.*;
import java.util.stream.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class ProductCategoryPage extends OutlinePage {
    private TextField searchBar;
    private TableView<ProductCategory> masterTable;
    private MFXProgressSpinner progress;
    private Button createBtn;

    public ProductCategoryPage() {
        addNode(init());
        progress.setManaged(true);
        progress.setVisible(true);
        ProductCategoryViewModel.getAllProductCategories(this::onDataInitializationSuccess, this::errorMessage);
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
        searchBar.setPromptText("Search product categories");
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
        TableColumn<ProductCategory, String> name = new TableColumn<>("Name");
        TableColumn<ProductCategory, String> description = new TableColumn<>("Description");

        name.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));
        description.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));

        var columnList = new LinkedList<>(Stream.of(name, description).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);
        getProductCategoryTable();

        masterTable.setItems(ProductCategoryViewModel.getCategories());
    }

    private void getProductCategoryTable() {
        masterTable.setPrefSize(1000, 1000);

        masterTable.setRowFactory(
                t -> {
                    TableRow<ProductCategory> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<ProductCategory>) event.getSource())
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

    private MFXContextMenu showContextMenu(TableRow<ProductCategory> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            ProductCategoryViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getName(), this));
        // Edit
        edit.setOnAction(
                e -> {
                    ProductCategoryViewModel.getItem(obj.getItem().getId(), () -> SpotyDialog.createDialog(new ProductCategoryForm(), this).showAndWait(), this::errorMessage);
                    e.consume();
                });
        contextMenu.addItems(edit, delete);
        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> SpotyDialog.createDialog(new ProductCategoryForm(), this).showAndWait());
    }

    private void onSuccess() {
        ProductCategoryViewModel.getAllProductCategories(null, null);
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
                ProductCategoryViewModel.getAllProductCategories(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            ProductCategoryViewModel.searchItem(nv, () -> progress.setVisible(false), this::errorMessage);
        });
    }
}
