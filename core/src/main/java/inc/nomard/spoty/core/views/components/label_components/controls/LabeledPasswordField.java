package inc.nomard.spoty.core.views.components.label_components.controls;

import inc.nomard.spoty.core.views.components.label_components.skins.*;
import io.github.palexdev.materialfx.validation.*;
import javafx.beans.property.*;
import javafx.scene.*;
import javafx.scene.control.*;

public class LabeledPasswordField extends Control implements Validated {
    protected final MFXValidator validator;
    private final StringProperty labelText = new SimpleStringProperty(this, "labelText");
    private final StringProperty text = new SimpleStringProperty(this, "text");
    private final ObjectProperty<Node> right = new SimpleObjectProperty<>(this, "right");

    public LabeledPasswordField() {
        super();
        this.validator = new MFXValidator();
        this.getStyleClass().add("labeled-text-field");
    }

    public String getLabelText() {
        return labelText.get();
    }

    public void setLabel(String labelText) {
        this.labelText.set(labelText);
    }

    public StringProperty labelTextProperty() {
        return labelText;
    }

    public ObjectProperty<Node> rightProperty() {
        return right;
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public void setRight(Node node) {
        this.right.set(node);
    }

    public StringProperty textProperty() {
        return text;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LabeledPasswordFieldSkin(this);
    }

    @Override
    public MFXValidator getValidator() {
        return this.validator;
    }
}