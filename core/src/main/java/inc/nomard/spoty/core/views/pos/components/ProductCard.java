package inc.nomard.spoty.core.views.pos.components;

import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.values.PreloadedData;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.utils.AppUtils;
import inc.nomard.spoty.utils.navigation.Spacer;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log
public class ProductCard extends VBox {
    private static final double IMAGE_SIZE = 160;
    private static final double ARC_SIZE = 20;
    private static final double SPACING = 10;
    private static final double LABEL_WIDTH = 150;
    @Getter
    private final Product product;
    private final Label productNameLbl;
    private final Label productPriceLbl;
    private final Label productQuantityLbl;

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
                getProductImage(new Image(product.getImage(), IMAGE_SIZE, IMAGE_SIZE, true, true, true)),
                labelsHolder,
                createSpacer()
        );

        this.setCache(true);
        this.setCacheHint(CacheHint.SPEED);
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

    private ImageView getProductImage(Image image) {
        var clip = new Rectangle(IMAGE_SIZE, IMAGE_SIZE);
        clip.setArcWidth(ARC_SIZE);
        clip.setArcHeight(ARC_SIZE);

        var imageView = new ImageView(PreloadedData.productPlaceholderImage);
        imageView.setFitWidth(IMAGE_SIZE);
        imageView.setFitHeight(IMAGE_SIZE);
        imageView.setClip(clip);

        image.progressProperty().addListener((obs, oldProgress, newProgress) -> {
            if (newProgress.doubleValue() >= 1.0 && !image.isError()) {
                imageView.setImage(image); // Set the loaded image
            } else if (image.isError()) {
                imageView.setImage(PreloadedData.imageErrorPlaceholderImage);
            }
        });

        return imageView;
    }

    private Region createSpacer() {
        return new Spacer(Orientation.VERTICAL);
    }
}
