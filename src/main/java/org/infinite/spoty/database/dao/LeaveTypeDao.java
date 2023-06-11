package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.LeaveType;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class LeaveTypeDao {
    public static void saveLeaveType(LeaveType obj) {
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

    public static void updateLeaveType(LeaveType obj, int id) {
        Transaction transaction = null;
        LeaveType leaveType;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            leaveType = session.get(LeaveType.class, id);
            leaveType.setName(obj.getName());
            leaveType.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // leaveType.setUpdatedBy();
            session.merge(leaveType);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static LeaveType findLeaveType(int id) {
        Transaction transaction = null;
        LeaveType leaveType;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            leaveType = session.get(LeaveType.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return leaveType;
    }

    public static ObservableList<LeaveType> getLeaveType() {
        Transaction transaction = null;
        ObservableList<LeaveType> leaveTypes;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            leaveTypes = FXCollections.observableList(session.createQuery("from LeaveType", LeaveType.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return leaveTypes;
    }

    public static void deleteLeaveType(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(LeaveType.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
