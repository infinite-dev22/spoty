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

package org.infinite.spoty.database.management;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.Objects;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.*;

public class SQLiteTableCreator {
  private static SQLiteTableCreator instance;
    private final ConnectionSource conn;

    private SQLiteTableCreator() {
        SQLiteConnection connection = SQLiteConnection.getInstance();
        conn = connection.getConnection();
    }

  public static SQLiteTableCreator getInstance() {
    if (Objects.equals(instance, null)) instance = new SQLiteTableCreator();
    return instance;
  }

    public void createTablesIfNotExist() throws SQLException {
        TableUtils.createTableIfNotExists(conn, AdjustmentMaster.class);
        TableUtils.createTableIfNotExists(conn, AdjustmentDetail.class);
        TableUtils.createTableIfNotExists(conn, Branch.class);
        TableUtils.createTableIfNotExists(conn, Brand.class);
        TableUtils.createTableIfNotExists(conn, Company.class);
        TableUtils.createTableIfNotExists(conn, Currency.class);
        TableUtils.createTableIfNotExists(conn, Customer.class);
        TableUtils.createTableIfNotExists(conn, Expense.class);
        TableUtils.createTableIfNotExists(conn, ExpenseCategory.class);
        TableUtils.createTableIfNotExists(conn, Holiday.class);
        TableUtils.createTableIfNotExists(conn, LeaveType.class);
        TableUtils.createTableIfNotExists(conn, PaymentPurchase.class);
        TableUtils.createTableIfNotExists(conn, PaymentPurchaseReturn.class);
        TableUtils.createTableIfNotExists(conn, PaymentSale.class);
        TableUtils.createTableIfNotExists(conn, PaymentSaleReturn.class);
        TableUtils.createTableIfNotExists(conn, Permission.class);
        TableUtils.createTableIfNotExists(conn, ProductCategory.class);
        TableUtils.createTableIfNotExists(conn, ProductMaster.class);
        TableUtils.createTableIfNotExists(conn, ProductDetail.class);
        TableUtils.createTableIfNotExists(conn, ProductMaster.class);
        TableUtils.createTableIfNotExists(conn, ProductDetail.class);
        TableUtils.createTableIfNotExists(conn, PurchaseMaster.class);
        TableUtils.createTableIfNotExists(conn, PurchaseDetail.class);
        TableUtils.createTableIfNotExists(conn, PurchaseReturnMaster.class);
        TableUtils.createTableIfNotExists(conn, PurchaseReturnDetail.class);
        TableUtils.createTableIfNotExists(conn, QuotationMaster.class);
        TableUtils.createTableIfNotExists(conn, QuotationDetail.class);
        TableUtils.createTableIfNotExists(conn, RequisitionMaster.class);
        TableUtils.createTableIfNotExists(conn, RequisitionDetail.class);
        TableUtils.createTableIfNotExists(conn, Role.class);
        TableUtils.createTableIfNotExists(conn, RolePermission.class);
        TableUtils.createTableIfNotExists(conn, SaleMaster.class);
        TableUtils.createTableIfNotExists(conn, SaleDetail.class);
        TableUtils.createTableIfNotExists(conn, SaleReturnMaster.class);
        TableUtils.createTableIfNotExists(conn, SaleReturnDetail.class);
        TableUtils.createTableIfNotExists(conn, StockInMaster.class);
        TableUtils.createTableIfNotExists(conn, StockInDetail.class);
        TableUtils.createTableIfNotExists(conn, Supplier.class);
        TableUtils.createTableIfNotExists(conn, TransferMaster.class);
        TableUtils.createTableIfNotExists(conn, TransferDetail.class);
        TableUtils.createTableIfNotExists(conn, UnitOfMeasure.class);
        TableUtils.createTableIfNotExists(conn, User.class);
    }
}
