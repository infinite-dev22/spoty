package inc.nomard.spoty.core.views.components.label_components.controls;

import inc.nomard.spoty.core.views.components.label_components.skins.*;
import io.github.palexdev.materialfx.validation.*;
import javafx.beans.property.*;
import javafx.scene.*;
import javafx.scene.control.*;

public class LabeledTextField extends Control implements Validated {
    protected final MFXValidator validator;
    private final StringProperty labelText = new SimpleStringProperty(this, "labelText");
    private final StringProperty text = new SimpleStringProperty(this, "text");
    private final ObjectProperty<Node> right = new SimpleObjectProperty<>(this, "right");
    private final ObjectProperty<Node> left = new SimpleObjectProperty<>(this, "left");

    public LabeledTextField() {
        super();
        this.validator = new MFXValidator();
        this.getStyleClass().add("labeled-text-field");
    }

    public String getLabelText() {
        return labelText.get();
    }

    public void setLabel(String labelText) {
        this.labelText.set(labelText);
    }

    public StringProperty labelTextProperty() {
        return labelText;
    }

    public ObjectProperty<Node> rightProperty() {
        return right;
    }

    public ObjectProperty<Node> leftProperty() {
        return left;
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public void setRight(Node node) {
        this.right.set(node);
    }

    public void setLeft(Node node) {
        this.left.set(node);
    }

    public StringProperty textProperty() {
        return text;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LabeledTextFieldSkin(this);
    }

    @Override
    public MFXValidator getValidator() {
        return this.validator;
    }
}


/*import atlantafx.base.controls.*;
import inc.nomard.spoty.core.views.components.label_components.skins.*;
import io.github.palexdev.materialfx.validation.*;
import javafx.beans.property.*;
import javafx.scene.control.*;
import lombok.*;

public class LabeledTextField extends CustomTextField implements Validated {
    protected final MFXValidator validator;
    private final StringProperty text;
    @Setter
    @Getter
    private String label;

    public LabeledTextField() {
        super();
        this.validator = new MFXValidator();
        this.text = new SimpleStringProperty();
    }

    public LabeledTextField(String label) {
        super();
        this.validator = new MFXValidator();
        this.text = new SimpleStringProperty(label);
    }

    public StringProperty customTextProperty() {
        return text;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LabeledTextFieldSkin(this);
    }

    @Override
    public MFXValidator getValidator() {
        return this.validator;
    }
}*/

    /*public class LabeledTextField extends VBox implements Validated {
        protected final MFXValidator validator;
        private final StringProperty labelProperty = new SimpleStringProperty();
        private final StringProperty promptTextProperty = new SimpleStringProperty();
        private final DoubleProperty minWidthProperty = new SimpleDoubleProperty();
        private final DoubleProperty prefWidthProperty = new SimpleDoubleProperty();
        private final DoubleProperty maxWidthProperty = new SimpleDoubleProperty();
        private CustomTextField textField;

        public LabeledTextField() {
            this.validator = new MFXValidator();
            init();
        }

        private void init() {
            textField = new CustomTextField();
            textField.promptTextProperty().bindBidirectional(promptTextProperty);
            textField.minWidthProperty().bindBidirectional(minWidthProperty);
            textField.prefWidthProperty().bindBidirectional(prefWidthProperty);
            textField.maxWidthProperty().bindBidirectional(maxWidthProperty);
            minWidthProperty.set(getMinWidth());
            prefWidthProperty.set(getPrefWidth());
            maxWidthProperty.set(getMaxWidth());
            var lbl = new Label();
            lbl.textProperty().bindBidirectional(labelProperty);
            lbl.setLabelFor(textField);

            this.setAlignment(Pos.TOP_LEFT);
            this.getChildren().addAll(lbl, textField);
        }

        public void setLabel(String label) {
            labelProperty.set(label);
        }

        public void setPromptText(String promptText) {
            promptTextProperty.set(promptText);
        }

        public void setRight(Node node) {
            textField.setRight(node);
        }

        public void setLeft(Node node) {
            textField.setLeft(node);
        }

        public StringProperty textProperty() {
            return textField.textProperty();
        }

        public String getText() {
            return textField.getText();
        }

        public void setText(String text) {
            textField.setText(text);
        }

        public ReadOnlyBooleanProperty delegateFocusedProperty() {
            return textField.focusedProperty();
        }

        @Override
        public MFXValidator getValidator() {
            return this.validator;
        }
    }*/
