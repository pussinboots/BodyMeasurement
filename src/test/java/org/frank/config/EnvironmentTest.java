package org.frank.config;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnvironmentTest {

    @BeforeClass
    public static void setupClass() {
        System.setProperty("EXISTING", "value");
        Environment.initDefaults();
    }

    @Test
    public void testDefaultProperty() throws Exception {
        Environment.defaultProperty("NOT_EXISTING", "default");
        assertEquals("default", System.getProperty("NOT_EXISTING"));
    }

    @Test
    public void testProperty() throws Exception {
        assertNull(Environment.property("DKHKDHKJDH"));
        assertEquals("value", Environment.property("EXISTING"));
    }

    @Test
    public void testInitDefaults() throws Exception {
        assertEquals("jdbc:postgresql://localhost:5432?user=postgres", System.getProperty("JDBC_DATABASE_URL"));
        assertEquals("Select 1;", System.getProperty("DATABASE_HEALTH_QUERY"));
    }

    @Test
    public void testEnvironment() {
        assertEquals("Select 1;", Environment.environment().config().databaseHealthQuery());
    }
}