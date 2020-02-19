package com.github.perscholas.service;

import com.github.perscholas.DatabaseConnection;
import com.github.perscholas.dao.StudentDao;
import com.github.perscholas.model.CourseInterface;
import com.github.perscholas.model.Student;
import com.github.perscholas.model.StudentInterface;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// TODO - Implement respective DAO interface
public class StudentService implements StudentDao {

    private final DatabaseConnection connection;

    public StudentService(DatabaseConnection connection) {
        this.connection = connection;
    }

    public List<StudentInterface> getAllStudentsWhere(String requirement) {
        ResultSet result = connection.executeQuery("SELECT * FROM students WHERE " + requirement);
        List<StudentInterface> studentList = new ArrayList<>();
        try{
            while(result.next()){
                String studentEmail = result.getString("email");
                String name = result.getString("name");
                String password = result.getString("password");
                StudentInterface student = new Student(studentEmail, name, password);
                studentList.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentList;
    }

    @Override
    public List<StudentInterface> getAllStudents() {
        return getAllStudentsWhere("true");
    }

    @Override
    public StudentInterface getStudentByEmail(String studentEmail) {

        return getAllStudentsWhere("`email` = " + studentEmail).get(0);

    }

    @Override
    public Boolean validateStudent(String studentEmail, String password) {

        return !getAllStudentsWhere("`email` = '" + studentEmail + "' AND `password` = '" + password+ "'").isEmpty();
    }

    @Override
    public void registerStudentToCourse(String studentEmail, int courseId) {

    }

    @Override
    public List<CourseInterface> getStudentCourses(String studentEmail) {
        return null;
    }
}
