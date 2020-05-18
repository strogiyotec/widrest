package com.miro.widrest.config;

import com.miro.widrest.db.H2Storage;
import com.miro.widrest.operations.AtomicWidgetOperations;
import com.miro.widrest.operations.H2AtomicOperations;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

/**
 * Config for h2 db.
 */
@Configuration
@Profile("db")
public class H2Config {

    @Bean
    public AtomicWidgetOperations atomicWidgetOperations(final Environment environment) throws IOException {
        return new H2AtomicOperations(
                this.transactionTemplate(environment),
                new H2Storage(this.jdbcTemplate(environment))
        );
    }

    @Bean
    public JdbcTemplate jdbcTemplate(final Environment environment) throws IOException {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource(environment));
        File initSql = new File(getClass().getClassLoader().getResource("schema.sql").getFile());
        jdbcTemplate.execute(Files.readString(initSql.toPath()));
        return jdbcTemplate;
    }

    @Bean
    public TransactionTemplate transactionTemplate(final Environment environment) {
        return new TransactionTemplate(
                new DataSourceTransactionManager(
                        this.dataSource(environment)
                )
        );
    }

    @Bean
    public DataSource dataSource(final Environment environment) {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        hikariConfig.setJdbcUrl(environment.getProperty("spring.datasource.url"));
        hikariConfig.setUsername(environment.getProperty("spring.datasource.username"));
        hikariConfig.setPassword("");
        hikariConfig.setMinimumIdle(Integer.parseInt(Objects.requireNonNull(environment.getProperty("spring.datasource.hikari.minimum-idle"))));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(Objects.requireNonNull(environment.getProperty("spring.datasource.hikari.maximum-pool-size"))));
        return new HikariDataSource(
                hikariConfig
        );
    }
}
