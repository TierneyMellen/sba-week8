package com.github.perscholas;

import java.sql.*;

public enum DatabaseConnection {

    //MYSQL,
    MARIADB;

    private Connection getConnection() {
        String username = "root";
        String password = "";
        String url = "jdbc:" + name().toLowerCase() + "://127.0.0.1/";
        String databaseName = "managementSystem";
        try {
            return DriverManager.getConnection(url + databaseName, username, password);
        } catch (SQLException e) {
            try {
                return DriverManager.getConnection(url, username, password);
            }catch (SQLException e1) {
                throw new Error(e1);
            }
        }
    }

/*
    private Statement getScrollableStatement() {
        int resultSetType = ResultSet.TYPE_SCROLL_INSENSITIVE;
        int resultSetConcurrency = ResultSet.CONCUR_READ_ONLY;
        try {
            return getConnection().createStatement(resultSetType, resultSetConcurrency);
        } catch (SQLException e) {
            throw new Error(e);
        }
    }
*/
    public void executeStatement(String sqlStatement) {
        try {
            Statement statement = getConnection().createStatement();
            statement.executeQuery(sqlStatement);
        } catch (SQLException e) {
            String errorMessage = String.format("Error executing statement `%s`", sqlStatement);
            throw new Error(errorMessage, e);
        }
    }

    public ResultSet executeQuery(String sqlQuery) {
        try {
            Statement statement = getConnection().createStatement();
            return statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            String errorMessage = String.format("Error executing query `%s`", sqlQuery);
            throw new Error(errorMessage, e);
        }
    }

}
