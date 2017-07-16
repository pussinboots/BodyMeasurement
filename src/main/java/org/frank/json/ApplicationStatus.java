package org.frank.json;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Accessors(fluent = true)
public class ApplicationStatus {

    public enum State {CONNECTING, RUNNING, ERROR, UNKNOWN}

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    @Accessors(fluent = true)
    public static class Status {
        private String type;
        private State state;
        private Date checkedAt;
    }

    private List<Status> states = new ArrayList<Status>();

    public ApplicationStatus addStatus(Status status) {
        states.add(status);
        return this;
    }
}
