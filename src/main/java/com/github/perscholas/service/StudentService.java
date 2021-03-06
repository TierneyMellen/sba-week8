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
        ResultSet result = connection.executeQuery("SELECT * FROM students WHERE " + requirement + ";");
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

        return getAllStudentsWhere("`email` = '" + studentEmail + "'").get(0);

    }

    @Override
    public Boolean validateStudent(String studentEmail, String password) {

        return getStudentByEmail(studentEmail).getPassword().equals(password);
    }

    @Override
    public void registerStudentToCourse(String studentEmail, int courseId) {
        String thingWeWantToAdd = "insert into registration (id, email) values (" + courseId + ", '" + studentEmail + "');";
        DatabaseConnection.MANAGEMENTSYSTEM.executeStatement(thingWeWantToAdd);
    }

    @Override
    public List<CourseInterface> getStudentCourses(String studentEmail) {
        CourseService courseService= new CourseService(DatabaseConnection.MANAGEMENTSYSTEM);
        ResultSet result = connection.executeQuery("SELECT * FROM registration WHERE `email` = '" + studentEmail + "';");
        List<CourseInterface> courseList = new ArrayList<>();
        try{
            while(result.next()){
                Integer id = result.getInt("id");
                CourseInterface course = courseService.getCourseById(id);
                courseList.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;
    }
}
