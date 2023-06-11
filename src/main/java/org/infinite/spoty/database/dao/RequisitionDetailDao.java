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
    public static void saveRequisitionDetail(RequisitionDetail obj) {
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

    public static void updateRequisitionDetail(RequisitionDetail obj, int id) {
        Transaction transaction = null;
        RequisitionDetail requisitionDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            requisitionDetail = session.get(RequisitionDetail.class, id);
            requisitionDetail.setRequisition(obj.getRequisition());
            requisitionDetail.setProductDetail(obj.getProductDetail());
            requisitionDetail.setDescription(obj.getDescription());
            requisitionDetail.setQuantity(obj.getQuantity());
            requisitionDetail.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // requisitionDetail.setUpdatedBy();
            session.merge(requisitionDetail);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static RequisitionDetail findRequisitionDetail(int id) {
        Transaction transaction = null;
        RequisitionDetail requisitionDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            requisitionDetail = session.get(RequisitionDetail.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return requisitionDetail;
    }

    public static ObservableList<RequisitionDetail> fetchRequisitionDetails() {
        Transaction transaction = null;
        ObservableList<RequisitionDetail> requisitionDetails;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            requisitionDetails = FXCollections.observableList(
                    session.createQuery("from RequisitionDetail", RequisitionDetail.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return requisitionDetails;
    }

    public static void deleteRequisitionDetail(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(RequisitionDetail.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
