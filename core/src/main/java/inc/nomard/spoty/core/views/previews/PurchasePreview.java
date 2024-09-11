package inc.nomard.spoty.core.views.previews;

import atlantafx.base.controls.*;
import atlantafx.base.theme.*;
import inc.nomard.spoty.network_bridge.dtos.purchases.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import inc.nomard.spoty.utils.navigation.*;
import inc.nomard.spoty.utils.navigation.Spacer;
import java.util.*;
import java.util.stream.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.util.converter.*;
import lombok.extern.java.*;
import org.kordamp.ikonli.fontawesome5.*;
import org.kordamp.ikonli.javafx.*;

@Log
public class PurchasePreview extends BorderPane {
    static final ObservableList<PurchaseDetail> purchaseDetailsList = FXCollections.observableArrayList();
    private static final ListProperty<PurchaseDetail> purchaseDetails = new SimpleListProperty<>(purchaseDetailsList);
    private static final StringProperty orderDateProperty = new SimpleStringProperty();
    private static final StringProperty orderRefProperty = new SimpleStringProperty();
    private static final StringProperty customerNameProperty = new SimpleStringProperty();
    private static final StringProperty customerPhoneProperty = new SimpleStringProperty();
    private static final StringProperty customerEmailProperty = new SimpleStringProperty();
    private static final DoubleProperty subTotalProperty = new SimpleDoubleProperty();
    private static final DoubleProperty discountProperty = new SimpleDoubleProperty();
    private static final DoubleProperty taxProperty = new SimpleDoubleProperty();
    private static final DoubleProperty netCostProperty = new SimpleDoubleProperty();
    private static final DoubleProperty paidAmountProperty = new SimpleDoubleProperty();
    private static final StringProperty noteProperty = new SimpleStringProperty();
    private static final StringProperty servedByProperty = new SimpleStringProperty();
    private final ModalPane modalPane;

    public PurchasePreview(PurchaseMaster purchase, ModalPane modalPane) {
        this.modalPane = modalPane;
        initUI();
        initData(purchase);
    }

    public static ObservableList<PurchaseDetail> getPurchaseDetails() {
        return purchaseDetails.get();
    }

    public void initUI() {
        this.setTop(buildTop());
        this.setCenter(assembleBody());
        this.setPadding(new Insets(10d));
        this.setMaxWidth(1000d);
        this.setPrefWidth(700d);
        this.setMinWidth(400d);
    }

