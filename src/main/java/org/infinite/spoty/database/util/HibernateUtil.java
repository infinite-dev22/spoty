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

package org.infinite.spoty.database.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.infinite.spoty.database.models.*;

public class HibernateUtil {
  private static SessionFactory sessionFactory;

  public static SessionFactory getSessionFactory() {
    java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
    try {
      Files.createDirectories(
          Paths.get(
              System.getProperty("user.home") + "/.config/ZenmartERP/datastores/databases/derby"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    if (sessionFactory == null) {
      try {
        Configuration configuration = new Configuration();

        // Hibernate Settings
        Properties settings = new Properties();
        settings.put(Environment.USER, "admin");
        settings.put(Environment.PASS, "password");
        settings.put(Environment.DIALECT, "org.hibernate.dialect.DerbyDialect");
        settings.put(Environment.DRIVER, "org.apache.derby.jdbc.EmbeddedDriver");
        settings.put(Environment.SHOW_SQL, "false");
        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        settings.put(Environment.HBM2DDL_AUTO, "update");
        settings.put(Environment.POOL_SIZE, "20");
        settings.put(Environment.C3P0_MAX_SIZE, "30");
        settings.put(Environment.C3P0_MIN_SIZE, "5");
        settings.put(Environment.C3P0_IDLE_TEST_PERIOD, "-1");
        //                settings.put(Environment.AUTOCOMMIT, "true");
        settings.put(Environment.ENABLE_LAZY_LOAD_NO_TRANS, "true");
        if (System.getProperty("os.name").contains("Linux")) {
          settings.put(
              Environment.URL,
              "jdbc:derby:"
                  + System.getProperty("user.home")
                  + "/.config/ZenmartERP/datastores/databases/derby/database;create=true");
        }
        if (System.getProperty("os.name").contains("mac")) {
          settings.put(
              Environment.URL,
              "jdbc:derby:"
                  + System.getProperty("user.home")
                  + "/.config/ZenmartERP/database;create=true");
        }
        if (System.getProperty("os.name").contains("Windows")) {
          settings.put(
              Environment.URL,
              "jdbc:derby:"
                  + System.getenv("APPDATA")
                  + "/datastores/databases/derby/database;create=true");
        }
        // create-drop
        // create-only
        // validate
        // update

        configuration.setProperties(settings);

        configuration.addAnnotatedClass(AdjustmentMaster.class);
        configuration.addAnnotatedClass(AdjustmentDetail.class);
        configuration.addAnnotatedClass(Branch.class);
        configuration.addAnnotatedClass(Brand.class);
        configuration.addAnnotatedClass(Company.class);
        configuration.addAnnotatedClass(Currency.class);
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Expense.class);
        configuration.addAnnotatedClass(ExpenseCategory.class);
        configuration.addAnnotatedClass(Holiday.class);
        configuration.addAnnotatedClass(LeaveType.class);
        configuration.addAnnotatedClass(PaymentPurchase.class);
        configuration.addAnnotatedClass(PaymentPurchaseReturn.class);
        configuration.addAnnotatedClass(PaymentSale.class);
        configuration.addAnnotatedClass(PaymentSaleReturn.class);
        configuration.addAnnotatedClass(Permission.class);
        configuration.addAnnotatedClass(ProductCategory.class);
        configuration.addAnnotatedClass(ProductMaster.class);
        configuration.addAnnotatedClass(ProductDetail.class);
        configuration.addAnnotatedClass(ProductMaster.class);
        configuration.addAnnotatedClass(ProductDetail.class);
        configuration.addAnnotatedClass(PurchaseMaster.class);
        configuration.addAnnotatedClass(PurchaseDetail.class);
        configuration.addAnnotatedClass(PurchaseReturnMaster.class);
        configuration.addAnnotatedClass(PurchaseReturnDetail.class);
        configuration.addAnnotatedClass(QuotationMaster.class);
        configuration.addAnnotatedClass(QuotationDetail.class);
        configuration.addAnnotatedClass(Role.class);
        configuration.addAnnotatedClass(SaleMaster.class);
        configuration.addAnnotatedClass(SaleDetail.class);
        configuration.addAnnotatedClass(SaleReturnMaster.class);
        configuration.addAnnotatedClass(SaleReturnDetail.class);
        configuration.addAnnotatedClass(Supplier.class);
        configuration.addAnnotatedClass(TransferMaster.class);
        configuration.addAnnotatedClass(TransferDetail.class);
        configuration.addAnnotatedClass(UnitOfMeasure.class);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(RequisitionMaster.class);
        configuration.addAnnotatedClass(RequisitionDetail.class);
        configuration.addAnnotatedClass(StockInMaster.class);
        configuration.addAnnotatedClass(StockInDetail.class);

        ServiceRegistry serviceRegistry =
            new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return sessionFactory;
  }
}
