package com.miro.widrest.db;

import com.miro.widrest.Utils;
import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.impl.ImmutableWidget;
import com.miro.widrest.domain.impl.MockedIdentifier;
import com.miro.widrest.domain.impl.MockedWidget;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

final class ImMemoryStorageTestCase {

    @Test
    @DisplayName("Test that new widget was created")
    public void testInsertAndGet() {
        final InMemoryStorage inMemoryStorage = new InMemoryStorage();
        //insert
        final DbWidget widget = inMemoryStorage.add(new MockedWidget());
        //get
        Assertions.assertEquals(
                widget,
                inMemoryStorage.get(
                        new WidgetStorage.SearchById(widget)
                )
        );
        Assertions.assertTrue(
                inMemoryStorage.exists(
                        new WidgetStorage.SearchById(widget)
                )
        );
    }

    @Test
    @DisplayName("Test that storage removes element")
    public void testDelete() {
        final InMemoryStorage storage = new InMemoryStorage();
        final DbWidget widget = storage.add(new MockedWidget());
        storage.remove(widget);
        //doesn't exist after deletion
        Assertions.assertFalse(storage.exists(new WidgetStorage.SearchById(widget)));
    }

    @Test
    @DisplayName("Test that returns Empty widget if didn't delete anything")
    public void testDeleteNonExisting() {
        final InMemoryStorage storage = new InMemoryStorage();
        //when doesn't exist returns empty
        Assertions.assertEquals(
                storage.remove(new MockedIdentifier()),
                DbWidget.empty

        );
    }

    @Test
    @DisplayName("Test that widgets are sorted from lowest z-index to highest")
    public void testGetAll() {
        final InMemoryStorage storage = new InMemoryStorage();
        storage.add(new ImmutableWidget(1, 1, 30, 1, 1));
        storage.add(new ImmutableWidget(1, 1, 10, 1, 1));
        storage.add(new ImmutableWidget(1, 1, 20, 1, 1));
        final List<DbWidget> widgets = Lists.newArrayList(storage.getAll());

        Assertions.assertTrue(
                widgets.get(0).getZ() < widgets.get(1).getZ()
        );
        Assertions.assertTrue(
                widgets.get(1).getZ() < widgets.get(2).getZ()
        );
    }

    @Test
    @DisplayName("Test that widget is updated")
    public void testUpdate() {
        final InMemoryStorage storage = new InMemoryStorage();
        final DbWidget widget = storage.add(new MockedWidget());
        final DbWidget updated = storage.update(
                new MockedWidget(),
                widget
        );
        //Id is immutable
        Assertions.assertEquals(widget.getId(), updated.getId());
        Assertions.assertEquals(
                storage.get(new WidgetStorage.SearchById(updated)),
                updated
        );
    }

    @Test
    @DisplayName("Test that return empty widget if didn't delete anything")
    public void testUpdateNonExisting() {
        final InMemoryStorage storage = new InMemoryStorage();
        //when doesn't exist returns empty
        Assertions.assertEquals(
                storage.update(
                        new MockedWidget(),
                        new MockedIdentifier()
                ),
                DbWidget.empty
        );
    }

    @Test
    @DisplayName("Test that widgets with same or higher z indexes were moved one level up")
    public void testMoveIndexes() {
        final int zIndex = 20;
        final InMemoryStorage storage = new InMemoryStorage();
        storage.add(new ImmutableWidget(1, 1, 30, 1, 1));
        storage.add(new ImmutableWidget(1, 1, 10, 1, 1));
        storage.add(new ImmutableWidget(1, 1, zIndex, 1, 1));
        //z-indexes before update are 10,20,30
        Assertions.assertEquals(
                Utils.indexes(storage.getAll()),
                List.of(10, 20, 30)
        );
        //only two widgets were modified
        Assertions.assertEquals(
                Utils.indexes(storage.moveIndexes(new ImmutableWidget(1, 1, zIndex, 1, 1))),
                List.of(20, 30)
        );
        Assertions.assertEquals(
                Utils.indexes(storage.getAll()),
                List.of(10, 21, 31)
        );
    }

}
