package inc.nomard.spoty.core.views.components.validatables;

import inc.nomard.spoty.core.util.validation.Validated;
import inc.nomard.spoty.core.util.validation.Validator;
import javafx.scene.control.PasswordField;

import java.util.Objects;

public class ValidatablePasswordField extends PasswordField implements Validated {
    private static int MAX_CHARACTERS = 20;
    protected final Validator validator;

    public ValidatablePasswordField() {
        this.validator = new Validator();
    }

    @Override
    public Validator getValidator() {
        return this.validator;
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (withinLimit(start, end, text)) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (withinLimit(getSelection().getStart(), getSelection().getEnd(), text)) {
            super.replaceSelection(text);
        }
    }

    private boolean withinLimit(int start, int end, String text) {
        if (!Objects.isNull(getText())) {
            int newLength = getText().length() - (end - start) + text.length();
            return newLength <= MAX_CHARACTERS;
        }
        return true;
    }

    public void setMaxLength(int value) {
        MAX_CHARACTERS = value;
    }
}
