package com.github.perscholas;

import com.github.perscholas.dao.StudentDao;
import com.github.perscholas.model.CourseInterface;
import com.github.perscholas.service.StudentService;
import com.github.perscholas.dao.CourseDao;
import com.github.perscholas.service.CourseService;
import com.github.perscholas.utils.IOConsole;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SchoolManagementSystem implements Runnable {
    private static final IOConsole console = new IOConsole();

    @Override
    public void run() {
        String smsDashboardInput;
        do{
            smsDashboardInput = getSchoolManagementSystemDashboardInput();
            if ("login".equals(smsDashboardInput)) {

                StudentDao studentService = new StudentService(DatabaseConnection.MANAGEMENTSYSTEM); // TODO - get literal value

                String studentEmail = console.getStringInput("Enter your email:");
                String studentPassword = console.getStringInput("Enter your password:");
                Boolean isValidLogin = studentService.validateStudent(studentEmail, studentPassword);
                if (isValidLogin) {
                    String studentDashboardInput = getStudentDashboardInput();
                    if ("register".equals(studentDashboardInput)) {
                        Integer courseId = getCourseRegistryInput();
                        studentService.registerStudentToCourse(studentEmail, courseId);
                        String studentCourseViewInput = getCourseViewInput();
                        if ("view".equals(studentCourseViewInput)) {
                            List<CourseInterface> courses = studentService.getStudentCourses(studentEmail); // TODO - Instantiate and populate `courses`;
                            console.println(new StringBuilder()
                                    .append("[ %s ] is registered to the following courses:")
                                    .append("\n\t" + courses)
                                    .toString(), studentEmail);
                        }
                    }
                }
            }
        }while(!"logout".equals(smsDashboardInput));
    }

    private String getCourseViewInput() {
        return console.getStringInput(new StringBuilder()
                .append("Welcome to the Course View Dashboard!")
                .append("\nFrom here, you can select any of the following options:")
                .append("\n\t[ view ], [ logout ]")
                .toString());
    }

    private String getSchoolManagementSystemDashboardInput() {
        return console.getStringInput(new StringBuilder()
                .append("Welcome to the School Management System Dashboard!")
                .append("\nFrom here, you can select any of the following options:")
                .append("\n\t[ login ], [ logout ]")
                .toString());
    }

    private String getStudentDashboardInput() {
        return console.getStringInput(new StringBuilder()
                .append("Welcome to the Student Dashboard!")
                .append("\nFrom here, you can select any of the following options:")
                .append("\n\t[ register ], [ logout]")
                .toString());
    }


    private Integer getCourseRegistryInput() {
        CourseService courseService = new CourseService(DatabaseConnection.MANAGEMENTSYSTEM);
        Map<Integer, String> mapOfCourses = courseService.getAllCourses()
                .stream()
                .collect(Collectors.toMap(CourseInterface::getId, CourseInterface::getName));
        return console.getIntegerInput(new StringBuilder()
                .append("Welcome to the Course Registration Dashboard!")
                .append("\nFrom here, you can select any of the following options:")
                .append("\n\t" + mapOfCourses.toString())
                .toString());
    }
}
