package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.QuotationMaster;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class QuotationMasterDao {
    public static int saveQuotationMaster(QuotationMaster obj) {
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

    public static int updateQuotationMaster(QuotationMaster obj, long id) {
        Transaction transaction = null;
        QuotationMaster quotationMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            quotationMaster = session.load(QuotationMaster.class, id);
            quotationMaster.setUser(obj.getUser());
            quotationMaster.setRef(obj.getRef());
            quotationMaster.setDate(obj.getDate());
            quotationMaster.setClient(obj.getClient());
            quotationMaster.setBranch(obj.getBranch());
            quotationMaster.setTaxRate(obj.getTaxRate());
            quotationMaster.setNetTax(obj.getNetTax());
            quotationMaster.setDiscount(obj.getDiscount());
            quotationMaster.setShipping(obj.getShipping());
            quotationMaster.setTotal(obj.getTotal());
            quotationMaster.setStatus(obj.getStatus());
            quotationMaster.setNotes(obj.getNotes());
            quotationMaster.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // quotationMaster.setUpdatedBy();
            session.update(quotationMaster);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static QuotationMaster findQuotationMaster(long id) {
        Transaction transaction = null;
        QuotationMaster quotationMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            quotationMaster = session.load(QuotationMaster.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return quotationMaster;
    }

    public static ObservableList<QuotationMaster> getQuotationMaster() {
        Transaction transaction = null;
        ObservableList<QuotationMaster> purchaseCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseCategories = FXCollections.observableList(session.createQuery("from QuotationMaster").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }
    public static int deleteQuotationMaster(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(QuotationMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
