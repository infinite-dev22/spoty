package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.Permission;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class PermissionDao {
    public static int savePermission(Permission obj) {
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

    public static int updatePermission(Permission obj, long id) {
        Transaction transaction = null;
        Permission permission;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            permission = session.load(Permission.class, id);
            permission.setName(obj.getName());
            permission.setLabel(obj.getLabel());
            permission.setDescription(obj.getDescription());
            permission.setRole(obj.getRole());
            permission.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // permission.setUpdatedBy();
            session.update(permission);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static Permission findPermission(long id) {
        Transaction transaction = null;
        Permission permission;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            permission = session.load(Permission.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return permission;
    }

    public static ObservableList<Permission> getPermission() {
        Transaction transaction = null;
        ObservableList<Permission> permissions;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            permissions = FXCollections.observableList(session.createQuery("from Permission").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return permissions;
    }
    public static int deletePermission(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(Permission.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
