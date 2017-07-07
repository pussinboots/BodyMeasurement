package org.frank.pesistence.database;

public class JDBCUrlResolver {
	private static String jdbcUrl;
	static {
		jdbcUrl = (System.getenv("JDBC_DATABASE_URL") != null) ? System.getenv("JDBC_DATABASE_URL") : "postgresql://localhost:5432?user=postgres";
	}

	private JDBCUrlResolver() {}

	public static String jdbcUrl() { return jdbcUrl; }
}