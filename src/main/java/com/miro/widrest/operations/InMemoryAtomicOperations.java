package com.miro.widrest.operations;

import com.miro.widrest.db.WidgetStorage;
import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Widget;
import com.miro.widrest.domain.impl.EmptyZIndexWidget;
import com.miro.widrest.domain.impl.PredefinedZIndexWidget;
import com.miro.widrest.domain.impl.UpdatedZIndexWidget;
import lombok.AllArgsConstructor;

import java.util.concurrent.locks.ReadWriteLock;

@AllArgsConstructor
public final class InMemoryAtomicOperations implements AtomicWidgetOperations {

    private final ReadWriteLock lock;

    private final WidgetStorage storage;

    @Override
    public DbWidget create(final Widget widget) {
        try {
            this.lock.writeLock().lock();
            if (widget.getZ() == null) {
                return new EmptyZIndexWidget(this.storage, widget);
            } else {
                return new PredefinedZIndexWidget(this.storage, widget);
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
            return new UpdatedZIndexWidget(this.storage, widget, id);
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
    public boolean delete(final Identifiable id) {
        try {
            this.lock.readLock().lock();
            return this.storage.remove(id);
        } finally {
            this.lock.readLock().unlock();
        }
    }
}
