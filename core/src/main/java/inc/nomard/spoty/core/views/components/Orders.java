package inc.nomard.spoty.core.views.components;

import inc.nomard.spoty.core.values.strings.Labels;
import inc.nomard.spoty.core.viewModels.dashboard.DashboardViewModel;
import inc.nomard.spoty.network_bridge.dtos.sales.SaleMaster;
import inc.nomard.spoty.utils.UIUtils;
import inc.nomard.spoty.utils.navigation.Spacer;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import lombok.extern.java.Log;

import java.util.LinkedList;
import java.util.stream.Stream;

@Log
public class Orders extends AnchorPane {
    public TableView<SaleMaster> saleOrders;

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
        return new ViewAll();
    }

    private TableView<SaleMaster> buildBottom() {
        saleOrders = new TableView<>();
        saleOrders.setBorder(null);
        setupTable();
        UIUtils.anchor(saleOrders, 32d, 0d, 0d, 0d);
        return saleOrders;
    }

    private void setupTable() {
        TableColumn<SaleMaster, String> saleCustomer = new TableColumn<>("Customer");
        TableColumn<SaleMaster, String> saleStatus = new TableColumn<>("Order Status");
        TableColumn<SaleMaster, String> salePaymentStatus = new TableColumn<>("Pay Status");
        TableColumn<SaleMaster, String> saleGrandTotal = new TableColumn<>("Total Amount");
        TableColumn<SaleMaster, String> saleAmountPaid = new TableColumn<>("Paid Amount");

        saleCustomer.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));
        saleStatus.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));
        salePaymentStatus.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));
        saleGrandTotal.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));
        saleAmountPaid.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));

        var columnList = new LinkedList<>(Stream.of(saleCustomer,
                saleStatus,
                salePaymentStatus,
                saleGrandTotal,
                saleAmountPaid).toList());
        saleOrders.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        saleOrders.getColumns().addAll(columnList);
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

        saleOrders.setRowFactory(
                t -> {
                    TableRow<SaleMaster> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
//                                showContextMenu((TableRow<SaleMaster>) event.getSource())
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
