package org.frank.persistence;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@ToString
@Accessors(fluent = true)
public class BodyMeasurementPojo {
    public String patientId, type, value, createdBy;
    public Date createdAt, measuredAt;
}