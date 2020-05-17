package com.miro.widrest.config;

import com.miro.widrest.db.InMemoryStorage;
import com.miro.widrest.operations.AtomicWidgetOperations;
import com.miro.widrest.operations.InMemoryAtomicOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.locks.ReentrantReadWriteLock;

@Configuration(proxyBeanMethods = false)
@Profile("default")
public class InMemoryConfiguration {

    @Bean
    public AtomicWidgetOperations atomicOperation() {
        return new InMemoryAtomicOperations(
                new ReentrantReadWriteLock(true),
                new InMemoryStorage()
        );
    }
}
