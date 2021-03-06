package org.frank.resources;

import org.frank.TestDatabase;
import org.frank.TestDatabaseEntities;
import org.frank.config.Environment;
import org.frank.json.ApplicationStatus;
import org.frank.json.BodyMeasurement;
import org.frank.json.JSONResponse;
import org.frank.persistence.database.BodyMeasurementDB;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class BodyMeasurementResourceTest extends JerseyTest {

    static {
        Environment.environment().config()
                .jdbcUrl("jdbc:hsqldb:mem:test")
                .databaseHealthQuery("SELECT * FROM INFORMATION_SCHEMA.SYSTEM_TABLES;");
    }

    public static void logToStdOut (String className)
    {
        Logger logger = Logger.getLogger(className);
        new ConsoleHandler(){ { setLevel(Level.ALL); logger.setLevel(Level.FINE); logger.addHandler(this); } };
    }

    private BodyMeasurement expectedMeasurementOne;
    private BodyMeasurement expectedMeasurementTwo;

    @Before
    public void before() throws SQLException, IOException {

        logToStdOut("org.glassfish.grizzly.http.server.HttpHandler");
        logToStdOut("org.glassfish.grizzly.http");
        setupTestDB();
    }

    private void setupTestDB() throws SQLException, IOException {
        BodyMeasurementDB measurementDBOne = TestDatabaseEntities.createEntity(new BodyMeasurementDB()
                .createdBy("test")
                .type("Blood Pressure")
                .value("90/140")
        );

        BodyMeasurementDB measurementDBTwo = TestDatabaseEntities.createEntity(new BodyMeasurementDB()
                .createdBy("test")
                .type("Body Temperature")
                .value("36,5")
        );

        List<BodyMeasurement> inserted = TestDatabase.db(BodyMeasurementDB.class)
                .startDatabase()
                .connect()
                .insert(measurementDBOne, measurementDBTwo)
                .close()
                .insertedAs(entity -> BodyMeasurement.fromPojo(entity.toPojo()));

        expectedMeasurementOne = inserted.get(0);
        expectedMeasurementTwo = inserted.get(1);
    }

    @Override
    protected DeploymentContext configureDeployment() {
        ResourceConfig resourceConfig = ResourceConfig.forApplication(new BodyMeasurementApplication());
        ServletDeploymentContext deploymentContext = ServletDeploymentContext.forServlet(new ServletContainer(resourceConfig))
                .build();
        return deploymentContext;
    }


    @Override
    protected TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }

    @Test
    public void testGetMetaStatus() {
        final ApplicationStatus applicationStatus = target().path("body/meta/status").request().get(new GenericType<ApplicationStatus>(){});

        assertEquals(2, applicationStatus.states().size());
        assertEquals(ApplicationStatus.State.RUNNING, applicationStatus.states().get(0).state());
        assertEquals(ApplicationStatus.State.RUNNING, applicationStatus.states().get(1).state());
        assertEquals("application", applicationStatus.states().get(0).type());
        assertEquals("database", applicationStatus.states().get(1).type());
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

        assertThat(expectedMeasurementOne, equalTo(response.data().item()));
    }

    @Test
    public void testGetMeasurements() {
        final JSONResponse<BodyMeasurement> jsonResponse = target().path("body/measurements").request().get(new GenericType<JSONResponse<BodyMeasurement>>(){});

        assertEquals((Integer)2, jsonResponse.data().size());
        assertEquals(2, jsonResponse.data().items().size());
        assertThat(expectedMeasurementOne, equalTo(jsonResponse.data().asList().get(0)));
        assertThat(expectedMeasurementTwo, equalTo(jsonResponse.data().asList().get(1)));
    }

    @Test
    public void testGetMeasurementsFilterByPatientId() {
        final JSONResponse<BodyMeasurement> jsonResponse = target().path("body/measurements").queryParam("patientId", expectedMeasurementOne.patientId()).request().get(new GenericType<JSONResponse<BodyMeasurement>>(){});

        assertEquals((Integer)1, jsonResponse.data().size());
        assertEquals(1, jsonResponse.data().items().size());
        assertThat(expectedMeasurementOne, equalTo(jsonResponse.data().asList().get(0)));
    }

    @Test
    public void testSaveMeasurement() {
        final JSONResponse<BodyMeasurement> jsonResponse = target().path("body/measurement").request().post(Entity.json(expectedMeasurementOne), new GenericType<JSONResponse<BodyMeasurement>>(){});

        assertThat(expectedMeasurementOne.withId(3L), equalTo(jsonResponse.data().item()));
    }
}
