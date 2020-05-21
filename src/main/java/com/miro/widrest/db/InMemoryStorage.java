package com.miro.widrest.db;

import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Pageable;
import com.miro.widrest.domain.Widget;
import com.miro.widrest.domain.impl.DbSavedWidget;
import com.miro.widrest.domain.impl.HigherIndexWidget;
import com.miro.widrest.domain.impl.ImmutableIdentifier;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@AllArgsConstructor
public final class InMemoryStorage implements WidgetStorage {

    /**
     * Pk generator.
     */
    private final AtomicLong serial;

    /**
     * Actual storage.
     */
    private final ConcurrentMap<Identifiable, DbWidget> storage;

    public InMemoryStorage() {
        this.storage = new ConcurrentHashMap<>();
        this.serial = new AtomicLong(0);
    }

    @Override
    public boolean remove(final Identifiable id) {
        return this.storage.remove(id) != null;
    }

    @Override
    public DbWidget add(final Widget widget) {
        return this.storage.computeIfAbsent(
                new ImmutableIdentifier(this.serial.incrementAndGet()),
                identifiable -> new DbSavedWidget(widget, identifiable)
        );
    }

    @Override
    public Iterable<? extends DbWidget> incrementIndexes(final Integer startIndex) {
        //store replaced widgets
        final List<DbWidget> replaced = new ArrayList<>();
        this.storage.replaceAll((identifiable, dbWidget) -> {
            //move this widget higher
            if (dbWidget.getZ() >= startIndex) {
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
        replaced.sort(Comparator.comparing(DbWidget::getZ));
        return replaced;
    }

    @Override
    public DbWidget update(final Widget widget, final Identifiable id) {
        return Optional.ofNullable(
                this.storage.computeIfPresent(
                        id,
                        (identifiable, dbWidget) ->
                                new DbSavedWidget(widget, identifiable)
                )
        ).orElse(DbWidget.empty);
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
    public DbWidget getLastByZIndex() {
        if (this.storage.isEmpty()) {
            return null;
        } else {
            return Collections.min(this.storage.values(), Comparator.comparing(DbWidget::getZ));
        }
    }

    @Override
    public boolean exists(final BiPredicate<Identifiable, DbWidget> predicate) {
        return this.storage.entrySet()
                .stream()
                .anyMatch(entry -> predicate.test(entry.getKey(), entry.getValue()));
    }

    @Override
    public Iterable<? extends DbWidget> getAll(final Pageable pageable) {
        return this.storage
                .values()
                .stream()
                .sorted(Comparator.comparing(DbWidget::getZ))
                .skip(pageable.skip())
                .limit(pageable.getSize())
                .collect(Collectors.toList());
    }
}
