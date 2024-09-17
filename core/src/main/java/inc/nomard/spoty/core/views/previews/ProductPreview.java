package inc.nomard.spoty.core.views.previews;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import inc.nomard.spoty.core.values.PreloadedData;
import inc.nomard.spoty.core.views.pos.components.ProductCard;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.functional_paradigm.SpotyGotFunctional;
import inc.nomard.spoty.utils.navigation.Spacer;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;
import lombok.extern.java.Log;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log
public class ProductPreview extends BorderPane {
    private static final double IMAGE_SIZE = 160;
    private static final double ARC_SIZE = 20;
    private static final Map<String, Image> IMAGE_CACHE = new ConcurrentHashMap<>();
    private static final int MAX_CONCURRENT_TASKS = 2;
    private static final ExecutorService imageLoaderExecutor = Executors.newFixedThreadPool(MAX_CONCURRENT_TASKS);
    private static final StringProperty nameProperty = new SimpleStringProperty();
    private static final StringProperty categoryProperty = new SimpleStringProperty();
    private static final StringProperty brandProperty = new SimpleStringProperty();
    private static final DoubleProperty costProperty = new SimpleDoubleProperty();
    private static final DoubleProperty priceProperty = new SimpleDoubleProperty();
    private static final StringProperty unitProperty = new SimpleStringProperty();
    private static final DoubleProperty taxProperty = new SimpleDoubleProperty();
    private static final DoubleProperty discountProperty = new SimpleDoubleProperty();
    private static final DoubleProperty stockQuantityProperty = new SimpleDoubleProperty();
    private static final DoubleProperty alertProperty = new SimpleDoubleProperty();
    private static final StringProperty descriptionProperty = new SimpleStringProperty();
    private static final ObjectProperty<ImagePattern> barcodeProperty = new SimpleObjectProperty<>();
    private final Rectangle productImageHolder = new Rectangle();
    private final ModalPane modalPane;
    private Image productImage;

    public ProductPreview(Product product, ModalPane modalPane) {
        this.modalPane = modalPane;
        initUI();
        initData(product);
    }

    private Text buildHeaderText(String txt) {
        var text = new Text(txt);
        text.getStyleClass().addAll(Styles.TITLE_2, Styles.TEXT_SUBTLE, Styles.TEXT_ITALIC);
        return text;
    }

    private HBox buildSpacedTitledText(String title, StringProperty property) {
        var text1 = new Text(title + ":");
        text1.getStyleClass().add(Styles.TEXT_SUBTLE);
        var text2 = new Text();
        text2.getStyleClass().addAll(Styles.TEXT_BOLD);
        text2.textProperty().bind(property);
        return new HBox(20d, text1, new Spacer(), text2);
    }

    private HBox buildSpacedTitledText(String title, DoubleProperty property) {
        var text1 = new Text(title + ":");
        text1.getStyleClass().add(Styles.TEXT_SUBTLE);
        var text2 = new Text();
        text2.getStyleClass().addAll(Styles.TEXT_BOLD);
        text2.textProperty().bindBidirectional(property, new NumberStringConverter());
        return new HBox(20d, text1, new Spacer(), text2);
    }

    private Rectangle buildProductImageHolder() {
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

    private VBox buildProductDetails() {
        var vbox = new VBox(20d,
                buildSpacedTitledText("Name", nameProperty),
                buildSpacedTitledText("Category", categoryProperty),
                buildSpacedTitledText("Brand", brandProperty),
                buildSpacedTitledText("Cost", costProperty),
                buildSpacedTitledText("Price", priceProperty),
                buildSpacedTitledText("Unit", unitProperty),
                buildSpacedTitledText("Tax", taxProperty),
                buildSpacedTitledText("Discount", discountProperty),
                buildSpacedTitledText("Stock Quantity", stockQuantityProperty),
                buildSpacedTitledText("Alert Quantity", alertProperty),
                buildSpacedTitledText("Description", descriptionProperty));
        VBox.setVgrow(vbox, Priority.ALWAYS);
        return vbox;
    }

    private VBox buildProduct() {
        var vbox = new VBox(30d,
                buildProductImageHolder(),
                buildProductDetails());
        vbox.setPadding(new Insets(10d));
        vbox.getStyleClass().add("card-raised");
        HBox.setHgrow(vbox, Priority.ALWAYS);
        return vbox;
    }

    private VBox buildBody() {
        var vbox = new VBox(10d,
                buildHeaderText("Product details"),
                buildProduct());
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    private ScrollPane buildBodyScroll() {
        var scroll = new ScrollPane((buildBody()));
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        return scroll;
    }

    public void initUI() {
        this.setTop(buildTop());
        this.setCenter(buildBodyScroll());
        this.setPadding(new Insets(10d));
        this.setMaxWidth(1000d);
        this.setPrefWidth(700d);
        this.setMinWidth(400d);

        this.getStylesheets().addAll(
                SpotyCoreResourceLoader.load("styles/base.css"),
                SpotyCoreResourceLoader.load("styles/Common.css"),
                SpotyCoreResourceLoader.load("styles/TextFields.css"),
                SpotyCoreResourceLoader.load("styles/theming/Default.css")
        );
    }

    public void initData(Product product) {
        nameProperty.set(product.getName());
        categoryProperty.set(product.getCategoryName());
        brandProperty.set(product.getBrandName());
        costProperty.set(product.getCostPrice());
        priceProperty.set(product.getSalePrice());
        unitProperty.set(product.getUnit().getName());
        taxProperty.set(Objects.nonNull(product.getTax()) ? product.getTax().getPercentage() : 0d);
        discountProperty.set(Objects.nonNull(product.getDiscount()) ? product.getDiscount().getPercentage() : 0d);
        stockQuantityProperty.set(product.getQuantity());
        alertProperty.set(product.getStockAlert());
        descriptionProperty.set(product.getDescription());
        Platform.runLater(() -> loadImageAsync(product.getImage()));
    }

    private FontIcon buildFontIcon(SpotyGotFunctional.ParameterlessConsumer onAction, String styleClass) {
        var icon = new FontIcon(FontAwesomeSolid.CIRCLE);
        icon.setOnMouseClicked(event -> onAction.run());
        icon.getStyleClass().addAll(styleClass, Styles.DANGER);
        return icon;
    }

    private HBox buildTop() {
        var hbox = new HBox(buildFontIcon(this::dispose, "close-icon"));
        hbox.setMaxHeight(20d);
        hbox.setMinHeight(20d);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    public void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
    }
}
