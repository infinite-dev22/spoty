package inc.nomard.spoty.core.views.pos.components;

import atlantafx.base.theme.*;
import inc.nomard.spoty.core.values.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.mfxcore.controls.*;
import java.util.*;
import javafx.application.*;
import javafx.concurrent.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import lombok.*;
import lombok.extern.java.*;

@Log
public class ProductCard extends VBox {
    private static final Random RND = new Random();
    private static final double IMAGE_SIZE = 160;
    private static final double ARC_SIZE = 20;
    private static final double SPACING = 10;
    private static final double LABEL_WIDTH = 200;
    //    private static final String PLACEHOLDER_IMAGE = SpotyCoreResourceLoader.load("images/product-image-placeholder.png");
//    private static final String NO_IMAGE_PLACEHOLDER = SpotyCoreResourceLoader.load("images/no-image-placeholder.png");
//    private static final String IMAGE_FAILED_TO_LOAD_PLACEHOLDER = SpotyCoreResourceLoader.load("images/image-loading-failed.png");

    private static final Map<String, Image> IMAGE_CACHE = new HashMap<>();

    private final Rectangle productImageHolder = new Rectangle();
    @Getter
    private final Product product;
    private final Label productNameLbl;
    private final Label productPriceLbl;
    private final Label productQuantityLbl;
    private Image productImage;

    public ProductCard(Product product) {
        this.product = product;

        productNameLbl = new Label(product.getName());
        productPriceLbl = new Label(String.valueOf(product.getSalePrice()));
        productQuantityLbl = new Label(product.getQuantity() + " Pcs available");

        initializeLabels();
        this.getStyleClass().add("pos-product-card");

        getChildren().addAll(
                getProductImage(),
                productNameLbl,
                productPriceLbl,
                productQuantityLbl,
                createSpacer()
        );

        loadImageAsync(product.getImage());
    }

    private void initializeLabels() {
        productNameLbl.setWrappingWidth(LABEL_WIDTH);
        productNameLbl.setWrapText(true);
        productNameLbl.setLabelFor(this);
        productPriceLbl.setLabelFor(this);
        productQuantityLbl.setLabelFor(this);

        productNameLbl.getStyleClass().add("pos-product-card-name");
        productPriceLbl.getStyleClass().add("pos-product-card-price");
        productQuantityLbl.getStyleClass().add(Styles.TEXT_MUTED);

        setAlignment(Pos.CENTER);
        setSpacing(SPACING);
    }

    private Rectangle getProductImage() {
        productImage = PreloadedData.productPlaceholderImage/*getCachedImage(image)*/;
        updateProductImageHolder();
        return productImageHolder;
    }

    private void updateProductImageHolder() {
        if (productImage != null) {
            productImageHolder.setFill(new ImagePattern(productImage));
            productImageHolder.setWidth(IMAGE_SIZE);
            productImageHolder.setHeight(IMAGE_SIZE);
            productImageHolder.setCache(true);
            productImageHolder.setCacheHint(CacheHint.SPEED);
            productImageHolder.setArcWidth(ARC_SIZE);
            productImageHolder.setArcHeight(ARC_SIZE);
            centerImage();
        }
    }

    private void centerImage() {
        if (productImage != null) {
            double ratioX = productImageHolder.getWidth() / productImage.getWidth();
            double ratioY = productImageHolder.getHeight() / productImage.getHeight();
            double reduceCoeff = Math.min(ratioX, ratioY);
            double w = productImage.getWidth() * reduceCoeff;
            double h = productImage.getHeight() * reduceCoeff;
            productImageHolder.setX((productImageHolder.getWidth() - w) / 2);
            productImageHolder.setY((productImageHolder.getHeight() - h) / 2);
        }
    }

    private Region createSpacer() {
        Region space = new Region();
        space.setPrefHeight(SPACING);
        return space;
    }

    private void loadImageAsync(String imagePath) {
        if (imagePath.isEmpty()) {
            Platform.runLater(this::updateProductImageHolderWithPlaceholder);
            return;
        }

        if (IMAGE_CACHE.containsKey(imagePath)) {
            productImage = IMAGE_CACHE.get(imagePath);
            Platform.runLater(this::updateProductImageHolder);
            return;
        }

        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() {
                try {
                    return new Image(imagePath, IMAGE_SIZE, IMAGE_SIZE, true, false);
                } catch (IllegalArgumentException e) {
                    log.warning("Invalid image URL: " + imagePath);
                    return PreloadedData.imageErrorPlaceholderImage;
                }
            }
        };

        loadImageTask.setOnSucceeded(event -> {
            productImage = loadImageTask.getValue();
            IMAGE_CACHE.put(imagePath, productImage);
            Platform.runLater(() -> {
                productImageHolder.setFill(new ImagePattern(productImage));
                centerImage();
            });
        });

        loadImageTask.setOnFailed(event -> {
            log.warning("Failed to load image from URL: " + imagePath + ". " + loadImageTask.getException());
            Platform.runLater(this::updateProductImageHolderWithPlaceholder);
        });

        new Thread(loadImageTask).start();
    }

    private void updateProductImageHolderWithPlaceholder() {
        productImage = PreloadedData.noImagePlaceholderImage;
        updateProductImageHolder();
    }

    private Image getCachedImage(String imagePath) {
        return IMAGE_CACHE.computeIfAbsent(imagePath, path -> new Image(path, IMAGE_SIZE, IMAGE_SIZE, true, false));
    }
}

