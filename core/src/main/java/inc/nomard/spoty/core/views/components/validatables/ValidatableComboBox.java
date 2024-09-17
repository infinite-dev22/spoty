package inc.nomard.spoty.core.views.components.validatables;

import io.github.palexdev.materialfx.validation.MFXValidator;
import io.github.palexdev.materialfx.validation.Validated;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

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
