package inc.nomard.spoty.core.views.layout;

import inc.nomard.spoty.core.*;
import inc.nomard.spoty.core.views.util.*;
import io.github.palexdev.materialfx.dialogs.*;
import javafx.scene.*;

public abstract class ModalPage extends MFXGenericDialog implements Modal {
    public ModalPage() {
        super();
        setLightMode();
    }

    protected void addNode(MFXGenericDialog node) {
        this.setContent(node);
    }

    @Override
    public void reset() {
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

    public void setLightMode() {
        this.getStylesheets().addAll(SpotyCoreResourceLoader.load("styles/base.css"),
                SpotyCoreResourceLoader.load("styles/Buttons.css"),
                SpotyCoreResourceLoader.load("styles/Common.css"),
                SpotyCoreResourceLoader.load("styles/TextFields.css"),
                SpotyCoreResourceLoader.load("styles/theming/Default.css"));
    }

    public void setDarkMode() {
        this.getStylesheets().addAll(SpotyCoreResourceLoader.load("styles/base.css"),
                SpotyCoreResourceLoader.load("styles/Buttons.css"),
                SpotyCoreResourceLoader.load("styles/Common.css"),
                SpotyCoreResourceLoader.load("styles/TextFields.css"),
                SpotyCoreResourceLoader.load("styles/theming/Dark.css"));
    }
}
