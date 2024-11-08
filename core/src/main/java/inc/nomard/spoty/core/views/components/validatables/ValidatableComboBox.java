package inc.nomard.spoty.core.views.components.validatables;

import inc.nomard.spoty.core.util.validation.Validated;
import inc.nomard.spoty.core.util.validation.Validator;
import inc.nomard.spoty.core.values.strings.Values;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class ValidatableComboBox<T> extends ComboBox<T> implements Validated {
    protected final Validator validator;

    public ValidatableComboBox() {
        this.validator = new Validator();
        this.setMaxHeight(Values.FIELD_HEIGHT);
        this.setMinHeight(Values.FIELD_HEIGHT);
    }

    public ValidatableComboBox(ObservableList<T> observableList) {
        super(observableList);
        this.validator = new Validator();
    }

    @Override
    public Validator getValidator() {
        return this.validator;
    }
}
