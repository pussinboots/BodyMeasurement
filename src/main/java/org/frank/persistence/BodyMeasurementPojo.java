package org.frank.persistence;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@ToString
@Accessors(fluent = true)
public class BodyMeasurementPojo {
    public  enum State{CREATED, VERIFIED, REPLACED, DELETED}

    private Long id;
    private String patientId, type, value, createdBy;
    private Date createdAt, measuredAt;
    private State state;
}