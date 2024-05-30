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

package inc.nomard.spoty.core.viewModels.sales;

import com.google.gson.*;
import com.google.gson.reflect.*;

import static inc.nomard.spoty.core.values.SharedResources.*;

import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.sales.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.*;
import lombok.extern.java.Log;

@Log
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
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        saleDetailsList.forEach(saleDetail -> {
            var task = salesRepository.postDetail(saleDetail);
            if (Objects.nonNull(onActivity)) {
                task.setOnRunning(workerStateEvent -> onActivity.run());
            }
            if (Objects.nonNull(onSuccess)) {
                task.setOnFailed(workerStateEvent -> onSuccess.run());
            }
            if (Objects.nonNull(onFailed)) {
                task.setOnFailed(workerStateEvent -> {
                    onFailed.run();
                    System.err.println("The task failed with the following exception:");
                    task.getException().printStackTrace(System.err);
                });
            }
            SpotyThreader.spotyThreadPool(task);
        });
        saleDetailsList.clear();
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

    public static void updatePosSale(Long index) {
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

        getSaleDetails().set(Math.toIntExact(index), saleDetail);
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

    public static void getPosSale(SaleDetail saleDetail) {
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
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
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
            task.setOnFailed(workerStateEvent -> {
                onFailed.run();
                System.err.println("The task failed with the following exception:");
                task.getException().printStackTrace(System.err);
            });
        }
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateSaleDetails(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
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
                        task.setOnFailed(workerStateEvent -> {
                            onFailed.run();
                            System.err.println("The task failed with the following exception:");
                            task.getException().printStackTrace(System.err);
                        });
                    }
                    SpotyThreader.spotyThreadPool(task);
                });
    }

    public static void getAllSaleDetails(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {

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
                task.setOnFailed(workerStateEvent -> {
                    onFailed.run();
                    System.err.println("The task failed with the following exception:");
                    task.getException().printStackTrace(System.err);
                });
            }
            SpotyThreader.spotyThreadPool(task);
        }
    }

    public static void searchItem(
            String search,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();

        var task = salesRepository.searchDetail(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> {
                onFailed.run();
                System.err.println("The task failed with the following exception:");
                task.getException().printStackTrace(System.err);
            });
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

    public static void deleteSaleDetails(LinkedList<Long> indexes,
                                         ParameterlessConsumer onActivity,
                                         ParameterlessConsumer onSuccess,
                                         ParameterlessConsumer onFailed) {
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
}
