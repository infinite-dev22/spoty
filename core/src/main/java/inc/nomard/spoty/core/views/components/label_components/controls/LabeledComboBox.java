package inc.nomard.spoty.core.views.components.label_components.controls;

import inc.nomard.spoty.core.views.components.label_components.skins.*;
import io.github.palexdev.materialfx.validation.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.util.*;

public class LabeledComboBox<T> extends Control implements Validated {
    protected final MFXValidator validator;

    private final StringProperty labelText = new SimpleStringProperty(this, "labelText");
    private final ObjectProperty<T> value = new SimpleObjectProperty<>(this, "value");
    private final ObjectProperty<StringConverter<T>> converter = new SimpleObjectProperty<>(this, "converter");
    private final ObjectProperty<ObservableList<T>> items = new SimpleObjectProperty<>(this, "items");
    private final ComboBox<T> comboBox = new ComboBox<>();

    public LabeledComboBox() {
        super();
        this.getStyleClass().add("labeled-combo-box");
        this.validator = new MFXValidator();
    }

    public String getLabelText() {
        return labelText.get();
    }

    public T getValue() {
        return value.get();
    }

    public void setValue(T value) {
        this.value.set(value);
    }

    public void setLabel(String labelText) {
        this.labelText.set(labelText);
    }

    public StringProperty labelTextProperty() {
        return labelText;
    }

    public ObjectProperty<T> valueProperty() {
        return value;
    }

    public ObservableList<T> getItems() {
        return comboBox.getItems();
    }

    public void setItems(ObservableList<T> items) {
        comboBox.setItems(items);
    }

    public ObjectProperty<StringConverter<T>> converterProperty() {
        return converter;
    }

    public ObjectProperty<ObservableList<T>> itemsProperty() {
        return items;
    }

    public void setConverter(StringConverter<T> converter) {
        this.converter.set(converter);
    }

    public ComboBox<T> getComboBox() {
        return comboBox;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LabeledComboBoxSkin<>(this);
    }

    @Override
    public MFXValidator getValidator() {
        return this.validator;
    }
}