package inc.nomard.spoty.core.views.layout;

import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import javafx.scene.Node;
import javafx.scene.Parent;

public abstract class ModalPage extends MFXGenericDialog implements Modal {
    public ModalPage() {
        super();
        setViewProperties();
    }

    protected void addNode(MFXGenericDialog node) {
        this.setContent(node);
    }

    @Override
    public void reset() {
    }

    private void setStyles() {
        this.getStylesheets().addAll(
                SpotyCoreResourceLoader.load("styles/base.css"),
                SpotyCoreResourceLoader.load("styles/Splash.css"),
                SpotyCoreResourceLoader.load("styles/Common.css"),
                SpotyCoreResourceLoader.load("styles/toolitip.css"),
                SpotyCoreResourceLoader.load("styles/TextFields.css"),
                SpotyCoreResourceLoader.load("styles/theming/base.css")
        );
    }

    @Override
    public Node getSnapshotTarget() {
        return this;
    }

    @Override
    public Parent getView() {
        return this;
    }

    @Override
    public void dispose() {
    }

    public void setViewProperties() {
        this.setShowMinimize(false);
        this.setShowAlwaysOnTop(false);
        this.setShowClose(false);
        this.setStyles();
    }
}
