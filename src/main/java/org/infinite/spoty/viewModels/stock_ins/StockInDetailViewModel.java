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

package org.infinite.spoty.viewModels.stock_ins;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.data_source.daos.Product;
import org.infinite.spoty.data_source.daos.stock_ins.StockInDetail;
import org.infinite.spoty.data_source.daos.stock_ins.StockInMaster;
import org.infinite.spoty.data_source.daos.stock_ins.StockInTransaction;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.StockInsRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.viewModels.ProductViewModel;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.infinite.spoty.values.SharedResources.*;

public class StockInDetailViewModel {
    public static final ObservableList<StockInDetail> stockInDetailsList =
            FXCollections.observableArrayList();
    private static final ListProperty<StockInDetail> stockInDetails =
            new SimpleListProperty<>(stockInDetailsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<StockInMaster> stockIn = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty("");
    private static final StringProperty serial = new SimpleStringProperty("");
    private static final StringProperty description = new SimpleStringProperty("");
    private static final StringProperty location = new SimpleStringProperty("");
    private static final StockInsRepositoryImpl stockInsRepository = new StockInsRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        StockInDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        StockInDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
        return product;
    }

    public static StockInMaster getStockIn() {
        return stockIn.get();
    }

    public static void setStockIn(StockInMaster stockIn) {
        StockInDetailViewModel.stockIn.set(stockIn);
    }

    public static ObjectProperty<StockInMaster> stockInProperty() {
        return stockIn;
    }

    public static Integer getQuantity() {
        return Integer.parseInt(!quantity.get().isEmpty() ? quantity.get() : "0");
    }

