package com.github.perscholas;

import com.github.perscholas.utils.DirectoryReference;
import com.github.perscholas.utils.FileReader;
import com.mysql.cj.jdbc.Driver;

// import javax.xml.crypto.Data;
import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConfigurator {
    public static void initialize() {
        registerJdbcDriver();
        createDatabase();
        useDatabase();
        createStudentTable();
        createCourseTable();
        executeSqlFile("students.populate-table.sql");
        executeSqlFile("courses.populate-table.sql");
    }

    private static void registerJdbcDriver() {
        // Attempt to register JDBC Driver
        try {
            DriverManager.registerDriver(Driver.class.newInstance());
        } catch (InstantiationException | IllegalAccessException | SQLException e1) {
            throw new Error(e1);
        }
    }

    private static void createDatabase() {
        DatabaseConnection.MARIADB.executeStatement("CREATE OR REPLACE DATABASE managementSystem;");
    }

    private static void useDatabase() {
        DatabaseConnection.MARIADB.executeStatement("USE managementSystem;");
    }

    private static void createStudentTable() {
        File creationStatementFile = DirectoryReference.RESOURCE_DIRECTORY.getFileFromDirectory("students.create-table.sql");
        FileReader fileReader = new FileReader(creationStatementFile.getAbsolutePath());
        DatabaseConnection.MARIADB.executeStatement(fileReader.toString());
    }

    private static void createCourseTable() {
        File creationStatementFile = DirectoryReference.RESOURCE_DIRECTORY.getFileFromDirectory("courses.create-table.sql");
        FileReader fileReader = new FileReader(creationStatementFile.getAbsolutePath());
        DatabaseConnection.MARIADB.executeStatement(fileReader.toString());
    }


    private static void executeSqlFile(String fileName) {
        File creationStatementFile = DirectoryReference.RESOURCE_DIRECTORY.getFileFromDirectory(fileName);
        FileReader fileReader = new FileReader(creationStatementFile.getAbsolutePath());
        String fileContent = fileReader.toString();
        String[] statements = fileContent.split(";");
        for (int i = 0; i < statements.length; i++) {
            String statement = statements[i];
            DatabaseConnection.MARIADB.executeStatement(statement);
        }
    }
}
