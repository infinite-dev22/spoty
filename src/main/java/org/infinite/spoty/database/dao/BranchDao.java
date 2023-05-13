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
    public static int saveBranch(Branch obj) {
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

    public static int updateBranch(Branch obj, long id) {
        Transaction transaction = null;
        Branch branch = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            branch = session.load(Branch.class, id);
            branch.setName(obj.getName());
            branch.setCity(obj.getCity());
            branch.setPhone(obj.getPhone());
            branch.setEmail(obj.getEmail());
            branch.setCountry(obj.getCountry());
            branch.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // branch.setUpdatedBy();
            session.update(branch);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static Branch findBranch(long id) {
        Transaction transaction = null;
        Branch branch;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            branch = session.load(Branch.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return branch;
    }

    public static ObservableList<Branch> getBranch() {
        Transaction transaction = null;
        ObservableList<Branch> branches;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            branches = FXCollections.observableList(session.createQuery("from Branch").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return branches;
    }
    public static int deleteBranch(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(Branch.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
