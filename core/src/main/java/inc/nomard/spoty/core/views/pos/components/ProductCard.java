package inc.nomard.spoty.core.views.pos.components;

import atlantafx.base.theme.*;
import inc.nomard.spoty.core.values.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.navigation.*;
import io.github.palexdev.mfxcore.controls.*;
import java.util.*;
import java.util.concurrent.*;
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
    private static final double IMAGE_SIZE = 160;
    private static final double ARC_SIZE = 20;
    private static final double SPACING = 10;
    private static final double LABEL_WIDTH = 150;
    private static final Map<String, Image> IMAGE_CACHE = new ConcurrentHashMap<>();
    private static final int MAX_CONCURRENT_TASKS = 10;
    private static final ExecutorService imageLoaderExecutor = Executors.newFixedThreadPool(MAX_CONCURRENT_TASKS);
    //    private final ImageView productImageHolder = new ImageView();
    private final Rectangle productImageHolder = new Rectangle();
    @Getter
    private final Product product;
    private final Label productNameLbl;
    private final Label productPriceLbl;
    private final Label productQuantityLbl;
    private Image productImage;
    private boolean imageLoaded = false; // Flag to track if image is loaded

    public ProductCard(Product product) {
        this.product = product;

        productNameLbl = new Label(product.getName());
        productPriceLbl = new Label("UGX " + AppUtils.decimalFormatter().format(product.getSalePrice()));
        productQuantityLbl = new Label(AppUtils.decimalFormatter().format(product.getQuantity()) + " Pcs available");

        var labelsHolder = new VBox(
                productNameLbl,
                productPriceLbl,
                productQuantityLbl);
        labelsHolder.setAlignment(Pos.CENTER);
        labelsHolder.setPadding(new Insets(0d, 5d, 0d, 5d));
        labelsHolder.setMaxWidth(160);

        initializeLabels();

        productImageHolder.setCache(true);
        productImageHolder.setCacheHint(CacheHint.SPEED);

        this.getStyleClass().add("pos-product-card");

        getChildren().addAll(
                getProductImage(),
                labelsHolder,
                createSpacer()
        );

        this.setCache(true);
        this.setCacheHint(CacheHint.SPEED);

        // Set up lazy loading listener
        this.boundsInParentProperty().addListener((obs, oldBounds, newBounds) -> {
            if (!imageLoaded && isVisibleInParent()) {
                // Delay loading slightly to handle rapid scrolling
                Platform.runLater(() -> {
                    if (!imageLoaded && isVisibleInParent()) {
                        imageLoaded = true;
                        loadImageAsync(product.getImage());
                    }
                });
            }
        });
    }

    private boolean isVisibleInParent() {
        if (getParent() == null) return false;
        var parentBounds = getParent().getBoundsInLocal();
        var nodeBounds = getBoundsInParent();
        return parentBounds.intersects(nodeBounds);
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
        productImage = PreloadedData.productPlaceholderImage;
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
        return new Spacer(Orientation.VERTICAL);
    }

    private void loadImageAsync(String imagePath) {
        if (imagePath.isEmpty()) {
            // No image URL, use placeholder
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
                return loadImage(imagePath);
            }
        };

        loadImageTask.setOnSucceeded(event -> {
            productImage = loadImageTask.getValue();
            if (productImage != null) {
                IMAGE_CACHE.put(imagePath, productImage);
                Platform.runLater(() -> {
                    productImageHolder.setFill(new ImagePattern(productImage));
                    centerImage();
                });
            } else {
                log.warning("Image is null, using placeholder.");
                Platform.runLater(this::updateProductImageHolderWithPlaceholder);
            }
        });

        loadImageTask.setOnFailed(event -> {
            log.warning("Failed to load image from URL: " + imagePath + ". " + loadImageTask.getException());
            Platform.runLater(this::updateProductImageHolderWithPlaceholder);
        });

        imageLoaderExecutor.submit(loadImageTask);
    }

    private Image loadImage(String imagePath) {
        try {
            Image image = new Image(imagePath, IMAGE_SIZE, IMAGE_SIZE, true, false);
            if (image.isError()) {
                SpotyLogger.writeToFile(new Exception("Error loading image from URL: " + imagePath), ProductCard.class);
                return PreloadedData.imageErrorPlaceholderImage;
            }
            return image;
        } catch (Exception e) {
            SpotyLogger.writeToFile(new Exception("Invalid image URL: " + imagePath), ProductCard.class);
            return PreloadedData.imageErrorPlaceholderImage;
        }
    }

    private void updateProductImageHolderWithPlaceholder() {
        productImage = PreloadedData.noImagePlaceholderImage;
        updateProductImageHolder();
    }
}
