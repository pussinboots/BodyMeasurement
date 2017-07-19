package org.frank.resources;

import lombok.Data;
import lombok.experimental.Accessors;
import org.frank.json.ApplicationStatus;
import org.frank.json.ApplicationStatus.State;
import org.frank.json.ApplicationStatus.Status;
import org.frank.json.BodyMeasurement;
import org.frank.json.JSONResponse;
import org.frank.persistence.PersistenceProvider;
import org.frank.persistence.database.BodyMeasurementDB;
import org.frank.utils.CollectionsUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static org.frank.utils.CollectionsUtils.Pair.pair;

@Path("body")
public class BodyMeasurementResource {

    public static final int DEFAULT_ITEM_LIMIT = 10;
    public static final int MAX_ITEM_LIMIT = 100;

    @Data
    @Accessors(fluent = true)
    public static class Paging implements PersistenceProvider.Page {
        private @QueryParam("page") @DefaultValue("1") int page = 1;
        private @QueryParam("limit") @DefaultValue(DEFAULT_ITEM_LIMIT + "") long limit;

        //Paging start with first page that is page 1
        public int page() { return ( page > 0 )? page - 1 : 0; }
        public long limit() { return ( limit > 0 && limit <= MAX_ITEM_LIMIT)? limit : DEFAULT_ITEM_LIMIT; }
        @Override
        public long offset() { return page() * limit(); }
    }

    @Data
    @Accessors(fluent = true)
    private static class FilterMeasurements implements PersistenceProvider.StorageFilter<FilterMeasurements> {
        private @QueryParam("patientId") String patientId;

        @Override
        public Map<String, Object> transform(FilterMeasurements entity) {
            return CollectionsUtils.hashMap(pair("patientId", patientId));
        }

        @Override
        public Map<String, Object> transform() { return transform(this); }
    }

    private static class DatabaseStatus {
        public static Status status(PersistenceProvider.Storage storage) {
            Status databaseStatus = new Status().type("database");
            try {
                storage.healthCheck();
                databaseStatus.state(State.RUNNING);
            } catch (SQLException e) {
                databaseStatus.state(State.ERROR);
                databaseStatus.errorMessage(e.getMessage());
            }
            databaseStatus.checkedAt(Calendar.getInstance().getTime());
            return databaseStatus;
        }
    }


    private PersistenceProvider.Storage<BodyMeasurementDB, Long> bodyMeasurementDao;

    @PostConstruct
    public void setup() throws SQLException {
        bodyMeasurementDao = PersistenceProvider.storage(BodyMeasurementDB.class);
    }

    @PreDestroy
    public void pre_destroy () throws IOException {
        bodyMeasurementDao.close();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/meta/status")
    public ApplicationStatus status() {
        return new ApplicationStatus()
                .addStatus(new Status().type("application").state(State.RUNNING).checkedAt(Calendar.getInstance().getTime()))
                .addStatus(DatabaseStatus.status(bodyMeasurementDao));
    }

    @GET
    @Path("measurement/{measurementId}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONResponse<BodyMeasurement> getMeasurement(@PathParam("measurementId") Long measurementId) throws SQLException {
        BodyMeasurement bodyMeasurement = BodyMeasurement.fromPojo(bodyMeasurementDao.getAs(measurementId, BodyMeasurementDB.tranformerToPojo()));
        return new JSONResponse<BodyMeasurement>().item(bodyMeasurement);
    }

    @GET
    @Path("measurements")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONResponse<BodyMeasurement> getMeasurements(@BeanParam FilterMeasurements filterMeasurements, @BeanParam Paging paging) throws SQLException {
        List<BodyMeasurement> bodyMeasurements = bodyMeasurementDao.listAs(filterMeasurements, paging, entity -> BodyMeasurement.fromPojo(entity.toPojo()));
        return new JSONResponse<BodyMeasurement>().items(bodyMeasurements);
    }

    @POST
    @Path("measurement")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JSONResponse<BodyMeasurement> saveMeasurement(BodyMeasurement bodyMeasurement) throws SQLException {
        BodyMeasurementDB bodyMeasurementDB = bodyMeasurementDao.save(new BodyMeasurementDB().fromPojo(bodyMeasurement.toPojo()));
        return new JSONResponse<BodyMeasurement>().item(BodyMeasurement.fromPojo(bodyMeasurementDB.toPojo()));
    }
}
