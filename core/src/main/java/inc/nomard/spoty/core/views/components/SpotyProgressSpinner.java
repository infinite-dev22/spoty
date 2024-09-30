package inc.nomard.spoty.core.views.components;

import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Skin;
import javafx.util.StringConverter;

/**
 * A ProgressIndicator that displays progress value as a ring that gradually
 * empties out as a task is completed.
 */
public class SpotyProgressSpinner extends MFXProgressSpinner {
    private final String STYLE_CLASS = "spoty-progress-spinner";
    public SpotyProgressSpinner() {
        super();

        getStyleClass().add(STYLE_CLASS);
    }
}
