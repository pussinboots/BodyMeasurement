package org.frank.json;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.frank.persistence.BodyMeasurementPojo;
import org.frank.utils.Transformer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Accessors(fluent = true)
@NoArgsConstructor
public class BodyMeasurement extends Transformer.SelfTransformer<BodyMeasurement, BodyMeasurementPojo> {
	private String type, value;

	@Override
	public BodyMeasurementPojo transform(BodyMeasurement entity) {
		return new BodyMeasurementPojo().type(entity.type()).value(entity.value());
	}
}