package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.TransferMaster;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class TransferMasterDao {
    public static int saveTransferMaster(TransferMaster obj) {
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

    public static int updateTransferMaster(TransferMaster obj, long id) {
        Transaction transaction = null;
        TransferMaster transferMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            transferMaster = session.load(TransferMaster.class, id);
            transferMaster.setUser(obj.getUser());
            transferMaster.setRef(obj.getRef());
            transferMaster.setDate(obj.getDate());
            transferMaster.setFromBranch(obj.getFromBranch());
            transferMaster.setToBranch(obj.getToBranch());
            transferMaster.setItems(obj.getItems());
            transferMaster.setTaxRate(obj.getTaxRate());
            transferMaster.setTax(obj.getTax());
            transferMaster.setDiscount(obj.getDiscount());
            transferMaster.setShipping(obj.getShipping());
            transferMaster.setTotal(obj.getTotal());
            transferMaster.setStatus(obj.getStatus());
            transferMaster.setNotes(obj.getNotes());
            transferMaster.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // transferMaster.setUpdatedBy();
            session.update(transferMaster);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static TransferMaster findTransferMaster(long id) {
        Transaction transaction = null;
        TransferMaster transferMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            transferMaster = session.load(TransferMaster.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return transferMaster;
    }

    public static ObservableList<TransferMaster> getTransferMaster() {
        Transaction transaction = null;
        ObservableList<TransferMaster> saleCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            saleCategories = FXCollections.observableList(session.createQuery("from TransferMaster").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return saleCategories;
    }
    public static int deleteTransferMaster(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(TransferMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
