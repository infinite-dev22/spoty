package inc.nomard.spoty.core.values;

import inc.nomard.spoty.core.*;
import javafx.scene.image.*;

public class PreloadedData {
    private static final String PLACEHOLDER_IMAGE = SpotyCoreResourceLoader.load("images/product-image-placeholder.png");
    private static final String USER_PLACEHOLDER_IMAGE = SpotyCoreResourceLoader.load("images/user-place-holder.png");
    private static final String NO_IMAGE_PLACEHOLDER = SpotyCoreResourceLoader.load("images/no-image-placeholder.png");
    private static final String IMAGE_FAILED_TO_LOAD_PLACEHOLDER = SpotyCoreResourceLoader.load("images/image-loading-failed.png");
    private static final double IMAGE_SIZE = 160;
    public static Image userPlaceholderImage = null;
    public static Image productPlaceholderImage = null;
    public static Image noImagePlaceholderImage = null;
    public static Image imageErrorPlaceholderImage = null;

    public static void preloadImages() {
        userPlaceholderImage = new Image(USER_PLACEHOLDER_IMAGE, IMAGE_SIZE, IMAGE_SIZE, true, false);
        productPlaceholderImage = new Image(PLACEHOLDER_IMAGE, IMAGE_SIZE, IMAGE_SIZE, true, false);
        noImagePlaceholderImage = new Image(NO_IMAGE_PLACEHOLDER, IMAGE_SIZE, IMAGE_SIZE, true, false);
        imageErrorPlaceholderImage = new Image(IMAGE_FAILED_TO_LOAD_PLACEHOLDER, IMAGE_SIZE, IMAGE_SIZE, true, false);
    }
}
