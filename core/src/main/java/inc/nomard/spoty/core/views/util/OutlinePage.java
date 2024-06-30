package inc.nomard.spoty.core.views.util;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public abstract class OutlinePage extends StackPane implements Page {

    protected static final int OUTLINE_WIDTH = 200;
    protected final StackPane userContentArea = new StackPane();
    protected boolean isRendered = false;

    protected OutlinePage(Stage stage) {
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