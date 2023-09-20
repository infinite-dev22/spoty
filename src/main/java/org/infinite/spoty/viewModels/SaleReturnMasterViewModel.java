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

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.SaleReturnMaster;
import org.infinite.spoty.utils.SpotyLogger;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaleReturnMasterViewModel {
    public static final ObservableList<SaleReturnMaster> saleReturnMasterList =
            FXCollections.observableArrayList();
    private static final ListProperty<SaleReturnMaster> saleReturns = new SimpleListProperty<>(saleReturnMasterList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty totalCost = new SimpleStringProperty("");
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
    private static final SQLiteConnection connection = SQLiteConnection.getInstance();
    private static final ConnectionSource connectionSource = connection.getConnection();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        SaleReturnMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static @Nullable Date getDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, SaleReturnMasterViewModel.class);
        }
        return null;
    }

    public static void setDate(String date) {
        SaleReturnMasterViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        SaleReturnMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        SaleReturnMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        SaleReturnMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static double getTotalCost() {
        return Double.parseDouble(!totalCost.get().isEmpty() ? totalCost.get() : "0");
    }

    public static void setTotalCost(String totalCost) {
        SaleReturnMasterViewModel.totalCost.set(totalCost);
    }

    public static StringProperty totalCostProperty() {
        return totalCost;
    }

    public static ObservableList<SaleReturnMaster> getSaleReturns() {
        return saleReturns.get();
    }

    public static void setSaleReturns(ObservableList<SaleReturnMaster> saleReturns) {
        SaleReturnMasterViewModel.saleReturns.set(saleReturns);
    }

    public static ListProperty<SaleReturnMaster> saleReturnsProperty() {
        return saleReturns;
    }

    public static void resetProperties() {
        setId(0);
        setDate("");
        setBranch(null);
        setNote("");
        setStatus("");
        setTotalCost("");
    }

    public static void getSaleReturnMasters() throws SQLException {
        Dao<SaleReturnMaster, Long> saleReturnMasterDao =
                DaoManager.createDao(connectionSource, SaleReturnMaster.class);

        Platform.runLater(
                () -> {
                    saleReturnMasterList.clear();

                    try {
                        saleReturnMasterList.addAll(saleReturnMasterDao.queryForAll());
                    } catch (SQLException e) {
                        SpotyLogger.writeToFile(e, SaleReturnMasterViewModel.class);
                    }
                });
    }

    public static ObservableList<SaleReturnMaster> getSaleReturnMasterList() {
        return saleReturnMasterList;
    }
}
