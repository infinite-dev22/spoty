package inc.nomard.spoty.core.views.components.validatables;

import inc.nomard.spoty.core.util.validation.Validated;
import inc.nomard.spoty.core.util.validation.Validator;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import inc.nomard.spoty.core.values.strings.Values;

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
