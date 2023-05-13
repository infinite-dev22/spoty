package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.User;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class UserDao {
    public static int saveUser(User obj) {
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

    public static int updateUser(User obj, long id) {
        Transaction transaction = null;
        User user;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            user = session.load(User.class, id);
            user.setFirstName(obj.getFirstName());
            user.setLastName(obj.getLastName());
            user.setUserName(obj.getUserName());
            user.setEmail(obj.getEmail());
            user.setPassword(obj.getPassword());
            user.setPhone(obj.getPhone());
            user.setRole(obj.getRole());
            user.setStatus(obj.getStatus());
            user.setAccessAllBranches(obj.isAccessAllBranches());
            user.setAvatar(obj.getAvatar());
            user.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // user.setUpdatedBy();
            session.update(user);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static User findUser(long id) {
        Transaction transaction = null;
        User user;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            user = session.load(User.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return user;
    }

    public static ObservableList<User> getUser() {
        Transaction transaction = null;
        ObservableList<User> purchaseCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseCategories = FXCollections.observableList(session.createQuery("from User").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }

    public static int deleteUser(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(User.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
