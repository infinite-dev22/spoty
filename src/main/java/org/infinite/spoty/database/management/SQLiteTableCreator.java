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
    createTableIfNotExists(
            """
                        CREATE TABLE IF NOT EXISTS branches (
                               id         INTEGER   PRIMARY KEY AUTOINCREMENT,
                               name       VARCHAR   NOT NULL,
                               city       VARCHAR   NOT NULL,
                               phone      VARCHAR   NOT NULL,
                               email      VARCHAR,
                               town       VARCHAR   NOT NULL,
                               zip_code   VARCHAR,
                               created_at TIMESTAMP,
                               created_by VARCHAR,
                               updated_at TIMESTAMP,
                               updated_by VARCHAR
                           );
                        """,
            Branch.class);

    createTableIfNotExists(
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

    createTableIfNotExists(
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

    createTableIfNotExists(
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

    createTableIfNotExists(
            """
                        CREATE TABLE IF NOT EXISTS supplier (
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

    createTableIfNotExists(
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

    createTableIfNotExists(
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
                               updated_by  VARCHAR
                           );
                        """,
            Permission.class);

    createTableIfNotExists(
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

    createTableIfNotExists(
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

    createTableIfNotExists(
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

    createTableIfNotExists(
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

    createTableIfNotExists(
            """
                    CREATE TABLE IF NOT EXISTS adjustment_masters (
                          id                   INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT ,
                          user_id              INTEGER     ,
                          date                 TIMESTAMP     ,
                          ref                  VARCHAR     ,
                          branch_id            INTEGER NOT NULL    ,
                          notes                VARCHAR     ,
                          created_at           TIMESTAMP     ,
                          created_by           VARCHAR     ,
                          updated_at           TIMESTAMP     ,
                          updated_by           VARCHAR     ,
                          FOREIGN KEY ( user_id ) REFERENCES users( id )  ,
                          FOREIGN KEY ( branch_id ) REFERENCES branches( id )
                     );
                    """,
            AdjustmentMaster.class);

    createTableIfNotExists(
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
                          FOREIGN KEY ( product_id ) REFERENCES product_details( id )
                    );
                    """,
        AdjustmentDetail.class);

    createTableIfNotExists(
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

    createTableIfNotExists(
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

    createTableIfNotExists(
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

    createTableIfNotExists(
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

    createTableIfNotExists(
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

    createTableIfNotExists(
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

    createTableIfNotExists(
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

    createTableIfNotExists(
        """
                    CREATE TABLE IF NOT EXISTS product_details (
                        id               INTEGER   NOT NULL
                                                   PRIMARY KEY AUTOINCREMENT,
                        product_id       INTEGER,
                        branch_id        INTEGER,
                        unit_id          INTEGER,
                        sale_unit_id     INTEGER,
                        purchase_unit_id INTEGER,
                        name             VARCHAR,
                        quantity         BIGINT,
                        cost             DOUBLE,
                        price            DOUBLE,
                        net_tax          DOUBLE,
                        tax_type         VARCHAR,
                        stock_alert      BIGINT,
                        serial_number    VARCHAR,
                        created_at       TIMESTAMP,
                        created_by       VARCHAR,
                        updated_at       TIMESTAMP,
                        updated_by       VARCHAR,
                        FOREIGN KEY (
                            product_id
                        )
                        REFERENCES product_masters (id) ON DELETE CASCADE,
                        FOREIGN KEY (
                            unit_id
                        )
                        REFERENCES units_of_measure (id),
                        FOREIGN KEY (
                            sale_unit_id
                        )
                        REFERENCES units_of_measure (id),
                        FOREIGN KEY (
                            purchase_unit_id
                        )
                        REFERENCES units_of_measure (id),
                        FOREIGN KEY (
                            branch_id
                        )
                        REFERENCES branches (id)
                    );
                    """,
        ProductDetail.class);

    createTableIfNotExists(
        """
                    CREATE TABLE IF NOT EXISTS product_masters (
                           id           INTEGER   NOT NULL
                                                  PRIMARY KEY AUTOINCREMENT,
                           code         VARCHAR,
                           barcode_type VARCHAR,
                           name         VARCHAR   NOT NULL,
                           category_id  INTEGER   NOT NULL,
                           brand_id     INTEGER   NOT NULL,
                           image        BLOB,
                           note         VARCHAR,
                           not_sale     BOOLEAN   NOT NULL,
                           has_variants BOOLEAN   NOT NULL,
                           created_at   TIMESTAMP,
                           created_by   VARCHAR,
                           updated_at   TIMESTAMP,
                           updated_by   VARCHAR,
                           FOREIGN KEY (
                               brand_id
                           )
                           REFERENCES brands (id),
                           FOREIGN KEY (
                               category_id
                           )
                           REFERENCES product_category (id)
                       );
                    """,
        ProductMaster.class);

    createTableIfNotExists(
        """
                    CREATE TABLE IF NOT EXISTS purchase_details (
                           id                 INTEGER            PRIMARY KEY AUTOINCREMENT,
                           cost               [DOUBLE PRECISION],
                           purchase_master_id INTEGER            CONSTRAINT FK_NAME REFERENCES purchase_masters (id) ON DELETE CASCADE,
                           net_tax            [DOUBLE PRECISION],
                           tax_type           VARCHAR,
                           discount           [DOUBLE PRECISION],
                           discount_type      VARCHAR,
                           product_id         BIGINT             NOT NULL,
                           serial_number      VARCHAR,
                           total              [DOUBLE PRECISION],
                           quantity           BIGINT,
                           created_at         TIMESTAMP,
                           created_by         VARCHAR,
                           updated_at         TIMESTAMP,
                           updated_by         VARCHAR
                       );
                    """,
        PurchaseDetail.class);

    createTableIfNotExists(
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
                           total          DOUBLE,
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

    createTableIfNotExists(
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
                           total                     DOUBLE    NOT NULL,
                           serial_number             VARCHAR,
                           created_at                TIMESTAMP,
                           created_by                VARCHAR,
                           updated_at                TIMESTAMP,
                           updated_by                VARCHAR,
                           FOREIGN KEY (
                               product_id
                           )
                           REFERENCES product_details (id),
                           FOREIGN KEY (
                               purchase_return_master_id
                           )
                           REFERENCES purchase_return_masters (id)
                       );
                    """,
        PurchaseReturnDetail.class);

    createTableIfNotExists(
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
                           total            DOUBLE,
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

    createTableIfNotExists(
        """
                    CREATE TABLE IF NOT EXISTS quotation_detail (
                           id            INTEGER   NOT NULL
                                                   PRIMARY KEY AUTOINCREMENT,
                           price         DOUBLE    NOT NULL,
                           sale_unit_id  INTEGER,
                           product_id    BIGINT    NOT NULL,
                           quotation_id  INTEGER,
                           net_tax       DOUBLE,
                           tax_type      VARCHAR,
                           discount      DOUBLE,
                           discount_type VARCHAR,
                           total         DOUBLE    NOT NULL,
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
                           REFERENCES product_details (id),
                           FOREIGN KEY (
                               sale_unit_id
                           )
                           REFERENCES units_of_measure (id)
                       );
                    """,
        QuotationDetail.class);

    createTableIfNotExists(
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
                        total            DOUBLE    NOT NULL,
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

    createTableIfNotExists(
        """
                    CREATE TABLE IF NOT EXISTS requisition_detail (
                           id                INTEGER   NOT NULL
                                                       PRIMARY KEY AUTOINCREMENT,
                           product_detail_id BIGINT    NOT NULL,
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
                               product_detail_id
                           )
                           REFERENCES product_details (id)
                       );
                    """,
        RequisitionDetail.class);

    createTableIfNotExists(
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

    createTableIfNotExists(
        """
                    CREATE TABLE IF NOT EXISTS role_permission (
                           id            INTEGER NOT NULL
                                                 PRIMARY KEY AUTOINCREMENT,
                           role_id       INTEGER,
                           permission_id INTEGER,
                           CONSTRAINT unq_role_permission_role_id UNIQUE (
                               role_id
                           ),
                           CONSTRAINT unq_role_permission_permission_id UNIQUE (
                               permission_id
                           ),
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

    createTableIfNotExists(
        """
                    CREATE TABLE IF NOT EXISTS sales_detail (
                           id               INTEGER   NOT NULL
                                                      PRIMARY KEY AUTOINCREMENT,
                           reference_number VARCHAR,
                           sale_id          INTEGER,
                           product_id       BIGINT    NOT NULL,
                           serial_number    VARCHAR,
                           price            DOUBLE    NOT NULL,
                           net_tax          DOUBLE,
                           tax_type         VARCHAR,
                           discount         DOUBLE,
                           discount_type    VARCHAR,
                           total            DOUBLE    NOT NULL,
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
                           REFERENCES product_details (id)
                       );
                    """,
        SaleDetail.class);

    createTableIfNotExists(
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

    createTableIfNotExists(
        """
                    CREATE TABLE IF NOT EXISTS sales_return_detail (
                           id                     INTEGER   NOT NULL
                                                            PRIMARY KEY AUTOINCREMENT,
                           sales_return_master_id INTEGER,
                           product_id             BIGINT    NOT NULL,
                           price                  DOUBLE    NOT NULL,
                           sale_unit_id           INTEGER   NOT NULL,
                           net_tax                DOUBLE,
                           tax_type               VARCHAR,
                           discount               DOUBLE,
                           discount_type          VARCHAR,
                           serial_number          VARCHAR,
                           quantity               INTEGER   NOT NULL,
                           total                  DOUBLE    NOT NULL,
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
                           REFERENCES product_details (id)
                       );
                    """,
        SaleReturnDetail.class);

    createTableIfNotExists(
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
                           total            DOUBLE    NOT NULL,
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

    createTableIfNotExists(
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
                        REFERENCES product_details (id)
                    );
                    """,
        StockInDetail.class);

    createTableIfNotExists(
        """
                    CREATE TABLE IF NOT EXISTS stock_in_master (
                        id               INTEGER   NOT NULL
                                                   PRIMARY KEY AUTOINCREMENT,
                        user_id          INTEGER,
                        reference_number VARCHAR,
                        date             TIMESTAMP NOT NULL,
                        branch_id        INTEGER   NOT NULL,
                        shipping         VARCHAR,
                        total_cost       DOUBLE    NOT NULL,
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

    createTableIfNotExists(
        """
                    CREATE TABLE IF NOT EXISTS transfer_detail (
                        id            INTEGER   NOT NULL
                                                PRIMARY KEY AUTOINCREMENT,
                        transfer_id   INTEGER,
                        product_id    BIGINT    NOT NULL,
                        quantity      BIGINT    NOT NULL,
                        serial_number VARCHAR,
                        description   VARCHAR,
                        price         DOUBLE    NOT NULL,
                        total         DOUBLE    NOT NULL,
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
                        REFERENCES product_details (id)
                    );
                    """,
        TransferDetail.class);

    createTableIfNotExists(
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
                        total            DOUBLE    NOT NULL,
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

    createTableIfNotExists(
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
                        REFERENCES role (id)
                    );
                    """,
        User.class);

    createTableIfNotExists(
        """
                    CREATE UNIQUE INDEX IF NOT EXISTS unq_role_permission_permission_id ON role_permission (
                        permission_id
                    );
                    """,
        RolePermission.class);

    createTableIfNotExists(
        """
                    CREATE UNIQUE INDEX IF NOT EXISTS unq_role_permission_role_id ON role_permission (
                        role_id
                    );
                    """,
        RolePermission.class);

    createTableIfNotExists(
        """
                    CREATE UNIQUE INDEX IF NOT EXISTS unq_users_first_name ON users (
                        first_name
                    );
                    """,
        User.class);
  }

  private <T> void createTableIfNotExists(String query, Class<T> clazz) throws SQLException {
    DaoManager.createDao(conn, clazz).executeRawNoArgs(query);
  }
}
