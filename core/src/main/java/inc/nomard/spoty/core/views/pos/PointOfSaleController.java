/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package inc.nomard.spoty.core.views.pos;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.sales.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.pos.components.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.sales.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.legacy.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxresources.fonts.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class PointOfSaleController implements Initializable {
    private static volatile PointOfSaleController instance;
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private final Stage stage;
    @FXML
    private MFXFilterComboBox<Customer> customer;
    @FXML
    private MFXLegacyTableView<SaleDetail> cart;
    @FXML
    private MFXTextField searchBar;
    @FXML
    private MFXComboBox<Discount> discount;
    @FXML
    private MFXButton checkOutBtn, emptyCartBtn;
    @FXML
    private HBox filterPane, leftHeaderPane;
    @FXML
    private MFXScrollPane productScrollPane;
    // @FXML
    // private BootstrapPane productHolder;
    @FXML
    private TableColumn<SaleDetail, SaleDetail> cartName;
    @FXML
    private TableColumn<SaleDetail, Long> cartQuantity;
    @FXML
    private TableColumn<SaleDetail, Double> cartPrice;
    @FXML
    private TableColumn<SaleDetail, Double> cartSubTotal;
    @FXML
    private TableColumn<SaleDetail, SaleDetail> cartActions;
    @FXML
    private MFXScrollPane scrollPane;
    @FXML
    private VBox cartItemHolder;
    @FXML
    private MFXComboBox<Tax> tax;
    @FXML
    private BorderPane contentPane;
    @FXML
    private MFXProgressSpinner progress;
    private Long availableProductQuantity = 0L;

    private PointOfSaleController(Stage stage) {
        this.stage = stage;
    }

    public static PointOfSaleController getInstance(Stage stage) {
        if (instance == null) {
            synchronized (PointOfSaleController.class) {
                instance = new PointOfSaleController(stage);
            }
        }
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkOutBtn.setText("CheckOut $0.00");
        configureProductScrollPane();
        setIcons();
        setSearchBar();
        setPOSComboBoxes();
        initializeCategoryFilters();
        initializeProductsGridView();
        setCheckoutProductsTable();
        bindTotalLabels();
        SaleMasterViewModel.setDefaultCustomer();
    }

    private void setPOSComboBoxes() {
        setupFilterComboBox(customer, CustomerViewModel.getCustomers(), SaleMasterViewModel.customerProperty(),
                customer -> (customer == null) ? "" : customer.getName());
        setupComboBox(discount, DiscountViewModel.getDiscounts(), SaleMasterViewModel.discountProperty(), discount -> (discount == null) ? "" : discount.getName() + " (" + discount.getPercentage() + "%)");
        setupComboBox(tax, TaxViewModel.getTaxes(), SaleMasterViewModel.netTaxProperty(), tax -> (tax == null) ? "" : tax.getName() + " (" + tax.getPercentage() + "%)");
    }

    private <T> void setupFilterComboBox(MFXFilterComboBox<T> comboBox, ObservableList<T> items,
                                         Property<T> property, Function<T, String> converter) {
        if (property != null) {
            comboBox.valueProperty().bindBidirectional(property);
        }
        if (converter != null) {
            StringConverter<T> itemConverter = FunctionalStringConverter.to(converter);
            Function<String, Predicate<T>> filterFunction = searchStr -> item ->
                    StringUtils.containsIgnoreCase(itemConverter.toString(item), searchStr);
            comboBox.setConverter(itemConverter);
            comboBox.setFilterFunction(filterFunction);
        }
        if (items.isEmpty()) {
            items.addListener((ListChangeListener<T>) c -> comboBox.setItems(items));
        } else {
            comboBox.itemsProperty().bindBidirectional(new SimpleListProperty<>(items));
        }
    }

    private <T> void setupComboBox(MFXComboBox<T> comboBox, ObservableList<T> items,
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
        productsGridView.setHgap(20);
        productsGridView.setVgap(20);
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
        SaleDetailViewModel.setSubTotalPrice(calculateSubTotal(productCard.getProduct()));
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
            subTotal -= (product.getDiscount().getPercentage() / 100) * subTotal;
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
        MFXRectangleToggleNode toggleNode = new MFXRectangleToggleNode(text);
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
        cartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
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
        if (!BaseController.getInstance(stage).morphPane.getChildren().contains(notification)) {
            BaseController.getInstance(stage).morphPane.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification, stage));
        }
    }

    private void setIcons() {
        searchBar.setTrailingIcon(new MFXFontIcon("fas-magnifying-glass"));
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.equals(oldValue, newValue)) {
                return;
            }
            if (oldValue.isBlank() && newValue.isBlank()) {
                ProductViewModel.getAllProducts(null, null);
            }
            progress.setVisible(true);
            ProductViewModel.searchItem(newValue, () -> progress.setVisible(false), this::displayErrorMessage);
        });
    }
}
