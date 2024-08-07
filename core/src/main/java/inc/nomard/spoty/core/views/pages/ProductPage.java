package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.forms.ProductForm;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.SpotyDialog;
import inc.nomard.spoty.core.views.layout.message.SpotyMessage;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import inc.nomard.spoty.core.views.previews.ProductPreviewController;
import inc.nomard.spoty.core.views.util.NodeUtils;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.utils.AppUtils;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
public class ProductPage extends OutlinePage {
    private TextField searchBar;
    private TableView<Product> masterTable;
    private MFXProgressSpinner progress;
    private Button createBtn;
    private MFXStageDialog viewDialog;
    private FXMLLoader viewFxmlLoader;
    private TableColumn<Product, String> productName;
    private TableColumn<Product, Product> productCategory;
    private TableColumn<Product, Product> productBrand;
    private TableColumn<Product, Product> costPrice;
    private TableColumn<Product, Product> salePrice;
    private TableColumn<Product, Product> productQuantity;
    private TableColumn<Product, Product> tax;
    private TableColumn<Product, Product> discount;
    private TableColumn<Product, Product> createdBy;
    private TableColumn<Product, Product> createdAt;
    private TableColumn<Product, Product> updatedBy;
    private TableColumn<Product, Product> updatedAt;

    public ProductPage() {
        try {
            productViewDialogPane();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        addNode(init());
        progress.setManaged(true);
        progress.setVisible(true);
        ProductViewModel.getAllProducts(this::onDataInitializationSuccess, this::errorMessage);
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
        searchBar.setPromptText("Search products");
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

    private void productViewDialogPane() throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/ProductPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new ProductPreviewController());
        MFXGenericDialog dialogContent = viewFxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialogContent.setPrefHeight(screenHeight * .98);
        dialogContent.setPrefWidth(700);
        viewDialog = SpotyDialog.createDialog(dialogContent, this);
    }

    private void setupTable() {
        productName = new TableColumn<>("Name");
        productCategory = new TableColumn<>("Category");
        productBrand = new TableColumn<>("Brand");
        costPrice = new TableColumn<>("Cost Price");
        salePrice = new TableColumn<>("Sale Price");
        productQuantity = new TableColumn<>("Quantity");
        tax = new TableColumn<>("Tax");
        discount = new TableColumn<>("Discount");
        createdBy = new TableColumn<>("Created By");
        createdAt = new TableColumn<>("Created At");
        updatedBy = new TableColumn<>("Updated By");
        updatedAt = new TableColumn<>("Updated At");

        productName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        productCategory.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        productBrand.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        costPrice.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        salePrice.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        productQuantity.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        tax.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        discount.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        createdBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        createdAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        updatedBy.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        updatedAt.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(productName,
                productCategory,
                productBrand,
                costPrice,
                salePrice,
                productQuantity,
                tax,
                discount,
                createdBy,
                createdAt,
                updatedBy,
                updatedAt).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);
        styleTable();

        masterTable.setItems(ProductViewModel.getProducts());
    }

    private void styleTable() {
        masterTable.setPrefSize(1000, 1000);

        masterTable.setRowFactory(
                t -> {
                    TableRow<Product> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<Product>) event.getSource())
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

    private ContextMenu showContextMenu(TableRow<Product> obj) {
        var contextMenu = new ContextMenu();
        var view = new MenuItem("View");
        var delete = new MenuItem("Delete");
        var edit = new MenuItem("Edit");

        // Actions
        // View
        view.setOnAction(event -> {
            try {
                productViewShow(obj.getItem());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            event.consume();
        });
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            ProductViewModel.deleteProduct(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getName(), this));
        // Edit
        edit.setOnAction(
                event -> {
                    ProductViewModel.getProduct(obj.getItem().getId(), () -> SpotyDialog.createDialog(new ProductForm(), this).showAndWait(), this::errorMessage);
                    event.consume();
                });
        contextMenu.getItems().addAll(view, edit, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> SpotyDialog.createDialog(new ProductForm(), this).showAndWait());
    }

    public void productViewShow(Product product) {
        ProductPreviewController controller = viewFxmlLoader.getController();
        controller.init(product);
        viewDialog.showAndWait();
    }

    private void onSuccess() {
        ProductViewModel.getAllProducts(null, null);
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
                ProductViewModel.getAllProducts(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            ProductViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }

    private void setupTableColumns() {
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productCategory.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        productCategory.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getCategoryName());
            }
        });
        productBrand.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        productBrand.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getBrandName());
            }
        });
        costPrice.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        costPrice.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getCostPrice()));
            }
        });
        salePrice.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        salePrice.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getSalePrice()));
            }
        });
        productQuantity.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        productQuantity.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getQuantity()));
            }
        });
        tax.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        tax.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getTax()) ? null : AppUtils.decimalFormatter().format(item.getTax().getPercentage()));
            }
        });
        discount.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        discount.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getDiscount()) ? null : AppUtils.decimalFormatter().format(item.getDiscount().getPercentage()));
            }
        });
        createdBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedBy()) ? null : item.getCreatedBy().getName());
            }
        });
        createdAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedAt()) ? null : item.getCreatedAt().format(dtf));
            }
        });
        updatedBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedBy()) ? null : item.getUpdatedBy().getName());
            }
        });
        updatedAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updatedAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getUpdatedAt()) ? null : item.getUpdatedAt().format(dtf));
            }
        });
    }
}
