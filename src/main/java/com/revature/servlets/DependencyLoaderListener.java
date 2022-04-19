package com.revature.servlets;
import com.revature.util.ConnectionFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;

public class DependencyLoaderListener implements ServletContextListener {
    Connection con;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //add startup code here
        con= ConnectionFactory.getInstance().getConnection();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //add teardown code here
        ConnectionFactory.closeConnection();
    }
}
