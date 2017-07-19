package org.frank.resources;

import org.frank.TestDatabase;
import org.frank.TestDatabaseEntities;
import org.frank.config.Environment;
import org.frank.json.BodyMeasurement;
import org.frank.json.JSONResponse;
import org.frank.persistence.BodyMeasurementPojo;
import org.frank.persistence.database.BodyMeasurementDB;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class BodyMeasurementResourcePagingTest extends JerseyTest {

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

    private BodyMeasurement expectedBaseEntity;
    private List<BodyMeasurement> expectedInserted;

    @Before
    public void before() throws SQLException, IOException {

        logToStdOut("org.glassfish.grizzly.http.server.HttpHandler");
        setupTestDB();
    }

    private void setupTestDB() throws SQLException, IOException {

        BodyMeasurementDB baseEntity = new BodyMeasurementDB().createdBy("test")
                .measuredAt(Calendar.getInstance().getTime())
                .patientId(UUID.randomUUID().toString())
                .state(BodyMeasurementPojo.State.CREATED)
                .type("Blood Pressure")
                .value("90/140");
        expectedBaseEntity = BodyMeasurement.fromPojo(baseEntity.toPojo());

        List<BodyMeasurementDB> entities = TestDatabaseEntities.entities(baseEntity)
                .setValueCreators((entity, count) -> {
                    Random random = new Random();
                    int asitolic = random.nextInt(120);
                    int desitolic = random.nextInt(180);
                    entity.value(String.format("%s/%s", asitolic, desitolic));

                }, (entity, count) -> {
                    Calendar date = Calendar.getInstance();
                    date.add(Calendar.DAY_OF_YEAR, (-1)*count);
                    entity.measuredAt(date.getTime());
                }).nextEntities(100);


        expectedInserted = TestDatabase.db(BodyMeasurementDB.class)
                .startDatabase()
                .connect()
                .insert(entities)
                .close()
                .insertedAs(entity -> BodyMeasurement.fromPojo(entity.toPojo()));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(BodyMeasurementResource.class);
    }

    @Test
    public void testGetMeasurementsNotExistingPage() {
        int page = 200;
        final JSONResponse<BodyMeasurement> jsonResponse = target().path("body/measurements").queryParam("page", page).request().get(new GenericType<JSONResponse<BodyMeasurement>>(){});

        assertEquals((Integer)0, jsonResponse.data().size());
        assertEquals(0, jsonResponse.data().items().size());
    }

    @Test
    //if limit is over max limit then the default limit is returned
    public void testGetMeasurementsOverMaxLimit() {
        int page = 1;
        int limit = 1000;
        final JSONResponse<BodyMeasurement> jsonResponse = target().path("body/measurements").queryParam("page", page).queryParam("limit", limit).request().get(new GenericType<JSONResponse<BodyMeasurement>>(){});

        assertEquals((Integer)10, jsonResponse.data().size());
        assertEquals(10, jsonResponse.data().items().size());
    }

    @Test
    //invalid paging will return always the first page
    public void testGetMeasurementsInvalidPage() {
        int page = -1029;
        int limit = -12;
        final JSONResponse<BodyMeasurement> jsonResponse = target().path("body/measurements").queryParam("page", page).queryParam("limit", limit).request().get(new GenericType<JSONResponse<BodyMeasurement>>(){});

        assertEquals((Integer)10, jsonResponse.data().size());
        assertEquals(10, jsonResponse.data().items().size());
    }

    @Test
    public void testGetMeasurementsWithDefaultPaging() {
        final JSONResponse<BodyMeasurement> jsonResponse = target().path("body/measurements").request().get(new GenericType<JSONResponse<BodyMeasurement>>(){});

        assertEquals((Integer)10, jsonResponse.data().size());
        assertEquals(10, jsonResponse.data().items().size());
        for(int i = 0; i < jsonResponse.data().size(); i++) {
            assertThat(expectedInserted.get(i), IsEqual.equalTo(jsonResponse.data().asList().get(i)));
        }
    }

    @Test
    public void testGetMeasurementsTheSecondPage() {
        int page = 2;
        final JSONResponse<BodyMeasurement> jsonResponse = target().path("body/measurements").queryParam("page", page).request().get(new GenericType<JSONResponse<BodyMeasurement>>(){});

        assertEquals((Integer)10, jsonResponse.data().size());
        assertEquals(10, jsonResponse.data().items().size());
        for(int i = 0; i < jsonResponse.data().size(); i++) {
            assertThat(expectedInserted.get((page - 1) * jsonResponse.data().size() + i), IsEqual.equalTo(jsonResponse.data().asList().get(i)));
        }
    }

    @Test
    public void testGetMeasurementsTheLastPage() {
        int page = 10;
        final JSONResponse<BodyMeasurement> jsonResponse = target().path("body/measurements").queryParam("page", page).request().get(new GenericType<JSONResponse<BodyMeasurement>>(){});

        assertEquals((Integer)10, jsonResponse.data().size());
        assertEquals(10, jsonResponse.data().items().size());
        for(int i = 0; i < jsonResponse.data().size(); i++) {
            assertThat(expectedInserted.get((page - 1) * jsonResponse.data().size() + i), IsEqual.equalTo(jsonResponse.data().asList().get(i)));
        }
    }

    @Test
    public void testGetMeasurementsTheThirdPageWithSizeThree() {
        int page = 3;
        int limit = 3;
        final JSONResponse<BodyMeasurement> jsonResponse = target().path("body/measurements").queryParam("page", page).queryParam("limit", limit).request().get(new GenericType<JSONResponse<BodyMeasurement>>(){});

        assertEquals((Integer)limit, jsonResponse.data().size());
        assertEquals(limit, jsonResponse.data().items().size());
        for(int i = 0; i < jsonResponse.data().size(); i++) {
            assertThat(expectedInserted.get((page - 1) * jsonResponse.data().size() + i), IsEqual.equalTo(jsonResponse.data().asList().get(i)));
        }
    }
}
