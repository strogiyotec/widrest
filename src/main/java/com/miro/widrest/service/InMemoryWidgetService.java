package com.miro.widrest.service;

import com.miro.widrest.db.WidgetStorage;
import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Widget;
import com.miro.widrest.domain.impl.FirstWidget;
import com.miro.widrest.domain.impl.LowerIndexWidget;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.concurrent.locks.ReadWriteLock;

@AllArgsConstructor
public final class InMemoryWidgetService implements WidgetService {

    private final ReadWriteLock lock;

    private final WidgetStorage storage;

    @Override
    public DbWidget create(final Widget widget) {
        try {
            this.lock.writeLock().lock();
            if (widget.getZ() == null) {
                return this.saveEmptyZIndexWidget(widget);
            } else {
                return this.saveWidgetWithIndex(widget);
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public DbWidget get(final Identifiable id) {
        try {
            this.lock.readLock().lock();
            return this.storage.get(new WidgetStorage.SearchById(id));
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public DbWidget update(final Identifiable id, final Widget widget) {
        try {
            this.lock.writeLock().lock();
            return this.storage.update(widget, id);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public Iterable<? extends DbWidget> getAll() {
        try {
            this.lock.readLock().lock();
            return this.storage.getAll();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public DbWidget delete(final Identifiable id) {
        try {
            this.lock.readLock().lock();
            return this.storage.remove(id);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    private DbWidget saveWidgetWithIndex(final Widget widget) {
        this.lock.writeLock().lock();
        final DbWidget sameZIndexWidget = this.storage.get((identifiable, dbWidget) -> dbWidget.getZ().equals(widget.getZ()));
        // we already have widget with given index
        if (sameZIndexWidget != null) {
            this.storage.moveIndexes(widget);
            return this.storage.add(widget);
        } else {
            return this.storage.add(widget);
        }
    }

    private DbWidget saveEmptyZIndexWidget(final Widget widget) {
        final DbWidget lowestZIndexWidget = this.storage.getLast(Comparator.comparing(DbWidget::getZ).reversed());
        //storage is not empty
        if (lowestZIndexWidget != null) {
            return this.storage.add(new LowerIndexWidget(widget));
        } else {
            return this.storage.add(new FirstWidget(widget));
        }
    }
}
