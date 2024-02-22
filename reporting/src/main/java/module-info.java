module spoty.reporting {
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
    requires spoty.utils;
    requires spoty.network_bridge;

    opens inc.normad.spoty.reporting.accounting;
    opens inc.normad.spoty.reporting.general;
    opens inc.normad.spoty.reporting.navigation;
    opens inc.normad.spoty.reporting;

    exports inc.normad.spoty.reporting.accounting;
    exports inc.normad.spoty.reporting.general;
    exports inc.normad.spoty.reporting.navigation;
}