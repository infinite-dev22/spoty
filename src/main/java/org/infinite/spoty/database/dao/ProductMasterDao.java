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
    public static int saveProductMaster(ProductMaster obj) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            obj.setCreatedAt(new Date());
            // TODO: created by should be a system user.
            // obj.setCreatedBy();
            session.save(obj);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static int updateProductMaster(ProductMaster obj, long id) {
        Transaction transaction = null;
        ProductMaster productMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            productMaster = session.load(ProductMaster.class, id);
            productMaster.setCode(obj.getCode());
            productMaster.setBarcodeType(obj.getBarcodeType());
            productMaster.setName(obj.getName());
            productMaster.setCost(obj.getCost());
            productMaster.setPrice(obj.getPrice());
            productMaster.setCategory(obj.getCategory());
            productMaster.setBrand(obj.getBrand());
            productMaster.setActive(obj.isActive());
            productMaster.setNotForSale(obj.isNotForSale());
            productMaster.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // productMaster.setUpdatedBy();
            session.update(productMaster);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static ProductMaster findProductMaster(long id) {
        Transaction transaction = null;
        ProductMaster productMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            productMaster = session.load(ProductMaster.class, id);
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
            productCategories = FXCollections.observableList(session.createQuery("from ProductMaster").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return productCategories;
    }
    public static int deleteProductMaster(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(ProductMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
