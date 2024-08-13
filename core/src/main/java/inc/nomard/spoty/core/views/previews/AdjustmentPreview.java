package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.network_bridge.dtos.adjustments.*;
import io.github.palexdev.materialfx.controls.*;
import java.util.*;
import java.util.stream.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import lombok.extern.java.*;

@Log
public class AdjustmentPreview extends ModalPage {
    static final ObservableList<AdjustmentDetail> adjustmentDetailsList = FXCollections.observableArrayList();
    private static final ListProperty<AdjustmentDetail> adjustmentDetails =
            new SimpleListProperty<>(adjustmentDetailsList);
    public Label adjustmentDate;
    public Label adjustmentRef;
    public TableView<AdjustmentDetail> itemsTable;
    public Label doneBy;
    public Label adjustmentNote;
    private TableColumn<AdjustmentDetail, String> product;
    private TableColumn<AdjustmentDetail, String> type;
    private TableColumn<AdjustmentDetail, String> quantity;

    public AdjustmentPreview(AdjustmentMaster adjustment) {
        setPadding(new Insets(10, 10, 10, 10));

        var vBox = new VBox();
        vBox.setMaxHeight(900.0);
        vBox.setMaxWidth(1000.0);
        vBox.setMinHeight(600.0);
        vBox.setMinWidth(500.0);
        vBox.setPrefHeight(800.0);
        vBox.setPrefWidth(700.0);
        vBox.setPadding(new Insets(20, 10, 20, 10));

        var hBox1 = new HBox();
        hBox1.setMaxHeight(150.0);
        hBox1.setMinHeight(50.0);
        hBox1.setPrefHeight(100.0);

        var vBox1 = new VBox();
        vBox1.setAlignment(Pos.BOTTOM_LEFT);
        vBox1.setPrefHeight(80.0);

        var headerLabel = new Label("Inventory Adjustment");
        headerLabel.getStyleClass().add("preview-header");
        vBox1.getChildren().add(headerLabel);

        var region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        var hBox2 = new HBox();
        hBox2.setAlignment(Pos.TOP_RIGHT);
        hBox2.setLayoutX(10.0);
        hBox2.setLayoutY(10.0);
        hBox2.setPrefHeight(80.0);

        var dateLabel = new Label("Date: ");
        dateLabel.getStyleClass().add("preview-header");
        adjustmentDate = new Label();

        hBox2.getChildren().addAll(dateLabel, adjustmentDate);
        hBox1.getChildren().addAll(vBox1, region, hBox2);

        var hBox3 = new HBox();
        hBox3.setAlignment(Pos.CENTER_RIGHT);
        hBox3.setLayoutX(10.0);
        hBox3.setLayoutY(10.0);
        hBox3.setPrefHeight(50.0);
        hBox3.setSpacing(20.0);

        var invoiceLabel = new Label("Invoice ID");
        invoiceLabel.getStyleClass().add("preview-header");
        adjustmentRef = new Label();
        adjustmentRef.setLayoutX(10.0);
        adjustmentRef.setLayoutY(10.0);

        hBox3.getChildren().addAll(invoiceLabel, adjustmentRef);

        var hBox4 = new HBox();
        hBox4.setLayoutX(10.0);
        hBox4.setLayoutY(10.0);
        hBox4.setPrefHeight(50.0);
        hBox4.setSpacing(20.0);

        var noteLabel = new Label("Note: ");
        noteLabel.getStyleClass().add("preview-header");
        adjustmentNote = new Label();
        adjustmentNote.setLayoutX(10.0);
        adjustmentNote.setLayoutY(10.0);
        adjustmentNote.setWrapText(true);

        hBox4.getChildren().addAll(noteLabel, adjustmentNote);

        Separator separator = new Separator();

        VBox vBox2 = new VBox();
        VBox.setMargin(vBox2, new Insets(30, 0, 10, 0));

        itemsTable = new TableView<>();
        itemsTable.setPrefWidth(200.0);
        vBox2.getChildren().add(itemsTable);

        var hBox5 = new HBox();
        hBox5.setSpacing(20.0);
        hBox5.setPadding(new Insets(0, 50, 0, 0));

        VBox.setMargin(hBox5, new Insets(30, 0, 30, 0));

        var doneByLabel = new Label("Done By:");
        doneBy = new Label("Jonathan Mark");

        hBox5.getChildren().addAll(doneByLabel, doneBy);

        vBox.getChildren().addAll(hBox1, hBox3, hBox4, separator, vBox2, hBox5);

        MFXScrollPane scrollPane = new MFXScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(vBox);

        setCenter(scrollPane);
        Platform.runLater(this::setupTable);
        adjustmentDetailsList.clear();
        adjustmentRef.setText(adjustment.getRef());
        adjustmentNote.setText(adjustment.getNotes());
//        doneBy.setText(adjustment.getCreatedBy().getName());
        adjustmentDetailsList.addAll(adjustment.getAdjustmentDetails());
    }

    private void setupTable() {
        product = new TableColumn<>("Name");
        type = new TableColumn<>("Type");
        quantity = new TableColumn<>("Quantity");

        // Set table column width.
        product.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.5));
        type.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.5));
        quantity.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.5));

        var columnList = new LinkedList<>(Stream.of(product, type, quantity).toList());
        itemsTable
                .getColumns()
                .addAll(columnList);

        // Populate table.
        itemsTable.setItems(adjustmentDetails.get());
    }
}
