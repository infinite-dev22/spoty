module spoty.core {
    requires MaterialFX;
    requires VirtualizedFX;
    requires fr.brouillard.oss.cssfx;
    requires mfx.components;
    requires mfx.resources;
    requires java.logging;
    requires org.kordamp.ikonli.javafx;
    requires java.prefs;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.fontawesome5;
    requires lombok;
    requires java.net.http;
    requires com.google.gson;
    requires atlantafx.base;
    requires eu.hansolo.fx.charts;
    requires spoty.network_bridge;
    requires spoty.utils;
    requires com.calendarfx.view;
    requires com.dlsc.gemsfx;

    opens inc.nomard.spoty.core.views.pages;
    opens inc.nomard.spoty.core.views.components;
    opens inc.nomard.spoty.core.views.dashboard;
    opens inc.nomard.spoty.core.views.settings.app_settings;
    opens inc.nomard.spoty.core.views.settings;
    opens inc.nomard.spoty.core.views.forms;
    opens inc.nomard.spoty.core.views.splash;
    opens inc.nomard.spoty.core.views.layout.navigation;
    opens inc.nomard.spoty.core.views.layout.message;
    opens inc.nomard.spoty.core.components.animations;
    opens inc.nomard.spoty.core.components;
    opens inc.nomard.spoty.core;
    opens inc.nomard.spoty.core.views.layout.message.enums;
    opens inc.nomard.spoty.core.components.title;
    opens inc.nomard.spoty.core.views.pos;
    opens inc.nomard.spoty.core.views.previews;

    exports inc.nomard.spoty.core;
    opens inc.nomard.spoty.core.views.util;
    opens inc.nomard.spoty.core.views.layout;
    opens inc.nomard.spoty.core.views.layout.navigation.event;
}
