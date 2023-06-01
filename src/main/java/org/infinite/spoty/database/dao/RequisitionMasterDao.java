package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.RequisitionMaster;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class RequisitionMasterDao {
    public static void saveRequisitionMaster(RequisitionMaster obj) {
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

    public static int updateRequisitionMaster(RequisitionMaster obj, long id) {
        Transaction transaction = null;
        RequisitionMaster requisitionMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            requisitionMaster = session.load(RequisitionMaster.class, id);
            requisitionMaster.setDate(obj.getDate());
            requisitionMaster.setSupplier(obj.getSupplier());
            requisitionMaster.setUser(obj.getUser());
            requisitionMaster.setBranch(obj.getBranch());
            requisitionMaster.setShipVia(obj.getShipVia());
            requisitionMaster.setShipMethod(obj.getShipMethod());
            requisitionMaster.setShippingTerms(obj.getShippingTerms());
            requisitionMaster.setDeliveryDate(obj.getDeliveryDate());
            requisitionMaster.setNotes(obj.getNotes());
            requisitionMaster.setStatus(obj.getStatus());
            requisitionMaster.setTotalCost(obj.getTotalCost());
            requisitionMaster.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // requisitionMaster.setUpdatedBy();
            session.update(requisitionMaster);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static RequisitionMaster findRequisitionMaster(long id) {
        Transaction transaction = null;
        RequisitionMaster requisitionMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            requisitionMaster = session.load(RequisitionMaster.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return requisitionMaster;
    }

    public static ObservableList<RequisitionMaster> fetchRequisitionMasters() {
        Transaction transaction = null;
        ObservableList<RequisitionMaster> requisitionMasters;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            requisitionMasters = FXCollections.observableList(session.createQuery("from RequisitionMaster").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return requisitionMasters;
    }
    public static int deleteRequisitionMaster(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(RequisitionMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}