package com.github.perscholas;

        import com.github.perscholas.utils.*;

        import java.sql.Connection;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.sql.Statement;

/**
 * Created by leon on 2/18/2020.
 */
public enum DatabaseConnection implements DatabaseConnectionInterface {
    MANAGEMENTSYSTEM;

    private static final IOConsole console = new IOConsole(IOConsole.AnsiColor.PURPLE);
    private final com.github.perscholas.utils.ConnectionBuilder connectionBuilder;

    DatabaseConnection(com.github.perscholas.utils.ConnectionBuilder connectionBuilder) {
        this.connectionBuilder = connectionBuilder;
    }

    DatabaseConnection() {
        this(new ConnectionBuilder()
                .setUser("root")
                .setPassword("")
                .setPort(3306)
                .setDatabaseVendor("mariadb")
                .setHost("127.0.0.1"));
    }

    @Override
    public String getDatabaseName() {
        return name().toLowerCase();
    }

    @Override
    public Connection getDatabaseConnection() {
        return connectionBuilder
                .setDatabaseName(getDatabaseName())
                .build();
    }

    @Override
    public Connection getDatabaseEngineConnection() {
        return connectionBuilder.build();
    }

    @Override
    public void create() {
        String sqlStatement = "CREATE DATABASE IF NOT EXIST " + name().toLowerCase() + ";"; // TODO - define statement
        String info;
        try {
            // TODO - execute statement
            getDatabaseEngineConnection().prepareStatement(sqlStatement).execute();
            info = "Successfully executed statement `%s`.";
        } catch (Exception sqlException) {
            info = "Failed to executed statement `%s`.";
        }
        console.println(info, sqlStatement);
    }

    @Override
    public void drop() {
        String sqlStatement = "DROP " + name().toLowerCase() + ";";
        String info;
        try {
            getDatabaseEngineConnection().prepareStatement(sqlStatement).execute();
            info = "Successfully executed statement `%s`.";
        } catch (Exception sqlException) {
            info = "Failed to executed statement `%s`.";
        }
        console.println(info, sqlStatement);
    }

    @Override
    public void use() {
        String sqlStatement = "USE " + name().toLowerCase() + ";";
        String info;
        try {
            getDatabaseEngineConnection().prepareStatement(sqlStatement).execute();
            info = "Successfully executed statement `%s`.";
        } catch (Exception sqlException) {
            info = "Failed to executed statement `%s`.";
        }
        console.println(info, sqlStatement);
    }

    @Override
    public void executeStatement(String sqlStatement) {
        try {
            Connection connection = DatabaseConnection.MANAGEMENTSYSTEM.getDatabaseConnection();
            Statement statement = connection.createStatement();
            statement.executeQuery(sqlStatement);
        } catch (SQLException e) {
            String errorMessage = String.format("Error executing statement `%s`", sqlStatement);
            throw new Error(errorMessage, e);
        }
    }

    @Override
    public ResultSet executeQuery(String sqlQuery) {
        try {
            Connection connection = DatabaseConnection.MANAGEMENTSYSTEM.getDatabaseConnection();
            Statement statement = connection.createStatement();
            return statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            String errorMessage = String.format("Error executing query `%s`", sqlQuery);
            throw new Error(errorMessage, e);
        }
    }

}