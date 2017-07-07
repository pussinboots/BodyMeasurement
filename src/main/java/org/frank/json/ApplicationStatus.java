package org.frank.json;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Accessors(fluent = true)
public class ApplicationStatus {

    public enum State {CONNECTING, RUNNING, ERROR, UNKNOWN;}

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    @Accessors(fluent = true)
    public static class Status {
        private String type;
        private State state;
        private Date checkedAt;
    }

    private Status databaseState;
    private Status applicationState;
}
