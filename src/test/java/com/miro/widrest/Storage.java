package com.miro.widrest;

import com.miro.widrest.db.InMemoryStorage;
import com.miro.widrest.domain.Widget;
import com.miro.widrest.domain.impl.FirstWidget;
import com.miro.widrest.domain.impl.ImmutableWidget;
import com.miro.widrest.domain.impl.MockedWidget;
import com.miro.widrest.operations.InMemoryAtomicOperations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class Storage {

    @Test
    @DisplayName("Test that it creates 20 unique widgets with unique z indexes concurrently")
    public void testCreateConcurrently() throws InterruptedException {
        final int iterations = 20;
        final InMemoryStorage storage = new InMemoryStorage();
        final InMemoryAtomicOperations operations = new InMemoryAtomicOperations(new ReentrantReadWriteLock(true), storage);
        final ExecutorService service = Executors.newFixedThreadPool(10);
        final CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < iterations; i++) {
            service.submit(() -> {
                latch.await();
                operations.create(
                        new ImmutableWidget(
                                1, 1, 10, 10, 10
                        )
                );
                //to use Callable that doesn't require try catch
                return null;
            });
        }
        latch.countDown();
        service.shutdown();
        service.awaitTermination(10, TimeUnit.SECONDS);
        final List<Integer> indexes = Utils.indexes(storage.getAll());
        final long count = indexes
                .stream()
                .distinct()
                .count();
        Assertions.assertEquals(count, iterations);
    }

    @Test
    @DisplayName("Test that when first widget doesn't have z-index then FirstWidget is used")
    public void testAddToEmptyStorage() {
        final InMemoryStorage storage = new InMemoryStorage();
        final InMemoryAtomicOperations operations = new InMemoryAtomicOperations(new ReentrantReadWriteLock(true), storage);
        final Widget widget = new ImmutableWidget(1, 1, null, 1, 1);

        Assertions.assertEquals(
                operations.create(widget).getZ(),
                new FirstWidget(widget).getZ()
        );
    }

    @Test
    @DisplayName("Test that during insert with same z-index, all widgets whose z-index is bigger are moved one level up")
    public void testAddWithZIndex() {
        final InMemoryStorage storage = new InMemoryStorage();
        final InMemoryAtomicOperations operations = new InMemoryAtomicOperations(new ReentrantReadWriteLock(true), storage);
        for (int i = 0; i < 5; i++) {
            operations.create(
                    new MockedWidget(10)
            );
        }
        Assertions.assertEquals(
                Utils.indexes(storage.getAll()),
                List.of(10, 11, 12, 13, 14)
        );
    }
}
