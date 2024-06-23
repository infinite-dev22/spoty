package inc.nomard.spoty.core.views.dashboard;

import inc.nomard.spoty.core.viewModels.dashboard.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.wrappers.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.responsiveness.layouts.*;
import io.github.palexdev.materialfx.controls.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import lombok.extern.java.*;

@Log
public class Dashboard extends BorderPane {
    private MFXScrollPane scrollPane;
    private BootstrapPane kpisContainer;

    public Dashboard() {
        configureScrollPane();
        configureKPIContainer();
        loadDashboardComponents();
    }

    private void configureScrollPane() {
        kpisContainer = new BootstrapPane();
        kpisContainer.setHgap(20.0);
        kpisContainer.setVgap(20.0);
        scrollPane = new MFXScrollPane(kpisContainer);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-border-radius: 10; -fx-background-radius: 10;");
        UIUtils.anchor(scrollPane, 0d, 0d, 0d, 0d);
        var pane = new AnchorPane();
        pane.getChildren().add(scrollPane);
        this.setCenter(pane);
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });

        scrollPane.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double width = newWidth.doubleValue() - 10;
            kpisContainer.setMaxWidth(width);
            kpisContainer.setPrefWidth(width);
            kpisContainer.setMinWidth(width);
        });
    }

    private void configureKPIContainer() {
        kpisContainer.setAlignment(Pos.CENTER_LEFT);
        kpisContainer.setPadding(new Insets(10));
    }

    private void loadKPIComponentsAsync(BootstrapRow row) {
        ObservableList<DashboardKPIModelWrapper> kpisList = DashboardViewModel.getKPIs();
        buildKPIs(kpisList, row);
        kpisContainer.addRow(row);
        kpisList.addListener((ListChangeListener<? super DashboardKPIModelWrapper>) c -> System.out.println("KPI SIZE: " + DashboardViewModel.getKPIs().size()));
    }

    private void loadDashboardComponents() {
        var row1 = new BootstrapRow();
        var row2 = new BootstrapRow();
        var row3 = new BootstrapRow();
        loadKPIComponentsAsync(row1);
        buildFinancialProjections(row2, 12);
        buildTotalRevenue(row3, 6);
        buildTopProducts(row3, 6);
        buildOrders(row3, 6);
        buildStockAlerts(row3, 6);

        kpisContainer.addRow(row1);
        kpisContainer.addRow(row2);
        kpisContainer.addRow(row3);
    }

    private void buildStockAlerts(BootstrapRow row, int columnWidth) {
        var stockAlerts = new StockAlerts();
        var column = new BootstrapColumn(stockAlerts);
        column.setBreakpointColumnWidth(Breakpoint.LARGE, columnWidth);
        column.setBreakpointColumnWidth(Breakpoint.SMALL, columnWidth);
        column.setBreakpointColumnWidth(Breakpoint.XSMALL, 12);
        row.addColumn(column);
    }

    private void buildOrders(BootstrapRow row, int columnWidth) {
        var orders = new Orders();
        var column = new BootstrapColumn(orders);
        column.setBreakpointColumnWidth(Breakpoint.LARGE, columnWidth);
        column.setBreakpointColumnWidth(Breakpoint.SMALL, columnWidth);
        column.setBreakpointColumnWidth(Breakpoint.XSMALL, 12);
        row.addColumn(column);
    }

    private void buildTopProducts(BootstrapRow row, int columnWidth) {
        var topProducts = new TopProducts();
        var column = new BootstrapColumn(topProducts);
        column.setBreakpointColumnWidth(Breakpoint.LARGE, columnWidth);
        column.setBreakpointColumnWidth(Breakpoint.SMALL, columnWidth);
        column.setBreakpointColumnWidth(Breakpoint.XSMALL, 12);
        row.addColumn(column);
    }

    private void buildTotalRevenue(BootstrapRow row, int columnWidth) {
        var graph = new TotalRevenue();
        var column = new BootstrapColumn(graph);
        column.setBreakpointColumnWidth(Breakpoint.LARGE, columnWidth);
        column.setBreakpointColumnWidth(Breakpoint.SMALL, columnWidth);
        column.setBreakpointColumnWidth(Breakpoint.XSMALL, 12);
        row.addColumn(column);
    }

    private void buildFinancialProjections(BootstrapRow row, int columnWidth) {
        var graph = new GraphCard();
        var column = new BootstrapColumn(graph);
        column.setBreakpointColumnWidth(Breakpoint.LARGE, columnWidth);
        column.setBreakpointColumnWidth(Breakpoint.SMALL, columnWidth);
        column.setBreakpointColumnWidth(Breakpoint.XSMALL, 12);
        row.addColumn(column);
    }

    private void buildKPIs(ObservableList<DashboardKPIModelWrapper> kpisList, BootstrapRow row) {
        for (DashboardKPIModelWrapper data : kpisList) {
            var kpiCard = new KPICard();
            kpiCard.bindToModel(data);

            var column = new BootstrapColumn(kpiCard);
            column.setBreakpointColumnWidth(Breakpoint.LARGE, 3);
            column.setBreakpointColumnWidth(Breakpoint.SMALL, 6);
            column.setBreakpointColumnWidth(Breakpoint.XSMALL, 12);

            row.addColumn(column);
        }
    }
}
