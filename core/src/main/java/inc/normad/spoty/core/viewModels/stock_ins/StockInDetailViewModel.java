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

package inc.normad.spoty.core.viewModels.stock_ins;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.normad.spoty.core.viewModels.ProductViewModel;
import inc.normad.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import inc.normad.spoty.core.viewModels.adjustments.AdjustmentDetailViewModel;
import inc.normad.spoty.network_bridge.dtos.Product;
import inc.normad.spoty.network_bridge.dtos.stock_ins.StockInDetail;
import inc.normad.spoty.network_bridge.dtos.stock_ins.StockInMaster;
import inc.normad.spoty.network_bridge.dtos.stock_ins.StockInTransaction;
import inc.normad.spoty.network_bridge.models.FindModel;
import inc.normad.spoty.network_bridge.models.SearchModel;
import inc.normad.spoty.network_bridge.repositories.implementations.StockInsRepositoryImpl;
import inc.normad.spoty.utils.ParameterlessConsumer;
import inc.normad.spoty.utils.SpotyLogger;
import inc.normad.spoty.utils.SpotyThreader;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static inc.normad.spoty.core.values.SharedResources.*;

public class StockInDetailViewModel {
    public static final ObservableList<StockInDetail> stockInDetailsList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
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
    }

    public static void createStockInDetails(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        stockInDetailsList.forEach(stockInDetail -> {
            var task = stockInsRepository.postDetail(stockInDetail);
            if (Objects.nonNull(onActivity)) {
                task.setOnRunning(workerStateEvent -> onActivity.run());
            }
            if (Objects.nonNull(onSuccess)) {
                task.setOnFailed(workerStateEvent -> onSuccess.run());
            }
            if (Objects.nonNull(onFailed)) {
                task.setOnFailed(workerStateEvent -> onFailed.run());
            }
            SpotyThreader.spotyThreadPool(task);
        });

        setProductQuantity();
        stockInDetailsList.clear();
    }

    public static void getAllStockInDetails(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        Type listType = new TypeToken<ArrayList<StockInDetail>>() {
        }.getType();

        var task = stockInsRepository.fetchAllDetail();
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> {
            try {
                ArrayList<StockInDetail> stockInDetailList = gson.fromJson(
                        task.get().body(), listType);
                stockInDetailsList.clear();
                stockInDetailsList.addAll(stockInDetailList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, StockInDetailViewModel.class);
            }

            onSuccess.run();
        });
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();

        var task = stockInsRepository.searchMaster(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<StockInDetail>>() {
            }.getType();
            try {
                ArrayList<StockInDetail> stockInDetailList = gson.fromJson(
                        task.get().body(), listType);

                stockInDetailsList.clear();
                stockInDetailsList.addAll(stockInDetailList);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, AdjustmentDetailViewModel.class);
            }

        });
        SpotyThreader.spotyThreadPool(task);
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

    public static void updateStockInDetails(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        // TODO: Add save multiple on API.
        stockInDetailsList.forEach(
                stockInDetail -> {
                    var task = stockInsRepository.putDetail(stockInDetail);
                    if (Objects.nonNull(onActivity)) {
                        task.setOnRunning(workerStateEvent -> onActivity.run());
                    }
                    if (Objects.nonNull(onSuccess)) {
                        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
                    }
                    if (Objects.nonNull(onFailed)) {
                        task.setOnFailed(workerStateEvent -> onFailed.run());
                    }
                    SpotyThreader.spotyThreadPool(task);
                });

        updateProductQuantity();

        // getAllStockInDetails();
    }

    public static void removeStockInDetail(Long index, int tempIndex) {
        Platform.runLater(() -> stockInDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }

    public static void deleteStockInDetails(@NotNull LinkedList<Long> indexes,
                                            @Nullable ParameterlessConsumer onActivity,
                                            @Nullable ParameterlessConsumer onSuccess,
                                            @Nullable ParameterlessConsumer onFailed) {
        LinkedList<FindModel> findModelList = new LinkedList<>();
        indexes.forEach(index -> findModelList.add(new FindModel(index)));

        var task = stockInsRepository.deleteMultipleDetails(findModelList);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onSuccess)) {
            task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        SpotyThreader.spotyThreadPool(task);
    }

    private static void setProductQuantity() {
        getStockInDetails()
                .forEach(
                        stockInDetail -> {
                            long productDetailQuantity =
                                    stockInDetail.getProduct().getQuantity() + stockInDetail.getQuantity();

                            ProductViewModel.setQuantity(productDetailQuantity);

                            try {
                                ProductViewModel.updateProduct(null, null, null);
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

                                ProductViewModel.updateProduct(null, null, null);

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
