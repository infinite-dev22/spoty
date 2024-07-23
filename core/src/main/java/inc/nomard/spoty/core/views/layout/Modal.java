package inc.nomard.spoty.core.views.layout;

import io.github.palexdev.mfxcore.controls.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import lombok.*;

public interface Modal {
    int HGAP_20 = 20;
    int HGAP_30 = 30;
    int VGAP_10 = 10;
    int VGAP_20 = 20;

    Parent getView();

    Node getSnapshotTarget();

    void reset();

    void dispose();

    ///////////////////////////////////////////////////////////////////////////

    @Setter
    class PageHeader extends HBox {
        private Label label;

        public PageHeader() {
            super();
            this.getStyleClass().add("card-flat");
            BorderPane.setAlignment(this, Pos.CENTER);
            this.setPadding(new Insets(5d));
            this.getChildren().addAll(buildLabel());
        }


        private HBox buildLabel() {
            label = new Label();
            label.getStyleClass().add("h2");
            label.setVisible(false);
            var hbox = new HBox(label);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
            HBox.setHgrow(hbox, Priority.ALWAYS);
            return hbox;
        }
    }
}
