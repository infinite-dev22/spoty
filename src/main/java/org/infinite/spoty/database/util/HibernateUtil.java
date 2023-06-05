package org.infinite.spoty.database.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.infinite.spoty.database.models.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
//        getDatabaseConnection();
//        createDB();
//        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        try {
            Files.createDirectories(Paths.get(System.getProperty("user.home") + "/.config/ZenmartERP/datastores/databases/sqlite3"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                // Hibernate Settings
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "org.sqlite.JDBC");
                if (System.getProperty("os.name").contains("Linux")) {
                    settings.put(Environment.URL, "jdbc:sqlite:" + System.getProperty("user.home") + "/.config/ZenmartERP/datastores/databases/sqlite3/database.sqlite3");
                }
                if (System.getProperty("os.name").contains("mac")) {
                    settings.put(Environment.URL, "jdbc:sqlite:" + System.getProperty("user.home") + "/.config/ZenmartERP/database.sqlite");
                }
                if (System.getProperty("os.name").contains("Windows")) {
                    settings.put(Environment.URL, "jdbc:sqlite:" + System.getenv("APPDATA") + "/datastores/databases/sqlite3/database.sqlite3");
                }
                settings.put(Environment.USER, "");
                settings.put(Environment.PASS, "");
                settings.put(Environment.DRIVER, "org.sqlite.JDBC");
                settings.put(Environment.DIALECT, "org.sqlite.hibernate.dialect.SQLiteDialect");
                settings.put(Environment.SHOW_SQL, "false");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "create-only");
                settings.put(Environment.POOL_SIZE, "5");
                settings.put(Environment.AUTOCOMMIT, "true");
                // create-drop
                // create-only
                // validate
                // update

                configuration.setProperties(settings);

                configuration.addAnnotatedClass(AdjustmentMaster.class);
                configuration.addAnnotatedClass(AdjustmentDetail.class);
                configuration.addAnnotatedClass(Branch.class);
                configuration.addAnnotatedClass(Brand.class);
                configuration.addAnnotatedClass(Client.class);
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

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
