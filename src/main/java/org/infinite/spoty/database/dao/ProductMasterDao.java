package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.ProductMaster;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class ProductMasterDao {
    public static void saveProductMaster(ProductMaster obj) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            obj.setCreatedAt(new Date());
            // TODO: created by should be a system user.
            // obj.setCreatedBy();
            session.persist(obj);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static int updateProductMaster(ProductMaster obj, int id) {
        Transaction transaction = null;
        ProductMaster productMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            productMaster = session.get(ProductMaster.class, id);
            productMaster.setCode(obj.getCode());
            productMaster.setBarcodeType(obj.getBarcodeType());
            productMaster.setName(obj.getName());
            productMaster.setCategory(obj.getCategory());
            productMaster.setBrand(obj.getBrand());
            productMaster.setActive(obj.isActive());
            productMaster.setNotForSale(obj.isNotForSale());
            productMaster.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // productMaster.setUpdatedBy();
            session.merge(productMaster);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static ProductMaster findProductMaster(int id) {
        Transaction transaction = null;
        ProductMaster productMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            productMaster = session.get(ProductMaster.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return productMaster;
    }

    public static ObservableList<ProductMaster> getProductMaster() {
        Transaction transaction = null;
        ObservableList<ProductMaster> productCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            productCategories = FXCollections.observableList(
                    session.createQuery("from ProductMaster", ProductMaster.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return productCategories;
    }

    public static int deleteProductMaster(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(ProductMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
