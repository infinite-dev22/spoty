package inc.nomard.spoty.core.views.components.label_components.controls;

import inc.nomard.spoty.core.util.validation.Validated;
import inc.nomard.spoty.core.util.validation.Validator;
import inc.nomard.spoty.core.views.components.label_components.skins.LabeledPasswordFieldSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class LabeledPasswordField extends Control implements Validated {
    protected final Validator validator;
    private final StringProperty labelText = new SimpleStringProperty(this, "labelText");
    private final StringProperty text = new SimpleStringProperty(this, "text");
    private final ObjectProperty<Node> right = new SimpleObjectProperty<>(this, "right");

    public LabeledPasswordField() {
        super();
        this.validator = new Validator();
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
    public Validator getValidator() {
        return this.validator;
    }
}