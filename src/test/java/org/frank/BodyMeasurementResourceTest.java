package org.frank;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.frank.json.ApplicationStatus;
import org.frank.json.BodyMeasurement;
import org.frank.json.JSONResponse;
import org.frank.persistence.BodyMeasurementPojo;
import org.frank.persistence.PersistenceProvider;
import org.frank.persistence.database.BodyMeasurementDB;
import org.frank.persistence.database.JDBCUrlResolver;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class BodyMeasurementResourceTest extends JerseyTest {

    static {
        System.setProperty("JDBC_DATABASE_URL", "jdbc:hsqldb:mem:test");
    }

    private BodyMeasurement expectedMeasurement;

    @Before
    public void before() throws SQLException, IOException {
        Logger.getGlobal().setLevel(Level.FINE);
        setupTestDB();
    }

    private void setupTestDB() throws SQLException, IOException {
        ConnectionSource connectionSource = new JdbcConnectionSource(JDBCUrlResolver.jdbcUrl());
        DaoManager.createDao( connectionSource, BodyMeasurementDB.class ).executeRaw("DROP SCHEMA PUBLIC CASCADE");

        TableUtils.createTableIfNotExists(connectionSource, BodyMeasurementDB.class);
        connectionSource.closeQuietly();

        PersistenceProvider.Storage<BodyMeasurementDB, Long> storage = PersistenceProvider.storage(BodyMeasurementDB.class);
        assertEquals("Ensure an empty test database.", 0, storage.list().size());
        BodyMeasurementDB measurementDB = new BodyMeasurementDB().createdBy("test")
                .measuredAt(Calendar.getInstance().getTime())
                .patientId(UUID.randomUUID().toString())
                .state(BodyMeasurementPojo.State.CREATED)
                .type("Blood Pressure")
                .value("90/140");

        storage.save(measurementDB);
        expectedMeasurement = BodyMeasurement.fromPojo(measurementDB.toPojo());
        storage.save(measurementDB.type("Body Temperature").value("36,5"));
        storage.close();
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(BodyMeasurementResource.class);
    }

    @Test
    public void testGetMetaStatus() {
        final ApplicationStatus applicationStatus = target().path("body/meta/status").request().get(new GenericType<ApplicationStatus>(){});

        assertEquals(ApplicationStatus.State.RUNNING, applicationStatus.states().get(0).state());
        assertEquals("application", applicationStatus.states().get(0).type());
    }

    @Test
    public void testGetNonExistingMeasurement() {
        Response response = target().path("body/measurement/123456").request().get();
        final JSONResponse<BodyMeasurement> jsonResponse = response.readEntity(new GenericType<JSONResponse<BodyMeasurement>>(){});

        assertEquals(200, response.getStatus());
        assertNull(jsonResponse.data().item());
    }

    @Test
    public void testGetMeasurement() {
        final JSONResponse<BodyMeasurement> response = target().path("body/measurement/1").request().get(new GenericType<JSONResponse<BodyMeasurement>>(){});

        assertThat(expectedMeasurement, equalTo(response.data().item()));
    }

    @Test
    public void testGetMeasurements() {
        final JSONResponse<BodyMeasurement> jsonResponse = target().path("body/measurements").request().get(new GenericType<JSONResponse<BodyMeasurement>>(){});

        assertEquals((Integer)2, jsonResponse.data().size());
        assertEquals(2, jsonResponse.data().items().size());
        assertThat(expectedMeasurement, equalTo(jsonResponse.data().asList().get(0)));
        assertThat(expectedMeasurement.withId(2L).withType("Body Temperature").withValue("36,5"), equalTo(jsonResponse.data().asList().get(1)));
    }

    @Test
    public void testSaveMeasurement() {
        final JSONResponse<BodyMeasurement> jsonResponse = target().path("body/measurement").request().post(Entity.json(expectedMeasurement), new GenericType<JSONResponse<BodyMeasurement>>(){});

        assertThat(expectedMeasurement.withId(3L), equalTo(jsonResponse.data().item()));
    }
}
