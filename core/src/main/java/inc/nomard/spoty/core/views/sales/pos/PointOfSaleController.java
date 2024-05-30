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

package inc.nomard.spoty.core.views.sales.pos;

import inc.nomard.spoty.core.components.message.SpotyMessage;
import inc.nomard.spoty.core.components.message.SpotyMessageHolder;
import inc.nomard.spoty.core.components.message.enums.MessageDuration;
import inc.nomard.spoty.core.components.message.enums.MessageVariants;
import inc.nomard.spoty.core.viewModels.CustomerViewModel;
import inc.nomard.spoty.core.viewModels.ProductCategoryViewModel;
import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.core.viewModels.sales.SaleDetailViewModel;
import inc.nomard.spoty.core.viewModels.sales.SaleMasterViewModel;
import inc.nomard.spoty.core.views.sales.pos.components.ProductCard;
import inc.nomard.spoty.network_bridge.dtos.Customer;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.network_bridge.dtos.ProductCategory;
import inc.nomard.spoty.network_bridge.dtos.Tax;
import inc.nomard.spoty.network_bridge.dtos.sales.SaleDetail;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import lombok.extern.java.Log;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

@Log
public class PointOfSaleController implements Initializable {
    private final ToggleGroup toggleGroup = new ToggleGroup();
    @FXML
    public MFXFilterComboBox<Customer> customer;
    @FXML
    public MFXLegacyTableView<SaleDetail> cart;
    @FXML
    public MFXTextField discount, search;
    public String total;
    @FXML
    public MFXButton checkOutBtn, emptyCartBtn;
    @FXML
    public HBox filterPane;
    @FXML
    public MFXScrollPane productHolder;
    @FXML
    public HBox refresh;
    public TableColumn<SaleDetail, SaleDetail> cartName;
    public TableColumn<SaleDetail, Long> cartQuantity;
    public TableColumn<SaleDetail, Double> cartPrice;
    public TableColumn<SaleDetail, Double> cartSubTotal;
    public TableColumn<SaleDetail, SaleDetail> cartActions;
    @FXML
    public MFXScrollPane scrollPane;
    @FXML
    public VBox cartItemHolder;
    @FXML
    public MFXFilterComboBox<Tax> tax;
    @FXML
    public BorderPane contentPane;
    private Long availableProductQuantity = 0L;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkOutBtn.setText("CheckOut $0.00");
        setPOSComboBoxes();
        getCategoryFilters();
        getProductsGridView();
        setCheckoutProductsTable();
        getTotalLabels();
    }

    private void setPOSComboBoxes() {
        // Bi~Directional Binding.
        customer.valueProperty().bindBidirectional(SaleMasterViewModel.customerProperty());

        // ComboBox Converters.
        StringConverter<Customer> customerConverter =
                FunctionalStringConverter.to(customer -> (customer == null) ? "" : customer.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<Customer>> customerFilterFunction =
                searchStr ->
                        customer ->
                                StringUtils.containsIgnoreCase(customerConverter.toString(customer), searchStr);

        // Set items to combo boxes and display custom text.
        customer.setConverter(customerConverter);
        customer.setFilterFunction(customerFilterFunction);
        if (CustomerViewModel.getCustomers().isEmpty()) {
            CustomerViewModel.getCustomers()
                    .addListener(
                            (ListChangeListener<Customer>)
                                    c -> customer.setItems(CustomerViewModel.getCustomers()));
        } else {
            customer.itemsProperty().bindBidirectional(CustomerViewModel.customersProperty());
        }
    }

    private void getProductsGridView() {
        setProductsGridView();
        updateProductsGridView();
    }

    private void getCategoryFilters() {
        setCategoryFilters();
        updateCategoryFilters();
    }

    private void updateProductsGridView() {
        ProductViewModel.getProducts()
                .addListener((ListChangeListener<Product>) c -> setProductsGridView());
    }

    private void setProductsGridView() {
        GridPane productsGridView = new GridPane();
        productsGridView.setHgap(30);
        productsGridView.setVgap(20);
        productsGridView.setPadding(new Insets(5));

        int row = 1;
        int column = 0;

        for (Product product : ProductViewModel.getProducts()) {
            ProductCard productCard = new ProductCard(product);
            productCardOnAction(productCard);

            if (column == 4) {
                column = 0;
                ++row;
            }

            productsGridView.add(productCard, column++, row);
            GridPane.setMargin(productsGridView, new Insets(10));
        }

        productHolder.setContent(productsGridView);
    }

    private void productCardOnAction(ProductCard productCard) {
        productCard.setOnMouseClicked(
                event -> {
                    // Check if a sale detail with currently clicked product already exists else create new sale detail.
                    // Useful to update sale detail product quantity.
                    if (SaleDetailViewModel.getSaleDetails().stream()
                            .anyMatch(saleDetail -> saleDetail.getProduct() == productCard.getProduct())) {
                        // Get existing sale detail into an optional... just to cater for null safety but looks like it
                        // ain't required.
                        Optional<SaleDetail> optionalSaleDetail =
                                SaleDetailViewModel.getSaleDetails().stream()
                                        .filter(saleDetail -> saleDetail.getProduct() == productCard.getProduct())
                                        .findAny();
                        // If optional(sale detail that's already proved present, duh...) is present else tap out.
                        if (optionalSaleDetail.isPresent()) {
                            SaleDetail saleDetail = optionalSaleDetail.get();
                            try {
                                // Get product quantity in existing sale detail.
                                var quantity = calculateQuantity(saleDetail);
                                availableProductQuantity = quantity;
                                // If actual product quantity is greater or equal to existing sale detail's product
                                // quantity then increment by 1 else out of stock.
                                if (productCard.getProduct().getQuantity() >= availableProductQuantity) {
                                    SaleDetailViewModel.getPosSale(saleDetail);
                                    SaleDetailViewModel.setProduct(productCard.getProduct());
                                    SaleDetailViewModel.setPrice(productCard.getProduct().getPrice());
                                    SaleDetailViewModel.setQuantity(quantity);
                                    SaleDetailViewModel.setSubTotalPrice(calculateSubTotal(productCard.getProduct()));

                                    SaleDetailViewModel.updatePosSale(
                                            (long) SaleDetailViewModel.getSaleDetails().indexOf(saleDetail));
                                } else {
                                    SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
                                    SpotyMessage notification =
                                            new SpotyMessage.MessageBuilder("Product out of stock")
                                                    .duration(MessageDuration.SHORT)
                                                    .icon("fas-triangle-exclamation")
                                                    .type(MessageVariants.ERROR)
                                                    .build();
                                    notificationHolder.addMessage(notification);
                                }
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        }
                    } else {
                        // Check if actual product quantity greater than zero(0) else out of stock.
                        if (productCard.getProduct().getQuantity() > 0) {
                            SaleDetailViewModel.setProduct(productCard.getProduct());
                            SaleDetailViewModel.setPrice(productCard.getProduct().getPrice());
                            SaleDetailViewModel.setQuantity(1L);
                            availableProductQuantity = 1L;
                            SaleDetailViewModel.setSubTotalPrice(calculateSubTotal(productCard.getProduct()));
                            SaleDetailViewModel.addSaleDetail();
                        } else {
                            SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
                            SpotyMessage notification =
                                    new SpotyMessage.MessageBuilder("Product out of stock")
                                            .duration(MessageDuration.SHORT)
                                            .icon("fas-triangle-exclamation")
                                            .type(MessageVariants.ERROR)
                                            .build();
                            notificationHolder.addMessage(notification);
                        }
                    }
                });
    }

    private double calculateSubTotal(Product product) {
        return (SaleDetailViewModel.getQuantity()
                * ((product.getNetTax() > 0) ? (product.getNetTax() / 100) : 1.0))
                * product.getPrice();
    }

    private double calculateTotal(ObservableList<SaleDetail> saleDetails) {
        double totalPrice = 0;

        for (SaleDetail saleDetail : saleDetails) {
            totalPrice += saleDetail.getSubTotalPrice();
        }

        return totalPrice;
    }

    private long calculateQuantity(SaleDetail saleDetail) {
        return saleDetail.getQuantity() + 1;
    }

    private void setCategoryFilters() {
        ProductCategoryViewModel.getCategories()
                .forEach(
                        productCategory -> {
                            ToggleButton toggleButton = createToggle(productCategory.getName(), toggleGroup);
                            filterPane.getChildren().addAll(toggleButton);
                        });
    }

    private void updateCategoryFilters() {
        ProductCategoryViewModel.getCategories()
                .addListener(
                        (ListChangeListener<ProductCategory>)
                                c -> {
                                    filterPane.getChildren().clear();
                                    ProductCategoryViewModel.getCategories()
                                            .forEach(
                                                    productCategory -> {
                                                        ToggleButton toggleButton =
                                                                createToggle(productCategory.getName(), toggleGroup);
                                                        filterPane.getChildren().addAll(toggleButton);
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
        cartName.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        cartName.setCellFactory(
                tc ->
                        new TableCell<>() {
                            @Override
                            public void updateItem(SaleDetail item, boolean empty) {
                                super.updateItem(item, empty);

                                if (empty || item == null) {
                                    setText(null);
                                    setGraphic(null);
                                } else {
                                    setText(item.getProductName());
                                }
                            }
                        });

        cartQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        cartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        cartSubTotal.setCellValueFactory(new PropertyValueFactory<>("subTotalPrice"));
        cartActions.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        cartActions.setCellFactory(
                param ->
                        new TableCell<>() {
                            final MFXFontIcon delete = new MFXFontIcon("fas-trash", Color.RED);

                            @Override
                            protected void updateItem(SaleDetail item, boolean empty) {
                                super.updateItem(item, empty);

                                if (Objects.equals(item, null)) {
                                    setGraphic(null);
                                    return;
                                }

                                setGraphic(delete);

                                delete.setOnMouseClicked(event -> {
                                    getTableView().getItems().remove(item);
                                    availableProductQuantity = 0L;
                                });
                            }
                        });

        SaleDetailViewModel.getSaleDetails()
                .addListener(
                        (ListChangeListener<SaleDetail>)
                                c -> cart.setItems(SaleDetailViewModel.getSaleDetails()));

//        SaleDetailViewModel.getSaleDetails()
//                .addListener(
//                        (ListChangeListener<SaleDetail>)
//                                c -> SaleDetailViewModel.getSaleDetails().forEach(saleDetail -> {
//                                    var cartItem = new CartItem(
//                                            saleDetail.getProduct().getImage(),
//                                            saleDetail.getProductName(),
//                                            String.valueOf(saleDetail.getProduct().getPrice()),
//                                            saleDetail.getQuantity()
//                                    );
//                                    cartItemHolder.getChildren().add(cartItem);
//                                }));
    }

    private void getTotalLabels() {
        SaleDetailViewModel.getSaleDetails()
                .addListener(
                        (ListChangeListener<SaleDetail>)
                                listener -> checkOutBtn.setText("CheckOut $" + calculateTotal(SaleDetailViewModel.getSaleDetails())));
    }

    public void clearCart() {
        SaleDetailViewModel.getSaleDetails().clear();
        availableProductQuantity = 0L;
    }

    public void savePOSSale() {
        SaleMasterViewModel.setSaleStatus("Complete");
        SaleMasterViewModel.setPayStatus("Paid");
        SaleMasterViewModel.setTotal(calculateTotal(SaleDetailViewModel.getSaleDetails()));
        SaleMasterViewModel.setPaid(calculateTotal(SaleDetailViewModel.getSaleDetails()));
        SaleMasterViewModel.setNote("Approved.");
        SpotyThreader.spotyThreadPool(
                () -> {
                    try {
                        SaleMasterViewModel.saveSaleMaster(this::onAction, this::onSuccess, this::onFailed);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, this.getClass());
                    }
                });
    }

    private void onAction() {
    }

    private void onSuccess() {
        availableProductQuantity = 0L;
        ProductViewModel.getAllProducts(null, null, null);
    }

    private void onFailed() {
        ProductViewModel.getAllProducts(null, null, null);
    }
}
