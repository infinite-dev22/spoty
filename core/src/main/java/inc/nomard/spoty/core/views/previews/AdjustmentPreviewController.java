/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.network_bridge.dtos.adjustments.AdjustmentDetail;
import inc.nomard.spoty.network_bridge.dtos.adjustments.AdjustmentMaster;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class AdjustmentPreviewController implements Initializable {
    @FXML
    public Label adjustmentDate;
    @FXML
    public Label adjustmentRef;
    @FXML
    public MFXTableView<AdjustmentDetail> itemsTable;
    @FXML
    public Label doneBy;
    @FXML
    public Label adjustmentNote;
    static final ObservableList<AdjustmentDetail> adjustmentDetailsList = FXCollections.observableArrayList();
    private static final ListProperty<AdjustmentDetail> adjustmentDetails =
            new SimpleListProperty<>(adjustmentDetailsList);

    public static ObservableList<AdjustmentDetail> getAdjustmentDetails() {
        return adjustmentDetails.get();
    }

    public static ListProperty<AdjustmentDetail> adjustmentDetailsProperty() {
        return adjustmentDetails;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        // Set table column titles.
        MFXTableColumn<AdjustmentDetail> product =
                new MFXTableColumn<>("Name", false, Comparator.comparing(AdjustmentDetail::getProductName));
        MFXTableColumn<AdjustmentDetail> quantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(AdjustmentDetail::getQuantity));

        // Set table column data.
        product.setRowCellFactory(saleDetail -> {
            var cell = new MFXTableRowCell<>(AdjustmentDetail::getProductName);
            cell.setAlignment(Pos.CENTER_LEFT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        quantity.setRowCellFactory(saleDetail -> {
            var cell = new MFXTableRowCell<>(AdjustmentDetail::getQuantity);
            cell.setAlignment(Pos.CENTER_RIGHT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });

        // Set table column width.
        product.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.5));
        quantity.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.5));
        // Set table filter.
        itemsTable
                .getTableColumns()
                .addAll(product, quantity);

        styleTable();

        // Populate table.
        if (getAdjustmentDetails().isEmpty()) {
            getAdjustmentDetails()
                    .addListener(
                            (ListChangeListener<AdjustmentDetail>)
                                    change -> itemsTable.setItems(getAdjustmentDetails()));
        } else {
            itemsTable.itemsProperty().bindBidirectional(adjustmentDetailsProperty());
        }
    }

    private void styleTable() {
        itemsTable.setPrefSize(1000, 1000);
        itemsTable.features().enableBounceEffect();
        itemsTable.features().enableSmoothScrolling(0.5);
        itemsTable.setFooterVisible(false);
    }

    public void init(AdjustmentMaster adjustment) {
        adjustmentDetailsList.clear();
        adjustmentDate.setText(adjustment.getLocaleDate());
        adjustmentRef.setText(adjustment.getRef());
        adjustmentNote.setText(adjustment.getNotes());
//        doneBy.setText(adjustment.getCreatedBy().getName());
        adjustmentDetailsList.addAll(adjustment.getAdjustmentDetails());
    }
}
