package inc.nomard.spoty.core.views.previews;

import atlantafx.base.theme.*;
import inc.nomard.spoty.network_bridge.dtos.stock_ins.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.navigation.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import lombok.extern.java.*;

@Log
public class StockInPreview extends BorderPane {
    static final ObservableList<StockInDetail> stockInDetailsList = FXCollections.observableArrayList();
    private static final ListProperty<StockInDetail> stockInDetails = new SimpleListProperty<>(stockInDetailsList);
    private static final StringProperty orderDateProperty = new SimpleStringProperty();
    private static final StringProperty orderRefProperty = new SimpleStringProperty();
    private static final StringProperty servedByProperty = new SimpleStringProperty();

    public StockInPreview(StockInMaster stockIn) {
        initUI();
        initData(stockIn);
    }

    public static ObservableList<StockInDetail> getStockInDetails() {
        return stockInDetails.get();
    }

    public void initUI() {
        this.setCenter(assembleBody());
        this.setPadding(new Insets(10d));
        this.setMaxWidth(1000d);
        this.setPrefWidth(700d);
        this.setMinWidth(400d);
    }

    public void initData(StockInMaster stockIn) {
        stockInDetailsList.clear();
        stockInDetailsList.addAll(stockIn.getStockInDetails());
        orderDateProperty.set(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()).format(stockIn.getCreatedAt()));
        orderRefProperty.set(stockIn.getRef());
        servedByProperty.set(stockIn.getCreatedBy().getUserProfile().getName());
    }

    private Text buildHeaderText(String txt) {
        var text = new Text(txt);
        text.getStyleClass().addAll(Styles.TITLE_2, Styles.TEXT_SUBTLE, Styles.TEXT_ITALIC);
        return text;
    }

    private HBox buildTitledText(String title, StringProperty property) {
        var text1 = new Text(title + ":");
        text1.getStyleClass().add(Styles.TEXT_SUBTLE);
        var text2 = new Text();
        text2.getStyleClass().addAll(Styles.TEXT_BOLD);
        text2.textProperty().bind(property);
        return new HBox(20d, text1, text2);
    }

    private HBox buildSpacedTitledText(String title, StringProperty property) {
        var text1 = new Text(title + ":");
        text1.getStyleClass().add(Styles.TEXT_SUBTLE);
        var text2 = new Text();
        text2.getStyleClass().addAll(Styles.TEXT_BOLD);
        text2.textProperty().bind(property);
        return new HBox(20d, text1, new Spacer(), text2);
    }

    private TableView<StockInDetail> buildTable() {
        TableView<StockInDetail> table = new TableView<>();
        table.getColumns().addAll(buildColumns(table));
        table.setItems(getStockInDetails());
        return table;
    }

    private ArrayList<TableColumn<StockInDetail, StockInDetail>> buildColumns(TableView<StockInDetail> table) {
        // Set table column titles.
        TableColumn<StockInDetail, StockInDetail> product = new TableColumn<>("Product");
        TableColumn<StockInDetail, StockInDetail> quantity = new TableColumn<>("Quantity");

        // Set table column width.
        product.prefWidthProperty().bind(table.widthProperty().multiply(.6));
        quantity.prefWidthProperty().bind(table.widthProperty().multiply(.4));

        // Set column data.
        product.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        product.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(StockInDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getProductName());
            }
        });
        quantity.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        quantity.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(StockInDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getQuantity()));
            }
        });

        return new ArrayList<>(Stream.of(product, quantity).toList());
    }

    private VBox buildHeader() {
        var vbox1 = new VBox(buildHeaderText("Stock In Receipt"));
        vbox1.setAlignment(Pos.CENTER);
        var vbox2 = new VBox(buildTitledText("Date", orderDateProperty));
        vbox2.setAlignment(Pos.TOP_RIGHT);
        vbox2.setMaxHeight(80d);
        vbox2.setPrefHeight(80d);
        vbox2.setMinHeight(80d);
        var vbox = new VBox(20,
                vbox1,
                vbox2
        );
        vbox.setMaxHeight(150d);
        vbox.setPrefHeight(100d);
        vbox.setMinHeight(50d);
        return vbox;
    }

    private HBox buildReference() {
        var hbox = new HBox(20d, buildTitledText("Reference No.", orderRefProperty)
        );
        hbox.setMaxHeight(50d);
        hbox.setPrefHeight(50d);
        hbox.setMinHeight(50d);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        return hbox;
    }

    private VBox buildSaleProductDetails() {
        var vbox = new VBox(buildTable());
        VBox.setMargin(vbox, new Insets(2.5d, 0d, 2.5d, 0d));
        return vbox;
    }

    private HBox buildServedBy() {
        return new HBox(buildSpacedTitledText("Made By", servedByProperty));
    }

    private VBox assembleBody() {
        var vbox = new VBox(10d,
                buildHeader(),
                buildReference(),
                buildSaleProductDetails(),
                buildServedBy()
        );
        vbox.setMaxHeight(900d);
        vbox.setPrefHeight(800d);
        vbox.setMinHeight(600d);
        vbox.setMaxWidth(1000d);
        vbox.setPrefWidth(700d);
        vbox.setMinWidth(500d);
        vbox.setPadding(new Insets(20d, 10d, 20d, 10d));
        vbox.setAlignment(Pos.CENTER_RIGHT);
        return vbox;
    }
}
