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

package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.infinite.spoty.GlobalActions;
import org.infinite.spoty.SpotyResourceLoader;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.database.models.Brand;
import org.infinite.spoty.database.models.ProductCategory;
import org.infinite.spoty.database.models.UnitOfMeasure;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.values.strings.Values;
import org.infinite.spoty.viewModels.BrandViewModel;
import org.infinite.spoty.viewModels.ProductCategoryViewModel;
import org.infinite.spoty.viewModels.ProductViewModel;
import org.infinite.spoty.viewModels.UOMViewModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class ProductFormController implements Initializable {
    private static ProductFormController instance;
    private final String placeholderImage =
            SpotyResourceLoader.load("images/product-image-place-holder.png");
    @FXML
    public MFXTextField productName,
            productSerial,
            productPrice,
            productDiscount,
            productDescription,
            productTax,
            productStockAlert;
    @FXML
    public MFXFilterComboBox<Brand> productBrand;
    @FXML
    public MFXFilterComboBox<ProductCategory> productCategory;
    @FXML
    public MFXFilterComboBox<UnitOfMeasure> productUOM;
    @FXML
    public MFXFilterComboBox<String> productBarcodeType;
    @FXML
    public MFXFilterComboBox<String> productType;
    @FXML
    public ImageView productImageView;
    @FXML
    public MFXButton productSaveBtn, productCancelBtn;
    public Image productImage;
    @FXML
    public Label productBarcodeTypeValidationLabel,
            productUOMValidationLabel,
            productCategoryValidationLabel,
            productBrandValidationLabel,
            productPriceValidationLabel,
            productNameValidationLabel;
    private FileChooser fileChooser;

    public static ProductFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new ProductFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getFieldBindings();
        getComboBoxProperties();
        dialogOnActions();
        addImage();
        setProductImage(placeholderImage);
    }

    private void setProductImage(String image) {
        if (Objects.equals(productImage, null)) {
            productImage = new Image(image, 200, 200, true, false);
        }

        productImageView.setImage(productImage);
        productImageView.setCache(true);
        productImageView.setCacheHint(CacheHint.SPEED);
    }

    private void addImage() {
        if (Objects.equals(fileChooser, null)) {
            fileChooser = new FileChooser();
        }

        productImageView.setOnMouseClicked(event -> fileChooser.showOpenDialog(new Stage()));
    }

    private void getFieldBindings() {
        productName.textProperty().bindBidirectional(ProductViewModel.nameProperty());
        productSerial.textProperty().bindBidirectional(ProductViewModel.serialProperty());
        productPrice.textProperty().bindBidirectional(ProductViewModel.priceProperty());
        productDiscount.textProperty().bindBidirectional(ProductViewModel.discountProperty());
        productDescription.textProperty().bindBidirectional(ProductViewModel.descriptionProperty());
        productTax.textProperty().bindBidirectional(ProductViewModel.netTaxProperty());
        productStockAlert.textProperty().bindBidirectional(ProductViewModel.stockAlertProperty());
        productBrand.valueProperty().bindBidirectional(ProductViewModel.brandProperty());
        productCategory.valueProperty().bindBidirectional(ProductViewModel.categoryProperty());
        productUOM.valueProperty().bindBidirectional(ProductViewModel.unitProperty());
        productBarcodeType.valueProperty().bindBidirectional(ProductViewModel.barcodeTypeProperty());
        productType.valueProperty().bindBidirectional(ProductViewModel.productTypeProperty());
    }

    private void getComboBoxProperties() {
        // ComboBox Converters.
        StringConverter<UnitOfMeasure> uomConverter =
                FunctionalStringConverter.to(
                        unitOfMeasure -> (unitOfMeasure == null) ? "" : unitOfMeasure.getName());

        StringConverter<ProductCategory> productCategoryConverter =
                FunctionalStringConverter.to(
                        productCategory -> (productCategory == null) ? "" : productCategory.getName());

        StringConverter<Brand> brandConverter =
                FunctionalStringConverter.to(brand -> (brand == null) ? "" : brand.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<UnitOfMeasure>> uomFilterFunction =
                searchStr ->
                        unitOfMeasure ->
                                StringUtils.containsIgnoreCase(uomConverter.toString(unitOfMeasure), searchStr);

        Function<String, Predicate<ProductCategory>> productCategoryFilterFunction =
                searchStr ->
                        productCategory ->
                                StringUtils.containsIgnoreCase(
                                        productCategoryConverter.toString(productCategory), searchStr);

        Function<String, Predicate<Brand>> brandFilterFunction =
                searchStr ->
                        brand -> StringUtils.containsIgnoreCase(brandConverter.toString(brand), searchStr);

        // ProductType combo box properties.
        productUOM.setItems(UOMViewModel.getUnitsOfMeasure());
        productUOM.setConverter(uomConverter);
        productUOM.setFilterFunction(uomFilterFunction);

        productCategory.setItems(ProductCategoryViewModel.getCategories());
        productCategory.setConverter(productCategoryConverter);
        productCategory.setFilterFunction(productCategoryFilterFunction);

        productBrand.setItems(BrandViewModel.getBrands());
        productBrand.setConverter(brandConverter);
        productBrand.setFilterFunction(brandFilterFunction);

        productBarcodeType.setItems(FXCollections.observableArrayList(Values.BARCODE_TYPES));

        productType.setItems(FXCollections.observableArrayList(Values.PRODUCT_TYPES));
    }

    private void dialogOnActions() {
        productCancelBtn.setOnAction(
                (event) -> {
                    ProductViewModel.resetProperties();

                    closeDialog(event);

                    productBrand.clearSelection();
                    productCategory.clearSelection();
                    productUOM.clearSelection();
                    productBarcodeType.clearSelection();
                    productType.clearSelection();

                    productBarcodeTypeValidationLabel.setVisible(false);
                    productUOMValidationLabel.setVisible(false);
                    productCategoryValidationLabel.setVisible(false);
                    productBrandValidationLabel.setVisible(false);
                    productPriceValidationLabel.setVisible(false);
                    productNameValidationLabel.setVisible(false);
                });
        productSaveBtn.setOnAction(
                (event) -> {
                    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

                    if (!productBarcodeTypeValidationLabel.isVisible()
                            && !productUOMValidationLabel.isVisible()
                            && !productCategoryValidationLabel.isVisible()
                            && !productBrandValidationLabel.isVisible()
                            && !productPriceValidationLabel.isVisible()
                            && !productNameValidationLabel.isVisible()) {
                        if (ProductViewModel.getId() > 0) {
                            GlobalActions.spotyThreadPool()
                                    .execute(
                                            () -> {
                                                try {
                                                    ProductViewModel.updateProduct(ProductViewModel.getId());
                                                } catch (SQLException e) {
                                                    SpotyLogger.writeToFile(e, this.getClass());
                                                }
                                            });

                            SimpleNotification notification =
                                    new SimpleNotification.NotificationBuilder("Branch updated successfully")
                                            .duration(NotificationDuration.SHORT)
                                            .icon("fas-circle-check")
                                            .type(NotificationVariants.SUCCESS)
                                            .build();
                            notificationHolder.addNotification(notification);

                            closeDialog(event);

                            productBrand.clearSelection();
                            productCategory.clearSelection();
                            productUOM.clearSelection();
                            productBarcodeType.clearSelection();
                            productType.clearSelection();

                            return;
                        }
                        GlobalActions.spotyThreadPool()
                                .execute(
                                        () -> {
                                            try {
                                                ProductViewModel.saveProduct();
                                            } catch (SQLException e) {
                                                SpotyLogger.writeToFile(e, this.getClass());
                                            }
                                        });

                        SimpleNotification notification =
                                new SimpleNotification.NotificationBuilder("Branch saved successfully")
                                        .duration(NotificationDuration.SHORT)
                                        .icon("fas-circle-check")
                                        .type(NotificationVariants.SUCCESS)
                                        .build();
                        notificationHolder.addNotification(notification);

                        closeDialog(event);

                        productBrand.clearSelection();
                        productCategory.clearSelection();
                        productUOM.clearSelection();
                        productBarcodeType.clearSelection();
                        productType.clearSelection();

                        return;
                    }
                    SimpleNotification notification =
                            new SimpleNotification.NotificationBuilder("Required fields missing")
                                    .duration(NotificationDuration.SHORT)
                                    .icon("fas-triangle-exclamation")
                                    .type(NotificationVariants.ERROR)
                                    .build();
                    notificationHolder.addNotification(notification);
                });
    }
}
