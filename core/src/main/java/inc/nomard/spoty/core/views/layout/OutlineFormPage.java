package inc.nomard.spoty.core.views.layout;

import inc.nomard.spoty.core.views.util.Page;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public abstract class OutlineFormPage extends StackPane implements Page {

    protected static final int OUTLINE_WIDTH = 200;
    protected final StackPane userContentArea = new StackPane();
    protected boolean isRendered = false;

    protected OutlineFormPage() {
        super();

        getStyleClass().add("outline-page");

        createPageLayout();
    }

    protected void createPageLayout() {
        var pageBody = new StackPane();
        pageBody.getChildren().setAll(userContentArea);
        pageBody.getStyleClass().add("body");

        setMinWidth(Page.MAX_WIDTH);
        getChildren().setAll(pageBody);
    }

    protected void addNode(Node node) {
        userContentArea.getChildren().add(node);
    }

    @Override
    public Pane getView() {
        return this;
    }

    @Override
    public Node getSnapshotTarget() {
        return userContentArea;
    }

    @Override
    public void reset() {
    }

    public void dispose() {
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