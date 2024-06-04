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

package inc.nomard.spoty.core.viewModels.purchases;

import com.google.gson.*;
import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.purchases.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.adapters.*;
import java.util.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.*;
import lombok.extern.java.*;

@Log
public class PurchaseDetailViewModel {
    @Getter
    public static final ObservableList<PurchaseDetail> purchaseDetailsList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    new UnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<PurchaseDetail> purchaseDetails =
            new SimpleListProperty<>(purchaseDetailsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<PurchaseMaster> purchase = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>(null);
    private static final StringProperty quantity = new SimpleStringProperty("");
    private static final PurchasesRepositoryImpl purchasesRepository = new PurchasesRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        PurchaseDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static PurchaseMaster getPurchase() {
        return purchase.get();
    }

    public static void setPurchase(PurchaseMaster purchase) {
        PurchaseDetailViewModel.purchase.set(purchase);
    }

    public static ObjectProperty<PurchaseMaster> purchaseProperty() {
        return purchase;
    }

    public static Integer getQuantity() {
        return Integer.parseInt(quantity.get());
    }

    public static void setQuantity(String quantity) {
        PurchaseDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        PurchaseDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
        return product;
    }

    public static ObservableList<PurchaseDetail> getPurchaseDetails() {
        return purchaseDetails.get();
    }

    public static void setPurchaseDetails(ObservableList<PurchaseDetail> purchaseDetails) {
        PurchaseDetailViewModel.purchaseDetails.set(purchaseDetails);
    }

    public static ListProperty<PurchaseDetail> purchaseDetailsProperty() {
        return purchaseDetails;
    }

    public static void resetProperties() {
        setId(0L);
        setTempId(-1);
        setPurchase(null);
        setProduct(null);
        setQuantity("");
    }

    public static void addPurchaseDetail() {
        var purchaseDetail = PurchaseDetail.builder()
                .product(getProduct())
                .quantity(getQuantity())
                .build();
        purchaseDetailsList.add(purchaseDetail);
    }

    public static void updatePurchaseDetail(Long index) {
        var purchaseDetail = purchaseDetailsList.get(Math.toIntExact(index));
        purchaseDetail.setProduct(getProduct());
        purchaseDetail.setQuantity(getQuantity());
        purchaseDetailsList.set(getTempId(), purchaseDetail);
    }

    public static void getPurchaseDetail(PurchaseDetail purchaseDetail) {
        setTempId(getPurchaseDetails().indexOf(purchaseDetail));
        setProduct(purchaseDetail.getProduct());
        setQuantity(String.valueOf(purchaseDetail.getQuantity()));
    }

    public static void removePurchaseDetail(Long index, int tempIndex) {
        Platform.runLater(() -> purchaseDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }
}
