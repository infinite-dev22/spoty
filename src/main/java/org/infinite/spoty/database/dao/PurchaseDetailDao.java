package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.PurchaseDetail;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class PurchaseDetailDao {
    public static int savePurchaseDetail(PurchaseDetail obj) {
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

    public static int updatePurchaseDetail(PurchaseDetail obj, long id) {
        Transaction transaction = null;
        PurchaseDetail purchaseDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseDetail = session.load(PurchaseDetail.class, id);
            purchaseDetail.setCost(obj.getCost());
            purchaseDetail.setPurchase(obj.getPurchase());
            purchaseDetail.setNetTax(obj.getNetTax());
            purchaseDetail.setTaxtType(obj.getTaxtType());
            purchaseDetail.setDiscount(obj.getDiscount());
            purchaseDetail.setDiscountType(obj.getDiscountType());
            purchaseDetail.setProduct(obj.getProduct());
            purchaseDetail.setSerialNumber(obj.getSerialNumber());
            purchaseDetail.setTotal(obj.getTotal());
            purchaseDetail.setQuantity(obj.getQuantity());
            purchaseDetail.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // purchaseDetail.setUpdatedBy();
            session.update(purchaseDetail);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static PurchaseDetail findPurchaseDetail(long id) {
        Transaction transaction = null;
        PurchaseDetail purchaseDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseDetail = session.load(PurchaseDetail.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseDetail;
    }

    public static ObservableList<PurchaseDetail> fetchPurchaseDetails() {
        Transaction transaction = null;
        ObservableList<PurchaseDetail> purchaseCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseCategories = FXCollections.observableList(session.createQuery("from PurchaseDetail").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }
    public static int deletePurchaseDetail(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(PurchaseDetail.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}