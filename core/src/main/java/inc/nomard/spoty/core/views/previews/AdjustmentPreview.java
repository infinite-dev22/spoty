package inc.nomard.spoty.core.views.previews;

import atlantafx.base.controls.*;
import atlantafx.base.theme.*;
import inc.nomard.spoty.network_bridge.dtos.adjustments.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import inc.nomard.spoty.utils.navigation.Spacer;
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
import org.kordamp.ikonli.fontawesome5.*;
import org.kordamp.ikonli.javafx.*;

@Log
public class AdjustmentPreview extends BorderPane {
    static final ObservableList<AdjustmentDetail> adjustmentDetailsList = FXCollections.observableArrayList();
    private static final ListProperty<AdjustmentDetail> adjustmentDetails = new SimpleListProperty<>(adjustmentDetailsList);
    private static final StringProperty adjustmentDateProperty = new SimpleStringProperty();
    private static final StringProperty adjustmentRefProperty = new SimpleStringProperty();
    private static final StringProperty noteProperty = new SimpleStringProperty();
    private static final StringProperty doneByProperty = new SimpleStringProperty();
    private final ModalPane modalPane;

    public AdjustmentPreview(AdjustmentMaster adjustment, ModalPane modalPane) {
        this.modalPane = modalPane;
        initUI();
        initData(adjustment);
    }

    public static ObservableList<AdjustmentDetail> getAdjustmentDetails() {
        return adjustmentDetails.get();
    }

    public void initUI() {
        this.setTop(buildTop());
        this.setCenter(assembleCenter());
        this.setPadding(new Insets(10d));
        this.setMaxWidth(1000d);
        this.setPrefWidth(700d);
        this.setMinWidth(400d);
    }

    public void initData(AdjustmentMaster adjustment) {
        adjustmentDetailsList.clear();
        adjustmentDetailsList.addAll(adjustment.getAdjustmentDetails());
        adjustmentDateProperty.set(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()).format(adjustment.getCreatedAt()));
        adjustmentRefProperty.set(adjustment.getRef());
        noteProperty.set(adjustment.getNotes());
        doneByProperty.set(adjustment.getCreatedBy().getName());
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

    private TableView<AdjustmentDetail> buildTable() {
        TableView<AdjustmentDetail> table = new TableView<>();
        table.getColumns().addAll(buildColumns(table));
        table.setItems(getAdjustmentDetails());
        return table;
    }

    private ArrayList<TableColumn<AdjustmentDetail, AdjustmentDetail>> buildColumns(TableView<AdjustmentDetail> table) {
        // Set table column titles.
        TableColumn<AdjustmentDetail, AdjustmentDetail> product = new TableColumn<>("Product");
        TableColumn<AdjustmentDetail, AdjustmentDetail> quantity = new TableColumn<>("Quantity");
        TableColumn<AdjustmentDetail, AdjustmentDetail> type = new TableColumn<>("Type");

        // Set table column width.
        product.prefWidthProperty().bind(table.widthProperty().multiply(.5));
        quantity.prefWidthProperty().bind(table.widthProperty().multiply(.25));
        type.prefWidthProperty().bind(table.widthProperty().multiply(.25));

        // Set column data.
        product.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        product.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(AdjustmentDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getProductName());
            }
        });
        quantity.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        quantity.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(AdjustmentDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getQuantity()));
            }
        });
        type.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        type.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(AdjustmentDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getAdjustmentType());
            }
        });

        return new ArrayList<>(Stream.of(product, quantity, type).toList());
    }

    private HBox buildHeader() {
        var vbox1 = new VBox(buildHeaderText("Inventory Adjustment"));
        vbox1.setAlignment(Pos.BOTTOM_LEFT);
        var vbox2 = new VBox(buildTitledText("Date", adjustmentDateProperty));
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
        var hbox = new HBox(20d, buildTitledText("Receipt No.", adjustmentRefProperty)
        );
        hbox.setMaxHeight(50d);
        hbox.setPrefHeight(50d);
        hbox.setMinHeight(50d);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        return hbox;
    }

    private VBox buildAdjustmentProductDetails() {
        var vbox = new VBox(buildTable());
        VBox.setMargin(vbox, new Insets(2.5d, 0d, 2.5d, 0d));
        return vbox;
    }

    private HBox buildNote() {
        return new HBox(buildSpacedTitledText("Note", noteProperty));
    }

    private HBox buildDoneBy() {
        return new HBox(buildSpacedTitledText("Done By", doneByProperty));
    }

    private VBox assembleCenter() {
        var vbox = new VBox(10d,
                buildHeader(),
                buildReference(),
                buildAdjustmentProductDetails(),
                buildNote(),
                new Separator(),
                buildDoneBy()
        );
        vbox.setMaxHeight(900d);
        vbox.setPrefHeight(800d);
        vbox.setMinHeight(600d);
        vbox.setMaxWidth(1000d);
        vbox.setPrefWidth(700d);
        vbox.setMinWidth(400d);
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
