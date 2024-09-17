package inc.nomard.spoty.core.views.components.validatables;

import io.github.palexdev.materialfx.validation.MFXValidator;
import io.github.palexdev.materialfx.validation.Validated;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class ValidatableDatePicker extends DatePicker implements Validated {
    protected final MFXValidator validator;


    public ValidatableDatePicker() {
        this.validator = new MFXValidator();
    }

    public ValidatableDatePicker(LocalDate localDate) {
        super(localDate);
        this.validator = new MFXValidator();
    }

    @Override
    public MFXValidator getValidator() {
        return this.validator;
    }
}
