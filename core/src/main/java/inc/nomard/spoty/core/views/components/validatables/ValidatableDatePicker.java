package inc.nomard.spoty.core.views.components.validatables;

import inc.nomard.spoty.core.util.validation.Validated;
import inc.nomard.spoty.core.util.validation.Validator;
import inc.nomard.spoty.core.values.strings.Values;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class ValidatableDatePicker extends DatePicker implements Validated {
    protected final Validator validator;

    public ValidatableDatePicker() {
        this.validator = new Validator();
        this.setMaxHeight(45);
        this.setMaxHeight(Values.FIELD_HEIGHT);
        this.setMinHeight(Values.FIELD_HEIGHT);
    }

    public ValidatableDatePicker(LocalDate localDate) {
        super(localDate);
        this.validator = new Validator();
    }

    @Override
    public Validator getValidator() {
        return this.validator;
    }
}
