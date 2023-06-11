package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.ProductCategory;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class ProductCategoryDao {
    public static void saveProductCategory(ProductCategory obj) {
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

    public static void updateProductCategory(ProductCategory obj, int id) {
        Transaction transaction = null;
        ProductCategory productCategory;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            productCategory = session.get(ProductCategory.class, id);
            productCategory.setCode(obj.getCode());
            productCategory.setName(obj.getName());
            productCategory.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // productCategory.setUpdatedBy();
            session.merge(productCategory);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static ProductCategory findProductCategory(int id) {
        Transaction transaction = null;
        ProductCategory productCategory;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            productCategory = session.get(ProductCategory.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return productCategory;
    }

    public static ObservableList<ProductCategory> getProductCategories() {
        Transaction transaction = null;
        ObservableList<ProductCategory> productCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            productCategories = FXCollections.observableList(
                    session.createQuery("from ProductCategory", ProductCategory.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return productCategories;
    }

    public static void deleteProductCategory(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(ProductCategory.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
