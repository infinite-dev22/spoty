package inc.nomard.spoty.core.views.components;

import inc.nomard.spoty.core.values.strings.Labels;
import inc.nomard.spoty.core.viewModels.dashboard.DashboardViewModel;
import inc.nomard.spoty.network_bridge.dtos.sales.SaleMaster;
import inc.nomard.spoty.utils.AppUtils;
import inc.nomard.spoty.utils.UIUtils;
import inc.nomard.spoty.utils.navigation.Spacer;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Stream;

@Log4j2
public class Orders extends AnchorPane {
    public TableView<SaleMaster> saleOrders;
    private TableColumn<SaleMaster, SaleMaster> saleCustomer;
    private TableColumn<SaleMaster, SaleMaster> saleStatus;
    private TableColumn<SaleMaster, SaleMaster> salePaymentStatus;
    private TableColumn<SaleMaster, SaleMaster> saleGrandTotal;
    private TableColumn<SaleMaster, SaleMaster> saleAmountPaid;

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

    private Label buildTitle() {
        var label = new Label(Labels.RECENT_ORDERS);
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
        saleCustomer = new TableColumn<>("Customer");
        saleStatus = new TableColumn<>("Order Status");
        salePaymentStatus = new TableColumn<>("Pay Status");
        saleGrandTotal = new TableColumn<>("Total Amount");
        saleAmountPaid = new TableColumn<>("Paid Amount");

        saleCustomer.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));
        saleStatus.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));
        salePaymentStatus.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));
        saleGrandTotal.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));
        saleAmountPaid.prefWidthProperty().bind(saleOrders.widthProperty().multiply(.25));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(saleCustomer,
                saleStatus,
                salePaymentStatus,
                saleGrandTotal,
                saleAmountPaid).toList());
        saleOrders.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        saleOrders.getColumns().addAll(columnList);
        styleSaleMasterTable();

        saleOrders.setItems(DashboardViewModel.getRecentOrders());
    }

    private void styleSaleMasterTable() {
        saleOrders.setPrefSize(1200, 1000);

        saleOrders.setRowFactory(
                t -> {
                    TableRow<SaleMaster> row = new TableRow<>();
                    EventHandler<MouseEvent> eventHandler =
                            event -> {
                                if (Objects.equals(event.getEventType(), MouseEvent.MOUSE_CLICKED)) {
                                    log.info("Row with Customer \"" + row.getItem().getCustomerName() + "\" has been clicked.");
                                }
                                event.consume();
                            };
                    row.setOnMouseClicked(eventHandler);
                    return row;
                });
    }

    private void setupTableColumns() {
        saleCustomer.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        saleCustomer.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleMaster item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getCustomerName());
            }
        });
        saleStatus.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        saleStatus.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                if (!empty && !Objects.isNull(item)) {
                    var chip = new Label(item.getSaleStatus());
                    chip.setPadding(new Insets(5, 10, 5, 10));
                    chip.setAlignment(Pos.CENTER);

                    Color col;
                    Color color;
                    switch (item.getSaleStatus().toLowerCase()) {
                        case "completed" -> {
                            col = Color.rgb(50, 215, 75);
                            color = Color.rgb(50, 215, 75, .1);
                        }
                        case "pending" -> {
                            col = Color.web("#9a1fe6");
                            color = Color.web("#9a1fe6", .1);
                        }
                        case "ordered" -> {
                            col = Color.rgb(255, 159, 10);
                            color = Color.rgb(255, 159, 10, .1);
                        }
                        default -> {
                            col = Color.web("#aeaeb2");
                            color = Color.web("#aeaeb2", .1);
                        }
                    }

                    chip.setTextFill(col);
                    chip.setBackground(new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY)));

                    setGraphic(chip);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(null);
                }
            }
        });
        salePaymentStatus.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        salePaymentStatus.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                if (!empty && !Objects.isNull(item)) {
                    var chip = new Label(item.getPaymentStatus());
                    chip.setPadding(new Insets(5, 10, 5, 10));
                    chip.setAlignment(Pos.CENTER);

                    Color col;
                    Color color;
                    switch (item.getPaymentStatus().toLowerCase()) {
                        case "paid" -> {
                            col = Color.rgb(50, 215, 75);
                            color = Color.rgb(50, 215, 75, .1);
                        }
                        case "unpaid" -> {
                            col = Color.web("#9a1fe6");
                            color = Color.web("#9a1fe6", .1);
                        }
                        case "partial" -> {
                            col = Color.rgb(255, 159, 10);
                            color = Color.rgb(255, 159, 10, .1);
                        }
                        default -> {
                            col = Color.web("#aeaeb2");
                            color = Color.web("#aeaeb2", .1);
                        }
                    }

                    chip.setTextFill(col);
                    chip.setBackground(new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY)));

                    setGraphic(chip);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(null);
                }
            }
        });
        saleGrandTotal.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        saleGrandTotal.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getTotal()));
            }
        });
        saleAmountPaid.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        saleAmountPaid.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleMaster item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER_RIGHT);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getAmountPaid()));
            }
        });
    }
}
