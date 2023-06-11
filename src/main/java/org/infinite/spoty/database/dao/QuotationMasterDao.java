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
    public static void saveQuotationMaster(QuotationMaster obj) {
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

    public static void updateQuotationMaster(QuotationMaster obj, int id) {
        Transaction transaction = null;
        QuotationMaster quotationMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            quotationMaster = session.get(QuotationMaster.class, id);
            quotationMaster.setUser(obj.getUser());
            quotationMaster.setRef(obj.getRef());
            quotationMaster.setDate(obj.getDate());
            quotationMaster.setCustomer(obj.getCustomer());
            quotationMaster.setBranch(obj.getBranch());
            quotationMaster.setShipping(obj.getShipping());
            // quotationMaster.setQuotationDetails(obj.getQuotationDetails());
            quotationMaster.setTotal(obj.getTotal());
            quotationMaster.setStatus(obj.getStatus());
            quotationMaster.setNotes(obj.getNotes());
            quotationMaster.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // quotationMaster.setUpdatedBy();
            session.merge(quotationMaster);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static QuotationMaster findQuotationMaster(int id) {
        Transaction transaction = null;
        QuotationMaster quotationMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            quotationMaster = session.get(QuotationMaster.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return quotationMaster;
    }

    public static ObservableList<QuotationMaster> fetchQuotationMasters() {
        Transaction transaction = null;
        ObservableList<QuotationMaster> purchaseCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseCategories = FXCollections.observableList(session.createQuery("from QuotationMaster", QuotationMaster.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }

    public static void deleteQuotationMaster(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(QuotationMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
