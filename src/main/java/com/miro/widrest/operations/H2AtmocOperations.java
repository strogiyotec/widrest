package com.miro.widrest.operations;

import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Widget;

public final class H2AtmocOperations implements AtomicWidgetOperations{

    @Override
    public DbWidget create(final Widget widget) {
        return null;
    }

    @Override
    public DbWidget get(final Identifiable id) {
        return null;
    }

    @Override
    public DbWidget update(final Identifiable id, final Widget widget) {
        return null;
    }

    @Override
    public Iterable<? extends DbWidget> getAll() {
        return null;
    }

    @Override
    public DbWidget delete(final Identifiable id) {
        return null;
    }
}
