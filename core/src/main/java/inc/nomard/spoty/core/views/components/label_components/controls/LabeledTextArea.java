package inc.nomard.spoty.core.views.components.label_components.controls;

import inc.nomard.spoty.core.views.components.label_components.skins.LabeledTextAreaSkin;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextArea;
import io.github.palexdev.materialfx.validation.MFXValidator;
import io.github.palexdev.materialfx.validation.Validated;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class LabeledTextArea extends Control implements Validated {
    protected final MFXValidator validator;
    private final StringProperty labelText = new SimpleStringProperty(this, "labelText");
    private final StringProperty text = new SimpleStringProperty(this, "text");
    private final ValidatableTextArea textArea = new ValidatableTextArea();

    public LabeledTextArea() {
        super();
        this.validator = new MFXValidator();
        this.getStyleClass().add("labeled-text-area");
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

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public StringProperty textProperty() {
        return text;
    }

    public ValidatableTextArea getTextArea() {
        return textArea;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LabeledTextAreaSkin(this);
    }

    @Override
    public MFXValidator getValidator() {
        return this.validator;
    }
}