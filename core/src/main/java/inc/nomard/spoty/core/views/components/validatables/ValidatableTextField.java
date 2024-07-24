package inc.nomard.spoty.core.views.components.validatables;

import io.github.palexdev.materialfx.validation.*;
import javafx.scene.control.*;

public class ValidatableTextField extends TextField implements Validated {
    protected final MFXValidator validator;

    public ValidatableTextField() {
        this.validator = new MFXValidator();
    }

    public ValidatableTextField(String text) {
        super(text);
        this.validator = new MFXValidator();
    }

    @Override
    public MFXValidator getValidator() {
        return this.validator;
    }
}
