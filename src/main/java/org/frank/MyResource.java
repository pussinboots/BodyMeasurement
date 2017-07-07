package org.frank;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.frank.json.BodyMeasurement;
import org.frank.pesistence.database.JDBCUrlResolver;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("myresource")
public class MyResource {

    /*Dao<Item, String> itemDao;
    Dao<Note, Long> noteDao;
    Dao<Project, Long> projectDao;*/

    @PostConstruct
    public void setup() {
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource(JDBCUrlResolver.jdbcUrl());
            /*itemDao = DaoManager.createDao( connectionSource, Item.class );
            noteDao = DaoManager.createDao( connectionSource, Note.class );
            projectDao = DaoManager.createDao( connectionSource, Project.class );*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void pre_destroy () {
        try {
            /*itemDao.getConnectionSource().close();
            noteDao.getConnectionSource().close();
            projectDao.getConnectionSource().close();*/
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Hello, Heroku!";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/measurement")
    public BodyMeasurement bodyMeasurement() {
        BodyMeasurement measurement = new BodyMeasurement().type("Blood Pressure").value("140/90");
        return measurement;
    }
}
