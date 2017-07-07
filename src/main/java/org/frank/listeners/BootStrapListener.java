package org.frank.listeners;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.frank.persistence.database.BodyMeasurementDB;
import org.frank.persistence.database.JDBCUrlResolver;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

public class BootStrapListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {

        
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource(JDBCUrlResolver.jdbcUrl());

            TableUtils.createTableIfNotExists( connectionSource, BodyMeasurementDB.class );
            /*TableUtils.createTableIfNotExists( connectionSource, Item.class );
            TableUtils.createTableIfNotExists( connectionSource, Project.class );*/

            connectionSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {}

}