    public static void setQuantity(String quantity) {
        StockInDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        StockInDetailViewModel.description.set(description);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static String getSerial() {
        return serial.get();
    }

    public static void setSerial(String serial) {
        StockInDetailViewModel.serial.set(serial);
    }

    public static StringProperty serialProperty() {
        return serial;
    }

    public static String getLocation() {
        return location.get();
    }

    public static void setLocation(String location) {
        StockInDetailViewModel.location.set(location);
    }

    public static StringProperty locationProperty() {
        return location;
    }

    public static ObservableList<StockInDetail> getStockInDetails() {
        return stockInDetails.get();
    }

    public static void setStockInDetails(ObservableList<StockInDetail> stockInDetails) {
        StockInDetailViewModel.stockInDetails.set(stockInDetails);
    }

    public static ListProperty<StockInDetail> stockInDetailsProperty() {
        return stockInDetails;
    }

    public static void resetProperties() {
        setId(0L);
        setTempId(-1);
        setProduct(null);
        setQuantity("");
        setSerial("");
        setDescription("");
        setLocation("");
    }

    public static void addStockInDetails() {
        var stockInDetail = StockInDetail.builder()
                .product(getProduct())
                .quantity(getQuantity())
                .serialNo(getSerial())
                .description(getDescription())
                .location(getLocation())
                .build();

        stockInDetailsList.add(stockInDetail);
        resetProperties();
    }

    public static void createStockInDetails() throws IOException, InterruptedException {
        stockInsRepository.postDetail(stockInDetailsList);

        setProductQuantity();
        Platform.runLater(stockInDetailsList::clear);
    }

    public static void getAllStockInDetails() {
        Type listType = new TypeToken<ArrayList<StockInDetail>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    stockInDetailsList.clear();

                    try {
                        ArrayList<StockInDetail> stockInDetailList = new Gson().fromJson(
                                stockInsRepository.fetchAllDetail().body(), listType);
                        stockInDetailsList.addAll(stockInDetailList);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, StockInDetailViewModel.class);
                    }
                });
    }

    public static void searchItem(String search) throws IOException, InterruptedException {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        Type listType = new TypeToken<ArrayList<StockInDetail>>() {
        }.getType();

        stockInDetailsList.clear();

        ArrayList<StockInDetail> stockInDetailList = new Gson().fromJson(
                stockInsRepository.searchDetail(searchModel).body(), listType);
        stockInDetailsList.addAll(stockInDetailList);
    }

    public static void updateStockInDetail() {
        var stockInDetail = StockInDetail.builder()
                .id(getId())
                .product(getProduct())
                .quantity(getQuantity())
                .serialNo(getSerial())
                .description(getDescription())
                .location(getLocation())
                .build();

        stockInDetailsList.remove(getTempId());
        stockInDetailsList.add(getTempId(), stockInDetail);
        resetProperties();
    }

    public static void getItem(StockInDetail stockInDetail) {
        setTempId(getStockInDetails().indexOf(stockInDetail));
        setId(stockInDetail.getId());
        setProduct(stockInDetail.getProduct());
        setQuantity(String.valueOf(stockInDetail.getQuantity()));
        setSerial(stockInDetail.getSerialNo());
        setDescription(stockInDetail.getDescription());
        setLocation(stockInDetail.getLocation());
    }

    public static void updateStockInDetails() {
        stockInDetailsList.forEach(
                stockInDetail -> {
                    try {
                        stockInsRepository.putDetail(stockInDetail);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, StockInDetailViewModel.class);
                    }
                });

        updateProductQuantity();

        getAllStockInDetails();
    }

    public static void removeStockInDetail(Long index, int tempIndex) {
        Platform.runLater(() -> stockInDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }

    public static void deleteStockInDetails(@NotNull LinkedList<Long> indexes) throws IOException, InterruptedException {
        LinkedList<FindModel> findModelList = new LinkedList<>();
        indexes.forEach(index -> findModelList.add(new FindModel(index)));
        stockInsRepository.deleteMultipleDetails(findModelList);
    }

    private static void setProductQuantity() {
        getStockInDetails()
                .forEach(
                        stockInDetail -> {
                            long productDetailQuantity =
                                    stockInDetail.getProduct().getQuantity() + stockInDetail.getQuantity();

                            ProductViewModel.setQuantity(productDetailQuantity);

                            try {
                                ProductViewModel.updateProduct();
                                createStockInTransaction(stockInDetail);
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, StockInDetailViewModel.class);
                            }
                        });
    }

    private static void updateProductQuantity() {
        getStockInDetails()
                .forEach(
                        stockInDetail -> {
                            try {
                                StockInTransaction stockInTransaction = getStockInTransaction(stockInDetail.getId());

                                Long adjustQuantity = stockInTransaction.getStockInQuantity();
                                Long currentProductQuantity = stockInDetail.getProduct().getQuantity();
                                long productQuantity =
                                        (currentProductQuantity - adjustQuantity) + stockInDetail.getQuantity();

                                ProductViewModel.setQuantity(productQuantity);

                                ProductViewModel.updateProduct();

                                updateStockInTransaction(stockInDetail);
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, StockInDetailViewModel.class);
                            }
                        });
    }

    private static StockInTransaction getStockInTransaction(Long stockInIndex) {
//        Dao<StockInTransaction, Long> stockInTransactionDao =
//                DaoManager.createDao(connectionSource, StockInTransaction.class);
//
//        PreparedQuery<StockInTransaction> preparedQuery =
//                stockInTransactionDao
//                        .queryBuilder()
//                        .where()
//                        .eq("stock_in_detail_id", stockInIndex)
//                        .prepare();
//
//        return stockInTransactionDao.queryForFirst(preparedQuery);
        return new StockInTransaction();
    }

    private static void createStockInTransaction(@NotNull StockInDetail stockInDetail) {
//        Dao<StockInTransaction, Long> stockInTransactionDao =
//                DaoManager.createDao(connectionSource, StockInTransaction.class);
//
//        StockInTransaction stockInTransaction = new StockInTransaction();
//        stockInTransaction.setBranch(stockInDetail.getStockIn().getBranch());
//        stockInTransaction.setStockInDetail(stockInDetail);
//        stockInTransaction.setProduct(stockInDetail.getProduct());
//        stockInTransaction.setStockInQuantity(stockInDetail.getQuantity());
//        stockInTransaction.setDate(new Date());
//
//        stockInTransactionDao.create(stockInTransaction);
    }

    private static void updateStockInTransaction(@NotNull StockInDetail stockInDetail) {
//        Dao<StockInTransaction, Long> stockInTransactionDao =
//                DaoManager.createDao(connectionSource, StockInTransaction.class);
//
//        StockInTransaction stockInTransaction = getStockInTransaction(stockInDetail.getId());
//        stockInTransaction.setBranch(stockInDetail.getStockIn().getBranch());
//        stockInTransaction.setStockInDetail(stockInDetail);
//        stockInTransaction.setProduct(stockInDetail.getProduct());
//        stockInTransaction.setStockInQuantity(stockInDetail.getQuantity());
//        stockInTransaction.setDate(new Date());
//
//        stockInTransactionDao.update(stockInTransaction);
    }
}
