package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.PurchaseMaster;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

@SuppressWarnings("unchecked")
public class PurchaseMasterDao {
    public static void savePurchaseMaster(PurchaseMaster obj) {
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
    }

    public static void updatePurchaseMaster(PurchaseMaster obj, int id) {
        Transaction transaction = null;
        PurchaseMaster purchaseMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseMaster = session.get(PurchaseMaster.class, id);
            purchaseMaster.setUser(obj.getUser());
            purchaseMaster.setRef(obj.getRef());
            purchaseMaster.setDate(obj.getDate());
            purchaseMaster.setSupplier(obj.getSupplier());
            purchaseMaster.setBranch(obj.getBranch());
            purchaseMaster.setTaxRate(obj.getTaxRate());
            purchaseMaster.setNetTax(obj.getNetTax());
            purchaseMaster.setDiscount(obj.getDiscount());
            purchaseMaster.setShipping(obj.getShipping());
            purchaseMaster.setPaid(obj.getPaid());
            purchaseMaster.setTotal(obj.getTotal());
            purchaseMaster.setStatus(obj.getStatus());
            purchaseMaster.setPaymentStatus(obj.getPaymentStatus());
            purchaseMaster.setNotes(obj.getNotes());
            purchaseMaster.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // purchaseMaster.setUpdatedBy();
            session.update(purchaseMaster);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static PurchaseMaster findPurchaseMaster(int id) {
        Transaction transaction = null;
        PurchaseMaster purchaseMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseMaster = session.get(PurchaseMaster.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseMaster;
    }

    public static ObservableList<PurchaseMaster> fetchPurchaseMasters() {
        Transaction transaction = null;
        ObservableList<PurchaseMaster> purchaseCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseCategories = FXCollections.observableList(session.createQuery("from PurchaseMaster").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }

    public static void deletePurchaseMaster(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.get(PurchaseMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
