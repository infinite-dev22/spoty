package inc.nomard.spoty.core.views.layout;

import inc.nomard.spoty.core.*;
import io.github.palexdev.materialfx.dialogs.*;
import javafx.scene.*;

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
    }
}