//package inc.nomard.spoty.core.views.pos.components;
//
//import inc.nomard.spoty.core.values.*;
//import inc.nomard.spoty.network_bridge.dtos.*;
//import io.github.palexdev.mfxcore.controls.*;
//
//import javafx.application.*;
//import javafx.concurrent.*;
//import javafx.geometry.*;
//import javafx.scene.*;
//import javafx.scene.image.*;
//import javafx.scene.layout.*;
//import javafx.scene.paint.*;
//import javafx.scene.shape.*;
//import lombok.*;
//import lombok.extern.java.*;
//
//import javafx.util.Duration;
//@Log
//public class ProductCard extends VBox {
//    private static final double IMAGE_SIZE = 160;
//    private static final double ARC_SIZE = 20;
//    private static final double SPACING = 10;
//    private static final double LABEL_WIDTH = 200;
//
//    private static final Map<String, Image> IMAGE_CACHE = new HashMap<>();
//
//    private final Rectangle productImageHolder = new Rectangle();
//    @Getter
//    private final Product product;
//    private final Label productNameLbl;
//    private final Label productPriceLbl;
//    private final Label productQuantityLbl;
//    private Image productImage;
//
//    public ProductCard(Product product) {
//        this.product = product;
//
//        productNameLbl = new Label(product.getName());
//        productPriceLbl = new Label(String.valueOf(product.getSalePrice()));
//        productQuantityLbl = new Label(product.getQuantity() + " Pcs available");
//
//        initializeLabels();
//        this.getStyleClass().add("pos-product-card");
//
//        getChildren().addAll(
//                getProductImage(),
//                productNameLbl,
//                productPriceLbl,
//                productQuantityLbl,
//                createSpacer()
//        );
//        preloadImage();
//        loadImageAsync();
//    }
//
//    public void preloadImage() {
//        IMAGE_CACHE.computeIfAbsent(product.getImage(), key -> new Image(key, IMAGE_SIZE, IMAGE_SIZE, true, false));
//    }
//
//    private void initializeLabels() {
//        productNameLbl.setWrappingWidth(LABEL_WIDTH);
//        productNameLbl.setWrapText(true);
//        productNameLbl.setLabelFor(this);
//        productPriceLbl.setLabelFor(this);
//        productQuantityLbl.setLabelFor(this);
//
//        productNameLbl.getStyleClass().add("pos-product-card-name");
//        productPriceLbl.getStyleClass().add("pos-product-card-price");
//        productQuantityLbl.getStyleClass().add("pos-product-card-quantity");
//
//        setAlignment(Pos.CENTER);
//        setSpacing(SPACING);
//    }
//
//    private Rectangle getProductImage() {
//        productImage = PreloadedData.productPlaceholderImage;
//        updateProductImageHolder();
//        return productImageHolder;
//    }
//
//    private void updateProductImageHolder() {
//        if (productImage != null) {
//            productImageHolder.setFill(new ImagePattern(productImage));
//            productImageHolder.setWidth(IMAGE_SIZE);
//            productImageHolder.setHeight(IMAGE_SIZE);
//            productImageHolder.setCache(true);
//            productImageHolder.setCacheHint(CacheHint.SPEED);
//            productImageHolder.setArcWidth(ARC_SIZE);
//            productImageHolder.setArcHeight(ARC_SIZE);
//            centerImage();
//        }
//    }
//
//    private void centerImage() {
//        if (productImage != null) {
//            double ratioX = productImageHolder.getWidth() / productImage.getWidth();
//            double ratioY = productImageHolder.getHeight() / productImage.getHeight();
//            double reduceCoeff = Math.min(ratioX, ratioY);
//            double w = productImage.getWidth() * reduceCoeff;
//            double h = productImage.getHeight() * reduceCoeff;
//            productImageHolder.setX((productImageHolder.getWidth() - w) / 2);
//            productImageHolder.setY((productImageHolder.getHeight() - h) / 2);
//        }
//    }
//
//    private Region createSpacer() {
//        Region space = new Region();
//        space.setPrefHeight(SPACING);
//        return space;
//    }
//
//    private void loadImageAsync() {
//        String imagePath = product.getImage();
//
//        if (imagePath.isEmpty()) {
//            Platform.runLater(this::updateProductImageHolderWithPlaceholder);
//            return;
//        }
//
//        if (IMAGE_CACHE.containsKey(imagePath)) {
//            productImage = IMAGE_CACHE.get(imagePath);
//            Platform.runLater(this::updateProductImageHolder);
//            return;
//        }
//
//        Task<Image> loadImageTask = new Task<>() {
//            @Override
//            protected Image call() {
//                try {
//                    return new Image(imagePath, IMAGE_SIZE, IMAGE_SIZE, true, false);
//                } catch (IllegalArgumentException e) {
//                    log.warning("Invalid image URL: " + imagePath);
//                    return PreloadedData.imageErrorPlaceholderImage;
//                }
//            }
//        };
//
//        loadImageTask.setOnSucceeded(event -> {
//            productImage = loadImageTask.getValue();
//            IMAGE_CACHE.put(imagePath, productImage);
//            Platform.runLater(() -> {
//                productImageHolder.setFill(new ImagePattern(productImage));
//                centerImage();
//            });
//        });
//
//        loadImageTask.setOnFailed(event -> {
//            log.warning("Failed to load image from URL: " + imagePath + ". " + loadImageTask.getException());
//            Platform.runLater(() -> updateProductImageHolderWithPlaceholder());
//        });
//
//        new Thread(loadImageTask).start();
//    }
//
//    private void updateProductImageHolderWithPlaceholder() {
//        productImage = PreloadedData.noImagePlaceholderImage;
//        updateProductImageHolder();
//    }
//
//    private Image getCachedImage(String imagePath) {
//        return IMAGE_CACHE.computeIfAbsent(imagePath, path -> new Image(path, IMAGE_SIZE, IMAGE_SIZE, true, false));
//    }
//}
