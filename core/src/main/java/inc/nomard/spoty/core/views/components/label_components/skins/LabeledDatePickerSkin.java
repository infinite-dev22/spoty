package inc.nomard.spoty.core.views.components.label_components.skins;

import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.views.components.label_components.controls.LabeledDatePicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SkinBase;
import javafx.scene.text.Text;

public class LabeledDatePickerSkin extends SkinBase<LabeledDatePicker> {

    private final Text label;
    private final DatePicker datePicker;

    public LabeledDatePickerSkin(LabeledDatePicker control) {
        super(control);

        label = new Text();
        label.getStyleClass().addAll(Styles.TEXT);
        datePicker = control.getDatePicker();

        getChildren().addAll(label, datePicker);

        // Bind the control's label text property to the label's text property
        label.textProperty().bind(control.labelTextProperty());
        datePicker.valueProperty().bindBidirectional(control.valueProperty());
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        double labelHeight = label.prefHeight(w);
        double datePickerHeight = datePicker.prefHeight(w);

        label.resizeRelocate(x, y, w, labelHeight);
        datePicker.resizeRelocate(x, y + labelHeight + 5, w, datePickerHeight);
    }
}
