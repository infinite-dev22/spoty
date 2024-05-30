module spoty.network_bridge {
    requires lombok;
    requires java.net.http;
    requires com.google.gson;
    requires spoty.utils;
    requires javafx.graphics;
    requires java.logging;


    opens inc.nomard.spoty.network_bridge.auth;
    opens inc.nomard.spoty.network_bridge.dtos;
    opens inc.nomard.spoty.network_bridge.dtos.adjustments;
    opens inc.nomard.spoty.network_bridge.dtos.hrm.employee;
    opens inc.nomard.spoty.network_bridge.dtos.hrm.leave;
    opens inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll;
    opens inc.nomard.spoty.network_bridge.dtos.purchases;
    opens inc.nomard.spoty.network_bridge.dtos.quotations;
    opens inc.nomard.spoty.network_bridge.dtos.requisitions;
    opens inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns;
    opens inc.nomard.spoty.network_bridge.dtos.returns.sale_returns;
    opens inc.nomard.spoty.network_bridge.dtos.sales;
    opens inc.nomard.spoty.network_bridge.dtos.stock_ins;
    opens inc.nomard.spoty.network_bridge.dtos.transfers;
    opens inc.nomard.spoty.network_bridge.models;
    opens inc.nomard.spoty.network_bridge.repositories.implementations;
    opens inc.nomard.spoty.network_bridge.repositories.interfaces;

    exports inc.nomard.spoty.network_bridge.auth;
    exports inc.nomard.spoty.network_bridge.end_points;
    exports inc.nomard.spoty.network_bridge.dtos;
    exports inc.nomard.spoty.network_bridge.dtos.adjustments;
    exports inc.nomard.spoty.network_bridge.dtos.hrm.employee;
    exports inc.nomard.spoty.network_bridge.dtos.hrm.leave;
    exports inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll;
    exports inc.nomard.spoty.network_bridge.dtos.purchases;
    exports inc.nomard.spoty.network_bridge.dtos.quotations;
    exports inc.nomard.spoty.network_bridge.dtos.requisitions;
    exports inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns;
    exports inc.nomard.spoty.network_bridge.dtos.returns.sale_returns;
    exports inc.nomard.spoty.network_bridge.dtos.sales;
    exports inc.nomard.spoty.network_bridge.dtos.stock_ins;
    exports inc.nomard.spoty.network_bridge.dtos.transfers;
    exports inc.nomard.spoty.network_bridge.models;
    exports inc.nomard.spoty.network_bridge.repositories.implementations;
    exports inc.nomard.spoty.network_bridge.repositories.interfaces;
    exports inc.nomard.spoty.network_bridge.dtos.payments;
}