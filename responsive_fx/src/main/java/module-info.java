module inc.nomard.spoty.responsive_fx {
    requires javafx.controls;
    requires javafx.fxml;

    opens inc.nomard.spoty.responsive_fx.layouts to javafx.fxml;
    opens inc.nomard.spoty.responsive_fx.math to javafx.fxml;

    exports inc.nomard.spoty.responsive_fx.layouts;
    exports inc.nomard.spoty.responsive_fx.math;
}