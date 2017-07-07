package org.frank;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BodyMeasurementResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(BodyMeasurementResource.class);
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt() {
        final String responseMsg = target().path("body/status").request().get(String.class);

        assertEquals("Hello, Heroku!", responseMsg);
    }


    @Test
    public void testMeasurement() {
        final String responseMsg = target().path("body/measurement").request().get(String.class);

        assertEquals("{\"type\":\"Blood Pressure\",\"value\":\"140/90\"}", responseMsg);
    }
}
