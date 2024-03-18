module spoty.core {
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
    requires spoty.network_bridge;
    requires spoty.utils;

    opens inc.nomard.spoty.core.views;
    opens inc.nomard.spoty.core.views.login;
    opens inc.nomard.spoty.core.views.components;
    opens inc.nomard.spoty.core.views.dashboard;
    opens inc.nomard.spoty.core.views.purchases;
    opens inc.nomard.spoty.core.views.sales;
    opens inc.nomard.spoty.core.views.requisition;
    opens inc.nomard.spoty.core.views.stock_in;
    opens inc.nomard.spoty.core.views.transfer;
    opens inc.nomard.spoty.core.views.product.category;
    opens inc.nomard.spoty.core.views.product.brand;
    opens inc.nomard.spoty.core.views.product.unit_of_measure;
    opens inc.nomard.spoty.core.views.product.products;
    opens inc.nomard.spoty.core.views.product.adjustment;
    opens inc.nomard.spoty.core.views.report;
    opens inc.nomard.spoty.core.views.human_resource.hrm;
    opens inc.nomard.spoty.core.views.human_resource.leave;
    opens inc.nomard.spoty.core.views.human_resource.pay_roll;
    opens inc.nomard.spoty.core.views.human_resource.pay_roll.pay_slip;
    opens inc.nomard.spoty.core.views.bank;
    opens inc.nomard.spoty.core.views.service;
    opens inc.nomard.spoty.core.views.quotation;
    opens inc.nomard.spoty.core.views.tax;
    opens inc.nomard.spoty.core.views.returns.sales;
    opens inc.nomard.spoty.core.views.returns.purchases;
    opens inc.nomard.spoty.core.views.expenses.expense;
    opens inc.nomard.spoty.core.views.expenses.category;
    opens inc.nomard.spoty.core.views.customers;
    opens inc.nomard.spoty.core.views.suppliers;
    opens inc.nomard.spoty.core.views.settings.system_settings;
    opens inc.nomard.spoty.core.views.settings.system_settings.app_settings;
    opens inc.nomard.spoty.core.views.settings.data_synchronizer;
    opens inc.nomard.spoty.core.views.settings.role_permission;
    opens inc.nomard.spoty.core.views.forms;
    opens inc.nomard.spoty.core.views.splash;
    opens inc.nomard.spoty.core.components.navigation;
    opens inc.nomard.spoty.core.components.notification;
    opens inc.nomard.spoty.core.components.animations;
    opens inc.nomard.spoty.core;
    opens inc.nomard.spoty.core.components.notification.enums;
    opens inc.nomard.spoty.core.components.title;
    opens inc.nomard.spoty.core.views.sales.pos;
    opens inc.nomard.spoty.core.views.printable.general;
    opens inc.nomard.spoty.core.views.previews;

    exports inc.nomard.spoty.core;
}
