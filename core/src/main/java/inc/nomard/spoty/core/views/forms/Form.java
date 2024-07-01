package inc.nomard.spoty.core.views.forms;

import inc.nomard.spoty.core.views.util.*;
import io.github.palexdev.mfxcore.controls.*;
import java.util.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.*;
import javafx.scene.layout.*;

public abstract class Form extends StackPane implements Page {

    protected static final int OUTLINE_WIDTH = 200;

    protected final ScrollPane scrollPane = new ScrollPane();
    protected final VBox userContent = new VBox();
    protected final StackPane userContentArea = new StackPane(userContent);
    protected boolean isRendered = false;

    protected Form() {
        super();

        userContent.getStyleClass().add("user-content");
        getStyleClass().add("outline-page");

        createPageLayout();
    }

    protected void createPageLayout() {
        StackPane.setMargin(userContent, new Insets(0, OUTLINE_WIDTH, 0, 0));

        scrollPane.setContent(userContentArea);
        NodeUtils.setScrollConstraints(scrollPane, AS_NEEDED, true, NEVER, true);
        scrollPane.setMaxHeight(20_000);

        var pageBody = new StackPane();
        pageBody.getChildren().setAll(scrollPane);
        pageBody.getStyleClass().add("body");

        setMinWidth(Page.MAX_WIDTH);
        getChildren().setAll(pageBody);
    }

    protected void addNode(Node node) {
        userContent.getChildren().add(node);
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

    ///////////////////////////////////////////////////////////////////////////

    public record Heading(String title, Node anchor) {

        private static final Heading TOP = new Heading("Top", new Text());

        public Heading {
            Objects.requireNonNull(title, "title");
            Objects.requireNonNull(anchor, "anchor");
        }
    }
}