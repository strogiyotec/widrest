package com.miro.widrest.db;

import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.impl.ImmutableWidget;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class ImMemoryStorageTestCase {

    @Test
    @DisplayName("Test that new widget was created")
    public void testInsertAndGet() {
        final InMemoryStorage inMemoryStorage = new InMemoryStorage();
        final DbWidget widget = inMemoryStorage.add(
                new ImmutableWidget(
                        1,
                        1,
                        2,
                        10,
                        10
                )
        );
        Assertions.assertEquals(
                widget,
                inMemoryStorage.get(new WidgetStorage.SearchById(widget))
        );
        Assertions.assertTrue(
                inMemoryStorage.exists(new WidgetStorage.SearchById(widget))
        );
    }
}
