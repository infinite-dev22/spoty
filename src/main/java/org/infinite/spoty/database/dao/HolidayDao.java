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
    public static int saveHoliday(Holiday obj) {
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

    public static int updateHoliday(Holiday obj, long id) {
        Transaction transaction = null;
        Holiday holiday;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            holiday = session.load(Holiday.class, id);
            holiday.setTitle(obj.getTitle());
            holiday.setCompany(obj.getCompany());
            holiday.setStartDate(obj.getStartDate());
            holiday.setEndDate(obj.getEndDate());
            holiday.setDescription(obj.getDescription());
            holiday.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // holiday.setUpdatedBy();
            session.update(holiday);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static Holiday findHoliday(long id) {
        Transaction transaction = null;
        Holiday holiday;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            holiday = session.load(Holiday.class, id);
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
            holidays = FXCollections.observableList(session.createQuery("from Holiday").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return holidays;
    }
    public static int deleteHoliday(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(Holiday.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
