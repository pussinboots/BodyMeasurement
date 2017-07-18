package org.frank.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.Wither;
import org.frank.persistence.BodyMeasurementPojo;
import org.frank.utils.TransformationBuilder;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;
import java.util.Date;

@XmlRootElement
@Data
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Wither
public class BodyMeasurement {

	private Long id;
	private String type, value, patientId, createdBy;
	private Date measuredAt;

	public static TransformationBuilder.SimpleTransformer<BodyMeasurement, BodyMeasurementPojo> tranformerToPojo() {
		return entity -> {
            if (entity == null) return null;
            return new BodyMeasurementPojo().id(entity.id())
                    .type(entity.type())
                    .value(entity.value())
                    .patientId(entity.patientId())
                    .createdBy(entity.createdBy())
                    .createdAt(Calendar.getInstance().getTime())
                    .measuredAt(entity.measuredAt());
        };
	}

	public static TransformationBuilder.SimpleTransformer<BodyMeasurementPojo, BodyMeasurement> transformerFromPojo() {
		return entity -> {
			if (entity == null) return null;
			return new BodyMeasurement().id(entity.id())
					.type(entity.type())
					.value(entity.value())
					.patientId(entity.patientId())
					.createdBy(entity.createdBy())
					.measuredAt(entity.measuredAt());
		};
	}

	public static BodyMeasurementPojo toPojo(BodyMeasurement entity) {
		return tranformerToPojo().transform(entity);
	}

	public static BodyMeasurement fromPojo(BodyMeasurementPojo entity) {
		return transformerFromPojo().transform(entity);
	}

	public BodyMeasurementPojo toPojo() {
		return toPojo(this);
	}
}