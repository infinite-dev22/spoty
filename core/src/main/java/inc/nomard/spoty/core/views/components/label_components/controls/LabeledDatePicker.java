package inc.nomard.spoty.core.views.components.label_components.controls;

import inc.nomard.spoty.core.views.components.label_components.skins.*;
import io.github.palexdev.materialfx.validation.*;
import java.time.*;
import javafx.beans.property.*;
import javafx.scene.control.*;

public class LabeledDatePicker extends Control implements Validated {
    protected final MFXValidator validator;
    private final StringProperty labelText = new SimpleStringProperty(this, "labelText");
    private final ObjectProperty<LocalDate> value = new SimpleObjectProperty<>(this, "value");
    private final DatePicker datePicker = new DatePicker();

    public LabeledDatePicker() {
        super();
        this.getStyleClass().add("labeled-date-picker");
        this.validator = new MFXValidator();
    }

    public LocalDate getValue() {
        return value.get();
    }

    public void setValue(LocalDate value) {
        this.value.set(value);
    }

    public ObjectProperty<LocalDate> valueProperty() {
        return value;
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

    public DatePicker getDatePicker() {
        return datePicker;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LabeledDatePickerSkin(this);
    }

    @Override
    public MFXValidator getValidator() {
        return this.validator;
    }
}