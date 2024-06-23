package inc.nomard.spoty.core.views.components;

import inc.nomard.spoty.core.components.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.dashboard.*;
import inc.nomard.spoty.network_bridge.dtos.sales.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.navigation.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import java.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import lombok.extern.java.*;

@Log
public class Orders extends AnchorPane {
    public MFXTableView<SaleMaster> saleOrders;

    public Orders() {
        this.setMinHeight(351d);
        this.setPrefHeight(551d);
        this.setMaxHeight(751d);
        this.getStyleClass().add("card-flat");
        this.getChildren().addAll(buildTop(), buildBottom());
    }

    private HBox buildTop() {
        var hbox = new HBox();
        hbox.setSpacing(20d);
        hbox.setPadding(new Insets(5d));
        hbox.getChildren().addAll(
                buildTitle(),
                new Spacer(),
                buildViewAll()
        );
        UIUtils.anchor(hbox, 0d, 0d, null, 0d);
        return hbox;
    }

    private io.github.palexdev.mfxcore.controls.Label buildTitle() {
        var label = new io.github.palexdev.mfxcore.controls.Label(Labels.RECENT_ORDERS);
        label.getStyleClass().add("card-title");
        UIUtils.anchor(label, 0d, null, 0d, 0d);
        return label;
    }

    private ViewAll buildViewAll() {
        var viewAll = new ViewAll();
        return viewAll;
    }

    private MFXTableView<SaleMaster> buildBottom() {
        saleOrders = new MFXTableView<>();
        saleOrders.setFooterVisible(false);
        saleOrders.setBorder(null);
        setupTable();
        UIUtils.anchor(saleOrders, 32d, 0d, 0d, 0d);
        return saleOrders;
    }

    private void setupTable() {
        MFXTableColumn<SaleMaster> saleCustomer =
                new MFXTableColumn<>("Customer", false, Comparator.comparing(SaleMaster::getCustomerName));
        MFXTableColumn<SaleMaster> saleStatus =
                new MFXTableColumn<>("Order Status", false, Comparator.comparing(SaleMaster::getSaleStatus));
        MFXTableColumn<SaleMaster> salePaymentStatus =
                new MFXTableColumn<>(
                        "Pay Status", false, Comparator.comparing(SaleMaster::getPaymentStatus));
        MFXTableColumn<SaleMaster> saleGrandTotal =
                new MFXTableColumn<>("Total Amount", false, Comparator.comparing(SaleMaster::getTotal));
        MFXTableColumn<SaleMaster> saleAmountPaid =
                new MFXTableColumn<>("Paid Amount", false, Comparator.comparing(SaleMaster::getAmountPaid));

        saleCustomer.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getCustomerName));
        saleStatus.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getSaleStatus));
        salePaymentStatus.setRowCellFactory(
                sale -> new MFXTableRowCell<>(SaleMaster::getPaymentStatus));
        saleGrandTotal.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getTotal));
        saleAmountPaid.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getAmountPaid));

        saleCustomer.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));
        saleStatus.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));
        salePaymentStatus.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));
        saleGrandTotal.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));
        saleAmountPaid.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));

        saleOrders
                .getTableColumns()
                .addAll(
                        saleCustomer,
                        saleStatus,
                        salePaymentStatus,
                        saleGrandTotal,
                        saleAmountPaid);
        styleSaleMasterTable();

        if (DashboardViewModel.getRecentOrders().isEmpty()) {
            DashboardViewModel.getRecentOrders()
                    .addListener(
                            (ListChangeListener<SaleMaster>)
                                    c -> saleOrders.setItems(DashboardViewModel.getRecentOrders()));
        } else {
            saleOrders.itemsProperty().bindBidirectional(DashboardViewModel.recentOrdersProperty());
        }
    }

    private void styleSaleMasterTable() {
        saleOrders.setPrefSize(1200, 1000);
        saleOrders.features().enableBounceEffect();
        saleOrders.features().enableSmoothScrolling(0.5);

        saleOrders.setTableRowFactory(
                t -> {
                    MFXTableRow<SaleMaster> row = new MFXTableRow<>(saleOrders, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
//                                showContextMenu((MFXTableRow<SaleMaster>) event.getSource())
//                                        .show(
//                                                saleOrders.getScene().getWindow(),
//                                                event.getScreenX(),
//                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }
}
