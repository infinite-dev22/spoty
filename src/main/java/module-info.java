module org.infinite.spotmarkpos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.kordamp.ikonli.javafx;
    requires mfx.resources;
    requires MaterialFX;
    requires fr.brouillard.oss.cssfx;

    opens org.infinite.spotmarkpos to javafx.fxml;
    opens org.infinite.spotmarkpos.controller to javafx.fxml;
    exports org.infinite.spotmarkpos;
}