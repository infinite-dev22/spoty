package inc.nomard.spoty.core.views.components;

import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.GlobalActions;
import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import inc.nomard.spoty.utils.functional_paradigm.SpotyGotFunctional;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InformativeDialog extends MFXStageDialog {
    public InformativeDialog(SpotyGotFunctional.ParameterlessConsumer parameterlessConsumer, Stage stage, Pane ownerNode) {
        this.setContent(buildDialogContent(parameterlessConsumer));
        this.initOwner(stage);
        this.initModality(Modality.WINDOW_MODAL);
        this.setOwnerNode(ownerNode);
        this.setScrimPriority(ScrimPriority.WINDOW);
        this.setScrimOwner(true);
        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(this.getScene());
        this.showAndWait();
    }

    private MFXGenericDialog buildDialogContent(SpotyGotFunctional.ParameterlessConsumer parameterlessConsumer) {
        var dialogContent = new MFXGenericDialog();
        dialogContent.setPrefSize(500.0, 250.0);
        dialogContent.setPadding(new Insets(10.0));
        dialogContent.setTop(buildTop());
        dialogContent.setCenter(buildCenter());
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

    private VBox buildCenter() {
        var titleLabel = new Label();
        titleLabel.getStyleClass().add("dialog-title");
        titleLabel.setText("Confirm exit!");
        var subTitleLabel = new Label();
        subTitleLabel.getStyleClass().add("dialog-sub-title");
        subTitleLabel.setText("Are you sure you want to close and exit this app?");
        var vbox = new VBox(titleLabel, subTitleLabel);
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
        cancelBtn.getStyleClass().add("primary-color");
        cancelBtn.setOnAction(GlobalActions::closeDialog);
        var deleteBtn = new Button();
        deleteBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
        deleteBtn.setText("Yes, Proceed");
        deleteBtn.getStyleClass().add("secondary-color");
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
        var informationIcon = new MFXFontIcon();
        informationIcon.setDescription("fas-circle-info");
        informationIcon.setSize(45);
        informationIcon.setColor(Color.LIGHTBLUE);
        StackPane.setAlignment(informationIcon, Pos.CENTER);
        var stackPane = new StackPane(circle, informationIcon);
        BorderPane.setAlignment(stackPane, Pos.CENTER);
        return stackPane;
    }
}
