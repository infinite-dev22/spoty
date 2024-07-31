package inc.nomard.spoty.core.views.components.validatables;

import atlantafx.base.controls.*;
import io.github.palexdev.materialfx.validation.*;

public class ValidatableTextField extends CustomTextField implements Validated {
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
