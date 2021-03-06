package com.github.perscholas.service;

import com.github.perscholas.DatabaseConnection;
import com.github.perscholas.dao.CourseDao;
import com.github.perscholas.model.Course;
import com.github.perscholas.model.CourseInterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// TODO - Implement respective DAO interface
public class CourseService implements CourseDao {

    private final DatabaseConnection connection;

    public CourseService(DatabaseConnection connection) {
        this.connection = connection;
    }

    public List<CourseInterface> getAllCoursesWhere(String request) {
        ResultSet result = connection.executeQuery("SELECT * FROM Courses WHERE " + request + ";");
        List<CourseInterface> courseList = new ArrayList<>();
        try{
            while(result.next()){
                Integer courseId = result.getInt("id");
                String name = result.getString("name");
                String instructor = result.getString("instructor");
                CourseInterface student = new Course(courseId, name, instructor);
                courseList.add(student);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return courseList;
    }


    @Override
    public List<CourseInterface> getAllCourses() {
        return getAllCoursesWhere("true");
    }

    public CourseInterface getCourseById(Integer id){
        return getAllCoursesWhere("`id` = '" + id + "'").get(0);
    }
}
