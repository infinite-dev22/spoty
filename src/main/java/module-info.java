module org.infinite.spoty {
    requires MaterialFX;
    requires VirtualizedFX;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;

    requires fr.brouillard.oss.cssfx;

    requires mfx.components;
    requires mfx.resources;

    requires org.hibernate.orm.core;
    requires org.hibernate.commons.annotations;

    requires org.jboss.logging;

    requires net.bytebuddy;

    requires jakarta.persistence;
    requires jakarta.transaction;
    requires jakarta.xml.bind;
    requires com.fasterxml.classmate;

    requires java.sql;
    requires java.logging;
    requires java.naming;
    requires org.apache.derby.tools;
    requires org.apache.derby.commons;
    requires org.apache.derby.engine;

    requires org.apache.commons.compress;
    requires org.kordamp.ikonli.javafx;

    requires org.jetbrains.annotations;

    opens org.infinite.spoty.views;
    opens org.infinite.spoty.views.components;
    opens org.infinite.spoty.views.dashboard;
    opens org.infinite.spoty.views.purchases;
    opens org.infinite.spoty.views.sales;
    opens org.infinite.spoty.views.requisition;
    opens org.infinite.spoty.views.stock_in;
    opens org.infinite.spoty.views.transfer;

    opens org.infinite.spoty.views.inventory.category;
    opens org.infinite.spoty.views.inventory.brand;
    opens org.infinite.spoty.views.inventory.unit_of_measure;
    opens org.infinite.spoty.views.inventory.products;
    opens org.infinite.spoty.views.inventory.adjustment;
    opens org.infinite.spoty.views.inventory.quotation;

    opens org.infinite.spoty.views.expenses.expense;
    opens org.infinite.spoty.views.expenses.category;

    opens org.infinite.spoty.views.people.customers;
    opens org.infinite.spoty.views.people.suppliers;
    opens org.infinite.spoty.views.people.users;

    opens org.infinite.spoty.views.settings;
    opens org.infinite.spoty.views.settings.system;
    opens org.infinite.spoty.views.settings.pos;
    opens org.infinite.spoty.views.settings.roles;
    opens org.infinite.spoty.views.settings.branches;
    opens org.infinite.spoty.views.settings.currency;
    opens org.infinite.spoty.views.settings.export;

    opens org.infinite.spoty.views.returns.sales;
    opens org.infinite.spoty.views.returns.purchases;

    opens org.infinite.spoty.forms;

    opens org.infinite.spoty.views.splash;

    opens org.infinite.spoty.database.models;

    opens org.infinite.spoty;

    exports org.infinite.spoty;
}