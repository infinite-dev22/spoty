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

package org.infinite.spoty.viewModels.purchases;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.infinite.spoty.data_source.dtos.Product;
import org.infinite.spoty.data_source.dtos.purchases.PurchaseDetail;
import org.infinite.spoty.data_source.dtos.purchases.PurchaseMaster;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.PurchasesRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.viewModels.adapters.UnixEpochDateTypeAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import static org.infinite.spoty.values.SharedResources.*;

public class PurchaseDetailViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    @Getter
    public static final ObservableList<PurchaseDetail> purchaseDetailsList =
            FXCollections.observableArrayList();
    private static final ListProperty<PurchaseDetail> purchaseDetails =
            new SimpleListProperty<>(purchaseDetailsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<PurchaseMaster> purchase = new SimpleObjectProperty<>(null);
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

    public static Double getCost() {
        return Double.parseDouble(cost.get());
    }

    public static void setCost(String cost) {
        PurchaseDetailViewModel.cost.set(cost);
    }

    public static StringProperty costProperty() {
        return cost;
    }

    public static Double getNetTax() {
        return Double.parseDouble(netTax.get());
    }

    public static void setNetTax(String netTax) {
        PurchaseDetailViewModel.netTax.set(netTax);
    }

    public static StringProperty netTaxProperty() {
        return netTax;
    }

    public static String getTaxType() {
        return taxType.get();
    }

    public static void setTaxType(String taxType) {
        PurchaseDetailViewModel.taxType.set(taxType);
    }

    public static StringProperty taxTypeProperty() {
        return taxType;
    }

    public static String getSerial() {
        return serial.get();
    }

    public static void setSerial(String serial) {
        PurchaseDetailViewModel.serial.set(serial);
    }

    public static StringProperty serialProperty() {
        return serial;
    }

    public static Double getDiscount() {
        return Double.parseDouble(discount.get());
    }

    public static void setDiscount(String discount) {
        PurchaseDetailViewModel.discount.set(discount);
    }

    public static StringProperty discountProperty() {
        return discount;
    }

    public static String getDiscountType() {
        return discountType.get();
    }

    public static void setDiscountType(String discountType) {
        PurchaseDetailViewModel.discountType.set(discountType);
    }

    public static StringProperty discountTypeProperty() {
        return discountType;
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

    public static Double getTotalPrice() {
        return Double.parseDouble(totalPrice.get());
    }

    public static void setTotalPrice(String totalPrice) {
        PurchaseDetailViewModel.totalPrice.set(totalPrice);
    }

    public static StringProperty totalPriceProperty() {
        return totalPrice;
    }

    public static Double getPrice() {
        return Double.parseDouble(price.get());
    }

    public static void setPrice(String price) {
        PurchaseDetailViewModel.price.set(price);
    }

    public static StringProperty priceProperty() {
        return price;
    }

    public static StringProperty totalProperty() {
        return totalPrice;
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

    public static void addPurchaseDetail() {
        var purchaseDetail = PurchaseDetail.builder()
                .cost(getCost())
                .purchase(getPurchase())
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
            purchaseDetailsList.add(purchaseDetail);
            resetProperties();
        });
    }

    public static void savePurchaseDetails() throws IOException, InterruptedException {
        // TODO: make postMultipleDetail()
        purchasesRepository.postDetail(purchaseDetailsList);
        purchaseDetailsList.clear();
    }

    public static void resetProperties() {
        setId(0L);
        setTempId(-1);
        setPurchase(null);
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

    public static void getAllPurchaseDetails() {
        Type listType = new TypeToken<ArrayList<PurchaseDetail>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    purchaseDetailsList.clear();

                    try {
                        ArrayList<PurchaseDetail> purchaseDetailList = gson.fromJson(
                                purchasesRepository.fetchAllDetail().body(), listType);
                        purchaseDetailsList.addAll(purchaseDetailList);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, PurchaseDetailViewModel.class);
                    }
                });
    }

    public static void updatePurchaseDetail(Long index) {
        PurchaseDetail oldPurchaseDetail = purchaseDetailsList.get(Math.toIntExact(index));

        var newPurchaseDetail = PurchaseDetail.builder()
                .id(oldPurchaseDetail.getId())
                .cost(getCost())
                .purchase(getPurchase())
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
                    purchaseDetailsList.remove(getTempId());
                    purchaseDetailsList.add(getTempId(), newPurchaseDetail);

                    resetProperties();
                });
    }

    public static void getItem(Long index, int tempIndex) throws IOException, InterruptedException {
        var findModel = FindModel.builder()
                .id(index)
                .build();
        var purchaseDetail = gson.fromJson(
                purchasesRepository.fetchDetail(findModel).body(),
                PurchaseDetail.class);

        setTempId(tempIndex);
        setId(purchaseDetail.getId());
        setPurchase(purchaseDetail.getPurchase());
        setCost(String.valueOf(purchaseDetail.getCost()));
        setNetTax(String.valueOf(purchaseDetail.getNetTax()));
        setTaxType(purchaseDetail.getTaxType());
        setDiscount(String.valueOf(purchaseDetail.getDiscount()));
        setDiscountType(purchaseDetail.getDiscountType());
        setProduct(purchaseDetail.getProduct());
        setSerial(purchaseDetail.getSerialNumber());
        setTotalPrice(String.valueOf(purchaseDetail.getTotal()));
        setQuantity(String.valueOf(purchaseDetail.getQuantity()));
    }

    public static void updateItem(Long index) {
        PurchaseDetail oldPurchaseDetail = purchaseDetailsList.get(Math.toIntExact(index));

        var newPurchaseDetail = PurchaseDetail.builder()
                .id(oldPurchaseDetail.getId())
                .cost(getCost())
                .purchase(getPurchase())
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
                    purchaseDetailsList.remove(getTempId());
                    purchaseDetailsList.add(getTempId(), newPurchaseDetail);

                    resetProperties();
                });
    }

    public static void searchItem(String search) throws Exception {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        Type listType = new TypeToken<ArrayList<PurchaseDetail>>() {
        }.getType();

        Platform.runLater(
                () -> {
                    purchaseDetailsList.clear();

                    try {
                        ArrayList<PurchaseDetail> purchaseDetailList = gson.fromJson(
                                purchasesRepository.searchDetail(searchModel).body(), listType);
                        purchaseDetailsList.addAll(purchaseDetailList);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, PurchaseDetailViewModel.class);
                    }
                });

    }

    public static void updatePurchaseDetails() {
        // TODO: Add save multiple on API.
        purchaseDetailsList.forEach(
                purchaseDetail -> {
                    try {
                        purchasesRepository.putDetail(purchaseDetail);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, PurchaseDetailViewModel.class);
                    }
                });
        // updateProductQuantity();
        getAllPurchaseDetails();
        getAllPurchaseDetails();
    }

    public static void removePurchaseDetail(Long index, int tempIndex) {
        Platform.runLater(() -> purchaseDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }

    public static void deletePurchaseDetails(@NotNull LinkedList<Long> indexes) throws IOException, InterruptedException {
        LinkedList<FindModel> findModelList = new LinkedList<>();
        indexes.forEach(index -> findModelList.add(new FindModel(index)));
        purchasesRepository.deleteMultipleDetails(findModelList);
    }

//    private static PurchaseTransaction getPurchaseTransaction(long purchaseIndex) {
////        PreparedQuery<PurchaseTransaction> preparedQuery =
////                purchaseTransactionDao
////                        .queryBuilder()
////                        .where()
////                        .eq("purchase_detail_id", purchaseIndex)
////                        .prepare();
//
//        // TODO: Query for purchase transaction by purchase detail id.
//
////        return purchaseTransactionDao.queryForFirst(preparedQuery);
//        return new PurchaseTransaction();
//    }
//
//    private static void createPurchaseTransaction(@NotNull PurchaseDetail purchaseDetail) {
//
//        PurchaseTransaction purchaseTransaction = new PurchaseTransaction();
//        purchaseTransaction.setBranch(purchaseDetail.getPurchase().getBranch());
//        purchaseTransaction.setPurchaseDetail(purchaseDetail);
//        purchaseTransaction.setProduct(purchaseDetail.getProduct());
//        purchaseTransaction.setAdjustQuantity(purchaseDetail.getQuantity());
//        purchaseTransaction.setPurchaseType(purchaseDetail.getPurchaseType());
//        purchaseTransaction.setDate(new Date());
//
////        purchaseTransactionDao.create(purchaseTransaction);
//        // TODO: Create purchase transaction.
//    }
//
//    private static void updatePurchaseTransaction(@NotNull PurchaseDetail purchaseDetail) {
//        PurchaseTransaction purchaseTransaction =
//                getPurchaseTransaction(purchaseDetail.getId());
//        purchaseTransaction.setBranch(purchaseDetail.getPurchase().getBranch());
//        purchaseTransaction.setPurchaseDetail(purchaseDetail);
//        purchaseTransaction.setProduct(purchaseDetail.getProduct());
//        purchaseTransaction.setAdjustQuantity(purchaseDetail.getQuantity());
//        purchaseTransaction.setPurchaseType(purchaseDetail.getPurchaseType());
//        purchaseTransaction.setDate(new Date());
//
////        purchaseTransactionDao.update(purchaseTransaction);
//        // TODO: Update purchase transaction.
//    }
}