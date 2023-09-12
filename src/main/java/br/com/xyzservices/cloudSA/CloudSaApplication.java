package br.com.xyzservices.cloudSA;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class CloudSaApplication {
	public static void main(String[] args) {
		SpringApplication.run(CloudSaApplication.class, args);

	}
	@Bean
	DataSource getDataSource() {


		HikariConfig config = new HikariConfig();

		config.setJdbcUrl("jdbc:postgresql://127.0.0.1:5432/pagila");
		config.setUsername("andresousa@google.com");
		config.addDataSourceProperty("ssl", "true");
		config.addDataSourceProperty("sslmode", "disable");
		// TODO: configure client certs

		return new CloudSqlAutoIamAuthnDataSource(config);
	}
}
