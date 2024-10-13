package inc.nomard.spoty.core.views.util;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public abstract class OutlinePage extends StackPane implements Page {
    protected boolean isRendered = false;

    protected OutlinePage() {
        super();
        createPageLayout();
    }

    protected void createPageLayout() {
        getStyleClass().add("body");
    }

    protected void addNode(Node... node) {
        getChildren().addAll(node);
    }

    @Override
    public Pane getView() {
        return this;
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