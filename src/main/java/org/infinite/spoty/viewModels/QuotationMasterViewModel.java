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

package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.QuotationMasterDao;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.Customer;
import org.infinite.spoty.database.models.QuotationMaster;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.infinite.spoty.values.SharedResources.PENDING_DELETES;

public class QuotationMasterViewModel {
    public static final ObservableList<QuotationMaster> quotationMasterList = FXCollections.observableArrayList();
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final ObjectProperty<Customer> customer = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        QuotationMasterViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static Date getDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setDate(String date) {
        QuotationMasterViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        QuotationMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        QuotationMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static Customer getCustomer() {
        return customer.get();
    }

    public static void setCustomer(Customer customer) {
        QuotationMasterViewModel.customer.set(customer);
    }

    public static ObjectProperty<Customer> customerProperty() {
        return customer;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        QuotationMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static void resetProperties() {
        setId(0);
        setDate("");
        setCustomer(null);
        setBranch(null);
        setStatus("");
        setNote("");
        PENDING_DELETES.clear();
        QuotationDetailViewModel.quotationDetailTempList.clear();
    }

    public static void saveQuotationMaster() {
        QuotationMaster quotationMaster = new QuotationMaster(getDate(), getCustomer(), getBranch(), getStatus(), getNote());
        if (!QuotationDetailViewModel.quotationDetailTempList.isEmpty()) {
            QuotationDetailViewModel.quotationDetailTempList.forEach(
                    quotationDetail -> quotationDetail.setQuotation(quotationMaster)
            );
            quotationMaster.setQuotationDetails(QuotationDetailViewModel.quotationDetailTempList);
        }
        QuotationMasterDao.saveQuotationMaster(quotationMaster);
        resetProperties();
        getQuotationMasters();
    }

    public static ObservableList<QuotationMaster> getQuotationMasters() {
        quotationMasterList.clear();
        quotationMasterList.addAll(QuotationMasterDao.fetchQuotationMasters());
        return quotationMasterList;
    }

    public static void getItem(int index) {
        QuotationMaster quotationMaster = QuotationMasterDao.findQuotationMaster(index);
        setId(quotationMaster.getId());
        setDate(quotationMaster.getLocaleDate());
        setCustomer(quotationMaster.getCustomer());
        setBranch(quotationMaster.getBranch());
        setStatus(quotationMaster.getStatus());
        setNote(quotationMaster.getNotes());
        QuotationDetailViewModel.quotationDetailTempList.addAll(quotationMaster.getQuotationDetails());
        getQuotationMasters();
    }

    public static void updateItem(int index) {
        QuotationMaster quotationMaster = QuotationMasterDao.findQuotationMaster(index);
        quotationMaster.setDate(getDate());
        quotationMaster.setCustomer(getCustomer());
        quotationMaster.setBranch(getBranch());
        quotationMaster.setStatus(getStatus());
        quotationMaster.setNotes(getNote());
        QuotationDetailViewModel.deleteQuotationDetails(PENDING_DELETES);
        quotationMaster.setQuotationDetails(QuotationDetailViewModel.quotationDetailTempList);
        QuotationMasterDao.updateQuotationMaster(quotationMaster, index);
        resetProperties();
        getQuotationMasters();
    }
}
