package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.Role;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class RoleDao {
    public static int saveRole(Role obj) {
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

    public static int updateRole(Role obj, long id) {
        Transaction transaction = null;
        Role role;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            role = session.load(Role.class, id);
            role.setName(obj.getName());
            role.setLabel(obj.getLabel());
            role.setActive(obj.isActive());
            role.setDescription(obj.getDescription());
            role.setPermission(obj.getPermission());
            role.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // role.setUpdatedBy();
            session.update(role);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static Role findRole(long id) {
        Transaction transaction = null;
        Role role;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            role = session.load(Role.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return role;
    }

    public static ObservableList<Role> getRole() {
        Transaction transaction = null;
        ObservableList<Role> purchaseCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseCategories = FXCollections.observableList(session.createQuery("from Role").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }
    public static int deleteRole(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(Role.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
