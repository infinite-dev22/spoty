package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.Company;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

@SuppressWarnings("unchecked")
public class CompanyDao {
    public static int saveCompany(Company obj) {
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

    public static int updateCompany(Company obj, long id) {
        Transaction transaction = null;
        Company company;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            company = session.load(Company.class, id);
            company.setName(obj.getName());
            company.setPhone(obj.getPhone());
            company.setEmail(obj.getEmail());
            company.setCity(obj.getCity());
            company.setCountry(obj.getCountry());
            company.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // company.setUpdatedBy();
            session.update(company);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static Company findCompany(long id) {
        Transaction transaction = null;
        Company company;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            company = session.load(Company.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return company;
    }

    public static ObservableList<Company> getCompany() {
        Transaction transaction = null;
        ObservableList<Company> companys;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            companys = FXCollections.observableList(session.createQuery("from Company").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return companys;
    }

    public static int deleteCompany(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(Company.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
