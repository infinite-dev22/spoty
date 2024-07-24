package inc.nomard.spoty.core.views.components.validatables;

import io.github.palexdev.materialfx.validation.*;
import java.time.*;
import javafx.scene.control.*;

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
