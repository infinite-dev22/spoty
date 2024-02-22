module spoty.accounting {
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
    requires spoty.reporting;
    requires spoty.utils;


    opens inc.normad.spoty.accounting.navigation;
    opens inc.normad.spoty.accounting.views;
    opens inc.normad.spoty.accounting;

    exports inc.normad.spoty.accounting.navigation;
    exports inc.normad.spoty.accounting.views;
}