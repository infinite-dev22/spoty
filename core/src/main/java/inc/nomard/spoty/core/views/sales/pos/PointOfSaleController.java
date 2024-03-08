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

import inc.nomard.spoty.core.components.animations.SpotyAnimations;
import inc.nomard.spoty.core.components.notification.SimpleNotification;
import inc.nomard.spoty.core.components.notification.SimpleNotificationHolder;
import inc.nomard.spoty.core.components.notification.enums.NotificationDuration;
import inc.nomard.spoty.core.components.notification.enums.NotificationVariants;
import inc.nomard.spoty.core.viewModels.BranchViewModel;
import inc.nomard.spoty.core.viewModels.CustomerViewModel;
import inc.nomard.spoty.core.viewModels.ProductCategoryViewModel;
import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.core.viewModels.sales.SaleDetailViewModel;
import inc.nomard.spoty.core.viewModels.sales.SaleMasterViewModel;
import inc.nomard.spoty.core.views.sales.pos.components.ProductCard;
import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.Customer;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.network_bridge.dtos.ProductCategory;
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
import io.github.palexdev.mfxcore.controls.Label;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.animation.RotateTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

public class PointOfSaleController implements Initializable {
    private final ToggleGroup toggleGroup = new ToggleGroup();
    @FXML
    public MFXFilterComboBox<Customer> posCustomerComboBox;
    @FXML
    public MFXFilterComboBox<Branch> posBranchComboBox;
    @FXML
    public MFXLegacyTableView<SaleDetail> posItemsTable;
    @FXML
    public MFXTextField posDiscountTextField, posProductSearch;
    @FXML
    public Label posTotalLabel;
    @FXML
    public MFXButton posCheckOutBtn, posEmptyCartBtn;
    @FXML
    public HBox posFilterPane;
    @FXML
    public MFXScrollPane posProductHolder;
    @FXML
    public HBox refresh;
    public TableColumn<SaleDetail, SaleDetail> cartName;
    public TableColumn<SaleDetail, Long> cartQuantity;
    public TableColumn<SaleDetail, Double> cartPrice;
    public TableColumn<SaleDetail, Double> cartSubTotal;
    public TableColumn<SaleDetail, SaleDetail> cartActions;
    private RotateTransition transition;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        setPOSComboBoxes();
        getCategoryFilters();
        getProductsGridView();
        setCheckoutProductsTable();
        getTotalLabels();
    }

    private void setPOSComboBoxes() {
        // Bi~Directional Binding.
        posCustomerComboBox.valueProperty().bindBidirectional(SaleMasterViewModel.customerProperty());
        posBranchComboBox.valueProperty().bindBidirectional(SaleMasterViewModel.branchProperty());

        // ComboBox Converters.
        StringConverter<Customer> customerConverter =
                FunctionalStringConverter.to(customer -> (customer == null) ? "" : customer.getName());

        StringConverter<Branch> branchConverter =
                FunctionalStringConverter.to(branch -> (branch == null) ? "" : branch.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<Customer>> customerFilterFunction =
                searchStr ->
                        customer ->
                                StringUtils.containsIgnoreCase(customerConverter.toString(customer), searchStr);

        Function<String, Predicate<Branch>> branchFilterFunction =
                searchStr ->
                        branch -> StringUtils.containsIgnoreCase(branchConverter.toString(branch), searchStr);

        // Set items to combo boxes and display custom text.
        posCustomerComboBox.setItems(CustomerViewModel.getCustomers());
        posCustomerComboBox.setConverter(customerConverter);
        posCustomerComboBox.setFilterFunction(customerFilterFunction);

        posBranchComboBox.setItems(BranchViewModel.getBranches());
        posBranchComboBox.setConverter(branchConverter);
        posBranchComboBox.setFilterFunction(branchFilterFunction);
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

        posProductHolder.setContent(productsGridView);
    }

    private void productCardOnAction(@NotNull ProductCard productCard) {
        productCard.setOnMouseClicked(
                event -> {
                    if (SaleDetailViewModel.getSaleDetails().stream()
                            .anyMatch(saleDetail -> saleDetail.getProduct() == productCard.getProduct())) {
                        Optional<SaleDetail> optionalSaleDetail =
                                SaleDetailViewModel.getSaleDetails().stream()
                                        .filter(saleDetail -> saleDetail.getProduct() == productCard.getProduct())
                                        .findAny();

                        if (optionalSaleDetail.isPresent()) {
                            SaleDetail saleDetail = optionalSaleDetail.get();
                            try {
                                SpotyThreader.spotyThreadPool(() -> {
                                    try {
                                        SaleDetailViewModel.getSaleDetail(saleDetail);
                                    } catch (Exception e) {
                                        SpotyLogger.writeToFile(e, this.getClass());
                                    }
                                });
                                SaleDetailViewModel.setProduct(productCard.getProduct());
                                SaleDetailViewModel.setPrice(productCard.getProduct().getPrice());
                                SaleDetailViewModel.setQuantity(calculateQuantity(saleDetail));
                                SaleDetailViewModel.setSubTotalPrice(calculateSubTotal(productCard.getProduct()));

                                SaleDetailViewModel.updateSaleDetail(
                                        (long) SaleDetailViewModel.getSaleDetails().indexOf(saleDetail));

                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        }
                    } else {
                        SaleDetailViewModel.setProduct(productCard.getProduct());
                        SaleDetailViewModel.setPrice(productCard.getProduct().getPrice());
                        SaleDetailViewModel.setQuantity(1L);
                        SaleDetailViewModel.setSubTotalPrice(calculateSubTotal(productCard.getProduct()));
                        SaleDetailViewModel.addSaleDetail();
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

    @Contract(pure = true)
    private long calculateQuantity(@NotNull SaleDetail saleDetail) {
        return saleDetail.getQuantity() + 1;
    }

    private void setCategoryFilters() {
        ProductCategoryViewModel.getCategories()
                .forEach(
                        productCategory -> {
                            ToggleButton toggleButton = createToggle(productCategory.getName(), toggleGroup);
                            posFilterPane.getChildren().addAll(toggleButton);
                        });
    }

    private void updateCategoryFilters() {
        ProductCategoryViewModel.getCategories()
                .addListener(
                        (ListChangeListener<ProductCategory>)
                                c -> {
                                    posFilterPane.getChildren().clear();
                                    ProductCategoryViewModel.getCategories()
                                            .forEach(
                                                    productCategory -> {
                                                        ToggleButton toggleButton =
                                                                createToggle(productCategory.getName(), toggleGroup);
                                                        posFilterPane.getChildren().addAll(toggleButton);
                                                    });
                                });
    }

    private @NotNull ToggleButton createToggle(String text, ToggleGroup toggleGroup) {
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

                                delete.setOnMouseClicked(event -> getTableView().getItems().remove(item));
                            }
                        });

        SaleDetailViewModel.getSaleDetails()
                .addListener(
                        (ListChangeListener<SaleDetail>)
                                c -> posItemsTable.setItems(SaleDetailViewModel.getSaleDetails()));
    }

    private void getTotalLabels() {
        SaleDetailViewModel.getSaleDetails()
                .addListener(
                        (ListChangeListener<SaleDetail>)
                                listener -> posTotalLabel.setText("Total: $" + calculateTotal(SaleDetailViewModel.getSaleDetails())));
    }

    public void clearCart() {
        SaleDetailViewModel.getSaleDetails().clear();
    }

    public void savePOSSale() {
        SaleMasterViewModel.setSaleStatus("Complete");
        SaleMasterViewModel.setPayStatus("Paid");
        SaleMasterViewModel.setTotal(calculateTotal(SaleDetailViewModel.getSaleDetails()));
        SaleMasterViewModel.setPaid(calculateTotal(SaleDetailViewModel.getSaleDetails()));
        SaleMasterViewModel.setNote("Approved.");

        SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();
        SpotyThreader.spotyThreadPool(
                () -> {
                    try {
                        SaleMasterViewModel.saveSaleMaster(this::onAction, this::onSuccess, this::onFailed);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, this.getClass());
                    }
                });

        SimpleNotification notification =
                new SimpleNotification.NotificationBuilder("Sale saved successfully")
                        .duration(NotificationDuration.MEDIUM)
                        .icon("fas-circle-check")
                        .type(NotificationVariants.SUCCESS)
                        .build();
        notificationHolder.addNotification(notification);
    }


    private void onAction() {
        transition.playFromStart();
        transition.setOnFinished((ActionEvent event) -> transition.playFromStart());
    }

    private void onSuccess() {
        transition.setOnFinished(null);
    }

    private void onFailed() {
        transition.setOnFinished(null);
    }

    private void setIcons() {
        var refreshIcon = new MFXFontIcon("fas-arrows-rotate", 24);
        refresh.getChildren().addFirst(refreshIcon);

        transition = SpotyAnimations.rotateTransition(refreshIcon, Duration.millis(1000), 360);

        refreshIcon.setOnMouseClicked(mouseEvent -> SaleDetailViewModel.getAllSaleDetails(this::onAction, this::onSuccess, this::onFailed));
    }
}
