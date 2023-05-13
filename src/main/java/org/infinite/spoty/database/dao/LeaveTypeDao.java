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
    public static int saveLeaveType(LeaveType obj) {
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

    public static int updateLeaveType(LeaveType obj, long id) {
        Transaction transaction = null;
        LeaveType leaveType;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            leaveType = session.load(LeaveType.class, id);
            leaveType.setName(obj.getName());
            leaveType.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // leaveType.setUpdatedBy();
            session.update(leaveType);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static LeaveType findLeaveType(long id) {
        Transaction transaction = null;
        LeaveType leaveType;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            leaveType = session.load(LeaveType.class, id);
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
            leaveTypes = FXCollections.observableList(session.createQuery("from LeaveType").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return leaveTypes;
    }
    public static int deleteLeaveType(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(LeaveType.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
