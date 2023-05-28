package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.AdjustmentMaster;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class AdjustmentMasterDao {
    public static int saveAdjustmentMaster(AdjustmentMaster obj) {
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

    public static int updateAdjustmentMaster(AdjustmentMaster obj, long id) {
        Transaction transaction = null;
        AdjustmentMaster adjustmentMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            adjustmentMaster = session.load(AdjustmentMaster.class, id);
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
        return 1;
    }

    public static AdjustmentMaster findAdjustmentMaster(long id) {
        Transaction transaction = null;
        AdjustmentMaster adjustmentMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            adjustmentMaster = session.load(AdjustmentMaster.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return adjustmentMaster;
    }

    public static ObservableList<AdjustmentMaster> fetchAdjustmentMasters() {
        Transaction transaction = null;
        ObservableList<AdjustmentMaster> adjustmentMasters = null;
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
    public static int deleteAdjustmentMaster(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(AdjustmentMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
