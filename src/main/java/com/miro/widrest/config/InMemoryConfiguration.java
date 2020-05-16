package com.miro.widrest.config;

import com.miro.widrest.db.InMemoryStorage;
import com.miro.widrest.service.InMemoryWidgetService;
import com.miro.widrest.service.WidgetService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.locks.ReentrantReadWriteLock;

@Configuration(proxyBeanMethods = false)
@Profile("default")
public class InMemoryConfiguration {

    @Bean
    public WidgetService service() {
        return new InMemoryWidgetService(
                new ReentrantReadWriteLock(true),
                new InMemoryStorage()
        );
    }
}
