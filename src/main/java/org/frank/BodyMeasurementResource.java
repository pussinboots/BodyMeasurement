package org.frank;

import org.frank.json.ApplicationStatus;
import org.frank.json.ApplicationStatus.State;
import org.frank.json.ApplicationStatus.Status;
import org.frank.json.BodyMeasurement;
import org.frank.json.Items;
import org.frank.persistence.PersistenceProvider;
import org.frank.persistence.database.BodyMeasurementDB;
import org.frank.utils.TransformationBuilder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

@Path("body")
public class BodyMeasurementResource {

    private PersistenceProvider.Storage<BodyMeasurementDB, Long> bodyMeasurementDao;

    @PostConstruct
    public void setup() {
        try {
            bodyMeasurementDao = PersistenceProvider.storage(BodyMeasurementDB.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void pre_destroy () {
        try {
            bodyMeasurementDao.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/meta/status")
    public ApplicationStatus status() {
        return new ApplicationStatus().applicationState(new Status().type("application").state(State.RUNNING).checkedAt(Calendar.getInstance().getTime()));
    }

    @GET
    @Path("measurement/{measurementId}")
    @Produces(MediaType.APPLICATION_JSON)
    public BodyMeasurement getMeasurement(@PathParam("measurementId") Long measurementId) throws SQLException {
        BodyMeasurement bodyMeasurement = new BodyMeasurement().to(bodyMeasurementDao.get(measurementId).to());
        return bodyMeasurement;
    }

    @GET
    @Path("measurements")
    @Produces(MediaType.APPLICATION_JSON)
    public Items<BodyMeasurement> getMeasurements() throws SQLException {
        List<BodyMeasurement> bodyMeasurements = bodyMeasurementDao.listAs(new TransformationBuilder.ListTransformer<BodyMeasurementDB, BodyMeasurement>() {
            @Override
            public BodyMeasurement transform(BodyMeasurementDB entity) {
                return new BodyMeasurement().to(entity.to());
            }
        });
        return new Items(bodyMeasurements);
    }

    @POST
    @Path("measurement")
    @Consumes(MediaType.APPLICATION_JSON)
    public BodyMeasurement saveMeasurement(BodyMeasurement bodyMeasurement) throws SQLException {
        bodyMeasurementDao.save(new BodyMeasurementDB().from(bodyMeasurement.from()));
        return bodyMeasurement;
    }
}
