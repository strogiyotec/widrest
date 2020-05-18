package com.miro.widrest;

import com.miro.widrest.db.InMemoryStorage;
import com.miro.widrest.db.WidgetStorage;
import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Pageable;
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

public final class InMemoryAtomicOperationsTestCase {

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
        service.awaitTermination(20, TimeUnit.SECONDS);
        final List<Integer> indexes = Utils.indexes(storage.getAll(new Pageable(0, iterations)));
        final long count = indexes
                .stream()
                .distinct()
                .count();
        Assertions.assertEquals(count, iterations);
    }

    @Test
    @DisplayName("Test when widget has new z-index then all other widgets with the same or bigger indexes are moved one level up")
    public void testUpdate() {
        final InMemoryStorage storage = new InMemoryStorage();
        final InMemoryAtomicOperations operations = new InMemoryAtomicOperations(new ReentrantReadWriteLock(true), storage);
        //populate storage
        operations.create(new MockedWidget(10));
        operations.create(new MockedWidget(11));
        final DbWidget widget = operations.create(new MockedWidget(12));
        //update z index from 12 to 10 should move two other widgets one level up
        operations.update(widget, new ImmutableWidget(1, 1, 10, 1, 1));
        //check that existing widget has new z-index
        Assertions.assertEquals(
                storage.get(new WidgetStorage.SearchById(widget)).getZ(),
                10
        );
        // check that two other indexes were moved one level up
        Assertions.assertEquals(
                Utils.indexes(storage.getAll(new Pageable())),
                List.of(10, 11, 12)
        );


    }

    @Test
    @DisplayName("Test that when first widget doesn't have z-index then FirstWidget is used")
    public void testAddToEmptyStorage() {
        final InMemoryStorage storage = new InMemoryStorage();
        final InMemoryAtomicOperations operations =
                new InMemoryAtomicOperations(
                        new ReentrantReadWriteLock(true),
                        storage
                );
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
        final InMemoryAtomicOperations operations =
                new InMemoryAtomicOperations(
                        new ReentrantReadWriteLock(true),
                        storage
                );
        for (int i = 0; i < 5; i++) {
            operations.create(
                    new MockedWidget(10)
            );
        }
        Assertions.assertEquals(
                Utils.indexes(storage.getAll(new Pageable())),
                List.of(10, 11, 12, 13, 14)
        );
    }
}
