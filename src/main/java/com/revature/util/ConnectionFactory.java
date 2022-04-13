package com.revature.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * <p>This ConnectionFactory class follows the Singleton Design Pattern and facilitates obtaining a connection to a Database for the ERS application.</p>
 * <p>Following the Singleton Design Pattern, the provided Constructor is private, and you obtain an instance via the {@link ConnectionFactory#getInstance()} method.</p>
 */
public class ConnectionFactory {

    private static ConnectionFactory instance;
    private static Connection connection;

    private ConnectionFactory() {

    }

    /**
     * <p>This method follows the Singleton Design Pattern to restrict this class to only having 1 instance.</p>
     * <p>It is invoked via:</p>
     *
     * {@code ConnectionFactory.getInstance()}
     */
    //Creates an instance of ConnectionFactory if one doesn't exist. Returns the existing ConnectionFactory otherwise.
    public static ConnectionFactory getInstance() {
        if(instance == null) {
            instance = new ConnectionFactory();
        }

        return instance;
    }

    /*public static void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * <p>The {@link ConnectionFactory#getConnection()} method is responsible for leveraging a specific Database Driver to obtain an instance of the {@link java.sql.Connection} interface.</p>
     * <p>Typically, this is accomplished via the use of the {@link java.sql.DriverManager} class.</p>
     */
    // Code required to actually connect to the Database
    public Connection getConnection() {
        //Creates a Hashtable to store values and gets the context of the current thread
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        //loads in application.properties file as input
        InputStream input = loader.getResourceAsStream("application.properties");
        //code to handle IOExceptions
        try {
            props.load(input);
        } catch (IOException e) {
            System.out.println("Couldn't get the input");
            e.printStackTrace();
        }
        //creates a connection string using input from application.properties
        String connectionString = "jdbc:postgresql://" +
                props.getProperty("hostname") + ":" +
                props.getProperty("port") + "/" +
                props.getProperty("dbname") + "?schemaName="+
                props.getProperty("schemaName");

        String username = props.getProperty("username");
        String password = props.getProperty("password");

        try {
            connection = DriverManager.getConnection(connectionString, username, password);
        } catch (SQLException e) {
            System.out.println("Error connecting to Kyle's Database");
            e.printStackTrace();
        }

        System.out.println("Connection String: " + connectionString);

        return connection;
    }
}
