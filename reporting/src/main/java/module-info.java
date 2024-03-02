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

    opens inc.nomard.spoty.reporting.accounting;
    opens inc.nomard.spoty.reporting.general;
    opens inc.nomard.spoty.reporting.navigation;
    opens inc.nomard.spoty.reporting;

    exports inc.nomard.spoty.reporting.accounting;
    exports inc.nomard.spoty.reporting.general;
    exports inc.nomard.spoty.reporting.navigation;
}