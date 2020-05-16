package com.miro.widrest.db;

import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Widget;
import com.miro.widrest.domain.impl.ConstantIdentifiable;
import com.miro.widrest.domain.impl.DbSavedWidget;
import com.miro.widrest.domain.impl.HigherIndexWidget;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiPredicate;

@AllArgsConstructor
public final class InMemoryStorage implements WidgetStorage {

    private final AtomicLong serial;

    private final ConcurrentMap<Identifiable, DbWidget> storage;

    public InMemoryStorage() {
        this.storage = new ConcurrentHashMap<>();
        this.serial = new AtomicLong(0);
    }

    @Override
    public DbWidget add(final Widget widget) {
        return this.storage.computeIfAbsent(
                new ConstantIdentifiable(this.serial.incrementAndGet()),
                identifiable -> new DbSavedWidget(widget, identifiable)
        );
    }

    @Override
    public Iterable<? extends DbWidget> moveIndexes(final Widget lowestIndexWidget) {
        //store replaced widgets
        final List<DbWidget> replaced = new ArrayList<>();
        this.storage.replaceAll((identifiable, dbWidget) -> {
            //move this widget higher
            if (dbWidget.getZ() >= lowestIndexWidget.getZ()) {
                replaced.add(dbWidget);
                return new DbSavedWidget(
                        new HigherIndexWidget(
                                dbWidget
                        ),
                        identifiable
                );
            }
            return dbWidget;
        });
        return replaced;
    }

    @Override
    public DbWidget update(final Widget widget, final Identifiable id) {
        return this.storage.computeIfPresent(
                id,
                (identifiable, dbWidget) ->
                        new DbSavedWidget(widget, identifiable)
        );
    }

    @Override
    public DbWidget get(final BiPredicate<Identifiable, DbWidget> predicate) {
        return this.storage.entrySet()
                .stream()
                .filter(entry -> predicate.test(entry.getKey(), entry.getValue()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(DbWidget.empty);
    }

    @Override
    public DbWidget getLast(final Comparator<DbWidget> comparator) {
        if (this.storage.isEmpty()) {
            return null;
        } else {
            return Collections.max(this.storage.values(), comparator);
        }
    }

    @Override
    public boolean exists(final BiPredicate<Identifiable, DbWidget> predicate) {
        return this.storage.entrySet()
                .stream()
                .anyMatch(entry -> predicate.test(entry.getKey(), entry.getValue()));
    }

    @Override
    public Iterable<? extends DbWidget> getAll() {
        return this.storage.values();
    }
}
