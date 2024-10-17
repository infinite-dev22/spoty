/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in AdjustmentDetailViewModel c is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of AdjustmentDetailViewModel c is prohibid.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package inc.nomard.spoty.core.viewModels.adjustments;

import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.network_bridge.dtos.adjustments.AdjustmentDetail;
import inc.nomard.spoty.network_bridge.dtos.adjustments.AdjustmentMaster;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static inc.nomard.spoty.core.values.SharedResources.*;

@Slf4j
public class AdjustmentDetailViewModel {
    @Getter
    public static final ObservableList<AdjustmentDetail> adjustmentDetailsList =
            FXCollections.observableArrayList();
    private static final ListProperty<AdjustmentDetail> adjustmentDetails =
            new SimpleListProperty<>(adjustmentDetailsList);
    private static final LongProperty id = new SimpleLongProperty();
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<AdjustmentMaster> adjustment = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty();
    private static final StringProperty adjustmentType = new SimpleStringProperty();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        AdjustmentDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        AdjustmentDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
        return product;
    }

    public static AdjustmentMaster getAdjustment() {
        return adjustment.get();
    }

    public static void setAdjustment(AdjustmentMaster adjustment) {
        AdjustmentDetailViewModel.adjustment.set(adjustment);
    }

    public static ObjectProperty<AdjustmentMaster> adjustmentProperty() {
        return adjustment;
    }

    public static Long getQuantity() {
        return Long.parseLong(quantity.get());
    }

    public static void setQuantity(String quantity) {
        AdjustmentDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static String getAdjustmentType() {
        return adjustmentType.get();
    }

    public static void setAdjustmentType(String adjustmentType) {
        AdjustmentDetailViewModel.adjustmentType.set(adjustmentType);
    }

    public static StringProperty adjustmentTypeProperty() {
        return adjustmentType;
    }

    public static ObservableList<AdjustmentDetail> getAdjustmentDetails() {
        return adjustmentDetails.get();
    }

    public static void setAdjustmentDetails(ObservableList<AdjustmentDetail> adjustmentDetails) {
        AdjustmentDetailViewModel.adjustmentDetails.set(adjustmentDetails);
    }

    public static ListProperty<AdjustmentDetail> adjustmentDetailsProperty() {
        return adjustmentDetails;
    }

    public static void resetProperties() {
        setId(0L);
        setTempId(-1);
        setProduct(null);
        setAdjustment(null);
        setAdjustmentType("");
        setQuantity("");
    }

    public static void addAdjustmentDetails() {
        var adjustmentDetail = AdjustmentDetail.builder()
                .product(getProduct())
                .quantity(getQuantity())
                .adjustmentType(getAdjustmentType())
                .build();
        adjustmentDetailsList.add(adjustmentDetail);
    }

    public static void updateAdjustmentDetail(Long index) {
        var adjustmentDetail = adjustmentDetailsList.get(Math.toIntExact(index));
        adjustmentDetail.setProduct(getProduct());
        adjustmentDetail.setQuantity(getQuantity());
        adjustmentDetail.setAdjustmentType(getAdjustmentType());
        adjustmentDetailsList.set(getTempId(), adjustmentDetail);
    }

    public static void getAdjustmentDetail(AdjustmentDetail adjustmentDetail) {
        setTempId(getAdjustmentDetails().indexOf(adjustmentDetail));
        setProduct(adjustmentDetail.getProduct());
        setQuantity(String.valueOf(adjustmentDetail.getQuantity()));
        setAdjustmentType(adjustmentDetail.getAdjustmentType());
    }

    public static void removeAdjustmentDetail(Long index, int tempIndex) {
        Platform.runLater(() -> adjustmentDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }
}
