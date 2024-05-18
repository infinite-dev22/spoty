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

package inc.nomard.spoty.core.viewModels.requisitions;

import com.google.gson.*;
import com.google.gson.reflect.*;
import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.requisitions.*;
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

public class RequisitionDetailViewModel {
    @Getter
    public static final ObservableList<RequisitionDetail> requisitionDetailsList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<RequisitionDetail> requisitionDetails =
            new SimpleListProperty<>(requisitionDetailsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<RequisitionMaster> requisition = new SimpleObjectProperty<>(null);
    private static final StringProperty cost = new SimpleStringProperty("");
    private static final StringProperty netTax = new SimpleStringProperty("");
    private static final StringProperty taxType = new SimpleStringProperty("");
    private static final StringProperty discount = new SimpleStringProperty("");
    private static final StringProperty discountType = new SimpleStringProperty("");
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>(null);
    private static final StringProperty serial = new SimpleStringProperty("");
    private static final StringProperty price = new SimpleStringProperty("");
    private static final StringProperty totalPrice = new SimpleStringProperty("");
    private static final StringProperty quantity = new SimpleStringProperty("");
    private static final RequisitionsRepositoryImpl requisitionsRepository = new RequisitionsRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        RequisitionDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static RequisitionMaster getRequisition() {
        return requisition.get();
    }

    public static void setRequisition(RequisitionMaster requisition) {
        RequisitionDetailViewModel.requisition.set(requisition);
    }

    public static ObjectProperty<RequisitionMaster> requisitionProperty() {
        return requisition;
    }

    public static Integer getQuantity() {
        return Integer.parseInt(quantity.get());
    }

    public static void setQuantity(String quantity) {
        RequisitionDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static Double getCost() {
        return Double.parseDouble(cost.get());
    }

    public static void setCost(String cost) {
        RequisitionDetailViewModel.cost.set(cost);
    }

    public static StringProperty costProperty() {
        return cost;
    }

    public static Double getNetTax() {
        return Double.parseDouble(netTax.get());
    }

    public static void setNetTax(String netTax) {
        RequisitionDetailViewModel.netTax.set(netTax);
    }

    public static StringProperty netTaxProperty() {
        return netTax;
    }

    public static String getTaxType() {
        return taxType.get();
    }

    public static void setTaxType(String taxType) {
        RequisitionDetailViewModel.taxType.set(taxType);
    }

    public static StringProperty taxTypeProperty() {
        return taxType;
    }

    public static String getSerial() {
        return serial.get();
    }

    public static void setSerial(String serial) {
        RequisitionDetailViewModel.serial.set(serial);
    }

    public static StringProperty serialProperty() {
        return serial;
    }

    public static Double getDiscount() {
        return Double.parseDouble(discount.get());
    }

    public static void setDiscount(String discount) {
        RequisitionDetailViewModel.discount.set(discount);
    }

    public static StringProperty discountProperty() {
        return discount;
    }

    public static String getDiscountType() {
        return discountType.get();
    }

    public static void setDiscountType(String discountType) {
        RequisitionDetailViewModel.discountType.set(discountType);
    }

    public static StringProperty discountTypeProperty() {
        return discountType;
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        RequisitionDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
        return product;
    }

    public static Double getTotalPrice() {
        return Double.parseDouble(totalPrice.get());
    }

    public static void setTotalPrice(String totalPrice) {
        RequisitionDetailViewModel.totalPrice.set(totalPrice);
    }

    public static StringProperty totalPriceProperty() {
        return totalPrice;
    }

    public static Double getPrice() {
        return Double.parseDouble(price.get());
    }

    public static void setPrice(String price) {
        RequisitionDetailViewModel.price.set(price);
    }

    public static StringProperty priceProperty() {
        return price;
    }

    public static StringProperty totalProperty() {
        return totalPrice;
    }

    public static ObservableList<RequisitionDetail> getRequisitionDetails() {
        return requisitionDetails.get();
    }

    public static void setRequisitionDetails(ObservableList<RequisitionDetail> requisitionDetails) {
        RequisitionDetailViewModel.requisitionDetails.set(requisitionDetails);
    }

    public static ListProperty<RequisitionDetail> requisitionDetailsProperty() {
        return requisitionDetails;
    }

    public static void addRequisitionDetail() {
        var requisitionDetail = RequisitionDetail.builder()
                .cost(getCost())
                .requisition(getRequisition())
                .netTax(getNetTax())
                .taxType(getTaxType())
                .discount(getDiscount())
                .discountType(getDiscountType())
                .product(getProduct())
                .serialNumber(getSerial())
                .price(getPrice())
                .total(getTotalPrice())
                .quantity(getQuantity())
                .build();

        Platform.runLater(() -> {
            requisitionDetailsList.add(requisitionDetail);

        });
    }

    public static void saveRequisitionDetails(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        // TODO: make postMultipleDetail()
        requisitionDetailsList.forEach(requisitionDetails -> {
            var task = requisitionsRepository.postDetail(requisitionDetailsList);
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
        requisitionDetailsList.clear();
    }

    public static void resetProperties() {
        setId(0L);
        setTempId(-1);
        setRequisition(null);
        setProduct(null);
        setCost("");
        setNetTax("");
        setTaxType(null);
        setDiscount("");
        setDiscountType(null);
        setSerial(null);
        setTotalPrice("");
        setQuantity("");
    }

    public static void getAllRequisitionDetails(ParameterlessConsumer onActivity) {
        Type listType = new TypeToken<ArrayList<RequisitionDetail>>() {
        }.getType();

        var task = requisitionsRepository.fetchAllDetail();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            ArrayList<RequisitionDetail> requisitionDetailList = new ArrayList<>();
            try {
                requisitionDetailList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, RequisitionDetailViewModel.class);
            }
            requisitionDetailsList.clear();
            requisitionDetailsList.addAll(requisitionDetailList);
        });
    }

    public static void updateRequisitionDetail(Long index) {
        RequisitionDetail oldRequisitionDetail = requisitionDetailsList.get(Math.toIntExact(index));

        var newRequisitionDetail = RequisitionDetail.builder()
                .id(oldRequisitionDetail.getId())
                .cost(getCost())
                .requisition(getRequisition())
                .netTax(getNetTax())
                .taxType(getTaxType())
                .discount(getDiscount())
                .discountType(getDiscountType())
                .product(getProduct())
                .serialNumber(getSerial())
                .price(getPrice())
                .total(getTotalPrice())
                .quantity(getQuantity())
                .build();

        Platform.runLater(
                () -> {
                    requisitionDetailsList.remove(getTempId());
                    requisitionDetailsList.add(getTempId(), newRequisitionDetail);


                });
    }

    public static void getItem(Long index, int tempIndex, ParameterlessConsumer onActivity, ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder()
                .id(index)
                .build();

        var task = requisitionsRepository.fetchDetail(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            RequisitionDetail requisitionDetail = new RequisitionDetail();
            try {
                requisitionDetail = gson.fromJson(
                        task.get().body(),
                        RequisitionDetail.class);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, RequisitionDetailViewModel.class);
            }

            setTempId(tempIndex);
            setId(requisitionDetail.getId());
            setRequisition(requisitionDetail.getRequisition());
            setCost(String.valueOf(requisitionDetail.getCost()));
            setNetTax(String.valueOf(requisitionDetail.getNetTax()));
            setTaxType(requisitionDetail.getTaxType());
            setDiscount(String.valueOf(requisitionDetail.getDiscount()));
            setDiscountType(requisitionDetail.getDiscountType());
            setProduct(requisitionDetail.getProduct());
            setSerial(requisitionDetail.getSerialNumber());
            setTotalPrice(String.valueOf(requisitionDetail.getTotal()));
            setQuantity(String.valueOf(requisitionDetail.getQuantity()));
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(Long index) {
        RequisitionDetail oldRequisitionDetail = requisitionDetailsList.get(Math.toIntExact(index));

        var newRequisitionDetail = RequisitionDetail.builder()
                .id(oldRequisitionDetail.getId())
                .cost(getCost())
                .requisition(getRequisition())
                .netTax(getNetTax())
                .taxType(getTaxType())
                .discount(getDiscount())
                .discountType(getDiscountType())
                .product(getProduct())
                .serialNumber(getSerial())
                .price(getPrice())
                .total(getTotalPrice())
                .quantity(getQuantity())
                .build();

        Platform.runLater(
                () -> {
                    requisitionDetailsList.remove(getTempId());
                    requisitionDetailsList.add(getTempId(), newRequisitionDetail);

                });
    }

    public static void searchItem(String search, ParameterlessConsumer onActivity, ParameterlessConsumer onFailed) {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        var task = requisitionsRepository.searchDetail(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<RequisitionDetail>>() {
            }.getType();

            Platform.runLater(
                    () -> {
                        requisitionDetailsList.clear();

                        try {
                            ArrayList<RequisitionDetail> requisitionDetailList = gson.fromJson(
                                    task.get().body(), listType);
                            requisitionDetailsList.addAll(requisitionDetailList);
                        } catch (Exception e) {
                            SpotyLogger.writeToFile(e, RequisitionDetailViewModel.class);
                        }
                    });
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateRequisitionDetails(ParameterlessConsumer onActivity, ParameterlessConsumer onSuccess, ParameterlessConsumer onFailed) {
        // TODO: Add save multiple on API.
        requisitionDetailsList.forEach(
                requisitionDetail -> {
                    try {
                        var task = requisitionsRepository.putDetail(requisitionDetail);
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
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, RequisitionDetailViewModel.class);
                    }
                });
        // updateProductQuantity();
        // getAllRequisitionDetails();
        // getAllRequisitionDetails();
    }

    public static void removeRequisitionDetail(Long index, int tempIndex) {
        Platform.runLater(() -> requisitionDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }

    public static void deleteRequisitionDetails(LinkedList<Long> indexes, ParameterlessConsumer onActivity, ParameterlessConsumer onSuccess, ParameterlessConsumer onFailed) {
        LinkedList<FindModel> findModelList = new LinkedList<>();
        indexes.forEach(index -> findModelList.add(new FindModel(index)));

        var task = requisitionsRepository.deleteMultipleDetails(findModelList);
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

//    private static RequisitionTransaction getRequisitionTransaction(long requisitionIndex) {
////        PreparedQuery<RequisitionTransaction> preparedQuery =
////                requisitionTransactionDao
////                        .queryBuilder()
////                        .where()
////                        .eq("requisition_detail_id", requisitionIndex)
////                        .prepare();
//
//        // TODO: Query for requisition transaction by requisition detail id.
//
////        return requisitionTransactionDao.queryForFirst(preparedQuery);
//        return new RequisitionTransaction();
//    }
//
//    private static void createRequisitionTransaction(RequisitionDetail requisitionDetail) {
//
//        RequisitionTransaction requisitionTransaction = new RequisitionTransaction();
//        requisitionTransaction.setBranch(requisitionDetail.getRequisition().getBranch());
//        requisitionTransaction.setRequisitionDetail(requisitionDetail);
//        requisitionTransaction.setProduct(requisitionDetail.getProduct());
//        requisitionTransaction.setAdjustQuantity(requisitionDetail.getQuantity());
//        requisitionTransaction.setRequisitionType(requisitionDetail.getRequisitionType());
//        requisitionTransaction.setDate(new Date());
//
////        requisitionTransactionDao.create(requisitionTransaction);
//        // TODO: Create requisition transaction.
//    }
//
//    private static void updateRequisitionTransaction(RequisitionDetail requisitionDetail) {
//        RequisitionTransaction requisitionTransaction =
//                getRequisitionTransaction(requisitionDetail.getId());
//        requisitionTransaction.setBranch(requisitionDetail.getRequisition().getBranch());
//        requisitionTransaction.setRequisitionDetail(requisitionDetail);
//        requisitionTransaction.setProduct(requisitionDetail.getProduct());
//        requisitionTransaction.setAdjustQuantity(requisitionDetail.getQuantity());
//        requisitionTransaction.setRequisitionType(requisitionDetail.getRequisitionType());
//        requisitionTransaction.setDate(new Date());
//
////        requisitionTransactionDao.update(requisitionTransaction);
//        // TODO: Update requisition transaction.
//    }
}
