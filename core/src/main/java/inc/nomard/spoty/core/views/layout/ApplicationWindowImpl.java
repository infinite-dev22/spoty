package inc.nomard.spoty.core.views.layout;

import inc.nomard.spoty.core.*;
import javafx.scene.*;
import javafx.scene.layout.*;

public class ApplicationWindowImpl extends StackPane implements ApplicationWindow {
    protected final GlassPane window = new GlassPane();
    protected boolean isRendered = false;

    protected ApplicationWindowImpl() {
        super();

        createPageLayout();
    }

    protected void createPageLayout() {
        setMinWidth(ApplicationWindow.MAX_WIDTH);
        setMinHeight(ApplicationWindow.MAX_HEIGHT);
        getChildren().setAll(window);
    }

    protected void addNode(Node node) {
        window.getChildren().add(node);
    }

    @Override
    public Parent getView() {
        return this;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void setLightMode() {
        if (getStyleClass().isEmpty()) {
            getStyleClass().clear();
        }
        getStyleClass().addAll(SpotyCoreResourceLoader.load("styles/base.css"),
                SpotyCoreResourceLoader.load("styles/Buttons.css"),
                SpotyCoreResourceLoader.load("styles/Common.css"),
                SpotyCoreResourceLoader.load("styles/Progress.css"),
                SpotyCoreResourceLoader.load("styles/Splash.css"),
                SpotyCoreResourceLoader.load("styles/TextFields.css"),
                SpotyCoreResourceLoader.load("styles/theming/Default.css"));
    }

    @Override
    public void setDarkMode() {
        if (getStyleClass().isEmpty()) {
            getStyleClass().clear();
        }
        getStyleClass().addAll(SpotyCoreResourceLoader.load("styles/base.css"),
                SpotyCoreResourceLoader.load("styles/Buttons.css"),
                SpotyCoreResourceLoader.load("styles/Common.css"),
                SpotyCoreResourceLoader.load("styles/Progress.css"),
                SpotyCoreResourceLoader.load("styles/Splash.css"),
                SpotyCoreResourceLoader.load("styles/TextFields.css"),
                SpotyCoreResourceLoader.load("styles/theming/Dark.css"));
    }

    @Override
    public void setMorph(Boolean morph) {
        window.morph(morph);
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        if (isRendered) {
            return;
        }

        isRendered = true;
        onRendered();
    }

    // Some properties can only be obtained after node placed
    // to the scene graph and here is the place do this.
    protected void onRendered() {
    }
}
