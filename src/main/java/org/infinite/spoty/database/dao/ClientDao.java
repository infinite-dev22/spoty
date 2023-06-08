package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.Client;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;
@Deprecated
public class ClientDao {
    public static int saveClient(Client obj) {
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

    public static int updateClient(Client obj, long id) {
        Transaction transaction = null;
        Client client = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            client = session.load(Client.class, id);
            client.setName(obj.getName());
            client.setCode(obj.getCode());
            client.setEmail(obj.getEmail());
            client.setPhone(obj.getPhone());
            client.setTaxNumber(obj.getTaxNumber());
            client.setAddress(obj.getAddress());
            client.setCity(obj.getCity());
            client.setCountry(obj.getCountry());
            client.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // client.setUpdatedBy();
            session.update(client);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static Client findClient(long id) {
        Transaction transaction = null;
        Client client;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            client = session.load(Client.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return client;
    }

    public static ObservableList<Client> getClient() {
        Transaction transaction = null;
        ObservableList<Client> clients;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            clients = FXCollections.observableList(session.createQuery("from Client").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return clients;
    }
    public static int deleteClient(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(Client.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
