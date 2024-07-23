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
    private static final List<String> IMAGE_PATHS = List.of(
            "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=160&h=160&ixlib=rb-4.0.3&q=85&fm=png&crop=entropy&cs=srgb&dl=rachit-tank-2cFZ_FB08UM-unsplash.png",
            "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=160&h=160&ixlib=rb-4.0.3&q=85&fm=png&crop=entropy&cs=srgb&dl=c-d-x-PDX_a_82obo-unsplash.png",
            "https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=160&h=160&ixlib=rb-4.0.3&q=85&fm=png&crop=entropy&cs=srgb&dl=giorgio-trovato-K62u25Jk6vo-unsplash.png",
            "https://images.unsplash.com/photo-1560343090-f0409e92791a?w=160&h=160&ixlib=rb-4.0.3&q=85&fm=png&crop=entropy&cs=srgb&dl=irene-kredenets-KStSiM1UvPw-unsplash.png",
            "https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=160&h=160&ixlib=rb-4.0.3&q=85&fm=png&crop=entropy&cs=srgb&dl=caroline-attwood-E1rH__X9SA0-unsplash.png",
            "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=160&h=160&ixlib=rb-4.0.3&q=85&fm=png&crop=entropy&cs=srgb&dl=joan-tran-reEySFadyJQ-unsplash.png",
            "",
            "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=160&h=160&ixlib=rb-4.0.3&q=85&fm=png&crop=entropy&cs=srgb&dl=joan-tran-reEySFadwwveyJQ-unsplash.png"
    );

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

        loadImageAsync();
    }

    public static void preloadImages() {
        IMAGE_PATHS.forEach(path -> IMAGE_CACHE.computeIfAbsent(path, key -> new Image(key, IMAGE_SIZE, IMAGE_SIZE, true, false)));
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

    private void loadImageAsync() {
        String imagePath = IMAGE_PATHS.get(RND.nextInt(IMAGE_PATHS.size()));

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
            Platform.runLater(() -> updateProductImageHolderWithPlaceholder());
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
