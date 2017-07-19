package org.frank.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class Environment {

    private final static Environment environment = new Environment();

    @Getter(lazy = true) @Setter
    private final EnvironmentConfig config = initConfig();

    private Environment() {}

    private EnvironmentConfig initConfig() {
        return new EnvironmentConfig()
                .jdbcUrl(property("JDBC_DATABASE_URL"))
                .databaseHealthQuery(property("DATABASE_HEALTH_QUERY"));
    }

    @Data
    @Accessors(fluent = true)
    public static class EnvironmentConfig {
        private String jdbcUrl, databaseHealthQuery;
    }

    public static void defaultProperty(String property, String value) {
        if (System.getProperty(property) == null) {
            System.setProperty(property, value);
        }
    }

    public static String property(String property) {
        return (System.getenv(property) != null) ? System.getenv(property) : System.getProperty(property);
    }

    public static void initDefaults() {
        defaultProperty("JDBC_DATABASE_URL", "jdbc:postgresql://localhost:5432?user=postgres");
        defaultProperty("DATABASE_HEALTH_QUERY", "Select 1;");
    }

    public static Environment environment() {
        return environment;
    }
}
