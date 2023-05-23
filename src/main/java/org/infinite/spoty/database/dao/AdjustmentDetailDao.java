package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.AdjustmentDetail;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class AdjustmentDetailDao {
    public static int saveAdjustmentDetail(AdjustmentDetail obj) {
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

    public static int updateAdjustmentDetail(AdjustmentDetail obj, long id) {
        Transaction transaction = null;
        AdjustmentDetail adjustmentDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            adjustmentDetail = session.load(AdjustmentDetail.class, id);
            adjustmentDetail.setAdjustment(obj.getAdjustment());
            adjustmentDetail.setProductDetail(obj.getProductDetail());
            adjustmentDetail.setAdjustmentType(obj.getAdjustmentType());
            adjustmentDetail.setQuantity(obj.getQuantity());
            adjustmentDetail.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // adjustmentDetail.setUpdatedBy();
            session.update(adjustmentDetail);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static AdjustmentDetail findAdjustmentDetail(long id) {
        Transaction transaction = null;
        AdjustmentDetail adjustmentDetail = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            adjustmentDetail = session.load(AdjustmentDetail.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return adjustmentDetail;
    }

    public static ObservableList<AdjustmentDetail> getAdjustmentDetail() {
        Transaction transaction = null;
        ObservableList<AdjustmentDetail> adjustmentDetails = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            adjustmentDetails = FXCollections.observableList(session.createQuery("from AdjustmentDetail").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return adjustmentDetails;
    }
    public static int deleteAdjustmentDetail(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(AdjustmentDetail.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}