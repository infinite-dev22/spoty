module org.infinite.spoty {
    requires VirtualizedFX;
    requires MaterialFX;

    requires jdk.localedata;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;

    requires mfx.resources;

    requires fr.brouillard.oss.cssfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.scenicview.scenicview;
    requires lombok;
    requires org.slf4j;
    requires mfx.components;

    opens org.infinite.spoty;
    opens org.infinite.spoty.controller;
    opens org.infinite.spoty.controller.components;
    opens org.infinite.spoty.controller.dashboard;
    opens org.infinite.spoty.controller.purchases;
    opens org.infinite.spoty.controller.sales;
    opens org.infinite.spoty.controller.requisition;
    opens org.infinite.spoty.controller.stock_in;
    opens org.infinite.spoty.controller.transfer;

    opens org.infinite.spoty.controller.inventory;
    opens org.infinite.spoty.controller.inventory.category;
    opens org.infinite.spoty.controller.inventory.brand;
    opens org.infinite.spoty.controller.inventory.unit_of_measure;
    opens org.infinite.spoty.controller.inventory.products;
    opens org.infinite.spoty.controller.inventory.adjustment;
    opens org.infinite.spoty.controller.inventory.quotation;

    opens org.infinite.spoty.controller.expenses;
    opens org.infinite.spoty.controller.expenses.expense;
    opens org.infinite.spoty.controller.expenses.category;

    opens org.infinite.spoty.controller.people;
    opens org.infinite.spoty.controller.people.customers;
    opens org.infinite.spoty.controller.people.suppliers;
    opens org.infinite.spoty.controller.people.users;

    opens org.infinite.spoty.controller.settings;
    opens org.infinite.spoty.controller.settings.system;
    opens org.infinite.spoty.controller.settings.pos;
    opens org.infinite.spoty.controller.settings.roles;
    opens org.infinite.spoty.controller.settings.branches;
    opens org.infinite.spoty.controller.settings.currency;
    opens org.infinite.spoty.controller.settings.export;

    opens org.infinite.spoty.controller.returns;
    opens org.infinite.spoty.controller.returns.sales;
    opens org.infinite.spoty.controller.returns.purchases;

    opens org.infinite.spoty.forms;

    opens org.infinite.spoty.controller.splash;
}