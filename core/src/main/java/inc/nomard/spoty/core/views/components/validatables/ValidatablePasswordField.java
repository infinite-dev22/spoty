package inc.nomard.spoty.core.views.components.validatables;

import io.github.palexdev.materialfx.validation.*;
import javafx.scene.control.*;

public class ValidatablePasswordField extends PasswordField implements Validated {
    protected final MFXValidator validator;

    public ValidatablePasswordField() {
        this.validator = new MFXValidator();
    }

    @Override
    public MFXValidator getValidator() {
        return this.validator;
    }
}
