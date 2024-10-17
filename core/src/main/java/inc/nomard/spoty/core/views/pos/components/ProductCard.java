package inc.nomard.spoty.core.views.pos.components;

import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.values.PreloadedData;
import inc.nomard.spoty.core.views.layout.navigation.Spacer;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.utils.AppUtils;
import io.github.palexdev.virtualizedfx.cells.base.VFXCell;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductCard extends VBox implements VFXCell<Product> {
    private static final Double IMAGE_SIZE = 160d;
    private static final Double ARC_SIZE = 20d;
    private static final Double SPACING = 10d;
    protected final IntegerProperty indexProperty = new SimpleIntegerProperty();
    private final Label productNameLbl;
    private final Label productPriceLbl;
    private final Label productQuantityLbl;
    private final ObjectProperty<Product> productProperty = new SimpleObjectProperty<>();

    public ProductCard() {
        productNameLbl = new Label();
        productPriceLbl = new Label();
        productQuantityLbl = new Label();

        var labelsHolder = new VBox(
                productNameLbl,
                productPriceLbl,
                productQuantityLbl);
        labelsHolder.setAlignment(Pos.CENTER);
        labelsHolder.setPadding(new Insets(0d, 5d, 0d, 5d));
        labelsHolder.setMaxWidth(160);

        initializeLabels();
        attachListeners();

        this.getStyleClass().add("pos-product-card");

        getChildren().addAll(
                getProductImage(),
                labelsHolder,
                createSpacer()
        );

        this.setCache(true);
        this.setCacheHint(CacheHint.SPEED);
    }

    public Product getProduct() {
        return this.productProperty.get();
    }

    public void setProduct(Product product) {
        this.productProperty.set(product);
    }

    public ObjectProperty<Product> productProperty() {
        return this.productProperty;
    }

    public Integer getIndex() {
        return this.indexProperty.get();
    }

    public void setIndex(Integer index) {
        this.indexProperty.set(index);
    }

    public IntegerProperty indexProperty() {
        return this.indexProperty;
    }

    private void initializeLabels() {
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

    private void attachListeners() {
        this.productProperty.addListener((_, _, product) -> {
            productNameLbl.setText(product.getName());
            productPriceLbl.setText("UGX " + AppUtils.decimalFormatter().format(product.getSalePrice()));
            productQuantityLbl.setText(AppUtils.decimalFormatter().format(product.getQuantity()) + " Pcs");
        });
    }

    private ImageView getProductImage() {
        var clip = new Rectangle(IMAGE_SIZE, IMAGE_SIZE);
        clip.setArcWidth(ARC_SIZE);
        clip.setArcHeight(ARC_SIZE);

        var imageView = new ImageView(PreloadedData.productPlaceholderImage);
        imageView.setFitWidth(IMAGE_SIZE);
        imageView.setFitHeight(IMAGE_SIZE);
        imageView.setClip(clip);

        productProperty.addListener((_, _, newValue) -> {
            var image = new Image(newValue.getImage(), IMAGE_SIZE, IMAGE_SIZE, true, true, true);
            image.progressProperty().addListener((_, _, newProgress) -> {
                if (newProgress.doubleValue() >= 1.0 && !image.isError()) {
                    imageView.setImage(image); // Set the loaded image
                } else if (image.isError()) {
                    imageView.setImage(PreloadedData.imageErrorPlaceholderImage);
                }
            });
        });

        return imageView;
    }

    private Region createSpacer() {
        return new Spacer(Orientation.VERTICAL);
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }

    @Override
    public Node toNode() {
        return this;
    }

    @Override
    public void updateIndex(int i) {
        this.setIndex(i);
    }

    @Override
    public void updateItem(Product product) {
        this.setProduct(product);
    }

    @Override
    public void beforeLayout() {
        VFXCell.super.beforeLayout();
    }

    @Override
    public void afterLayout() {
        VFXCell.super.afterLayout();
    }

    @Override
    public void onCache() {
        VFXCell.super.onCache();
    }

    @Override
    public void onDeCache() {
        VFXCell.super.onDeCache();
    }

    @Override
    public void dispose() {
        VFXCell.super.dispose();
    }
}
