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

package org.infinite.spoty.viewModels.sales;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.infinite.spoty.data_source.dtos.Product;
import org.infinite.spoty.data_source.dtos.sales.SaleDetail;
import org.infinite.spoty.data_source.dtos.sales.SaleTransaction;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.SalesRepositoryImpl;
import org.infinite.spoty.utils.ParameterlessConsumer;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.viewModels.ProductViewModel;
import org.infinite.spoty.viewModels.adapters.UnixEpochDateTypeAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static org.infinite.spoty.values.SharedResources.*;

public class SaleDetailViewModel {
    @Getter
    public static final ObservableList<SaleDetail> saleDetailsList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<SaleDetail> saleDetails =
            new SimpleListProperty<>(saleDetailsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty ref = new SimpleStringProperty();
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final StringProperty serial = new SimpleStringProperty();
    private static final DoubleProperty subTotalPrice = new SimpleDoubleProperty();
    private static final StringProperty netTax = new SimpleStringProperty();
    private static final StringProperty taxType = new SimpleStringProperty();
    private static final StringProperty discount = new SimpleStringProperty();
    private static final StringProperty discountType = new SimpleStringProperty();
    private static final DoubleProperty price = new SimpleDoubleProperty();
    private static final StringProperty quantity = new SimpleStringProperty();
    private static final SalesRepositoryImpl salesRepository = new SalesRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        SaleDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getRef() {
        return ref.get();
    }

    public static void setRef(String ref) {
        SaleDetailViewModel.ref.set(ref);
    }

    public static StringProperty refProperty() {
        return ref;
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        SaleDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
        return product;
    }

    public static String getSerial() {
        return serial.get();
    }

    public static void setSerial(String serial) {
        SaleDetailViewModel.serial.set(serial);
    }

    public static StringProperty serialProperty() {
        return serial;
    }

    public static double getSubTotalPrice() {
        return subTotalPrice.get();
    }

    public static void setSubTotalPrice(double price) {
        SaleDetailViewModel.subTotalPrice.set(price);
    }

    public static DoubleProperty subTotalPriceProperty() {
        return subTotalPrice;
    }

    public static double getNetTax() {
        return (Objects.equals(netTax.get(), null) || netTax.get().isBlank())
                ? 0.0
                : Double.parseDouble(netTax.get());
    }

    public static void setNetTax(String netTax) {
        SaleDetailViewModel.netTax.set(netTax);
    }

    public static StringProperty netTaxProperty() {
        return netTax;
    }

    public static String getTaxType() {
        return taxType.get();
    }

    public static void setTaxType(String taxType) {
        SaleDetailViewModel.taxType.set(taxType);
    }

    public static StringProperty taxTypeProperty() {
        return taxType;
    }

    public static double getDiscount() {
        return (Objects.equals(discount.get(), null) || discount.get().isBlank())
                ? 0.0
                : Double.parseDouble(discount.get());
    }

    public static void setDiscount(String discount) {
        SaleDetailViewModel.discount.set(discount);
    }

    public static StringProperty discountProperty() {
        return discount;
    }

    public static String getDiscountType() {
        return discountType.get();
    }

    public static void setDiscountType(String discountType) {
        SaleDetailViewModel.discountType.set(discountType);
    }

    public static StringProperty discountTypeProperty() {
        return discountType;
    }

    public static double getPrice() {
        return price.get();
    }

    public static void setPrice(double price) {
        SaleDetailViewModel.price.set(price);
    }

    public static DoubleProperty totalProperty() {
        return price;
    }

    public static int getQuantity() {
        return Integer.parseInt(quantity.get());
    }

    public static void setQuantity(Long quantity) {
        SaleDetailViewModel.quantity.set((quantity < 1) ? "" : String.valueOf(quantity));
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static ObservableList<SaleDetail> getSaleDetails() {
        return saleDetails.get();
    }

    public static void setSaleDetails(ObservableList<SaleDetail> saleDetails) {
        SaleDetailViewModel.saleDetails.set(saleDetails);
    }

    public static ListProperty<SaleDetail> saleDetailsProperty() {
        return saleDetails;
    }

    public static void resetProperties() {
        setId(0L);
        setTempId(-1);
        setProduct(null);
        setSerial("");
        setNetTax("");
        setTaxType("");
        setDiscount("");
        setQuantity(0L);
        setDiscountType("");
        setPrice(0);
        setSubTotalPrice(0);
    }

    public static void addSaleDetail() {
        var saleDetail =
                SaleDetail.builder()
                        .product(getProduct())
                        .quantity(getQuantity())
                        .serialNumber(getSerial())
                        .netTax(getNetTax())
                        .taxType(getTaxType())
                        .discount(getDiscount())
                        .discountType(getDiscountType())
                        .price(getPrice())
                        .subTotalPrice(getSubTotalPrice())
                        .build();

        saleDetailsList.add(saleDetail);
    }

    public static void saveSaleDetails(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        saleDetailsList.forEach(saleDetail -> {
            var task = salesRepository.postDetail(saleDetail);
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
        saleDetailsList.clear();
    }

    private static void setProductQuantity() {
        saleDetailsList.forEach(
                saleDetail -> {
                    long productDetailQuantity =
                            saleDetail.getProduct().getQuantity() - saleDetail.getQuantity();

                    ProductViewModel.setQuantity(productDetailQuantity);

                    try {
                        ProductViewModel.updateProduct(null, null, null);
                        createSaleTransaction(saleDetail);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, SaleDetailViewModel.class);
                    }
                });
    }

    private static void updateProductQuantity() {
        saleDetailsList.forEach(
                saleDetail -> {
                    try {
                        SaleTransaction saleTransaction = getSaleTransaction(saleDetail.getId());

                        Long adjustQuantity = saleTransaction.getSaleQuantity();
                        Long currentProductQuantity = saleDetail.getProduct().getQuantity();
                        long productQuantity =
                                (currentProductQuantity + adjustQuantity) - saleDetail.getQuantity();

                        ProductViewModel.setQuantity(productQuantity);

                        ProductViewModel.updateProduct(null, null, null);

                        updateSaleTransaction(saleDetail);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, SaleDetailViewModel.class);
                    }
                });
    }

    public static void updateSaleDetail(Long index) {
        var saleDetail = getSaleDetails().get(Math.toIntExact(index));

        saleDetail.setProduct(getProduct());
        saleDetail.setQuantity(getQuantity());
        saleDetail.setSerialNumber(getSerial());
        saleDetail.setPrice(getPrice());
        saleDetail.setNetTax(getNetTax());
        saleDetail.setTaxType(getTaxType());
        saleDetail.setSubTotalPrice(getSubTotalPrice());
        saleDetail.setDiscount(getDiscount());
        saleDetail.setDiscountType(getDiscountType());

        getSaleDetails().remove(getTempId());
        getSaleDetails().add(getTempId(), saleDetail);
    }

    public static void getSaleDetail(SaleDetail saleDetail) {
        setTempId(getSaleDetails().indexOf(saleDetail));
        setId(saleDetail.getId());
        setProduct(saleDetail.getProduct());
        setSerial(saleDetail.getSerialNumber());
        setNetTax(String.valueOf(saleDetail.getNetTax()));
        setTaxType(saleDetail.getTaxType());
        setDiscount(String.valueOf(saleDetail.getDiscount()));
        setDiscountType(saleDetail.getDiscountType());
        setQuantity((long) saleDetail.getQuantity());
        setProduct(saleDetail.getProduct());
        setPrice(saleDetail.getPrice());
        setSubTotalPrice(saleDetail.getSubTotalPrice());
    }

    public static void updateItem(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var saleDetail =
                SaleDetail.builder()
                        .id(getId())
                        .product(getProduct())
                        .quantity(getQuantity())
                        .serialNumber(getSerial())
                        .netTax(getNetTax())
                        .taxType(getTaxType())
                        .discount(getDiscount())
                        .discountType(getDiscountType())
                        .price(getPrice())
                        .subTotalPrice(getSubTotalPrice())
                        .build();

        var task = salesRepository.putDetail(saleDetail);
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
        // getAllSaleDetails();
    }

    public static void updateSaleDetails(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        // TODO: Add endpoint to update multiple.
        saleDetailsList.forEach(
                saleDetail -> {
                    var task = salesRepository.putDetail(saleDetail);
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
        // getAllSaleDetails();
    }

    public static void getAllSaleDetails(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {

        var task = salesRepository.fetchAllDetail();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onSuccess)) {
            task.setOnFailed(workerStateEvent -> {
                Type listType = new TypeToken<ArrayList<SaleDetail>>() {
                }.getType();
                ArrayList<SaleDetail> saleDetailList = new ArrayList<>();
                try {
                    saleDetailList = gson.fromJson(
                            task.get().body(), listType);
                } catch (InterruptedException | ExecutionException e) {
                    SpotyLogger.writeToFile(e, SaleDetailViewModel.class);
                }

                saleDetailsList.clear();
                saleDetailsList.addAll(saleDetailList);
            });
            if (Objects.nonNull(onFailed)) {
                task.setOnFailed(workerStateEvent -> onFailed.run());
            }
            SpotyThreader.spotyThreadPool(task);
        }
    }

    public static void searchItem(
            String search,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();

        var task = salesRepository.searchDetail(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<SaleDetail>>() {
            }.getType();
            ArrayList<SaleDetail> saleDetailList = new ArrayList<>();
            try {
                saleDetailList = gson.fromJson(
                        task.get().body(), listType);
            } catch (Exception e) {
                SpotyLogger.writeToFile(e, SaleDetailViewModel.class);
            }

            saleDetailsList.clear();
            saleDetailsList.addAll(saleDetailList);
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void removeSaleDetail(Long index, int tempIndex) {
        Platform.runLater(() -> saleDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }

    public static void deleteSaleDetails(@NotNull LinkedList<Long> indexes,
                                         @Nullable ParameterlessConsumer onActivity,
                                         @Nullable ParameterlessConsumer onSuccess,
                                         @Nullable ParameterlessConsumer onFailed) {
        LinkedList<FindModel> findModelList = new LinkedList<>();
        indexes.forEach(index -> findModelList.add(new FindModel(index)));

        var task = salesRepository.deleteMultipleDetails(findModelList);
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

    private static SaleTransaction getSaleTransaction(Long saleIndex) {
//        Dao<SaleTransaction, Long> saleTransactionDao =
//                DaoManager.createDao(connectionSource, SaleTransaction.class);
//
//        PreparedQuery<SaleTransaction> preparedQuery =
//                saleTransactionDao
//                        .queryBuilder()
//                        .where()
//                        .eq("sale_detail_id", saleIndex)
//                        .prepare();
//
//        return saleTransactionDao.queryForFirst(preparedQuery);
        return new SaleTransaction();
    }

    private static void createSaleTransaction(@NotNull SaleDetail saleDetail) {
//        Dao<SaleTransaction, Long> saleTransactionDao =
//                DaoManager.createDao(connectionSource, SaleTransaction.class);
//
//        SaleTransaction saleTransaction = new SaleTransaction();
//        saleTransaction.setBranch(saleDetail.getSaleMaster().getBranch());
//        saleTransaction.setSaleDetail(saleDetail);
//        saleTransaction.setProduct(saleDetail.getProduct());
//        saleTransaction.setSaleQuantity(saleDetail.getQuantity());
//        saleTransaction.setDate(new Date());
//
//        saleTransactionDao.create(saleTransaction);
    }

    private static void updateSaleTransaction(@NotNull SaleDetail saleDetail) {
//        Dao<SaleTransaction, Long> saleTransactionDao =
//                DaoManager.createDao(connectionSource, SaleTransaction.class);
//
//        SaleTransaction saleTransaction = getSaleTransaction(saleDetail.getId());
//        saleTransaction.setBranch(saleDetail.getSaleMaster().getBranch());
//        saleTransaction.setSaleDetail(saleDetail);
//        saleTransaction.setProduct(saleDetail.getProduct());
//        saleTransaction.setSaleQuantity(saleDetail.getQuantity());
//        saleTransaction.setDate(new Date());
//
//        saleTransactionDao.update(saleTransaction);
    }
}
