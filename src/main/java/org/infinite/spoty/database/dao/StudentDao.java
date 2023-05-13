package org.infinite.spoty.database.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.Student;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.List;

public class StudentDao {
    public void saveStudent(Student student) {
        Transaction transaction = null;

        // Auto Closes Session Object.
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Start transaction
            transaction = session.beginTransaction();
            // Save Student Object
            session.save(student);
            // Commit Transaction
            transaction.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (transaction != null) transaction.rollback();
        }
    }

    public void updateStudent(Student student, int studentId) {
        Transaction transaction = null;
        Student stdnt = null;

        // Auto Closes Session Object.
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Start transaction
            transaction = session.beginTransaction();
            // Fetch Student Object By ID
            stdnt = session.get(Student.class, studentId);
            stdnt.setFirstname(student.getFirstname());
            stdnt.setLastname(student.getLastname());
            stdnt.setEmail(student.getEmail());

            // Update Student Object
            session.update(stdnt);
            // Commit Transaction
            transaction.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (transaction != null) transaction.rollback();
        }
    }

    public Student getStudentById(int studentId) {
        Transaction transaction = null;
        Student student = null;

        // Auto Closes Session Object.
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Start transaction
            transaction = session.beginTransaction();
            // Fetch Student Object By ID
            student = session.get(Student.class, studentId);
            // Commit Transaction
            transaction.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (transaction != null) transaction.rollback();
        }
        return student;
    }

    public List<Student> getStudents() {
        Transaction transaction = null;
        List<Student> studentList = null;

        // Auto Closes Session Object.
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Start transaction
            transaction = session.beginTransaction();
            // Fetch Student Object By ID
            studentList = session.createQuery("from student").list();
            // Commit Transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
        }
        return studentList;
    }

    public int deleteStudentById(int studentId) {
        Transaction transaction = null;
        Student student = null;

        // Auto Closes Session Object.
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Start transaction
            transaction = session.beginTransaction();
            // Fetch Student Object By ID
            student = session.get(Student.class, studentId);
            // Delete Fetched student
            session.delete(student);
            // Commit Transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            return 0;
        }
        return 1;
    }
}
