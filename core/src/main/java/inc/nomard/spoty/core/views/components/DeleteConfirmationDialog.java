package inc.nomard.spoty.core.views.components;

import inc.nomard.spoty.core.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.mfxcomponents.controls.buttons.*;
import io.github.palexdev.mfxcomponents.theming.enums.*;
import io.github.palexdev.mfxcore.controls.*;
import io.github.palexdev.mfxresources.fonts.*;
import javafx.geometry.*;
import javafx.scene.layout.*;

public class DeleteConfirmationDialog extends MFXGenericDialog {
    public DeleteConfirmationDialog(SpotyGotFunctional.EventConsumer eventConsumer) {
        this.setPrefSize(500.0, 200.0);
        this.setPadding(new Insets(10.0));
        this.setTop(buildTop());
        this.setCenter(buildCenter());
        this.setBottom(buildBottom(eventConsumer));
    }

    private VBox buildCenter() {
        var titleLabel = new Label();
        titleLabel.getStyleClass().add("card-title");
        titleLabel.setText("Are you sure?");
        var subTitleLabel = new Label();
        titleLabel.getStyleClass().add("card-sub-title");
        subTitleLabel.setText("You want to delete this test suite \"Hello World\"");
        var vbox = new VBox(titleLabel, subTitleLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20.0);
        BorderPane.setAlignment(vbox, Pos.CENTER);
        return vbox;
    }

    private HBox buildBottom(SpotyGotFunctional.EventConsumer eventConsumer) {
        var cancelBtn = new MFXButton();
        cancelBtn.setVariants(ButtonVariants.FILLED);
        cancelBtn.setText("No, Cancel");
        cancelBtn.getStyleClass().add("card-cancel-btn");
        cancelBtn.setOnAction(GlobalActions::closeDialog);
        var deleteBtn = new MFXButton();
        deleteBtn.setVariants(ButtonVariants.OUTLINED);
        deleteBtn.setText("Yes, Delete");
        deleteBtn.getStyleClass().add("card-delete-btn");
        deleteBtn.setOnAction(event -> {
            eventConsumer.run(event);
            GlobalActions.closeDialog(event);
        });
        var hbox = new HBox(cancelBtn, deleteBtn);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(50.0);
        BorderPane.setAlignment(hbox, Pos.CENTER);
        return hbox;
    }

    private MFXFontIcon buildTop() {
        var trashCanIcon = new MFXFontIcon();
        trashCanIcon.getStyleClass().add("card-top-icon");
        trashCanIcon.setIconsProvider(IconsProviders.FONTAWESOME_REGULAR);
        trashCanIcon.setDescription("far-trash-can");
        BorderPane.setAlignment(trashCanIcon, Pos.CENTER);
        return trashCanIcon;
    }
}
