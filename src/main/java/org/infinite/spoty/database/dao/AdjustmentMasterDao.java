package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.AdjustmentMaster;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

@SuppressWarnings("unchecked")
public class AdjustmentMasterDao {
    public static void saveAdjustmentMaster(AdjustmentMaster obj) {
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

    public static void updateAdjustmentMaster(AdjustmentMaster obj, int id) {
        Transaction transaction = null;
        AdjustmentMaster adjustmentMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            adjustmentMaster = session.get(AdjustmentMaster.class, id);
            adjustmentMaster.setUser(obj.getUser());
            adjustmentMaster.setDate(obj.getDate());
            adjustmentMaster.setRef(obj.getRef());
            adjustmentMaster.setBranch(obj.getBranch());
            adjustmentMaster.setNotes(obj.getNotes());
            adjustmentMaster.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // adjustmentMaster.setUpdatedBy();
            session.update(adjustmentMaster);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static AdjustmentMaster findAdjustmentMaster(int id) {
        Transaction transaction = null;
        AdjustmentMaster adjustmentMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            adjustmentMaster = session.get(AdjustmentMaster.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return adjustmentMaster;
    }

    public static ObservableList<AdjustmentMaster> fetchAdjustmentMasters() {
        Transaction transaction = null;
        ObservableList<AdjustmentMaster> adjustmentMasters;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            adjustmentMasters = FXCollections.observableList(session.createQuery("from AdjustmentMaster").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return adjustmentMasters;
    }

    public static void deleteAdjustmentMaster(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.get(AdjustmentMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
