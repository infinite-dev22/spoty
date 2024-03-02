module spoty.utils {
    requires MaterialFX;
    requires VirtualizedFX;
    requires fr.brouillard.oss.cssfx;
    requires mfx.components;
    requires mfx.resources;
    requires java.logging;
    requires java.naming;
    requires org.kordamp.ikonli.javafx;
    requires org.jetbrains.annotations;
    requires java.prefs;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.fontawesome5;
    requires lombok;
    requires java.net.http;
    requires com.google.gson;
    requires atlantafx.base;

    opens inc.nomard.spoty.utils;

    exports inc.nomard.spoty.utils;
    exports inc.nomard.spoty.utils.navigation;
}