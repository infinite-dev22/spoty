package inc.nomard.spoty.core.views.pos.components;

import inc.nomard.spoty.core.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.mfxcore.controls.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Log
public class ProductCard extends VBox {
    private static final Random RND = new Random();
    private static final double IMAGE_SIZE = 160;
    private static final double ARC_SIZE = 20;
    private static final double SPACING = 10;
    private static final double LABEL_WIDTH = 200;
    private static final String PLACEHOLDER_IMAGE = SpotyCoreResourceLoader.load("images/product-image-placeholder.png");
    private static final String NO_IMAGE_PLACEHOLDER = SpotyCoreResourceLoader.load("images/no-image-placeholder.png");
    private static final String IMAGE_FAILED_TO_LOAD_PLACEHOLDER = SpotyCoreResourceLoader.load("images/image-loading-failed.png");
    private static final List<String> IMAGE_PATHS = List.of(
            "https://images.unsplash.com/photo-1523275335684-37898b6baf30?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb&dl=rachit-tank-2cFZ_FB08UM-unsplash.jpg",
            "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb&dl=c-d-x-PDX_a_82obo-unsplash.jpg",
            "https://images.unsplash.com/photo-1572635196237-14b3f281503f?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb&dl=giorgio-trovato-K62u25Jk6vo-unsplash.jpg",
            "https://images.unsplash.com/photo-1560343090-f0409e92791a?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb&dl=irene-kredenets-KStSiM1UvPw-unsplash.jpg",
            "https://images.unsplash.com/photo-1586495777744-4413f21062fa?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb&dl=caroline-attwood-E1rH__X9SA0-unsplash.jpg",
            "https://images.unsplash.com/photo-1602143407151-7111542de6e8?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb&dl=joan-tran-reEySFadyJQ-unsplash.jpg",
            "",
            "https://images.unsplash.com/photo-1602143407151-7111542de6e8?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb&dl=joan-tran-reEySFadwwveyJQ-unsplash.jpg"
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
                getProductImage(PLACEHOLDER_IMAGE),
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
        productQuantityLbl.getStyleClass().add("pos-product-card-quantity");

        setAlignment(Pos.CENTER);
        setSpacing(SPACING);
    }

    private Rectangle getProductImage(String image) {
        productImage = getCachedImage(image);
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
                    return new Image(IMAGE_FAILED_TO_LOAD_PLACEHOLDER, IMAGE_SIZE, IMAGE_SIZE, true, false);
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
        productImage = getCachedImage(NO_IMAGE_PLACEHOLDER);
        updateProductImageHolder();
    }

    private Image getCachedImage(String imagePath) {
        return IMAGE_CACHE.computeIfAbsent(imagePath, path -> new Image(path, IMAGE_SIZE, IMAGE_SIZE, true, false));
    }
}
