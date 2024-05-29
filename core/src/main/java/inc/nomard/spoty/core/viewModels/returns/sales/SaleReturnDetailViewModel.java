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

package inc.nomard.spoty.core.viewModels.returns.sales;

import com.google.gson.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.returns.sale_returns.*;
import inc.nomard.spoty.utils.adapters.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.*;


import lombok.extern.slf4j.*;

@Slf4j
public class SaleReturnDetailViewModel {
    @Getter
    public static final ObservableList<SaleReturnDetail> saleReturnDetailsList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final LongProperty id = new SimpleLongProperty();
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<SaleReturnMaster> SaleReturn = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty("");
    private static final StringProperty serial = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    private static final StringProperty location = new SimpleStringProperty("");

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        SaleReturnDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        SaleReturnDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
        return product;
    }

    public static SaleReturnMaster getSaleReturn() {
        return SaleReturn.get();
    }

    public static void setSaleReturn(SaleReturnMaster SaleReturn) {
        SaleReturnDetailViewModel.SaleReturn.set(SaleReturn);
    }

    public static ObjectProperty<SaleReturnMaster> SaleReturnProperty() {
        return SaleReturn;
    }

    public static long getQuantity() {
        return Long.parseLong(!quantity.get().isEmpty() ? quantity.get() : "0");
    }

    public static void setQuantity(String quantity) {
        SaleReturnDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        SaleReturnDetailViewModel.description.set(description);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static String getSerial() {
        return serial.get();
    }

    public static void setSerial(String serial) {
        SaleReturnDetailViewModel.serial.set(serial);
    }

    public static StringProperty serialProperty() {
        return serial;
    }

    public static String getLocation() {
        return location.get();
    }

    public static void setLocation(String location) {
        SaleReturnDetailViewModel.location.set(location);
    }

    public static StringProperty locationProperty() {
        return location;
    }

    public static void resetProperties() {
        setId(0);
        setProduct(null);
        setQuantity("");
        setSerial("");
        setDescription("");
        setLocation("");
    }

    public static void getSaleReturnDetails() throws Exception {
//        Dao<SaleReturnDetail, Long> saleReturnDetailDao =
//                DaoManager.createDao(connectionSource, SaleReturnDetail.class);
//
//        saleReturnDetailsList.clear();
//        saleReturnDetailsList.addAll(saleReturnDetailDao.queryForAll());
    }
}
