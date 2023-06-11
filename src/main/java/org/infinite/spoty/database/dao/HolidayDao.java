package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.Holiday;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class HolidayDao {
    public static void saveHoliday(Holiday obj) {
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

    public static void updateHoliday(Holiday obj, int id) {
        Transaction transaction = null;
        Holiday holiday;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            holiday = session.get(Holiday.class, id);
            holiday.setTitle(obj.getTitle());
            holiday.setCompany(obj.getCompany());
            holiday.setStartDate(obj.getStartDate());
            holiday.setEndDate(obj.getEndDate());
            holiday.setDescription(obj.getDescription());
            holiday.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // holiday.setUpdatedBy();
            session.merge(holiday);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static Holiday findHoliday(int id) {
        Transaction transaction = null;
        Holiday holiday;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            holiday = session.get(Holiday.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return holiday;
    }

    public static ObservableList<Holiday> getHoliday() {
        Transaction transaction = null;
        ObservableList<Holiday> holidays;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            holidays = FXCollections.observableList(session.createQuery("from Holiday", Holiday.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return holidays;
    }

    public static void deleteHoliday(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(Holiday.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