    public void initData(PurchaseMaster purchase) {
        purchaseDetailsList.clear();
        purchaseDetailsList.addAll(purchase.getPurchaseDetails());
        orderDateProperty.set(purchase.getLocaleDate());
        orderRefProperty.set(purchase.getRef());
        customerNameProperty.set(purchase.getSupplier().getName());
        customerPhoneProperty.set(purchase.getSupplier().getPhone());
        customerEmailProperty.set(purchase.getSupplier().getEmail());
        subTotalProperty.set(purchase.getSubTotal());
        discountProperty.set(Objects.nonNull(purchase.getDiscount()) ? purchase.getDiscount().getPercentage() : 0d);
        taxProperty.set(Objects.nonNull(purchase.getTax()) ? purchase.getTax().getPercentage() : 0d);
        netCostProperty.set(purchase.getTotal());
        paidAmountProperty.set(purchase.getPaidAmount());
        noteProperty.set(purchase.getNotes());
        servedByProperty.set(purchase.getCreatedBy().getUserProfile().getName());
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

    private HBox buildSpacedTitledText(String title, DoubleProperty property) {
        var text1 = new Text(title + ":");
        text1.getStyleClass().add(Styles.TEXT_SUBTLE);
        var text2 = new Text();
        text2.getStyleClass().addAll(Styles.TEXT_BOLD);
        text2.textProperty().bindBidirectional(property, new NumberStringConverter(AppUtils.decimalFormatter()));
        return new HBox(20d, text1, new Spacer(), text2);
    }

    private TableView<PurchaseDetail> buildTable() {
        TableView<PurchaseDetail> table = new TableView<>();
        table.getColumns().addAll(buildColumns(table));
        table.setItems(getPurchaseDetails());
        return table;
    }

    private ArrayList<TableColumn<PurchaseDetail, PurchaseDetail>> buildColumns(TableView<PurchaseDetail> table) {
        // Set table column titles.
        TableColumn<PurchaseDetail, PurchaseDetail> product = new TableColumn<>("Product");
        TableColumn<PurchaseDetail, PurchaseDetail> quantity = new TableColumn<>("Quantity");
        TableColumn<PurchaseDetail, PurchaseDetail> cost = new TableColumn<>("Price");

        // Set table column width.
        product.prefWidthProperty().bind(table.widthProperty().multiply(.5));
        quantity.prefWidthProperty().bind(table.widthProperty().multiply(.25));
        cost.prefWidthProperty().bind(table.widthProperty().multiply(.25));

        // Set column data.
        product.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        product.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getProductName());
            }
        });
        quantity.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        quantity.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getQuantity()));
            }
        });
        cost.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        cost.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(PurchaseDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : "UGX " + AppUtils.decimalFormatter().format(item.getSubTotalCost()));
            }
        });

        return new ArrayList<>(Stream.of(product, quantity, cost).toList());
    }

    private HBox buildHeader() {
        var vbox1 = new VBox(buildHeaderText("Purchase Receipt"));
        vbox1.setAlignment(Pos.BOTTOM_LEFT);
        var vbox2 = new VBox(buildTitledText("Date", orderDateProperty));
        vbox2.setAlignment(Pos.TOP_RIGHT);
        vbox2.setMaxHeight(80d);
        vbox2.setPrefHeight(80d);
        vbox2.setMinHeight(80d);
        var hbox = new HBox(
                vbox1,
                new Spacer(),
                vbox2
        );
        hbox.setMaxHeight(150d);
        hbox.setPrefHeight(100d);
        hbox.setMinHeight(50d);
        return hbox;
    }

    private HBox buildReference() {
        var hbox = new HBox(20d, buildTitledText("Receipt No.", orderRefProperty)
        );
        hbox.setMaxHeight(50d);
        hbox.setPrefHeight(50d);
        hbox.setMinHeight(50d);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        return hbox;
    }

    private VBox buildCustomerDetails() {
        var vbox = new VBox(10d,
                buildSpacedTitledText("Customer's Name", customerNameProperty),
                buildSpacedTitledText("Customer's Phone No.", customerPhoneProperty),
                buildSpacedTitledText("Customer's Email", customerEmailProperty),
                new Separator()
        );
        VBox.setMargin(vbox, new Insets(5d, 0d, 2.5d, 0d));
        return vbox;
    }

    private VBox buildPurchaseProductDetails() {
        var vbox = new VBox(buildTable());
        VBox.setMargin(vbox, new Insets(2.5d, 0d, 2.5d, 0d));
        return vbox;
    }

    private VBox buildMoneyDetails() {
        var vbox1 = new VBox(10d,
                buildSpacedTitledText("Sub Total", subTotalProperty),
                buildSpacedTitledText("Discount(%)", discountProperty),
                buildSpacedTitledText("Tax(%)", taxProperty)
        );
        vbox1.setMaxWidth(350d);
        vbox1.setPrefWidth(300d);
        vbox1.setMinWidth(250d);
        VBox.setMargin(vbox1, new Insets(2.5d, 0d, 2.5d, 0d));
        vbox1.setAlignment(Pos.CENTER_RIGHT);
        var vbox2 = new VBox(buildSpacedTitledText("Net Cost", netCostProperty));
        vbox2.setMaxWidth(350d);
        vbox2.setPrefWidth(300d);
        vbox2.setMinWidth(250d);
        VBox.setMargin(vbox2, new Insets(2.5d, 0d, 2.5d, 0d));
        vbox2.setAlignment(Pos.CENTER_RIGHT);
        var vbox3 = new VBox(buildSpacedTitledText("Paid Amount", paidAmountProperty));
        vbox3.setMaxWidth(350d);
        vbox3.setPrefWidth(300d);
        vbox3.setMinWidth(250d);
        VBox.setMargin(vbox3, new Insets(2.5d, 0d, 5d, 0d));
        vbox3.setAlignment(Pos.CENTER_RIGHT);
        var vbox = new VBox(vbox1,
                new Separator(),
                vbox2,
                new Separator(),
                vbox3,
                new Separator());
        vbox.setAlignment(Pos.CENTER_RIGHT);
        return vbox;
    }

    private HBox buildNote() {
        return new HBox(buildSpacedTitledText("Note", noteProperty));
    }

    private HBox buildServedBy() {
        return new HBox(buildSpacedTitledText("Done By", servedByProperty));
    }

    private VBox assembleBody() {
        var vbox = new VBox(10d,
                buildHeader(),
                buildReference(),
                buildCustomerDetails(),
                buildPurchaseProductDetails(),
                buildMoneyDetails(),
                buildNote(),
                new Separator(),
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

    private FontIcon buildFontIcon(SpotyGotFunctional.ParameterlessConsumer onAction, String styleClass) {
        var icon = new FontIcon(FontAwesomeSolid.CIRCLE);
        icon.setOnMouseClicked(event -> onAction.run());
        icon.getStyleClass().addAll(styleClass, Styles.DANGER);
        return icon;
    }

    private HBox buildTop() {
        var hbox = new HBox(buildFontIcon(this::dispose, "close-icon"));
        hbox.setMaxHeight(20d);
        hbox.setMinHeight(20d);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    public void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
    }
}