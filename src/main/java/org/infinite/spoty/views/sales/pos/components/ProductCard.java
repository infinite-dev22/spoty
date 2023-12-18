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

package org.infinite.spoty.views.sales.pos.components;

import io.github.palexdev.mfxcore.controls.Label;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.infinite.spoty.SpotyResourceLoader;
import org.infinite.spoty.data_source.dtos.Product;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ProductCard extends VBox {

    private final String imageSrc = SpotyResourceLoader.load("images/no-image-available-icon.png");
    private final ImageView productImageView = new ImageView();
    private Image productImage;
    private Label productNameLbl;
    private Label productPriceLbl;
    private Label productQuantityLbl;
    private Product product;

    public ProductCard(
            String productName, double productPrice, double productQuantity, String imageUrl) {
        if (Objects.equals(productNameLbl, null)) {
            productNameLbl = new Label(productName);
        }
        if (Objects.equals(productPriceLbl, null)) {
            productPriceLbl = new Label(String.valueOf(productPrice));
        }
        if (Objects.equals(productQuantityLbl, null)) {
            productQuantityLbl = new Label(String.valueOf(productQuantity));
        }

        setSpacing(10);
        getChildren()
                .addAll(getProductImage(imageUrl, productQuantityLbl), productNameLbl, productPriceLbl);
        getStyleClass().add("card");
    }

    public ProductCard(String productName, double productPrice, double productQuantity) {
        if (Objects.equals(productNameLbl, null)) {
            productNameLbl = new Label(productName);
        }
        if (Objects.equals(productPriceLbl, null)) {
            productPriceLbl = new Label(String.valueOf(productPrice));
        }
        if (Objects.equals(productQuantityLbl, null)) {
            productQuantityLbl = new Label(String.valueOf(productQuantity));
        }

        setSpacing(10);
        setPadding(new Insets(10));
        getStyleClass().add("card");
        getChildren()
                .addAll(getProductImage(imageSrc, productQuantityLbl), productNameLbl, productPriceLbl);
    }

    public ProductCard(Product product) {
        this.product = product;

        if (Objects.equals(productNameLbl, null)) {
            productNameLbl = new Label(product.getName() + " - " + product.getUnit().getShortName());
        }
        if (Objects.equals(productPriceLbl, null)) {
            productPriceLbl = new Label(String.valueOf(product.getPrice()));
        }
        if (Objects.equals(productQuantityLbl, null)) {
            productQuantityLbl = new Label(product.getQuantity() + " Pcs");
        }

        productQuantityLbl.getStyleClass().add("pos-product-label");
        productNameLbl.setWrappingWidth(100);
        productNameLbl.setWrapText(true);

        VBox labelsHolder = new VBox();
        labelsHolder.setSpacing(10);
        labelsHolder.setPadding(new Insets(10));
        labelsHolder.getChildren().addAll(productNameLbl, productPriceLbl);

        getStyleClass().add("pos-product-card");
        setAlignment(Pos.CENTER);
        getChildren()
                .addAll(getProductImage(imageSrc, productQuantityLbl), labelsHolder);
    }

    public Product getProduct() {
        return product;
    }

    @Contract("_, _ -> new")
    private @NotNull AnchorPane getProductImage(String image, Label productQuantityLbl) {
        AnchorPane.setTopAnchor(productQuantityLbl, -12.0);
        AnchorPane.setRightAnchor(productQuantityLbl, -12.0);

        if (Objects.equals(productImage, null)) {
            productImage = new Image(image, 100, 200, true, false);
        }

        productImageView.setImage(productImage);
        productImageView.setCache(true);
        productImageView.setCacheHint(CacheHint.SPEED);

        centerImage();

        return new AnchorPane(productImageView, productQuantityLbl);
    }

    public void centerImage() {
        if (productImage != null) {
            double w;
            double h;

            double ratioX = productImageView.getFitWidth() / productImage.getWidth();
            double ratioY = productImageView.getFitHeight() / productImage.getHeight();

            double reduceCoEff = Math.min(ratioX, ratioY);

            w = productImage.getWidth() * reduceCoEff;
            h = productImage.getHeight() * reduceCoEff;

            productImageView.setX((productImageView.getFitWidth() - w) / 2);
            productImageView.setY((productImageView.getFitHeight() - h) / 2);

        }
    }
}
