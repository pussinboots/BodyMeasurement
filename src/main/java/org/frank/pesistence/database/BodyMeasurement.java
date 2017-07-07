package org.frank.pesistence.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class BodyMeasurement {
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
}