package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.forms.ProductForm;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.layout.SideModalPane;
import inc.nomard.spoty.core.views.previews.ProductPreview;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.utils.AppUtils;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.navigation.Spacer;
import inc.nomard.spoty.core.views.components.SpotyProgressSpinner;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import lombok.extern.log4j.Log4j2;

import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
@Log4j2
public class ProductPage extends OutlinePage {
    private final SideModalPane modalPane;
    private TextField searchBar;
    private TableView<Product> masterTable;
    private SpotyProgressSpinner progress;
    private Button createBtn;
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

    public ProductPage() {
        modalPane = new SideModalPane();
        getChildren().addAll(modalPane, init());
        progress.setManaged(true);
        progress.setVisible(true);
        modalPane.displayProperty().addListener((observableValue, closed, open) -> {
            if (!open) {
                modalPane.setAlignment(Pos.CENTER);
                modalPane.usePredefinedTransitionFactories(null);
            }
        });
        createBtn.setDisable(true);

        CompletableFuture<Void> allDataInitialization = CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> ProductCategoryViewModel.getAllProductCategories(null, null, null, null)),
                CompletableFuture.runAsync(() -> BrandViewModel.getAllBrands(null, null, null, null)),
                CompletableFuture.runAsync(() -> UOMViewModel.getAllUOMs(null, null, null, null)),
                CompletableFuture.runAsync(() -> DiscountViewModel.getDiscounts(null, null, null, null)),
                CompletableFuture.runAsync(() -> TaxViewModel.getTaxes(null, null, null, null)),
                CompletableFuture.runAsync(() -> ProductViewModel.getAllProducts(null, null, null, null)));

        allDataInitialization.thenRun(this::onDataInitializationSuccess)
                .exceptionally(this::onDataInitializationFailure);
    }

    private Void onDataInitializationFailure(Throwable throwable) {
        SpotyLogger.writeToFile(throwable, EmployeePage.class);
        this.errorMessage("An error occurred while loading view");
        createBtn.setDisable(true);
        return null;
    }

    private void onDataInitializationSuccess() {
        progress.setManaged(false);
        progress.setVisible(false);
        createBtn.setDisable(false);
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
        progress = new SpotyProgressSpinner();
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

    private VBox buildCenter() {
        masterTable = new TableView<>();
        VBox.setVgrow(masterTable, Priority.ALWAYS);
        HBox.setHgrow(masterTable, Priority.ALWAYS);
        var paging = new HBox(new Spacer(), buildPagination(), new Spacer(), buildPageSize());
        paging.setPadding(new Insets(0d, 20d, 0d, 5d));
        paging.setAlignment(Pos.CENTER);
        if (ProductViewModel.getTotalPages() > 0) {
            paging.setVisible(true);
            paging.setManaged(true);
        } else {
            paging.setVisible(false);
            paging.setManaged(false);
        }
        ProductViewModel.totalPagesProperty().addListener((observableValue, oldNum, newNum) -> {
            if (ProductViewModel.getTotalPages() > 0) {
                paging.setVisible(true);
                paging.setManaged(true);
            } else {
                paging.setVisible(false);
                paging.setManaged(false);
            }
        });
        var centerHolder = new VBox(masterTable, paging);
        VBox.setVgrow(centerHolder, Priority.ALWAYS);
        HBox.setHgrow(centerHolder, Priority.ALWAYS);
        return centerHolder;
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
                createdAt).toList());
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
                viewShow(obj.getItem());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            event.consume();
        });
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(AppManager.getGlobalModalPane(), () -> {
            ProductViewModel.deleteProduct(obj.getItem().getId(), this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getName()).showDialog());
        // Edit
        edit.setOnAction(
                event -> {
                    ProductViewModel.getProduct(obj.getItem().getId(), this::showFormDialog, this::errorMessage);
                    event.consume();
                });
        contextMenu.getItems().addAll(view, edit, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(_ -> {
                this.showFormDialog();
        });
    }

    private void showFormDialog() {
        var dialog = new ModalContentHolder(700, -1);
        dialog.getChildren().add(new ProductForm(modalPane));
        dialog.setPadding(new Insets(5d));
        modalPane.setAlignment(Pos.TOP_RIGHT);
        modalPane.usePredefinedTransitionFactories(Side.RIGHT);
        modalPane.setOutTransitionFactory(node -> Animations.fadeOutRight(node, Duration.millis(400)));
        modalPane.setInTransitionFactory(node -> Animations.slideInRight(node, Duration.millis(400)));
        modalPane.show(dialog);
        modalPane.setPersistent(true);
    }

    public void viewShow(Product product) {
        var scrollPane = new ScrollPane(new ProductPreview(product, modalPane));
        scrollPane.setMaxHeight(10_000);

        var dialog = new ModalContentHolder(710, -1);
        dialog.getChildren().add(scrollPane);
        dialog.setPadding(new Insets(5d));
        modalPane.show(dialog);
        modalPane.setPersistent(true);
    }

    private void onSuccess() {
        ProductViewModel.getAllProducts(null, null, null, null);
    }

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        progress.setManaged(false);
        progress.setVisible(false);
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                ProductViewModel.getAllProducts(null, null, null, null);
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
                this.setAlignment(Pos.CENTER_RIGHT);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getCostPrice()));
            }
        });
        salePrice.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        salePrice.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getSalePrice()));
            }
        });
        productQuantity.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        productQuantity.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getQuantity()));
            }
        });
        tax.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        tax.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getTax()) ? null : AppUtils.decimalFormatter().format(item.getTax().getPercentage()));
            }
        });
        discount.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        discount.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);
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
                this.setAlignment(Pos.CENTER_RIGHT);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedAt()) ? null : item.getCreatedAt().format(dtf));
            }
        });
    }

    private Pagination buildPagination() {
        var pagination = new Pagination(ProductViewModel.getTotalPages(), 0);
        pagination.setMaxPageIndicatorCount(5);
        pagination.pageCountProperty().bindBidirectional(ProductViewModel.totalPagesProperty());
        pagination.setPageFactory(pageNum -> {
            progress.setManaged(true);
            progress.setVisible(true);
            ProductViewModel.getAllProducts(() -> {
                progress.setManaged(false);
                progress.setVisible(false);
            }, null, pageNum, ProductViewModel.getPageSize());
            ProductViewModel.setPageNumber(pageNum);
            return new StackPane(); // null isn't allowed
        });
        return pagination;
    }

    private ComboBox<Integer> buildPageSize() {
        var pageSize = new ComboBox<Integer>();
        pageSize.setItems(FXCollections.observableArrayList(25, 50, 75, 100));
        pageSize.valueProperty().bindBidirectional(ProductViewModel.pageSizeProperty().asObject());
        pageSize.valueProperty().addListener(
                (observableValue, integer, t1) -> {
                    progress.setManaged(true);
                    progress.setVisible(true);
                    ProductViewModel
                            .getAllProducts(
                                    () -> {
                                        progress.setManaged(false);
                                        progress.setVisible(false);
                                    },
                                    null,
                                    ProductViewModel.getPageNumber(),
                                    t1);
                    ProductViewModel.setPageSize(t1);
                });
        return pageSize;
    }
}
