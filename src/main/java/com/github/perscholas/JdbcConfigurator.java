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
        DatabaseConnection dbc = DatabaseConnection.MARIADB;
        dbc.executeStatement("CREATE DATABASE IF NOT EXISTS managementSystem;");
        dbc.executeStatement("USE managementSystem;");

        createTable("courses.create-table.sql");
        createTable("students.create-table.sql");
        createTable("registration.create-table.sql");

        populateTable("students.populate-table.sql");
        populateTable("courses.populate-table.sql");
    }

    private static void registerJdbcDriver() {
        // Attempt to register JDBC Driver
        try {
            DriverManager.registerDriver(Driver.class.newInstance());
        } catch (InstantiationException | IllegalAccessException | SQLException e1) {
            throw new Error(e1);
        }
    }
    private static void createTable(String fileName) {
        File creationStatementFile = DirectoryReference.RESOURCE_DIRECTORY.getFileFromDirectory(fileName);
        FileReader fileReader = new FileReader(creationStatementFile.getAbsolutePath());
        DatabaseConnection.MARIADB.executeStatement(fileReader.toString());
    }

    private static void populateTable(String fileName) {
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
