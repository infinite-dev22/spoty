package inc.nomard.spoty.core.views.util;

import inc.nomard.spoty.utils.functional_paradigm.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxcomponents.theming.enums.*;
import java.util.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import lombok.*;

public interface Page {
    int MAX_WIDTH = 1053;
    int HGAP_20 = 20;
    int HGAP_30 = 30;
    int VGAP_10 = 10;
    int VGAP_20 = 20;

    Parent getView();

    Node getSnapshotTarget();

    void reset();

    ///////////////////////////////////////////////////////////////////////////

    class PageHeader extends HBox {
        @Setter
        private SpotyGotFunctional.ParameterlessConsumer onTextEmpty, onSearchAction;
        private MFXTextField searchBar;
        private MFXProgressSpinner progress;
        private MFXButton createBtn;

        public PageHeader() {
            super();
            this.getStyleClass().add("card-flat");
            BorderPane.setAlignment(this, Pos.CENTER);
            this.setPadding(new Insets(5d));
            this.getChildren().addAll(buildLeftTop(), buildCenterTop(), buildRightTop());
            this.setSearchBarListener();
        }


        private HBox buildLeftTop() {
            progress = new MFXProgressSpinner();
            progress.setMinSize(30d, 30d);
            progress.setPrefSize(30d, 30d);
            progress.setMaxSize(30d, 30d);
            progress.setVisible(false);
            var hbox = new HBox(progress);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
            HBox.setHgrow(hbox, Priority.ALWAYS);
            return hbox;
        }

        private HBox buildCenterTop() {
            searchBar = new MFXTextField();
            searchBar.setFloatMode(FloatMode.DISABLED);
            searchBar.setMinWidth(300d);
            searchBar.setPrefWidth(500d);
            searchBar.setMaxWidth(700d);
            var hbox = new HBox(searchBar);
            hbox.setAlignment(Pos.CENTER);
            hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
            HBox.setHgrow(hbox, Priority.ALWAYS);
            return hbox;
        }

        private HBox buildRightTop() {
            createBtn = new MFXButton("Create");
            createBtn.getStyleClass().add("filled");
            var hbox = new HBox(createBtn);
            hbox.setAlignment(Pos.CENTER_RIGHT);
            hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
            HBox.setHgrow(hbox, Priority.ALWAYS);
            return hbox;
        }

        public void setSearchBarListener() {
            searchBar.textProperty().addListener((observableValue, ov, nv) -> {
                if (Objects.equals(ov, nv)) {
                    return;
                }
                if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                    if (Objects.nonNull(onTextEmpty)) {
                        onTextEmpty.run();
                    }
                }
                progress.setVisible(true);
                if (Objects.nonNull(onSearchAction)) {
                    onSearchAction.run();
                }
            });
        }

        public void setSearchPrompt(String prompt) {
            searchBar.setPromptText(prompt);
        }

        public void setCreateBtnAction(EventHandler<ActionEvent> action) {
            createBtn.setOnAction(action);
        }
    }
}