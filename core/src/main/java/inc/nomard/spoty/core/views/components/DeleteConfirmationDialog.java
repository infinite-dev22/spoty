package inc.nomard.spoty.core.views.components;

import atlantafx.base.theme.*;
import inc.nomard.spoty.core.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.mfxresources.fonts.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;

public class DeleteConfirmationDialog extends MFXStageDialog {

    public DeleteConfirmationDialog(SpotyGotFunctional.ParameterlessConsumer parameterlessConsumer, String itemName, Pane ownerNode) {
        this.setContent(buildDialogContent(parameterlessConsumer, itemName));
        this.initOwner(AppManager.getPrimaryStage());
        this.initModality(Modality.WINDOW_MODAL);
        this.setOwnerNode(ownerNode);
        this.setScrimPriority(ScrimPriority.WINDOW);
        this.setScrimOwner(true);
        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(this.getScene());
        this.showAndWait();
    }

    private MFXGenericDialog buildDialogContent(SpotyGotFunctional.ParameterlessConsumer parameterlessConsumer, String itemName) {
        var dialogContent = new MFXGenericDialog();
        dialogContent.setPrefSize(500.0, 200.0);
        dialogContent.setPadding(new Insets(10.0));
        dialogContent.setTop(buildTop());
        dialogContent.setCenter(buildCenter(itemName));
        dialogContent.setBottom(buildBottom(parameterlessConsumer));
        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.getStylesheets().addAll(SpotyCoreResourceLoader.load("styles/base.css"),
                SpotyCoreResourceLoader.load("styles/Common.css"),
                SpotyCoreResourceLoader.load("styles/MFXColors.css"),
                SpotyCoreResourceLoader.load("styles/TextFields.css"),
                SpotyCoreResourceLoader.load("styles/toolitip.css"));
        return dialogContent;
    }

    private VBox buildCenter(String itemName) {
        var titleLabel = new Label();
        titleLabel.getStyleClass().add("dialog-title");
        titleLabel.setText("Are you sure?");
        var subTitleLabel = new Label();
        subTitleLabel.getStyleClass().add("dialog-sub-title");
        subTitleLabel.setText("Do you want to delete this item ");
        var subTitleItemLabel = new Label();
        subTitleItemLabel.getStyleClass().add("dialog-sub-title-item");
        subTitleItemLabel.setText("\"" + itemName + "\"");
        var vbox = new VBox(titleLabel, new HBox(subTitleLabel, subTitleItemLabel));
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20.0);
        vbox.setPadding(new Insets(20.0));
        BorderPane.setAlignment(vbox, Pos.CENTER);
        return vbox;
    }

    private HBox buildBottom(SpotyGotFunctional.ParameterlessConsumer parameterlessConsumer) {
        var cancelBtn = new Button();
        cancelBtn.setDefaultButton(true);
        cancelBtn.setText("No, Cancel");
        cancelBtn.getStyleClass().add("dialog-cancel-btn");
        cancelBtn.setOnAction(GlobalActions::closeDialog);
        var deleteBtn = new Button();
        deleteBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
        deleteBtn.setText("Yes, Delete");
        deleteBtn.getStyleClass().add("dialog-delete-btn");
        deleteBtn.setOnAction(event -> {
            parameterlessConsumer.run();
            GlobalActions.closeDialog(event);
        });
        var hbox = new HBox(cancelBtn, deleteBtn);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(50.0);
        BorderPane.setAlignment(hbox, Pos.CENTER);
        return hbox;
    }

    private StackPane buildTop() {
        var circle = new Circle();
        circle.getStyleClass().add("dialog-top-icon-background");
        StackPane.setAlignment(circle, Pos.CENTER);
        var trashCanIcon = new MFXFontIcon();
        trashCanIcon.getStyleClass().add("dialog-top-icon");
        trashCanIcon.setIconsProvider(IconsProviders.FONTAWESOME_REGULAR);
        trashCanIcon.setDescription("far-trash-can");
        trashCanIcon.setSize(45);
        trashCanIcon.setColor(Color.RED);
        StackPane.setAlignment(trashCanIcon, Pos.CENTER);
        var stackPane = new StackPane(circle, trashCanIcon);
        BorderPane.setAlignment(stackPane, Pos.CENTER);
        return stackPane;
    }
}
