package com.ecommerce.backend;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {

		// Force Java to use the correct timezone
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));

		validateRailwayDatabaseConfig();
		configureDatabaseFromHostedUrl();

		SpringApplication.run(BackendApplication.class, args);
	}

	private static void validateRailwayDatabaseConfig() {
		boolean runningOnRailway = firstPresent(
				System.getenv("RAILWAY_ENVIRONMENT_ID"),
				System.getenv("RAILWAY_SERVICE_ID"),
				System.getenv("RAILWAY_PROJECT_ID")) != null;

		if (!runningOnRailway) {
			return;
		}

		boolean hasDatabaseConfig = firstPresent(
				System.getenv("SPRING_DATASOURCE_URL"),
				System.getenv("DATABASE_JDBC_URL"),
				System.getenv("DATABASE_URL"),
				System.getenv("POSTGRES_URL"),
				System.getenv("PGHOST")) != null;

		if (!hasDatabaseConfig) {
			throw new IllegalStateException(
					"Missing Railway database configuration. Add DATABASE_URL, DATABASE_JDBC_URL, "
							+ "SPRING_DATASOURCE_URL, or Railway Postgres variables PGHOST/PGPORT/PGDATABASE/PGUSER/PGPASSWORD "
							+ "to the shopsphere-backend service.");
		}
	}

	private static void configureDatabaseFromHostedUrl() {
		String configuredUrl = firstPresent(
				System.getenv("SPRING_DATASOURCE_URL"),
				System.getenv("DATABASE_URL"),
				System.getenv("POSTGRES_URL"));

		if (configuredUrl == null || !isHostedPostgresUrl(configuredUrl)) {
			return;
		}

		URI databaseUri = URI.create(configuredUrl);

		String jdbcUrl = "jdbc:postgresql://%s:%d%s".formatted(
				databaseUri.getHost(),
				databaseUri.getPort() == -1 ? 5432 : databaseUri.getPort(),
				databaseUri.getPath());

		if (databaseUri.getQuery() != null && !databaseUri.getQuery().isBlank()) {
			jdbcUrl += "?" + databaseUri.getQuery();
		}

		System.setProperty("spring.datasource.url", jdbcUrl);

		String userInfo = databaseUri.getUserInfo();

		if (userInfo != null) {
			String[] credentials = userInfo.split(":", 2);

			setIfMissing(
					"spring.datasource.username",
					"SPRING_DATASOURCE_USERNAME",
					decode(credentials[0]));

			if (credentials.length > 1) {
				setIfMissing(
						"spring.datasource.password",
						"SPRING_DATASOURCE_PASSWORD",
						decode(credentials[1]));
			}
		}
	}

	private static boolean isHostedPostgresUrl(String url) {
		return url.startsWith("postgres://") || url.startsWith("postgresql://");
	}

	private static String firstPresent(String... values) {
		for (String value : values) {
			if (value != null && !value.isBlank()) {
				return value;
			}
		}
		return null;
	}

	private static void setIfMissing(String propertyName, String envName, String value) {
		if ((System.getenv(envName) == null || System.getenv(envName).isBlank()) && value != null) {
			System.setProperty(propertyName, value);
		}
	}

	private static String decode(String value) {
		try {
			return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException ex) {
			return value;
		}
	}
}