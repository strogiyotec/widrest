package com.miro.widrest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication(proxyBeanMethods = false)
public class WidrestApplication implements CommandLineRunner {

    @Autowired
    private JdbcTemplate template;

    public static void main(String[] args) {
        SpringApplication.run(WidrestApplication.class, args);
    }

    @Override
    public void run(final String... args) throws Exception {
        System.out.println(
                template.queryForList("SELECT * from widgets;")
        );
    }
}
