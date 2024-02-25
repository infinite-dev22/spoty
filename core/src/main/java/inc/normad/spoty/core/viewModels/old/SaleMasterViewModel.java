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

package inc.normad.spoty.core.viewModels.old;

import inc.normad.spoty.network_bridge.dtos.Branch;
import inc.normad.spoty.network_bridge.dtos.Customer;
import inc.normad.spoty.network_bridge.dtos.sales.SaleMaster;
import inc.normad.spoty.utils.SpotyLogger;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static inc.normad.spoty.core.values.SharedResources.PENDING_DELETES;

public class SaleMasterViewModel {
    @Getter
    public static final ObservableList<SaleMaster> saleMasterList =
            FXCollections.observableArrayList();
    private static final ListProperty<SaleMaster> sales = new SimpleListProperty<>(saleMasterList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final StringProperty ref = new SimpleStringProperty("");
    private static final ObjectProperty<Customer> customer = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty saleStatus = new SimpleStringProperty("");
    private static final DoubleProperty total = new SimpleDoubleProperty();
    private static final DoubleProperty paid = new SimpleDoubleProperty();
    private static final StringProperty payStatus = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        SaleMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static @Nullable Date getDate() {
        try {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());

            if (Objects.equals(date.get(), "")) {
                setDate(dateFormat.format(new Date()));
            }

            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, SaleMasterViewModel.class);
        }
        return null;
    }

    public static void setDate(String date) {
        SaleMasterViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
    }

    public static Customer getCustomer() {
        return customer.get();
    }

    public static void setCustomer(Customer customer) {
        SaleMasterViewModel.customer.set(customer);
    }

    public static ObjectProperty<Customer> customerProperty() {
        return customer;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        SaleMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getSaleStatus() {
        return saleStatus.get();
    }

    public static void setSaleStatus(String saleStatus) {
        SaleMasterViewModel.saleStatus.set(saleStatus);
    }

    public static StringProperty saleStatusProperty() {
        return saleStatus;
    }

    public static String getPayStatus() {
        return payStatus.get();
    }

    public static void setPayStatus(String payStatus) {
        SaleMasterViewModel.payStatus.set(payStatus);
    }

    public static StringProperty payStatusProperty() {
        return payStatus;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        SaleMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static ObservableList<SaleMaster> getSales() {
        return sales.get();
    }

    public static void setSales(ObservableList<SaleMaster> sales) {
        SaleMasterViewModel.sales.set(sales);
    }

    public static ListProperty<SaleMaster> salesProperty() {
        return sales;
    }

    public static double getTotal() {
        return total.get();
    }

    public static void setTotal(double total) {
        SaleMasterViewModel.total.set(total);
    }

    public static DoubleProperty totalProperty() {
        return total;
    }

    public static double getPaid() {
        return paid.get();
    }

    public static void setPaid(double paid) {
        SaleMasterViewModel.paid.set(paid);
    }

    public static DoubleProperty paidProperty() {
        return paid;
    }

    public static void resetProperties() {
        Platform.runLater(
                () -> {
                    setId(0);
                    setDate("");
                    setCustomer(null);
                    setBranch(null);
                    setSaleStatus("");
                    setNote("");
                    setTotal(0);
                    setPaid(0);
                    PENDING_DELETES.clear();
                    SaleDetailViewModel.resetProperties();
                });
    }

    public static void saveSaleMaster() throws Exception {
//        Dao<SaleMaster, Long> saleMasterDao = DaoManager.createDao(connectionSource, SaleMaster.class);
//
//        SaleMaster saleMaster =
//                new SaleMaster(
//                        getCustomer(),
//                        getBranch(),
//                        getTotal(),
//                        getPaid(),
//                        getSaleStatus(),
//                        getPayStatus(),
//                        getNote(),
//                        getDate());
//
//        if (!SaleDetailViewModel.saleDetailList.isEmpty()) {
//            SaleDetailViewModel.saleDetailList.forEach(
//                    saleDetail -> saleDetail.setSaleMaster(saleMaster));
//
//            saleMaster.setSaleDetails(SaleDetailViewModel.getSaleDetailList());
//        }
//
//        saleMasterDao.create(saleMaster);
        SaleDetailViewModel.saveSaleDetails();

        Platform.runLater(SaleMasterViewModel::resetProperties);
        getSaleMasters();
    }

    public static void getSaleMasters() throws Exception {
//        Dao<SaleMaster, Long> saleMasterDao = DaoManager.createDao(connectionSource, SaleMaster.class);
//
//        Platform.runLater(
//                () -> {
//                    saleMasterList.clear();
//
//                    try {
//                        saleMasterList.addAll(saleMasterDao.queryForAll());
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, SaleMasterViewModel.class);
//                    }
//                });
    }

    public static void getItem(long index) throws Exception {
//        Dao<SaleMaster, Long> saleMasterDao = DaoManager.createDao(connectionSource, SaleMaster.class);
//
//        SaleMaster saleMaster = saleMasterDao.queryForId(index);
//
//        Platform.runLater(
//                () -> {
//                    setId(saleMaster.getId());
//                    setDate(saleMaster.getLocaleDate());
//                    setCustomer(saleMaster.getCustomer());
//                    setBranch(saleMaster.getBranch());
//                    setNote(saleMaster.getNotes());
//                    setSaleStatus(saleMaster.getSaleStatus());
//                    setPayStatus(saleMaster.getPaymentStatus());
//
//                    SaleDetailViewModel.saleDetailList.clear();
//                    SaleDetailViewModel.saleDetailList.addAll(saleMaster.getSaleDetails());
//                });

        getSaleMasters();
    }

    public static void updateItem(long index) throws Exception {
//        Dao<SaleMaster, Long> saleMasterDao = DaoManager.createDao(connectionSource, SaleMaster.class);
//
//        SaleMaster saleMaster = saleMasterDao.queryForId(index);
//        saleMaster.setCustomer(getCustomer());
//        saleMaster.setBranch(getBranch());
//        saleMaster.setSaleStatus(getSaleStatus());
//        saleMaster.setPaymentStatus(getPayStatus());
//        saleMaster.setNotes(getNote());
//        saleMaster.setDate(getDate());
//
//        SaleDetailViewModel.deleteSaleDetails(PENDING_DELETES);
//        saleMaster.setSaleDetails(SaleDetailViewModel.getSaleDetailList());
//
//        saleMasterDao.update(saleMaster);
        SaleDetailViewModel.updateSaleDetails();

        Platform.runLater(SaleMasterViewModel::resetProperties);
        getSaleMasters();
    }

    public static void deleteItem(long index) throws Exception {
//        Dao<SaleMaster, Long> saleMasterDao = DaoManager.createDao(connectionSource, SaleMaster.class);
//
//        saleMasterDao.deleteById(index);

        getSaleMasters();
    }
}
