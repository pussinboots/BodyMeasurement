package org.frank.persistence.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.experimental.Accessors;
import org.frank.persistence.BodyMeasurementPojo;
import org.frank.utils.TransformationBuilder;

import java.util.Date;

@Data
@Accessors(fluent = true)
@DatabaseTable
public class BodyMeasurementDB extends TransformationBuilder.Transformer.SelfTransformer<BodyMeasurementPojo, BodyMeasurementDB> {
    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(width=64)
    private String patientId;

    @DatabaseField(width=250)
    private String type;

    @DatabaseField(width=4000)
    private String value;

	@DatabaseField
    private Date createdAt;

    @DatabaseField
    private Date measuredAt;

	@DatabaseField(width=250)
    private String createdBy;

    @DatabaseField
    private BodyMeasurementPojo.State state = BodyMeasurementPojo.State.CREATED;

    @Override
    public BodyMeasurementDB from(BodyMeasurementPojo entity) {
        if (entity == null) return null;
        return new BodyMeasurementDB()
                .id(entity.id())
                .type(entity.type())
                .value(entity.value())
                .createdAt(entity.createdAt())
                .createdBy(entity.createdBy())
                .measuredAt(entity.measuredAt())
                .patientId(entity.patientId())
                .state(entity.state());
    }

    @Override
    public BodyMeasurementPojo to(BodyMeasurementDB entity) {
        if (entity == null) return null;
        return new BodyMeasurementPojo()
                .id(entity.id())
                .type(entity.type())
                .value(entity.value())
                .createdAt(entity.createdAt())
                .createdBy(entity.createdBy())
                .measuredAt(entity.measuredAt())
                .patientId(entity.patientId())
                .state(entity.state());
    }
}