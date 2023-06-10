package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.TransferMaster;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

@SuppressWarnings("unchecked")
public class TransferMasterDao {
    public static void saveTransferMaster(TransferMaster obj) {
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

    public static void updateTransferMaster(TransferMaster obj, int id) {
        Transaction transaction = null;
        TransferMaster transferMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            transferMaster = session.get(TransferMaster.class, id);
            transferMaster.setUser(obj.getUser());
            transferMaster.setRef(obj.getRef());
            transferMaster.setDate(obj.getDate());
            transferMaster.setFromBranch(obj.getFromBranch());
            transferMaster.setToBranch(obj.getToBranch());
            transferMaster.setTransferDetails(obj.getTransferDetails());
            transferMaster.setShipping(obj.getShipping());
            transferMaster.setTotal(obj.getTotal());
            transferMaster.setStatus(obj.getStatus());
            transferMaster.setApprovedBy(obj.getApprovedBy());
            transferMaster.setApprovalDate(obj.getApprovalDate());
            transferMaster.setReceivedBy(obj.getReceivedBy());
            transferMaster.setReceiveDate(obj.getReceiveDate());
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
    }

    public static TransferMaster findTransferMaster(int id) {
        Transaction transaction = null;
        TransferMaster transferMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            transferMaster = session.get(TransferMaster.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return transferMaster;
    }

    public static ObservableList<TransferMaster> fetchTransferMasters() {
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

    public static void deleteTransferMaster(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.get(TransferMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
