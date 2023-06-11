package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.PurchaseReturnDetail;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class PurchaseReturnDetailDao {
    public static void savePurchaseReturnDetail(PurchaseReturnDetail obj) {
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

    public static void updatePurchaseReturnDetail(PurchaseReturnDetail obj, int id) {
        Transaction transaction = null;
        PurchaseReturnDetail purchaseReturnDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseReturnDetail = session.get(PurchaseReturnDetail.class, id);
            purchaseReturnDetail.setCost(obj.getCost());
            purchaseReturnDetail.setPurchaseUnit(obj.getPurchaseUnit());
            purchaseReturnDetail.setPurchaseReturn(obj.getPurchaseReturn());
            purchaseReturnDetail.setProduct(obj.getProduct());
            purchaseReturnDetail.setTaxType(obj.getTaxType());
            purchaseReturnDetail.setNetTax(obj.getNetTax());
            purchaseReturnDetail.setDiscount(obj.getDiscount());
            purchaseReturnDetail.setDiscountType(obj.getDiscountType());
            purchaseReturnDetail.setQuantity(obj.getQuantity());
            purchaseReturnDetail.setTotal(obj.getTotal());
            purchaseReturnDetail.setSerialNumber(obj.getSerialNumber());
            purchaseReturnDetail.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // purchaseReturnDetail.setUpdatedBy();
            session.merge(purchaseReturnDetail);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static PurchaseReturnDetail findPurchaseReturnDetail(int id) {
        Transaction transaction = null;
        PurchaseReturnDetail purchaseReturnDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseReturnDetail = session.get(PurchaseReturnDetail.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseReturnDetail;
    }

    public static ObservableList<PurchaseReturnDetail> fetchPurchaseReturnDetails() {
        Transaction transaction = null;
        ObservableList<PurchaseReturnDetail> purchaseCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseCategories = FXCollections.observableList(
                    session.createQuery("from PurchaseReturnDetail", PurchaseReturnDetail.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }

    public static void deletePurchaseReturnDetail(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(PurchaseReturnDetail.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
