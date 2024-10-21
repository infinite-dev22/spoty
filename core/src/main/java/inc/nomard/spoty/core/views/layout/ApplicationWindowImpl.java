package inc.nomard.spoty.core.views.layout;

import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class ApplicationWindowImpl extends StackPane implements ApplicationWindow {
    protected final StackPane window1 = new StackPane();
    protected final AnchorPane contentLayer = new AnchorPane();
    protected boolean isRendered = false;

    protected ApplicationWindowImpl() {
        super();
        this.setStyleSheets();
        this.getStyleClass().add("app-root");
        this.setPadding(new Insets(0d));
        AppManager.setParent(this);
        createPageLayout();
        AppManager.setMorphPane(contentLayer);
    }

    protected void createPageLayout() {
        setMinWidth(ApplicationWindow.MAX_WIDTH);
        setMinHeight(ApplicationWindow.MAX_HEIGHT);
        window1.getStyleClass().add("windows");
        getChildren().setAll(window1, contentLayer);
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
        this.getStylesheets().addAll(
                SpotyCoreResourceLoader.load("styles/base.css"),
                SpotyCoreResourceLoader.load("styles/Common.css"),
                SpotyCoreResourceLoader.load("styles/toolitip.css"),
                SpotyCoreResourceLoader.load("styles/TextFields.css"),
                SpotyCoreResourceLoader.load("styles/theming/base.css")
        );
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
