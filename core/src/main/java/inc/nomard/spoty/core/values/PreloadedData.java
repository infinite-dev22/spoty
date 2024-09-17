package inc.nomard.spoty.core.values;

import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import javafx.scene.image.Image;

public class PreloadedData {
    private static final String PLACEHOLDER_IMAGE = SpotyCoreResourceLoader.load("images/product-image-placeholder.png");
    private static final String USER_PLACEHOLDER_IMAGE = SpotyCoreResourceLoader.load("images/user-place-holder.png");
    private static final String NO_IMAGE_PLACEHOLDER = SpotyCoreResourceLoader.load("images/no-image-placeholder.png");
    private static final String IMAGE_FAILED_TO_LOAD_PLACEHOLDER = SpotyCoreResourceLoader.load("images/image-loading-failed.png");
    // Window icons
    private static final String ICON16 = SpotyCoreResourceLoader.load("images/icons/icon-16x16.png");
    private static final String ICON32 = SpotyCoreResourceLoader.load("images/icons/icon-32x32.png");
    private static final String ICON64 = SpotyCoreResourceLoader.load("images/icons/icon-64x64.png");
    private static final String ICON128 = SpotyCoreResourceLoader.load("images/icons/icon-128x128.png");
    private static final String ICON256 = SpotyCoreResourceLoader.load("images/icons/icon-256x256.png");
    private static final String ICON512 = SpotyCoreResourceLoader.load("images/icons/icon-512x512.png");
    private static final double IMAGE_SIZE = 160;
    public static Image userPlaceholderImage = null;
    public static Image productPlaceholderImage = null;
    public static Image noImagePlaceholderImage = null;
    public static Image imageErrorPlaceholderImage = null;
    public static Image icon16 = null;
    public static Image icon32 = null;
    public static Image icon64 = null;
    public static Image icon128 = null;
    public static Image icon256 = null;
    public static Image icon512 = null;

    public static void preloadImages() {
        userPlaceholderImage = new Image(USER_PLACEHOLDER_IMAGE, IMAGE_SIZE, IMAGE_SIZE, true, true);
        productPlaceholderImage = new Image(PLACEHOLDER_IMAGE, IMAGE_SIZE, IMAGE_SIZE, true, true);
        noImagePlaceholderImage = new Image(NO_IMAGE_PLACEHOLDER, IMAGE_SIZE, IMAGE_SIZE, true, true);
        imageErrorPlaceholderImage = new Image(IMAGE_FAILED_TO_LOAD_PLACEHOLDER, IMAGE_SIZE, IMAGE_SIZE, true, true);
        icon16 = new Image(ICON16, true);
        icon32 = new Image(ICON32, true);
        icon64 = new Image(ICON64, true);
        icon128 = new Image(ICON128, true);
        icon256 = new Image(ICON256, true);
        icon512 = new Image(ICON512, true);
    }
}
