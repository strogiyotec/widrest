package com.miro.widrest.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Config for h2 db.
 */
@Configuration
@Profile("db")
public class DbConfig {

    @Bean
    public JdbcTemplate jdbcTemplate(final Environment environment) throws IOException {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource(environment));
        File initSql = new File(getClass().getClassLoader().getResource("schema.sql").getFile());
        jdbcTemplate.execute(Files.readString(initSql.toPath()));
        return jdbcTemplate;
    }

    @Bean
    public DataSource dataSource(final Environment environment) {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        hikariConfig.setJdbcUrl(environment.getProperty("spring.datasource.url"));
        hikariConfig.setUsername(environment.getProperty("spring.datasource.username"));
        hikariConfig.setPassword("");
        return new HikariDataSource(
                hikariConfig
        );
    }
}
