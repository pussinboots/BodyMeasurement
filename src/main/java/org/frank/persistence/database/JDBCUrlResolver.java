package org.frank.persistence.database;

public class JDBCUrlResolver {
	private static String jdbcUrl;
	static {
		if (System.getProperty("JDBC_DATABASE_URL") == null) {
			System.setProperty("JDBC_DATABASE_URL", "jdbc:postgresql://localhost:5432?user=postgres");
		}
		jdbcUrl = (System.getenv("JDBC_DATABASE_URL") != null) ? System.getenv("JDBC_DATABASE_URL") : System.getProperty("JDBC_DATABASE_URL");
	}

	private JDBCUrlResolver() {}

	public static String jdbcUrl() { return jdbcUrl; }
}