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
    public static void savePermission(Permission obj) {
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

    public static void updatePermission(Permission obj, int id) {
        Transaction transaction = null;
        Permission permission;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            permission = session.get(Permission.class, id);
            permission.setName(obj.getName());
            permission.setLabel(obj.getLabel());
            permission.setDescription(obj.getDescription());
            permission.setRole(obj.getRole());
            permission.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // permission.setUpdatedBy();
            session.merge(permission);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static Permission findPermission(int id) {
        Transaction transaction = null;
        Permission permission;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            permission = session.get(Permission.class, id);
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
            permissions = FXCollections.observableList(
                    session.createQuery("from Permission", Permission.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return permissions;
    }

    public static void deletePermission(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(Permission.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
