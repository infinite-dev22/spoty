package org.infinite.spoty.database;


import org.infinite.spoty.database.dao.StudentDao;
import org.infinite.spoty.database.models.Student;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        StudentDao studentDao = new StudentDao();
        Student student = new Student("Mwigo", "Mark", "mjm@email.com");
        studentDao.saveStudent(student);
        System.out.println(student.getId());
    }
}
