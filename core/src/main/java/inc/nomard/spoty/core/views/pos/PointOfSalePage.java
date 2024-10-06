package inc.nomard.spoty.core.views.pos;

import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.sales.SaleDetailViewModel;
import inc.nomard.spoty.core.viewModels.sales.SaleMasterViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.SpotyProgressSpinner;
import inc.nomard.spoty.core.views.pages.EmployeePage;
import inc.nomard.spoty.core.views.pos.components.ProductCard;
import inc.nomard.spoty.core.views.util.FunctionalStringConverter;
import inc.nomard.spoty.core.views.util.NodeUtils;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.sales.SaleDetail;
import inc.nomard.spoty.utils.AppUtils;
import inc.nomard.spoty.utils.SpotyLogger;
import io.github.palexdev.virtualizedfx.grid.VFXGrid;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import lombok.extern.log4j.Log4j2;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

@Log4j2
public class PointOfSalePage extends OutlinePage {
    public TableColumn<SaleDetail, SaleDetail> productDiscount;
    private ToggleGroup toggleGroup;
    private TableView<SaleDetail> cart;
    private TextField searchBar;
    private ComboBox<Discount> discount;
    private CustomButton checkOutBtn;
    private Button emptyCartBtn;
    // private BootstrapPane productHolder;
    private TableColumn<SaleDetail, SaleDetail> cartName;
    private TableColumn<SaleDetail, SaleDetail> cartQuantity;
    private TableColumn<SaleDetail, SaleDetail> productTax;
    private TableColumn<SaleDetail, SaleDetail> cartSubTotal;
    private TableColumn<SaleDetail, SaleDetail> cartActions;
    private ComboBox<Tax> tax;
    private SpotyProgressSpinner progress;
    private Long availableProductQuantity = 0L;


