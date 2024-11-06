package inc.nomard.spoty.core.views.util;

import inc.nomard.spoty.core.views.components.SpotyProgressSpinner;
import inc.nomard.spoty.utils.functional_paradigm.SpotyGotFunctional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import lombok.Setter;

import java.util.Objects;

public interface Page {
    Parent getView();

    void reset();
    void onRendered();

    ///////////////////////////////////////////////////////////////////////////

    class PageHeader extends HBox {
        @Setter
        private SpotyGotFunctional.ParameterlessConsumer onTextEmpty, onSearchAction;
        private TextField searchBar;
        private SpotyProgressSpinner progress;
        private Button createBtn;

        public PageHeader() {
            super();
            this.getStyleClass().add("card-flat");
            BorderPane.setAlignment(this, Pos.CENTER);
            this.setPadding(new Insets(5d));
            this.getChildren().addAll(buildLeftTop(), buildCenterTop(), buildRightTop());
            this.setSearchBarListener();
        }


        private HBox buildLeftTop() {
            progress = new SpotyProgressSpinner();
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
            searchBar = new TextField();
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
            createBtn = new Button("Create");
            createBtn.setDefaultButton(true);
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