package inc.nomard.spoty.core.views.components;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.utils.functional_paradigm.SpotyGotFunctional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;

public class InformativeDialog extends BorderPane {
    private final ModalPane modalPane;

    public InformativeDialog(ModalPane modalPane, SpotyGotFunctional.ParameterlessConsumer parameterlessConsumer) {
        this.modalPane = modalPane;
        this.setPrefSize(500.0, 250.0);
        this.setPadding(new Insets(10.0));
        this.setTop(buildTop());
        this.setCenter(buildCenter());
        this.setBottom(buildBottom(parameterlessConsumer));
        this.getStylesheets().addAll(SpotyCoreResourceLoader.load("styles/base.css"),
                SpotyCoreResourceLoader.load("styles/Common.css"),
                SpotyCoreResourceLoader.load("styles/MFXColors.css"),
                SpotyCoreResourceLoader.load("styles/TextFields.css"),
                SpotyCoreResourceLoader.load("styles/toolitip.css"));
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
        cancelBtn.setOnAction(event -> dispose());
        var deleteBtn = new Button();
        deleteBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
        deleteBtn.setText("Yes, Proceed");
        deleteBtn.getStyleClass().add("secondary-color");
        deleteBtn.setOnAction(event -> {
            parameterlessConsumer.run();
            dispose();
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
        var informationIcon = new FontIcon(FontAwesomeRegular.QUESTION_CIRCLE);
        informationIcon.setIconColor(Color.LIGHTBLUE);
        informationIcon.setIconSize(45);
        informationIcon.getStyleClass().add("dialog-top-icon");
        StackPane.setAlignment(informationIcon, Pos.CENTER);
        var stackPane = new StackPane(circle, informationIcon);
        BorderPane.setAlignment(stackPane, Pos.CENTER);
        return stackPane;
    }

    public void showDialog() {
        var dialog = new ModalContentHolder(450, 225);
        dialog.getChildren().add(this);
        dialog.setPadding(new Insets(5d));
        modalPane.setAlignment(Pos.CENTER);
        modalPane.show(dialog);
        modalPane.setPersistent(true);
    }

    private void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
    }
}
