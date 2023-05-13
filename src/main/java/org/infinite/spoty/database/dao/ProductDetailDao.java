package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class ProductDetailDao {
    public static int saveProductDetail(ProductDetail obj) {
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

    public static int updateProductDetail(ProductDetail obj, long id) {
        Transaction transaction = null;
        ProductDetail productDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            productDetail = session.load(ProductDetail.class, id);
            productDetail.setProduct(obj.getProduct());
            productDetail.setName(obj.getName());
            productDetail.setQuantity(obj.getQuantity());
            productDetail.setBranch(obj.getBranch());
            productDetail.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // productDetail.setUpdatedBy();
            session.update(productDetail);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static ProductDetail findProductDetail(long id) {
        Transaction transaction = null;
        ProductDetail productDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            productDetail = session.load(ProductDetail.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return productDetail;
    }

    public static ObservableList<ProductDetail> getProductDetail() {
        Transaction transaction = null;
        ObservableList<ProductDetail> productCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            productCategories = FXCollections.observableList(session.createQuery("from ProductDetail").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return productCategories;
    }
    public static int deleteProductDetail(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(ProductDetail.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
