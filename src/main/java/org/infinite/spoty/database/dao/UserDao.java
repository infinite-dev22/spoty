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
    public static void saveUser(User obj) {
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

    public static void updateUser(User obj, int id) {
        Transaction transaction = null;
        User user;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            user = session.get(User.class, id);
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
            session.merge(user);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static User findUser(int id) {
        Transaction transaction = null;
        User user;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            user = session.get(User.class, id);
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
            purchaseCategories = FXCollections.observableList(
                    session.createQuery("from User", User.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }

    public static void deleteUser(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(User.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
