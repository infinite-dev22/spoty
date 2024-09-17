package inc.nomard.spoty.core.views.layout;

import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;

public class SpotyDialog extends MFXStageDialog {
    public static MFXStageDialog createDialog(MFXGenericDialog content, Pane owner) {
        var dialog = MFXGenericDialogBuilder.build(content)
                .toStageDialogBuilder()
                .initOwner(AppManager.getPrimaryStage())
                .initModality(Modality.WINDOW_MODAL)
                .setOwnerNode(owner)
                .setScrimPriority(ScrimPriority.WINDOW)
                .setScrimOwner(true)
                .get();
        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
        return dialog;
    }
}
