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
    public static void saveAdjustmentDetail(AdjustmentDetail obj) {
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

    public static void updateAdjustmentDetail(AdjustmentDetail obj, int id) {
        Transaction transaction = null;
        AdjustmentDetail adjustmentDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            adjustmentDetail = session.get(AdjustmentDetail.class, id);
            adjustmentDetail.setAdjustment(obj.getAdjustment());
            adjustmentDetail.setProductDetail(obj.getProductDetail());
            adjustmentDetail.setAdjustmentType(obj.getAdjustmentType());
            adjustmentDetail.setQuantity(obj.getQuantity());
            adjustmentDetail.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // adjustmentDetail.setUpdatedBy();
            session.merge(adjustmentDetail);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static AdjustmentDetail findAdjustmentDetail(int id) {
        Transaction transaction = null;
        AdjustmentDetail adjustmentDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            adjustmentDetail = session.get(AdjustmentDetail.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return adjustmentDetail;
    }

    public static ObservableList<AdjustmentDetail> getAdjustmentDetail() {
        Transaction transaction = null;
        ObservableList<AdjustmentDetail> adjustmentDetails;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            adjustmentDetails = FXCollections.observableList(session.createQuery("from AdjustmentDetail", AdjustmentDetail.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return adjustmentDetails;
    }

    public static void deleteAdjustmentDetail(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(AdjustmentDetail.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
