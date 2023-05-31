package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.RequisitionDetail;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class RequisitionDetailDao {
    public static int saveRequisitionDetail(RequisitionDetail obj) {
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

    public static int updateRequisitionDetail(RequisitionDetail obj, long id) {
        Transaction transaction = null;
        RequisitionDetail requisitionDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            requisitionDetail = session.load(RequisitionDetail.class, id);
            requisitionDetail.setRequisition(obj.getRequisition());
            requisitionDetail.setProductDetail(obj.getProductDetail());
            requisitionDetail.setDescription(obj.getDescription());
            requisitionDetail.setQuantity(obj.getQuantity());
            requisitionDetail.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // requisitionDetail.setUpdatedBy();
            session.update(requisitionDetail);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static RequisitionDetail findRequisitionDetail(long id) {
        Transaction transaction = null;
        RequisitionDetail requisitionDetail = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            requisitionDetail = session.load(RequisitionDetail.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return requisitionDetail;
    }

    public static ObservableList<RequisitionDetail> getRequisitionDetail() {
        Transaction transaction = null;
        ObservableList<RequisitionDetail> requisitionDetails = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            requisitionDetails = FXCollections.observableList(session.createQuery("from RequisitionDetail").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return requisitionDetails;
    }

    public static int deleteRequisitionDetail(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(RequisitionDetail.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
