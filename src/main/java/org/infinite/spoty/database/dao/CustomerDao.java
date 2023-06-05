package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.Customer;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class CustomerDao {
    public static void saveCustomer(Customer obj) {
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
    }

    public static void updateCustomer(Customer obj, int id) {
        Transaction transaction = null;
        Customer customer = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            customer = session.get(Customer.class, id);
            customer.setName(obj.getName());
            customer.setCode(obj.getCode());
            customer.setEmail(obj.getEmail());
            customer.setPhone(obj.getPhone());
            customer.setCity(obj.getCity());
            customer.setAddress(obj.getAddress());
            customer.setTaxNumber(obj.getTaxNumber());
            customer.setCountry(obj.getCountry());
            customer.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // customer.setUpdatedBy();
            session.update(customer);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static Customer findCustomer(int id) {
        Transaction transaction = null;
        Customer customer;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            customer = session.get(Customer.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return customer;
    }

    public static ObservableList<Customer> fetchCustomers() {
        Transaction transaction = null;
        ObservableList<Customer> customers;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            customers = FXCollections.observableList(session.createQuery("from Customer").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return customers;
    }

    public static int deleteCustomer(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.get(Customer.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
