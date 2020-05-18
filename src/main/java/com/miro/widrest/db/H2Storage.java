package com.miro.widrest.db;

import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Widget;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Comparator;
import java.util.function.BiPredicate;

@AllArgsConstructor
public final class H2Storage implements WidgetStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean remove(final Identifiable id) {
        return this.jdbcTemplate.update(
                "DELETE from widgets where id = ?",
                id.getId()
        ) != 0;
    }

    @Override
    public DbWidget add(final Widget widget) {
        return null;
    }

    @Override
    public DbWidget update(final Widget widget, final Identifiable id) {
        return null;
    }

    @Override
    public DbWidget get(final BiPredicate<Identifiable, DbWidget> predicate) {
        return null;
    }

    @Override
    public DbWidget getLast(final boolean lowestZIndex) {
        return null;
    }

    @Override
    public Iterable<? extends DbWidget> getAll() {
        return null;
    }

    @Override
    public Iterable<? extends DbWidget> moveIndexes(final Widget lowestIndexWidget) {
        return null;
    }

    @Override
    public boolean exists(final BiPredicate<Identifiable, DbWidget> predicate) {
        return false;
    }

    static class Hello implements Comparator<DbWidget> {

        @Override
        public int compare(final DbWidget o1, final DbWidget o2) {
            return 0;
        }
    }
}
