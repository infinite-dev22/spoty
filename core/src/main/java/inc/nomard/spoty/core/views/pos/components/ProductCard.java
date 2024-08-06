package inc.nomard.spoty.core.views.pos.components;

import atlantafx.base.theme.*;
import inc.nomard.spoty.core.values.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.navigation.*;
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
    private static final double IMAGE_SIZE = 160;
    private static final double ARC_SIZE = 20;
    private static final double SPACING = 10;
    private static final double LABEL_WIDTH = 150;

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
        this.getStyleClass().add("pos-product-card");

        getChildren().addAll(
                getProductImage(),
                labelsHolder,
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
        return new Spacer(Orientation.VERTICAL);
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
}
