package inc.nomard.spoty.core.views.pos;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.sales.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.core.views.pos.components.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.sales.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.mfxresources.fonts.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class PointOfSalePage extends OutlinePage {
    private final ToggleGroup toggleGroup = new ToggleGroup();
    public TableColumn<SaleDetail, SaleDetail> productDiscount;
    private ComboBox<Customer> customer;
    private TableView<SaleDetail> cart;
    private TextField searchBar;
    private ComboBox<Discount> discount;
    private Button checkOutBtn, emptyCartBtn;
    private HBox filterPane;
    private ScrollPane productScrollPane;
    // @FXML
    // private BootstrapPane productHolder;
    private TableColumn<SaleDetail, SaleDetail> cartName;
    private TableColumn<SaleDetail, Long> cartQuantity;
    private TableColumn<SaleDetail, SaleDetail> productTax;
    private TableColumn<SaleDetail, Double> cartSubTotal;
    private TableColumn<SaleDetail, SaleDetail> cartActions;
    private ComboBox<Tax> tax;
    private MFXProgressSpinner progress;
    private Long availableProductQuantity = 0L;

    public PointOfSalePage() {
        addNode(init());
        configureProductScrollPane();
        setSearchBar();
        setPOSComboBoxes();
        initializeCategoryFilters();
        initializeProductsGridView();
        setCheckoutProductsTable();
        bindTotalLabels();
        SaleMasterViewModel.setDefaultCustomer();
    }

    // Top UI.
    private HBox buildTop() {
        var hbox = new HBox();
        BorderPane.setAlignment(hbox, Pos.CENTER);
        return hbox;
    }

    // Header UI.
    private HBox buildLeftHeaderPane() {
        progress = new MFXProgressSpinner();
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
        filterPane = new HBox();
        filterPane.setAlignment(Pos.CENTER_LEFT);
        filterPane.setSpacing(10d);
        filterPane.setPadding(new Insets(5d));
        var scroll = new ScrollPane(filterPane);
        scroll.maxHeight(100d);
        scroll.minHeight(60d);
        scroll.prefHeight(80d);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setVgrow(scroll, Priority.NEVER);
        return scroll;
    }

    // Product Card Holder UI.
    private ScrollPane buildProductCardHolderUI() {
        productScrollPane = new ScrollPane();
        productScrollPane.setFitToHeight(true);
        productScrollPane.setFitToWidth(true);
        productScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        productScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        return productScrollPane;
    }

    // Product UI.
    private VBox buildProductUI() {
        var vbox = new VBox();
        vbox.getStyleClass().add("card-flat");
        vbox.setPadding(new Insets(2.5d));
        VBox.setVgrow(vbox, Priority.ALWAYS);
        vbox.getChildren().addAll(buildFilterUI(), buildProductCardHolderUI());
        return vbox;
    }

    // Center UI.
    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(10d);
        BorderPane.setAlignment(vbox, Pos.CENTER);
        BorderPane.setMargin(vbox, new Insets(0d, 5d, 0d, 0d));
        vbox.getChildren().addAll(buildHeaderUI(), buildProductUI());
        return vbox;
    }

    // Customer UI.
    private VBox buildCustomerUI() {
        customer = new ComboBox<>();
        customer.setEditable(true);
        customer.setPromptText("Customer");
        customer.setPrefWidth(1000d);
        HBox.setHgrow(customer, Priority.ALWAYS);
        var vbox = new VBox(customer);
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
        cartQuantity.setPrefWidth(50d);
        productTax = new TableColumn<>("Tax(%)");
        productTax.setEditable(false);
        productTax.setSortable(false);
        productTax.setPrefWidth(40d);
        productDiscount = new TableColumn<>("Discount(%)");
        productDiscount.setEditable(false);
        productDiscount.setSortable(false);
        productDiscount.setPrefWidth(60d);
        cartSubTotal = new TableColumn<>("Sub Total");
        cartSubTotal.setEditable(false);
        cartSubTotal.setSortable(false);
        cartSubTotal.setPrefWidth(75d);
        cartActions = new TableColumn<>();
        cartActions.setEditable(false);
        cartActions.setSortable(false);
        cartActions.setPrefWidth(20d);
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

    // Deductions UI.
    private HBox buildDeductions() {
        discount = new ComboBox<>();
        discount.setPromptText("Discount(%)");
        discount.setPrefWidth(500d);
        tax = new ComboBox<>();
        tax.setPromptText("Tax(%)");
        tax.setPrefWidth(500d);
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10d);
        hbox.getChildren().addAll(discount, tax);
        return hbox;
    }

    // Cart Buttons UI.
    private HBox buildButtons() {
        checkOutBtn = new Button("CheckOut $0.00");
        checkOutBtn.setOnAction(event -> savePOSSale());
        checkOutBtn.setPrefWidth(650d);
        checkOutBtn.getStyleClass().add(Styles.SUCCESS);
        checkOutBtn.setDisable(true);
        emptyCartBtn = new Button("Empty Cart");
        emptyCartBtn.setOnAction(event -> clearCart());
        emptyCartBtn.setPrefWidth(400d);
        emptyCartBtn.getStyleClass().add(Styles.DANGER);
        emptyCartBtn.setDisable(true);
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
        vbox.setPrefHeight(584d);
        vbox.setMinWidth(338d);
        vbox.setPrefWidth(538d);
        vbox.setMinWidth(538d);
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
        pane.setPadding(new Insets(5d, 5d, 5d, 0d));
        pane.setTop(buildTop());
        pane.setCenter(buildCenter());
        pane.setRight(buildRight());
        return pane;
    }


    private void setPOSComboBoxes() {
        setupFilterComboBox(customer, CustomerViewModel.getCustomers(), SaleMasterViewModel.customerProperty(),
                customer -> (customer == null) ? "" : customer.getName());
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


    private void initializeProductsGridView() {
        updateProductsGridView();
        setProductsGridView();
    }

    private void initializeCategoryFilters() {
        setCategoryFilters();
        updateCategoryFilters();
    }

    private void updateProductsGridView() {
        ProductViewModel.getProducts().addListener((ListChangeListener<Product>) c -> setProductsGridView());
    }

    private void setProductsGridView() {
        var productsGridView = new GridPane();
        var row = 1;
        var column = 0;
        productsGridView.setHgap(15);
        productsGridView.setVgap(15);
        productsGridView.setPadding(new Insets(5));
        for (Product product : ProductViewModel.getProducts()) {
            ProductCard productCard = new ProductCard(product);
            configureProductCardAction(productCard);
            if (column == 6) {
                column = 0;
                ++row;
            }
            productsGridView.add(productCard, column++, row);
            GridPane.setMargin(productsGridView, new Insets(10));
        }
        productScrollPane.setContent(productsGridView);
    }

    // private void setProductsGridView() {
    //     var row = new BootstrapRow();
    //     for (Product product : ProductViewModel.getProducts()) {
    //         ProductCard productCard = new ProductCard(product);
    //         configureProductCardAction(productCard);
    //         var column = new BootstrapColumn(productCard);
    //         column.setBreakpointColumnWidth(Breakpoint.LARGE, 2);
    //         column.setBreakpointColumnWidth(Breakpoint.SMALL, 4);
    //         column.setBreakpointColumnWidth(Breakpoint.XSMALL, 8);
    //         row.addColumn(column);
    //     }
    //     productHolder.addRow(row);
    // }

    // private void configureProductScrollPane() {
    //     productScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
    //         if (event.getDeltaX() != 0) {
    //             event.consume();
    //         }
    //     });
    //     productScrollPane.widthProperty().addListener((obs, oV, nV) -> {
    //         productHolder.setMaxWidth(nV.doubleValue() - 10);
    //         productHolder.setPrefWidth(nV.doubleValue() - 10);
    //         productHolder.setMinWidth(nV.doubleValue() - 10);
    //     });
    //     productHolder.setAlignment(Pos.CENTER_LEFT);
    //     productHolder.setPadding(new Insets(10));
    // }

    private void configureProductScrollPane() {
        productScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaX() != 0) {
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
                displayNotification("Product out of stock", MessageVariants.ERROR, "fas-triangle-exclamation");
            }
        } catch (Exception e) {
            SpotyLogger.writeToFile(e, this.getClass());
        }
    }

    private void updateSaleDetail(SaleDetail saleDetail, ProductCard productCard, long quantity) {
        SaleDetailViewModel.setProduct(productCard.getProduct());
        SaleDetailViewModel.setPrice(productCard.getProduct().getSalePrice());
        SaleDetailViewModel.setQuantity(quantity);
        SaleDetailViewModel.setSubTotalPrice(calculateSubTotal(productCard.getProduct()) + saleDetail.getSubTotalPrice());
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
            displayNotification("Product out of stock", MessageVariants.ERROR, "fas-triangle-exclamation");
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
        return saleDetails.stream().mapToDouble(SaleDetail::getSubTotalPrice).sum();
    }

    private long calculateQuantity(SaleDetail saleDetail) {
        return saleDetail.getQuantity() + 1;
    }

    private void setCategoryFilters() {
        ProductCategoryViewModel.getCategories().forEach(productCategory -> {
            ToggleButton toggleButton = createToggle(productCategory.getName(), toggleGroup);
            filterPane.getChildren().add(toggleButton);
        });
    }

    private void updateCategoryFilters() {
        ProductCategoryViewModel.getCategories().addListener((ListChangeListener<ProductCategory>) c -> {
            filterPane.getChildren().clear();
            ProductCategoryViewModel.getCategories().forEach(productCategory -> {
                ToggleButton toggleButton = createToggle(productCategory.getName(), toggleGroup);
                filterPane.getChildren().add(toggleButton);
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
            enableButtonsOnSaleDetailsChange();
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

        cartQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        cartSubTotal.setCellValueFactory(new PropertyValueFactory<>("subTotalPrice"));

        cartActions.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        cartActions.setCellFactory(param -> new TableCell<>() {
            final MFXFontIcon delete = new MFXFontIcon("fas-trash", Color.RED);

            @Override
            protected void updateItem(SaleDetail item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(delete);
                    delete.setOnMouseClicked(event -> removeItemFromCart(item));
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

    private void enableButtonsOnSaleDetailsChange() {
        if (checkOutBtn.isDisabled()) {
            checkOutBtn.setDisable(false);
        }
        if (emptyCartBtn.isDisabled()) {
            emptyCartBtn.setDisable(false);
        }
    }

    private void bindTotalLabels() {
        SaleDetailViewModel.getSaleDetails().addListener((ListChangeListener<SaleDetail>) listener ->
                checkOutBtn.setText("CheckOut $" + calculateTotal(SaleDetailViewModel.getSaleDetails()))
        );
    }

    public void clearCart() {
        SaleDetailViewModel.getSaleDetails().clear();
        availableProductQuantity = 0L;
        checkOutBtn.setDisable(true);
        emptyCartBtn.setDisable(true);
    }

    public void savePOSSale() {
        SaleMasterViewModel.setSaleStatus("Complete");
        SaleMasterViewModel.setPaymentStatus("Paid");
        double total = calculateTotal(SaleDetailViewModel.getSaleDetails());
        SaleMasterViewModel.setTotal(total);
        SaleMasterViewModel.setAmountPaid(total);
        SaleMasterViewModel.setNotes("Approved.");

        SpotyThreader.spotyThreadPool(() -> {
            try {
                SaleMasterViewModel.saveSaleMaster(this::onSuccess, this::displaySuccessMessage, this::displayErrorMessage);
            } catch (Exception e) {
                SpotyLogger.writeToFile(e, this.getClass());
            }
        });
    }

    private void onSuccess() {
        clearCart();
        SaleMasterViewModel.setDefaultCustomer();
        SaleMasterViewModel.getAllSaleMasters(null, null);
        ProductViewModel.getAllProducts(null, null);
    }

    private void displaySuccessMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, "fas-circle-check");
    }

    private void displayErrorMessage(String message) {
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

    public void setSearchBar() {
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.equals(oldValue, newValue)) {
                return;
            }
            if (oldValue.isBlank() && newValue.isBlank()) {
                ProductViewModel.getAllProducts(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            ProductViewModel.searchItem(newValue, () -> progress.setVisible(false), this::displayErrorMessage);
        });
    }
}
