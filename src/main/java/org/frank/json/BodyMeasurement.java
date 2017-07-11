package org.frank.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.Wither;
import org.frank.persistence.BodyMeasurementPojo;
import org.frank.utils.TransformationBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Wither
public class BodyMeasurement extends TransformationBuilder.Transformer.SelfTransformer<BodyMeasurement, BodyMeasurementPojo> {
	private Long id;
	private String type, value, patientId, createdBy;
	public Date measuredAt;

	@Override
	public BodyMeasurementPojo from(BodyMeasurement entity) {
		if (entity == null) return null;
		return new BodyMeasurementPojo().id(entity.id())
				.type(entity.type())
				.value(entity.value())
				.patientId(entity.patientId())
				.createdBy(createdBy())
				.createdAt(Calendar.getInstance().getTime())
				.measuredAt(entity.measuredAt());
	}

	@Override
	public BodyMeasurement to(BodyMeasurementPojo entity) {
		if (entity == null) return null;
		return new BodyMeasurement().id(entity.id())
				.type(entity.type())
				.value(entity.value())
				.patientId(entity.patientId())
				.createdBy(entity.createdBy())
				.measuredAt(entity.measuredAt());
	}
}