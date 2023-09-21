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

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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

    /**
     * create initial required data into database.
     * @throws SQLException returned on fail.
     */
    public void seedDatabase() throws SQLException {
        instance.runQuery(
                "INSERT OR IGNORE INTO branches ("
                        + "id,"
                        + "name,"
                        + "city,"
                        + "phone,"
                        + "town,"
                        + "created_at,"
                        + "created_by)"
                        + "VALUES ("
                        + "'1',"
                        + "'Default Branch',"
                        + "'" + Locale.getDefault().getDisplayCountry() + "',"
                        + "'+123456789012',"
                        + "'" + Locale.getDefault().getDisplayCountry() + "',"
                        + "'" + Timestamp.from(new Date().toInstant()) + "',"
                        + "'Zenmart ERP');",
                Branch.class);
        instance.runQuery(
                "INSERT OR IGNORE INTO currencies ( "
                        + "id,"
                        + "code,"
                        + "name,"
                        + "symbol,"
                        + "created_at,"
                        + "created_by)"
                        + "VALUES ("
                        + "'1',"
                        + "'" + java.util.Currency.getInstance(Locale.getDefault()).getCurrencyCode() + "',"
                        + "'" + java.util.Currency.getInstance(Locale.getDefault()).getDisplayName() + "',"
                        + "'" + java.util.Currency.getInstance(Locale.getDefault()).getSymbol() + "',"
                        + "'" + Timestamp.from(new Date().toInstant()) + "',"
                        + "'Zenmart ERP');",
                Currency.class);
        instance.runQuery(
                "INSERT OR IGNORE INTO customers ( "
                        + "id,"
                        + "name,"
                        + "phone,"
                        + "country,"
                        + "created_at,"
                        + "created_by)"
                        + "VALUES ("
                        + "'1',"
                        + "'Walk-In Customer',"
                        + "'',"
                        + "'" + Locale.getDefault().getDisplayName() + "',"
                        + "'" + Timestamp.from(new Date().toInstant()) + "',"
                        + "'Zenmart ERP');",
                Customer.class);
        instance.runQuery(
                "INSERT OR IGNORE INTO suppliers ( "
                        + "id,"
                        + "name,"
                        + "phone,"
                        + "address,"
                        + "city,"
                        + "country,"
                        + "created_at,"
                        + "created_by)"
                        + "VALUES ("
                        + "'1',"
                        + "'Walk-In Supplier',"
                        + "'',"
                        + "'',"
                        + "'',"
                        + "'" + Locale.getDefault().getDisplayName() + "',"
                        + "'" + Timestamp.from(new Date().toInstant()) + "',"
                        + "'Zenmart ERP');",
                Supplier.class);
        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                            name,
                            label,
                            description
                        )
                        VALUES (
                            'create_adjustments',
                            'Create Adjustments',
                            'User can create adjustments'
                        );

                        """,
                Permission.class);
        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                            name,
                            label,
                            description
                        )
                        VALUES (
                            'view_adjustments',
                            'View Adjustments',
                            'User can view adjustments'
                        );

                        """,
                Permission.class);
        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                            name,
                            label,
                            description
                        )
                        VALUES (
                            'edit_adjustments',
                            'Edit Adjustments',
                            'User can edit adjustments'
                        );

                        """,
                Permission.class);
        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                            name,
                            label,
                            description
                        )
                        VALUES (
                            'delete_adjustments',
                            'Delete Adjustments',
                            'User can delete adjustments'
                        );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_purchases',
                                                    'Create Purchases',
                                                    'User can create purchases'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_purchases',
                                                    'View Purchases',
                                                    'User can view purchases'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_purchases',
                                                    'Edit Purchases',
                                                    'User can edit purchases'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'delete_purchases',
                                                    'Delete Purchases',
                                                    'User can delete purchases'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_currencies',
                                                    'Create Currencies',
                                                    'User can create currencies'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_currencies',
                                                    'View Currencies',
                                                    'User can view currencies'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_currencies',
                                                    'Edit Currencies',
                                                    'User can edit currencies'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'delete_currencies',
                                                    'Delete Currencies',
                                                    'User can delete currencies'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_payments_sales_reports',
                                                    'Access Payments Sales Reports',
                                                    'User can access payments sales reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_payments_purchases_reports',
                                                    'Access Payments Purchases Reports',
                                                    'User can access payments purchases reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_sales_returns_payments_reports',
                                                    'Access Sales Returns Payments Reports',
                                                    'User can access sales returns payments reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_purchases_returns_payments_reports',
                                                    'Access Purchases Returns Payments Reports',
                                                    'User can access purchases returns payments reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_sales_reports',
                                                    'Access Sales Reports',
                                                    'User can access sales reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_purchases_reports',
                                                    'Access Purchases Reports',
                                                    'User can access purchases reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_customers_reports',
                                                    'Access Customers Reports',
                                                    'User can access customers reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_suppliers_reports',
                                                    'Access Suppliers Reports',
                                                    'User can access suppliers reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_profits_and_losses_reports',
                                                    'Access Profits And Losses Reports',
                                                    'User can access profits and losses reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_product_quantity_reports',
                                                    'Access Product Quantity Reports',
                                                    'User can access product quantity reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_branch_stock_charts_reports',
                                                    'Access Branch Stock Charts Reports',
                                                    'User can access branch stock charts reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_top_selling_products_reports',
                                                    'Access Top Selling Products Reports',
                                                    'User can access top selling products reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_customer_rankings_reports',
                                                    'Access Customer Rankings Reports',
                                                    'User can access customer rankings reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_users_reports',
                                                    'Access Users Reports',
                                                    'User can access users reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_stocks_reports',
                                                    'Access Stocks Reports',
                                                    'User can access stocks reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_products_reports',
                                                    'Access Products Reports',
                                                    'User can access products reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_product_sales_reports',
                                                    'Access Product Sales Reports',
                                                    'User can access product sales reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_product_purchases_reports',
                                                    'Access Product Purchases Reports',
                                                    'User can access product purchases reports'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_units_of_measure',
                                                    'Create Units Of Measure',
                                                    'User can create units of measure'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_units_of_measure',
                                                    'View Units Of Measure',
                                                    'User can view units of measure'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_units_of_measure',
                                                    'Edit Units Of Measure',
                                                    'User can edit units of measure'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'delete_units_of_measure',
                                                    'Delete Units Of Measure',
                                                    'User can delete units of measure'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_units_of_measure',
                                                    'Access Units Of Measure',
                                                    'User can access units of measure'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_users',
                                                    'Create Users',
                                                    'User can create users'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_users',
                                                    'View Users',
                                                    'User can view users'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_users',
                                                    'Edit Users',
                                                    'User can edit users'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'delete_users',
                                                    'Delete Users',
                                                    'User can delete users'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_expenses',
                                                    'Create Expenses',
                                                    'User can create expenses'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_expenses',
                                                    'View Expenses',
                                                    'User can view expenses'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_expenses',
                                                    'Edit Expenses',
                                                    'User can edit expenses'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'delete_expenses',
                                                    'Delete Expenses',
                                                    'User can delete expenses'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_products',
                                                    'Create Products',
                                                    'User can create products'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_products',
                                                    'View Products',
                                                    'User can view products'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_products',
                                                    'Edit Products',
                                                    'User can edit products'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'delete_products',
                                                    'Delete Products',
                                                    'User can delete products'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'import_products',
                                                    'Import Products',
                                                    'User can import products'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_product_categories',
                                                    'Create Product Categories',
                                                    'User can create product categories'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_product_categories',
                                                    'View Product Categories',
                                                    'User can view product categories'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_product_categories',
                                                    'Edit Product Categories',
                                                    'User can edit product categories'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'delete_product_categories',
                                                    'Delete Product Categories',
                                                    'User can delete product categories'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_product_categories',
                                                    'Access Product Categories',
                                                    'User can access product categories'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_payment_returns',
                                                    'Create Payment returns',
                                                    'User can create payment returns'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_payment_returns',
                                                    'View Payment returns',
                                                    'User can view payment returns'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_payment_returns',
                                                    'Edit Payment returns',
                                                    'User can edit payment returns'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'delete_payment_returns',
                                                    'Delete Payment returns',
                                                    'User can delete payment returns'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_permissions',
                                                    'Create Permissions',
                                                    'User can create permissions'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_permissions',
                                                    'View Permissions',
                                                    'User can view permissions'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_permissions',
                                                    'Edit Permissions',
                                                    'User can edit permissions'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                            name,
                            label,
                            description
                        )
                        VALUES (
                            'delete_permissions',
                            'Delete Permissions',
                            'User can delete permissions'
                        );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                   INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_payment_sales',
                                                    'Create Sale returns',
                                                    'User can create payment sales'
                                                );
                        """,
                Permission.class);
        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                            name,
                            label,
                            description
                        )
                        VALUES (
                            'view_payment_sales',
                            'View Sale returns',
                            'User can view payment sales'
                        );
                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_payment_sales',
                                                    'Edit Sale returns',
                                                    'User can edit payment sales'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'delete_payment_sales',
                                                    'Delete Sale returns',
                                                    'User can delete payment sales'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_purchase_returns',
                                                    'Create Purchase returns',
                                                    'User can create purchase returns'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                            name,
                            label,
                            description
                        )
                        VALUES (
                            'view_purchase_returns',
                            'View Purchase returns',
                            'User can view purchase returns'
                        );
                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_purchase_returns',
                                                    'Edit Purchase returns',
                                                    'User can edit purchase returns'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'delete_purchase_returns',
                                                    'Delete Purchase returns',
                                                    'User can delete purchase returns'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_pos',
                                                    'Access POS',
                                                    'User can access to a POS'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_sale_returns',
                                                    'Create Sale returns',
                                                    'User can create sale returns'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_sale_returns',
                                                    'View Sale returns',
                                                    'User can view sale returns'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_sale_returns',
                                                    'Edit Sale returns',
                                                    'User can edit sale returns'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'delete_sale_returns',
                                                    'Delete Sale returns',
                                                    'User can delete sale returns'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_customers',
                                                    'Create Customers',
                                                    'User can create customers'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_customers',
                                                    'View Customers',
                                                    'User can view customers'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_customers',
                                                    'Edit Customers',
                                                    'User can edit customers'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'delete_customers',
                                                    'Delete Customers',
                                                    'User can delete customers'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'import_customers',
                                                    'Import Customers',
                                                    'User can import customers'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_transfers',
                                                    'Create Transfers',
                                                    'User can create transfers'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_transfers',
                                                    'View Transfers',
                                                    'User can view transfers'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_transfers',
                                                    'Edit Transfers',
                                                    'User can edit transfers'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'delete_transfers',
                                                    'Delete Transfers',
                                                    'User can delete transfers'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_sales',
                                                    'Create Sales',
                                                    'User can create sales'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_sales',
                                                    'View Sales',
                                                    'User can view sales'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_sales',
                                                    'Edit Sales',
                                                    'User can edit sales'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'delete_sales',
                                                    'Delete Sales',
                                                    'User can delete sales'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_suppliers',
                                                    'Create Suppliers',
                                                    'User can create suppliers'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_suppliers',
                                                    'View Suppliers',
                                                    'User can view suppliers'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_suppliers',
                                                    'Edit Suppliers',
                                                    'User can edit suppliers'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'delete_suppliers',
                                                    'Delete Suppliers',
                                                    'User can delete suppliers'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'import_suppliers',
                                                    'Import Suppliers',
                                                    'User can import suppliers'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_brands',
                                                    'Create Brands',
                                                    'User can create brands'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_brands',
                                                    'View Brands',
                                                    'User can view brands'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_brands',
                                                    'Edit Brands',
                                                    'User can edit brands'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'delete_brands',
                                                    'Delete Brands',
                                                    'User can delete brands'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_brands',
                                                    'Access Brands',
                                                    'User can access brands'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'create_quotations',
                                                    'Create Quotations',
                                                    'User can create quotations'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_quotations',
                                                    'View Quotations',
                                                    'User can view quotations'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_quotations',
                                                    'Edit Quotations',
                                                    'User can edit quotations'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'delete_quotations',
                                                    'Delete Quotations',
                                                    'User can delete quotations'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_dashboard',
                                                    'Access Dashboard',
                                                    'User can access to a Dashboard'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_system_settings',
                                                    'View System Settings',
                                                    'User can view system settings'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_system_settings',
                                                    'Edit System Settings',
                                                    'User can edit system settings'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_currency_settings',
                                                    'Access Currency Settings',
                                                    'User can access currency settings'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'view_pos_settings',
                                                    'View POS Settings',
                                                    'User can view pos settings'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'edit_pos_settings',
                                                    'Edit POS Settings',
                                                    'User can edit pos settings'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                                                    INSERT OR IGNORE INTO permissions (
                                                    name,
                                                    label,
                                                    description
                                                )
                                                VALUES (
                                                    'access_branch_settings',
                                                    'Access Branch Settings',
                                                    'User can access branch settings'
                                                );

                        """,
                Permission.class);
        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                            name,
                            label,
                            description
                        )
                        VALUES (
                            'view_backup_settings',
                            'View Backup Settings',
                            'User can delete backup settings'
                        );""",
                Permission.class);
        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                            name,
                            label,
                            description
                        )
                        VALUES (
                            'create_backup',
                            'Create Backup',
                            'User can create backup'
                        );""",
                Permission.class);
        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                            name,
                            label,
                            description
                        )
                        VALUES (
                            'create_payment_purchases',
                            'Create Purchase returns',
                            'User can create payment purchases'
                        );""",
                Permission.class);
        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                            name,
                            label,
                            description
                        )
                        VALUES (
                            'view_payment_purchases',
                            'View Purchase returns',
                            'User can view payment purchases'
                        );""",
                Permission.class);
        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                            name,
                            label,
                            description
                        )
                        VALUES (
                            'edit_payment_purchases',
                            'Edit Purchase returns',
                            'User can edit payment purchases'
                        );""",
                Permission.class);
        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                            name,
                            label,
                            description
                        )
                        VALUES (
                            'delete_payment_purchases',
                            'Delete Purchase returns',
                            'User can delete payment purchases'
                        );""",
                Permission.class);
        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                            name,
                            label,
                            description
                        )
                        VALUES (
                            'create_branches',
                            'Create Branches',
                            'User can create branches'
                        );""",
                Permission.class);
        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                            name,
                            label,
                            description
                        )
                        VALUES (
                            'view_branches',
                            'View Branches',
                            'User can view branches'
                        );""",
                Permission.class);
        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                            name,
                            label,
                            description
                        )
                        VALUES (
                            'edit_branches',
                            'Edit Branches',
                            'User can edit branches'
                        );""",
                Permission.class);

        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                            name,
                            label,
                            description
                        )
                        VALUES (
                            'delete_branches',
                            'Delete Branches',
                            'User can delete branches'
                        );""",
                Permission.class);

        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                                  name,
                                  label,
                                  description
                        )
                        VALUES (
                                'create_requisitions',
                                'Create Requisitions',
                                'User can create requisitions'
                        );""",
                Permission.class);

        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                                  name,
                                  label,
                                  description
                        )
                        VALUES (
                                'view_requisitions',
                                'View Requisitions',
                                'User can view requisitions'
                        );""",
                Permission.class);

        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                                  name,
                                  label,
                                  description
                        )
                        VALUES (
                                'edit_requisitions',
                                'Edit Requisitions',
                                'User can edit requisitions'
                        );""",
                Permission.class);

        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                                  name,
                                  label,
                                  description
                        )
                        VALUES (
                                'delete_requisitions',
                                'Delete Requisitions',
                                'User can delete requisitions'
                        );""",
                Permission.class);

        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                                  name,
                                  label,
                                  description
                        )
                        VALUES (
                                'create_stock_ins',
                                'Create Stock Ins',
                                'User can create stock ins'
                        );""",
                Permission.class);

        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                                  name,
                                  label,
                                  description
                        )
                        VALUES (
                                'view_stock_ins',
                                'View Stock Ins',
                                'User can view stock ins'
                        );""",
                Permission.class);

        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                                  name,
                                  label,
                                  description
                        )
                        VALUES (
                                'edit_stock_ins',
                                'Edit Stock Ins',
                                'User can edit stock ins'
                        );""",
                Permission.class);

        instance.runQuery(
                """
                        INSERT OR IGNORE INTO permissions (
                                  name,
                                  label,
                                  description
                        )
                        VALUES (
                                'delete_stock_ins',
                                'Delete Stock Ins',
                                'User can delete stock ins'
                        );""",
                Permission.class);
    }

    public void createTablesIfNotExist() throws SQLException {
        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS branches (
                               id         INTEGER   PRIMARY KEY AUTOINCREMENT,
                               name       VARCHAR   NOT NULL,
                               city       VARCHAR   NOT NULL,
                               phone      VARCHAR   NOT NULL,
                               email      VARCHAR,
                               town       VARCHAR   NOT NULL,
                               created_at TIMESTAMP,
                               created_by VARCHAR,
                               updated_at TIMESTAMP,
                               updated_by VARCHAR
                           );
                        """,
                Branch.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS brands (
                               id          INTEGER   PRIMARY KEY AUTOINCREMENT,
                               name        VARCHAR   NOT NULL,
                               description VARCHAR,
                               image       BLOB,
                               created_at  TIMESTAMP,
                               created_by  VARCHAR,
                               updated_at  TIMESTAMP,
                               updated_by  VARCHAR
                           );
                        """,
                Brand.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS companies (
                            id         INTEGER   PRIMARY KEY AUTOINCREMENT,
                            name       VARCHAR   NOT NULL,
                            city       VARCHAR   NOT NULL,
                            phone      VARCHAR   NOT NULL,
                            email      VARCHAR,
                            country    VARCHAR   NOT NULL,
                            created_at TIMESTAMP,
                            created_by VARCHAR,
                            updated_at TIMESTAMP,
                            updated_by VARCHAR
                        );
                        """,
                Company.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS units_of_measure (
                            id             INTEGER   NOT NULL
                                                     PRIMARY KEY AUTOINCREMENT,
                            name           VARCHAR   NOT NULL,
                            short_name     VARCHAR,
                            base_unit_id   INTEGER,
                            operator       VARCHAR,
                            operator_value DOUBLE,
                            created_at     TIMESTAMP,
                            created_by     VARCHAR,
                            updated_at     TIMESTAMP,
                            updated_by     VARCHAR,
                            FOREIGN KEY (
                                base_unit_id
                            )
                            REFERENCES units_of_measure (id)
                        );
                        """,
                UnitOfMeasure.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS suppliers (
                            id         INTEGER   PRIMARY KEY AUTOINCREMENT,
                            name       VARCHAR   NOT NULL,
                            code       VARCHAR,
                            email      VARCHAR,
                            phone      VARCHAR   NOT NULL,
                            tax_number VARCHAR,
                            address    VARCHAR   NOT NULL,
                            city       VARCHAR   NOT NULL,
                            country    VARCHAR   NOT NULL,
                            created_at TIMESTAMP,
                            created_by VARCHAR,
                            updated_at TIMESTAMP,
                            updated_by VARCHAR
                        );
                        """,
                Supplier.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS role (
                            id          INTEGER   NOT NULL
                                                  PRIMARY KEY AUTOINCREMENT,
                            name        VARCHAR   NOT NULL,
                            label       VARCHAR,
                            status      BOOLEAN   NOT NULL,
                            description VARCHAR,
                            created_at  TIMESTAMP,
                            created_by  VARCHAR,
                            updated_at  TIMESTAMP,
                            updated_by  VARCHAR
                        );
                        """,
                Role.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS permissions (
                               id          INTEGER   NOT NULL
                                                     PRIMARY KEY AUTOINCREMENT,
                               name        VARCHAR   NOT NULL,
                               label       VARCHAR   NOT NULL,
                               description VARCHAR   NOT NULL,
                               created_at  TIMESTAMP,
                               created_by  VARCHAR,
                               updated_at  TIMESTAMP,
                               updated_by  VARCHAR,
                               UNIQUE(name, label, description)
                           );
                        """,
                Permission.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS leave_type (
                               id         INTEGER   PRIMARY KEY AUTOINCREMENT,
                               name       VARCHAR   NOT NULL,
                               created_at TIMESTAMP,
                               created_by VARCHAR,
                               updated_at TIMESTAMP,
                               updated_by VARCHAR
                           );
                        """,
                LeaveType.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS product_category (
                            id         INTEGER   PRIMARY KEY AUTOINCREMENT,
                            code       VARCHAR,
                            name       VARCHAR   NOT NULL,
                            created_at TIMESTAMP,
                            created_by VARCHAR,
                            updated_at TIMESTAMP,
                            updated_by VARCHAR
                        );
                        """,
                ProductCategory.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS currencies (
                            id         INTEGER   PRIMARY KEY AUTOINCREMENT,
                            code       VARCHAR   NOT NULL,
                            name       VARCHAR   NOT NULL,
                            symbol     VARCHAR,
                            created_at TIMESTAMP,
                            created_by VARCHAR,
                            updated_at TIMESTAMP,
                            updated_by VARCHAR
                        );
                        """,
                Currency.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS customers (
                               id         INTEGER   PRIMARY KEY AUTOINCREMENT,
                               name       VARCHAR   NOT NULL,
                               code       VARCHAR,
                               email      VARCHAR,
                               phone      VARCHAR   NOT NULL,
                               city       VARCHAR,
                               address    VARCHAR,
                               tax_number VARCHAR,
                               country    VARCHAR   NOT NULL,
                               created_at TIMESTAMP,
                               created_by VARCHAR,
                               updated_at TIMESTAMP,
                               updated_by VARCHAR
                           );
                        """,
                Customer.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS adjustment_masters (
                              id                   INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT ,
                              user_id              INTEGER     ,
                              date                 TIMESTAMP     ,
                              ref                  VARCHAR     ,
                              branch_id            INTEGER NOT NULL    ,
                              notes                VARCHAR     ,
                              status               VARCHAR     ,
                              total_amount         double,
                              created_at           TIMESTAMP     ,
                              created_by           VARCHAR     ,
                              updated_at           TIMESTAMP     ,
                              updated_by           VARCHAR     ,
                              FOREIGN KEY ( user_id ) REFERENCES users( id )  ,
                              FOREIGN KEY ( branch_id ) REFERENCES branches( id )
                         );
                        """,
                AdjustmentMaster.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS adjustment_details (
                              id                   INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT ,
                              product_id           BIGINT NOT NULL    ,
                              adjustment_master_id INTEGER     ,
                              quantity             BIGINT     ,
                              adjustment_type      VARCHAR     ,
                              created_at           TIMESTAMP     ,
                              created_by           VARCHAR     ,
                              updated_at           TIMESTAMP     ,
                              updated_by           VARCHAR     ,
                              FOREIGN KEY ( adjustment_master_id ) REFERENCES adjustment_masters( id )  ,
                              FOREIGN KEY ( product_id ) REFERENCES products( id )
                        );
                        """,
                AdjustmentDetail.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS expense_categories (
                               id          INTEGER   NOT NULL
                                                     PRIMARY KEY AUTOINCREMENT,
                               user_id     INTEGER,
                               name        VARCHAR   NOT NULL,
                               description VARCHAR,
                               created_at  TIMESTAMP,
                               created_by  VARCHAR,
                               updated_at  TIMESTAMP,
                               updated_by  VARCHAR,
                               FOREIGN KEY (
                                   user_id
                               )
                               REFERENCES users (first_name)
                           );
                        """,
                ExpenseCategory.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS expenses (
                               id                  INTEGER   NOT NULL
                                                             PRIMARY KEY AUTOINCREMENT,
                               date                TIMESTAMP NOT NULL,
                               reference_number    VARCHAR,
                               name                VARCHAR   NOT NULL,
                               user_id             INTEGER,
                               expense_category_id INTEGER   NOT NULL,
                               branch_id           INTEGER   NOT NULL,
                               details             VARCHAR,
                               amount              DOUBLE    NOT NULL,
                               created_at          TIMESTAMP,
                               created_by          VARCHAR,
                               updated_at          TIMESTAMP,
                               updated_by          VARCHAR,
                               FOREIGN KEY (
                                   user_id
                               )
                               REFERENCES users (id),
                               FOREIGN KEY (
                                   branch_id
                               )
                               REFERENCES branches (id),
                               FOREIGN KEY (
                                   expense_category_id
                               )
                               REFERENCES expense_categories (id)
                           );
                        """,
                Expense.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS holiday (
                               id          INTEGER   NOT NULL
                                                     PRIMARY KEY AUTOINCREMENT,
                               title       VARCHAR   NOT NULL,
                               company_id  INTEGER   NOT NULL,
                               start_date  TIMESTAMP,
                               end_date    TIMESTAMP,
                               description VARCHAR,
                               created_at  TIMESTAMP,
                               created_by  VARCHAR,
                               updated_at  TIMESTAMP,
                               updated_by  VARCHAR,
                               FOREIGN KEY (
                                   company_id
                               )
                               REFERENCES companies (id)
                           );
                        """,
                Holiday.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS payment_purchases (
                            id               INTEGER   NOT NULL
                                                       PRIMARY KEY AUTOINCREMENT,
                            user_id          INTEGER,
                            date             TIMESTAMP NOT NULL,
                            reference_number VARCHAR,
                            purchase_id      INTEGER   NOT NULL,
                            paymentMethod    VARCHAR   NOT NULL,
                            amount           DOUBLE    NOT NULL,
                            change           DOUBLE    NOT NULL,
                            notes            VARCHAR,
                            created_at       TIMESTAMP,
                            created_by       VARCHAR,
                            updated_at       TIMESTAMP,
                            updated_by       VARCHAR,
                            FOREIGN KEY (
                                user_id
                            )
                            REFERENCES users (id),
                            FOREIGN KEY (
                                purchase_id
                            )
                            REFERENCES purchase_masters (id)
                        );
                        """,
                PaymentPurchase.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS payments_purchase_returns (
                               id                 INTEGER   NOT NULL
                                                            PRIMARY KEY AUTOINCREMENT,
                               user_id            INTEGER,
                               date               TIMESTAMP NOT NULL,
                               reference_number   VARCHAR,
                               purchase_return_id INTEGER   NOT NULL,
                               paymentMethod      VARCHAR   NOT NULL,
                               amount             DOUBLE    NOT NULL,
                               change             DOUBLE    NOT NULL,
                               notes              VARCHAR,
                               created_at         TIMESTAMP,
                               created_by         VARCHAR,
                               updated_at         TIMESTAMP,
                               updated_by         VARCHAR,
                               FOREIGN KEY (
                                   user_id
                               )
                               REFERENCES users (id),
                               FOREIGN KEY (
                                   purchase_return_id
                               )
                               REFERENCES purchase_return_masters (id)
                           );
                        """,
                PaymentPurchaseReturn.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS payments_sale_returns (
                               id               INTEGER   NOT NULL
                                                          PRIMARY KEY AUTOINCREMENT,
                               user_id          INTEGER,
                               date             TIMESTAMP NOT NULL,
                               reference_number VARCHAR,
                               sale_return_id   INTEGER   NOT NULL,
                               paymentMethod    VARCHAR   NOT NULL,
                               amount           DOUBLE    NOT NULL,
                               change           DOUBLE    NOT NULL,
                               notes            VARCHAR,
                               created_at       TIMESTAMP,
                               created_by       VARCHAR,
                               updated_at       TIMESTAMP,
                               updated_by       VARCHAR,
                               FOREIGN KEY (
                                   user_id
                               )
                               REFERENCES users (id),
                               FOREIGN KEY (
                                   sale_return_id
                               )
                               REFERENCES sales_return_master (id)
                           );
                        """,
                PaymentSaleReturn.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS payments_sales (
                               id               INTEGER   NOT NULL
                                                          PRIMARY KEY AUTOINCREMENT,
                               user_id          INTEGER,
                               date             TIMESTAMP NOT NULL,
                               reference_number VARCHAR,
                               sale_id          INTEGER   NOT NULL,
                               payment_method   VARCHAR   NOT NULL,
                               amount           DOUBLE    NOT NULL,
                               change           DOUBLE    NOT NULL,
                               notes            VARCHAR,
                               created_at       TIMESTAMP,
                               created_by       VARCHAR,
                               updated_at       TIMESTAMP,
                               updated_by       VARCHAR,
                               FOREIGN KEY (
                                   user_id
                               )
                               REFERENCES users (id),
                               FOREIGN KEY (
                                   sale_id
                               )
                               REFERENCES sales_master (id)
                           );
                        """,
                PaymentSale.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS products (
                            id               INTEGER   NOT NULL
                                                       PRIMARY KEY AUTOINCREMENT,
                            unit_id          INTEGER,
                            category_id      INTEGER,
                            brand_id         INTEGER,
                            barcode_type     VARCHAR,
                            product_type     VARCHAR,
                            name             VARCHAR,
                            quantity         BIGINT,
                            cost             DOUBLE,
                            price            DOUBLE,
                            discount         DOUBLE,
                            net_tax          DOUBLE,
                            tax_type         VARCHAR,
                            stock_alert      BIGINT,
                            serial_number    VARCHAR,
                            image            VARCHAR,
                            created_at       TIMESTAMP,
                            created_by       VARCHAR,
                            updated_at       TIMESTAMP,
                            updated_by       VARCHAR,
                            FOREIGN KEY (
                                unit_id
                            )
                            REFERENCES units_of_measure (id),
                            FOREIGN KEY (
                                category_id
                            )
                            REFERENCES product_category (id),
                            FOREIGN KEY (
                                brand_id
                            )
                            REFERENCES brands (id)
                        );
                        """,
                Product.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS purchase_details (
                               id                 INTEGER            PRIMARY KEY AUTOINCREMENT,
                               cost               [DOUBLE PRECISION],
                               purchase_master_id INTEGER,
                               net_tax            [DOUBLE PRECISION],
                               tax_type           VARCHAR,
                               discount           [DOUBLE PRECISION],
                               discount_type      VARCHAR,
                               product_id         INTEGER            NOT NULL,
                               serial_number      VARCHAR,
                               price              [DOUBLE PRECISION],
                               quantity           BIGINT,
                               created_at         TIMESTAMP,
                               created_by         VARCHAR,
                               updated_at         TIMESTAMP,
                               updated_by         VARCHAR,
                               FOREIGN KEY (
                                   purchase_master_id
                               )
                               REFERENCES purchase_masters (id) ON DELETE CASCADE,
                               FOREIGN KEY (
                                   product_id
                               )
                               REFERENCES products (id)
                           );
                        """,
                PurchaseDetail.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS purchase_masters (
                               id             INTEGER   NOT NULL
                                                        PRIMARY KEY AUTOINCREMENT,
                               user_id        INTEGER,
                               ref            VARCHAR,
                               date           TIMESTAMP NOT NULL,
                               supplier_id    INTEGER   NOT NULL,
                               branch_id      INTEGER   NOT NULL,
                               tax_rate       DOUBLE,
                               net_tax        DOUBLE,
                               discount       DOUBLE,
                               shipping       VARCHAR,
                               paid           DOUBLE,
                               price          DOUBLE,
                               due            DOUBLE,
                               status         VARCHAR,
                               payment_status VARCHAR,
                               notes          VARCHAR,
                               created_at     TIMESTAMP,
                               created_by     VARCHAR,
                               updated_at     TIMESTAMP,
                               updated_by     VARCHAR,
                               FOREIGN KEY (
                                   user_id
                               )
                               REFERENCES users (id),
                               FOREIGN KEY (
                                   branch_id
                               )
                               REFERENCES branches (id),
                               FOREIGN KEY (
                                   supplier_id
                               )
                               REFERENCES supplier (id)
                           );
                        """,
                PurchaseMaster.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS purchase_return_details (
                               id                        INTEGER   NOT NULL
                                                                   PRIMARY KEY AUTOINCREMENT,
                               cost                      DOUBLE    NOT NULL,
                               purchase_unit_id          INTEGER   NOT NULL,
                               purchase_return_master_id INTEGER,
                               product_id                BIGINT    NOT NULL,
                               net_tax                   DOUBLE,
                               tax_type                  VARCHAR,
                               discount                  DOUBLE,
                               discount_type             VARCHAR,
                               quantity                  INTEGER   NOT NULL,
                               price                     DOUBLE    NOT NULL,
                               serial_number             VARCHAR,
                               created_at                TIMESTAMP,
                               created_by                VARCHAR,
                               updated_at                TIMESTAMP,
                               updated_by                VARCHAR,
                               FOREIGN KEY (
                                   product_id
                               )
                               REFERENCES products (id),
                               FOREIGN KEY (
                                   purchase_return_master_id
                               )
                               REFERENCES purchase_return_masters (id)
                           );
                        """,
                PurchaseReturnDetail.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS purchase_return_masters (
                               id               INTEGER   NOT NULL
                                                          PRIMARY KEY AUTOINCREMENT,
                               user_id          INTEGER,
                               reference_number VARCHAR,
                               date             TIMESTAMP NOT NULL,
                               supplier_id      INTEGER   NOT NULL,
                               branch_id        INTEGER   NOT NULL,
                               tax_rate         DOUBLE,
                               net_tax          DOUBLE,
                               discount         DOUBLE,
                               shipping         VARCHAR,
                               paid             DOUBLE,
                               price            DOUBLE,
                               status           VARCHAR,
                               payment_status   VARCHAR,
                               notes            VARCHAR,
                               created_at       TIMESTAMP,
                               created_by       VARCHAR,
                               updated_at       TIMESTAMP,
                               updated_by       VARCHAR,
                               FOREIGN KEY (
                                   user_id
                               )
                               REFERENCES users (id),
                               FOREIGN KEY (
                                   branch_id
                               )
                               REFERENCES branches (id),
                               FOREIGN KEY (
                                   supplier_id
                               )
                               REFERENCES supplier (id)
                           );
                        """,
                PurchaseReturnMaster.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS quotation_detail (
                               id            INTEGER   NOT NULL
                                                       PRIMARY KEY AUTOINCREMENT,
                               totalPrice         DOUBLE    NOT NULL,
                               sale_unit_id  INTEGER,
                               product_id    BIGINT    NOT NULL,
                               quotation_id  INTEGER,
                               net_tax       DOUBLE,
                               tax_type      VARCHAR,
                               discount      DOUBLE,
                               discount_type VARCHAR,
                               price         DOUBLE    NOT NULL,
                               quantity      BIGINT    NOT NULL,
                               serial_number VARCHAR,
                               created_at    TIMESTAMP,
                               created_by    VARCHAR,
                               updated_at    TIMESTAMP,
                               updated_by    VARCHAR,
                               FOREIGN KEY (
                                   quotation_id
                               )
                               REFERENCES quotation_master (id) ON DELETE CASCADE,
                               FOREIGN KEY (
                                   product_id
                               )
                               REFERENCES products (id),
                               FOREIGN KEY (
                                   sale_unit_id
                               )
                               REFERENCES units_of_measure (id)
                           );
                        """,
                QuotationDetail.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS quotation_master (
                            id               INTEGER   NOT NULL
                                                       PRIMARY KEY AUTOINCREMENT,
                            user_id          INTEGER,
                            date             TIMESTAMP NOT NULL,
                            reference_number VARCHAR,
                            customer_id      INTEGER   NOT NULL,
                            branch_id        INTEGER   NOT NULL,
                            shipping         VARCHAR   NOT NULL,
                            price            DOUBLE    NOT NULL,
                            status           VARCHAR   NOT NULL,
                            notes            VARCHAR,
                            created_at       TIMESTAMP,
                            created_by       VARCHAR,
                            updated_at       TIMESTAMP,
                            updated_by       VARCHAR,
                            FOREIGN KEY (
                                user_id
                            )
                            REFERENCES users (id),
                            FOREIGN KEY (
                                customer_id
                            )
                            REFERENCES customers (id),
                            FOREIGN KEY (
                                branch_id
                            )
                            REFERENCES branches (id)
                        );
                        """,
                QuotationMaster.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS requisition_detail (
                               id                INTEGER   NOT NULL
                                                           PRIMARY KEY AUTOINCREMENT,
                               product_id BIGINT    NOT NULL,
                               requisition_id    INTEGER,
                               quantity          BIGINT    NOT NULL,
                               description       VARCHAR,
                               created_at        TIMESTAMP,
                               created_by        VARCHAR,
                               updated_at        TIMESTAMP,
                               updated_by        VARCHAR,
                               FOREIGN KEY (
                                   requisition_id
                               )
                               REFERENCES requisition_master (id) ON DELETE CASCADE,
                               FOREIGN KEY (
                                   product_id
                               )
                               REFERENCES products (id)
                           );
                        """,
                RequisitionDetail.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS requisition_master (
                               id               INTEGER   NOT NULL
                                                          PRIMARY KEY AUTOINCREMENT,
                               reference_number VARCHAR,
                               date             TIMESTAMP NOT NULL,
                               user_id          INTEGER,
                               supplier_id      INTEGER   NOT NULL,
                               branch_id        INTEGER   NOT NULL,
                               ship_via         VARCHAR,
                               ship_method      VARCHAR,
                               shipping_terms   VARCHAR,
                               delivery_date    TIMESTAMP,
                               notes            VARCHAR,
                               status           VARCHAR   NOT NULL,
                               total_cost       DOUBLE    NOT NULL,
                               created_at       TIMESTAMP,
                               created_by       VARCHAR,
                               updated_at       TIMESTAMP,
                               updated_by       VARCHAR,
                               FOREIGN KEY (
                                   user_id
                               )
                               REFERENCES users (id),
                               FOREIGN KEY (
                                   supplier_id
                               )
                               REFERENCES supplier (id),
                               FOREIGN KEY (
                                   branch_id
                               )
                               REFERENCES branches (id)
                           );
                        """,
                RequisitionMaster.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS role_permission (
                               id            INTEGER NOT NULL
                                                     PRIMARY KEY AUTOINCREMENT,
                               role_id       INTEGER NOT NULL,
                               permission_id INTEGER NOT NULL,
                               FOREIGN KEY (
                                   role_id
                               )
                               REFERENCES role (id),
                               FOREIGN KEY (
                                   permission_id
                               )
                               REFERENCES permissions (id)
                           );
                        """,
                RolePermission.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS sales_detail (
                               id               INTEGER   NOT NULL
                                                          PRIMARY KEY AUTOINCREMENT,
                               reference_number VARCHAR,
                               sale_id          INTEGER,
                               product_id       BIGINT    NOT NULL,
                               serial_number    VARCHAR,
                               sub_total_price  DOUBLE    NOT NULL,
                               net_tax          DOUBLE,
                               tax_type         VARCHAR,
                               discount         DOUBLE,
                               discount_type    VARCHAR,
                               price            DOUBLE    NOT NULL,
                               quantity         BIGINT    NOT NULL,
                               created_at       TIMESTAMP,
                               created_by       VARCHAR,
                               updated_at       TIMESTAMP,
                               updated_by       VARCHAR,
                               FOREIGN KEY (
                                   sale_id
                               )
                               REFERENCES sales_master (id) ON DELETE CASCADE,
                               FOREIGN KEY (
                                   product_id
                               )
                               REFERENCES products (id)
                           );
                        """,
                SaleDetail.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS sales_master (
                            id               INTEGER   NOT NULL
                                                       PRIMARY KEY AUTOINCREMENT,
                            user_id          INTEGER,
                            date             TIMESTAMP NOT NULL,
                            reference_number VARCHAR,
                            customer_id      INTEGER   NOT NULL,
                            branch_id        INTEGER   NOT NULL,
                            tax_rate         DOUBLE,
                            net_tax          DOUBLE,
                            discount         DOUBLE,
                            total            DOUBLE    NOT NULL,
                            amountPaid       DOUBLE    NOT NULL,
                            amountDue        DOUBLE    NOT NULL,
                            paymentStatus    VARCHAR   NOT NULL,
                            saleStatus       VARCHAR   NOT NULL,
                            notes            VARCHAR,
                            created_at       TIMESTAMP,
                            created_by       VARCHAR,
                            updated_at       TIMESTAMP,
                            updated_by       VARCHAR,
                            FOREIGN KEY (
                                user_id
                            )
                            REFERENCES users (id),
                            FOREIGN KEY (
                                customer_id
                            )
                            REFERENCES customers (id),
                            FOREIGN KEY (
                                branch_id
                            )
                            REFERENCES branches (id)
                        );
                        """,
                SaleMaster.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS sales_return_detail (
                               id                     INTEGER   NOT NULL
                                                                PRIMARY KEY AUTOINCREMENT,
                               sales_return_master_id INTEGER,
                               product_id             BIGINT    NOT NULL,
                               totalPrice                  DOUBLE    NOT NULL,
                               sale_unit_id           INTEGER   NOT NULL,
                               net_tax                DOUBLE,
                               tax_type               VARCHAR,
                               discount               DOUBLE,
                               discount_type          VARCHAR,
                               serial_number          VARCHAR,
                               quantity               INTEGER   NOT NULL,
                               price                  DOUBLE    NOT NULL,
                               created_at             TIMESTAMP,
                               created_by             VARCHAR,
                               updated_at             TIMESTAMP,
                               updated_by             VARCHAR,
                               FOREIGN KEY (
                                   sales_return_master_id
                               )
                               REFERENCES sales_return_master (id) ON DELETE CASCADE,
                               FOREIGN KEY (
                                   sale_unit_id
                               )
                               REFERENCES units_of_measure (id),
                               FOREIGN KEY (
                                   product_id
                               )
                               REFERENCES products (id)
                           );
                        """,
                SaleReturnDetail.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS sales_return_master (
                               id               INTEGER   NOT NULL
                                                          PRIMARY KEY AUTOINCREMENT,
                               user_id          INTEGER,
                               date             TIMESTAMP NOT NULL,
                               reference_number VARCHAR,
                               customer_id      INTEGER   NOT NULL,
                               branch_id        INTEGER   NOT NULL,
                               tax_rate         DOUBLE,
                               net_tax          DOUBLE,
                               discount         DOUBLE,
                               price            DOUBLE    NOT NULL,
                               paid             DOUBLE    NOT NULL,
                               paymentStatus    VARCHAR   NOT NULL,
                               status           VARCHAR   NOT NULL,
                               notes            VARCHAR,
                               created_at       TIMESTAMP,
                               created_by       VARCHAR,
                               updated_at       TIMESTAMP,
                               updated_by       VARCHAR,
                               FOREIGN KEY (
                                   customer_id
                               )
                               REFERENCES customers (id),
                               FOREIGN KEY (
                                   branch_id
                               )
                               REFERENCES branches (id),
                               FOREIGN KEY (
                                   user_id
                               )
                               REFERENCES users (id)
                           );
                        """,
                SaleReturnMaster.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS stock_in_detail (
                            id                 INTEGER   NOT NULL
                                                         PRIMARY KEY AUTOINCREMENT,
                            stock_in_master_id INTEGER,
                            product_id         BIGINT    NOT NULL,
                            quantity           BIGINT    NOT NULL,
                            serial_number      VARCHAR,
                            description        VARCHAR,
                            location           VARCHAR,
                            created_at         TIMESTAMP,
                            created_by         VARCHAR,
                            updated_at         TIMESTAMP,
                            updated_by         VARCHAR,
                            FOREIGN KEY (
                                stock_in_master_id
                            )
                            REFERENCES stock_in_master (id) ON DELETE CASCADE,
                            FOREIGN KEY (
                                product_id
                            )
                            REFERENCES products (id)
                        );
                        """,
                StockInDetail.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS stock_in_master (
                            id               INTEGER   NOT NULL
                                                       PRIMARY KEY AUTOINCREMENT,
                            user_id          INTEGER,
                            reference_number VARCHAR,
                            date             TIMESTAMP NOT NULL,
                            branch_id        INTEGER   NOT NULL,
                            shipping         VARCHAR,
                            total_amount     DOUBLE    NOT NULL,
                            status           VARCHAR   NOT NULL,
                            approved_by_id   INTEGER,
                            recorded_by_id   INTEGER,
                            approval_date    TIMESTAMP,
                            record_date      TIMESTAMP,
                            notes            VARCHAR,
                            created_at       TIMESTAMP,
                            created_by       VARCHAR,
                            updated_at       TIMESTAMP,
                            updated_by       VARCHAR,
                            FOREIGN KEY (
                                user_id
                            )
                            REFERENCES users (id),
                            FOREIGN KEY (
                                branch_id
                            )
                            REFERENCES branches (id),
                            FOREIGN KEY (
                                approved_by_id
                            )
                            REFERENCES users (id),
                            FOREIGN KEY (
                                recorded_by_id
                            )
                            REFERENCES users (id)
                        );
                        """,
                StockInMaster.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS transfer_detail (
                            id            INTEGER   NOT NULL
                                                    PRIMARY KEY AUTOINCREMENT,
                            transfer_id   INTEGER,
                            product_id    BIGINT    NOT NULL,
                            quantity      BIGINT    NOT NULL,
                            serial_number VARCHAR,
                            description   VARCHAR,
                            totalPrice         DOUBLE    NOT NULL,
                            price         DOUBLE    NOT NULL,
                            created_at    TIMESTAMP,
                            created_by    VARCHAR,
                            updated_at    TIMESTAMP,
                            updated_by    VARCHAR,
                            FOREIGN KEY (
                                transfer_id
                            )
                            REFERENCES transfer_master (id) ON DELETE CASCADE,
                            FOREIGN KEY (
                                product_id
                            )
                            REFERENCES products (id)
                        );
                        """,
                TransferDetail.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS transfer_master (
                            id               INTEGER   NOT NULL
                                                       PRIMARY KEY AUTOINCREMENT,
                            user_id          INTEGER,
                            reference_number VARCHAR,
                            date             TIMESTAMP NOT NULL,
                            from_branch_id   INTEGER   NOT NULL,
                            to_branch_id     INTEGER   NOT NULL,
                            shipping         VARCHAR,
                            price            DOUBLE    NOT NULL,
                            status           VARCHAR   NOT NULL,
                            approved_by_id   INTEGER,
                            received_by_id   INTEGER,
                            approval_date    TIMESTAMP,
                            receive_date     TIMESTAMP,
                            notes            VARCHAR,
                            created_at       TIMESTAMP,
                            created_by       VARCHAR,
                            updated_at       TIMESTAMP,
                            updated_by       VARCHAR,
                            FOREIGN KEY (
                                user_id
                            )
                            REFERENCES users (id),
                            FOREIGN KEY (
                                approved_by_id
                            )
                            REFERENCES users (id),
                            FOREIGN KEY (
                                received_by_id
                            )
                            REFERENCES users (id),
                            FOREIGN KEY (
                                from_branch_id
                            )
                            REFERENCES branches (id),
                            FOREIGN KEY (
                                to_branch_id
                            )
                            REFERENCES branches (id)
                        );
                        """,
                TransferMaster.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS users (
                            id                  INTEGER   NOT NULL
                                                          PRIMARY KEY AUTOINCREMENT,
                            first_name          VARCHAR   NOT NULL,
                            last_name           VARCHAR   NOT NULL,
                            username            VARCHAR   NOT NULL,
                            email               VARCHAR,
                            password            VARCHAR,
                            phone               VARCHAR,
                            role_id             INTEGER   NOT NULL,
                            branch_id             INTEGER   NOT NULL,
                            active              BOOLEAN   NOT NULL,
                            access_all_branches BOOLEAN   NOT NULL,
                            avatar              BLOB,
                            created_at          TIMESTAMP,
                            created_by          VARCHAR,
                            updated_at          TIMESTAMP,
                            updated_by          VARCHAR,
                            CONSTRAINT unq_users_first_name UNIQUE (
                                first_name
                            ),
                            FOREIGN KEY (
                                role_id
                            )
                            REFERENCES role (id),
                            FOREIGN KEY (
                                branch_id
                            )
                            REFERENCES branches (id)
                        );
                        """,
                User.class);

        runQuery(
                """
                        CREATE UNIQUE INDEX IF NOT EXISTS unq_users_first_name ON users (
                            first_name
                        );
                        """,
                User.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS sale_transactions (
                            id                  INTEGER   NOT NULL
                                                          PRIMARY KEY AUTOINCREMENT,
                            branch_id           INTEGER   NOT NULL,
                            product_id          BIGINT    NOT NULL,
                            sale_detail_id      BIGINT    NOT NULL,
                            date                TIMESTAMP,
                            sale_quantity       BIGINT,
                            created_at          TIMESTAMP,
                            created_by          VARCHAR,
                            updated_at          TIMESTAMP,
                            updated_by          VARCHAR,
                            FOREIGN KEY (
                                product_id
                            )
                            REFERENCES products (id),
                            FOREIGN KEY (
                                sale_detail_id
                            )
                            REFERENCES sales_detail (id),
                            FOREIGN KEY (
                                branch_id
                            )
                            REFERENCES branches (id)
                        );""",
                SaleTransaction.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS adjustment_transactions (
                            id                        INTEGER   NOT NULL
                                                                PRIMARY KEY AUTOINCREMENT,
                            branch_id                 INTEGER   NOT NULL,
                            product_id                BIGINT    NOT NULL,
                            adjustment_detail_id      INTEGER   NOT NULL,
                            date                      TIMESTAMP,
                            adjust_quantity           BIGINT,
                            adjustment_type           VARCHAR,
                            created_at                TIMESTAMP,
                            created_by                VARCHAR,
                            updated_at                TIMESTAMP,
                            updated_by                VARCHAR,
                            FOREIGN KEY (
                                adjustment_detail_id
                            )
                            REFERENCES adjustment_details (id),
                            FOREIGN KEY (
                                product_id
                            )
                            REFERENCES products (id),
                            FOREIGN KEY (
                                branch_id
                            )
                            REFERENCES branches (id)
                        );""",
                AdjustmentTransaction.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS stock_in_transactions (
                            id                      INTEGER   NOT NULL
                                                              PRIMARY KEY AUTOINCREMENT,
                            branch_id               INTEGER   NOT NULL,
                            product_id              BIGINT    NOT NULL,
                            stock_in_detail_id      INTEGER   NOT NULL,
                            date                    TIMESTAMP,
                            stock_in_quantity       BIGINT,
                            created_at              TIMESTAMP,
                            created_by              VARCHAR,
                            updated_at              TIMESTAMP,
                            updated_by              VARCHAR,
                            FOREIGN KEY (
                                branch_id
                            )
                            REFERENCES branches (id),
                            FOREIGN KEY (
                                stock_in_detail_id
                            )
                            REFERENCES stock_in_detail (id),
                            FOREIGN KEY (
                                product_id
                            )
                            REFERENCES products (id)
                        );""",
                StockInTransaction.class);

        runQuery(
                """
                        CREATE TABLE IF NOT EXISTS transfer_transactions (
                            id                INTEGER   NOT NULL
                                                        PRIMARY KEY AUTOINCREMENT,
                            from_branch       INTEGER   NOT NULL,
                            to_branch         INTEGER   NOT NULL,
                            product_id        BIGINT    NOT NULL,
                            transfer_detail   INTEGER   NOT NULL,
                            date              TIMESTAMP,
                            transfer_quantity BIGINT,
                            created_at        TIMESTAMP,
                            created_by        VARCHAR,
                            updated_at        TIMESTAMP,
                            updated_by        VARCHAR,
                            FOREIGN KEY (
                                from_branch
                            )
                            REFERENCES branches (id),
                            FOREIGN KEY (
                                to_branch
                            )
                            REFERENCES branches (id),
                            FOREIGN KEY (
                                transfer_detail
                            )
                            REFERENCES transfer_detail (id),
                            FOREIGN KEY (
                                product_id
                            )
                            REFERENCES products (id)
                        );""",
                TransferTransaction.class);
    }

    private <T> void runQuery(String query, Class<T> clazz) throws SQLException {
        DaoManager.createDao(conn, clazz).executeRawNoArgs(query);
    }
}
