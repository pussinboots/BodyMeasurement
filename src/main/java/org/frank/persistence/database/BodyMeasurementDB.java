package org.frank.persistence.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.experimental.Accessors;
import org.frank.json.BodyMeasurement;
import org.frank.persistence.BodyMeasurementPojo;
import org.frank.utils.Transformer;

import java.util.Date;

@Data
@Accessors(fluent = true)
@DatabaseTable
public class BodyMeasurementDB extends Transformer.SelfTransformer<BodyMeasurementPojo, BodyMeasurementDB> {
    @DatabaseField(generatedId = true)
    public long id;

    @DatabaseField(width=64)
    public String patientId;

    @DatabaseField(width=250)
    public String type;

    @DatabaseField(width=4000)
    public String value;

	@DatabaseField
    public Date createdAt;

    @DatabaseField
    public Date measuredAt;

	@DatabaseField(width=250)
    public String createdBy;

    @Override
    public BodyMeasurementDB transform(BodyMeasurementPojo entity) {
        return new BodyMeasurementDB()
                .type(entity.type())
                .value(entity.value())
                .createdAt(entity.createdAt())
                .createdBy(entity.createdBy())
                .measuredAt(entity.measuredAt())
                .patientId(entity.patientId());
    }
}