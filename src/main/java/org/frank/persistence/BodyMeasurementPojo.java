package org.frank.persistence;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.frank.persistence.database.BodyMeasurementDB;
import org.frank.utils.TransformationBuilder;

import java.util.Date;

@Data
@ToString
@Accessors(fluent = true)
public class BodyMeasurementPojo extends TransformationBuilder.Transformer.SelfTransformer<BodyMeasurementDB, BodyMeasurementPojo> {

    public  enum State{CREATED, VERIFIED, REPLACED, DELETED};

    private Long id;
    private String patientId, type, value, createdBy;
    private Date createdAt, measuredAt;
    private State state;

    @Override
    public BodyMeasurementPojo from(BodyMeasurementDB entity) {
        if (entity == null) return null;
        return new BodyMeasurementPojo().id(entity.id())
                .patientId(entity.patientId())
                .type(entity.type())
                .value(entity.value())
                .createdBy(entity.createdBy())
                .createdAt(entity.createdAt())
                .measuredAt(entity.measuredAt());
    }

    @Override
    public BodyMeasurementDB to(BodyMeasurementPojo entity) {
        if (entity == null) return null;
        return new BodyMeasurementDB().id(entity.id)
                .patientId(entity.patientId())
                .type(entity.type())
                .value(entity.value())
                .createdBy(entity.createdBy())
                .createdAt(entity.createdAt())
                .measuredAt(entity.measuredAt());
    }
}