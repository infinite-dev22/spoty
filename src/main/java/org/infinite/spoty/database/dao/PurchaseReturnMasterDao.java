package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.PurchaseReturnMaster;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class PurchaseReturnMasterDao {
    public static int savePurchaseReturnMaster(PurchaseReturnMaster obj) {
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

    public static int updatePurchaseReturnMaster(PurchaseReturnMaster obj, long id) {
        Transaction transaction = null;
        PurchaseReturnMaster purchaseReturnMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseReturnMaster = session.load(PurchaseReturnMaster.class, id);
            purchaseReturnMaster.setUser(obj.getUser());
            purchaseReturnMaster.setRef(obj.getRef());
            purchaseReturnMaster.setDate(obj.getDate());
            purchaseReturnMaster.setPurchaseReturnDetails(obj.getPurchaseReturnDetails());
            purchaseReturnMaster.setSupplier(obj.getSupplier());
            purchaseReturnMaster.setBranch(obj.getBranch());
            purchaseReturnMaster.setTaxRate(obj.getTaxRate());
            purchaseReturnMaster.setNetTax(obj.getNetTax());
            purchaseReturnMaster.setDiscount(obj.getDiscount());
            purchaseReturnMaster.setShipping(obj.getShipping());
            purchaseReturnMaster.setPaid(obj.getPaid());
            purchaseReturnMaster.setTotal(obj.getTotal());
            purchaseReturnMaster.setStatus(obj.getStatus());
            purchaseReturnMaster.setPaymentStatus(obj.getPaymentStatus());
            purchaseReturnMaster.setNotes(obj.getNotes());
            purchaseReturnMaster.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // purchaseReturnMaster.setUpdatedBy();
            session.update(purchaseReturnMaster);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static PurchaseReturnMaster findPurchaseReturnMaster(long id) {
        Transaction transaction = null;
        PurchaseReturnMaster purchaseReturnMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseReturnMaster = session.load(PurchaseReturnMaster.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseReturnMaster;
    }

    public static ObservableList<PurchaseReturnMaster> fetchPurchaseReturnMasters() {
        Transaction transaction = null;
        ObservableList<PurchaseReturnMaster> purchaseCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseCategories = FXCollections.observableList(session.createQuery("from PurchaseReturnMaster").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }
    public static int deletePurchaseReturnMaster(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(PurchaseReturnMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
