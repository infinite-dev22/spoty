package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class BranchDao {
    public static void saveBranch(Branch obj) {
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

    public static void updateBranch(Branch obj, int id) {
        Transaction transaction = null;
        Branch branch;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            branch = session.get(Branch.class, id);
            branch.setName(obj.getName());
            branch.setCity(obj.getCity());
            branch.setPhone(obj.getPhone());
            branch.setEmail(obj.getEmail());
            branch.setTown(obj.getTown());
            branch.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // branch.setUpdatedBy();
            session.merge(branch);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static Branch findBranch(int id) {
        Transaction transaction = null;
        Branch branch;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            branch = session.get(Branch.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return branch;
    }

    public static ObservableList<Branch> getBranches() {
        Transaction transaction = null;
        ObservableList<Branch> branches;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            branches = FXCollections.observableList(session.createQuery("from Branch", Branch.class).list());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return branches;
    }
    public static int deleteBranch(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(Branch.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
