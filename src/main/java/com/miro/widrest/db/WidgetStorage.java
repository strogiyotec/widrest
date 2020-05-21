package com.miro.widrest.db;

import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Pageable;
import com.miro.widrest.domain.Widget;
import lombok.AllArgsConstructor;

import java.util.function.BiPredicate;

public interface WidgetStorage {

    /**
     * Remove widget with given id.
     *
     * @param id Id
     * @return True if was removed
     */
    boolean remove(Identifiable id);

    DbWidget add(Widget widget);

    DbWidget update(Widget widget, Identifiable id);

    DbWidget get(BiPredicate<Identifiable, DbWidget> predicate);

    DbWidget getLastByZIndex();

    Iterable<? extends DbWidget> getAll(final Pageable pageable);

    Iterable<? extends DbWidget> incrementIndexes(Integer startIndex);

    boolean exists(BiPredicate<Identifiable, DbWidget> predicate);

    /**
     * Predicate that searches widget by id.
     */
    @AllArgsConstructor
    final class SearchById implements BiPredicate<Identifiable, DbWidget> {

        private final Identifiable identifiable;

        @Override
        public boolean test(final Identifiable identifiable, final DbWidget dbWidget) {
            return this.identifiable.equals(identifiable);
        }
    }

    /**
     * Predicates that searches widget by z-index.
     */
    @AllArgsConstructor
    final class SearchByZIndex implements BiPredicate<Identifiable, DbWidget> {

        private final Integer zIndex;

        @Override
        public boolean test(final Identifiable identifiable, final DbWidget dbWidget) {
            return dbWidget.getZ().equals(this.zIndex);
        }
    }
}
