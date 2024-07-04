package inc.nomard.spoty.core.views.layout;

import inc.nomard.spoty.core.*;
import inc.nomard.spoty.core.views.util.*;
import io.github.palexdev.materialfx.dialogs.*;

public abstract class ModalPage extends MFXStageDialog implements Page {
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

    public void setLightMode() {
        this.getContent().getStylesheets().addAll(SpotyCoreResourceLoader.load("styles/base.css"),
                SpotyCoreResourceLoader.load("styles/Buttons.css"),
                SpotyCoreResourceLoader.load("styles/Common.css"),
                SpotyCoreResourceLoader.load("styles/TextFields.css"),
                SpotyCoreResourceLoader.load("styles/theming/Default.css"));
    }

    public void setDarkMode() {
        this.getContent().getStylesheets().addAll(SpotyCoreResourceLoader.load("styles/base.css"),
                SpotyCoreResourceLoader.load("styles/Buttons.css"),
                SpotyCoreResourceLoader.load("styles/Common.css"),
                SpotyCoreResourceLoader.load("styles/TextFields.css"),
                SpotyCoreResourceLoader.load("styles/theming/Dark.css"));
    }
}
