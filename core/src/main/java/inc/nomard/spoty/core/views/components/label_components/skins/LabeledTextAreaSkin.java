package inc.nomard.spoty.core.views.components.label_components.skins;

import inc.nomard.spoty.core.views.components.label_components.controls.*;
import javafx.scene.control.*;

public class LabeledTextAreaSkin extends SkinBase<LabeledTextArea> {

    private final Label label;
    private final TextArea textArea;

    public LabeledTextAreaSkin(LabeledTextArea control) {
        super(control);

        label = new Label();
        textArea = control.getTextArea();

        getChildren().addAll(label, textArea);

        // Bind the control's label text property to the label's text property
        label.textProperty().bind(control.labelTextProperty());
        textArea.textProperty().bindBidirectional(control.textProperty());
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        double labelHeight = label.prefHeight(w);
        double textAreaHeight = h - labelHeight - 5;

        label.resizeRelocate(x, y, w, labelHeight);
        textArea.resizeRelocate(x, y + labelHeight + 5, w, textAreaHeight);
    }
}
