package inc.nomard.spoty.core.views.layout;

import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ApplicationWindowImpl extends StackPane implements ApplicationWindow {
    protected final StackPane window1 = new StackPane();
    protected final StackPane window2 = new StackPane();
    protected final StackPane window3 = new StackPane();
    protected final StackPane window4 = new StackPane();
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
        getChildren().setAll(new StackPane(window1, window2, window3, window4), contentLayer);
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
        window1.setEffect(new GaussianBlur(60));
        window1.setMinWidth(MAX_WIDTH);
        window1.setMinHeight(MAX_HEIGHT);

        window2.setEffect(new GaussianBlur(60));
        window2.setMinWidth(MAX_WIDTH);
        window2.setMinHeight(MAX_HEIGHT);
        window2.getStyleClass().add("luminosity");

        window3.setEffect(new GaussianBlur(60));
        window3.setMinWidth(MAX_WIDTH);
        window3.setMinHeight(MAX_HEIGHT);
        window3.getStyleClass().add("tint");

        window4.setEffect(new GaussianBlur(60));
        window4.setMinWidth(MAX_WIDTH);
        window4.setMinHeight(MAX_HEIGHT);
        window4.getStyleClass().add("noise");
//        window4.setBackground(
//                new Background(
//                        new BackgroundFill(Color.rgb(0, 0, 0, 0.6), CornerRadii.EMPTY, Insets.EMPTY)
//                )
//        );
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
