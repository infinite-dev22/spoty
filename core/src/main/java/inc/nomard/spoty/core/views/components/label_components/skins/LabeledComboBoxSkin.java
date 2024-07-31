package inc.nomard.spoty.core.views.components.label_components.skins;

import inc.nomard.spoty.core.views.components.label_components.controls.*;
import javafx.scene.control.*;

public class LabeledComboBoxSkin<T> extends SkinBase<LabeledComboBox<T>> {

    private final Label label;
    private final ComboBox<T> comboBox;

    public LabeledComboBoxSkin(LabeledComboBox<T> control) {
        super(control);

        label = new Label();
        comboBox = control.getComboBox();

        getChildren().addAll(label, comboBox);

        // Bind the control's label text property to the label's text property
        label.textProperty().bind(control.labelTextProperty());
        comboBox.valueProperty().bindBidirectional(control.valueProperty());
        comboBox.converterProperty().bind(control.converterProperty());
        comboBox.itemsProperty().bindBidirectional(control.itemsProperty());
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        double labelHeight = label.prefHeight(w);
        double comboBoxHeight = comboBox.prefHeight(w);

        label.resizeRelocate(x, y, w, labelHeight);
        comboBox.resizeRelocate(x, y + labelHeight + 5, w, comboBoxHeight);
    }
}
