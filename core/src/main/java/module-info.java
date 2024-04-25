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
//    requires froxty;
    requires com.dlsc.gemsfx;

    opens inc.nomard.spoty.core.views;
    opens inc.nomard.spoty.core.views.login;
    opens inc.nomard.spoty.core.views.components;
    opens inc.nomard.spoty.core.views.dashboard;
    opens inc.nomard.spoty.core.views.purchases;
    opens inc.nomard.spoty.core.views.sales;
    opens inc.nomard.spoty.core.views.requisition;
    opens inc.nomard.spoty.core.views.stock_in;
    opens inc.nomard.spoty.core.views.transfer;
    opens inc.nomard.spoty.core.views.inventory.category;
    opens inc.nomard.spoty.core.views.inventory.brand;
    opens inc.nomard.spoty.core.views.inventory.unit_of_measure;
    opens inc.nomard.spoty.core.views.inventory.products;
    opens inc.nomard.spoty.core.views.inventory.adjustment;
    opens inc.nomard.spoty.core.views.report;
    opens inc.nomard.spoty.core.views.human_resource.hrm;
    opens inc.nomard.spoty.core.views.human_resource.leave;
    opens inc.nomard.spoty.core.views.human_resource.pay_roll;
    opens inc.nomard.spoty.core.views.human_resource.pay_roll.pay_slip;
    opens inc.nomard.spoty.core.views.bank;
    opens inc.nomard.spoty.core.views.quotation;
    opens inc.nomard.spoty.core.views.tax;
    opens inc.nomard.spoty.core.views.expenses.expense;
    opens inc.nomard.spoty.core.views.expenses.category;
    opens inc.nomard.spoty.core.views.customers;
    opens inc.nomard.spoty.core.views.suppliers;
    opens inc.nomard.spoty.core.views.settings.app_settings;
    opens inc.nomard.spoty.core.views.settings;
    opens inc.nomard.spoty.core.views.forms;
    opens inc.nomard.spoty.core.views.splash;
    opens inc.nomard.spoty.core.components.navigation;
    opens inc.nomard.spoty.core.components.message;
    opens inc.nomard.spoty.core.components.animations;
    opens inc.nomard.spoty.core.components;
    opens inc.nomard.spoty.core;
    opens inc.nomard.spoty.core.components.message.enums;
    opens inc.nomard.spoty.core.components.title;
    opens inc.nomard.spoty.core.views.sales.pos;
    opens inc.nomard.spoty.core.views.printable.general;
    opens inc.nomard.spoty.core.views.previews;
    opens inc.nomard.spoty.core.views.previews.purchases;
    opens inc.nomard.spoty.core.views.previews.sales;
    opens inc.nomard.spoty.core.views.previews.people;
    opens inc.nomard.spoty.core.views.previews.hrm.pay_roll;

    exports inc.nomard.spoty.core;
}