    public PointOfSalePage() {
        addNode(init());
        setSearchBar();
        setPOSComboBoxes();
        setCheckoutProductsTable();
        progress.setManaged(true);
        progress.setVisible(true);

        CompletableFuture<Void> allDataInitialization = CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> ProductViewModel.getAllProductsNonPaged(null, null)),
                CompletableFuture.runAsync(() -> CustomerViewModel.getAllCustomers(null, null, null, null)),
                CompletableFuture.runAsync(() -> TaxViewModel.getTaxes(null, null, null, null)),
                CompletableFuture.runAsync(() -> DiscountViewModel.getDiscounts(null, null, null, null)));

        allDataInitialization.thenRun(this::onDataInitializationSuccess)
                .exceptionally(this::onDataInitializationFailure);
    }

    private Void onDataInitializationFailure(Throwable throwable) {
        SpotyLogger.writeToFile(throwable, EmployeePage.class);
        this.errorMessage("An error occurred while loading view");
        return null;
    }

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        progress.setManaged(false);
        progress.setVisible(false);
    }

    private void onDataInitializationSuccess() {
        progress.setManaged(false);
        progress.setVisible(false);
        SaleMasterViewModel.setDefaultCustomer();
        cartListeners();
    }

    // Top UI.
    private HBox buildTop() {
        var hbox = new HBox();
        BorderPane.setAlignment(hbox, Pos.CENTER);
        return hbox;
    }

    // Header UI.
    private HBox buildLeftHeaderPane() {
        progress = new SpotyProgressSpinner();
        progress.setMinSize(30d, 30d);
        progress.setPrefSize(30d, 30d);
        progress.setMaxSize(30d, 30d);
        progress.setVisible(false);
        progress.setManaged(false);
        var hbox = new HBox(progress);
        hbox.setSpacing(10d);
        BorderPane.setAlignment(hbox, Pos.CENTER_LEFT);
        HBox.setHgrow(hbox, Priority.ALWAYS);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        return hbox;
    }

    private HBox buildCenterHeaderPane() {
        searchBar = new TextField();
        searchBar.setPromptText("Search products");
        searchBar.setMinWidth(300d);
        searchBar.setPrefWidth(500d);
        searchBar.setMaxWidth(700d);
        var hbox = new HBox(searchBar);
        hbox.setSpacing(10d);
        BorderPane.setAlignment(hbox, Pos.CENTER);
        HBox.setHgrow(hbox, Priority.ALWAYS);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        return hbox;
    }

    private HBox buildRightHeaderPane() {
        var hbox = new HBox();
        hbox.setSpacing(10d);
        BorderPane.setAlignment(hbox, Pos.CENTER_RIGHT);
        HBox.setHgrow(hbox, Priority.ALWAYS);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        return hbox;
    }

    private HBox buildHeaderUI() {
        var hbox = new HBox();
        hbox.setSpacing(10d);
        hbox.getStyleClass().add("card-flat-bottom");
        hbox.setPadding(new Insets(5d));
        hbox.getChildren().addAll(buildLeftHeaderPane(),
                buildCenterHeaderPane(),
                buildRightHeaderPane());
        return hbox;
    }

    // Filter UI.
    private ScrollPane buildFilterUI() {
        toggleGroup = new ToggleGroup();

        var filterPane = new HBox();
        filterPane.setAlignment(Pos.CENTER_LEFT);
        filterPane.setSpacing(10d);
        filterPane.setPadding(new Insets(5d));
        setCategoryFilters(filterPane);
        updateCategoryFilters(filterPane);

        var scroll = new ScrollPane(filterPane);
        scroll.maxHeight(120d);
        scroll.prefHeight(100d);
        scroll.minHeight(80d);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        configureFilterScrollPane(scroll);
        return scroll;
    }

    // Product CardHolder UI.
    private VFXGrid buildProductCardHolderUI() {
        return setProductsGridView();
    }

    // Product UI.
    private VBox buildProductUI() {
        var vbox = new VBox();
        vbox.getStyleClass().add("card-flat-top");
        vbox.setPadding(new Insets(2.5d));
        VBox.setVgrow(vbox, Priority.ALWAYS);

        var vbox1 = new VBox(buildFilterUI());
        vbox1.maxHeight(120d);
        vbox1.prefHeight(100d);
        vbox1.minHeight(80d);

        vbox.getChildren().addAll(vbox1, buildProductCardHolderUI());
        return vbox;
    }

    // Center UI.
    private VBox buildCenter() {
        var vbox = new VBox();
        BorderPane.setAlignment(vbox, Pos.CENTER);
        BorderPane.setMargin(vbox, new Insets(0d, 5d, 0d, 0d));
        vbox.getChildren().addAll(buildHeaderUI(), buildProductUI());
        return vbox;
    }

    // Customer UI.
    private VBox buildCustomerUI() {
        var customerComboBox = new ComboBox<Customer>();
        customerComboBox.setEditable(true);
        customerComboBox.setPromptText("Customer");
        customerComboBox.setPrefWidth(1000d);
        setupFilterComboBox(customerComboBox, CustomerViewModel.getCustomers(), SaleMasterViewModel.customerProperty(),
                customer -> (customer == null) ? "" : customer.getName());
        HBox.setHgrow(customerComboBox, Priority.ALWAYS);
        var vbox = new VBox(customerComboBox);
        vbox.setPrefWidth(538d);
        vbox.setSpacing(10d);
        return vbox;
    }

    // Cart UI.
    private TableView<SaleDetail> buildCartTable() {
        // Table columns.
        cartName = new TableColumn<>("Name");
        cartName.setEditable(false);
        cartName.setSortable(false);
        cartName.setPrefWidth(75d);
        cartQuantity = new TableColumn<>("Qnty");
        cartQuantity.setEditable(true);
        cartQuantity.setSortable(false);
        cartQuantity.setPrefWidth(45d);
        productTax = new TableColumn<>("Tax(%)");
        productTax.setEditable(false);
        productTax.setSortable(false);
        productTax.setPrefWidth(40d);
        productDiscount = new TableColumn<>("Discount(%)");
        productDiscount.setEditable(false);
        productDiscount.setSortable(false);
        productDiscount.setPrefWidth(60d);
        cartSubTotal = new TableColumn<>("Total");
        cartSubTotal.setEditable(false);
        cartSubTotal.setSortable(false);
        cartSubTotal.setPrefWidth(70d);
        cartActions = new TableColumn<>();
        cartActions.setEditable(false);
        cartActions.setSortable(false);
        cartActions.setPrefWidth(30d);
        var columnList = new LinkedList<>(Stream.of(cartName,
                cartQuantity,
                productTax,
                productDiscount,
                cartSubTotal,
                cartActions).toList());
        // Table.
        cart = new TableView<>();
        NodeUtils.setAnchors(cart, new Insets(0d));
        cart.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        cart.getColumns().addAll(columnList);
        return cart;
    }

    private ScrollPane buildCartScroll() {
        var cartItemHolder = new VBox();
        cartItemHolder.setPrefHeight(200d);
        cartItemHolder.setPrefWidth(100d);
        var scrollPane = new ScrollPane(cartItemHolder);
        scrollPane.setManaged(false);
        scrollPane.setVisible(false);
        NodeUtils.setAnchors(scrollPane, new Insets(0d));
        return scrollPane;
    }

    private AnchorPane buildCartUI() {
        var pane = new AnchorPane();
        VBox.setVgrow(pane, Priority.ALWAYS);
        pane.getChildren().addAll(buildCartTable(), buildCartScroll());
        return pane;
    }

    private void cartListeners() {
        SaleDetailViewModel.getSaleDetails().addListener((ListChangeListener<? super SaleDetail>) change -> {
            if (SaleDetailViewModel.getSaleDetails().isEmpty()) {
                checkOutBtn.setDisable(true);
                emptyCartBtn.setDisable(true);
                discount.setDisable(true);
                tax.setDisable(true);
            }
            if (!SaleDetailViewModel.getSaleDetails().isEmpty()) {
                checkOutBtn.setDisable(false);
                emptyCartBtn.setDisable(false);
                discount.setDisable(false);
                tax.setDisable(false);
            }
        });
    }

    // Deductions UI.
    private HBox buildDeductions() {
        discount = new ComboBox<>();
        discount.setPromptText("Discount(%)");
        discount.setPrefWidth(500d);
        discount.setDisable(true);
        tax = new ComboBox<>();
        tax.setPromptText("Tax(%)");
        tax.setPrefWidth(500d);
        tax.setDisable(true);
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10d);
        hbox.getChildren().addAll(discount, tax);
        return hbox;
    }

    // Cart Buttons UI.
    private HBox buildButtons() {
        checkOutBtn = new CustomButton("CheckOut $0.00");
        checkOutBtn.setOnAction(event -> savePOSSale());
        checkOutBtn.setPrefWidth(650d);
        checkOutBtn.getStyleClass().add(Styles.ACCENT);
        checkOutBtn.setDisable(true);
        bindTotalLabels();
        emptyCartBtn = new Button("Empty Cart");
        emptyCartBtn.setOnAction(event -> clearCart());
        emptyCartBtn.setPrefWidth(400d);
        emptyCartBtn.getStyleClass().addAll(Styles.DANGER);
        emptyCartBtn.setDisable(true);
        emptyCartBtn.setCancelButton(true);
        var region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        var hbox = new HBox();
        hbox.setSpacing(5d);
        hbox.getChildren().addAll(checkOutBtn, region, emptyCartBtn);
        return hbox;
    }

    // Cart Bottom UI.
    private VBox buildCartBottomUI() {
        var vbox = new VBox();
        vbox.setSpacing(20d);
        vbox.setPadding(new Insets(5d, 10d, 10d, 10d));
        vbox.getChildren().addAll(buildDeductions(), buildButtons());
        return vbox;
    }

    // Right UI.
    private VBox buildRight() {
        var vbox = new VBox();
        vbox.setMaxWidth(508d);
        vbox.setPrefWidth(508d);
        vbox.setMinWidth(338d);
        vbox.getStyleClass().add("card-flat");
        vbox.setSpacing(10d);
        vbox.setPadding(new Insets(10d, 5d, 5d, 5d));
        BorderPane.setAlignment(vbox, Pos.CENTER);
        BorderPane.setMargin(vbox, new Insets(0d, 0d, 0d, 5d));
        vbox.getChildren().addAll(buildCustomerUI(), buildCartUI(), buildCartBottomUI());
        return vbox;
    }

    // POS UI.
    private BorderPane init() {
        var pane = new BorderPane();
        pane.setTop(buildTop());
        pane.setCenter(buildCenter());
        pane.setRight(buildRight());
        return pane;
    }


    private void setPOSComboBoxes() {
        setupComboBox(discount, DiscountViewModel.getDiscounts(), SaleMasterViewModel.discountProperty(), discount -> (discount == null) ? "" : discount.getName() + " (" + discount.getPercentage() + "%)");
        setupComboBox(tax, TaxViewModel.getTaxes(), SaleMasterViewModel.netTaxProperty(), tax -> (tax == null) ? "" : tax.getName() + " (" + tax.getPercentage() + "%)");
    }

    private <T> void setupFilterComboBox(ComboBox<T> comboBox, ObservableList<T> items,
                                         Property<T> property, Function<T, String> converter) {
        if (property != null) {
            comboBox.valueProperty().bindBidirectional(property);
        }
        if (converter != null) {
            StringConverter<T> itemConverter = FunctionalStringConverter.to(converter);
            comboBox.setConverter(itemConverter);
        }
        if (items.isEmpty()) {
            items.addListener((ListChangeListener<T>) c -> comboBox.setItems(items));
        } else {
            comboBox.itemsProperty().bindBidirectional(new SimpleListProperty<>(items));
        }
    }

    private <T> void setupComboBox(ComboBox<T> comboBox, ObservableList<T> items,
                                   Property<T> property, Function<T, String> converter) {
        if (property != null) {
            comboBox.valueProperty().bindBidirectional(property);
        }
        if (converter != null) {
            StringConverter<T> itemConverter = FunctionalStringConverter.to(converter);
            comboBox.setConverter(itemConverter);
        }
        if (items.isEmpty()) {
            items.addListener((ListChangeListener<T>) c -> comboBox.setItems(items));
        } else {
            comboBox.itemsProperty().bindBidirectional(new SimpleListProperty<>(items));
        }
    }

    private VFXGrid setProductsGridView() {
        var productsGridView = new VFXGrid<>();
        productsGridView.autoArrange();
        productsGridView.makeScrollable();
        productsGridView.setSpacing(5d, 5d);
        productsGridView.setPadding(new Insets(5));

        productsGridView.setPrefSize(400, 400);
        productsGridView.setColumnsNum(3); // Set the number of columns

        productsGridView.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            progress.setManaged(true);
            progress.setVisible(true);
            productsGridView.getItems().clear();
            loadAllProducts(productsGridView);
            progress.setManaged(false);
            progress.setVisible(false);
        });

        ProductViewModel.getProducts().addListener((ListChangeListener<Product>) c -> loadAllProducts(productsGridView));

        loadAllProducts(productsGridView);
        return productsGridView;
    }

    private void addProductToGridPane(VFXGrid productsGridView, Product product) {
        ProductCard productCard = new ProductCard(product);
        configureProductCardAction(productCard);
        productsGridView.getItems().addAll(productCard);
    }

    private void loadAllProducts(VFXGrid productsGridView) {
        for (Product product : ProductViewModel.getProducts()) {
            addProductToGridPane(productsGridView, product);
        }
    }

    private void configureFilterScrollPane(ScrollPane scrollPane) {
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                event.consume();
            }
        });
    }

    private void configureProductCardAction(ProductCard productCard) {
        productCard.setOnMouseClicked(event -> handleProductCardClick(productCard));
    }

    private void handleProductCardClick(ProductCard productCard) {
        Optional<SaleDetail> existingSaleDetail = SaleDetailViewModel.getSaleDetails().stream()
                .filter(saleDetail -> saleDetail.getProduct() == productCard.getProduct()).findAny();

        existingSaleDetail.ifPresentOrElse(saleDetail -> updateExistingSaleDetail(saleDetail, productCard),
                () -> addNewSaleDetail(productCard));
    }

    private void updateExistingSaleDetail(SaleDetail saleDetail, ProductCard productCard) {
        try {
            long quantity = calculateQuantity(saleDetail);
            availableProductQuantity = quantity;

            if (productCard.getProduct().getQuantity() >= availableProductQuantity) {
                SaleDetailViewModel.getCartSale(saleDetail);
                updateSaleDetail(saleDetail, productCard, quantity);
            } else {
                SpotyUtils.errorMessage("Product out of stock");
            }
        } catch (Exception e) {
            SpotyLogger.writeToFile(e, this.getClass());
        }
    }

    private void updateSaleDetail(SaleDetail saleDetail, ProductCard productCard, long quantity) {
        SaleDetailViewModel.setProduct(productCard.getProduct());
        SaleDetailViewModel.setPrice(productCard.getProduct().getSalePrice());
        SaleDetailViewModel.setQuantity(quantity);
        SaleDetailViewModel.setSubTotalPrice(calculateSubTotal(productCard.getProduct()) + saleDetail.getUnitPrice());
        SaleDetailViewModel.updateCartSale((long) SaleDetailViewModel.getSaleDetails().indexOf(saleDetail));
    }

    private void addNewSaleDetail(ProductCard productCard) {
        if (productCard.getProduct().getQuantity() > 0) {
            SaleDetailViewModel.setProduct(productCard.getProduct());
            SaleDetailViewModel.setPrice(productCard.getProduct().getSalePrice());
            SaleDetailViewModel.setQuantity(1L);
            availableProductQuantity = 1L;
            SaleDetailViewModel.setSubTotalPrice(calculateSubTotal(productCard.getProduct()));
            SaleDetailViewModel.addSaleDetail();
        } else {
            SpotyUtils.errorMessage("Product out of stock");
        }
    }

    private double calculateSubTotal(Product product) {
        double subTotal = product.getSalePrice();

        if (Objects.nonNull(product.getTax()) && product.getTax().getPercentage() > 0) {
            subTotal += (product.getTax().getPercentage() / 100) * subTotal;
        }
        if (Objects.nonNull(product.getDiscount()) && product.getDiscount().getPercentage() > 0) {
            subTotal += (product.getDiscount().getPercentage() / 100) * subTotal;
        }
        return subTotal;
    }

    private double calculateTotal(ObservableList<SaleDetail> saleDetails) {
        return saleDetails.stream().mapToDouble(SaleDetail::getUnitPrice).sum();
    }

    private long calculateQuantity(SaleDetail saleDetail) {
        return saleDetail.getQuantity() + 1;
    }

    private void setCategoryFilters(HBox node) {
        ProductCategoryViewModel.getCategories().forEach(productCategory -> {
            ToggleButton toggleButton = createToggle(productCategory.getName(), toggleGroup);
            node.getChildren().add(toggleButton);
        });
    }

    private void updateCategoryFilters(HBox node) {
        ProductCategoryViewModel.getCategories().addListener((ListChangeListener<ProductCategory>) c -> {
            node.getChildren().clear();
            ProductCategoryViewModel.getCategories().forEach(productCategory -> {
                ToggleButton toggleButton = createToggle(productCategory.getName(), toggleGroup);
                node.getChildren().add(toggleButton);
            });
        });
    }

    private ToggleButton createToggle(String text, ToggleGroup toggleGroup) {
        ToggleButton toggleNode = new ToggleButton(text);
        toggleNode.setAlignment(Pos.CENTER_LEFT);
        toggleNode.setMaxWidth(Double.MAX_VALUE);
        toggleNode.setToggleGroup(toggleGroup);
        toggleNode.setAlignment(Pos.CENTER);
        toggleNode.getStyleClass().add("shadow");
        return toggleNode;
    }

    private void setCheckoutProductsTable() {
        setupCartTableColumns();

        SaleDetailViewModel.getSaleDetails().addListener((ListChangeListener<SaleDetail>) c -> {
            cart.setItems(SaleDetailViewModel.getSaleDetails());
            enableUIOnSaleDetailsChange();
        });
    }

    private void setupCartTableColumns() {
        cartName.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        cartName.setCellFactory(tc -> new TableCell<>() {
            @Override
            public void updateItem(SaleDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getProductName());
            }
        });

        cartQuantity.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        cartQuantity.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getQuantity()));
            }
        });
        cartSubTotal.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        cartSubTotal.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getUnitPrice()));
            }
        });

        cartActions.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        cartActions.setCellFactory(param -> new TableCell<>() {
            final Button deleteIcon = new Button(null, new FontIcon(Feather.TRASH));

            @Override
            protected void updateItem(SaleDetail item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setGraphic(null);
                } else {
                    deleteIcon.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.FLAT, Styles.DANGER);
                    setGraphic(deleteIcon);
                    deleteIcon.setOnAction(event -> removeItemFromCart(item));
                }
            }
        });

        productTax.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        productTax.setCellFactory(param -> new TableCell<>() {
            final Label value = new Label();

            @Override
            protected void updateItem(SaleDetail item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(value);
                    value.setText(
                            (Objects.nonNull(item.getProduct().getTax()))
                                    ? String.valueOf(item.getProduct().getTax().getPercentage())
                                    : "0.00"
                    );
                }
            }
        });

        productDiscount.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        productDiscount.setCellFactory(param -> new TableCell<>() {
            final Label value = new Label();

            @Override
            protected void updateItem(SaleDetail item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(value);
                    value.setText(
                            (Objects.nonNull(item.getProduct().getDiscount()))
                                    ? String.valueOf(item.getProduct().getDiscount().getPercentage())
                                    : "0.00"
                    );
                }
            }
        });
    }

    private void removeItemFromCart(SaleDetail item) {
        cart.getItems().remove(item);
        availableProductQuantity = 0L;
    }

    private void enableUIOnSaleDetailsChange() {
        if (checkOutBtn.isDisabled()) {
            checkOutBtn.setDisable(false);
        }
        if (emptyCartBtn.isDisabled()) {
            emptyCartBtn.setDisable(false);
        }
        if (discount.isDisabled()) {
            discount.setDisable(false);
        }
        if (tax.isDisabled()) {
            tax.setDisable(false);
        }
    }

    private void bindTotalLabels() {
        SaleDetailViewModel.getSaleDetails().addListener((ListChangeListener<SaleDetail>) listener ->
                checkOutBtn.setText("CheckOut: UGX " + AppUtils.decimalFormatter().format(calculateTotal(SaleDetailViewModel.getSaleDetails())))
        );
    }

    public void clearCart() {
        SaleDetailViewModel.getSaleDetails().clear();
        availableProductQuantity = 0L;
        checkOutBtn.setDisable(true);
        emptyCartBtn.setDisable(true);
        discount.setDisable(true);
        tax.setDisable(true);
    }

    public void savePOSSale() {
        SaleMasterViewModel.setSaleStatus("Complete");
        SaleMasterViewModel.setPaymentStatus("Paid");
        double total = calculateTotal(SaleDetailViewModel.getSaleDetails());
        SaleMasterViewModel.setTotal(total);
        SaleMasterViewModel.setAmountPaid(total);
        SaleMasterViewModel.setNotes("Approved.");

        try {
            checkOutBtn.startLoading();
            SaleMasterViewModel.saveSaleMaster(this::onSuccess, SpotyUtils::successMessage, this::displayErrorMessage);
        } catch (Exception e) {
            SpotyLogger.writeToFile(e, this.getClass());
        }
    }

    private void onSuccess() {
        clearCart();
        checkOutBtn.stopLoading();
        SaleMasterViewModel.setDefaultCustomer();
        SaleMasterViewModel.getAllSaleMasters(null, null, null, null);
        ProductViewModel.getAllProductsNonPaged(null, this::displayErrorMessage);
    }

    private void displayErrorMessage(String message) {
        SpotyUtils.errorMessage(message);
        checkOutBtn.stopLoading();
        progress.setManaged(false);
        progress.setVisible(false);
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.equals(oldValue, newValue)) {
                return;
            }
            if (oldValue.isBlank() && newValue.isBlank()) {
                ProductViewModel.getAllProductsNonPaged(null, this::displayErrorMessage);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            ProductViewModel.searchItem(newValue, () -> progress.setVisible(false), this::displayErrorMessage);
        });
    }
}
