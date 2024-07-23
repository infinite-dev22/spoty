package inc.nomard.spoty.core.views.layout;

import inc.nomard.spoty.core.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;

public class ApplicationWindowImpl extends StackPane implements ApplicationWindow {
    protected final StackPane window = new StackPane();
    protected final GlassPane contentLayer = new GlassPane();
    protected boolean isRendered = false;

    protected ApplicationWindowImpl() {
        super();
        this.setStyleSheets();
        this.getStyleClass().add("app-root");
        this.setPadding(new Insets(0d));
        AppManager.setParent(this);
        createPageLayout();
        AppManager.setMorphPane(contentLayer);
        blur();
    }

    protected void createPageLayout() {
        setMinWidth(ApplicationWindow.MAX_WIDTH);
        setMinHeight(ApplicationWindow.MAX_HEIGHT);
        getChildren().setAll(window, contentLayer);
    }

    protected void addNode(Node node) {
        contentLayer.getChildren().add(node);
    }

    @Override
    public Parent getView() {
        return this;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void setStyleSheets() {
        this.getStylesheets().addAll(SpotyCoreResourceLoader.load("styles/base.css"),
                SpotyCoreResourceLoader.load("styles/Common.css"),
                SpotyCoreResourceLoader.load("styles/Progress.css"),
                SpotyCoreResourceLoader.load("styles/Splash.css"),
                SpotyCoreResourceLoader.load("styles/TextFields.css"),
                SpotyCoreResourceLoader.load("styles/theming/base.css"));
    }

    @Override
    public void setMorph(Boolean morph) {
        contentLayer.morph(morph);
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

    public void blur() {
        window.setEffect(new GaussianBlur(100));
        window.setMinWidth(MAX_WIDTH);
        window.setMinHeight(MAX_HEIGHT);
        window.getStyleClass().add("window");
        this.setBackground(
                new Background(
                        new BackgroundFill(Color.rgb(0, 0, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)
                )
        );
    }

    // Some properties can only be obtained after node placed
    // to the scene graph and here is the place do this.
    protected void onRendered() {
    }
}
