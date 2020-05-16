package com.miro.widrest.service;

import com.miro.widrest.db.WidgetStorage;
import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Widget;
import com.miro.widrest.domain.impl.FirstWidget;
import com.miro.widrest.domain.impl.LowerIndexWidget;
import lombok.AllArgsConstructor;

import java.util.Comparator;

@AllArgsConstructor
public final class DefaultWidgetService implements WidgetService {

    private final WidgetStorage storage;

    @Override
    public DbWidget create(final Widget widget) {
        if (widget.getZ() == null) {
            return this.saveEmptyZIndexWidget(widget);
        } else {
            return this.saveWidgetWithIndex(widget);
        }
    }

    @Override
    public DbWidget get(final Identifiable id) {
        return this.storage.get(new WidgetStorage.SearchById(id));
    }

    @Override
    public DbWidget update(final Identifiable id, final Widget widget) {
        return null;
    }

    @Override
    public Iterable<? extends DbWidget> getAll() {
        return this.storage.getAll();
    }

    private DbWidget saveWidgetWithIndex(final Widget widget) {
        synchronized (this) {
            final DbWidget sameZIndexWidget = this.storage.get((identifiable, dbWidget) -> dbWidget.getZ().equals(widget.getZ()));
            // we already have widget with given index
            if (sameZIndexWidget != null) {
                synchronized (this) {
                    this.storage.moveIndexes(widget);
                    return this.storage.add(widget);
                }
            } else {
                return this.storage.add(widget);
            }
        }
    }

    private DbWidget saveEmptyZIndexWidget(final Widget widget) {
        synchronized (this) {
            final DbWidget lowestZIndexWidget = this.storage.getLast(Comparator.comparing(DbWidget::getZ).reversed());
            //storage is not empty
            if (lowestZIndexWidget != null) {
                return this.storage.add(new LowerIndexWidget(widget));
            } else {
                return this.storage.add(new FirstWidget(widget));
            }
        }
    }
}
