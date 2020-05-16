package com.miro.widrest.db;

import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Widget;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.function.BiPredicate;

public interface WidgetStorage {

    DbWidget add(Widget widget);

    Iterable<? extends DbWidget> moveIndexes(Widget lowestIndexWidget);

    DbWidget update(Widget widget, Identifiable id);

    DbWidget get(BiPredicate<Identifiable, DbWidget> predicate);

    DbWidget getLast(Comparator<DbWidget> comparator);

    boolean exists(BiPredicate<Identifiable, DbWidget> predicate);

    Iterable<? extends DbWidget> getAll();

    @AllArgsConstructor
    final class SearchById implements BiPredicate<Identifiable, DbWidget> {

        private final Identifiable identifiable;

        @Override
        public boolean test(final Identifiable identifiable, final DbWidget dbWidget) {
            return this.identifiable.equals(identifiable);
        }
    }
}
