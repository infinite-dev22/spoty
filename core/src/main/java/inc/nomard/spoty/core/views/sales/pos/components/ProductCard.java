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

package inc.nomard.spoty.core.views.sales.pos.components;

import inc.nomard.spoty.core.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.mfxcore.controls.*;
import java.util.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import lombok.*;
import lombok.extern.slf4j.*;

@Slf4j
public class ProductCard extends VBox {
    private static final Random RND = new Random();
    private final ArrayList<String> images = new ArrayList<>(List.of(
            SpotyCoreResourceLoader.load("images/dark.png"),
            SpotyCoreResourceLoader.load("images/dark_or_light.png"),
            SpotyCoreResourceLoader.load("images/light.png"),
            SpotyCoreResourceLoader.load("images/product-image-place-holder.png"),
            SpotyCoreResourceLoader.load("images/user-place-holder.png"),
            SpotyCoreResourceLoader.load("images/no-image-available-icon.png")));
    private final String imageSrc = SpotyCoreResourceLoader.load("images/no-image-available-icon.png");
    private final ImageView productImageView = new ImageView();
    @Getter
    private final Product product;
    private Image productImage;
    private Label productNameLbl;
    private Label productPriceLbl;
    private Label productQuantityLbl;

    public ProductCard(Product product) {
        this.product = product;

        if (Objects.equals(productNameLbl, null)) {
            productNameLbl = new Label(product.getName());
        }
        if (Objects.equals(productPriceLbl, null)) {
            productPriceLbl = new Label(String.valueOf(product.getPrice()));
        }
        if (Objects.equals(productQuantityLbl, null)) {
            productQuantityLbl = new Label(product.getQuantity() + " Pcs available");
        }

        productNameLbl.setWrappingWidth(200);
        productNameLbl.setWrapText(true);

        productNameLbl.setLabelFor(this);
        productPriceLbl.setLabelFor(this);
        productQuantityLbl.setLabelFor(this);

        var space = new Region();
        space.setPrefHeight(10);

        this.getStyleClass().add("pos-product-card");
        productNameLbl.getStyleClass().add("pos-product-card-name");
        productPriceLbl.getStyleClass().add("pos-product-card-price");
        productQuantityLbl.getStyleClass().add("pos-product-card-quantity");
        setAlignment(Pos.CENTER);
        setSpacing(10);
        getChildren()
                .addAll(
                        getProductImage(images.get(RND.nextInt((images.size()) - 1) + 1)),
                        productNameLbl,
                        productPriceLbl,
                        productQuantityLbl,
                        space);
    }

    private ImageView getProductImage(String image) {
        if (Objects.equals(productImage, null)) {
            productImage = new Image(image, 200, 200, true, false);
        }

        productImageView.setImage(productImage);
        productImageView.setCache(true);
        productImageView.setCacheHint(CacheHint.SPEED);

        centerImage();

        return productImageView;
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
