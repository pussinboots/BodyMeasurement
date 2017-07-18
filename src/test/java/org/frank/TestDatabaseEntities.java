package org.frank;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.frank.persistence.BodyMeasurementPojo;
import org.frank.persistence.database.BodyMeasurementDB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Accessors(fluent = true)
@Getter
@Setter
public class TestDatabaseEntities {

    public interface ValueCreator {
        void setValue(BodyMeasurementDB entity, int count);
    }

    private BodyMeasurementDB baseEntity;
    @Setter(AccessLevel.NONE)
    private ValueCreator[] valueCreators;
    private int count = 0;

    public TestDatabaseEntities setValueCreators(ValueCreator ...valueCreators) {
        this.valueCreators = valueCreators;
        return this;
    }

    public List<BodyMeasurementDB> nextEntities(int amount) {
        List<BodyMeasurementDB> entities = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            entities.add(nextEntity(this.valueCreators()));
        }
        return entities;
    }

    public BodyMeasurementDB nextEntity() {
        return nextEntity(this.valueCreators());
    }

    public BodyMeasurementDB nextEntity(ValueCreator ...valueCreators) {
        BodyMeasurementDB entity = BodyMeasurementDB.fromPojo(baseEntity.toPojo());
        for(ValueCreator valueCreator : valueCreators) {
            valueCreator.setValue(entity, count);
        }
        count++;
        return entity;
    }

    public static TestDatabaseEntities entities(BodyMeasurementDB entity) {
        return new TestDatabaseEntities().baseEntity(createEntity(entity));
    }

    public static BodyMeasurementDB createEntity(BodyMeasurementDB entity) {
        BodyMeasurementDB testEntity = new BodyMeasurementDB()
                .createdBy(entity.createdBy())
                .measuredAt(Calendar.getInstance().getTime())
                .patientId(UUID.randomUUID().toString())
                .state(BodyMeasurementPojo.State.CREATED)
                .type(entity.type())
                .value(entity.value());

        if (entity.measuredAt() != null) testEntity.measuredAt(entity.measuredAt());
        if (entity.patientId() != null) testEntity.patientId(entity.patientId());
        if (entity.state() != null) testEntity.state(entity.state());

        return testEntity;
    }
}
