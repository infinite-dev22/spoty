package inc.nomard.spoty.core.views.components.label_components.skins;

import atlantafx.base.controls.CustomTextField;
import inc.nomard.spoty.core.views.components.label_components.controls.LabeledTextField;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;

public class LabeledTextFieldSkin extends SkinBase<LabeledTextField> {

    private final Label label;
    private final CustomTextField textField;

    public LabeledTextFieldSkin(LabeledTextField control) {
        super(control);

        label = new Label();
        textField = new CustomTextField();

        getChildren().addAll(label, textField);

        // Bind the control's label text property to the label's text property
        label.textProperty().bind(control.labelTextProperty());

        // Bind the control's text property to the textField's text property
        textField.textProperty().bindBidirectional(control.textProperty());
        textField.rightProperty().bindBidirectional(control.rightProperty());
        textField.leftProperty().bindBidirectional(control.leftProperty());
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        double labelHeight = label.prefHeight(w);
        double textFieldHeight = textField.prefHeight(w);

        label.resizeRelocate(x, y, w, labelHeight);
        textField.resizeRelocate(x, y + labelHeight + 5, w, textFieldHeight);
    }
}

