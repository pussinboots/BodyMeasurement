package org.frank.listeners;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.frank.config.Environment;
import org.frank.persistence.database.BodyMeasurementDB;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

public class BootStrapListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {

        Environment.initDefaults();
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource(Environment.environment().config().jdbcUrl());

            TableUtils.createTableIfNotExists( connectionSource, BodyMeasurementDB.class );

            connectionSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {}

}