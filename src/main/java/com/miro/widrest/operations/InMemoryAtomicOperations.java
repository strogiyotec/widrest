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
            final DbWidget withTheSameId = this.storage.get(new WidgetStorage.SearchById(id));
            //non existing id
            if (withTheSameId == DbWidget.empty) {
                return DbWidget.empty;
            }
            final DbWidget withTheSameIndex = this.storage.get(new WidgetStorage.SearchByZIndex(widget.getZ()));
            if (withTheSameIndex != DbWidget.empty) {
                return this.updateWidgetWithExistingIndex(id, widget, withTheSameIndex);
            } else {
                //new z-index doesn't exist in storage then just save it
                return this.storage.update(widget, id);
            }
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

    /**
     * Update widget whose index exists in storage.
     *
     * @param id               Id of Widget
     * @param widget           New Data
     * @param withTheSameIndex Widget whose index is the same as index from given widget
     * @return Updated Widget
     */
    private DbWidget updateWidgetWithExistingIndex(
            final Identifiable id,
            final Widget widget,
            final DbWidget withTheSameIndex
    ) {
        //if db widget with existing index is the same as to be updated
        if (withTheSameIndex.equals(id)) {
            return this.storage.update(widget, id);
        } else {
            //else before updating z-index move all widgets one level up to free one space
            return new UpdatedZIndexWidget(this.storage, widget, withTheSameIndex, id);
        }
    }
}
