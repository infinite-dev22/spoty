module spoty.utils {
    requires javafx.controls;
    requires java.logging;
    requires lombok;
    requires java.xml;
    requires com.google.gson;
    requires net.coobird.thumbnailator;
    requires jdk.compiler;
    requires org.slf4j;

    opens inc.nomard.spoty.utils;

    opens inc.nomard.spoty.utils.responsiveness.layouts;
    opens inc.nomard.spoty.utils.responsiveness.math;

    exports inc.nomard.spoty.utils.responsiveness.layouts;
    exports inc.nomard.spoty.utils.responsiveness.math;
    exports inc.nomard.spoty.utils;
    exports inc.nomard.spoty.utils.adapters;
    exports inc.nomard.spoty.utils.exceptions;
    exports inc.nomard.spoty.utils.flavouring;
    exports inc.nomard.spoty.utils.functional_paradigm;
    exports inc.nomard.spoty.utils.connectivity;
}