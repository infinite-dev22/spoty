package inc.nomard.spoty.core.views.components.label_components.skins;

import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.views.components.label_components.controls.LabeledComboBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SkinBase;
import javafx.scene.text.Text;

public class LabeledComboBoxSkin<T> extends SkinBase<LabeledComboBox<T>> {

    private final Text label;
    private final ComboBox<T> comboBox;

    public LabeledComboBoxSkin(LabeledComboBox<T> control) {
        super(control);

        label = new Text();
        label.getStyleClass().addAll(Styles.TEXT);
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
