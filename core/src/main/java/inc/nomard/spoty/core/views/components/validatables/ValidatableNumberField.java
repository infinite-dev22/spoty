package inc.nomard.spoty.core.views.components.validatables;

import inc.nomard.spoty.core.values.strings.Values;

import java.util.Objects;

public class ValidatableNumberField extends ValidatableTextField {
    private static int MAX_CHARACTERS = 15;

    public ValidatableNumberField() {
        super();
        this.setMaxHeight(Values.FIELD_HEIGHT);
        this.setMinHeight(Values.FIELD_HEIGHT);
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (validate(text) && withinLimit(start, end, text)) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (validate(text) && withinLimit(getSelection().getStart(), getSelection().getEnd(), text)) {
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

    private boolean validate(String text) {
        return text.matches("[0-9]*\\.?[0-9]*");
    }

    public int getValue() {
        if (getText().isEmpty()) {
            return 0;
        }
        return Integer.parseInt(getText());
    }

    public void setValue(int value) {
        setText(String.valueOf(value));
    }

    public void setMaxLength(int value) {
        MAX_CHARACTERS = value;
    }
}
