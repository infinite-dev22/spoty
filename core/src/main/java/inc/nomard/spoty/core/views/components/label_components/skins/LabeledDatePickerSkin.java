package inc.nomard.spoty.core.views.components.label_components.skins;

import inc.nomard.spoty.core.views.components.label_components.controls.*;
import javafx.scene.control.*;

public class LabeledDatePickerSkin extends SkinBase<LabeledDatePicker> {

    private final Label label;
    private final DatePicker datePicker;

    public LabeledDatePickerSkin(LabeledDatePicker control) {
        super(control);

        label = new Label();
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
