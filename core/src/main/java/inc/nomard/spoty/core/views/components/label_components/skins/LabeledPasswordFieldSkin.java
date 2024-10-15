package inc.nomard.spoty.core.views.components.label_components.skins;

import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.views.components.label_components.controls.LabeledPasswordField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SkinBase;
import javafx.scene.text.Text;

public class LabeledPasswordFieldSkin extends SkinBase<LabeledPasswordField> {

    private final Text label;
    private final PasswordField textField;

    public LabeledPasswordFieldSkin(LabeledPasswordField control) {
        super(control);

        label = new Text();
        label.getStyleClass().addAll(Styles.TEXT);
        textField = new PasswordField();

        getChildren().addAll(label, textField);

        // Bind the control's label text property to the label's text property
        label.textProperty().bind(control.labelTextProperty());

        // Bind the control's text property to the textField's text property
        textField.textProperty().bindBidirectional(control.textProperty());
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        double labelHeight = label.prefHeight(w);
        double textFieldHeight = textField.prefHeight(w);

        label.resizeRelocate(x, y, w, labelHeight);
        textField.resizeRelocate(x, y + labelHeight + 5, w, textFieldHeight);
    }
}

