package inc.nomard.spoty.core.views.components.validatables;

import io.github.palexdev.materialfx.validation.*;
import javafx.collections.*;
import javafx.scene.control.*;

public class ValidatableComboBox<T> extends ComboBox<T> implements Validated {
    protected final MFXValidator validator;

    public ValidatableComboBox() {
        this.validator = new MFXValidator();
    }

    public ValidatableComboBox(ObservableList<T> observableList) {
        super(observableList);
        this.validator = new MFXValidator();
    }

    @Override
    public MFXValidator getValidator() {
        return this.validator;
    }
}
